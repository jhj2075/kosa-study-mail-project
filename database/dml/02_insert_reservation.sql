ALTER SESSION SET CURRENT_SCHEMA = MAIL_APP;

INSERT INTO EMAIL_RESERVATION (
    USER_ID,
    SUBJECT,
    CONTENT,
    RECEIVER_EMAIL,
    RESERVE_DATETIME,
    STATUS
) VALUES (
    1,
    'RabbitMQ 01:50 scheduler test',
    'This reservation should be picked up by scheduler-service at 01:50.',
    'receiver@example.com',
    TO_TIMESTAMP('2026-06-22 01:50:00', 'YYYY-MM-DD HH24:MI:SS'),
    'WAITING'
);

COMMIT;
