package fire.pb.shop.srv.market.jdbc;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.jdbc.utils.handler.ResultSetHandler;

public class JdbcTemplate {
	
	private JdbcTemplate() {}
	
	private static class JdbcTemplateHolder {
		static JdbcTemplate instance = new JdbcTemplate();
	}
	
	public static JdbcTemplate getInstance() {
		return JdbcTemplateHolder.instance;
	}
	
    public ResultSet query(String sql) throws SQLException {
        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }

        Connection conn = ConnectionUtils.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            rethrow(e, sql);
        }
        
		return rs;
    }
	
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            throw new SQLException("Null ResultSetHandler");
        }
        Connection conn = ConnectionUtils.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        T result = null;
        
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            result = rsh.handle(rs);
        } catch (SQLException e) {
            rethrow(e, sql);
        } finally {
        	if (rs != null)
        		rs.close();
        	if (stmt != null)
        		stmt.close();
        }
        
		return result;
    }
    
    public <T> T query(Page page, String sql, ResultSetHandler<T> rsh) throws SQLException {
        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            throw new SQLException("Null ResultSetHandler");
        }
        Connection conn = ConnectionUtils.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        T result = null;
        
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //	设置最大记录数为当前页
			stmt.setMaxRows(page.getEndIndex());
			rs = stmt.executeQuery(sql);
			//	直接移动游标为当前
			if (page.getBeginIndex() > 0) {
				rs.absolute(page.getBeginIndex());
			}
			
			int row1 = page.getEndIndex() - page.getBeginIndex();
			if (row1 > 8) {
				MarketManager.LOG.error("翻页出现大于8条记录的查询" + row1);
			}
			
            result = rsh.handle(rs);
        } catch (SQLException e) {
            rethrow(e, sql);
        } finally {
        	if (rs != null)
        		rs.close();
        	if (stmt != null)
        		stmt.close();
        }
        
		return result;
    }
	
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {

        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            throw new SQLException("Null ResultSetHandler");
        }
        Connection conn = ConnectionUtils.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            fillStatement(stmt, params);
            rs = stmt.executeQuery();
            result = rsh.handle(rs);
        } catch (SQLException e) {
            rethrow(e, sql, params);
        } finally {
        	if (rs != null)
        		rs.close();
        	if (stmt != null)
        		stmt.close();
        }
        
        return result;
    }
    
    public int update(String sql) throws SQLException {
        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }

        Statement stmt = null;
        int rows = 0;

        try {
            stmt = ConnectionUtils.getConnection().createStatement();
            rows = stmt.executeUpdate(sql);
            if (MarketManager.LOG.isDebugEnabled()) {
            	MarketManager.LOG.debug(sql);
            }
        } catch (SQLException e) {
            rethrow(e, sql);
        } finally {
        	if (stmt != null)
        		stmt.close();
        }

        return rows;
    }
    
    public int update(Connection conn, String sql) throws SQLException {
    	return update(conn, sql, (Object[])null);
    }
	
    public int update(Connection conn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = conn.prepareStatement(sql);
            fillStatement(stmt, params);
            rows = stmt.executeUpdate();
            if (MarketManager.LOG.isDebugEnabled()) {
            	MarketManager.LOG.debug(sql);
            }
        } catch (SQLException e) {
            rethrow(e, sql, params);
        } finally {
        	if (stmt != null)
        		stmt.close();
        }

        return rows;
    }
    
    public void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {

        // check the parameter count, if we can
        ParameterMetaData pmd = null;
        if (true) {
            pmd = stmt.getParameterMetaData();
            int stmtCount = pmd.getParameterCount();
            int paramsCount = params == null ? 0 : params.length;

            if (stmtCount != paramsCount) {
                throw new SQLException("Wrong number of parameters: expected "
                        + stmtCount + ", was given " + paramsCount);
            }
        }

        // nothing to do here
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }
    
    public void rethrow(SQLException cause, String sql, Object... params)
            throws SQLException {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuffer msg = new StringBuffer(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params));
        }

        SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
                cause.getErrorCode());
        e.setNextException(cause);

        throw e;
    }

}
