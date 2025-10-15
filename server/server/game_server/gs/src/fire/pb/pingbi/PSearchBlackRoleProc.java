/**
 * 
 */
package fire.pb.pingbi;

import fire.pb.pingbi.service.PingBiService;
import mkdb.Procedure;

/**
 * 搜索要屏蔽的玩家
 *
 */
public class PSearchBlackRoleProc extends Procedure {

	private long roleId;
	private long searchRoleId;
	
	public PSearchBlackRoleProc(long roleId,long searchRoleId){
		this.roleId = roleId;
		this.searchRoleId = searchRoleId;
	}
	
	protected boolean process() throws Exception {
		//不能搜索自己
		if(roleId == searchRoleId){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145347, null);
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(searchRoleId);
		//没有查询到角色,发送提示消息
		if(prop == null || prop.getDeletetime()!=0){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145345, null);
			return false;
		}
		//封装角色信息返回给客户端
		SSearchBlackRoleInfo msg = new SSearchBlackRoleInfo();
		msg.searchblackrole = PingBiService.getInstance().toSearchBlackRoleInfo(searchRoleId, prop);
		mkdb.Procedure.psendWhileCommit(roleId, msg);
		return true;
	}
}
