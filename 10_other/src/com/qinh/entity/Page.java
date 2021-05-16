package com.qinh.entity;

import java.util.List;

/**
 * 封装分页查询数据
 *
 * @author Qh
 * @version 1.0
 * @date 2021-05-15-23:03
 */
public class Page {

    private int start;
    private int end;
    private int count;
    private List<Employee> employees;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
