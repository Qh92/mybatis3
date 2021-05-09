package com.qinh.dao;

import com.qinh.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-09-14:21
 */
public interface EmployeeMapperDynamicSQL {

    List<Employee> listEmpsByConditionIf(Employee employee);

    List<Employee> listEmpsByConditionTrim(Employee employee);

    List<Employee> listEmpsByConditionChoose(Employee employee);

    void updateEmp(Employee employee);

    List<Employee> listEmpsByConditionForeach(@Param("ids") List<Integer> ids);

    void addEmps(@Param("emps") List<Employee> emps);

    List<Employee> listEmpsTestInnerParameter(Employee employee);
}
