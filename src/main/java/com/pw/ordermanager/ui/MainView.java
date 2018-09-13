package com.pw.ordermanager.ui;

import com.pw.ordermanager.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    public MainView(){
        final Button logoutBtn = new Button("Logout");
        logoutBtn.addClickListener(e -> {
            SecurityUtils.revokeAccess();
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });
        add(logoutBtn);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!SecurityUtils.isAccessGranted()){
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }
}
