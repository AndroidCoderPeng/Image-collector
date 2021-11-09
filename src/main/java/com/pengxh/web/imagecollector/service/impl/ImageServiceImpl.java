package com.pengxh.web.imagecollector.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengxh.web.imagecollector.base.request.RequestValidException;
import com.pengxh.web.imagecollector.dao.ImageMapper;
import com.pengxh.web.imagecollector.dto.ImageDTO;
import com.pengxh.web.imagecollector.model.ImageModel;
import com.pengxh.web.imagecollector.service.IImageService;
import com.pengxh.web.imagecollector.utils.Constant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author a203
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, ImageModel> implements IImageService {

    public ImageServiceImpl() {

    }

    private String obtainAgent() {
        return Constant.USER_AGENT[new Random().nextInt(15)];
    }

    @Override
    public void saveImage() {
        //爬虫抓取数据
        ImageModel image = new ImageModel();
        save(image);
    }

    @Override
    public void deleteImage(String imageId) {
        //不用写SQL，采用mybatis-plus框架操作
        QueryWrapper<ImageModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IMAGE_ID", imageId);
        Assert.isFalse(this.count(queryWrapper) == 0, () -> new RequestValidException("图片不存在，无法删除"));
        this.baseMapper.delete(queryWrapper);
    }

    @Override
    public void updateImage(ImageModel imageModel) {
        //不用写SQL，采用mybatis-plus框架操作
        QueryWrapper<ImageModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IMAGE_ID", imageModel.getImageId());
        Assert.isFalse(this.count(queryWrapper) == 0, () -> new RequestValidException("图片不存在，无法修改"));
        this.baseMapper.updateById(imageModel);
    }

    @Override
    public ImageDTO selectImageDetail(String imageId) {
        //不用写SQL，采用mybatis-plus框架操作
        QueryWrapper<ImageModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IMAGE_ID", imageId);
        ImageModel imageModel = this.baseMapper.selectOne(queryWrapper);
        if (imageModel == null) {
            return null;
        }
        return new ImageDTO(imageModel);
    }

    @Override
    public ImageDTO searchImage(String keywords) {

        return null;
    }

    @Override
    public List<ImageDTO> selectImageList() {
        List<ImageModel> models = list();
        List<ImageDTO> imageList = new ArrayList<>();
        for (ImageModel model : models) {
            imageList.add(new ImageDTO(model));
        }
        return imageList;
    }

    @Override
    public List<ImageDTO> selectImageListPage(Page<ImageDTO> page) {
        List<ImageModel> imageModels = this.baseMapper.selectImageListPage(page);
        List<ImageDTO> imageList = new ArrayList<>();
        for (ImageModel model : imageModels) {
            imageList.add(new ImageDTO(model));
        }
        return imageList;
    }
}
