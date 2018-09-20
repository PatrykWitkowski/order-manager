package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.ProductService;
import com.pw.ordermanager.backend.service.SellerService;
import com.pw.ordermanager.backend.service.impl.ProductServiceImpl;
import com.pw.ordermanager.backend.service.impl.SellerServiceImpl;
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

import java.util.HashMap;
import java.util.Map;

@Tag("product-search-box")
public class ProductSearchBox extends Component implements HasComponents, HasSize {

    private ProductService productService;
    private SellerService sellerService;

    @Getter
    private ComboBox<Product> productSearchField;
    @Getter
    private ComboBox<Seller> sellerSearchField;
    private Button searchButton;

    public ProductSearchBox(AbstractSearchDialog dialog) {
        //cannot autowired because it is in component which is created by new
        productService = ProductServiceImpl.getInstance();
        sellerService = SellerServiceImpl.getInstance();

        productSearchField = new ComboBox<>("Product");
        sellerSearchField = new ComboBox<>("Seller");
        searchButton = new Button(new Icon(VaadinIcon.SEARCH));

        searchButton.addClickListener(e -> {
            initDialog(dialog);
            dialog.open();
        });

        productSearchField.addFocusListener(e -> {
            if(sellerSearchField.getValue() != null){
                productSearchField.setItems(productService.findProductsBySeller(sellerSearchField.getValue()));
            } else {
                productSearchField.setItems(productService.findAllProducts());
            }
        });

        sellerSearchField.addFocusListener(e -> {
            if(productSearchField.getValue() != null){
                sellerSearchField.setItems(productSearchField.getValue().getPrices().keySet());
            } else {
                //sellerSearchField.setItems(sellerService.findAllSellers());
            }
        });

        HorizontalLayout upperSearchPanel = new HorizontalLayout(productSearchField, searchButton);
        upperSearchPanel.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        add(upperSearchPanel, sellerSearchField);
    }

    public boolean isFilled(){
        return productSearchField.getValue() != null && sellerSearchField.getValue() != null;
    }

    private void initDialog(AbstractSearchDialog dialog) {
        Map<String, String> searchCriteria = new HashMap<>();
        //searchCriteria.put(Product_.CODE, productSearchField.getValue().getCode());
        //searchCriteria.put(Product_.NAME, productSearchField.getValue().getName());
        //searchCriteria.put(Seller_.NAME, sellerSearchField.getValue().getName());
        dialog.initSearchCriteria(searchCriteria);
    }

    public void setSearchDialogResult(String productCode, String sellerCode){
        // find product by code and set
        //productSearchField.setValue();

        // find seller by code and set
        //sellerSearchField.setValue();
    }
}