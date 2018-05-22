package com.noobug.NooblogRebuild.repository;

import com.noobug.NooblogRebuild.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findOneByUserIdAndArticleId(Long userId, Long articleId);
}
