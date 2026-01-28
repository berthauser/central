package com.visus.central.infraestructure.util;

import java.util.Timer;
import java.util.TimerTask;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;

public class BarcodeScannerUtil {
	
	/**
     * Configura un TextField para detectar códigos de barra de lectores automáticos.
     * Los lectores suelen enviar un Enter al final del código.
     */
    public static void configureBarcodeScanner(TextField textField, Runnable searchAction) {
        // Listener para la tecla Enter
        textField.addKeyPressListener(Key.ENTER, _ -> {
            String value = textField.getValue();
            if (value != null && !value.trim().isEmpty()) {
                searchAction.run();
            }
        });
        
        // También detectar cuando se pierde el foco
        textField.addBlurListener(_ -> {
            String value = textField.getValue();
            if (value != null && value.length() >= 8) { // Mínimo 8 caracteres para códigos válidos
                searchAction.run();
            }
        });
     
        // Auto-seleccionar el texto cuando se hace clic (para facilitar el escaneo repetido)
        textField.addFocusListener(_ -> {
            UI.getCurrent().access(() -> {
                textField.getElement().executeJs("this.select()");
            });
        });
        
        // Limpiar automáticamente después de 2 segundos de inactividad (opcional)
        textField.addValueChangeListener(event -> {
            if (event.getValue() != null && !event.getValue().isEmpty()) {
                UI.getCurrent().access(() -> {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            UI.getCurrent().access(() -> {
                                textField.clear();
                            });
                        }
                    }, 2000); // 2 segundos
                });
            }
        });
    }

}
