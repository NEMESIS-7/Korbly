package com.arete.korbly.modules.termsheet.enums;

public enum AmortizationStructure {
    FULL_AMORTIZATION("Loan to paid wholly including principal and interest"),
    PARTIAL_AMORTIZATION("Loan to paid in parts. At the end of loan term, there will be a balloon payment"),
    NEGATIVE_AMORTIZATION("Borrower pays less than the interest owed, so the loan amount grows instead of shrinking"),
    STRAIGHT_LINE_AMORTIZATION("Same amount paid every month, but interest changes"),
    BULLET_AMORTIZATION("Interest is paid only during the loan term. At the end of the loan term, whole loan amount is paid in one go");

    private final String value;

    AmortizationStructure(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
