package com.pengxh.web.imagecollector.dto;

import com.pengxh.web.imagecollector.model.ImageModel;
import lombok.Data;

/**
 * @author a203
 */
@Data
public class ImageDTO {
    private String imageTitle;
    private String url;
    private String category;
    private String orientation;

    public ImageDTO(ImageModel model) {
        this.imageTitle = model.getImageTitle();
        this.url = model.getUrl();
        this.category = model.getCategory();
        this.orientation = model.getOrientation();
    }
}