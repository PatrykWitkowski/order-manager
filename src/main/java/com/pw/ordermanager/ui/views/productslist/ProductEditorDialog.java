package com.pw.ordermanager.ui.views.productslist;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.components.SellerManager;
import com.pw.ordermanager.ui.validators.WebsiteValidator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProductEditorDialog extends AbstractEditorDialog<Product> {

    private TextField productName = new TextField();
    private TextField productType = new TextField();
    private TextArea description = new TextArea();
    private TextField productWebsiteUrl = new TextField();
    private volatile boolean productManagerAlreadyAdded = false;
    private Tab sellerTab;
    private SellerManager sellerManager;
    private HashMap<Seller, Double> pricesInProductBeforeChanges;

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
    }

    @Override
    public final void open(Product item, Operation operation) {
        this.pricesInProductBeforeChanges = new HashMap<>(item.getPrices());
        createSellerManager(item);
        super.open(item, operation);
    }

    private void createSellerManager(Product item) {
        if(!productManagerAlreadyAdded){
            sellerManager = new SellerManager(item);
            sellerTab = addNewTab("Sellers", new Div(sellerManager));
            productManagerAlreadyAdded = true;
        } else{
            sellerManager = new SellerManager(item);
            updateTab(sellerTab, new Div(sellerManager));
        }
    }

    private void createWebsite() {
        Button goToWebsiteButton = createGoToWebsiteButton();
        productWebsiteUrl.setLabel("Website");
        productWebsiteUrl.setSuffixComponent(goToWebsiteButton);
        getFormLayout().add(productWebsiteUrl);

        goToWebsiteButton.setEnabled(false);
        final WebsiteValidator websiteValidator = new WebsiteValidator("The website's url is incorrect.");
        productWebsiteUrl.addValueChangeListener(e -> {
           if(StringUtils.isNotBlank(e.getValue())){
               final ValidationResult validationResult = websiteValidator.apply(e.getValue(), null);
               if(validationResult.isError()){
                   goToWebsiteButton.setEnabled(false);
               } else {
                   goToWebsiteButton.setEnabled(true);
               }
           } else {
               goToWebsiteButton.setEnabled(false);
           }
        });

        getBinder().forField(productWebsiteUrl)
                .withValidator(websiteValidator)
                .bind(Product::getProductWebsiteUrl, Product::setProductWebsiteUrl);
    }

    private Button createGoToWebsiteButton() {
        Button goToWebsiteButton = new Button(new Icon(VaadinIcon.BROWSER));
        goToWebsiteButton.addClickListener(e -> {
            String urlToOpen = "window.open($0,'_blank')";
            if(StringUtils.isNotBlank(productWebsiteUrl.getValue())
                    && StringUtils.startsWith(productWebsiteUrl.getValue(), "www.")){
                    urlToOpen = "window.open('http://' + $0,'_blank')";
            }

                getUI().get().getPage()
                    .executeJavaScript(urlToOpen, productWebsiteUrl.getValue());
        });
        return goToWebsiteButton;
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

    @Override
    protected void saveClicked(Operation operation) {
        if(sellerManager.invalidDataInTable()){
            Notification.show("Cannot save. Invalid data in table.", 4000, Notification.Position.MIDDLE);
        } else {
            super.saveClicked(operation);
        }
    }

    @Override
    public void cancelClicked() {
        final boolean equals = Arrays.equals(sellerManager.getCurrentProduct().getPrices().keySet().toArray(),
                pricesInProductBeforeChanges.keySet().toArray());
        if(!equals){
            getCurrentItem().setPrices(pricesInProductBeforeChanges);
        }

        super.cancelClicked();
    }
}
