package com.pw.ordermanager.ui.views.orderslist;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.service.OrderService;
import com.pw.ordermanager.backend.service.OrderedProductService;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

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
        @Encode(value = OrderStatusToStringEncoder.class, path = "status")
        @Encode(value = LocalDateToStringEncoder.class, path = "orderDate")
        @Encode(value = LongToStringEncoder.class, path = "counter")
        @Encode(value = LongToStringEncoder.class, path = "orderId")
        @Include({"counter","totalPrice","title","status","orderDate","description","orderId"})
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

        addOrder.addClickListener(e -> {
            final User currentUser = SecurityUtils.getCurrentUser().getUser();
            final Order newOrder = new Order(currentUser);
            //currentUser.getOrders().add(newOrder);
            openForm(newOrder, AbstractEditorDialog.Operation.ADD);
        });
    }

    public void saveUpdate(Order order,
                           AbstractEditorDialog.Operation operation) {
        final double totalPrice = order.getOrderedProduct().stream().map(p -> p.getPrice())
                .mapToDouble(Double::doubleValue).sum();
        order.setTotalPrice(totalPrice);

        orderService.saveOrder(order);
        final List<OrderedProduct> orderedProductServiceByOrder = orderedProductService.findByOrder(order);
        orderedProductServiceByOrder.stream()
                .filter(op -> !order.getOrderedProduct().contains(op))
                .forEach(op -> orderedProductService.delete(op));
        order.getOrderedProduct().forEach(orderedProduct -> orderedProductService.save(orderedProduct));
        updateList();
        Notification.show(
                "Order successfully " + operation.getNameInText() + "ed.",
                3000, Notification.Position.BOTTOM_START);
    }

    public void deleteUpdate(Order order) {
        order.getOrderedProduct().forEach(orderedProduct -> orderedProductService.delete(orderedProduct));
        order.setOrderedProduct(null);
        orderService.deleteOrder(order);
        updateList();
        Notification.show("Order successfully deleted.", 3000,
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
        final Long orderId = order.getOrderId();
        Order orderToEdit = orderService.findOrderById(orderId);
        openForm(orderToEdit, AbstractEditorDialog.Operation.EDIT);
    }

    private void openForm(Order order,
                          AbstractEditorDialog.Operation operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (orderForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(orderForm));
        }
        orderForm.open(order, operation);
    }
}
