package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.support.OrderedProductSupport;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Tag("seller-manager")
public class SellerManager extends Component implements HasComponents {

    private static final String MONEY_PATTERN = "^[0-9]+((\\.|,)[0-9]{1,2})?$";

    private ComboBox<Seller> sellersComboBox = new ComboBox<>("Sellers");
    private TextField price = new TextField("Price");
    private Grid<Seller> selectedSellers = new Grid<>();
    private Button addSeller = new Button("Add");
    private Button deleteSeller = new Button("Delete");
    @Getter
    private Product currentProduct;
    private boolean invalidDataInTable = false;

    public SellerManager(Product product){
        currentProduct = product;

        createSellersComboBox();
        createPrice();
        createGrid();
        createColumns();
        createAddSeller();
        createDeleteSeller();
        updateSellers();

        HorizontalLayout buttonBar = new HorizontalLayout(addSeller, deleteSeller);
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);

        selectedSellers.setWidth("40em");
        VerticalLayout leftSideBar = new VerticalLayout(sellersComboBox, price, buttonBar);
        leftSideBar.setWidth("40%");
        HorizontalLayout productsLayout = new HorizontalLayout(leftSideBar, selectedSellers);

        add(productsLayout);
    }

    private void createPrice() {
        price.setPattern(MONEY_PATTERN);
        price.setSuffixComponent(new Span("$"));
    }

    private void createSellersComboBox() {
        sellersComboBox.setItems(SecurityUtils.getCurrentUser().getUser().getSellers());
    }

    private void createDeleteSeller() {
        deleteSeller.getElement().setAttribute("theme", "error");
        deleteSeller.setEnabled(false);
        deleteSeller.addClickListener(e -> {
            selectedSellers.getSelectedItems().stream()
                    .forEach(item -> getCurrentProduct().getPrices().remove(item));
            updateSellers();
        });
    }

    private void updateSellers() {
        selectedSellers.setItems(getCurrentProduct().getPrices().keySet());
    }

    private void createAddSeller() {
        addSeller.getElement().setAttribute("theme", "primary");
        addSeller.addClickListener(e -> {
            if(price.isInvalid()){
                Notification.show("Wrong input format for the price.", 4000, Notification.Position.MIDDLE);
            } else {
                if(sellersComboBox.getValue() != null && StringUtils.isNotBlank(price.getValue())){
                    if(!getCurrentProduct().getPrices().keySet().contains(sellersComboBox.getValue())){
                        getCurrentProduct().getPrices().put(sellersComboBox.getValue(), Double.parseDouble(formatMoneyWithCommaToDot(price.getValue())));
                        updateSellers();
                    } else {
                        Notification.show("A product with this seller already exists.", 4000, Notification.Position.MIDDLE);
                    }
                } else {
                    Notification.show("You need to fill a seller and a price fields.", 4000, Notification.Position.MIDDLE);
                }
            }
        });
    }

    private void createColumns() {
        addNipColumn();
        addNameColumn();
        addAddressColumn();
        addPriceColumn();
    }

    private void addPriceColumn() {
        selectedSellers.addComponentColumn(seller -> {
            TextField priceField = new TextField();
            priceField.setWidth("8em");
            if(!getCurrentProduct().getPrices().isEmpty() && getCurrentProduct().getPrices().get(seller) != null){
                    priceField.setValue(OrderedProductSupport.priceFormat(getCurrentProduct().getPrices().get(seller)));
            }
            priceField.setPattern(MONEY_PATTERN);
            priceField.addValueChangeListener(e -> {
               if(e.getValue() != null){
                   if(priceField.isInvalid()){
                       invalidDataInTable = true;
                       Notification.show("Wrong input format for the price.", 4000, Notification.Position.MIDDLE);
                   } else {
                       invalidDataInTable = false;
                       getCurrentProduct().getPrices().replace(seller, Double.parseDouble(formatMoneyWithCommaToDot(e.getValue())));
                   }
               }
            });

            return priceField;
        }).setHeader("Price");
    }

    private void addAddressColumn() {
        selectedSellers.addColumn(TemplateRenderer.<Seller>of(
                "<div title='[[item.location]]'>[[item.street]] [[item.local]]" +
                        "<br><small>[[item.postalCode]], [[item.location]]</small></div>")
                .withProperty("street", s -> s.getAddress().getStreet())
                .withProperty("local", s -> s.getAddress().getLocalNumber())
                .withProperty("postalCode", s -> s.getAddress().getPostalCode())
                .withProperty("location", s -> s.getAddress().getLocation()))
                .setHeader("Address");
    }

    private void addNameColumn() {
        selectedSellers.addColumn(seller -> {
            if(!getCurrentProduct().getPrices().isEmpty()) {
                final Optional<Seller> optionalSeller = getCurrentProduct().getPrices().keySet().stream().filter(item -> Objects.equals(item, seller))
                        .findFirst();
                if(optionalSeller.isPresent()){
                    return seller.getName();
                }
            }
            return null;
        }).setHeader("Name");
    }

    private void addNipColumn() {
        selectedSellers.addColumn(seller -> {
            if(!getCurrentProduct().getPrices().isEmpty()) {
                final Optional<Seller> optionalSeller = getCurrentProduct().getPrices().keySet().stream().filter(item -> Objects.equals(item, seller))
                        .findFirst();
                if(optionalSeller.isPresent()){
                    return seller.getNip();
                }
            }
            return null;
            }).setHeader("NIP");
    }

    private void createGrid() {
        final List<Seller> sellers = SecurityUtils.getCurrentUser().getUser().getSellers();
        if(sellers != null){
            selectedSellers.setItems(sellers);
        }
        selectedSellers.addSelectionListener(e -> {
            if(e.getAllSelectedItems().isEmpty()){
                deleteSeller.setEnabled(false);
            } else{
                deleteSeller.setEnabled(true);
            }
        });
    }

    private String formatMoneyWithCommaToDot(String money){
        return StringUtils.replace(money, ",", ".");
    }

    public boolean invalidDataInTable(){
        return invalidDataInTable;
    }
}
