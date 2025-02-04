package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor //필요한 생성자 자동으로 만들어줌
@RequestMapping("/api") //중복되는 시작 경로 설정해둘 수 있음
public class ProductController {
    private final ProductService productService;
//    private final ApiUseTimeRepository apiUseTimeRepository;

    //모든 처리는 service에서 이루어지도록 하자! Controller는 단지 출입구 역할!
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 측정 시작 시간
//        long startTime = System.currentTimeMillis();

//        try {
//            // 응답 보내기
//            return productService.createProduct(requestDto, userDetails.getUser());
//        } finally {
//            // 측정 종료 시간
//            long endTime = System.currentTimeMillis();
//            // 수행시간 = 종료 시간 - 시작 시간
//            long runTime = endTime - startTime;
//
//            // 로그인 회원 정보
//            User loginUser = userDetails.getUser();
//
//            // API 사용시간 및 DB 에 기록
//            ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser)
//                    .orElse(null);
//            if (apiUseTime == null) {
//                // 로그인 회원의 기록이 없으면
//                apiUseTime = new ApiUseTime(loginUser, runTime);
//            } else {
//                // 로그인 회원의 기록이 이미 있으면
//                apiUseTime.addUseTime(runTime);
//            }
//
//            System.out.println("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
//            apiUseTimeRepository.save(apiUseTime);
//        }
        /* 이 기능은 부가적인 기능. 이런 식으로 핵심 기능에 부가 기능을 붙이는 식으로 구현을 하면
        추후 수정이 일어났을 때 부가기능이 붙어있는 모든 메소드를 수정해야하고, 부가기능이 추가되었을 때도
        모든 메소드에 추가를 해줘야하는 불편함이 존재...
        -> 이런 부가기능들을 핵심 기능과 분리하고 모듈화하면 편하다. 그걸 해주는게 Spring AOP

        @Aspect: 스프링 bean 클래스에만 적용 가능, AOP 설정 적용할 것이라고 알려주는 것.

        Advice: 만든 부가기능을 핵심 기능의 어느 타이밍에 수행할 것인지 정하는 것
        ex) 핵심기능 전(@Around)/후(@After)/모두(@Around), 핵심기능 오류 시(@AfterThrowing), 핵심 기능의 반환 값 사용 시 등등...

        @PointCut: 부가 기능을 어디에 수행할지 정하는 것
        */


        return productService.createProduct(requestDto, userDetails.getUser());
    }


    //board?page={page}&listSize={listSize} -> @RequestParam (다중 값)
    ///board/{id} -> @PathVariable (단일 값)
    @PutMapping("/products/{id}")
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
