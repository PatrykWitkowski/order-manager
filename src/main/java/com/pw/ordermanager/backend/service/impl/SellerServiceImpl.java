package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.dts.UserDts;
import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.jpa.SellerRepository;
import com.pw.ordermanager.backend.service.SellerService;
import com.pw.ordermanager.backend.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    private volatile static SellerServiceImpl instance;

    private SellerServiceImpl() {
    }

    public static SellerServiceImpl getInstance() {
        if (instance == null) {
            synchronized (SellerServiceImpl.class) {
                if (instance == null) {
                    instance = new SellerServiceImpl();
                }
            }
        }

        return instance;
    }

    @Override
    public List<Seller> findAllSellers() {
        final UserDts currentUser = SecurityUtils.getCurrentUser();
        return sellerRepository.findByOwner(currentUser.getUser());
    }
}
