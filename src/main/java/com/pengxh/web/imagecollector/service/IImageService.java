package com.pengxh.web.imagecollector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pengxh.web.imagecollector.dto.ImageDTO;
import com.pengxh.web.imagecollector.model.ImageModel;

import java.util.List;

/**
 * @author a203
 */
public interface IImageService extends IService<ImageModel> {

    /**
     * 保存图片
     */
    void saveImage();

    /**
     * 图片列表
     *
     * @return List<ImageDTO>
     */
    List<ImageDTO> selectImageList();
}