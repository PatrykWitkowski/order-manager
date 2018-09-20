package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.service.SellerService;

import java.util.List;

public class SellerServiceImpl implements SellerService {

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
        return null;
    }
}
