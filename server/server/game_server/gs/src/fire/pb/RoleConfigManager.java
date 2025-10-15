package fire.pb;

import fire.pb.common.SCommon;

public class RoleConfigManager {

	public static fire.pb.role.RoleConfig getRoleConfigBySchoolID(int id){
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.RoleConfig.class).get(id);
	}
	
	public static fire.pb.role.RoleModData getRoleModData(Integer id){
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.RoleModData.class).get(id);
	}
	
	public static SCommon getRoleCommonConfig(int id) {
		return fire.pb.main.ConfigManager.getInstance().getConf(SCommon.class).get(id);
	}
	
	/**
	 * 根据(主角id,游戏设定10个主角)
	 * @param id
	 * @return
	 */
	public static fire.pb.role.SCreateRoleConfig getCreateRoleConfig(Integer shapeid){
		final int id = shapeid % 10 == 0 ? 10 : shapeid % 10;
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.SCreateRoleConfig.class).get(id);
	}
	
	/**
	 * 根据门派，获得门派下的角色
	 * @param schoolid
	 * @return
	 */
	public static java.util.List<Integer> getSchoolShape(int schoolid){
		java.util.List<Integer> shapeids = new java.util.ArrayList<Integer>();
		java.util.Map<Integer, fire.pb.role.SCreateRoleConfig> roleconfig = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.SCreateRoleConfig.class);
		if (roleconfig == null)
			return shapeids;
		for (fire.pb.role.SCreateRoleConfig config:roleconfig.values()){
			if (config.schools.contains(schoolid))
				shapeids.add(config.id);
		}
		return shapeids;
	}
	
	/**
	 * 根据门派和性别，获得门派下的异性角色
	 * @param schoolid
	 * @param sex
	 * @return
	 */
	public static java.util.List<Integer> getSchoolOppoShape(int schoolid, int sex){
		java.util.List<Integer> shapeids = new java.util.ArrayList<Integer>();
		java.util.Map<Integer, fire.pb.role.SCreateRoleConfig> roleconfig = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.SCreateRoleConfig.class);
		if (roleconfig == null)
			return shapeids;
		for (fire.pb.role.SCreateRoleConfig config:roleconfig.values()){
			if (config.schools.contains(schoolid) && config.sex != sex)
				shapeids.add(config.id);
		}
		return shapeids;
	}
}
