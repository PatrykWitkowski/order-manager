package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Seller;

import java.util.List;

public interface SellerService {

    List<Seller> findAllSellers();
}
