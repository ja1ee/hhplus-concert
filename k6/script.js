
import { signToken } from './api/signToken.js';
import { reserveSeat } from './api/reserveSeat.js';
import { payForSeat } from './api/payForSeat.js';
import { Counter } from 'k6/metrics';

export const options = {
    scenarios: {
        generate_new_vus: {
            executor: 'constant-arrival-rate',  // VU가 한 번 실행 후 종료
            rate: 50, // 초당 50개의 VU 생성
            timeUnit: '1s',
            duration: '10s',
            preAllocatedVUs: 50,
        },
    },
};

const successCount = new Counter('Z_success_seat_reservation');
const failureCount = new Counter('Z_fail_seat_reservation');

export default async () => {
    const userId = __VU;  // VU 번호를 userId로 사용

    // 1. 대기열 발급
    const queueToken = await signToken(userId);

    // 2. 좌석 예약
    const reserveResponse = await reserveSeat(userId);
    if (reserveResponse.status !== 200) return failureCount.add(1); // 요청 실패

    const responseBody = JSON.parse(reserveResponse.body);
    if (!responseBody.data){
        return successCount.add(1); // 이미 예약된 좌석일 경우
    }

    // 3. 결제 요청
    const paymentResponse = await payForSeat(responseBody.data);
    if (paymentResponse.status === 200) {
        successCount.add(1);
    } else {
        failureCount.add(1);
    }
}
