package com.pw.ordermanager.ui.common;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dialog.Dialog;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Map;

public abstract class AbstractSearchDialog extends Composite<Dialog> {

    @Getter(AccessLevel.PROTECTED)
    private Map<String, String> initValues;

    public void initSearchCriteria(Map<String, String>values){
        this.initValues = values;
    }


}
