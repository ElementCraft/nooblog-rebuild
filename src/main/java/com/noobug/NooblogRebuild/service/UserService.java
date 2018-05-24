package com.noobug.NooblogRebuild.service;

import com.noobug.NooblogRebuild.consts.error.UserError;
import com.noobug.NooblogRebuild.domain.Role;
import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.domain.UserColumn;
import com.noobug.NooblogRebuild.domain.UserLog;
import com.noobug.NooblogRebuild.repository.UserColumnRepository;
import com.noobug.NooblogRebuild.repository.UserLogRepository;
import com.noobug.NooblogRebuild.repository.UserRepository;
import com.noobug.NooblogRebuild.security.jwt.TokenProvider;
import com.noobug.NooblogRebuild.tools.entity.Result;
import com.noobug.NooblogRebuild.tools.utils.SecurityUtil;
import com.noobug.NooblogRebuild.web.dto.UserColumnInfoDTO;
import com.noobug.NooblogRebuild.web.dto.UserInfoDTO;
import com.noobug.NooblogRebuild.web.dto.UserLoginDTO;
import com.noobug.NooblogRebuild.web.mapper.UserColumnMapper;
import com.noobug.NooblogRebuild.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.noobug.NooblogRebuild.tools.entity.Result.error;
import static com.noobug.NooblogRebuild.tools.entity.Result.ok;

/**
 * @author 小王子
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private UserColumnRepository userColumnRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserColumnMapper userColumnMapper;

    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAllByDeleted(Boolean.FALSE, pageable);
    }

    /**
     * 登录
     *
     * @param loginDTO   登录信息
     * @param remoteAddr 登录地址
     * @return
     */
    public Result login(UserLoginDTO loginDTO, String remoteAddr) {
        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();

        // 判空
        if (account == null || password == null) {
            return error(UserError.Login.REQUIRE_IS_NULL);
        }

        // 加密密码
        String md5 = securityUtil.md5(loginDTO.getPassword());

        return userRepository.findByAccountAndDeleted(account, Boolean.FALSE)
                .map(user -> {
                    if (user.getPassword().equals(md5)) {
                        List<Role> roles = roleService.findAllByUserId(user.getId());

                        String token = tokenProvider.createToken(account, md5, roles);

                        // 添加登录日志
                        this.addUserLog(user, remoteAddr);
                        return ok(token);
                    } else {
                        return error(UserError.Login.INCORRECT_PASSWORD);
                    }
                })
                .orElse(error(UserError.Login.NOT_EXIST_ACCOUNT));
    }

    /**
     * 添加用户日志
     *
     * @param user       用户
     * @param remoteAddr 远程地址
     * @return
     */
    private UserLog addUserLog(User user, String remoteAddr) {
        return userLogRepository.save(new UserLog(null, user, remoteAddr));
    }

    /**
     * 获取用户资料信息
     *
     * @param account 帐号
     * @return 用户资料DTO
     */
    @Transactional(readOnly = true)
    public Result<?> getUserInfoByAccount(String account) {
        Long currentUserId = securityUtil.getCurrentUserId();

        return userRepository.findByAccountAndDeleted(account, Boolean.FALSE)
                .map(user -> {
                    // 非公开用户只有自己能获取个人信息
                    if (!user.getId().equals(currentUserId) && !user.getIsPublic()) {
                        return error(UserError.PRIVATE);
                    }

                    UserInfoDTO dto = userMapper.user2InfoDTO(user);
                    return ok(dto);
                })
                .orElse(error(UserError.NON_EXIST_ID));
    }

    /**
     * 获取用户一级栏目
     *
     * @param account 帐号
     * @return
     */
    @Transactional(readOnly = true)
    public Result<?> getLv1UserColumn(String account) {
        UserInfoDTO currentUser = securityUtil.getCurrentUser();

        Optional<User> user = userRepository.findByAccountAndDeleted(account, Boolean.FALSE);

        if(!user.isPresent()){
            return error(UserError.NON_EXIST_ID);
        }

        // 非公开用户只有用户自己能获取栏目信息
        if (!account.equals(currentUser.getAccount()) && !user.get().getIsPublic()) {
            return error(UserError.PRIVATE);
        }

        List<UserColumn> userColumns = userColumnRepository.findAllByUserAccountAndIsDefaultAndParentIdIsNullOrParentId(account, Boolean.FALSE, 0);

        return ok(userColumnMapper.userColumns2UserColumnInfoDTOs(userColumns));
    }

    /**
     * 获取用户二级栏目
     *
     * @param parentId 父栏目ID
     * @return
     */
    @Transactional(readOnly = true)
    public Result<?> getLv2UserColumnByParentId(Long parentId) {
        Long currentUserId = securityUtil.getCurrentUserId();

        UserColumn col1 = userColumnRepository.findOne(parentId);
        if(col1 == null){
            return error(UserError.Column.PARENT_IS_NULL);
        }

        User user = col1.getUser();

        // 非公开用户只有用户自己能获取栏目信息
        if(!user.getId().equals(currentUserId) && !user.getIsPublic()){
            return error(UserError.PRIVATE);
        }

        List<UserColumn> userColumns = userColumnRepository.findAllByParentIdAndIsDefault(parentId, Boolean.FALSE);

        return ok(userColumnMapper.userColumns2UserColumnInfoDTOs(userColumns));
    }
}
