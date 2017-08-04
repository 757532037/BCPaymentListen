package com.bc.main;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bc.db.DbSource;


public class Main {

    public static void main(String[] args) throws SQLException {
	ApplicationContext context = new ClassPathXmlApplicationContext("bc_server_listen_beans.xml");
	DbSource dbSource = (DbSource) context.getBean("dbSource");
	dbSource.checkConnect();
    }


}
