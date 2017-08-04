package com.bc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DbSource {

    private static Log logger = LogFactory.getLog(DbSource.class);
    DataSource dataSource;

    Connection connection;
    /**  
     *  设置dataSource  
     * @return dataSource dataSource  
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private String idleSql;
	
    /**  
     *  设置idleSql  
     * @return idleSql idleSql  
     */
    public void setIdleSql(String idleSql) {
        this.idleSql = idleSql;
    }

    public void checkConnect() throws SQLException {
	if (connection == null || connection.isClosed()) {
	    connection = dataSource.getConnection();
	    checkConnect();
	}
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
	    pstmt = connection.prepareStatement(idleSql);
	    rs = pstmt.executeQuery();
	    String id = "";
//	    while (rs.next()) {
//		id = rs.getString(1);
//	    }
	    logger.info("sql connect is running" + id);
	} catch (Exception e) {
	    logger.error("sql connect is not running,now is autoReConnet");
	    logger.error(e, e);
	    connection = dataSource.getConnection();
	} finally {
	    if (rs != null) {
		rs.close();
		rs = null;
	    }
	    if (pstmt != null) {
		pstmt.close();
		pstmt = null;
	    }
	}

    }
    
}
