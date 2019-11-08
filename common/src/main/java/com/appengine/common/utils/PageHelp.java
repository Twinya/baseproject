package com.appengine.common.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class PageHelp<T> {

    private long pageNumber;
    private long pageSize;
    private long totalPages;
    private long totalRow;
    private Set<T> content;
    private Object[] obj;


    public PageHelp(long pageNumber, long pageSize, Set<T> content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRow = content.size();
        this.totalPages = (long) Math.ceil(1.0 * totalRow / pageSize);
        this.content = content;
    }

    public PageHelp(long pageNumber, long pageSize, Object[] obj) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRow = obj.length;
        this.totalPages = (long) Math.ceil(1.0 * totalRow / pageSize);
        this.obj = obj;
    }

}
