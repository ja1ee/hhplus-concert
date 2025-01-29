package kr.hhplus.be.server.api.common.aop.lock;

import kr.hhplus.be.server.api.common.exception.CustomException;
import kr.hhplus.be.server.api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
@Order(0) //트랜잭션보다 우선 실행
public class RedisLockAop {
    private static final String REDIS_LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;
    private final AopForTransaction transaction;

    @Around("@annotation(kr.hhplus.be.server.api.common.aop.lock.RedisLock)")
    public Object getLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock annotation = method.getAnnotation(RedisLock.class);

        String key = REDIS_LOCK_PREFIX + annotation.prefix() + joinPoint.getArgs()[0];
        RLock rLock = redissonClient.getLock(key);

        if (!rLock.tryLock()) {
            throw new CustomException(ErrorCode.FAILED_GET_LOCK);
        }

        try {
            return transaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new CustomException(ErrorCode.INTERRUPT_OCCURRED);
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already Unlock serviceName = {}, Key = {}", method.getName(), REDIS_LOCK_PREFIX);
            }
        }
    }
}