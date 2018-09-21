package com.pw.ordermanager.ui.views.orderslist;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.SellerRepository;
import com.pw.ordermanager.backend.service.OrderService;
import com.pw.ordermanager.backend.service.OrderedProductService;
import com.pw.ordermanager.backend.service.SellerService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.pw.ordermanager.ui.MainLayout;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.encoders.LocalDateToStringEncoder;
import com.pw.ordermanager.ui.encoders.LongToStringEncoder;
import com.pw.ordermanager.ui.encoders.OrderStatusToStringEncoder;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays the list of available orders, with a search filter as well as
 * buttons to add a new order or edit existing ones.
 *
 * Implemented using a simple template.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Order List")
@Tag("orders-list")
@HtmlImport("frontend://src/views/orderslist/orders-list.html")
public class OrdersList extends PolymerTemplate<OrdersList.OrdersModel> implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateList();
        }
    }

    public interface OrdersModel extends TemplateModel {
        @Encode(value = OrderStatusToStringEncoder.class, path = "orderStatus")
        @Encode(value = LocalDateToStringEncoder.class, path = "date")
        @Encode(value = LongToStringEncoder.class, path = "counter")
        @Include({"counter","totalPrice","title","orderStatus","date","description"})
        void setOrders(List<Order> orders);
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderedProductService orderedProductService;

    @Id("search")
    private TextField search;
    @Id("newOrder")
    private Button addOrder;
    @Id("header")
    private H2 header;

    private OrderEditorDialog orderForm = new OrderEditorDialog(
            this::saveUpdate, this::deleteUpdate);

    public OrdersList() { }

    /**
     * Needs to be done here, because autowireds field are injected after constructor
     */
    @PostConstruct
    public void init(){
        search.setPlaceholder("Search orders");
        search.addValueChangeListener(e -> updateList());
        search.setValueChangeMode(ValueChangeMode.EAGER);

        addOrder.addClickListener(e -> openForm(new Order(),
                AbstractEditorDialog.Operation.ADD));
    }

    public void saveUpdate(Order order,
                           AbstractEditorDialog.Operation operation) {
        order.getOrderedProduct().forEach(orderedProduct -> orderedProductService.save(orderedProduct));
        orderService.saveOrder(order);
        updateList();
        Notification.show(
                "Beverage successfully " + operation.getNameInText() + "ed.",
                3000, Notification.Position.BOTTOM_START);
    }

    public void deleteUpdate(Order order) {
        order.getOrderedProduct().forEach(orderedProduct -> orderedProductService.delete(orderedProduct));
        orderService.deleteOrder(order);
        updateList();
        Notification.show("Beverage successfully deleted.", 3000,
                Notification.Position.BOTTOM_START);
    }

    private void updateList() {
        List<Order> orders = orderService.findOrders(SecurityUtils.getCurrentUser().getUser(), search.getValue());
        if (search.isEmpty()) {
            header.setText("Orders");
            header.add(new Span(orders.size() + " in total"));
        } else {
            header.setText("Search for “" + search.getValue() + "”");
            if (!orders.isEmpty()) {
                header.add(new Span(orders.size() + " results"));
            }
        }
        getModel().setOrders(orders);
    }

    @EventHandler
    private void edit(@ModelItem Order order) {
        openForm(order, AbstractEditorDialog.Operation.EDIT);
    }

    private void openForm(Order order,
                          AbstractEditorDialog.Operation operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (orderForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(orderForm));
        }
        order.setOwner(SecurityUtils.getCurrentUser().getUser());
        orderForm.open(order, operation);
    }
}
