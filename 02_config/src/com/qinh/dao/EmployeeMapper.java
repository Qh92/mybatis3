package com.qinh.dao;

import com.qinh.entity.Employee;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-22:27
 */
public interface EmployeeMapper {
    Employee getEmpById(Integer id);
}
