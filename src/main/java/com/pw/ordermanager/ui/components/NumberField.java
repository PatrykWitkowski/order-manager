package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.support.OrderedProductSupport;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;

public class NumberField extends TextField implements ValueProvider {

    private static final String NUMBER_FIELD_PATTERN = "[0-9]*";

    public NumberField(OrderedProduct orderedProduct, Grid<OrderedProduct> selectedProducts){
        init(orderedProduct);
        numberFieldOnChange(orderedProduct, selectedProducts);
        addUpDownComponent();
    }

    private void init(OrderedProduct orderedProduct) {
        setValue(orderedProduct.getAmount().toString());
        setPattern(NUMBER_FIELD_PATTERN);
        setPreventInvalidInput(true);
        setWidth("7em");
    }

    private void addUpDownComponent() {
        UpButton up = createUpButton();
        DownButton down = createDownButton();
        VerticalLayout upAndDown = new VerticalLayout(up, down);
        upAndDown.setBoxSizing(BoxSizing.CONTENT_BOX);
        upAndDown.setPadding(false);
        setSuffixComponent(new Span(upAndDown));
    }

    private void numberFieldOnChange(OrderedProduct orderedProduct, Grid<OrderedProduct> selectedProducts) {
        addValueChangeListener(e -> {
            calculateNewPrice(orderedProduct);
            ListDataProvider<OrderedProduct> dataProvider = updateGrid(selectedProducts);
            updateTotalPrice(selectedProducts, dataProvider);
        });
    }

    private void calculateNewPrice(OrderedProduct orderedProduct) {
        Double newPrice = orderedProduct.getProduct()
                .getPrices()
                .get(orderedProduct.getSeller()) * Long.parseLong(getValue());
        orderedProduct.setPrice(newPrice);
        orderedProduct.setAmount(Long.parseLong(getValue()));
    }

    private ListDataProvider<OrderedProduct> updateGrid(Grid<OrderedProduct> selectedProducts) {
        ListDataProvider<OrderedProduct> dataProvider
                = (ListDataProvider<OrderedProduct>)selectedProducts.getDataProvider();
        selectedProducts.setItems(dataProvider.getItems());
        return dataProvider;
    }

    private void updateTotalPrice(Grid<OrderedProduct> selectedProducts, ListDataProvider<OrderedProduct> dataProvider) {
        double totalPrice = OrderedProductSupport.calculateTotalPrice(dataProvider.getItems());
        String totalPriceText = OrderedProductSupport.priceFormat(totalPrice);
        selectedProducts.getFooterRows().stream()
                .findFirst()
                .ifPresent(footerRow -> footerRow.getCell(selectedProducts.getColumnByKey("priceColumnKey"))
                        .setText("Total: " + totalPriceText));
    }

    private DownButton createDownButton() {
        DownButton down = new DownButton();
        down.addClickListener(e -> {
            Long oldValue = Long.parseLong(getValue());
            if(oldValue > 0){
                oldValue--;
            }
            String newValue = oldValue.toString();
            setValue(newValue);
        });
        return down;
    }

    private UpButton createUpButton() {
        UpButton up = new UpButton();
        up.addClickListener(e -> {
            Long oldValue = Long.parseLong(getValue());
            oldValue++;
            String newValue = oldValue.toString();
            setValue(newValue);
        });
        return up;
    }

    @Override
    public Object apply(Object o) {
        return this;
    }

    private class UpButton extends Button{
        public UpButton(){
            setIcon(new Icon(VaadinIcon.ANGLE_UP));
            setHeight("1em");
            setWidth("1em");
        }
    }

    private class DownButton extends Button{
        public DownButton(){
            setIcon(new Icon(VaadinIcon.ANGLE_DOWN));
            setHeight("1em");
            setWidth("1em");
        }
    }


}
