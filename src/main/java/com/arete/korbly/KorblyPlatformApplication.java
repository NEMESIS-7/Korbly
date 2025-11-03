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
		System.setProperty("SPRING_DATASOURCE_URL", System.getenv("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", System.getenv("SPRING_DATASOURCE_PASSWORD"));

		System.setProperty("CLOUDINARY_NAME", System.getenv("CLOUDINARY_NAME"));
		System.setProperty("CLOUDINARY_API_SECRET", System.getenv("CLOUDINARY_API_SECRET"));
		System.setProperty("CLOUDINARY_API_KEY", System.getenv("CLOUDINARY_API_KEY"));
		System.setProperty("JWT_SECRET", System.getenv("JWT_SECRET"));

		System.setProperty("SPRING_MAIL_USERNAME", System.getenv("SPRING_MAIL_USERNAME"));
		System.setProperty("SPRING_MAIL_PASSWORD", System.getenv("SPRING_MAIL_PASSWORD"));

		System.setProperty("REDIS_URL", System.getenv("REDIS_URL"));

		System.setProperty("SENTRY_AUTH_TOKEN", System.getenv("SENTRY_AUTH_TOKEN"));

		SpringApplication.run(KorblyPlatformApplication.class, args);
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
