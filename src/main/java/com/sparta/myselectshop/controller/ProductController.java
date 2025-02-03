package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor //필요한 생성자 자동으로 만들어줌
@RequestMapping("/api") //중복되는 시작 경로 설정해둘 수 있음
public class ProductController {
    private final ProductService productService;

    //모든 처리는 service에서 이루어지도록 하자! Controller는 단지 출입구 역할!
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }
}
