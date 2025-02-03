package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto { //클라이언트에 넘겨주는 정보
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();
    }
}