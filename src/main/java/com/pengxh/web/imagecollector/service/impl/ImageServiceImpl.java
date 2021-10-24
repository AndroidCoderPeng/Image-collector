package com.pengxh.web.imagecollector.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}
