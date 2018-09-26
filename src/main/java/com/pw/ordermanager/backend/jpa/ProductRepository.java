package com.pw.ordermanager.backend.jpa;

import com.pw.ordermanager.backend.entity.Product;
import com.pw.ordermanager.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>, Serializable {

    List<Product> findByOwner(User owner);

}
