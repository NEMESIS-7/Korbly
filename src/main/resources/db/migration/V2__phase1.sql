ALTER TABLE allocation
    DROP CONSTRAINT fk5n4cy2l3oix4k9ituhx4rs021;

ALTER TABLE allocation
    DROP CONSTRAINT fkcx7fbdg1512i7hp5vb5nu27qk;

CREATE TABLE audit_log
(
    log_id      UUID NOT NULL,
    user_id     UUID,
    actor_id    UUID,
    actor_role  SMALLINT,
    entity_type VARCHAR(255),
    action      VARCHAR(255),
    timestamp   TIMESTAMP WITHOUT TIME ZONE,
    ip_address  VARCHAR(255),
    request_id  VARCHAR(255),
    entity_id   UUID,
    created_on  TIMESTAMP WITHOUT TIME ZONE,
    delete_yn   VARCHAR(255),
    CONSTRAINT pk_auditlog PRIMARY KEY (log_id)
);

CREATE TABLE event_publication_archive
(
    id              UUID NOT NULL,
    completion_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_event_publication_archive PRIMARY KEY (id)
);

CREATE TABLE regulator
(
    regulator_id           UUID         NOT NULL,
    regulator_name         VARCHAR(255) NOT NULL,
    regulator_jurisdiction VARCHAR(255) NOT NULL,
    regulator_type         VARCHAR(255),
    regulator_status       VARCHAR(255),
    user_id                UUID,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    delete_yn              VARCHAR(255),
    CONSTRAINT pk_regulator PRIMARY KEY (regulator_id)
);

ALTER TABLE allocation
    ADD confirmed_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE investor
    ADD investor_type VARCHAR(255);

ALTER TABLE audit_log
    ADD CONSTRAINT uc_auditlog_user UNIQUE (user_id);

ALTER TABLE regulator
    ADD CONSTRAINT uc_regulator_regulatorname UNIQUE (regulator_name);

ALTER TABLE regulator
    ADD CONSTRAINT uc_regulator_user UNIQUE (user_id);


ALTER TABLE audit_log
    ADD CONSTRAINT FK_AUDITLOG_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (user_id);

ALTER TABLE regulator
    ADD CONSTRAINT FK_REGULATOR_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (user_id);

ALTER TABLE event_publication
    DROP COLUMN event_type;

ALTER TABLE event_publication
    DROP COLUMN listener_id;

ALTER TABLE event_publication
    DROP COLUMN publication_date;

ALTER TABLE event_publication
    DROP COLUMN serialized_event;

ALTER TABLE allocation
    DROP COLUMN investor_id_investor_id;

ALTER TABLE allocation
    DROP COLUMN tranche_id_tranche_id;

ALTER TABLE allocation
    ALTER COLUMN amount TYPE DECIMAL USING (amount::DECIMAL);

ALTER TABLE tranche
    ALTER COLUMN amount TYPE DECIMAL(10, 2) USING (amount::DECIMAL(10, 2));

ALTER TABLE sme
    ALTER COLUMN annual_revenue TYPE DECIMAL USING (annual_revenue::DECIMAL);

ALTER TABLE investor
    ALTER COLUMN assets_under_management TYPE DECIMAL USING (assets_under_management::DECIMAL);

ALTER TABLE deal
    ALTER COLUMN deal_description TYPE VARCHAR(300) USING (deal_description::VARCHAR(300));

ALTER TABLE deal
    ALTER COLUMN deal_title TYPE VARCHAR(150) USING (deal_title::VARCHAR(150));

ALTER TABLE tranche
    ALTER COLUMN interest_rate TYPE DECIMAL(5, 2) USING (interest_rate::DECIMAL(5, 2));

ALTER TABLE investor
    ALTER COLUMN minimum_investment TYPE DECIMAL USING (minimum_investment::DECIMAL);

ALTER TABLE sme
    ALTER COLUMN monthly_revenue TYPE DECIMAL USING (monthly_revenue::DECIMAL);

ALTER TABLE credit_memo
    ALTER COLUMN ohlson_score TYPE DECIMAL USING (ohlson_score::DECIMAL);

ALTER TABLE sme
    ALTER COLUMN requested_amount TYPE DECIMAL USING (requested_amount::DECIMAL);

ALTER TABLE deal
    ALTER COLUMN total_amount TYPE DECIMAL(15, 2) USING (total_amount::DECIMAL(15, 2));