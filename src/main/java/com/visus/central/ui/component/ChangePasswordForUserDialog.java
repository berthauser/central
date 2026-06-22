package com.visus.central.ui.component;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.visus.central.domain.model.User;
import com.visus.central.domain.port.in.PasswordChangeUseCase;
import com.visus.central.domain.port.out.UserRepository;
import com.visus.central.infraestructure.security.SecurityUtils;
import com.visus.central.infraestructure.util.SpringContextBridge;

public class ChangePasswordForUserDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

    public ChangePasswordForUserDialog(User user, PasswordChangeUseCase useCase) {
    	if (!SecurityUtils.isAdmin()) {
    		Notification.show("Solo ADMIN puede cambiar contraseñas de otros usuarios.", 3000, Notification.Position.MIDDLE);
    		close();
    		return;
    	}
        setHeaderTitle("Cambiar contraseña de: " + user.getUsername());
        setWidth("400px");

        PasswordField newPasswordField = new PasswordField("Nueva contraseña");
        newPasswordField.setRequired(true);
        newPasswordField.setMinLength(6);
        newPasswordField.setWidthFull();

        PasswordField confirmPasswordField = new PasswordField("Confirmar nueva contraseña");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setWidthFull();

        Button btnGuardar = new Button("Guardar", _ -> {
            String newPwd = newPasswordField.getValue();
            String confirmPwd = confirmPasswordField.getValue();

            if (!newPwd.equals(confirmPwd)) {
                Notification.show("Las contraseñas no coinciden", 3000, Notification.Position.MIDDLE);
                return;
            }

            UserRepository repo = SpringContextBridge.getBean(UserRepository.class);
            PasswordEncoder encoder = SpringContextBridge.getBean(PasswordEncoder.class);
            user.setPassword(encoder.encode(newPwd));
            repo.actualizar(user);
            Notification.show("Contraseña actualizada", 3000, Notification.Position.MIDDLE);
            close();
        });
        btnGuardar.addClassName("btn-grabar");
        
        Button btnCancelar = new Button("Cancelar", _ -> close());
        btnCancelar.addClassName("btn-volver");

        add(new VerticalLayout(newPasswordField, confirmPasswordField, btnGuardar, btnCancelar));
    }

}
