package fire.pb;

import fire.log.Logger;



//系统配置
public class PSetSysConfigOne extends mkdb.Procedure{

	private final long roleId;
	private final int sysConfigType;
	private final int val;
	private static Logger logger = Logger.getLogger("SYSTEM");
	
	public PSetSysConfigOne(final long roleId, final int sysConfigType, final int val){
		this.roleId = roleId;
		this.sysConfigType = sysConfigType;
		this.val = val;
	}
	
	public boolean process(){
		
		final xbean.Properties roleProp = xtable.Properties.get(roleId);
		if (null == roleProp)
			return false;
		
		final java.util.Map<Integer, Integer>  sysConfig = roleProp.getSysconfigmap();
		
		if(sysConfigType < SysConfigType.Music || sysConfigType > SysConfigType.huodongopen){
			logger.error("PSetSyssettingOne 系统设置超出范围  roleid:" + roleId + " systype:" + sysConfigType );
			return false;
		}
		
		roleProp.getSysconfigmap().put(sysConfigType, val);

		final SGetSysConfig msg = new SGetSysConfig();
		msg.sysconfigmap.putAll(sysConfig);
		psendWhileCommit(roleId, msg);
		
		logger.error("PSetSyssettingOne 系统设置成功, roleid:" + roleId + " systype:" + sysConfigType );
		
		return true;
	}
}
