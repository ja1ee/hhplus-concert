
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS balance_history;
DROP TABLE IF EXISTS concert;
DROP TABLE IF EXISTS concert_schedule;
DROP TABLE IF EXISTS concert_seat;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_outbox;
DROP TABLE IF EXISTS user;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS user (
                                            id bigint PRIMARY KEY AUTO_INCREMENT,
                                            balance decimal not null
);

CREATE TABLE IF NOT EXISTS balance_history (
                                            id bigint PRIMARY KEY AUTO_INCREMENT,
                                            amount decimal(38,2) not null,
                                            changed_at datetime(6) not null,
                                            type tinyint not null,
                                            user_id bigint not null
    );

CREATE TABLE IF NOT EXISTS concert (
                                            id bigint PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE IF NOT EXISTS concert_schedule (
                                            id bigint PRIMARY KEY AUTO_INCREMENT,
                                            concert_date date not null,
                                            concert_id bigint not null,
                                            is_open bit not null default 1
);
ALTER TABLE concert_schedule ADD INDEX idx_is_open_concert_date (concert_date, is_open);

CREATE TABLE IF NOT EXISTS concert_seat (
                                            id bigint PRIMARY KEY AUTO_INCREMENT,
                                            is_reserved bit default false,
                                            price decimal(38,2) not null,
                                            schedule_id bigint not null,
                                            seat_no int not null,
                                            version bigint not null default 0
);

CREATE TABLE IF NOT EXISTS reservation (
                                           id bigint PRIMARY KEY AUTO_INCREMENT,
                                           concert_date date not null,
                                           concert_id bigint not null,
                                           expired_at datetime(6),
                                           final_price decimal(38,2) not null,
                                           is_reserved bit not null,
                                           seat_id bigint not null,
                                           seat_no int not null,
                                           user_id bigint not null
);

-- ALTER TABLE reservation MODIFY COLUMN expired_at DATETIME NULL

CREATE TABLE IF NOT EXISTS reservation_outbox (
                                          id bigint PRIMARY KEY AUTO_INCREMENT,
                                          created_at datetime(6) not null,
                                          event_type varchar(255) not null,
                                          payload varchar(255) not null,
                                          published bit not null
);
