package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.common.OrderStatus;
import com.pw.ordermanager.backend.entity.Order;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    public static final String ORDER_TEST_TITLE = "order test";
    public static final String TEST_USER = "test_user";
    public static final String ORDER_TEST_2_TITLE = "order test 2";
    public static final String TEST_INVALID_USER = "test_invalid_user";

    @Autowired
    private OrderService testedService;

    @Autowired
    private UserRepository userRepository;

    private static Order orderTestOrdered;
    private static Order orderTestPlanned;
    private static User user;
    private static boolean alreadyExecuted = false;
    private static User userWithoutOrders;

    @Before
    public void init(){
        if(!alreadyExecuted){
            user = User.builder().username(TEST_USER).password(TEST_USER).build();
            userWithoutOrders = User.builder().username(TEST_INVALID_USER).password(TEST_INVALID_USER).build();
            orderTestPlanned = Order.builder().title(ORDER_TEST_TITLE).status(OrderStatus.PLANNED)
                    .owner(user).orderDate(LocalDate.now()).build();
            orderTestOrdered = Order.builder().title(ORDER_TEST_2_TITLE).status(OrderStatus.ORDERED)
                    .owner(user).orderDate(LocalDate.now()).build();

            userRepository.save(user);
            userRepository.save(userWithoutOrders);
            testedService.saveOrder(orderTestPlanned);
            testedService.saveOrder(orderTestOrdered);
            alreadyExecuted = true;
        }
    }

    @Test
    public void shouldDeleteOrder(){
        final Order orderCancelled = Order.builder().title(ORDER_TEST_TITLE).status(OrderStatus.CANCELLED)
                .owner(user).orderDate(LocalDate.now()).build();

        testedService.saveOrder(orderCancelled);
        testedService.deleteOrder(orderCancelled);
    }

    @Test
    public void shouldFindOrderByIdWhenExists(){
        final Order result = testedService.findOrderById(1L);

        assertThat(result.getTitle(), is(ORDER_TEST_TITLE));
        assertThat(result.getStatus(), is(OrderStatus.PLANNED));
    }

    @Test
    public void shouldNotFindOrderByIdWhenNotExists(){
        final Order result = testedService.findOrderById(999L);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldFindOrderWhenExists(){
        final List<Order> result = testedService.findOrders(user, null);

        assertThat(result, hasSize(2));
    }

    @Test
    public void shouldNotFindOrderWhenNotExists(){
        final List<Order> result = testedService.findOrders(userWithoutOrders, null);

        assertThat(result, empty());
    }

    @Test
    public void shouldFilterFoundOrderByTitle(){
        final List<Order> result = testedService.findOrders(user, "2");

        assertThat(result, hasSize(1));
        assertThat(result.stream().findFirst().get().getTitle(), is(ORDER_TEST_2_TITLE));
    }

    @Test
    public void shouldFilterFoundOrderByStatus(){
        final List<Order> result = testedService.findOrders(user, "planned");

        assertThat(result, hasSize(1));
        assertThat(result.stream().findFirst().get().getStatus(), is(OrderStatus.PLANNED));
    }
}
