ALTER TABLE appuser DROP COLUMN identity_key;
ALTER TABLE appuser ADD COLUMN identity_key_a varchar(255) NOT NULL;
ALTER TABLE appuser ADD COLUMN identity_key_b varchar(255) NOT NULL;
