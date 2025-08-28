package com.arete.korbly;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class KorblyPlatformApplication {

	public static void main(String[] args) {


		Dotenv dotenv = Dotenv
				.configure()
				.load();

		System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

		System.setProperty("CLOUDINARY_NAME", dotenv.get("CLOUDINARY_NAME"));
		System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
		System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

		System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
		System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));

		System.setProperty("REDIS_URL", dotenv.get("REDIS_URL"));

		System.setProperty("SENTRY_AUTH_TOKEN", dotenv.get("SENTRY_AUTH_TOKEN"));

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
