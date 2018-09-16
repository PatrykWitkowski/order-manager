package com.pw.ordermanager;

import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.vaadin.flow.server.*;

import javax.servlet.ServletException;

public class OrderManagerServlet extends VaadinServlet
        implements SessionDestroyListener {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionDestroyListener(this);
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        SecurityUtils.revokeAccess(event.getSession().getCsrfToken());
    }
}
