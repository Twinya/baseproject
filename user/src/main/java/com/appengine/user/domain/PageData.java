package com.appengine.user.domain;

import lombok.ToString;

import java.util.List;

/**
 * Created by you on 2017/9/21.
 */
@ToString
public abstract class PageData<T> {

    /**
     * content : [{"id":1,"name":"name"},{"id":2,"name":"name2"}]
     * first : true
     * last : true
     * number : 0
     * numberOfElements : 2
     * size : 100
     * sort : [{"ascending":true,"descending":false,"direction":"ASC","ignoreCase":false,"nullHandling":"NATIVE","property":"id"}]
     * totalElements : 2
     * totalPages : 1
     */
    private boolean empty;
    private boolean first;
    private boolean last;
    private int number;
    private int numberOfElements;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> content;

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

}
