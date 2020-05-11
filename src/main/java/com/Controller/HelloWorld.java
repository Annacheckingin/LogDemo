package com.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.model.User;
import com.model.Status;

@Controller
@ResponseBody
public class HelloWorld
{

    static Connection connection = null;

    public HelloWorld() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ZZZGOODS?useUnicode=true&characterEncoding=UTF-8&amp" + "&serverTimezone=UTC&useSSL=false", "root", "1997127132012");
            if (connection == null) {
                System.out.println("connecttipn is null in 构造函数");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(path = "/mobileLog")
    @ResponseBody
    public Status log(@RequestParam(name = "name") String name, @RequestParam(name = "pwd") String password) {
        System.out.println("going here");
        User commingUser = new User();
        commingUser.setName(name);
        commingUser.setPwd(password);
        ResultSet resultset = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
        } catch (SQLException excep) {
            if (stmt == null) {
                System.out.println("statement was null");
            }
        }
        if (connection == null) {
            System.out.println("connection was null");
        }
        String sql = "select password from log where name =" + "\'" + commingUser.getName() + "\'" + ";";

        System.out.println(sql);
        System.out.println("1");
        String getedPwd = password;
        try {
            System.out.println("2");
            resultset = stmt.executeQuery(sql);
            getedPwd = "";
            while (resultset.next()) {
                getedPwd = resultset.getString("password");
            }
            System.out.println("3");

//        查到了存在的用户
            if (getedPwd.equals(commingUser.getPwd()))
            {
                 Status status=new Status();
                 status.setStatusCode(true);
                 status.setContent("llll");
                  return status;
            } else {
                //没找到相应的用户
                System.out.println("5");
                String resultString = "{\"result\":false}";
                Status status=new Status();
                status.setStatusCode(false);
                return status;


            }
        } catch (SQLException e) {

            System.out.println("6");

            Status status=new Status();
            status.setStatusCode(false);
            return status;

        }


    }

    @RequestMapping(path = "/mobileRegister")
    @ResponseBody
    public String mobileRegister(@RequestParam("name") String name, @RequestParam("pwd") String password) throws SQLException {
        System.out.println("registe here");
        String findSql = "select  * from log where name=" + "\'" + name + "\';";
        Statement findstmt = connection.createStatement();

        if (connection == null) {
            System.out.println("connecttion is null");
        }

        if (findstmt == null) {
            System.out.println("findstmt is null");
        }

        ResultSet resultset = findstmt.executeQuery(findSql);
        List<String> resultlist = new ArrayList<String>();

        while (resultset.next()) {
            resultlist.add(resultset.getString("name"));
        }

        if (resultlist.contains(name)) {

            return "ok";
        } else
            {

            System.out.println("going 5");
            String registerSql = "insert into log (name,password) values(?,?)";
            PreparedStatement prestmt = connection.prepareStatement(registerSql);
            System.out.println("going 6");
            prestmt.setString(1, name);
            prestmt.setString(2, password);
            int isRegited = prestmt.executeUpdate();

            if (isRegited > 0) {


                String resultString = "{\"result\":true}";
                System.out.println(resultString);
                return resultString;

            } else {
                String resultString = "{\"result\":false}";
                return resultString;

            }
        }

    }
}