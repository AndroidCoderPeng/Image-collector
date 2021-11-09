package com.pengxh.web.imagecollector.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengxh.web.imagecollector.dto.ImageDTO;
import com.pengxh.web.imagecollector.model.ImageModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author a203
 */
public interface ImageMapper extends BaseMapper<ImageModel> {
    /**
     * 图片分页列表
     *
     * @param page 分页
     * @return {@link List<ImageModel>}
     */
    List<ImageModel> selectImageListPage(@Param("page") Page<ImageDTO> page);
}
