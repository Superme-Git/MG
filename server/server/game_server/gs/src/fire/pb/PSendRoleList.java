package fire.pb;

import fire.log.Module;
import fire.pb.fushi.payday.SPayServerType;
import fire.pb.game.SPVPCrossWhiteList;
import fire.pb.main.ConfigManager;
import fire.pb.role.PCreateRole;
import fire.pb.state.State;
import fire.pb.state.StateManager;
import gnet.link.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PSendRoleList extends mkdb.Procedure
{
	private final User user;
	private final boolean isReturnList;
	public PSendRoleList(User user, boolean isReturnList)
	{
		this.user = user;
		this.isReturnList = isReturnList;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			SPayServerType msg = new SPayServerType();
			msg.paytype = fire.pb.fushi.Module.GetPayServiceType();
			msg.opendy = ConfigManager.OPEN_DY ? 1 : 0;
			user.send(msg);
		}

		final xbean.User u = xtable.User.select(user.getUserid());
		if(u == null)
		{
			SRoleList sRoleList = new SRoleList();
			sRoleList.gacdon = ConfigManager.isOpenRecruit ? 1 : 0;
			user.send(sRoleList);
			return false;
		}
		
		long prevloginroleid = u.getPrevloginroleid();
		
		int stateId = StateManager.selectStateIdByRoleId(prevloginroleid);
		byte prevroleinbattle = 0;
		if(stateId == State.TRUSTEESHIP_STATE)
			prevroleinbattle = 1;

		List<Long> roleIds = null;
		boolean corss = false;

		// 跨服PVP
		if (fire.pb.PropConf.Cross.PVP == 1) {
			Map<Integer, SPVPCrossWhiteList> cfgs = ConfigManager.getInstance().getConf(SPVPCrossWhiteList.class);
			for (Map.Entry<Integer, SPVPCrossWhiteList> e : cfgs.entrySet()) {
				if (e.getValue().getUserid() == user.getUserid()) {
					for (long roleid : u.getIdlist()) {
						if (roleid == e.getValue().getRoleid()) {
							roleIds = new ArrayList<Long>();
							roleIds.add(roleid); // 只取一个角色
							break;
						}
					}
					if (roleIds == null || roleIds.isEmpty()) {
						// 没有角色
						Module.logger.info("[PSendRoleList] cross pvp userid:" + user.getUserid()
								+ " roleIdList.size() == 0");
					}
					break;
				}
			}
			corss = true;
		}
		boolean isExistPrevloginroleid = false;
		LinkedList<fire.pb.RoleInfo> roles = new LinkedList<fire.pb.RoleInfo>();
		for(int i=0; i<u.getIdlist().size(); i++){
			//处理合服后，如果出现大于指定最大数量的角色的时候，取最大数量
			if(i==PCreateRole.maxRoleNum){
				break;
			}
			//角色id
			long roleId=u.getIdlist().get(i);
			if (corss) {
				if (roleIds == null) {
					return false;
				}
				if (!roleIds.contains(roleId)) {
					continue;
				}
				prevloginroleid = roleId;
			}
			if (prevloginroleid == roleId)
				isExistPrevloginroleid = true;
			final fire.pb.RoleInfo info = new fire.pb.RoleInfo();
			info.roleid = roleId;
			final xbean.Properties pro = xtable.Properties.select(info.roleid);
			if(null == pro){
				continue;
			}
			if (pro.getDeletetime()>0)
				continue;
			info.level = pro.getLevel();
			info.rolename = pro.getRolename();
			info.school = pro.getSchool();
			info.shape = pro.getShape();
			fire.pb.map.Role.getPlayerComponents(info.roleid, info.components);
			roles.add(info);
		}
		//处理合服后,如果最大数量个数的的角色列表中不包含prevloginroleid，就取第一个
		if (!corss) {
			if (!isExistPrevloginroleid&&u.getIdlist().size()> 0) {
				prevloginroleid = u.getIdlist().get(0);
			}
		}
		int gacdon = ConfigManager.isOpenRecruit ? 1 : 0;
		if(isReturnList)
			user.send(new SReturnRoleList(prevloginroleid,prevroleinbattle,roles,gacdon));
		else{
			user.send(new SRoleList(prevloginroleid,prevroleinbattle,roles,gacdon));
		}
	
		return true;
	}
	
}
