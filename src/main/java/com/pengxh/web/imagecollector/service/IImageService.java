package com.pengxh.web.imagecollector.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 删除图片
     *
     * @param imageId 图片ID
     */
    void deleteImage(String imageId);

    /**
     * 修改图片信息
     *
     * @param imageModel 图片实体
     */
    void updateImage(ImageModel imageModel);

    /**
     * 搜索图片
     *
     * @param keywords 搜索关键字
     * @return {@link ImageDTO}
     */
    ImageDTO searchImage(String keywords);

    /**
     * 图片列表
     *
     * @return {@link List<ImageDTO>}
     */
    List<ImageDTO> selectImageList();

    /**
     * 图片分页列表
     *
     * @param page 分页
     * @return {@link List<ImageDTO>}
     */
    List<ImageDTO> selectImageListPage(Page<ImageDTO> page);

    /**
     * 查询图片详情
     *
     * @param imageId 图片ID
     * @return {@link ImageDTO}
     */
    ImageDTO selectImageDetail(String imageId);
}
