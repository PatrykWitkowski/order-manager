package com.pw.ordermanager.ui.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractList extends VerticalLayout {

    private final TextField searchField;
    private final H2 header;
    private String listName;

    protected AbstractList(String listName){
        this.listName = listName;
        searchField = new TextField("", "Search " + StringUtils.lowerCase(listName));
        header = new H2(listName);

        initView();

        addSearchBar();
        addContent();
    }

    protected abstract void addContent();

    private void initView() {
        addClassName("categories-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

    protected void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        Button newButton = new Button("New " + StringUtils.lowerCase(listName) , new Icon("lumo", "plus"));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> openEditorDialog());

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    protected abstract void openEditorDialog();

    protected abstract void updateView();
}
