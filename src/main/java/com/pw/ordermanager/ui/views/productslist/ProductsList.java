package com.pw.ordermanager.ui.views.productslist;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.ProductService;
import com.pw.ordermanager.backend.service.UserService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.pw.ordermanager.ui.MainLayout;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.common.AbstractList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "products", layout = MainLayout.class)
@PageTitle("Products List")
public class ProductsList extends AbstractList implements BeforeEnterObserver {

    private Grid<Product> grid;
    private final ProductEditorDialog form = new ProductEditorDialog(
            this::saveProduct, this::deleteProduct);

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    public ProductsList(){
        super("Product");
    }

    @Override
    protected void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);
        grid = new Grid<>();

        grid.setSizeFull();
        grid.addColumn(TemplateRenderer.<Product> of(
                "<div title='[[item.name]]'>[[item.name]]<br><small>[[item.type]]</small></div>")
                .withProperty("name", Product::getName)
                .withProperty("type", Product::getType))
                .setHeader("Name").setResizable(true);
        grid.addComponentColumn(p -> {
            final ComboBox<Seller> sellerComboBox = new ComboBox<>(null, p.getPrices().keySet());
            Label price = new Label();

            sellerComboBox.addValueChangeListener(e -> {
                if(e.getValue() != null){
                    price.setText(" : " + p.getPrices().get(e.getValue()).toString() + " $");
                } else {
                    price.setText("Select a seller.");
                }
            });
            final HorizontalLayout horizontalLayout = new HorizontalLayout(sellerComboBox, price);
            horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            return horizontalLayout;
        }).setHeader("Sellers");
        grid.addColumn(new ComponentRenderer<>(this::createEditButton))
                .setFlexGrow(0);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        container.add(getHeader(), grid);
        add(container);
    }

    @Override
    protected void openEditorDialog() {
        form.open(new Product(SecurityUtils.getCurrentUser().getUser()),
                AbstractEditorDialog.Operation.ADD);
    }

    private Button createEditButton(Product product) {
        Button edit = new Button("Edit", event -> form.open(product,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }

    @Override
    protected void updateView() {
        List<Product> products
                = productService.findProducts(SecurityUtils.getCurrentUser().getUser(), getSearchField().getValue());
        grid.setItems(products);

        if (getSearchField().getValue().length() > 0) {
            getHeader().setText("Search for “"+ getSearchField().getValue() +"”");
        } else {
            getHeader().setText("Products");
        }
    }

    private void saveProduct(Product product,
                              AbstractEditorDialog.Operation operation){
        productService.saveProduct(product);
        userService.refreshUserData();

        Notification.show(
                "Product successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
        updateView();
    }

    private void deleteProduct(Product product) {
        productService.deleteProduct(product);
        userService.refreshUserData();

        Notification.show("Product successfully deleted.", 3000, Notification.Position.BOTTOM_START);
        updateView();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateView();
        }
    }
}
