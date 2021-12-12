ALTER TABLE scheduled_pipelines
    ADD COLUMN "pipeline_status" VARCHAR(40) DEFAULT '' NOT NULL;

ALTER TABLE scheduled_pipelines
    ADD COLUMN "actual_end" TIMESTAMP;