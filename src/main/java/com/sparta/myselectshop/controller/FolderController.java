package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
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

}
