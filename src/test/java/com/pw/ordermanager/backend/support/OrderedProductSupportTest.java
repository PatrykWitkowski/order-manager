package com.pw.ordermanager.backend.support;

import com.pw.ordermanager.backend.entity.OrderedProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderedProductSupportTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldFormatLongDoubleToPrice(){
        final String result = OrderedProductSupport.priceFormat(2.343545456436);

        assertThat(result, is("2.34"));
    }

    @Test
    public void shouldFormatZeroToPrice(){
        final String result = OrderedProductSupport.priceFormat(0);

        assertThat(result, is("0.00"));
    }

    @Test
    public void shouldIndicateUniqueForNoneProducts(){
        final OrderedProduct orderedProduct = entityManager.find(OrderedProduct.class, 20180001L);

        final boolean result = OrderedProductSupport.checkIfOrderedProductIsUnique(new ArrayList<>(), orderedProduct);

        assertThat(result, is(true));
    }

    @Test
    public void shouldIndicateUniqueForDifferentProducts(){
        final OrderedProduct orderedProduct1 = entityManager.find(OrderedProduct.class, 20180001L);
        final OrderedProduct orderedProduct2 = entityManager.find(OrderedProduct.class, 20180002L);

        final boolean result = OrderedProductSupport
                .checkIfOrderedProductIsUnique(Arrays.asList(orderedProduct1), orderedProduct2);

        assertThat(result, is(true));
    }

    @Test
    public void shouldIndicateNotUniqueForTheSameProducts(){
        final OrderedProduct orderedProduct1 = entityManager.find(OrderedProduct.class, 20180001L);
        final OrderedProduct orderedProduct2 = entityManager.find(OrderedProduct.class, 20180002L);

        final boolean result = OrderedProductSupport
                .checkIfOrderedProductIsUnique(Arrays.asList(orderedProduct1, orderedProduct2), orderedProduct2);

        assertThat(result, is(false));
    }

    @Test
    public void shouldCalculateTotalPrice(){
        final OrderedProduct orderedProduct1 = entityManager.find(OrderedProduct.class, 20180001L);
        final OrderedProduct orderedProduct2 = entityManager.find(OrderedProduct.class, 20180002L);

        final double result = OrderedProductSupport
                .calculateTotalPrice(Arrays.asList(orderedProduct1, orderedProduct2));

        assertThat(result, is(723.31));
    }
}
