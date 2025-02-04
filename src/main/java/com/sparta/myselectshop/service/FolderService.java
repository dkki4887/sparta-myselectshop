package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        //회원이 생성해둔 폴더와 새로 생성할 폴더가 중복되는지 검사
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);
        List<Folder> newFolderList = new ArrayList<>();

        for(String name : folderNames) {
            if(!isExistFolderName(name, existFolderList))
                newFolderList.add(new Folder(name, user));
            else
                throw new IllegalArgumentException("중복된 폴더명을 제거해주세요! 폴더명: " + name);
        }
        folderRepository.saveAll(newFolderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        return folderRepository.findAllByUser(user).stream()
                .map(folder -> new FolderResponseDto(folder))
                .collect(Collectors.toList());
    }

    private boolean isExistFolderName(String name, List<Folder> existFolderList) {
        for(Folder folder : existFolderList) {
            if(folder.getName().equals(name))
                return true;
        }
        return false;
    }
}
