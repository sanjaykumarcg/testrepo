package io.oasp.application.mtsj.imagemanagement.dataaccess.api;

import static com.querydsl.core.alias.Alias.$;

import org.springframework.data.domain.Page;

import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

import io.oasp.application.mtsj.imagemanagement.common.api.datatype.ContentType;
import io.oasp.application.mtsj.imagemanagement.logic.api.to.ImageSearchCriteriaTo;

/**
 * @author VAPADWAL
 *
 */
public interface ImageRepository extends DefaultRepository<ImageEntity> {
  default Page<ImageEntity> findImages(ImageSearchCriteriaTo criteria) {

    ImageEntity alias = newDslAlias();
    JPAQuery<ImageEntity> query = newDslQuery(alias);

    String name = criteria.getName();
    if ((name != null) && !name.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getName()), name, criteria.getNameOption());
    }
    String content = criteria.getContent();
    if ((content != null) && !content.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getContent()), content, criteria.getContentOption());
    }
    ContentType contentType = criteria.getContentType();
    if (contentType != null) {
      query.where(Alias.$(alias.getContentType()).eq(contentType));
    }
    String mimeType = criteria.getMimeType();
    if ((mimeType != null) && !mimeType.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getMimeType()), mimeType, criteria.getMimeTypeOption());
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, false);
  }

}
