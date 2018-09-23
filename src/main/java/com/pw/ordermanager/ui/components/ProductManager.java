package com.pw.ordermanager.ui.components;

import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.OrderedProduct;
import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.support.OrderedProductSupport;
import com.pw.ordermanager.ui.views.dialogs.ProductSearchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.List;
import java.util.stream.Collectors;

@Tag("product-manager")
public class ProductManager extends Component implements HasComponents {

    private ProductSearchBox productLookup;
    private Grid<OrderedProduct> selectedProducts = new Grid<>();
    private Button addProduct = new Button("Add");
    private Button deleteProduct = new Button("Delete");
    private Column<OrderedProduct> prices;
    private Order currentOrder;

    public ProductManager(Order currentItem){
        currentOrder = currentItem;

        productLookup = new ProductSearchBox(new ProductSearchDialog());

        createGrid();
        createColumns();
        calculateTotalPrice(prices);
        createAddProduct();
        createDeleteProduct();

        HorizontalLayout buttonBar = new HorizontalLayout(addProduct, deleteProduct);
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);

        selectedProducts.setWidth("40em");
        VerticalLayout leftSideBar = new VerticalLayout(productLookup, buttonBar);
        leftSideBar.setWidth("40%");
        HorizontalLayout productsLayout = new HorizontalLayout(leftSideBar, selectedProducts);

        add(productsLayout);
    }

    private void createGrid() {
        final List<OrderedProduct> orderedProduct = getCurrentOrder().getOrderedProduct();
        if(orderedProduct != null){
            selectedProducts.setItems(orderedProduct);
        }
        selectedProducts.addSelectionListener(e -> {
           if(e.getAllSelectedItems().isEmpty()){
               deleteProduct.setEnabled(false);
           } else{
               deleteProduct.setEnabled(true);
           }
        });
    }

    private void createColumns() {
        selectedProducts.addColumn(o -> o.getProduct().getCode()).setHeader("Code");
        selectedProducts.addColumn(o -> o.getProduct().getName()).setHeader("Product");
        selectedProducts.addColumn(o -> o.getSeller().getName()).setHeader("Seller");
        selectedProducts.addComponentColumn(o -> new NumberField(o, selectedProducts)).setHeader("Amount");
        prices = selectedProducts.addColumn(o -> formatToCurrencyPrecision(o.getPrice())).setHeader("Price").setKey("priceColumnKey");
    }

    private void createAddProduct() {
        addProduct.getElement().setAttribute("theme", "primary");
        addProduct.addClickListener(e -> {
            if(productLookup.isFilled()){
                OrderedProduct newOrderedProduct= new OrderedProduct();
                final Product productValue = productLookup.getProductSearchField().getValue();
                final Seller sellerValue = productLookup.getSellerSearchField().getValue();
                newOrderedProduct.setProduct(productValue);
                newOrderedProduct.setSeller(sellerValue);
                newOrderedProduct.setAmount(1L);
                newOrderedProduct.setPrice(productValue.getPrices().get(sellerValue));
                newOrderedProduct.setOrder(getCurrentOrder());
                if(OrderedProductSupport.checkIfOrderedProductIsUnique(getCurrentOrder().getOrderedProduct(), newOrderedProduct)){
                    //orderedProductService.save(newOrderedProduct);
                    getCurrentOrder().getOrderedProduct().add(newOrderedProduct);
                    selectedProducts.setItems(getCurrentOrder().getOrderedProduct());
                    calculateTotalPrice(prices);
                } else {
                    Notification.show("This product is already added.", 4000, Notification.Position.MIDDLE);
                }
            } else{
                Notification.show("You need to fill a product and a seller fields.", 4000, Notification.Position.MIDDLE);
            }
        });
    }

    private void createDeleteProduct() {
        deleteProduct.getElement().setAttribute("theme", "error");
        deleteProduct.setEnabled(false);
        deleteProduct.addClickListener(e -> {
            final List<OrderedProduct> orderedProducts = getCurrentOrder().getOrderedProduct().stream()
                    .filter(item -> !selectedProducts.getSelectedItems().contains(item))
                    .collect(Collectors.toList());
            getCurrentOrder().setOrderedProduct(orderedProducts);
            selectedProducts.setItems(orderedProducts);
            calculateTotalPrice(prices);
        });
    }

    private void calculateTotalPrice(Column<OrderedProduct> prices) {
        ListDataProvider<OrderedProduct> dataProvider
                = (ListDataProvider<OrderedProduct>)selectedProducts.getDataProvider();
        double totalPrice = dataProvider.getItems().stream()
                .map(OrderedProduct::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
        getCurrentOrder().setTotalPrice(totalPrice);
        String totalPriceText = formatToCurrencyPrecision(totalPrice);
        if(selectedProducts.getFooterRows().isEmpty()){
            selectedProducts.appendFooterRow().getCell(prices).setText("Total: " + totalPriceText);
        } else {
            selectedProducts.getFooterRows().stream().findFirst().ifPresent(footer ->{
                footer.getCell(selectedProducts.getColumnByKey("priceColumnKey")).setText("Total: " + totalPriceText);
            });
        }
    }

    private String formatToCurrencyPrecision(double totalPrice) {
        return String.format("%.2f", totalPrice);
    }

    public void setCurrentItem(Order item) {
        this.currentOrder = item;
    }

    private Order getCurrentOrder(){
        return currentOrder;
    }
}
