package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.support.ProductSupport;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.pw.ordermanager.ui.common.AbstractSearchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;

@Tag("product-search-box")
public class ProductSearchBox extends Component implements HasComponents, HasSize {

    @Getter
    private ComboBox<Product> productSearchField;
    @Getter
    private ComboBox<Seller> sellerSearchField;
    private Button searchButton;

    public ProductSearchBox(AbstractSearchDialog dialog) {
        createProductSearchField();
        createSellerSearchField();
        createSearchButton(dialog);

        HorizontalLayout upperSearchPanel = new HorizontalLayout(productSearchField, searchButton);
        upperSearchPanel.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        add(upperSearchPanel, sellerSearchField);
    }

    private void createSellerSearchField() {
        sellerSearchField = new ComboBox<>("Seller");
        sellerSearchProductOnFocus();
    }

    private void createProductSearchField() {
        productSearchField = new ComboBox<>("Product");
        productSearchFieldOnFocus();
    }

    private void createSearchButton(AbstractSearchDialog dialog) {
        searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.addClickListener(e -> {
            dialog.open();
        });
    }

    private void sellerSearchProductOnFocus() {
        sellerSearchField.addFocusListener(e -> {
            if(productSearchField.getValue() != null){
                sellerSearchField.setItems(productSearchField.getValue().getPrices().keySet());
            } else {
                final User currentOrderOwner = SecurityUtils.getCurrentUser().getUser();
                    sellerSearchField.setItems(currentOrderOwner.getSellers());
            }
        });
    }

    private void productSearchFieldOnFocus() {
        productSearchField.addFocusListener(e -> {
            final User currentOrderOwner = SecurityUtils.getCurrentUser().getUser();
                if(sellerSearchField.getValue() != null){
                    productSearchField.setItems(ProductSupport.findProductsBySeller(currentOrderOwner.getProducts(),
                            sellerSearchField.getValue()));
                } else {
                    productSearchField.setItems(currentOrderOwner.getProducts());
                }
        });
    }

    public boolean isFilled(){
        return productSearchField.getValue() != null && sellerSearchField.getValue() != null;
    }

}