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
     * 图片ID
     */
    @TableField("IMAGE_ID")
    private String imageId;

    /**
     * 图片标题
     */
    @TableField("IMAGE_TITLE")
    private String imageTitle;

    /**
     * 图片分类
     */
    @TableField("IMAGE_CATEGORY")
    private String category;

    /**
     * 图片表述
     */
    @TableField("IMAGE_DESCRIPTION")
    private String description;

    /**
     * 小图地址
     */
    @TableField("SMALL_IMAGE_URL")
    private String smallImageUrl;

    /**
     * 大图地址
     */
    @TableField("BIG_IMAGE_URL")
    private String bigImageUrl;

    /**
     * 图片爬取时间
     */
    @TableField("CREATE_TIME")
    private String createTime;
}
