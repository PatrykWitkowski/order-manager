package com.pw.ordermanager.ui.views.sellerslist;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SellerEditorDialog extends AbstractEditorDialog<Seller> {

    /**
     * Constructs a new instance.
     *
     * @param itemSaver   Callback to save the edited item
     * @param itemDeleter
     */
    protected SellerEditorDialog(BiConsumer<Seller, Operation> itemSaver, Consumer<Seller> itemDeleter) {
        super("", itemSaver, itemDeleter);
    }

    @Override
    protected void confirmDelete() {

    }
}
