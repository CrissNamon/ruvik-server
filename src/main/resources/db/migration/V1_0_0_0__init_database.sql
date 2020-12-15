CREATE TABLE IF NOT EXISTS role(
    id          serial PRIMARY KEY,
    name        varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS appuser(
    id              serial PRIMARY KEY,
    login           varchar(16) NOT NULL,
    password        varchar(255) NOT NULL,
    token           varchar(255) NOT NULL,
    identity_key    varchar(255) NOT NULL,
    database_key    varchar(255) NOT NULL,
    role            serial NOT NULL
);

ALTER TABLE appuser ADD CONSTRAINT fk_user_role FOREIGN KEY (role) REFERENCES role(id) ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS conversation(
    id              serial PRIMARY KEY,
    session_key     varchar(255) NOT NULL,
    receiver_id     serial NOT NULL,
    sender_id       serial NOT NULL,
    status          varchar(16) NOT NULL DEFAULT 'PENDING',
    onetime_key     varchar(255)
);

ALTER TABLE conversation ADD CONSTRAINT fk_user_receiver FOREIGN KEY (receiver_id) REFERENCES appuser(id) ON UPDATE CASCADE;
ALTER TABLE conversation ADD CONSTRAINT fk_user_sender FOREIGN KEY (sender_id) REFERENCES appuser(id) ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS message(
    id                  serial PRIMARY KEY,
    conversation_id     serial NOT NULL,
    text                text NOT NULL,
    sent_time           timestamptz NOT NULL
);

ALTER TABLE message ADD CONSTRAINT fk_conversation_message FOREIGN KEY (conversation_id) REFERENCES conversation(id) ON UPDATE CASCADE ON DELETE CASCADE;