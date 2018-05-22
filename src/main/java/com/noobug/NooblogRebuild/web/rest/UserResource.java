package com.noobug.NooblogRebuild.web.rest;

import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小王子
 */
@RestController
@RequestMapping("/api/user/")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    private ResponseEntity<Page<User>> all(Pageable pageable){
        return ResponseEntity.ok(userService.findAllByPage(pageable));
    }
}
