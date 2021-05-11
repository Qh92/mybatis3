package com.qinh.dao;

import com.qinh.entity.Employee;

import java.util.List;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-22:27
 */
public interface EmployeeMapper {

    /**
     * 单个参数（除集合或数组），mybatis不会封装参数
     *
     * @param id
     * @return
     */
    Employee getEmpById(Integer id);

    List<Employee> listEmployees();

}
