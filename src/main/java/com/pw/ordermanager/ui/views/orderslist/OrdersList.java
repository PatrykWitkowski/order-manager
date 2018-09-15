package com.pw.ordermanager.ui.views.orderslist;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.ui.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;

/**
 * Displays the list of available orders, with a search filter as well as
 * buttons to add a new order or edit existing ones.
 *
 * Implemented using a simple template.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Order List")
@Tag("order-list")
@HtmlImport("frontend://src/views/orderslist/orders-list.html")
public class OrdersList extends PolymerTemplate<OrdersList.OrdersModel> {

    public interface OrdersModel extends TemplateModel {
    }
}
