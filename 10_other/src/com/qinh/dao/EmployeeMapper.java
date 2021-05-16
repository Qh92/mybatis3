package com.qinh.dao;

import com.qinh.entity.Employee;
import com.qinh.entity.Page;

import java.util.List;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-22:27
 */
public interface EmployeeMapper {
    Employee getEmpById(Integer id);

    List<Employee> getEmps();

    Long addEmp(Employee employee);

    void getPageByProcedure(Page page);
}
