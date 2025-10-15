package fire.pb;

import fire.msp.role.GSetMaxScreenShowNum;
import fire.pb.util.FireProp;


//修改系统设置的存储过程
public class PResetSysConfig extends mkdb.Procedure{
	private final CResetSysConfig cliProto;
	private final long roleId;
	
	public PResetSysConfig(final long roleId, final CResetSysConfig cliProto){
		this.cliProto = cliProto;
		this.roleId = roleId;
	}
	
	public boolean process(){
		if (null == cliProto || roleId < 0)
			return false;
		
		if (!cliProto._validator_())
			return false;
		
		final xbean.Properties roleProp = xtable.Properties.get(roleId);
		if (null == roleProp)
			return false;
		final java.util.Map<Integer, Integer>  sysConfig = roleProp.getSysconfigmap();
		
		Integer oldMaxScreenShowNum = sysConfig.get(SysConfigType.MaxScreenShowNum);
		if(oldMaxScreenShowNum == null)
			oldMaxScreenShowNum = FireProp.getIntValue("sys", "sys.maxScreenShowNum");
		Integer cProtoNum = cliProto.sysconfigmap.get(SysConfigType.MaxScreenShowNum);
		int newMaxScreenShowNum = oldMaxScreenShowNum;
		if(cProtoNum != null)
			newMaxScreenShowNum = cProtoNum;
		if(newMaxScreenShowNum <10 || newMaxScreenShowNum > 100){
			//设置人数不合法
			cliProto.sysconfigmap.remove(SysConfigType.MaxScreenShowNum);
		}
		
		cliProto.sysconfigmap.remove(SysConfigType.skillopen);
		cliProto.sysconfigmap.remove(SysConfigType.factionopen);
		cliProto.sysconfigmap.remove(SysConfigType.petopen);
		cliProto.sysconfigmap.remove(SysConfigType.patopen);
		cliProto.sysconfigmap.remove(SysConfigType.guajiopen);
		cliProto.sysconfigmap.remove(SysConfigType.zhiyinopen);
		cliProto.sysconfigmap.remove(SysConfigType.huodongopen);
		
		for (Integer key : cliProto.sysconfigmap.keySet()){
			sysConfig.put(key, cliProto.sysconfigmap.get(key));
		}
		
		final SResetSysConfig msg = new SResetSysConfig(cliProto.sysconfigmap);
		psendWhileCommit(roleId, msg);
		
		if(oldMaxScreenShowNum != newMaxScreenShowNum){
			GsClient.pSendWhileCommit(new GSetMaxScreenShowNum(roleId,newMaxScreenShowNum));
		}
		
		return true;
	}
}
