package fire.pb.shop.srv.market.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

import fire.pb.shop.srv.market.MarketManager;

/**
 * 嵌入式数据库连接管理
 * 
 * @author liangyanpeng
 *
 */
public class ConnectionUtils {
	
	static ThreadLocal<Connection> connBindThread = new ThreadLocal<>();
	static JdbcConnectionPool jcp = null;
	
	private ConnectionUtils() {
	}
	
	public static Connection builder() {
		Connection conn = null;
		try {
			if (jcp == null || jcp.getActiveConnections() <= 0) {
				jcp = JdbcConnectionPool.create("jdbc:h2:mem:marketdb;DB_CLOSE_DELAY=-1;MVCC=TRUE;MODE=MYSQL;LOCK_MODE=3;CACHE_SIZE=10240", "sa", "");
//				jcp = JdbcConnectionPool.create("jdbc:h2:tcp://localhost/marketdb;CACHE_SIZE=10240", "sa", "");
				jcp.setMaxConnections(100);
			}
			conn = jcp.getConnection();
		} catch (Exception e) {
			MarketManager.LOG.error("初始化H2数据库" + e);
		}
		return conn;
	}
	
	private static final Connection create() {
		Connection conn = connBindThread.get();
		if (null == conn)
			connBindThread.set(conn = builder());
		return conn;
	}
	
	public static final Connection getConnection() {
		Connection conn = connBindThread.get();
		try {
			if (conn == null || conn.isClosed()) {
				return conn = create();
			}
		} catch (SQLException e) {
			MarketManager.LOG.error("获得Jdbc connection" + e);
		}
		return conn;
	}

	public static final boolean closeConnetion() {
		Connection conn = connBindThread.get();
		if (conn == null) {
			MarketManager.LOG.warn("Close connection 连接对象不存在!");
			return false;
		}
		try {
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			MarketManager.LOG.error("关闭Jdbc connection" + e);
			return false;
		} finally {
			connBindThread.set(null);
		}
		return true;
	}

}
