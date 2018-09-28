package com.pw.ordermanager.ui.validators;

import com.vaadin.flow.data.binder.ValidationResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
public class WebsiteValidatorTest {

    public static final String INVALID_WEBSITE_URL_ERROR_MSG = "Invalid website's url.";
    private static WebsiteValidator websiteValidator;

    @BeforeClass
    public static void init(){
        websiteValidator = new WebsiteValidator(INVALID_WEBSITE_URL_ERROR_MSG);
    }

    @Test
    public void testUrlStartsWithHttp(){
        final ValidationResult result = websiteValidator.apply("http://www.wikipedia.org", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlStartsWithHttps(){
        final ValidationResult result = websiteValidator.apply("https://www.wikipedia.org", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlStartsWithWWW(){
        final ValidationResult result = websiteValidator.apply("www.wikipedia.org", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlStartsWithoutWWW(){
        final ValidationResult result = websiteValidator.apply("https://wikipedia.org", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlWithLocalPrefixBeforeWebsitesName(){
        final ValidationResult result = websiteValidator.apply("https://pl.wikipedia.org", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlEndsWithLocale(){
        final ValidationResult result = websiteValidator.apply("https://www.google.pl", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlEndsWithRelativePath(){
        final ValidationResult result = websiteValidator.apply("https://wikipedia.org/wiki/Unit_testing", null);

        assertWebsitesUrl(result, false);
    }

    @Test
    public void testUrlEndsWithInvalidLocale(){
        final ValidationResult result = websiteValidator.apply("https://www.google.", null);

        assertWebsitesUrl(result, true);
    }

    @Test
    public void testUrlStartsWithInvalidPrefix(){
        final ValidationResult result = websiteValidator.apply("zzz.google.com", null);

        assertWebsitesUrl(result, true);
    }

    private void assertWebsitesUrl(ValidationResult result, boolean shouldBeError) {
        assertThat(result.isError(), is(shouldBeError));
        if(shouldBeError){
            assertThat(result.getErrorMessage(), is(INVALID_WEBSITE_URL_ERROR_MSG));
        }
    }
}
