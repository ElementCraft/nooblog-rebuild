package com.noobug.NooblogRebuild.web.rest;

import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.security.jwt.TokenProvider;
import com.noobug.NooblogRebuild.service.UserService;
import com.noobug.NooblogRebuild.tools.entity.Result;
import com.noobug.NooblogRebuild.tools.utils.UploadUtil;
import com.noobug.NooblogRebuild.web.dto.AddUserColumnDTO;
import com.noobug.NooblogRebuild.web.dto.UserFixInfoDTO;
import com.noobug.NooblogRebuild.web.dto.UserLoginDTO;
import com.noobug.NooblogRebuild.web.dto.UserRegDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

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

    @PutMapping("/info")
    private ResponseEntity<Result> fixInfo(@RequestBody UserFixInfoDTO fixInfoDTO){
        return ok(userService.fixInfo(fixInfoDTO));
    }

    @PostMapping("/login")
    private ResponseEntity<Result> login(@RequestBody UserLoginDTO loginDTO, HttpServletRequest request){
        Result result = userService.login(loginDTO, request.getRemoteAddr());

        return ok(result);
    }

    @PostMapping("/reg")
    private ResponseEntity<Result> reg(@RequestBody UserRegDTO regDTO, HttpServletRequest request){
        Result result = userService.reg(regDTO, request.getRemoteAddr());

        return ok(result);
    }

    @PostMapping("/column/add")
    private ResponseEntity<Result> addColumn(@RequestBody AddUserColumnDTO userColumnDTO){
        Result result = userService.addColumn(userColumnDTO);

        return ok(result);
    }

    @PostMapping("/upload/icon")
    public ResponseEntity<Result> uploadIcon(MultipartFile file){
        Result dto = null;
        UploadUtil.UploadLimit limit = new UploadUtil.UploadLimit();
        //limit.setSavePath("uploads" + File.separator + "icons" + File.separator);
        limit.setMaxSize(1024L * 1024L);
        limit.setAllowExt(new ArrayList<String>(){{
            add("jpg");
            add("jpeg");
            add("png");
            add("bmp");
            add("gif");
            add("webp");
        }});

        dto = UploadUtil.checkAndSave(file, limit);
        return ResponseEntity.ok(dto);
    }
}
