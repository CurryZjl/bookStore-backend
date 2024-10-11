package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.CartItemDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.CartItem;
import com.example.book_store_back_end.repositories.CartItemRepository;
import com.example.book_store_back_end.services.BookService;
import com.example.book_store_back_end.services.CartItemService;
import com.example.book_store_back_end.utils.SessionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final BookService bookService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, BookService bookService) {
        this.cartItemRepository = cartItemRepository;
        this.bookService = bookService;
    }

    @Override
    public List<CartItemDto> findCartItemsByUid(long uid) {
        List<CartItem> cartItems = this.cartItemRepository.findCartItemsByUid(uid);
        return cartItems.stream()
                .map(this::mapToCartItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteCartItemByCid(long cid) {
        try {
            this.cartItemRepository.deleteCartItemByCid(cid);
            return true;
        }
        catch (Exception e){
            System.err.println("Error deleting cartItem: Data integrity violation - " + e.getMessage());
        }
        return false;
    }

    @Transactional
    @Override
    public void deleteCartItemByUidAndBid(Long uid, Long bid){
        this.cartItemRepository.deleteCartItemByUidAndBook_Bid(uid, bid);
    }

    @Transactional
    @Override
    public boolean saveCartItem(CartItemDto cartItemDto) {
        CartItem cartItem = mapToCart(cartItemDto);
        try {
            cartItemRepository.save(cartItem);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving cartItem: Data integrity violation - " + e.getMessage());
        }
        return false;
    }

    @Transactional
    @Override
    public boolean existsByBook_BidAndUid(long bid, long uid) {
        return this.cartItemRepository.existsByBook_BidAndUid(bid, uid);
    }

    @Override
    public boolean updateCartItemAmount(long cid, long newAmount) {
        Optional<CartItem> cartItem = this.cartItemRepository.findById(cid);
        if( cartItem.isPresent() ){
            CartItem cartItem1  = cartItem.get();
            cartItem1.setAmount(newAmount);
            cartItemRepository.save(cartItem1);
            return true;
        }
        return false;
    }
    private  CartItemDto mapToCartItemDto(CartItem cartItem){
        Optional<BookDto> bookDto = this.bookService.findBookByBid(cartItem.getBook().getBid());
        if(bookDto.isEmpty()) return CartItemDto.builder().build();  /* Controls never reach here */
        return CartItemDto.builder()
                .bookDto(bookDto.get())
                .cid(cartItem.getCid())
                .amount(cartItem.getAmount())
                .build();
    }


    private CartItem mapToCart(CartItemDto cartItemDto){
        final long uid = SessionUtils.getCurrentAuthUid();
        return CartItem.builder()
                .book(Book.builder().bid(cartItemDto.getBookDto().getBid()).build())
                .amount(cartItemDto.getAmount())
                .uid(uid)
                .build();
    }
}
