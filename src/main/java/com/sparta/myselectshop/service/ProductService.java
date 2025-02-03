package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository; //final 꼭 달아주기... 안달면 repository nullpoint 예외뜬다...
    public static final int MIN_MY_PRICE = 100;


    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        // 받아온 DTO를 저장할 Entity 객체로 변환 -> 저장 -> controller에 반환
        //save가 객체 저장 후 해당 객체 반환해줌
        Product product = productRepository.save(new Product(productRequestDto));
        return new ProductResponseDto(product);
    }

    @Transactional //안걸면 변경감지 안일어나~
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        if(requestDto.getMyprice() < MIN_MY_PRICE)
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "이상으로 설정해주세요.");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
        product.setMyprice(requestDto.getMyprice());

        return new ProductResponseDto(product);
    }
}
