package com.pw.ordermanager.ui.views.settingslist;

import com.pw.ordermanager.backend.service.UserService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.pw.ordermanager.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Objects;

@Route(value = "settings", layout = MainLayout.class)
@PageTitle("Settings")
public class SettingsList extends VerticalLayout {

    private H2 changePasswordTitle = new H2("Change password");
    private Button confirm = new Button("Confirm");
    private Button reset = new Button("Reset");

    @Autowired
    private UserService userService;

    public SettingsList(){
        createChangePassword();
    }

    private void createChangePassword() {
        HorizontalLayout changePassword = new HorizontalLayout();
        changePassword.getStyle().set("border", "1px solid #9E9E9E");
        changePassword.setPadding(true);
        changePassword.setBoxSizing(BoxSizing.BORDER_BOX);

        PasswordField actualPassword = new PasswordField("Actual Password");
        actualPassword.setRevealButtonVisible(false);
        PasswordField newPassword = new PasswordField("New Password");
        newPassword.setRevealButtonVisible(false);
        PasswordField confirmNewPassword = new PasswordField("Confirm New Password");
        confirmNewPassword.setRevealButtonVisible(false);

        confirm.addClickListener(e -> checkActualPasswordIsCorrect(actualPassword, newPassword, confirmNewPassword));

        reset.addClickListener(e -> resetPasswordsFields(actualPassword, newPassword, confirmNewPassword));

        changePassword.add(actualPassword, newPassword, confirmNewPassword, confirm, reset);
        changePassword.setDefaultVerticalComponentAlignment(Alignment.END);
        VerticalLayout layout = new VerticalLayout(changePasswordTitle, changePassword);
        add(layout);
    }

    private void resetPasswordsFields(PasswordField actualPassword, PasswordField newPassword, PasswordField confirmNewPassword) {
        actualPassword.setValue(Strings.EMPTY);
        newPassword.setValue(Strings.EMPTY);
        confirmNewPassword.setValue(Strings.EMPTY);
        actualPassword.setInvalid(false);
        newPassword.setInvalid(false);
        confirmNewPassword.setInvalid(false);
    }

    private void checkActualPasswordIsCorrect(PasswordField actualPassword, PasswordField newPassword, PasswordField confirmNewPassword) {
        if(BCrypt.checkpw(actualPassword.getValue(), SecurityUtils.getCurrentUser().getUser().getPassword())){
            checkNewPasswordIsTheSameAsConfirm(actualPassword, newPassword, confirmNewPassword);
        } else {
            actualPassword.setInvalid(true);
            Notification.show("Actual password is incorrect.", 3000, Notification.Position.MIDDLE);
        }
    }

    private void checkNewPasswordIsTheSameAsConfirm(PasswordField actualPassword, PasswordField newPassword, PasswordField confirmNewPassword) {
        if(Objects.equals(newPassword.getValue(), confirmNewPassword.getValue())){
            checkNewPasswordIsDifferentThanOldOne(actualPassword, newPassword);
        } else {
            newPassword.setInvalid(true);
            confirmNewPassword.setInvalid(true);
            Notification.show("New password and its confirmation are not the same.", 3000, Notification.Position.MIDDLE);
        }
    }

    private void checkNewPasswordIsDifferentThanOldOne(PasswordField actualPassword, PasswordField newPassword) {
        if(!Objects.equals(actualPassword.getValue(), newPassword.getValue())){
            checkNewPasswordPreconditions(newPassword);

        } else {
            Notification.show("New password must be different than the old one.", 3000, Notification.Position.MIDDLE);
        }
    }

    private void checkNewPasswordPreconditions(PasswordField newPassword) {
        if(StringUtils.isNotBlank(newPassword.getValue()) && StringUtils.length(newPassword.getValue()) > 4
        && StringUtils.length(newPassword.getValue()) < 21) {
            userService.changePassword(newPassword.getValue());
            reset.click();
            Notification.show("Password successfully changed.", 3000, Notification.Position.MIDDLE);
        } else {
            Notification.show("New password must contains 5 - 20 character.", 3000, Notification.Position.MIDDLE);
        }
    }
}
