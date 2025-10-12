CREATE TABLE valuation_assumption
(
    valuation_id                       UUID                        NOT NULL,
    tranche_id                         UUID                        NOT NULL,
    as_of                              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    schedule_start_date                date                        NOT NULL,
    principal                          DECIMAL(20, 2)              NOT NULL,
    annual_rate                        DECIMAL(10, 6)              NOT NULL,
    tenor_months                       INTEGER                     NOT NULL,
    amortization_structure             VARCHAR(255)                NOT NULL,
    grace_principal_months             INTEGER                     NOT NULL,
    grace_interest_months              INTEGER                     NOT NULL,
    fee_upfront_pct                    DECIMAL(10, 6)              NOT NULL,
    fee_servicing_bps                  DECIMAL(10, 4)              NOT NULL,
    fee_exit_pct                       DECIMAL(10, 6)              NOT NULL,
    annual_discount_rate               DECIMAL(10, 6)              NOT NULL,
    currency                           VARCHAR(255)                NOT NULL,
    source                             VARCHAR(255)                NOT NULL,
    scenario_label                     VARCHAR(64),
    balloon_percent_of_original        DECIMAL(10, 6),
    balloon_amount_at_maturity         DECIMAL(20, 2),
    fixed_monthly_payment              DECIMAL(20, 2),
    negative_amortization_months       INTEGER,
    min_payment_percent_of_interest    DECIMAL(10, 6),
    min_payment_absolute_amount        DECIMAL(20, 2),
    negative_amortization_cap_multiple DECIMAL(10, 6),
    created_by                         UUID                        NOT NULL,
    created_at                         TIMESTAMP WITHOUT TIME ZONE,
    updated_at                         TIMESTAMP WITHOUT TIME ZONE,
    delete_yn                          VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_valuationassumption PRIMARY KEY (valuation_id)
);

CREATE TABLE valuation_result
(
    valuation_result_id            UUID NOT NULL,
    valuation_assumption_id        UUID,
    net_present_value              DECIMAL,
    internal_rate_of_return_annual DOUBLE PRECISION,
    cash_on_cash_multiple          DOUBLE PRECISION,
    payback_period_in_months       INTEGER,
    cashflow_schedule              JSONB,
    computed_at                    TIMESTAMP WITHOUT TIME ZONE,
    computed_by_user_id            UUID,
    CONSTRAINT pk_valuationresult PRIMARY KEY (valuation_result_id)
);

ALTER TABLE term_sheet
    ADD amended_by UUID;

ALTER TABLE conditions_precedent
    ADD code VARCHAR(255);

ALTER TABLE conditions_precedent
    ADD updated_by_user_id UUID;

ALTER TABLE conditions_precedent
    ADD version BIGINT;

ALTER TABLE conditions_precedent
    ADD waived_by_user_id UUID;

ALTER TABLE investor
    ADD CONSTRAINT uc_investor_dateestablished UNIQUE (date_established);

ALTER TABLE term_sheet
    ADD CONSTRAINT uc_termsheet_amended_by UNIQUE (amended_by);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT uc_valuationassumption_created_by UNIQUE (created_by);

CREATE INDEX idx_valuation_asof ON valuation_assumption (as_of);

CREATE INDEX idx_valuation_created_at ON valuation_assumption (created_at);

ALTER TABLE conditions_precedent
    ADD CONSTRAINT FK_CONDITIONSPRECEDENT_ON_UPDATED_BY_USER FOREIGN KEY (updated_by_user_id) REFERENCES app_user (user_id);

ALTER TABLE conditions_precedent
    ADD CONSTRAINT FK_CONDITIONSPRECEDENT_ON_WAIVED_BY_USER FOREIGN KEY (waived_by_user_id) REFERENCES app_user (user_id);

ALTER TABLE term_sheet
    ADD CONSTRAINT FK_TERMSHEET_ON_AMENDED_BY FOREIGN KEY (amended_by) REFERENCES app_user (user_id);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT FK_VALUATIONASSUMPTION_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES app_user (user_id);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT FK_VALUATIONASSUMPTION_ON_TRANCHE FOREIGN KEY (tranche_id) REFERENCES tranche (tranche_id);

CREATE INDEX idx_valuation_tranche ON valuation_assumption (tranche_id);

ALTER TABLE event_publication
    DROP COLUMN event_type;

ALTER TABLE event_publication
    DROP COLUMN listener_id;

ALTER TABLE event_publication
    DROP COLUMN publication_date;

ALTER TABLE event_publication
    DROP COLUMN serialized_event;

ALTER TABLE request_log
    DROP COLUMN query_params;

ALTER TABLE allocation
    ALTER COLUMN amount TYPE DECIMAL USING (amount::DECIMAL);

ALTER TABLE tranche
    ALTER COLUMN amount TYPE DECIMAL(12, 2) USING (amount::DECIMAL(12, 2));

ALTER TABLE sme
    ALTER COLUMN annual_revenue TYPE DECIMAL USING (annual_revenue::DECIMAL);

ALTER TABLE investor
    ALTER COLUMN assets_under_management TYPE DECIMAL USING (assets_under_management::DECIMAL);

ALTER TABLE deal
    ALTER COLUMN deal_description TYPE VARCHAR(300) USING (deal_description::VARCHAR(300));

ALTER TABLE deal
    ALTER COLUMN deal_title TYPE VARCHAR(150) USING (deal_title::VARCHAR(150));

ALTER TABLE tranche
    ALTER COLUMN interest_rate TYPE DECIMAL(12, 2) USING (interest_rate::DECIMAL(12, 2));

ALTER TABLE term_sheet
    ALTER COLUMN loan_amount TYPE DECIMAL USING (loan_amount::DECIMAL);

ALTER TABLE investor
    ALTER COLUMN minimum_investment TYPE DECIMAL USING (minimum_investment::DECIMAL);

ALTER TABLE sme
    ALTER COLUMN monthly_revenue TYPE DECIMAL USING (monthly_revenue::DECIMAL);

ALTER TABLE credit_memo
    ALTER COLUMN ohlson_score TYPE DECIMAL USING (ohlson_score::DECIMAL);

ALTER TABLE sme
    ALTER COLUMN requested_amount TYPE DECIMAL USING (requested_amount::DECIMAL);

ALTER TABLE deal
    ALTER COLUMN total_amount TYPE DECIMAL(12, 2) USING (total_amount::DECIMAL(12, 2));