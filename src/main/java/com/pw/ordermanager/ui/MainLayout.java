package com.pw.ordermanager.ui;

import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.pw.ordermanager.ui.components.ClickableRouterLink;
import com.pw.ordermanager.ui.views.LoginView;
import com.pw.ordermanager.ui.views.orderslist.OrdersList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@HtmlImport("frontend://styles/shared-styles.html")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout extends Div
        implements RouterLayout, PageConfigurator, BeforeEnterObserver {

    public MainLayout() {
        H2 title = new H2("Order Manager");
        title.addClassName("main-layout__title");

        RouterLink orders = new RouterLink(null, OrdersList.class);
        orders.add(new Icon(VaadinIcon.LIST), new Text("Orders"));
        orders.addClassName("main-layout__nav-item");
        // Only show as active for the exact URL, but not for sub paths
        orders.setHighlightCondition(HighlightConditions.sameLocation());

        ClickableRouterLink logout = new ClickableRouterLink(null, LoginView.class);
        logout.add(new Icon(VaadinIcon.POWER_OFF), new Text("Logout"));
        logout.addClassName("main-layout__nav-item");
        logout.addClickListener(e -> onLogout());

        Div navigation = new Div(orders, logout);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");

        finishSession();
    }

    private void finishSession() {
        VaadinService.getCurrent().addSessionDestroyListener((SessionDestroyEvent sessionDestroyEvent) -> {
            SecurityUtils.revokeAccess(sessionDestroyEvent.getSession().getCsrfToken());
        });
    }

    private void onLogout() {
        getUI().get().getSession().close();
        getUI().get().getPage().reload();
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!SecurityUtils.isAccessGranted(beforeEnterEvent.getUI().getSession().getCsrfToken())){
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }
}
