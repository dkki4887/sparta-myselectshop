package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository; //final 꼭 달아주기... 안달면 repository nullpoint 예외뜬다...

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        // 받아온 DTO를 저장할 Entity 객체로 변환 -> 저장 -> controller에 반환
        //save가 객체 저장 후 해당 객체 반환해줌
        Product product = productRepository.save(new Product(productRequestDto));
        return new ProductResponseDto(product);
    }
}
