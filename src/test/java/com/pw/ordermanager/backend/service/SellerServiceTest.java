package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.common.UserType;
import com.pw.ordermanager.backend.entity.Seller;
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
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerServiceTest {

    private static final String DB_TEST_USER = "db_test_user";
    private static final String USER_WITHOUT_SELLERS = "UserWithoutSellers";
    private static final String NIP_USER_2 = "123-456-88-18";
    private static final String DB_TEST_SELLER_2 = "db_test seller #2";
    private static final String NIP_USER_1 = "432-981-77-19";
    private static final String NIP_USER_3 = "453-234-32-22";
    private static final String DB_TEST_SELLER_1 = "db_test seller #1";
    private static final String DB_TEST_SELLER_3 = "db_test seller #3";
    private static final String NEW_NIP = "436-214-32-01";
    private static final String NEW_NIP_2 = "436-214-32-02";
    private static final String NEW_NAME = "test seller name";

    @Autowired
    private SellerService testedService;

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
    public void shouldNotFindSellersWhenNoExists(){
        User userWithoutSellers = User.builder().username(USER_WITHOUT_SELLERS)
                .password(USER_WITHOUT_SELLERS).type(UserType.SIMPLE).build();
        userRepository.save(userWithoutSellers);

        final List<Seller> result = testedService.findSellers(userWithoutSellers, null);

        assertThat(result, is(empty()));
    }

    @Test
    public void shouldFindAllSellersWhenExists(){
        final List<Seller> result = testedService.findSellers(user, null);

        assertThat(result, hasSize(3));
    }

    @Test
    public void shouldFilterSellersByNipWhenExists(){
        final List<Seller> result = testedService.findSellers(user, NIP_USER_2);

        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Seller::getNip).findFirst().get(), is(NIP_USER_2));
        assertThat(result.stream().map(Seller::getName).findFirst().get(), is(DB_TEST_SELLER_2));
    }

    @Test
    public void shouldFilterSellersByNameWhenExists(){
        final List<Seller> result = testedService.findSellers(user, "#2");

        assertThat(result, hasSize(1));
        assertThat(result.stream().map(Seller::getNip).findFirst().get(), is(NIP_USER_2));
        assertThat(result.stream().map(Seller::getName).findFirst().get(), is(DB_TEST_SELLER_2));
    }

    @Test
    public void shouldFilterSellersByAddressWhenExists(){
        final List<Seller> result = testedService.findSellers(user, "wroclaw");

        assertThat(result, hasSize(2));
        assertThat(result.stream().map(Seller::getNip).collect(Collectors.toList()), hasItems(NIP_USER_1, NIP_USER_3));
        assertThat(result.stream().map(Seller::getName).collect(Collectors.toList()), hasItems(DB_TEST_SELLER_1, DB_TEST_SELLER_3));
    }

    @Test
    public void shouldSaveSeller(){
        Seller seller = new Seller();
        seller.setNip(NEW_NIP);
        seller.setName(NEW_NAME);
        seller.setOwner(user);

        final Seller result = testedService.saveSeller(seller);

        assertThat(result, is(notNullValue()));
        assertThat(result.getSellerId(), is(notNullValue()));
        assertThat(result.getNip(), is(NEW_NIP));
        assertThat(result.getName(), is(NEW_NAME));
    }

    @Test
    public void shouldDeleteSeller(){
        Seller seller = new Seller();
        seller.setNip(NEW_NIP_2);
        seller.setName(NEW_NAME);
        seller.setOwner(user);
        final Seller savedSeller = testedService.saveSeller(seller);

        testedService.deleteSeller(savedSeller);

        final Seller result = entityManager.find(Seller.class, savedSeller.getSellerId());
        assertThat(result, is(nullValue()));
    }
}
