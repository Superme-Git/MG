
package gnet;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.mysql.C3P0Util;
import fire.pb.util.FireProp;







import com.locojoy.base.Runnable;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __ServerIDResponse__ extends mkio.Protocol { }

/** gs连上deliver上，发给gs；用户登录完成后，发给客户端
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class ServerIDResponse extends __ServerIDResponse__ {
	
	private void testMysqlConnect() {
		Connection conn = C3P0Util.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            if (stmt.execute("SELECT count(*) FROM role_relation")){
            	rs = stmt.getResultSet();
            }
            if (rs != null){
            	while(rs.next()) {
            		fire.log.Module.logger.info("mysql connect role_relation table,count(*):" + rs.getString("count(*)"));
            	}
            }
        } catch (SQLException ex1) {
        	fire.log.Module.logger.error("SQL执行有问题！");
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
        C3P0Util.close(conn,null,null);
	}
	
	@Override
	protected void process() {
		fire.pb.main.Gs.platType = plattype;
		try {
			String serverId = serverid.getString("ISO-8859-1");
			//
			if (fire.pb.PropConf.ServerId.ServerId != 0) {
				Integer id = Integer.parseInt(serverId);
				// 校验fire.pb.PropConf.ServerId.ServerId
				if (fire.pb.PropConf.ServerId.ServerId != id) {
					fire.log.Module.logger.error("fire.pb.PropConf.ServerId.ServerId:" + fire.pb.PropConf.ServerId.ServerId
							+ " != ServerIDResponse.serverId:" + id);
					ManagementFactory.getPlatformMBeanServer().setAttribute(
							new javax.management.ObjectName("bean:name=stopper"),
							new javax.management.Attribute("StopTime", 0));
				}
				// 校验fire.pb.main.Gs.serverid
				if (fire.pb.main.Gs.serverid.equals(serverId) == false) {
					fire.log.Module.logger.error("fire.pb.main.Gs.serverid:" + fire.pb.main.Gs.serverid
							+ " != ServerIDResponse.serverId:" + serverId);
					ManagementFactory.getPlatformMBeanServer().setAttribute(
							new javax.management.ObjectName("bean:name=stopper"),
							new javax.management.Attribute("StopTime", 0));
				}
			} else {
				fire.pb.main.Gs.serverid = serverId;
			}
			fire.log.Module.logger.info("plattype:"+plattype+",serverid:"+fire.pb.main.Gs.serverid);
			if(ConfigManager.getUseMysql()){
				fire.log.Module.logger.info("Default Config JdbcUrl:" + C3P0Util.getCPDS().getJdbcUrl());
				if(C3P0Util.MYSQLDBNAME != null && !C3P0Util.MYSQLDBNAME.isEmpty() && C3P0Util.MYSQLDBNAME != "") {
					C3P0Util.getCPDS().setJdbcUrl("jdbc:mysql://" + C3P0Util.MYSQLIP + ":" + C3P0Util.MYSQLPORT + "/" + C3P0Util.MYSQLDBNAME + "?&useUnicode=true&characterEncoding=utf-8");
				} else
					C3P0Util.getCPDS().setJdbcUrl("jdbc:mysql://" + C3P0Util.MYSQLIP + ":" + C3P0Util.MYSQLPORT + "/mt3_" + "weibo" + "_" + fire.pb.main.Gs.serverid + "?&useUnicode=true&characterEncoding=utf-8");
				C3P0Util.getCPDS().setUser(C3P0Util.MYSQLUSER);
				C3P0Util.getCPDS().setPassword(C3P0Util.MYSQLPASS);
				C3P0Util.getCPDS().setMaxIdleTime(1800);
				C3P0Util.getCPDS().setPreferredTestQuery("SELECT 1");
				C3P0Util.getCPDS().setIdleConnectionTestPeriod(30000);
				C3P0Util.getCPDS().setTestConnectionOnCheckout(true);
				C3P0Util.getCPDS().setAcquireRetryAttempts(3);
				C3P0Util.getCPDS().setCheckoutTimeout(3000);
				C3P0Util.getCPDS().setInitialPoolSize(10);
				C3P0Util.getCPDS().setMaxPoolSize(20);//100?
				C3P0Util.getCPDS().setMinPoolSize(10);
				C3P0Util.getCPDS().setMaxStatements(200);
				fire.log.Module.logger.info("Real Connect JdbcUrl:" + C3P0Util.getCPDS().getJdbcUrl());
				
				Gs.getExecService().execute(new Runnable() {
					@Override
					public void run() {
						testMysqlConnect();
					}
				});	
				 // 创建说不得大师空间数据
				fire.pb.friends.Module Module = new fire.pb.friends.Module();
		      Module.createXsh(Long.MAX_VALUE);
			}
		  
					
			Properties prop = ConfigManager.getInstance().getPropConf("sys");
			fire.pb.main.Gs.isYingyongbao = Integer.valueOf(FireProp.getStringValue(prop, "sys.plat.isYingyongbao")).intValue() == 1 ? true : false;
			fire.log.Module.logger.info("Gs.isYingyongbao=" + fire.pb.main.Gs.isYingyongbao);
		} catch (Exception e) {
			e.printStackTrace();
			fire.log.Module.logger.error("Mysql连接有问题！请确定配置并检查网络！将关闭服务器.");
        	try{
        		ManagementFactory.getPlatformMBeanServer().setAttribute(new javax.management.ObjectName("bean:name=stopper"),
					new javax.management.Attribute("StopTime", 1));
        	} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8902;

	public int getType() {
		return 8902;
	}

	public int plattype; // 当前服务器组，登录的是那个平台
	public com.locojoy.base.Octets serverid; // 当前服务器组的标识

	public ServerIDResponse() {
		serverid = new com.locojoy.base.Octets();
	}

	public ServerIDResponse(int _plattype_, com.locojoy.base.Octets _serverid_) {
		this.plattype = _plattype_;
		this.serverid = _serverid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(plattype);
		_os_.marshal(serverid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		plattype = _os_.unmarshal_int();
		serverid = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ServerIDResponse) {
			ServerIDResponse _o_ = (ServerIDResponse)_o1_;
			if (plattype != _o_.plattype) return false;
			if (!serverid.equals(_o_.serverid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += plattype;
		_h_ += serverid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(plattype).append(",");
		_sb_.append("B").append(serverid.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

