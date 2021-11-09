package com.pengxh.web.imagecollector.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengxh.web.imagecollector.base.BaseController;
import com.pengxh.web.imagecollector.base.page.PageFactory;
import com.pengxh.web.imagecollector.base.response.ResponseData;
import com.pengxh.web.imagecollector.dto.ImageDTO;
import com.pengxh.web.imagecollector.model.ImageModel;
import com.pengxh.web.imagecollector.service.IImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author a203
 */
@Controller
@RequestMapping("/image")
public class ImageController extends BaseController {

    private final IImageService imageService;

    public ImageController(IImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * 模糊检索图片
     */
    @GetMapping(value = "/search")
    @ResponseBody
    public Object search(String keywords) {
        ImageDTO imageDTO = imageService.searchImage(keywords);
        return ResponseData.success(imageDTO);
    }

    /**
     * 删除图片
     */
    @GetMapping(value = "/deleteImage")
    @ResponseBody
    public Object deleteImage(String imageId) {
        imageService.deleteImage(imageId);
        return ResponseData.success();
    }

    /**
     * 修改图片信息
     */
    @GetMapping(value = "/updateImage")
    @ResponseBody
    public Object updateImage(@RequestBody ImageModel imageModel) {
        imageService.updateImage(imageModel);
        return ResponseData.success();
    }

    /**
     * 图片列表
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public Object list() {
        List<ImageDTO> imageList = imageService.selectImageList();
        return ResponseData.success(imageList);
    }

    /**
     * 图片列表
     */
    @GetMapping(value = "/listPage")
    @ResponseBody
    public Object listPage() {
        Page<ImageDTO> page = PageFactory.defaultPage();
        List<ImageDTO> imageList = imageService.selectImageListPage(page);
        page.setRecords(imageList);
        return ResponseData.success(super.packForBT(page));
    }

    /**
     * 删除图片
     */
    @GetMapping(value = "/imageDetail")
    @ResponseBody
    public Object imageDetail(String imageId) {
        ImageDTO imageDTO = imageService.selectImageDetail(imageId);
        return ResponseData.success(imageDTO);
    }
}
