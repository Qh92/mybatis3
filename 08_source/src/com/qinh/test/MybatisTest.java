package com.qinh.test;

import com.qinh.dao.EmployeeMapper;
import com.qinh.entity.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

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

    /**
     * 1、获取sqlSessionFactory对象：
     *      解析文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultSqlSessionFactory对象
     *      注意：MappedStatement,代表一个增删改查的详细信息
     * 2、获取sqlSession对象
     *      返回一个DefaultSQLSession对象，包含Executor和Configuration
     *      这一步会创建Executor对象
     *
     * 3、获取接口的代理对象（MapperProxy）
     *      getMapper(),使用MapperProxyFactory创建一个MapperProxy的代理对象
     *      代理对象里面包含了，DefaultSqlSession(Executor)
     * 4、执行增删改查方法
     *
     * 总结：
     *  1、根据配置文件（全局，sql映射）初始化出Configuration对象
     *  2、创建一个DefaultSqlSession对象
     *      它里面包含Configuration以及Executor(根据全局配置文件中的defaultExecutorType创建出对应的Executor)
     *  3、DefaultSqlSession.getMapper():拿到Mapper接口对应的MapperProxy
     *  4、MapperProxy里面有(DefaultSqlSession)
     *  5、执行增删改查方法：
     *      1.调用DefaultSqlSession的增删改查（Executor）
     *      2.会创建一个StatementHandler对象.(同时也会创建出ParameterHandler和ResultSetHandler)
     *      3.调用StatementHandler预编译参数以及设置参数值
     *          使用ParameterHandler来给sql设置参数
     *      4.调用StatementHandler的增删改查
     *      5.ResultSetHandler封装结果
     *
     * 注意：
     *  四大对象每个创建的时候都有一个interceptorChain.pluginAll(parameterHandler)
     *
     * @throws IOException
     */
    @Test
    public void t2() throws IOException {
        //1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2、获取sqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try{
            //3、获取接口的实现类对象
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(mapper);
            System.out.println(employee);
        }finally {
            sqlSession.close();
        }

    }
}
