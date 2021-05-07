package com.qinh.dao;

import com.qinh.entity.Employee;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-07-16:59
 */
public interface EmployeeMapperPlus {


    Employee getEmpById(Integer id);

    Employee getEmpAndDept(Integer id);

    Employee getEmpByIdStep(Integer id);
}
