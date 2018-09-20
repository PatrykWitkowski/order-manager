package com.pw.ordermanager.ui.common;

import com.vaadin.flow.component.dialog.Dialog;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractSearchDialog<T extends Serializable> extends Dialog {

    @Getter(AccessLevel.PROTECTED)
    private Map<String, String> initValues;

    public void initSearchCriteria(Map<String, String>values){
        this.initValues = values;
    }


}
