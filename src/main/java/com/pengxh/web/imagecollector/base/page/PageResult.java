package com.pengxh.web.imagecollector.base.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author a203
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -4071521319254024213L;

    /**
     * 要查找第几页
     */
    private Integer page = 1;
    /**
     * 每页显示多少条
     */
    private Integer pageSize = 20;
    /**
     * 总页数
     */
    private Integer totalPage = 0;
    /**
     * 总记录数
     */
    private Long totalRows = 0L;
    /**
     * 结果集
     */
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(IPage<T> page) {
        this.setRows(page.getRecords());
        this.setTotalRows(page.getTotal());
        this.setPage((int) page.getCurrent());
        this.setPageSize((int) page.getSize());
    }
}
