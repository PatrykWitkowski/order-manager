package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.OrderedProductService;
import com.pw.ordermanager.ui.views.dialogs.ProductSearchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Tag("product-manager")
public class ProductManager extends Component implements HasComponents {

    @Autowired
    private OrderedProductService orderedProductService;

    private ProductSearchBox productLookup;
    private Grid<OrderedProduct> selectedProducts = new Grid<>();
    private Button addProduct = new Button("Add");
    private Button deleteProduct = new Button("Delete");

    public ProductManager(){
        productLookup = new ProductSearchBox(new ProductSearchDialog());

        selectedProducts.addColumn(o -> o.getProduct().getCode()).setHeader("Code");
        selectedProducts.addColumn(o -> o.getProduct().getName()).setHeader("Product");
        selectedProducts.addColumn(o -> o.getSeller().getName()).setHeader("Seller");
        selectedProducts.addComponentColumn(o -> new NumberField(o, selectedProducts)).setHeader("Amount");
        Grid.Column<OrderedProduct> prices = selectedProducts
                .addColumn(o -> o.getPrice()).setHeader("Price").setKey("priceColumnKey");

        ListDataProvider<OrderedProduct> dataProvider
                = (ListDataProvider<OrderedProduct>)selectedProducts.getDataProvider();
        double totalPrice = dataProvider.getItems().stream()
                .map(OrderedProduct::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
        selectedProducts.appendFooterRow().getCell(prices).setText("Total: " + totalPrice);

        deleteProduct.setEnabled(false);
        selectedProducts.addSelectionListener(e -> {
           if(e.getAllSelectedItems().isEmpty()){
               deleteProduct.setEnabled(false);
           } else{
               deleteProduct.setEnabled(true);
           }
        });

        HorizontalLayout buttonBar = new HorizontalLayout(addProduct, deleteProduct);
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);

        selectedProducts.setWidth("40em");
        VerticalLayout leftSideBar = new VerticalLayout(productLookup, buttonBar);
        leftSideBar.setWidth("40%");
        HorizontalLayout productsLayout = new HorizontalLayout(leftSideBar, selectedProducts);

        add(productsLayout);
    }

    @PostConstruct
    public void init(){
        selectedProducts.setItems(orderedProductService.findOrderedProduct());
        deleteProduct.addClickListener(e -> {
            selectedProducts.getSelectedItems().stream().forEach(item -> orderedProductService.delete(item));
            selectedProducts.getDataProvider().refreshAll();
        });
        addProduct.addClickListener(e -> {
            if(productLookup.isFilled()){
                OrderedProduct newOrderedProduct= new OrderedProduct();
                final Product productValue = productLookup.getProductSearchField().getValue();
                final Seller sellerValue = productLookup.getSellerSearchField().getValue();
                newOrderedProduct.setProduct(productValue);
                newOrderedProduct.setSeller(sellerValue);
                newOrderedProduct.setAmount(1L);
                newOrderedProduct.setPrice(productValue.getPrices().get(sellerValue));
                orderedProductService.save(newOrderedProduct);
                selectedProducts.setItems(orderedProductService.findOrderedProduct());
            }
        });
    }
}
