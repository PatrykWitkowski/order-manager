package com.pw.ordermanager.ui.views.sellerslist;

import com.pw.ordermanager.backend.entity.Address;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.validators.NIPValidator;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SellerEditorDialog extends AbstractEditorDialog<Seller> {

    private TextField nipField = new TextField();
    private TextField sellerName = new TextField();
    private TextArea description = new TextArea();
    private TextField addressStreet = new TextField();
    private TextField addressNumber = new TextField();
    private TextField addressPostalCode = new TextField();
    private TextField addressLocation = new TextField();

    /**
     * Constructs a new instance.
     *
     * @param itemSaver   Callback to save the edited item
     * @param itemDeleter
     */
    protected SellerEditorDialog(BiConsumer<Seller, Operation> itemSaver, Consumer<Seller> itemDeleter) {
        super("seller", itemSaver, itemDeleter);

        createNipField();
        createSellerName();
        createDescription();
        createAddress();
    }

    private void createAddress() {
        Div address = new Div();
        address.setText("Address");

        HorizontalLayout addressFirstRow = new HorizontalLayout();
        addressFirstRow.setWidth("100%");
        addressStreet.setLabel("Street");
        addressNumber.setLabel("Local number");
        addressFirstRow.expand(addressStreet);
        addressFirstRow.setFlexGrow(0.5, addressNumber);
        addressFirstRow.add(addressStreet, addressNumber);

        HorizontalLayout addressSecondRow = new HorizontalLayout();
        addressSecondRow.setWidth("100%");
        addressPostalCode.setLabel("Postal Code");
        addressLocation.setLabel("Location");
        addressSecondRow.expand(addressPostalCode);
        addressSecondRow.setFlexGrow(2, addressLocation);
        addressSecondRow.add(addressPostalCode, addressLocation);

        address.add(addressFirstRow, addressSecondRow);
        getFormLayout().add(address);
        getBinder().forField(addressStreet)
                .withConverter(new Converter<String, Address>() {
                    @Override
                    public Result<Address> convertToModel(String s, ValueContext valueContext) {
                        Address address = getCurrentItem().getAddress();
                        address.setStreet(s);
                        return Result.ok(address);
                    }

                    @Override
                    public String convertToPresentation(Address address, ValueContext valueContext) {
                        return address.getStreet() == null ? "" : address.getStreet();
                    }
                })
                .bind(Seller::getAddress, Seller::setAddress);

        getBinder().forField(addressNumber)
                .withConverter(new Converter<String, Address>() {
                    @Override
                    public Result<Address> convertToModel(String s, ValueContext valueContext) {
                        Address address = getCurrentItem().getAddress();
                        address.setLocalNumber(s);
                        return Result.ok(address);
                    }

                    @Override
                    public String convertToPresentation(Address address, ValueContext valueContext) {
                        return address.getLocalNumber() == null ? "" : address.getLocalNumber();
                    }
                })
                .bind(Seller::getAddress, Seller::setAddress);

        getBinder().forField(addressPostalCode)
                .withValidator(new RegexpValidator("Invalid postal code format.", "([0-9]{2}-[0-9]{3})?"))
                .withConverter(new Converter<String, Address>() {
                    @Override
                    public Result<Address> convertToModel(String s, ValueContext valueContext) {
                        Address address = getCurrentItem().getAddress();
                        address.setPostalCode(s);
                        return Result.ok(address);
                    }

                    @Override
                    public String convertToPresentation(Address address, ValueContext valueContext) {
                        return address.getPostalCode() == null ? "" : address.getPostalCode();
                    }
                })
                .bind(Seller::getAddress, Seller::setAddress);

        getBinder().forField(addressLocation)
                .withConverter(new Converter<String, Address>() {
                    @Override
                    public Result<Address> convertToModel(String s, ValueContext valueContext) {
                        Address address = getCurrentItem().getAddress();
                        address.setLocation(s);
                        return Result.ok(address);
                    }

                    @Override
                    public String convertToPresentation(Address address, ValueContext valueContext) {
                        return address.getLocation() == null ? "" : address.getLocation();
                    }
                })
                .bind(Seller::getAddress, Seller::setAddress);

    }

    private void createDescription() {
        description.setLabel("Description");
        getFormLayout().add(description);

        getBinder().forField(description)
                .bind(Seller::getDescription, Seller::setDescription);
    }

    private void createSellerName() {
        sellerName.setLabel("Name");
        sellerName.setRequired(true);
        getFormLayout().add(sellerName);

        getBinder().forField(sellerName)
                .withValidator(new StringLengthValidator("Seller's name must contain 3 - 20 chracters",
                        3, 20))
                .bind(Seller::getName, Seller::setName);
    }

    private void createNipField() {
        nipField.setLabel("NIP");
        nipField.setRequired(true);
        getFormLayout().add(nipField);

        getBinder().forField(nipField)
                .withValidator(new NIPValidator("Invalid NIP format or control sum."))
                .bind(Seller::getNip, Seller::setNip);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog("Delete seller",
                "Are you sure you want to delete the seller: “" + getCurrentItem().getName() + "”?", "");

    }
}
