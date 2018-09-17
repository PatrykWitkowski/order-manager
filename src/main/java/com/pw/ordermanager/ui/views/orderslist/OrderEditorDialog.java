package com.pw.ordermanager.ui.views.orderslist;

import com.pw.ordermanager.backend.common.OrderStatus;
import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.components.ProductManager;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OrderEditorDialog extends AbstractEditorDialog<Order> {


    private TextField oriderTitle = new TextField();
    private TextField description = new TextField();
    private ComboBox<OrderStatus> orderStatusComboBox = new ComboBox<>();
    private ProductManager productManager = new ProductManager();
    private DatePicker datePicker = new DatePicker();


    public OrderEditorDialog(BiConsumer<Order, Operation> saveHandler,
                              Consumer<Order> deleteHandler) {
        super("order", saveHandler, deleteHandler);

        //add components
    }

    @Override
    protected void confirmDelete() {

    }
}
