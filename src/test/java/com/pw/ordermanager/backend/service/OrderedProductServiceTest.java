package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.common.OrderStatus;
import com.pw.ordermanager.backend.entity.*;
import com.pw.ordermanager.backend.jpa.OrderRepository;
import com.pw.ordermanager.backend.jpa.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderedProductServiceTest {

    public static final String ORDER_TEST_TITLE = "order test";
    public static final String TEST_USER = "ordered_product_test_user";
    public static final String ORDER_TEST_2_TITLE = "order test 2";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderedProductService testedService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static Order orderTestOrdered;
    private static Order orderTestPlanned;
    private static User user;
    private static OrderedProduct orderedProduct1;
    private static OrderedProduct orderedProduct2;
    private static boolean alreadyExecuted = false;

    @Before
    public void init(){
        if(!alreadyExecuted){
            user = User.builder().username(TEST_USER).password(TEST_USER).build();
            orderTestPlanned = Order.builder().title(ORDER_TEST_TITLE).status(OrderStatus.PLANNED)
                    .owner(user).orderDate(LocalDate.now()).build();
            orderTestOrdered = Order.builder().title(ORDER_TEST_2_TITLE).status(OrderStatus.ORDERED)
                    .owner(user).orderDate(LocalDate.now()).build();
            orderedProduct1 = OrderedProduct.builder().order(orderTestPlanned).product(entityManager.find(Product.class, 1L))
                    .seller(entityManager.find(Seller.class, 1L)).amount(100L).price(2000.).build();
            orderedProduct2 = OrderedProduct.builder().order(orderTestPlanned).product(entityManager.find(Product.class, 1L))
                    .seller(entityManager.find(Seller.class, 1L)).amount(50L).price(44.).build();

            userRepository.save(user);
            orderRepository.save(orderTestPlanned);
            orderRepository.save(orderTestOrdered);
            testedService.save(orderedProduct1);
            testedService.save(orderedProduct2);
            alreadyExecuted = true;
        }
    }

    @Test
    public void shouldDeleteOrderedProduct(){
        final OrderedProduct orderedProduct = OrderedProduct.builder().order(orderTestPlanned)
                .product(entityManager.find(Product.class, 1L))
                .seller(entityManager.find(Seller.class, 1L)).amount(100L).price(2000.).build();

        testedService.save(orderedProduct);
        testedService.delete(orderedProduct);
    }

    @Test
    public void shouldFindOrderedProductWhenExists(){
        final List<OrderedProduct> result = testedService.findByOrder(orderTestPlanned);

        assertThat(result, hasSize(2));
    }

    @Test
    public void shouldNotFindOrderedProductWhenNotExists(){
        final List<OrderedProduct> result = testedService.findByOrder(orderTestOrdered);

        assertThat(result, hasSize(0));
    }
}
