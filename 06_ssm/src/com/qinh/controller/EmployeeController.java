package com.qinh.controller;

import com.qinh.entity.Employee;
import com.qinh.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-11-23:15
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping("/emps")
    public String emps(Map<String,Object> map){

        List<Employee> employees = employeeService.listEmployees();

        map.put("allEmps",employees);
        return "list";
    }
}
