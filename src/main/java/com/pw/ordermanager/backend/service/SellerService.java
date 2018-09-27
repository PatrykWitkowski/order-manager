package com.pw.ordermanager.backend.service;

import com.pw.ordermanager.backend.entity.Seller;
import com.pw.ordermanager.backend.entity.User;
import lombok.NonNull;

import java.util.List;

public interface SellerService {

    List<Seller> findSellers(@NonNull User user, String value);

    Seller saveSeller(@NonNull Seller seller);

    void deleteSeller(@NonNull Seller seller);
}
