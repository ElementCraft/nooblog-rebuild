package com.noobug.NooblogRebuild.repository;

import com.noobug.NooblogRebuild.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByStatusNot(int status, Pageable pageable);

    Page<Article> findAllByUserColumnIdAndStatusNot(long id, int status, Pageable pageable);

    Page<Article> findAllByUserColumnUserIdAndStatusNot(long id, int status, Pageable pageable);

    Page<Article> findAllByIsPrivateAndStatusNotOrderByGoodNumberDescCommentNumberDesc(Boolean isPrivate, int status, Pageable pageable);

    Page<Article> findAllByIsPrivateAndStatusNotOrderByGmtCreateDesc(Boolean isPrivate, int status, Pageable pageable);

    Optional<Article> findByIdAndStatusNot(Long articleId, int status);

    Page<Article> findAllByUserColumn_UserAccountAndStatusNot(String account, int status, Pageable pageable);
}
