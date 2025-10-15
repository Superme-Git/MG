package fire.pb.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;  

import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;

/** 
 * 数据库工具类  
 * by dc@2016.03.28
 */  
public class C3P0Util {
	
	static Properties prop = ConfigManager.getInstance().getPropConf("sys");
	public static final String MYSQLIP = FireProp.getStringValue(prop, "sys.mysql.ip");
	public static final String MYSQLPORT = FireProp.getStringValue(prop, "sys.mysql.port");
	public static final String MYSQLUSER = FireProp.getStringValue(prop, "sys.mysql.user");
	public static final String MYSQLPASS = FireProp.getStringValue(prop, "sys.mysql.pass");
	public static final String MYSQLDBNAME = FireProp.getStringValue(prop, "sys.mysql.dbname");
	
    static ComboPooledDataSource cpds = null;
    
    static{
        cpds = new ComboPooledDataSource();
    }
    
    public static ComboPooledDataSource getCPDS(){
    	return cpds;
    }
    
    /** 
     * 获得数据库连接
     * @return Connection
     */
    public static Connection getConnection(){  
        try {  
            return cpds.getConnection();  
        } catch (SQLException e) {
            e.printStackTrace();  
            return null;  
        }
    }  
      
    /** 
     * 数据库关闭操作 
     * @param conn   
     * @param st     
     * @param pst 
     * @param rs 
     */  
    public static void close(Connection conn, PreparedStatement pst, ResultSet rs){  
        if(rs!=null){  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        if(pst!=null){  
            try {  
                pst.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        if(conn!=null){  
            try {  
                conn.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    /** 
     * 测试DBUtil类 
     * @param args 
     */  
    public static void main(String[] args) {
        Connection conn = getConnection(); 
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            if (stmt.execute("SELECT * FROM user WHERE id = 329 or id = 338 or id = 339")){
            	rs = stmt.getResultSet();
            }
            
            if (rs != null){
            	while(rs.next()) {
            		System.out.println("id:" + rs.getString("id") + ",account:" + rs.getString(2));
            	}
            }
        } catch (SQLException ex1) {
            ex1.printStackTrace();
        } finally {
        	if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore
                stmt = null;
            }
        }  
        close(conn,null,null);  
    }
}  
