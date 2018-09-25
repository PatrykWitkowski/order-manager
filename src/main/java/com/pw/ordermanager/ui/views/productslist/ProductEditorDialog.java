package com.pw.ordermanager.ui.views.productslist;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProductEditorDialog extends AbstractEditorDialog<Product> {

    private TextField productName = new TextField();
    private TextField productType = new TextField();
    private TextArea description = new TextArea();
    private TextField productWebsiteUrl = new TextField();
    private ComboBox<Seller> sellers = new ComboBox<>();

    /**
     * Constructs a new instance.
     *
     * @param itemSaver   Callback to save the edited item
     * @param itemDeleter
     */
    protected ProductEditorDialog(BiConsumer<Product, Operation> itemSaver,
                                  Consumer<Product> itemDeleter) {
        super("product", itemSaver, itemDeleter);

        createName();
        createDescription();
        createType();
        createWebsite();
        createSellersComboBox();
    }

    private void createSellersComboBox() {
    }

    private void createWebsite() {
        productWebsiteUrl.setLabel("Website");
        getFormLayout().add(productWebsiteUrl);

        // add validation for url address
        getBinder().forField(productWebsiteUrl)
                .bind(Product::getProductWebsiteUrl, Product::setProductWebsiteUrl);
    }

    private void createDescription() {
        description.setLabel("Description");
        getFormLayout().add(description);

        getBinder().forField(description)
                .bind(Product::getDescription, Product::setDescription);
    }

    private void createType() {
        productType.setLabel("Type");
        productType.setRequired(true);
        getFormLayout().add(productType);

        getBinder().forField(productType)
                .withValidator(new StringLengthValidator(
                        "Type must contain 3 - 20 characters",
                        3, 20))
                .bind(Product::getType, Product::setType);
    }

    private void createName() {
        productName.setLabel("Name");
        productName.setRequired(true);
        getFormLayout().add(productName);

        getBinder().forField(productName)
                .withValidator(new StringLengthValidator(
                        "Name must contain 3 - 12 characters",
                        3, 12))
                .bind(Product::getName,Product::setName);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog("Delete product",
                "Are you sure you want to delete the product: “" + getCurrentItem().getName() + "”?", "");
    }
}
