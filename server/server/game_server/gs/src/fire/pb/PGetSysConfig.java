package fire.pb;


//系统设置
public class PGetSysConfig extends mkdb.Procedure{
	
	private long roleId;
	
	public PGetSysConfig(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.Properties roleProp = xtable.Properties.get(roleId);
		if (null == roleProp){
			return false;
		}
		java.util.Map<Integer, Integer>  sysConfigMap = roleProp.getSysconfigmap();

		final SGetSysConfig msg = new SGetSysConfig();
		msg.sysconfigmap.putAll(sysConfigMap);
		psendWhileCommit(roleId, msg);
		
		return true;
	}
}
