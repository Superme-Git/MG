/**
 * 
 */
package fire.pb.pingbi;

import java.util.ArrayList;
import java.util.List;

import fire.pb.pingbi.constants.PingBiConstants;
import fire.pb.pingbi.service.PingBiService;
import mkdb.Procedure;

/**
 * 
 * 添加要屏蔽的角色
 * 
 * @author nobody
 *
 */
public class PAddBlackRoleProc extends Procedure {

	private long roleId;
	private long toRoleId;
	
	public PAddBlackRoleProc(long roleId,long toRoleId){
		this.roleId = roleId;
		this.toRoleId = toRoleId;
	}
	
	protected boolean process() throws Exception {
		//不能屏蔽自己
		if(roleId == toRoleId){
			return false;
		}
		xbean.RolePingBiRolesInfo info = xtable.Rolepingbirolesinfos.get(roleId);
		if(info == null){
			info = xbean.Pod.newRolePingBiRolesInfo();
			xtable.Rolepingbirolesinfos.insert(roleId, info);
		}
		xbean.Properties prop = xtable.Properties.select(toRoleId);
		//目标角色不存在
		if(prop == null || prop.getDeletetime()!=0){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145345, null);
			return false;
		}
		//对方已在屏蔽列表里
		if(info.getPingbiroles().containsKey(toRoleId)){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145661, null);
			return false;
		}
		//最多只能屏蔽20人
		if(info.getPingbiroles().size() >= PingBiConstants.MAX_PINGBI_NUM){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145662, null);
			return false;
		}
		//添加屏蔽角色
		info.getPingbiroles().put(toRoleId, toRoleId);
		//重新发送屏蔽列表
		PingBiService.getInstance().sendBlackRoleList(info, roleId);
		//发送提示
		List<String> params = new ArrayList<String>();
		params.add(prop.getRolename());
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145660, params);
		// 记录日志
		if (fire.pb.friends.Module.logger.isInfoEnabled()) {
			fire.pb.friends.Module.logger.info("[PAddBlackRoleProc] roleId:" + roleId
					+ " targetRoleId:" + toRoleId
					+ " targetRoleName:" + prop.getRolename()
					+ " blackListNum:" + info.getPingbiroles().size());
		}
		return true;
	}
}
