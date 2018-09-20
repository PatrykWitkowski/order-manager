package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.OrderedProductService;
import com.pw.ordermanager.backend.service.impl.OrderedProductServiceImpl;
import com.pw.ordermanager.ui.views.dialogs.ProductSearchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

@Tag("product-manager")
public class ProductManager extends Component implements HasComponents {

    private OrderedProductService orderedProductService;

    private ProductSearchBox productLookup;
    private Grid<OrderedProduct> selectedProducts = new Grid<>();
    private Button addProduct = new Button("Add");
    private Button deleteProduct = new Button("Delete");

    public ProductManager(){
        //cannot autowired because it is in component which sie create by new
        orderedProductService = OrderedProductServiceImpl.getInstance();

        productLookup = new ProductSearchBox(new ProductSearchDialog());

        selectedProducts.addColumn(o -> o.getProduct().getCode()).setHeader("Code");
        selectedProducts.addColumn(o -> o.getProduct().getName()).setHeader("Product");
        selectedProducts.addColumn(o -> o.getSeller().getName()).setHeader("Seller");
        selectedProducts.addComponentColumn(o -> new NumberField(o, selectedProducts)).setHeader("Amount");
        Grid.Column<OrderedProduct> prices = selectedProducts
                .addColumn(o -> String.format("%.2f", o.getPrice()))
                .setHeader("Price").setKey("priceColumnKey");

        calculateTotalPrice(prices);

        deleteProduct.setEnabled(false);
        selectedProducts.addSelectionListener(e -> {
           if(e.getAllSelectedItems().isEmpty()){
               deleteProduct.setEnabled(false);
           } else{
               deleteProduct.setEnabled(true);
           }
        });

        selectedProducts.setItems(orderedProductService.findOrderedProducts());
        deleteProduct.addClickListener(e -> {
            selectedProducts.getSelectedItems().stream().forEach(item -> orderedProductService.delete(item));
            selectedProducts.setItems(orderedProductService.findOrderedProducts());
            calculateTotalPrice(prices);
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
                if(orderedProductService.checkIfOrderedProductIsUnique(newOrderedProduct)){
                    orderedProductService.save(newOrderedProduct);
                    selectedProducts.setItems(orderedProductService.findOrderedProducts());
                    calculateTotalPrice(prices);
                }
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

    private void calculateTotalPrice(Grid.Column<OrderedProduct> prices) {
        ListDataProvider<OrderedProduct> dataProvider
                = (ListDataProvider<OrderedProduct>)selectedProducts.getDataProvider();
        double totalPrice = dataProvider.getItems().stream()
                .map(OrderedProduct::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
        String totalPriceText = String.format("%.2f", totalPrice);
        if(selectedProducts.getFooterRows().isEmpty()){
            selectedProducts.appendFooterRow().getCell(prices).setText("Total: " + totalPriceText);
        } else {
            selectedProducts.getFooterRows().stream().findFirst().ifPresent(footer ->{
                footer.getCell(selectedProducts.getColumnByKey("priceColumnKey")).setText("Total: " + totalPriceText);
            });
        }
    }

}
