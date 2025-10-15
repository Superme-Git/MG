/**
 * 
 */
package fire.pb.pingbi;

import java.util.ArrayList;
import java.util.List;

import fire.pb.pingbi.service.PingBiService;
import mkdb.Procedure;

/**
 * 
 * 移除黑名单角色
 * 
 *
 */
public class PRemoveBlackRoleProc extends Procedure {

	private long roleId;
	private long toRoleId;
	
	public PRemoveBlackRoleProc(long roleId,long toRoleId){
		this.roleId = roleId;
		this.toRoleId = toRoleId;
	}
	
	protected boolean process() throws Exception {
		//不能移除自己
		if(roleId == toRoleId){
			return false;
		}
		xbean.RolePingBiRolesInfo info = xtable.Rolepingbirolesinfos.get(roleId);
		if(info == null){
			info = xbean.Pod.newRolePingBiRolesInfo();
			xtable.Rolepingbirolesinfos.insert(roleId, info);
		}
		//对方不在屏蔽列表里
		if(!info.getPingbiroles().containsKey(toRoleId)){
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(toRoleId);
		//目标角色不存在
		if(prop == null || prop.getDeletetime()!=0){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145345, null);
			return false;
		}
		//移除角色
		info.getPingbiroles().remove(toRoleId);
		//重新发送屏蔽列表
		PingBiService.getInstance().sendBlackRoleList(info, roleId);
		//发送提示
		List<String> params = new ArrayList<String>();
		params.add(prop.getRolename());
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145664, params);
		// 记录日志
		if (fire.pb.friends.Module.logger.isInfoEnabled()) {
			fire.pb.friends.Module.logger.info("[PRemoveBlackRoleProc] roleId:" + roleId
					+ " targetRoleId:" + toRoleId
					+ " targetRoleName:" + prop.getRolename()
					+ " blackListNum:" + info.getPingbiroles().size());
		}
		return true;
	}
}
