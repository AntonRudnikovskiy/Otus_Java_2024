create sequence address_seq start with 1 increment by 1;

CREATE TABLE address
(
    id BIGINT PRIMARY KEY,
    street VARCHAR(64),
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES Client(id)
);