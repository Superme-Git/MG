package fire.pb;

import java.util.ArrayList;
import java.util.Map;

import xbean.RoleAddPointProperties;
import fire.pb.SysConfig.SSysconfig;
import fire.pb.main.ConfigManager;

public class GameSystemConfig {

	static fire.pb.SysConfig.SSysconfig getConfig(Integer id){
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.SysConfig.SSysconfig.class).get(id);
	}
	
	static Integer getDefConfVal(Integer id){
		fire.pb.SysConfig.SSysconfig defConfig = getConfig(id);
		if (null != defConfig)
			return defConfig.getDefval();
		
		return null;
	}
	

	//获取对应的系统设置
	public static Integer getSysConfig(final long roleId, int sysConfigType){
		java.util.Map<Integer, Integer> sysConfigMap = xtable.Properties.selectSysconfigmap(roleId);
		if (null == sysConfigMap || null == sysConfigMap.get(sysConfigType)){
			return getDefConfVal(sysConfigType);
		}
		
		return sysConfigMap.get(sysConfigType);
	}
	
	
	//重设配置
	public static void resetSysConfig(xbean.Properties roleProp){
		Map<Integer, SSysconfig> gameConfigMap = ConfigManager.getInstance().getConf(SSysconfig.class);
		if(gameConfigMap == null){
			return;
		}
		
		roleProp.getSysconfigmap().clear();
		for(SSysconfig conf_ : gameConfigMap.values()){
			roleProp.getSysconfigmap().put(conf_.getId(), conf_.getDefval());
		}
	}
	
	public static void resetRolePropPoint(xbean.Properties roleProp){

		RoleAddPointProperties addPointFp = roleProp.getAddpointfp();
		roleProp.getPoint().clear();
		
		final fire.pb.role.RoleConfig roleConfig = RoleConfigManager.getRoleConfigBySchoolID(roleProp.getSchool());		
		ArrayList<Integer> pointLst = null;
		switch (roleConfig.defaultscheme){
		case 1:
			pointLst = roleConfig.addpoint;
			break;
		case 2:
			pointLst = roleConfig.addpoint2;
			break;
		case 3:
			pointLst = roleConfig.addpoint3;
			break;
		default:
			pointLst = roleConfig.addpoint;
		}
		
		addPointFp.getAgi_save().put(1, addPointFp.getAgi_save().get(1) + pointLst.get(4));
		addPointFp.getCons_save().put(1, addPointFp.getCons_save().get(1) + pointLst.get(0));
		addPointFp.getEndu_save().put(1, addPointFp.getEndu_save().get(1) + pointLst.get(3));
		addPointFp.getIq_save().put(1, addPointFp.getIq_save().get(1) + pointLst.get(1));
		addPointFp.getStr_save().put(1, addPointFp.getStr_save().get(1) + pointLst.get(2));
		
		roleProp.getPoint().put(1, 0);
		roleProp.getPoint().put(2, 5);
		roleProp.getPoint().put(3, 5);
	}
	
	public static void resetRolePropAddPointFp(xbean.Properties roleProp) {		
		for(int idx = 0; idx < 3; idx ++) {			
			roleProp.getAddpointfp().getAgi_save().put(idx + 1, 0);
			roleProp.getAddpointfp().getCons_save().put(idx + 1, 0);
			roleProp.getAddpointfp().getEndu_save().put(idx + 1, 0);
			roleProp.getAddpointfp().getIq_save().put(idx + 1, 0);
			roleProp.getAddpointfp().getStr_save().put(idx + 1, 0);
		}
	}
	
}
