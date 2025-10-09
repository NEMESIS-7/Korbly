package com.arete.korbly.modules.termsheet.util;

import com.arete.korbly.modules.shared.enums.CpMeta;
import com.arete.korbly.modules.termsheet.enums.CPCode;

import java.util.Map;

public final class CpCatalog {

    private CpCatalog() {}

    public static final Map<CPCode, CpMeta> META = Map.ofEntries(
            Map.entry(CPCode.CORP_BOARD_RESOLUTION_BORROW,
                    new CpMeta("Board Resolution",
                            "Board resolution authorizing the borrowing and execution of loan documents.",
                            true)),

            Map.entry(CPCode.INS_LOSS_PAYEE_ENDORSEMENT,
                    new CpMeta("Insurance Certificate",
                            "Proof of insurance covering pledged collateral with lender as loss payee.",
                            true)),

            Map.entry(CPCode.SEC_PERFECTION_FILINGS_COMPLETE,
                    new CpMeta("Collateral Perfection",
                            "Filing of security documents and registration with relevant registry.",
                            true)),

            Map.entry(CPCode.KYC_BORROWER_COMPLETE,
                    new CpMeta("KYC/KYB Documentation",
                            "Completed Know Your Customer (KYC) and Know Your Business compliance checks.",
                            true)),

            Map.entry(CPCode.FIN_TAX_CLEARANCE_CERT,
                    new CpMeta("Tax Clearance Certificate",
                            "Valid tax clearance certificate from the relevant authority.",
                            true)),

            Map.entry(CPCode.DOC_TERM_SHEET_EXECUTED,
                    new CpMeta("Executed Term Sheet",
                            "Counter-signed term sheet acknowledging key terms.",
                            true))
    );
}