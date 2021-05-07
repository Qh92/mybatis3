package com.qinh.dao;

import com.qinh.entity.Employee;
import org.apache.ibatis.annotations.Select;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-06-15:47
 */
public interface EmployeeMapperAnnotation {

    @Select("select * from employee where id = #{id}")
    Employee getEmpById(Integer id);
}
