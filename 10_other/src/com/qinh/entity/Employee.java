package com.qinh.entity;

/**
 * @author Qh
 * @version 1.0
 * @date 2021-05-05-21:53
 */
public class Employee {

    private Integer id;
    private String lastName;
    private String gender;
    private String email;

    //员工登录状态
    private EmpStatus empStatus = EmpStatus.LOGOUT;

    public Employee() {
    }

    public Employee(String lastName, String gender, String email) {
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
    }

    public EmpStatus getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(EmpStatus empStatus) {
        this.empStatus = empStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
