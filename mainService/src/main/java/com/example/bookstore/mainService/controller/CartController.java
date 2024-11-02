package com.example.bookstore.mainService.controller;

import com.example.bookstore.mainService.dto.BookDto;
import com.example.bookstore.mainService.dto.CartItemDto;
import com.example.bookstore.mainService.dto.ResponseDto;
import com.example.bookstore.mainService.services.CartItemService;
import com.example.bookstore.mainService.utils.SessionUtils;
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
        final long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false, "Auth error", null);
        }
        try{
            List<CartItemDto> cartItemDtos = this.cartItemService.findCartItemsByUid(uid);
            return new ResponseDto<>(true,"GET OK", cartItemDtos);
        }catch (Exception e){
            return new ResponseDto<>(false,"GET ERROR", null);
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
        final  long uid = SessionUtils.getCurrentAuthUid();
        if(uid == -1){
            return new ResponseDto<>(false , "Auth fault", null);
        }
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
