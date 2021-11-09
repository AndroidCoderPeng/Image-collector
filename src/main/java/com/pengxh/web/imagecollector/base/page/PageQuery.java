package com.pengxh.web.imagecollector.base.page;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class PageQuery {
    /**
     * 每页的条数
     */
    private Integer pageSize;

    /**
     * 页编码(第几页)
     */
    private Integer pageNo;

    /**
     * 排序方式(asc 或者 desc)
     */
    private String sort;

    /**
     * 排序的字段名称
     */
    private String orderByField;

    public PageQuery() {
    }

    public PageQuery(Integer pageSize, Integer pageNo, String sort, String orderByField) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.sort = sort;
        this.orderByField = orderByField;
    }
}
