package com.noobug.NooblogRebuild.repository;

import com.noobug.NooblogRebuild.domain.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    List<UserLog> findAllByGmtCreateBefore(ZonedDateTime time);
}
