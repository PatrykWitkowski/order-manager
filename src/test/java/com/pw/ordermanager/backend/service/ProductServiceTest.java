package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.common.UserType;
import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    private static final String USER_WITHOUT_PRODUCTS = "UserWithoutProducts";
    private static final String DB_TEST_USER = "db_test_user";
    private static final String PRODUCT_SERVICE_TEST_NAME = "test_name";
    private static final String PRODUCT_SERVICE_TEST_TYPE = "test_type";
    private static final String TEST_1 = "Test 1";
    private static final String DB_TYPE_1 = "db_type 1";
    private static final String TEST_2 = "Test 2";
    private static final String DB_TYPE_2 = "db_type 2";
    private static final String DB_TEST_SELLER_3 = "db_test seller #3";

    @Autowired
    private ProductService testedService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @Before
    public void init(){
        user = userRepository.findByUsername(DB_TEST_USER);
    }

    @Test
    public void shouldNotFoundProductWhenNoExists(){
        User userWithoutProducts = User.builder().username(USER_WITHOUT_PRODUCTS)
                .password(USER_WITHOUT_PRODUCTS).type(UserType.SIMPLE).build();
        userRepository.save(userWithoutProducts);

        final List<Product> result = testedService.findProducts(userWithoutProducts, null);

        assertThat(result, is(empty()));
    }

    @Test
    public void shouldFoundProductWhenExists(){
        final List<Product> result = testedService.findProducts(user, null);

        assertThat(result, hasSize(2));
    }

    @Test
    public void shouldFilterProductByNameWhenExists(){
        final List<Product> result = testedService.findProducts(user, "1");

        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Product::getName).findFirst().get(), is(TEST_1));
        assertThat(result.stream().map(Product::getType).findFirst().get(), is(DB_TYPE_1));
    }

    @Test
    public void shouldFilterProductByTypeWhenExists(){
        final List<Product> result = testedService.findProducts(user, DB_TYPE_2);

        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Product::getName).findFirst().get(), is(TEST_2));
        assertThat(result.stream().map(Product::getType).findFirst().get(), is(DB_TYPE_2));
    }

    @Test
    public void shouldFilterProductBySellerNameWhenExists(){
        final List<Product> result = testedService.findProducts(user, DB_TEST_SELLER_3);

        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Product::getName).findFirst().get(), is(TEST_1));
        assertThat(result.stream().map(Product::getType).findFirst().get(), is(DB_TYPE_1));
    }

    @Test
    public void shouldSaveProduct(){
        Product product = new Product();
        product.setName(PRODUCT_SERVICE_TEST_NAME);
        product.setType(PRODUCT_SERVICE_TEST_TYPE);
        product.setOwner(userRepository.findByUsername(DB_TEST_USER));

        final Product result = testedService.saveProduct(product);

        assertThat(result, is(notNullValue()));
        assertThat(result.getProductId(), is(notNullValue()));
        assertThat(result.getName(), is(PRODUCT_SERVICE_TEST_NAME));
        assertThat(result.getType(), is(PRODUCT_SERVICE_TEST_TYPE));
    }

    @Test
    public void shouldDeleteProduct(){
        Product product = new Product();
        product.setName(PRODUCT_SERVICE_TEST_NAME);
        product.setType(PRODUCT_SERVICE_TEST_TYPE);
        product.setOwner(userRepository.findByUsername(DB_TEST_USER));
        final Product savedProduct = testedService.saveProduct(product);

        testedService.deleteProduct(product);

        final Product result = entityManager.find(Product.class, savedProduct.getProductId());
        assertThat(result, is(nullValue()));
    }
}
