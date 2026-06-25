package com.visus.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.theme.lumo.Lumo;

@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@CssImport("./styles.css")
@CssImport("./card.css")
@JavaScript("./autofill.js")
@EnableTransactionManagement
@EnableScheduling
@Push(transport = Transport.WEBSOCKET)
@SpringBootApplication(scanBasePackages = "com.visus.central")
public class CentralApplication implements AppShellConfigurator {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SpringApplication.run(CentralApplication.class, args);
	}

}
