/**
 * 
 */
package fire.pb.pingbi;

import fire.pb.pingbi.service.PingBiService;
import mkdb.Procedure;

/**
 * 
 * 发送黑名单列表信息,玩家登录时发送
 * 
 *
 */
public class PSendBlackRolesProc extends Procedure {

	private long roleId;
	
	public PSendBlackRolesProc(long roleId){
		this.roleId = roleId;
	}
	
	protected boolean process() throws Exception {
		xbean.RolePingBiRolesInfo info = xtable.Rolepingbirolesinfos.get(roleId);
		if(info == null){
			info = xbean.Pod.newRolePingBiRolesInfo();
			xtable.Rolepingbirolesinfos.insert(roleId, info);
		}
		PingBiService.getInstance().sendBlackRoleList(info, roleId);
		return true;
	}
}
