package com.qinh.dao;

import com.qinh.entity.Department;
import com.qinh.entity.Employee;

import java.util.List;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-08-0:09
 */
public interface DepartmentMapper {

    Department getDeptById(Integer id);

    Department getDeptByIdPlus(Integer id);

    Department getDeptByIdStep(Integer id);

    List<Employee> getEmpsByDeptId(Integer deptId);
}
