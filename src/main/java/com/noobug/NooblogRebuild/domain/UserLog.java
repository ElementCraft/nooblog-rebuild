package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLog extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String logIp;

}
