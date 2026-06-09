# Database Scripts

This folder keeps the shared database scripts for the study project.

## Folder Layout

- `ddl/01_schema.sql`: Oracle DDL for users, email reservations, queue publish records, and send logs.
- `dml/01_seed.sql`: Minimal seed data for local development.

## Service Ownership

- `reservation-service`: reads and writes `USERS`, reads and writes `EMAIL_RESERVATION`.
- `scheduler-service`: finds due `EMAIL_RESERVATION` rows, publishes `{"mailId": <EMAIL_ID>}` to RabbitMQ, writes `EMAIL_QUEUE`, and changes status to `QUEUED`.
- `mail-worker-service`: consumes RabbitMQ messages, reads `EMAIL_RESERVATION`, updates status to `SENDING`, `SENT`, or `FAILED`, and writes `EMAIL_SEND_LOG`.

RabbitMQ stores and delivers mail job messages. It does not check reservation time and it does not send email.
