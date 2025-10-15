/**
 * 
 */
package fire.pb.pingbi.service;

import fire.pb.friends.OffLineMessageManager;
import fire.pb.pingbi.BlackRoleInfo;
import fire.pb.pingbi.SBlackRoles;
import fire.pb.pingbi.SearchBlackRoleInfo;

/**
 * 
 * 屏蔽系统服务类
 *
 */
public class PingBiService {

	private static final PingBiService instance = new PingBiService();

	public static PingBiService getInstance() {

		return instance;
	}

	private PingBiService() {

	}
	
	/**
	 * 发送黑名单角色列表信息
	 */
	public void sendBlackRoleList(xbean.RolePingBiRolesInfo info,long roleId){
		SBlackRoles msg = new SBlackRoles();
		for(long blackRoleId : info.getPingbiroles().keySet()){
			xbean.Properties prop = xtable.Properties.select(blackRoleId);
			if(prop != null){
				BlackRoleInfo blackRoleInfo = this.toBlackRoleInfo(blackRoleId, prop);
				msg.blackroles.add(blackRoleInfo);
			}
		}
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	/**
	 * 封装黑名单角色信息
	 * @param roleId
	 * @param prop
	 * @return
	 */
	public BlackRoleInfo toBlackRoleInfo(long roleId,xbean.Properties prop){
		BlackRoleInfo info = new BlackRoleInfo();
		info.roleid = roleId;
		info.name = prop.getRolename();
		info.level = (short)prop.getLevel();
		info.school = (byte)prop.getSchool();
		info.shape = prop.getShape();
		return info;
	}
	
	/**
	 * 封装搜索返回的角色信息
	 * @param roleId
	 * @param prop
	 * @return
	 */
	public SearchBlackRoleInfo toSearchBlackRoleInfo(long roleId,xbean.Properties prop){
		SearchBlackRoleInfo info = new SearchBlackRoleInfo();
		boolean onlineFlag = OffLineMessageManager.getOffLineMsgManager().isOnline(roleId);
		int flag = onlineFlag == true?1:0;
		info.roleid = roleId;
		info.name = prop.getRolename();
		info.rolelevel = (short)prop.getLevel();
		info.school = (byte)prop.getSchool();
		info.shape = prop.getShape();
		info.online = (byte)flag;
		return info;
	}
}
