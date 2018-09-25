package com.pw.ordermanager.ui.views.orderslist;

import com.pw.ordermanager.backend.common.OrderStatus;
import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.components.ProductManager;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OrderEditorDialog extends AbstractEditorDialog<Order> {

    private TextField orderTitle = new TextField();
    private TextArea description = new TextArea();
    private ComboBox<OrderStatus> orderStatusComboBox = new ComboBox<>();
    private DatePicker datePicker = new DatePicker();
    private volatile boolean productManagerAlreadyAdded = false;
    private Tab orderingTab;

    public OrderEditorDialog(BiConsumer<Order, Operation> saveHandler,
                              Consumer<Order> deleteHandler) {
        super("order", saveHandler, deleteHandler);

        createOrderTitle();
        createDescription();
        createOrderStatus();
        createDatePicker();
    }

    private void createProductManager(Order item) {
        ProductManager productManager;
        if(!productManagerAlreadyAdded){
            productManager = new ProductManager(item);
            orderingTab = addNewTab("Ordering", new Div(productManager));
            productManagerAlreadyAdded = true;
        } else{
            productManager = new ProductManager(item);
            updateTab(orderingTab, new Div(productManager));
        }
    }

    @Override
    public final void open(Order item, Operation operation) {
        createProductManager(item);
        super.open(item, operation);
    }

    private void createDatePicker() {
        datePicker.setLabel("Date");
        datePicker.setRequired(true);
        datePicker.setMax(LocalDate.now());
        datePicker.setMin(LocalDate.of(1, 1, 1));
        datePicker.setValue(LocalDate.now());
        getFormLayout().add(datePicker);

        getBinder().forField(datePicker)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(new DateRangeValidator(
                        "The date should be neither Before Christ nor in the future.",
                        LocalDate.of(1, 1, 1), LocalDate.now()))
                .bind(Order::getOrderDate, Order::setOrderDate);
    }

    private void createOrderStatus() {
        orderStatusComboBox.setLabel("Status");
        orderStatusComboBox.setRequired(true);
        orderStatusComboBox.setAllowCustomValue(false);
        orderStatusComboBox.setItems(OrderStatus.values());
        getFormLayout().add(orderStatusComboBox);

        getBinder().forField(orderStatusComboBox)
                .withValidator(Objects::nonNull,
                        "Order status must be filled.")
                .bind(Order::getStatus, Order::setStatus);
    }

    private void createDescription() {
        description.setLabel("Description");
        getFormLayout().add(description);

        getBinder().forField(description)
                .withValidator(new StringLengthValidator(
                        "Description must contain at least 15 characters",
                        15, null))
                .bind(Order::getDescription, Order::setDescription);
    }

    private void createOrderTitle() {
        orderTitle.setLabel("Title");
        orderTitle.setRequired(true);
        getFormLayout().add(orderTitle);

        getBinder().forField(orderTitle)
                .withValidator(new StringLengthValidator(
                        "Title must contain at least 5 characters",
                        5, null))
                .bind(Order::getTitle, Order::setTitle);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog("Delete review",
                "Are you sure you want to delete the order: “" + getCurrentItem().getTitle() + "”?", "");
    }
}
