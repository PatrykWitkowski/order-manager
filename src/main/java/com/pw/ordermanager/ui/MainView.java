package com.pw.ordermanager.ui;

import com.pw.ordermanager.backend.user.User;
import com.pw.ordermanager.backend.user.UserRepository;
import com.pw.ordermanager.backend.user.UserService;
import com.pw.ordermanager.backend.user.UserType;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends VerticalLayout {

    @Autowired
    private UserService userService;

    public MainView() {
        TextField username = new TextField();
        username.setLabel("Username");
        username.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField password = new PasswordField();
        password.setLabel("Password");
        password.setValueChangeMode(ValueChangeMode.EAGER);
        Button login = new Button();
        login.setText("Login");
        login.addClickListener(e -> login(username, password));
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

    private void login(TextField username, PasswordField password) {
        final User authenticatedUser = userService.authenticate(username.getValue(), password.getValue());
        if(authenticatedUser.getType() == UserType.ADMIN || authenticatedUser.getType() == UserType.SIMPLE){
            Notification.show("Password correct! :)");
        } else if (authenticatedUser.getType() == UserType.NOT_AUTH){
            Notification.show("Password wrong! :(");
        } else if(authenticatedUser.getType() == UserType.NONE){
            Notification.show("User not found! :(");
        }
    }

}
