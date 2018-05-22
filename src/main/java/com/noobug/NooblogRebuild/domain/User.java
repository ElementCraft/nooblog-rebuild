package com.noobug.NooblogRebuild.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 帐号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    @JsonIgnore
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     *
     * @see com.noobug.nooblog.consts.UserConst.Sex
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    //private City city;

    /**
     * 签名
     */
    private String signature;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 头像路径
     */
    private String iconPath;

    /**
     * 是否实名认证
     */
    @Column(name = "is_auth")
    private Boolean authenticated;

    /**
     * 是否封禁
     */
    @Column(name = "is_ban")
    private Boolean banned;

    /**
     * 是否公开博客
     */
    @Column(name = "is_public")
    private Boolean isPublic;

    /**
     * 逻辑删除
     */
    @Column(name = "is_deleted")
    private Boolean deleted;
}
