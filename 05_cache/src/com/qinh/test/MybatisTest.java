package com.qinh.test;

import com.qinh.dao.DepartmentMapper;
import com.qinh.dao.EmployeeMapper;
import com.qinh.entity.Department;
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
     *          效果，数据会从二级缓存中获取
     *              查出的数据都会被默认先放在一级缓存中。只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中
     *
     *      使用：
     *          1).开启全局二级缓存配置 <setting name="cacheEnabled" value="true"/>
     *          2).去mapper.xml中配置使用二级缓存
     *              <cache></cache>
     *          3).POJO需要实现序列化接口
     *
     * 和缓存有关的设置/属性：
     *      1).cacheEnabled=true,false:关闭缓存(关闭二级缓存，一级缓存一直可用)
     *      2).每个select标签都有useCache="true"
     *          false:不使用缓存（一级缓存依然可以使用，二级缓存不使用）
     *      3).每个增删改标签的 flushCache="true"（默认为true，一级、二级都会清除）
     *          增删改执行完成后就会清除缓存
     *          测试：flushCache="true"，一级缓存就清空,二级缓存也会被清除
     *          查询标签:flushCache="false"
     *              如果flushCache=true.每次查询之后都会清空缓存(一级、二级都清空)
     *      4).sqlSession.clearCache();只是清除当前session的一级缓存
     *      5).localCacheScope:本地缓存作用域(一级缓存SESSION),当前会话的所有数据保存在会话缓存中
     *                  STATEMENT:可以禁用一级缓存
     */

    /**
     * 一级缓存测试
     *
     * @throws IOException
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

    /**
     * 二级缓存测试，mapper.xml配置了cache标签
     *
     * @throws IOException
     */
    @Test
    public void t3() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            EmployeeMapper mapper2 = sqlSession2.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(employee);


            mapper.addEmp(new Employee(null,"testCache","1","cache@qq.com"));

            sqlSession.close();

            //第二次查询是从二级缓存中拿到的数据，并没有发送新的sql
            Employee employee2 = mapper2.getEmpById(1);
            System.out.println(employee2);

            //sqlSession.close();
            sqlSession2.close();

        }finally {
            sqlSession.close();
        }
    }


    /**
     * 二级缓存测试，mapper.xml没有配置cache标签
     *
     * @throws IOException
     */
    @Test
    public void t4() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
            DepartmentMapper mapper2 = sqlSession2.getMapper(DepartmentMapper.class);

            Department department = mapper.getDeptById(1);
            System.out.println(department);
            sqlSession.close();

            Department department2 = mapper2.getDeptById(1);
            System.out.println(department2);
            sqlSession2.close();

        }finally {
            sqlSession.close();
        }
    }





}
