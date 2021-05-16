package com.qinh.test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinh.dao.EmployeeMapper;
import com.qinh.entity.EmpStatus;
import com.qinh.entity.Employee;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

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

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "conf/mybatis-config.xml";
        InputStream inputStream  = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    @Test
    public void t2() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Page<Object> page = PageHelper.startPage(4, 1);
            List<Employee> emps = mapper.getEmps();
            emps.stream().forEach(System.out::println);
            System.out.println("当前页码: " + page.getPageNum());
            System.out.println("总记录数: " + page.getTotal());
            System.out.println("每页的记录数: " + page.getPageSize());
            System.out.println("总页码: " + page.getPages());

            System.out.println("----------------");

            //传入要连续显示多少页
            PageInfo<Employee> pageInfo = new PageInfo<>(emps,5);
            System.out.println("当前页码: " + pageInfo.getPageNum());
            System.out.println("总记录数: " + pageInfo.getTotal());
            System.out.println("每页的记录数: " + pageInfo.getPageSize());
            System.out.println("总页码: " + pageInfo.getPages());
            System.out.println("是否第一页: " + pageInfo.isIsFirstPage());
            System.out.println("#########################");
            int[] nums = pageInfo.getNavigatepageNums();
            for (int n : nums){
                System.out.println("页码" + n);
            }

        }finally {
            sqlSession.close();
        }

    }

    @Test
    public void testBatch() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取可以批量操作的sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++){
                mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0,5),"b","c"));
            }
            sqlSession.commit();
            long end = System.currentTimeMillis();
            //批量：（预编译sql一次 -> 设置参数 -> 10000次 -> 执行一次）10854
            //非批量：（预编译sql -> 设置参数-> 执行10000次）24131
            System.out.println("执行时长: " + (end - start));
        }finally {
            sqlSession.close();
        }
    }

    /**
     * oracle分页:
     *      借助rownum：行号，子查询
     * 存储过程包装分页逻辑
     */
    @Test
    public void testProcedure() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取可以批量操作的sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            com.qinh.entity.Page page = new com.qinh.entity.Page();
            page.setStart(1);
            page.setEnd(4);
            mapper.getPageByProcedure(page);
            System.out.println("总记录数: " + page.getCount());
            System.out.println("查出的数据: " + page.getEmployees().size());
            System.out.println("查出的数据: " + page.getEmployees());
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void teestEnumUse(){
        EmpStatus login = EmpStatus.LOGIN;
        System.out.println("枚举的索引: " + login.ordinal());
        System.out.println("枚举的名字: " + login.name());

        System.out.println("枚举的状态码: " + login.getCode());
        System.out.println("枚举的提示消息: " + login.getMessage());
    }

    /**
     * 默认mybatis在处理枚举对象的时候保存的是枚举的名称：EnumTypeHandler
     * 改变使用：EnumOrdinalTypeHandler
     *
     * @throws IOException
     */
    @Test
    public void testEnum() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee("test_enum", "0", "enum@sina.com");
            mapper.addEmp(employee);
            System.out.println("保存成功,id " + employee.getId());
            sqlSession.commit();
            Employee employee1 = mapper.getEmpById(30025);
            System.out.println(employee1.getEmpStatus());
        }finally {
            sqlSession.close();
        }
    }
}
