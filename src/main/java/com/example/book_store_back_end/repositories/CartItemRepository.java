package com.example.book_store_back_end.repositories;

import com.example.book_store_back_end.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.book b WHERE ci.uid = :uid AND b.deleted = false")
    List<CartItem> findCartItemsByUid(long uid);
    boolean existsByBook_BidAndUid( long bid , long uid);
    void deleteCartItemByCid(long cid);

    @Override
    Optional<CartItem> findById(Long aLong);
}
