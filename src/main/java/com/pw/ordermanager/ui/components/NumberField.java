package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.OrderedProduct;
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

    public NumberField(OrderedProduct orderedProduct, Grid<OrderedProduct> selectedProducts){
        setValue(orderedProduct.getAmount().toString());
        setPattern("[0-9]*");
        setPreventInvalidInput(true);

        addValueChangeListener(e -> {
            Double newPrice = orderedProduct.getProduct()
                    .getPrices()
                    .get(orderedProduct.getSeller()) * Long.parseLong(getValue());
            orderedProduct.setPrice(newPrice);
            orderedProduct.setAmount(Long.parseLong(getValue()));
            selectedProducts.setItems(orderedProduct);

            ListDataProvider<OrderedProduct> dataProvider
                    = (ListDataProvider<OrderedProduct>)selectedProducts.getDataProvider();
            double totalPrice = dataProvider.getItems().stream()
                    .map(OrderedProduct::getPrice)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            selectedProducts.getFooterRows().stream().findFirst().ifPresent(footerRow -> {
                footerRow.getCell(selectedProducts.getColumnByKey("priceColumnKey")).setText("Total: " + totalPrice);
            });
        });

        UpButton up = new UpButton();
        up.addClickListener(e -> {
            Long oldValue = Long.parseLong(getValue());
            oldValue++;
            String newValue = oldValue.toString();
            setValue(newValue);
        });

        DownButton down = new DownButton();
        down.addClickListener(e -> {
            Long oldValue = Long.parseLong(getValue());
            if(oldValue > 0){
                oldValue--;
            }
            String newValue = oldValue.toString();
            setValue(newValue);
        });

        VerticalLayout upAndDown = new VerticalLayout(up, down);
        upAndDown.setBoxSizing(BoxSizing.CONTENT_BOX);
        upAndDown.setPadding(false);

        setSuffixComponent(new Span(upAndDown));
        setWidth("7em");
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