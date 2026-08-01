package com.arete.korbly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class KorblyPlatformApplication {

	public static void main(String[] args) {
		setPropertyIfPresent("SPRING_DATASOURCE_URL");
		setPropertyIfPresent("SPRING_DATASOURCE_USERNAME");
		setPropertyIfPresent("SPRING_DATASOURCE_PASSWORD");
		setPropertyIfPresent("CLOUDINARY_NAME");
		setPropertyIfPresent("CLOUDINARY_API_SECRET");
		setPropertyIfPresent("CLOUDINARY_API_KEY");
		setPropertyIfPresent("JWT_SECRET");
		setPropertyIfPresent("SPRING_MAIL_USERNAME");
		setPropertyIfPresent("SPRING_MAIL_PASSWORD");
		setPropertyIfPresent("REDIS_URL");
		setPropertyIfPresent("SENTRY_AUTH_TOKEN");
		setPropertyIfPresent("BUCKET_NAME");

		SpringApplication.run(KorblyPlatformApplication.class, args);
	}

	private static void setPropertyIfPresent(String key) {
		String value = System.getenv(key);
		if (value != null) {
			System.setProperty(key, value);
		}
	}

	/*@Bean
	CommandLineRunner testCredit(CreditEvaluationService creditEvaluationService, SMERepository smeRepository) {
		return args -> {
			SME sme = smeRepository.findById(UUID.fromString("ba10348b-92b3-46c8-aac1-4bf22f7bd6e4")).orElseThrow();

			FinancialsDTO enums = new FinancialsDTO(
					new BigDecimal("1000000"),  // totalAssets
					new BigDecimal("500000"),   // totalLiabilities
					new BigDecimal("300000"),   // currentAssets
					new BigDecimal("200000"),   // currentLiabilities
					new BigDecimal("100000"),   // workingCapital
					new BigDecimal("150000"),   // retainedEarnings
					new BigDecimal("100000"),   // EBIT
					new BigDecimal("600000"),   // sales
					new BigDecimal("250000"),   // marketValueEquity
					new BigDecimal("450000"),   // totalDebt
					new BigDecimal("80000"),    // cashFlow
					new BigDecimal("50000"),    // netIncome
					new BigDecimal("3")         // companySize
			);

			CreditMemoDTO memo = creditEvaluationService.evaluateAndSave(UUID.fromString("ba10348b-92b3-46c8-aac1-4bf22f7bd6e4"), enums);
			System.out.println("Memo saved with Altman: " + memo.altmanScore() + " and ESG: " + memo.esgRiskRating());
		};*/
}
