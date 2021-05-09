package com.qinh.dao;

import com.qinh.entity.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-22:27
 */
public interface EmployeeMapper {

    /**
     * 多条记录封装一个map,Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javaBean
     * @MapKey,告诉mybatis封装这个map的时候使用哪个属性作为map的key
     *
     * @param lastName
     * @return
     */
    //@MapKey("id")
    @MapKey("lastName")
    Map<String,Employee> getEmpByLastNameReturnMap(String lastName);

    /**
     * 返回一条记录的map,key就是列名，值就是对应的值
     *
     * @param id
     * @return
     */
    Map<String,Object> getEmpByIdReturnMap(Integer id);

    /**
     * 返回集合
     *
     * @return
     */
    List<Employee> listEmployee();

    List<Employee> listEmpsByLastName(String lastName);

    /**
     * 参数为集合或数组
     *
     * @param ids
     * @return
     */
    Employee getEmpById2(List<Integer> ids);

    Employee getEmpById3(Integer[] ids);

    Employee getEmpByMap(Map<String, Object> map);

    /**
     * 添加@Param注解
     *
     * @param id
     * @param lastName
     * @return
     */
    Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    /**
     * 单个参数（除集合或数组），mybatis不会封装参数
     *
     * @param id
     * @return
     */
    Employee getEmpById(Integer id);

    Long addEmp(Employee employee);

    Integer updateEmp(Employee employee);

    Boolean deleteEmpById(Integer id);
}
