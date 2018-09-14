package com.pw.ordermanager.ui;

import com.pw.ordermanager.backend.dts.UserDts;
import com.pw.ordermanager.backend.service.UserService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserService userService;

    public LoginView() {
        TextField username = new TextField();
        username.setLabel("Username");
        username.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField password = new PasswordField();
        password.setLabel("Password");
        password.setValueChangeMode(ValueChangeMode.EAGER);
        Button login = new Button();
        login.setText("Login");
        login.addClickListener(e -> onLogin(username, password));
        login.setEnabled(false);
        username.addValueChangeListener(e -> {
            if (e.getValue().isEmpty() || password.getValue().isEmpty()){
                login.setEnabled(false);
        } else {
                login.setEnabled(true);
            }

        });
        password.addValueChangeListener(e -> {
            if (e.getValue().isEmpty() || username.getValue().isEmpty()){
                login.setEnabled(false);
            } else {
                login.setEnabled(true);
            }

        });

        HorizontalLayout loginData = new HorizontalLayout(username, password, login);
        loginData.setVerticalComponentAlignment(Alignment.END, login);

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        add(loginData);
    }

    private void onLogin(TextField username, PasswordField password) {
        final UserDts authenticatedUser = userService.authenticate(username.getValue(), password.getValue());
        if(authenticatedUser.getUser() != null){
            if(authenticatedUser.isAuthorized()){
                Notification.show("Password correct! :)");
                getUI().ifPresent(ui -> ui.navigate(MainView.class));
            } else{
                Notification.show("Password wrong! :(");
            }
        } else{
            Notification.show("User not found! :(");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.isAccessGranted()){
            beforeEnterEvent.rerouteTo(MainView.class);
        }
    }
}
