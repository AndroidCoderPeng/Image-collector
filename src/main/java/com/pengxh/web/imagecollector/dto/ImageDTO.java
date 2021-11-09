package com.pengxh.web.imagecollector.dto;

import com.pengxh.web.imagecollector.model.ImageModel;
import lombok.Data;

/**
 * @author a203
 */
@Data
public class ImageDTO {
    private Long imageId;
    private String imageTitle;
    private String category;
    private String description;
    private String smallImageUrl;
    private String bigImageUrl;
    private String creatTime;

    public ImageDTO(ImageModel model) {
        this.imageId = model.getImageId();
        this.imageTitle = model.getImageTitle();
        this.category = model.getCategory();
        this.description = model.getDescription();
        this.smallImageUrl = model.getSmallImageUrl();
        this.bigImageUrl = model.getBigImageUrl();
        this.creatTime = model.getCreateTime();
    }
}