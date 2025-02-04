package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    //다중 조건을 걸 때의 메소드명 규칙: 뒤에 In을 붙여준다
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);
    //select * from folder where user_id=? and name in (?, ?, ?, . . . )

    List<Folder> findAllByUser(User user);
}
