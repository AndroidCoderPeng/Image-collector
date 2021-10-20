package com.pengxh.web.imagecollector.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author a203
 */
@Data
@TableName("image_info")
public class ImageModel extends Model<ImageModel> {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库主键
     * <p>
     * type = IdType.AUTO 是数据库自增
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片标题
     */
    @TableField("IMAGE_TITLE")
    private String imageTitle;

    /**
     * 图片地址
     */
    @TableField("IMAGE_URL")
    private String url;

    /**
     * 图片分类
     */
    @TableField("CATEGORY")
    private String category;

    /**
     * 横屏/竖屏
     */
    @TableField("ORIENTATION")
    private String orientation;
}
