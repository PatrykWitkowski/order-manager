package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.properties.Product_;
import com.pw.ordermanager.backend.entity.properties.Seller_;
import com.pw.ordermanager.backend.service.ProductService;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import org.atmosphere.config.service.Get;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Tag("product-search-box")
public class ProductSearchBox extends Component implements HasComponents, HasSize {

    @Autowired
    private ProductService productService;

    @Getter
    private ComboBox<Product> productSearchField;
    @Getter
    private ComboBox<Seller> sellerSearchField;
    private Button searchButton;

    public ProductSearchBox(AbstractSearchDialog dialog) {
        productSearchField = new ComboBox<>("Product");
        sellerSearchField = new ComboBox<>("Seller");
        searchButton = new Button(new Icon(VaadinIcon.SEARCH));

        searchButton.addClickListener(e -> {
            initDialog(dialog);
            dialog.open();
        });

        HorizontalLayout upperSearchPanel = new HorizontalLayout(productSearchField, searchButton);
        upperSearchPanel.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        add(upperSearchPanel, sellerSearchField);
    }

    @PostConstruct
    public void init(){
        productSearchField.setItems(productService.findAllProducts());
        productSearchField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                sellerSearchField.setItems(e.getValue().getPrices().keySet());
            }
        });

        sellerSearchField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                productSearchField.setItems(productService.findProductsBySeller(e.getValue()));
            }
        });
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