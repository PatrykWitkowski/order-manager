package com.pw.ordermanager.ui.validators;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NIPValidator extends StringLengthValidator {

    public NIPValidator(String errorMessage) {
        super(errorMessage, 10, 13);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        final ValidationResult validationLengthResult = super.apply(value, context);
        if(oldPatternForPersonMatcher(value) || oldPatternForCompanyMatcher(value) || actualNipPatternMatcher(value)){
            if(validControlSum(value)){
                return validationLengthResult;
            } else {
                return this.toResult(value, false);
            }
        } else {
            return this.toResult(value, false);
        }
    }

    private boolean validControlSum(String value) {
        value = StringUtils.remove(value, '-');
        final int[] ints = value.chars().mapToObj(c -> (char) c).mapToInt(Character::getNumericValue).toArray();
        List<Integer> intsList = Arrays.stream(ints).boxed().collect(Collectors.toList());

        Integer sum = 0;

        sum += intsList.get(0) * 6;
        sum += intsList.get(1) * 5;
        sum += intsList.get(2) * 7;
        sum += intsList.get(3) * 2;
        sum += intsList.get(4) * 3;
        sum += intsList.get(5) * 4;
        sum += intsList.get(6) * 5;
        sum += intsList.get(7) * 6;
        sum += intsList.get(8) * 7;

        Integer controlNumber = sum % 11;

        return Objects.equals(controlNumber, intsList.get(9));

    }

    private boolean oldPatternForPersonMatcher(String value){
        return value.matches("[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}");
    }

    private boolean oldPatternForCompanyMatcher(String value){
        return value.matches("[0-9]{3}-[0-9]{2}-[0-9]{2}-[0-9]{3}");
    }

    private boolean actualNipPatternMatcher(String value){
        return value.matches("[0-9]{10}");
    }
}
