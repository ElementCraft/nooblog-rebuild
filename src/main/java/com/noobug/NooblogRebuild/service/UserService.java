package com.noobug.NooblogRebuild.service;

import com.noobug.NooblogRebuild.consts.RedisKey;
import com.noobug.NooblogRebuild.consts.UserConst;
import com.noobug.NooblogRebuild.consts.error.PublicError;
import com.noobug.NooblogRebuild.consts.error.UserError;
import com.noobug.NooblogRebuild.domain.*;
import com.noobug.NooblogRebuild.redis.RedisBase;
import com.noobug.NooblogRebuild.repository.UserColumnRepository;
import com.noobug.NooblogRebuild.repository.UserLogRepository;
import com.noobug.NooblogRebuild.repository.UserRepository;
import com.noobug.NooblogRebuild.security.jwt.TokenProvider;
import com.noobug.NooblogRebuild.tools.entity.Result;
import com.noobug.NooblogRebuild.tools.utils.SecurityUtil;
import com.noobug.NooblogRebuild.tools.utils.ValidateUtil;
import com.noobug.NooblogRebuild.web.dto.*;
import com.noobug.NooblogRebuild.web.mapper.UserColumnMapper;
import com.noobug.NooblogRebuild.web.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.noobug.NooblogRebuild.tools.entity.Result.error;
import static com.noobug.NooblogRebuild.tools.entity.Result.ok;

/**
 * @author 小王子
 */
