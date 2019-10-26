alter TABLE docker.user ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;
alter TABLE docker.controller ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;
alter TABLE docker.user_profile ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;
alter TABLE docker.permission ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;
alter TABLE docker.role ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;
