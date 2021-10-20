package com.pengxh.web.imagecollector.base.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author a203
 */
public class PageInfo<T> {
    /**
     * 结果集
     */
    private List<T> rows;


    /**
     * 总数
     */
    private int total;

    public PageInfo(Page<T> page) {
        this.rows = page.getRecords();
        this.total = new Integer(String.valueOf(page.getTotal()));
    }
}
