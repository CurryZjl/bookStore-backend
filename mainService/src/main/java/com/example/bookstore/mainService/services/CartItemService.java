package com.example.bookstore.mainService.services;

import com.example.bookstore.mainService.dto.CartItemDto;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> findCartItemsByUid(long uid);
    boolean existsByBook_BidAndUid( long bid, long uid);
    boolean deleteCartItemByCid(long cid);

    void deleteCartItemByUidAndBid(Long uid, Long bid);

    boolean saveCartItem(CartItemDto cartItemDto);
    boolean updateCartItemAmount(long cid, long newAmount);
}
