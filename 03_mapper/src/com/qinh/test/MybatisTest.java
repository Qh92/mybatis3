package com.qinh.test;

import com.qinh.dao.EmployeeMapper;
import com.qinh.dao.EmployeeMapperAnnotation;
import com.qinh.dao.EmployeeMapperPlus;
import com.qinh.entity.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 1、接口式编程
 *      原生： Dao ====> DaoImpl
 *      mybatis： Mapper ===> xxMapper.xml
 * 2、SqlSession代表和数据库的一次会话，用完必须关闭
 * 3、SqlSession和connection 一样都是非线程安全的。每次使用都应该去获取新的对象
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象
 *      （将接口和xml进行绑定）
 *      EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 *      mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等等系统运行环境信息
 *      sql映射文件：保存了每一个sql语句的映射信息，将sql抽取出来
 *
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-22:09
 */
public class MybatisTest {

    private void initLog() {
        FileInputStream fileInputStream = null;
        try {
            Properties properties = new Properties();
            fileInputStream = new FileInputStream("conf/log4j.xml");
            properties.load(fileInputStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        initLog();
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream  = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 1、根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象 有数据源一些运行环境信息
     * 2、sql映射文件；配置了每一个sql，以及sql的封装规则等。
     * 3、将sql映射文件注册在全局配置文件中
     * 4、写代码：
     * 		1）、根据全局配置文件得到SqlSessionFactory；
     * 		2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
     * 			一个sqlSession就是代表和数据库的一次会话，用完关闭
     * 		3）、使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
     *
     * @throws IOException
     */
    @Test
    public void t1() throws IOException {
        // 2、获取sqlSession实例，能直接执行已经映射的sql语句
        // sql的唯一标识：statement Unique identifier matching the statement to use.
        // 执行sql要用的参数：parameter A parameter object to pass to the statement.
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Employee e = sqlSession.selectOne("com.qinh.dao.EmployeeMapper.getEmpById", 1);
            System.out.println(e);
        }finally {
            sqlSession.close();
        }
    }

    /**
     * 测试增删改
     * 1、mybatis允许增删改直接定义以下类型返回值
     *      Integer、Long、Boolean
     * 2、需要手动提交数据
     *      sqlSessionFactory.openSession() ==> 手动提交
     *      sqlSessionFactory.openSession(true) ==> 自动提交
     *
     * @throws IOException
     */
    @Test
    public void t2() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象，不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            Employee employee = new Employee(null, "Westbrook333", "0", "aa@gamil.com");
            //Boolean resoult = mapper.deleteEmpById(4);
            //System.out.println(resoult);
            //mapper.updateEmp(employee);
            mapper.addEmp(employee);
            System.out.println(employee.getId());
            List<Employee> employees = mapper.listEmployee();
            System.out.println(employees);
            //手动提交数据
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }


    /**
     * 测试将参数封装成Map
     *
     * @throws IOException
     */
    @Test
    public void t4() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象，不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpByIdAndLastName(1, "Curry");
            Map<String ,Object> map = new HashMap<>();
            map.put("id",1);
            map.put("lastName","Curry");
            //Employee employee = mapper.getEmpByMap(map);
            System.out.println(employee);
        }finally {
            sqlSession.close();
        }
    }

    /**
     * 测试将参数为List
     *
     * @throws IOException
     */
    @Test
    public void t5() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象，不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Integer> ids = new ArrayList<>();
            ids.add(1);
            Employee employee = mapper.getEmpById2(ids);
            System.out.println(employee);
        }finally {
            sqlSession.close();
        }
    }

    /**
     * 测试返回结果为集合的情况
     *
     * @throws IOException
     */
    @Test
    public void t3() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象，不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Employee> employees = mapper.listEmpsByLastName("%e%");
            System.out.println(employees);

            System.out.println();

            Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
            for (Map.Entry<String,Object> entry:map.entrySet()){
                System.out.println("key : " + entry.getKey() + " value : " + entry.getValue());
            }

            System.out.println();

            Map<String, Employee> resultMap = mapper.getEmpByLastNameReturnMap("%e%");
            System.out.println(resultMap);

        }finally {
            sqlSession.close();
        }
    }


    /**
     * resultMap测试
     *
     * @throws IOException
     */
    @Test
    public void t6() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = sqlSession.getMapper(EmployeeMapperPlus.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(employee);

            System.out.println();

            Employee employee1 = mapper.getEmpAndDept(1);
            System.out.println(employee1);

            System.out.println();

            Employee employee2 = mapper.getEmpByIdStep(1);
            System.out.println(employee2.getLastName());


        }finally {
            sqlSession.close();
        }

    }
}
