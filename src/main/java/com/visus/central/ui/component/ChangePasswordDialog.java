package com.visus.central.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.visus.central.domain.port.in.PasswordChangeUseCase;
import com.visus.central.infraestructure.security.SecurityUtils;

public class ChangePasswordDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

    public ChangePasswordDialog(PasswordChangeUseCase passwordChangeUseCase) {
        setHeaderTitle("Cambiar Contraseña");
        setWidth("400px");

        PasswordField oldPasswordField = new PasswordField("Contraseña actual");
        oldPasswordField.setRequired(true);
        oldPasswordField.setWidthFull();

        PasswordField newPasswordField = new PasswordField("Nueva contraseña");
        newPasswordField.setRequired(true);
        newPasswordField.setMinLength(6);
        newPasswordField.setWidthFull();

        PasswordField confirmPasswordField = new PasswordField("Confirmar nueva contraseña");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setWidthFull();

        Button btnGuardar = new Button("Guardar", _ -> {
            String username = SecurityUtils.getUsername();
            if (username == null) {
                Notification.show("No hay sesión activa", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            String oldPwd = oldPasswordField.getValue();
            String newPwd = newPasswordField.getValue();
            String confirmPwd = confirmPasswordField.getValue();

            if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (!newPwd.equals(confirmPwd)) {
                Notification.show("Las contraseñas nuevas no coinciden", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                passwordChangeUseCase.changePassword(username, oldPwd, newPwd);
                Notification.show("Contraseña cambiada exitosamente", 3000, Notification.Position.MIDDLE);
                close();
            } catch (RuntimeException e) {
                Notification.show(e.getMessage(), 4000, Notification.Position.MIDDLE);
            }
        });
        
        btnGuardar.addClassName("btn-grabar");

        Button btnCancelar = new Button("Cancelar", _ -> close());
        btnCancelar.addClassName("btn-volver");

        HorizontalLayout footer = new HorizontalLayout(btnGuardar, btnCancelar);
        VerticalLayout form = new VerticalLayout(
            oldPasswordField, newPasswordField, confirmPasswordField, footer);
        
        form.setSpacing(true);
        form.setPadding(true);

        add(form);
    }

}
