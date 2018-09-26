package com.pw.ordermanager.ui.views.sellerslist;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.validators.NIPValidator;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

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
