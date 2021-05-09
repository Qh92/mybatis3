package com.qinh.test;

import com.qinh.dao.EmployeeMapperDynamicSQL;
import com.qinh.entity.Department;
import com.qinh.entity.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            inputStream = Resources.getResourceAsStream("conf/log4j.xml");
            properties.load(inputStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        //initLog();
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
     * 测试 if where trim choose标签
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
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);

            Employee employee = new Employee(null, null, null, null);
            //测试if where
            List<Employee> employees = mapper.listEmpsByConditionIf(employee);
            System.out.println(employees);
            //查询的时候如果某些条件没带可能引起sql拼装有问题

            //1.给where后面加上1=1，以后的条件都带上and

            //2.mybatis使用where标签来将所有的查询条件包括在内.mybatis就会将where标签中拼接的sql,多出来的and或者or去掉
            //where标签只会去掉第一个多出来的and或者or

            //测试Trim
            List<Employee> employees1 = mapper.listEmpsByConditionTrim(employee);
            System.out.println(employees1);

            //测试choose
            List<Employee> employees2 = mapper.listEmpsByConditionChoose(employee);
            System.out.println(employees2);

        }finally {
            sqlSession.close();
        }
    }

    /**
     * 测试set标签
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
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);

            Employee employee = new Employee(1, null, null, "james@gail.com");
            mapper.updateEmp(employee);

            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

    /**
     * 测试foreach标签
     */
    @Test
    public void t4() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象，不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Integer> ids = new ArrayList<>();
            ids.add(1);
            ids.add(2);
            ids.add(3);
            ids.add(5);
            List<Employee> employees = mapper.listEmpsByConditionForeach(ids);
            System.out.println(employees);

        }finally {
            sqlSession.close();
        }
    }


    /**
     * 批量保存
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
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Employee> emps = new ArrayList<>();
            Department department = new Department(1, "技术部");
            Department department1 = new Department(2, "测试部");
            Employee employee = new Employee(null, "curry2", "0", "curry@gmail.com");
            employee.setDept(department);
            Employee employee2 = new Employee(null, "frank2", "0", "frank@gmail.com");
            employee2.setDept(department1);
            Employee employee3 = new Employee(null, "jennifer2", "0", "curry@gmail.com");
            employee3.setDept(department);
            emps.add(employee);
            emps.add(employee2);
            emps.add(employee3);
            mapper.addEmps(emps);

            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }


    /**
     * 根据不同的数据库查询不同的sql
     *
     * @throws IOException
     */
    @Test
    public void t6() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象，不会自动提交数据
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapperDynamicSQL mapper = sqlSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee = new Employee(null, "j", null, null);
            List<Employee> employees = mapper.listEmpsTestInnerParameter(employee);
            System.out.println(employees);
        }finally {
            sqlSession.close();
        }
    }


}
