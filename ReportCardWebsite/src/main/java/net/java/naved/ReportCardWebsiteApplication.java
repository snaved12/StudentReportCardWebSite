package net.java.naved;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "net.java.naved")
public class ReportCardWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportCardWebsiteApplication.class, args);
	}

}
