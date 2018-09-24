package com.pw.ordermanager.backend.support;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.Seller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSupportTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldFindProductsBySeller(){
        final Product product1 = entityManager.find(Product.class, 20180001L);
        final Product product2 = entityManager.find(Product.class, 20180002L);
        final Seller seller = entityManager.find(Seller.class, 20180001L);

        final Set<Product> result = ProductSupport.findProductsBySeller(Arrays.asList(product1, product2), seller);

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(product1, product2));
    }

    @Test
    public void shouldFindNoProductsBySeller(){
        final Product product = entityManager.find(Product.class, 20180002L);
        final Seller seller = entityManager.find(Seller.class, 20180003L);

        final Set<Product> result = ProductSupport.findProductsBySeller(Arrays.asList(product), seller);

        assertThat(result, hasSize(0));
    }
}
