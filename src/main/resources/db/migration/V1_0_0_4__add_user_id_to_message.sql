ALTER TABLE message ADD COLUMN user_id serial;
ALTER TABLE message ADD CONSTRAINT fk_message_user FOREIGN KEY (user_id) REFERENCES appuser(id) ON UPDATE CASCADE;