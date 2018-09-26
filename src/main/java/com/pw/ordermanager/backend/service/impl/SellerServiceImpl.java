package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.SellerRepository;
import com.pw.ordermanager.backend.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<Seller> findSellers(User user, String value) {
        return sellerRepository.findByOwner(user);
    }

    @Override
    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    @Override
    public void deleteSeller(Seller seller) {
        sellerRepository.delete(seller);
    }
}
