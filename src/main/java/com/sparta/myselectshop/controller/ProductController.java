package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //필요한 생성자 자동으로 만들어줌
@RequestMapping("/api") //중복되는 시작 경로 설정해둘 수 있음
public class ProductController {
    private final ProductService productService;

    //모든 처리는 service에서 이루어지도록 하자! Controller는 단지 출입구 역할!
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.createProduct(productRequestDto, userDetails.getUser());
    }

    @PutMapping("/products/{id}")
    //board?page={page}&listSize={listSize} -> @RequestParam (다중 값)
    ///board/{id} -> @PathVariable (단일 값)
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        return productService.updateProduct(id, requestDto);
    }

    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.getProducts(userDetails.getUser(), page - 1, size, sortBy, isAsc);
    }

//    @GetMapping("/admin/products")
//    public List<ProductResponseDto> getAllProducts() {
//        return productService.getAllProducts();
//    }

    @PostMapping("/products/{productId}/folder")
    public void addFolder(@PathVariable Long productId,
                          @RequestParam Long folderId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.addFolder(productId, folderId, userDetails.getUser());
    }

    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(@PathVariable Long folderId,
                                                        @RequestParam int page,
                                                        @RequestParam int size,
                                                        @RequestParam String sortBy,
                                                        @RequestParam boolean isAsc,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.getProductsInFolder(folderId, page-1, size, sortBy, isAsc, userDetails.getUser());
    }

}
