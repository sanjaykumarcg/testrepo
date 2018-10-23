package io.oasp.application.mtsj.imagemanagement.logic.impl;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import io.oasp.application.mtsj.general.logic.base.AbstractComponentFacade;
import io.oasp.application.mtsj.imagemanagement.dataaccess.api.ImageEntity;
import io.oasp.application.mtsj.imagemanagement.dataaccess.api.ImageRepository;
import io.oasp.application.mtsj.imagemanagement.logic.api.Imagemanagement;
import io.oasp.application.mtsj.imagemanagement.logic.api.to.ImageEto;
import io.oasp.application.mtsj.imagemanagement.logic.api.to.ImageSearchCriteriaTo;

/**
 * Implementation of component interface of imagemanagement
 */
@Named
public class ImagemanagementImpl extends AbstractComponentFacade implements Imagemanagement {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(ImagemanagementImpl.class);

  /** @see #getImageDao() */
  @Inject
  private ImageRepository imageDao;

  /**
   * The constructor.
   */
  public ImagemanagementImpl() {

    super();
  }

  @Override
  public ImageEto findImage(Long id) {

    LOG.debug("Get Image with id {} from database.", id);
    return getBeanMapper().map(getImageDao().find(id), ImageEto.class);
  }

  @Override
  public Page<ImageEto> findImageEtos(ImageSearchCriteriaTo criteria) {

    // criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    Page<ImageEntity> images = getImageDao().findImages(criteria);
    return mapPaginatedEntityList(images, ImageEto.class);
  }

  @Override
  public boolean deleteImage(Long imageId) {

    ImageEntity image = getImageDao().find(imageId);
    getImageDao().delete(image);
    LOG.debug("The image with id '{}' has been deleted.", imageId);
    return true;
  }

  @Override
  public ImageEto saveImage(ImageEto image) {

    Objects.requireNonNull(image, "image");
    ImageEntity imageEntity = getBeanMapper().map(image, ImageEntity.class);

    // initialize, validate imageEntity here if necessary
    getImageDao().save(imageEntity);
    LOG.debug("Image with id '{}' has been created.", imageEntity.getId());

    return getBeanMapper().map(imageEntity, ImageEto.class);
  }

  /**
   * Returns the field 'imageDao'.
   *
   * @return the {@link ImageDao} instance.
   */
  public ImageRepository getImageDao() {

    return this.imageDao;
  }

}