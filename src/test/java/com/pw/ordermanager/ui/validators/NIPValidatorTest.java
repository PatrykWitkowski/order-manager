package com.pw.ordermanager.ui.validators;

import com.vaadin.flow.data.binder.ValidationResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
public class NIPValidatorTest {

    public static final String INVALID_NIP_ERROR_MSG = "Invalid NIP.";
    private static NIPValidator nipValidator;

    @BeforeClass
    public static void init(){
        nipValidator = new NIPValidator(INVALID_NIP_ERROR_MSG);
    }

    @Test
    public void testTooShortNip(){
        final ValidationResult result = nipValidator.apply("123456", null);

        assertNip(result, true);
    }

    @Test
    public void testTooLongNip(){
        final ValidationResult result = nipValidator.apply("12345678901", null);

        assertNip(result, true);
    }

    @Test
    public void testOldPatternForPerson(){
        final ValidationResult result = nipValidator.apply("123-456-32-18", null);

        assertNip(result, false);
    }

    @Test
    public void testOldPatternForCompany(){
        final ValidationResult result = nipValidator.apply("123-45-63-218", null);

        assertNip(result, false);
    }

    @Test
    public void testActualPattern(){
        final ValidationResult result = nipValidator.apply("1234563218", null);

        assertNip(result, false);
    }

    @Test
    public void testValidControlSum(){
        final ValidationResult result = nipValidator.apply("123-456-32-18", null);

        assertNip(result, false);
    }

    @Test
    public void testInvalidControlSum(){
        final ValidationResult result = nipValidator.apply("123-456-32-15", null);

        assertNip(result, true);
    }

    @Test
    public void testInvalidNipWithLetters(){
        final ValidationResult result = nipValidator.apply("123-abc-32-18", null);

        assertNip(result, true);
    }

    @Test
    public void testInvalidNipPattern(){
        final ValidationResult result = nipValidator.apply("123-45-632-18", null);

        assertNip(result, true);
    }

    private void assertNip(ValidationResult result, boolean shouldBeError) {
        assertThat(result.isError(), is(shouldBeError));
        if(shouldBeError){
            assertThat(result.getErrorMessage(), is(INVALID_NIP_ERROR_MSG));
        }
    }
}
