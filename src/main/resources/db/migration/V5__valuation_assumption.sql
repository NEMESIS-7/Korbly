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

CREATE INDEX idx_valuation_tranche ON valuation_assumption (tranche_id);
