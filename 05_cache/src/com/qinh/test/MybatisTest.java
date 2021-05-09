package com.qinh.test;

import com.qinh.dao.EmployeeMapper;
import com.qinh.dao.EmployeeMapperDynamicSQL;
import com.qinh.entity.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-22:09
 */
public class MybatisTest {


    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        //initLog();
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream  = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 两级缓存:
     * 一级缓存：（本地缓存）,sqlSession级别的缓存，一级缓存是一直开启的，sqlSession级别的一个Map
     *      与数据库同一次会话期间查询到的数据会放在本地缓存中
     *      以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库
     *
     *      一级缓存失效的情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）
     *      1.sqlSession不同
     *      2.sqlSession相同，查询条件不同
     *      3.sqlSession相同，两次查询直接执行了增删改操作(这次增删改可能对当前数据有影响)
     *      4.sqlSession相同，手动清除了一级缓存(缓存清空)
     *
     * 二级缓存：（全局缓存），基于namespace级别的缓存，一个namespace对应一个二级缓存
     *      工作机制：
     *      1.一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中
     *      2.如果会话关闭，一级缓存中的数据会被保存到二级缓存中。新的会话查询信息，就可以参照二级缓存中的内容
     *      3.sqlSession中 EmployeeMapper ===> Employee.
     *                      DepartmentMapper ===> Department
     *          不同namespace查出的数据会放在自己对应的缓存中(Map)
     *
     */

    @Test
    public void t1() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(employee);

            //sqlSession相同
            Employee employee1 = mapper.getEmpById(1);
            System.out.println(employee1);
            //true
            System.out.println(employee == employee1);

            System.out.println("-------------------");

            //sqlSession相同，查询条件不同
            Employee employee2 = mapper.getEmpById(3);
            System.out.println(employee2);

            System.out.println("-----------");

            //sqlSession不同
            SqlSession sqlSession1 = sqlSessionFactory.openSession();
            EmployeeMapper mapper1 = sqlSession1.getMapper(EmployeeMapper.class);
            Employee employee3 = mapper1.getEmpById(1);
            System.out.println(employee3);
            //false
            System.out.println(employee3 == employee);

            sqlSession1.close();
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void t2() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(employee);

            //mapper.addEmp(new Employee(null,"testCache","1","cache@qq.com"));
            sqlSession.clearCache();

            //sqlSession相同
            Employee employee1 = mapper.getEmpById(1);
            System.out.println(employee1);
            //false
            System.out.println(employee == employee1);


        }finally {
            sqlSession.close();
        }
    }





}
