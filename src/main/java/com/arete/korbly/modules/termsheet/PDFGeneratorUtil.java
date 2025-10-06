package com.arete.korbly.modules.termsheet;

import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Component
public class PDFGeneratorUtil {

    private static final Locale GH_LOCALE = new Locale("en", "GH");
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(GH_LOCALE);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final Font H1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private static final Font H2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font BODY = FontFactory.getFont(FontFactory.HELVETICA, 11);
    private static final Font SMALL = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font SMALL_ITALIC = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9);
    private static final Font TH = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

    public byte[] buildTermSheetPDF(TermSheet sheet, List<ConditionsPrecedent> cps) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Page: A4, slightly wider margins for tables
            Document document = new Document(PageSize.A4, 36, 36, 54, 45);
            PdfWriter.getInstance(document, baos);

            // Optional: register OS fonts (no-op if unavailable)
            FontFactory.registerDirectories();

            document.open();

            // ---------- HEADER ----------
            Paragraph header = new Paragraph("Term Sheet – " + safe(() -> sheet.getDealId().getDealTitle()), H1);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph meta = new Paragraph(
                    "Version " + nvl(String.valueOf(safe(sheet::getSheetVersion)), "-")
                            + "    |    SME: " + nvl(safe(() -> sheet.getSmeId().getCompanyName()), "-"),
                    SMALL
            );
            meta.setAlignment(Element.ALIGN_CENTER);
            meta.setSpacingAfter(10f);
            document.add(meta);

            LineSeparator sep = new LineSeparator(0.5f, 100f, null, Element.ALIGN_CENTER, -2f);
            document.add(new Chunk(sep));

            document.add(Chunk.NEWLINE);

            // ---------- DEAL SUMMARY ----------
            document.add(sectionTitle("Deal Summary"));

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(4f);
            summaryTable.setSpacingAfter(10f);
            summaryTable.setWidths(new float[]{28f, 72f});

            addSummaryRow(summaryTable, "Deal ID", nvl(safe(() -> sheet.getDealId().getDealId().toString()), "-"));
            addSummaryRow(summaryTable, "Tranche", nvl(safe(() -> sheet.getTrancheId().getTrancheType().getValue()), "-"));
            addSummaryRow(summaryTable, "Loan Amount", fmtMoney(safe(() -> sheet.getLoanAmount())));
            addSummaryRow(summaryTable, "Interest Rate", fmtPercent(safe(() -> sheet.getInterestRate())));
            addSummaryRow(summaryTable, "Maturity Date", fmtDate(safe(() -> sheet.getMaturityDate())));
            addSummaryRow(summaryTable, "Amortization", nvl(safe(() -> sheet.getAmortizationStructure().name()), "-"));
            addSummaryRow(summaryTable, "Prepayment Option", boolYesNo(safe(() -> sheet.getPrepaymentOption())));
            addSummaryRow(summaryTable, "Governing Law", nvl(safe(() -> sheet.getGoverningLaw().name()), "-"));

            document.add(summaryTable);

            // ---------- COVENANTS ----------
            document.add(sectionTitle("Covenants"));
            PdfPTable covTable = new PdfPTable(2);
            covTable.setWidthPercentage(100);
            covTable.setSpacingBefore(4f);
            covTable.setSpacingAfter(10f);
            covTable.setWidths(new float[]{30f, 70f});

            addHeaderRow(covTable, "Category", "Items");

            Map<String, List<String>> covenants = safe(() -> sheet.getCovenants());
            if (covenants != null && !covenants.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : covenants.entrySet()) {
                    covTable.addCell(cell(entry.getKey()));
                    covTable.addCell(cell(entry.getValue() == null ? "-" : String.join(", ", entry.getValue())));
                }
            } else {
                covTable.addCell(cell("-"));
                covTable.addCell(cell("-"));
            }
            document.add(covTable);

            // ---------- EVENTS OF DEFAULT ----------
            document.add(sectionTitle("Events of Default"));
            PdfPTable defaultTable = new PdfPTable(2);
            defaultTable.setWidthPercentage(100);
            defaultTable.setSpacingBefore(4f);
            defaultTable.setSpacingAfter(10f);
            defaultTable.setWidths(new float[]{30f, 70f});

            addHeaderRow(defaultTable, "Category", "Triggers");

            Map<String, List<String>> eods = safe(() -> sheet.getEventsOfDefault());
            if (eods != null && !eods.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : eods.entrySet()) {
                    defaultTable.addCell(cell(entry.getKey()));
                    defaultTable.addCell(cell(entry.getValue() == null ? "-" : String.join(", ", entry.getValue())));
                }
            } else {
                defaultTable.addCell(cell("-"));
                defaultTable.addCell(cell("-"));
            }
            document.add(defaultTable);

            // ---------- CONDITIONS PRECEDENT ----------
            document.add(sectionTitle("Conditions Precedent"));

            PdfPTable cpTable = new PdfPTable(5);
            cpTable.setWidthPercentage(100);
            cpTable.setSpacingBefore(4f);
            cpTable.setSpacingAfter(8f);
            cpTable.setWidths(new float[]{28f, 10f, 14f, 24f, 24f});

            addHeaderRow(cpTable, "Title", "Required", "Status", "Evidence", "Notes");

            if (cps != null && !cps.isEmpty()) {
                for (ConditionsPrecedent cp : cps) {
                    cpTable.addCell(cell(nvl(cp.getTitle(), "-")));
                    cpTable.addCell(cell(boolYesNo(cp.getRequired())));
                    cpTable.addCell(cell(nvl(safe(cp::getStatus, s -> s.name()), "-")));
                    cpTable.addCell(cell(nvl(cp.getEvidenceFileKey(), "-")));
                    cpTable.addCell(cell(nvl(cp.getNote(), "-")));
                }
            } else {
                PdfPCell empty = new PdfPCell(new Phrase("No conditions precedent specified", BODY));
                empty.setColspan(5);
                empty.setPadding(6f);
                cpTable.addCell(empty);
            }
            document.add(cpTable);

            // ---------- DISCLAIMER ----------
            Paragraph disclaimer = new Paragraph(
                    "Disclaimer: This Term Sheet is provided for discussion purposes only and does not constitute a binding agreement.",
                    SMALL_ITALIC
            );
            disclaimer.setAlignment(Element.ALIGN_CENTER);
            disclaimer.setSpacingBefore(8f);
            document.add(disclaimer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            // Keep original stacktrace; wrap with context
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }


    private Paragraph sectionTitle(String text) {
        Paragraph p = new Paragraph(text, H2);
        p.setSpacingBefore(6f);
        p.setSpacingAfter(3f);
        return p;
    }

    private void addSummaryRow(PdfPTable table, String label, String value) {
        PdfPCell left = new PdfPCell(new Phrase(label, TH));
        left.setPadding(6f);
        left.setVerticalAlignment(Element.ALIGN_MIDDLE);
        left.setBackgroundColor(new Color(245, 245, 245));

        PdfPCell right = new PdfPCell(new Phrase(nvl(value, "-"), BODY));
        right.setPadding(6f);
        right.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(left);
        table.addCell(right);
    }

    private void addHeaderRow(PdfPTable table, String... headers) {
        for (String h : headers) {
            PdfPCell th = new PdfPCell(new Phrase(h, TH));
            th.setPadding(6f);
            th.setVerticalAlignment(Element.ALIGN_MIDDLE);
            th.setBackgroundColor(new Color(232, 232, 232));
            table.addCell(th);
        }
    }

    private PdfPCell cell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(nvl(text, "-"), BODY));
        c.setPadding(6f);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private String fmtMoney(BigDecimal v) {
        if (v == null) return "-";
        try {
            return CURRENCY.format(v);
        } catch (Exception e) {
            return v.toPlainString();
        }
    }

    private String fmtPercent(Number n) {
        if (n == null) return "-";
        // Expecting e.g., 12.5 => "12.5%"
        return String.valueOf(n) + "%";
    }

    private String fmtDate(Object dateObj) {
        if (dateObj == null) return "-";
        // Support java.time.LocalDate; otherwise fallback to toString()
        if (dateObj instanceof LocalDate) {
            return ((LocalDate) dateObj).format(DATE_FMT);
        }
        return String.valueOf(dateObj);
    }

    private String boolYesNo(Boolean b) {
        return Boolean.TRUE.equals(b) ? "Yes" : "No";
        // null => "No" by design; change if you want "-"
    }

    private static String nvl(String s, String defaultVal) {
        return (s == null || s.trim().isEmpty()) ? defaultVal : s;
    }

    /** Null-safe supplier for nested getters without littering try/catch everywhere. */
    private static <T> T safe(SupplierEx<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception ignored) {
            return null;
        }
    }

    /** Null-safe + mapper (e.g., Enum::name). */
    private static <I, O> O safe(SupplierEx<I> supplier, java.util.function.Function<I, O> map) {
        try {
            I v = supplier.get();
            return v == null ? null : map.apply(v);
        } catch (Exception ignored) {
            return null;
        }
    }

    @FunctionalInterface
    private interface SupplierEx<T> {
        T get() throws Exception;
    }
}
