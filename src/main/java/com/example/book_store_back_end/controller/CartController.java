package com.example.book_store_back_end.controller;

import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.CartItemDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.services.BookService;
import com.example.book_store_back_end.services.CartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseDto<List<CartItemDto>> getCartBooks(){
        //从数据库获取所有书籍信息
        final long uid = 1L;
        List<CartItemDto> cartItemDtos = this.cartItemService.findCartItemsByUid(uid);
        if(!cartItemDtos.isEmpty()){
            return new ResponseDto<>(true,"GET OK", cartItemDtos);
        }
        else {
            return new ResponseDto<>(false, "No CartBooks", cartItemDtos);
        }
    }

    @PatchMapping("/{cid}")
    public ResponseDto<Long> updateCartItemAmount(@PathVariable("cid") long cid, @RequestBody long newAmount){
        boolean isUpdated = this.cartItemService.updateCartItemAmount(cid, newAmount);
        if(isUpdated){
            return new ResponseDto<>(true, "成功改变书籍数量为%d".formatted(newAmount), newAmount);
        }
        else {
            return new ResponseDto<>(false,"更改书籍数量失败", newAmount);
        }
    }

    @PutMapping
    public ResponseDto<CartItemDto> addNewCartItemAmount(@RequestBody long bid){
        final  long uid = 1L; //TODO
        boolean res = this.cartItemService.existsByBook_BidAndUid(bid, uid);
        if(!res){
            BookDto bookDto = BookDto.builder().bid(bid).build();
            CartItemDto cartItemDto =  CartItemDto.builder().amount(1L).bookDto(bookDto).build();
            this.cartItemService.saveCartItem(cartItemDto);
            return new ResponseDto<>(true,"添加成功",cartItemDto);
        }
        else{
            return new ResponseDto<>(false , "购物车中已有本书，不能重复添加", null);
        }
    }

    @DeleteMapping("/{cid}")
    public ResponseDto<Long> deleteCartItem(@PathVariable long cid){
        try {
            cartItemService.deleteCartItemByCid(cid);
        } catch (Exception e){
            return new ResponseDto<>(false , "删除错误", null);
        }
        return new ResponseDto<>(true, "成功删除购物车项目", cid);
    }

}
