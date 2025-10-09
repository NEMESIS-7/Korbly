
CREATE TABLE conditions_precedent
(
    cp_id               UUID NOT NULL,
    sheet_id            UUID,
    title               VARCHAR(255),
    description         TEXT,
    required            BOOLEAN,
    status              VARCHAR(255),
    evidence_file_key   VARCHAR(255),
    note                TEXT,
    approved_by_user_id UUID,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    waiver_reason       TEXT,
    delete_yn           VARCHAR(255),
    CONSTRAINT pk_conditionsprecedent PRIMARY KEY (cp_id)
);

CREATE TABLE term_sheet
(
    term_sheet_id          UUID NOT NULL,
    deal_id                UUID,
    tranche_id             UUID,
    sme_id                 UUID,
    loan_amount            DECIMAL,
    interest_rate          DOUBLE PRECISION,
    maturity_date          date,
    amortization_structure VARCHAR(255),
    prepayment_option      BOOLEAN,
    offering_period        JSONB,
    guarantees             JSONB,
    collateral             JSONB,
    seniority              VARCHAR(255),
    covenants              JSONB,
    events_of_default      JSONB,
    default_rate           DOUBLE PRECISION,
    grace_periods          JSONB,
    governing_law          VARCHAR(255),
    sheet_status           VARCHAR(255),
    sheet_version          INTEGER,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    signed_at              TIMESTAMP WITHOUT TIME ZONE,
    app_user_id            UUID,
    delete_yn              VARCHAR(255),
    parent_id              UUID,
    is_latest              BOOLEAN,
    CONSTRAINT pk_termsheet PRIMARY KEY (term_sheet_id)
);

ALTER TABLE conditions_precedent
    ADD CONSTRAINT uc_conditionsprecedent_approved_by_user UNIQUE (approved_by_user_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT uc_termsheet_app_user UNIQUE (app_user_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT uc_termsheet_deal UNIQUE (deal_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT uc_termsheet_sme UNIQUE (sme_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT uc_termsheet_tranche UNIQUE (tranche_id);

ALTER TABLE conditions_precedent
    ADD CONSTRAINT FK_CONDITIONSPRECEDENT_ON_APPROVED_BY_USER FOREIGN KEY (approved_by_user_id) REFERENCES app_user (user_id);

ALTER TABLE conditions_precedent
    ADD CONSTRAINT FK_CONDITIONSPRECEDENT_ON_SHEET FOREIGN KEY (sheet_id) REFERENCES term_sheet (term_sheet_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT FK_TERMSHEET_ON_APP_USER FOREIGN KEY (app_user_id) REFERENCES app_user (user_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT FK_TERMSHEET_ON_DEAL FOREIGN KEY (deal_id) REFERENCES deal (deal_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT FK_TERMSHEET_ON_PARENT FOREIGN KEY (parent_id) REFERENCES term_sheet (term_sheet_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT FK_TERMSHEET_ON_SME FOREIGN KEY (sme_id) REFERENCES sme (sme_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT FK_TERMSHEET_ON_TRANCHE FOREIGN KEY (tranche_id) REFERENCES tranche (tranche_id);
