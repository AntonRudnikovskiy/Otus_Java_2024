create sequence phone_seq start with 1 increment by 1;

CREATE TABLE phone
(
    id BIGINT PRIMARY KEY,
    number VARCHAR(64),
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES Client(id)
);