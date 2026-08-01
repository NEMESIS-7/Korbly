-- Fix 1: Drop unique constraints on term_sheet that prevent versioning.
-- Multiple term sheet versions per deal/tranche/SME must be allowed.
ALTER TABLE term_sheet DROP CONSTRAINT IF EXISTS uc_termsheet_deal;
ALTER TABLE term_sheet DROP CONSTRAINT IF EXISTS uc_termsheet_sme;
ALTER TABLE term_sheet DROP CONSTRAINT IF EXISTS uc_termsheet_tranche;
ALTER TABLE term_sheet DROP CONSTRAINT IF EXISTS uc_termsheet_app_user;

-- Fix 1b: Drop unique constraint on conditions_precedent.approved_by_user_id.
-- A single approver user must be able to approve more than one CP.
ALTER TABLE conditions_precedent DROP CONSTRAINT IF EXISTS uc_conditionsprecedent_approved_by_user;

-- Fix 2: Rebuild valuation tables with the definitive schema.
-- V4 created an incomplete schema; V5 failed because the tables already existed.
-- Drop everything V4 created for these tables, then recreate correctly.
ALTER TABLE IF EXISTS valuation_result DROP CONSTRAINT IF EXISTS uc_valuationresult_assumption;
ALTER TABLE IF EXISTS valuation_result DROP CONSTRAINT IF EXISTS FK_VALUATIONRESULT_ON_ASSUMPTION;
ALTER TABLE IF EXISTS valuation_assumption DROP CONSTRAINT IF EXISTS uc_valuationassumption_created_by;
ALTER TABLE IF EXISTS valuation_assumption DROP CONSTRAINT IF EXISTS FK_VALUATIONASSUMPTION_ON_CREATED_BY;
ALTER TABLE IF EXISTS valuation_assumption DROP CONSTRAINT IF EXISTS FK_VALUATIONASSUMPTION_ON_TRANCHE;
DROP INDEX IF EXISTS idx_asof;
DROP INDEX IF EXISTS idx_valuation_tranche;
DROP TABLE IF EXISTS valuation_result;
DROP TABLE IF EXISTS valuation_assumption;

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
    npv_vs_discount_rate           JSONB,
    npv_vs_tenor_months            JSONB,
    computed_at                    TIMESTAMP WITHOUT TIME ZONE,
    computed_by_user_id            UUID,
    CONSTRAINT pk_valuationresult PRIMARY KEY (valuation_result_id)
);

CREATE INDEX idx_valuation_tranche ON valuation_assumption (tranche_id);
CREATE INDEX idx_valuation_as_of ON valuation_assumption (as_of);
CREATE INDEX idx_valuation_created_at ON valuation_assumption (created_at);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT FK_VALUATIONASSUMPTION_ON_TRANCHE FOREIGN KEY (tranche_id) REFERENCES tranche (tranche_id);

ALTER TABLE valuation_assumption
    ADD CONSTRAINT FK_VALUATIONASSUMPTION_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES app_user (user_id);

ALTER TABLE valuation_result
    ADD CONSTRAINT FK_VALUATIONRESULT_ON_ASSUMPTION FOREIGN KEY (valuation_assumption_id) REFERENCES valuation_assumption (valuation_id);
