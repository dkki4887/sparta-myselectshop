package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.Exception.RestApiException;
import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/folders") //->인가되어있지 않은 api. 인증 처리가 필수!
    //폴더 이름이 여러개 넘어올거니까 RequestBody
    public void addFolders(@RequestBody FolderRequestDto folderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        folderService.addFolders(folderRequestDto.getFolderNames(), userDetails.getUser());
    }

    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }


//    GlobalExceptionHandler로 처리 할거임! AOP와 마찬가지로 동작하기 때문에 일일히 모든 컨트롤러에 예외처리 안해줘두 됨
//    @ExceptionHandler({IllegalArgumentException.class})
//    //컨트롤러에서 이 오류가 터졌을 때 요 메소드 실행됨
//    //AOP와 동일하게 모든 메소드에 넣어줄 필요 없이 얘가 알아서 실행됨!
//    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
//        System.out.println("FolderController.handleException");
//        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(
//                // HTTP body
//                restApiException,
//                // HTTP status code
//                HttpStatus.BAD_REQUEST
//        );
//    }

}
