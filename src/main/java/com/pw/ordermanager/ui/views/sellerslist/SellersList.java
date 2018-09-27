package com.pw.ordermanager.ui.views.sellerslist;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.SellerService;
import com.pw.ordermanager.backend.service.UserService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import com.pw.ordermanager.ui.MainLayout;
import com.pw.ordermanager.ui.common.AbstractEditorDialog;
import com.pw.ordermanager.ui.common.AbstractList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "sellers", layout = MainLayout.class)
@PageTitle("Sellers List")
public class SellersList extends AbstractList implements BeforeEnterObserver {

    private Grid<Seller> grid;
    private final SellerEditorDialog form = new SellerEditorDialog(
            this::saveSeller, this::deleteSeller);

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    public SellersList() {
        super("Seller");
    }

    @Override
    protected void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);
        grid = new Grid<>();

        grid.setSizeFull();
        grid.addColumn(Seller::getNip).setHeader("NIP");
        grid.addColumn(Seller::getName).setHeader("Name");
        grid.addColumn(TemplateRenderer.<Seller> of(
                "<div title='[[item.location]]'>[[item.street]] [[item.local]]" +
                        "<br><small>[[item.postalCode]] [[item.location]]</small></div>")
                .withProperty("street", s -> s.getAddress().getStreet())
                .withProperty("local", s -> s.getAddress().getLocalNumber())
                .withProperty("postalCode", s ->
                        StringUtils.isBlank(s.getAddress().getPostalCode()) ? s.getAddress().getPostalCode()
                                : s.getAddress().getPostalCode().concat(","))
                .withProperty("location", s -> s.getAddress().getLocation()))
                .setHeader("Address");


        grid.addColumn(new ComponentRenderer<>(this::createEditButton))
                .setFlexGrow(0);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        container.add(getHeader(), grid);
        add(container);
    }

    @Override
    protected void openEditorDialog() {
        form.open(new Seller(SecurityUtils.getCurrentUser().getUser()),
                AbstractEditorDialog.Operation.ADD);
    }

    private Button createEditButton(Seller seller) {
        Button edit = new Button("Edit", event -> form.open(seller,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }

    @Override
    protected void updateView() {
        List<Seller> sellers = sellerService.findSellers(SecurityUtils.getCurrentUser().getUser(), getSearchField().getValue());
        grid.setItems(sellers);

        if (getSearchField().getValue().length() > 0) {
            getHeader().setText("Search for “"+ getSearchField().getValue() +"”");
        } else {
            getHeader().setText("Sellers");
        }
    }

    private void saveSeller(Seller seller,
                             AbstractEditorDialog.Operation operation){
        sellerService.saveSeller(seller);
        userService.refreshUserData();

        Notification.show(
                "Seller successfully " + operation.getNameInText() + "ed.", 3000, Notification.Position.BOTTOM_START);
        updateView();
    }

    private void deleteSeller(Seller seller) {
        sellerService.deleteSeller(seller);
        userService.refreshUserData();

        Notification.show("Seller successfully deleted.", 3000, Notification.Position.BOTTOM_START);
        updateView();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateView();
        }
    }
}
