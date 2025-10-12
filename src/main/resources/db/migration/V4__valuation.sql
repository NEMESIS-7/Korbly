CREATE TABLE valuation_assumption
(
    valuation_id           UUID                        NOT NULL,
    tranche_id             UUID,
    as_of                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    principal              DECIMAL                     NOT NULL,
    annual_rate            DOUBLE PRECISION            NOT NULL,
    tenor_months           INTEGER                     NOT NULL,
    amortization_structure VARCHAR(255)                NOT NULL,
    grace_principal_months INTEGER                     NOT NULL,
    grace_interest_months  INTEGER                     NOT NULL,
    fee_upfront_pct        DECIMAL                     NOT NULL,
    fee_servicing_bps      DECIMAL                     NOT NULL,
    fee_exit_pct           DECIMAL                     NOT NULL,
    annual_discount_rate   DECIMAL                     NOT NULL,
    currency               VARCHAR(255),
    source                 VARCHAR(255),
    created_by             UUID,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    delete_yn              VARCHAR(255),
    CONSTRAINT pk_valuationassumption PRIMARY KEY (valuation_id)
);

CREATE TABLE valuation_result
(
    result_id            UUID    NOT NULL,
    assumption_id        UUID,
    net_present_value    DECIMAL,
    internal_rate_return DECIMAL,
    cash_on_cash         DECIMAL,
    pay_back_month       INTEGER NOT NULL,
    json_cash_flows      JSONB,
    json_sensitivities   JSONB,
    computed_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_valuationresult PRIMARY KEY (result_id)
);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT uc_valuationassumption_created_by UNIQUE (created_by);

ALTER TABLE valuation_result
    ADD CONSTRAINT uc_valuationresult_assumption UNIQUE (assumption_id);

CREATE INDEX idx_asof ON valuation_assumption (as_of);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT FK_VALUATIONASSUMPTION_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES app_user (user_id);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT FK_VALUATIONASSUMPTION_ON_TRANCHE FOREIGN KEY (tranche_id) REFERENCES tranche (tranche_id);

ALTER TABLE valuation_result
    ADD CONSTRAINT FK_VALUATIONRESULT_ON_ASSUMPTION FOREIGN KEY (assumption_id) REFERENCES valuation_assumption (valuation_id);

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