@Slf4j
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

    @Autowired
    private RedisBase<String, String> redis;

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

        if (!user.isPresent()) {
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
        if (col1 == null) {
            return error(UserError.Column.PARENT_IS_NULL);
        }

        User user = col1.getUser();

        // 非公开用户只有用户自己能获取栏目信息
        if (!user.getId().equals(currentUserId) && !user.getIsPublic()) {
            return error(UserError.PRIVATE);
        }

        List<UserColumn> userColumns = userColumnRepository.findAllByParentIdAndIsDefault(parentId, Boolean.FALSE);

        return ok(userColumnMapper.userColumns2UserColumnInfoDTOs(userColumns));
    }

    /**
     * 设置默认角色
     *
     * @param user 用户实体
     */
    private void setDefaultRole(User user) throws Exception {
        Assert.notNull(user, "[设置默认用户角色] NPE");
        Assert.notNull(user.getId(), "[设置默认用户角色] ID为空");
        roleService.findOneByCode(UserConst.DEFAULT_ROLE)
                .map(role -> {
                    return roleService.saveUserRole(new UserRole(null, user.getId(), role.getId()));
                })
                .orElseThrow(() -> new Exception("[设置默认用户角色] 数据库无该角色code"));
    }

    /**
     * 注册
     *
     * @param regDTO     注册信息
     * @param remoteAddr IP
     * @return
     */
    public Result reg(UserRegDTO regDTO, String remoteAddr) {
        String redisKey = RedisKey.of(RedisKey.USER_REG_LOCK, remoteAddr);

        if (redis.get(redisKey) != null) {
            return error(UserError.Reg.TOO_FREQUENTLY);
        }

        String account = regDTO.getAccount();
        String email = regDTO.getEmail();
        String password = regDTO.getPassword();
        String nickName = regDTO.getNickName();

        if (account == null || password == null || email == null || nickName == null) {
            return error(UserError.Reg.REQUIRE_IS_NULL);
        }

        // 注册参数合法性判断
        if (!ValidateUtil.lengthBetween(account, UserConst.Limit.LEN_ACCOUNT_MIN, UserConst.Limit.LEN_ACCOUNT_MAX)) {
            return Result.error(UserError.Reg.ACCOUNT_LENGTH);
        } else if (userRepository.findByAccountAndDeleted(account, Boolean.FALSE).isPresent()) {
            return Result.error(UserError.Reg.EXISTED_ACCOUNT);
        } else if (ValidateUtil.existChinese(account)) {
            return Result.error(UserError.Reg.ACCOUNT_EXIST_CHINESE);
        } else if (ValidateUtil.existSpace(account)) {
            return Result.error(UserError.Reg.ACCOUNT_EXIST_SPACE);
        } else if (ValidateUtil.allNumber(account)) {
            return Result.error(UserError.Reg.ACCOUNT_ALL_NUMBER);
        } else if (!ValidateUtil.lengthBetween(password, UserConst.Limit.LEN_PASSWORD_MIN, UserConst.Limit.LEN_PASSWORD_MAX)) {
            return Result.error(UserError.Reg.PASSWORD_LENGTH);
        } else if (ValidateUtil.existSpace(password)) {
            return Result.error(UserError.Reg.PASSWORD_EXIST_SPACE);
        } else if (!ValidateUtil.lengthBetween(nickName, UserConst.Limit.LEN_NICKNAME_MIN, UserConst.Limit.LEN_NICKNAME_MAX)) {
            return Result.error(UserError.Reg.NICKNAME_LENGTH);
        } else if (ValidateUtil.allSpace(nickName)) {
            return Result.error(UserError.Reg.NICKNAME_ALL_SPACE);
        } else if (!ValidateUtil.isEmail(email)) {
            return Result.error(UserError.Reg.EMAIL_INVALID);
        }

        // 加密密码
        String md5 = securityUtil.md5(account);
        regDTO.setPassword(md5);

        // 填充默认字段
        User user = userMapper.regDTO2User(regDTO);
        user.setAuthenticated(Boolean.FALSE);
        user.setBanned(Boolean.FALSE);
        user.setDeleted(Boolean.FALSE);
        user.setIsPublic(Boolean.TRUE);
        user.setScore(0);
        user.setSex(UserConst.Sex.UNKNOWN);

        // 注册用户信息存入数据库
        user = userRepository.save(user);

        // 设置为用户角色
        try {
            setDefaultRole(user);

        } catch (Exception e) {
            log.error("[新用户注册] 设置默认角色失败", e);
        }

        redis.setex(redisKey, String.valueOf(System.currentTimeMillis()), 30);

        log.info("[新用户注册] {}", user);
        return ok();
    }

    /**
     * 修改用户个人信息
     *
     * @param fixInfoDTO 修改的信息
     * @return
     */
    public Result fixInfo(UserFixInfoDTO fixInfoDTO) {
        Long currentUserId = securityUtil.getCurrentUserId();

        Integer sex = fixInfoDTO.getSex();
        String nickName = fixInfoDTO.getNickName();
        String signature = fixInfoDTO.getSignature();
        String iconPath = fixInfoDTO.getIconPath();

        if(nickName == null || sex == null){
            return error(PublicError.REQUIRE_IS_NULL);
        }

        // 判断传入参数的有效性
        if (!ValidateUtil.lengthBetween(nickName, UserConst.Limit.LEN_NICKNAME_MIN, UserConst.Limit.LEN_NICKNAME_MAX)) {
            return Result.error(UserError.Reg.NICKNAME_LENGTH);
        } else if (signature != null && signature.length() > UserConst.Limit.LEN_SIGNATURE_MAX) {
            return Result.error(UserError.Info.SIGNATURE_TOO_LONG);
        } else if (!UserConst.Sex.ALL.contains(sex)) {
            return Result.error(UserError.Info.UNKNOWN_SEX_TYPE);
        } else {
            if(iconPath != null){
                File file = new File(iconPath);
                if (!file.exists()) {
                    return Result.error(UserError.Info.UNKNOWN_ICON_PATH);
                }
            }

            User dbUser = userRepository.findOne(currentUserId);
            dbUser.setIsPublic(fixInfoDTO.getIsPublic());
            dbUser.setSex(sex);
            dbUser.setSignature(signature);
            dbUser.setNickName(nickName);
            dbUser.setIconPath(iconPath);
            userRepository.save(dbUser);
        }

        return ok();
    }

    /**
     * 用户新增栏目
     *
     * @param columnDTO 栏目DTO
     * @return
     */
    public Result addColumn(AddUserColumnDTO columnDTO) {
        Long currentUserId = securityUtil.getCurrentUserId();
        User user = userRepository.findOne(currentUserId);

        String title = columnDTO.getTitle().trim();

        if (title.isEmpty()) {
            return Result.error(UserError.Column.TITLE_IS_NULL);
        } else if (title.length() > UserConst.Limit.LEN_COLUMN_TITLE_MAX) {
            return Result.error(UserError.Column.TITLE_TOO_LONG);
        } else {
            Long parentId = columnDTO.getParentId();

            // 判断是否一级栏目
            if (parentId == null || parentId == 0L) {

                // 判断同级栏目标题是否重复
                Long count = userColumnRepository.countByUserIdAndTitle(currentUserId, title);
                if (count > 0) {
                    return Result.error(UserError.Column.DUPLICATE_TITLE);
                }
            } else {
                UserColumn col= userColumnRepository.findOne(parentId);
                // 为空说明指定的父级栏目不存在
                if (col == null) {
                    return Result.error(UserError.Column.PARENT_IS_NULL);
                } if (!col.getUser().getId().equals(currentUserId)) {
                    return Result.error(UserError.Column.PARENT_NO_OWN);
                } else {
                    // 判断指定的父级栏目是不是一级栏目
                    if (col.getParentId() != null && col.getParentId() > 0) {
                        return Result.error(UserError.Column.PARENT_NO_LEVEL1);
                    } else {
                        // 判断父级栏目下是否有重名栏目
                        Long count = userColumnRepository.countByParentIdAndTitle(parentId, title);
                        if (count > 0) {
                            return Result.error(UserError.Column.DUPLICATE_TITLE);
                        }
                    }
                }
            }

            UserColumn resultColumn = userColumnMapper.toEntity(columnDTO);
            resultColumn.setIsDefault(Boolean.FALSE);
            resultColumn.setUser(user);
            resultColumn.setSortLevel(0);
            userColumnRepository.save(resultColumn);
            return Result.ok();
        }
    }
}
