package com.pw.ordermanager.ui.validators;

import com.vaadin.flow.data.validator.RegexpValidator;

public class WebsiteValidator extends RegexpValidator {
    private static final String PATTERN = "((https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]\\.[^\\s]{2,})|(http|https):\\/\\/[a-z]{2}\\.[a-zA-Z0-9]+\\.[^\\s]{2,})?";

    public WebsiteValidator(String errorMessage) {
        super(errorMessage, PATTERN, true);
    }
}
