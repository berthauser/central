package com.visus.central.ui.component;

import java.util.List;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.visus.central.domain.model.User;

public class UserForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private final TextField username = new TextField("Usuario");
	private final PasswordField password = new PasswordField("Contraseña");
	private final TextField role = new TextField("Rol");
	private final Binder<User> binder = new Binder<>(User.class);
	private User currentUser;
	private final List<String> rolesPermitidos = List.of("ADMIN", "USER", "AUDITOR");
	private final Button crear = new Button("Crear");
	private final Button modificar = new Button("Modificar");
	private final Button eliminar = new Button("Eliminar");
	private final Button volver = new Button("Volver");

	public UserForm() {
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.START);

        username.addClassName("campo-estilo-imagen");
        password.addClassName("campo-estilo-imagen");
        role.addClassName("campo-estilo-imagen");

        binder.forField(username)
            .asRequired("El usuario es obligatorio")
            .withValidator(u -> u.length() >= 3, "Debe tener al menos 3 caracteres")
            .bind(User::getUsername, null);

        binder.forField(password)
            .withValidator(p -> p == null || p.length() >= 6, "Mínimo 6 caracteres")
            .bind(User::getPassword, null);

        binder.forField(role)
            .asRequired("El rol es obligatorio")
            .withValidator(rolesPermitidos::contains, "Rol inválido. Debe ser ADMIN, USER o AUDITOR")
            .bind(User::getRole, null);

        binder.setBean(new User());

        crear.addClickListener(_ -> fireEvent(new CreateEvent(this, getUserFromForm())));
        modificar.addClickListener(_ -> fireEvent(new UpdateEvent(this, getUserFromForm())));
        eliminar.addClickListener(_ -> fireEvent(new DeleteEvent(this, currentUser)));
        volver.addClickListener(_ -> fireEvent(new BackEvent(this)));

        crear.addClassName("btn-grabar");
        modificar.addClassName("btn-modificar");
        eliminar.addClassName("btn-eliminar");
        volver.addClassName("btn-volver");

        HorizontalLayout botones = new HorizontalLayout(crear, modificar, eliminar, volver);
        botones.setAlignItems(Alignment.END);

        add(username, password, role, botones);
    }

	public void setUser(User user) {
        this.currentUser = user;
        binder.setBean(new User());
    }

    public void clearForm() {
        currentUser = null;
        binder.setBean(new User());
    }

    private User getUserFromForm() {
        return new User();
    }

    // Eventos personalizados
    @SuppressWarnings("serial")
	public static class CreateEvent extends ComponentEvent<UserForm> {
        private final User user;
        public CreateEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }
        public User getUser() { return user; }
    }

    @SuppressWarnings("serial")
	public static class UpdateEvent extends ComponentEvent<UserForm> {
        private final User user;
        public UpdateEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }
        public User getUser() { return user; }
    }

    @SuppressWarnings("serial")
	public static class DeleteEvent extends ComponentEvent<UserForm> {
        private final User user;
        public DeleteEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }
        public User getUser() { return user; }
    }

    @SuppressWarnings("serial")
	public static class BackEvent extends ComponentEvent<UserForm> {
        public BackEvent(UserForm source) { super(source, false); }
    }

    public Registration addCreateListener(ComponentEventListener<CreateEvent> listener) {
        return addListener(CreateEvent.class, listener);
    }

    public Registration addUpdateListener(ComponentEventListener<UpdateEvent> listener) {
        return addListener(UpdateEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addBackListener(ComponentEventListener<BackEvent> listener) {
        return addListener(BackEvent.class, listener);
    }

}
