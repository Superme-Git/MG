package fire.pb.shop.srv.market.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import fire.pb.shop.srv.market.MarketManager;

/**
 * 事务控制
 * @author liangyanpeng
 *
 */
public class Transaction {

	public static final void begin() {
		try {
			ConnectionUtils.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			MarketManager.LOG.error("事务开始", e);
		}
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug(Thread.currentThread().getName() + "<======事务开始====>");
		}
	}
	
	public static final boolean commit() {
		Connection conn = ConnectionUtils.connBindThread.get();
		if (conn == null) {
			MarketManager.LOG.warn("Connection 不存在, 提交失败!");
			return false;
		}
		try {
			conn.commit();
		} catch (SQLException e) {
			MarketManager.LOG.error("Commit提交", e);
			return false;
		}
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug(Thread.currentThread().getName() + "<======提交事务结束====>");
		}
		return true;
	}
	
	public static final boolean rollback() {
		Connection conn = ConnectionUtils.connBindThread.get();
		if (conn == null) {
			MarketManager.LOG.warn("Connection 不存在, 回滚失败!");
			return false;
		}
		try {
			conn.rollback();
//			conn.commit();
		} catch (SQLException e) {
			MarketManager.LOG.error("Rollback回滚", e);
			return false;
		}
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug(Thread.currentThread().getName() + "<======回滚事务结束====>");
		}
		return true;
	}
	
	public static final boolean rollback(Savepoint rollSavepoint) {
		Connection conn = ConnectionUtils.connBindThread.get();
		if (conn == null) {
			MarketManager.LOG.warn("Connection 不存在, 根据会滚点回滚失败!");
			return false;
		}
		try {
			conn.rollback(rollSavepoint);
			conn.commit();
		} catch (SQLException e) {
			MarketManager.LOG.error("Rollback滚过到保存点", e);
			return false;
		}
		return true;
	}
	
}
