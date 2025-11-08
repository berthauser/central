package com.visus.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication(scanBasePackages = "com.visus.central")
@Theme("visus")
public class CentralApplication implements AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SpringApplication.run(CentralApplication.class, args);
	}

}
