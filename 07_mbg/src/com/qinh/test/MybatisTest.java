package com.qinh.test;

import com.qinh.dao.EmployeeMapper;
import com.qinh.entity.Employee;
import com.qinh.entity.EmployeeExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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


    @Test
    public void testMbg() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
    }

    /*@Test
    public void testSimple() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Employee> employees = mapper.selectAll();
            System.out.println(employees);
        }finally {
            sqlSession.close();
        }
    }*/

    @Test
    public void testMyBatis3() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //xxxExample???????????????????????????
            //1???????????????
            //List<Employee> emps = mapper.selectByExample(null);
            //2???????????????????????????e??????????????????????????????1???
            //???????????????????????????example
            EmployeeExample example = new EmployeeExample();
            //????????????Criteria?????????Criteria????????????????????????
            //select id, last_name, email, gender, d_id from employee
            //WHERE ( last_name like ? and gender = ? ) or email like "%e%"
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andLastNameLike("%e%");
            criteria.andGenderEqualTo("1");

            EmployeeExample.Criteria criteria2 = example.createCriteria();
            criteria2.andEmailLike("%e%");
            example.or(criteria2);

            List<Employee> list = mapper.selectByExample(example);
            /*for (Employee employee : list) {
                System.out.println(employee.getId());
            }*/
            System.out.println(list);

        }finally{
            openSession.close();
        }
    }







}
