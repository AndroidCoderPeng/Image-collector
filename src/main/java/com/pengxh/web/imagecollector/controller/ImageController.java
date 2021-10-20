package com.pengxh.web.imagecollector.controller;

import com.pengxh.web.imagecollector.base.BaseController;
import com.pengxh.web.imagecollector.base.response.ResponseData;
import com.pengxh.web.imagecollector.dto.ImageDTO;
import com.pengxh.web.imagecollector.service.IImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 数据列表
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public Object list() {
        List<ImageDTO> imageList = imageService.selectImageList();
        return ResponseData.success(imageList);
    }
}
