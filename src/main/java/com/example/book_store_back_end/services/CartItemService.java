package com.example.book_store_back_end.services;

import com.example.book_store_back_end.dto.CartItemDto;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> findCartItemsByUid(long uid);
    boolean existsByBook_BidAndUid( long bid, long uid);
    boolean deleteCartItemByCid(long cid);

    void deleteCartItemByUidAndBid(Long uid, Long bid);

    boolean saveCartItem(CartItemDto cartItemDto);
    boolean updateCartItemAmount(long cid, long newAmount);
}
