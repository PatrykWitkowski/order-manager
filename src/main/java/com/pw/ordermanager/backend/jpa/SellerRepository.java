package com.pw.ordermanager.backend.jpa;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface SellerRepository extends JpaRepository<Seller,Long>, Serializable {

    List<Seller> findByOwner(User user);
}
