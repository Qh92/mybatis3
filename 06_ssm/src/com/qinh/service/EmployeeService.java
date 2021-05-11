package com.qinh.service;

import com.qinh.dao.EmployeeMapper;
import com.qinh.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-11-23:17
 */
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    public List<Employee> listEmployees(){
        List<Employee> employees = employeeMapper.listEmployees();
        return employees;
    }
}
