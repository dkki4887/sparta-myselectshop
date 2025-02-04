package com.sparta.myselectshop.service;

import com.sparta.myselectshop.Exception.ProductNotFoundException;
import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository; //final 꼭 달아주기... 안달면 repository nullpoint 예외뜬다...
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;

    private final MessageSource messageSource;

    public static final int MIN_MY_PRICE = 100;


    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user) {
        // 받아온 DTO를 저장할 Entity 객체로 변환 -> 저장 -> controller에 반환
        //save가 객체 저장 후 해당 객체 반환해줌
        Product product = productRepository.save(new Product(productRequestDto, user));
        return new ProductResponseDto(product);
    }

    @Transactional //안걸면 변경감지 안일어나~
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        if(requestDto.getMyprice() < MIN_MY_PRICE) {
//            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "이상으로 설정해주세요.");

            //messages.properties 사용
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "below.min.my.price",
                            new Integer[]{MIN_MY_PRICE},
                            "Wrong Price",
                            Locale.getDefault()
                    )
            );
        }

//        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        //커스텀한 ProductNotFoundException 사용
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        messageSource.getMessage(
                                "not.found.product",
                                null,
                                "Not Found Product",
                                Locale.getDefault()
                        )
                ));
//        product.update(requestDto);
        product.setMyprice(requestDto.getMyprice());

        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    //폴더의 정보도 같이 전달해주어야 함
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = getPageable(page, size, sortBy, isAsc);

        Page<Product> products;
        //유저 권한 확인해서 관리자면 상품 전부!
        if(user.getRole() == UserRoleEnum.USER)
            products = productRepository.findAllByUser(user, pageable);
        else
            products = productRepository.findAll(pageable);

        return products.map(ProductResponseDto::new);
    }

    @Transactional //안걸면 변경감지 안일어나~
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));

//        product.updateByItemDto(itemDto);
        product.setLprice(itemDto.getLprice());
    }

//    public List<ProductResponseDto> getAllProducts() {
//        //For문을 사용한 getProducts
////        List<Product> productList = productRepository.findAll();
////
////        List<ProductResponseDto> responseDto = new ArrayList<>();
////        for(Product product : productList)
////            responseDto.add(new ProductResponseDto(product));
////
////        return responseDto;
//
//        //stream을 사용한 getProducts
//        return productRepository.findAll().stream()
//                .map(product -> new ProductResponseDto(product)).collect(Collectors.toList());
//    }

    public void addFolder(Long productId, Long folderId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 폴더입니다."));

        if(!product.getUser().getId().equals(user.getId()) || !folder.getUser().getId().equals(user.getId()))
            throw new IllegalArgumentException("회원님의 관심 상품이 아니거나, 회원님의 폴더가 아닙니다.");

        //폴더에 이미 해당 상품이 등록되어 있는지 중복 확인이 필요함!
        if(productFolderRepository.findByProductAndFolder(product, folder).isPresent())
            throw new IllegalArgumentException("중복된 폴더입니다.");

        productFolderRepository.save(new ProductFolder(product, folder));
    }

    public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size, String sortBy, boolean isAsc, User user) {
        Pageable pageable = getPageable(page, size, sortBy, isAsc);
        Page<Product> productList = productRepository.findAllByUserAndProductFolderList_FolderId(user, folderId, pageable);

        return productList.map(ProductResponseDto::new);
    }

    public Pageable getPageable(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }
}
