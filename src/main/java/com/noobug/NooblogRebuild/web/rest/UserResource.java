package com.noobug.NooblogRebuild.web.rest;

import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.security.jwt.TokenProvider;
import com.noobug.NooblogRebuild.service.UserService;
import com.noobug.NooblogRebuild.tools.entity.Result;
import com.noobug.NooblogRebuild.web.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

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
        return ok(userService.findAllByPage(pageable));
    }

    @GetMapping("/col1/{account}")
    private ResponseEntity<Result> col1(@PathVariable String account){
        return ok(userService.getLv1UserColumn(account));
    }

    @GetMapping("/col2/{parentId}")
    private ResponseEntity<Result> col2(@PathVariable Long parentId){
        return ok(userService.getLv2UserColumnByParentId(parentId));
    }

    @GetMapping("/info/{account}")
    private ResponseEntity<Result> info(@PathVariable String account){
        return ok(userService.getUserInfoByAccount(account));
    }

    @PostMapping("/login")
    private ResponseEntity<Result> login(@RequestBody UserLoginDTO loginDTO, HttpServletRequest request){
        Result result = userService.login(loginDTO, request.getRemoteAddr());

        return ok(result);
    }


}
