package com.noobug.NooblogRebuild.service;

import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 小王子
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAllByDeleted(Boolean.FALSE ,pageable);
    }
}
