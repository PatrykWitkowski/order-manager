package com.pw.ordermanager.backend.service.impl;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.User;
import com.pw.ordermanager.backend.jpa.SellerRepository;
import com.pw.ordermanager.backend.service.SellerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public List<Seller> findSellers(User user, String value) {
        List<Seller> sellers = sellerRepository.findByOwner(user);

        if(StringUtils.isNotBlank(value)){
            List<Seller> filteredSellers = sellers.stream()
                    .filter(s -> StringUtils.containsIgnoreCase(s.getNip(), value))
                    .collect(Collectors.toList());

            if(filteredSellers.isEmpty()){
                filteredSellers = sellers.stream()
                        .filter(s -> StringUtils.containsIgnoreCase(s.getName(), value))
                        .collect(Collectors.toList());

                if(filteredSellers.isEmpty()){
                    filteredSellers = sellers.stream()
                            .filter(s -> StringUtils.containsIgnoreCase(s.getAddress().getLocation(), value))
                            .collect(Collectors.toList());
                }
            }
            sellers = filteredSellers;
        }

        return sellers;
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
