package com.noobug.NooblogRebuild.web.mapper;

import com.noobug.NooblogRebuild.domain.Article;
import com.noobug.NooblogRebuild.web.dto.AddNewArticleDTO;
import com.noobug.NooblogRebuild.web.dto.ArticleListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Article 相关 Mapper
 *
 * @author noobug.com
 */
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {UserColumnMapper.class})
public interface ArticleMapper extends EntityMapper<ArticleListDTO, Article> {

    /**
     * 新增文章DTO转文章实体
     *
     * @param addNewArticleDTO 新增文章DTO
     * @return 文章实体
     */
    @Mapping(target = "userColumn.id", source = "columnId")
    Article addNewArticleDTO2Article(AddNewArticleDTO addNewArticleDTO);

}
