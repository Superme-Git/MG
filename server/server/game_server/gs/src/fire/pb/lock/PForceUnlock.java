/**
 * 
 */
package fire.pb.lock;

import fire.pb.PropRole;
import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 强行解锁
 *
 */
public class PForceUnlock extends Procedure {

	private long roleid;
	
	public PForceUnlock(long roleid){
		this.roleid = roleid;
	}
	
	@Override
	public boolean process(){
		//获取用户安全锁信息,没有初始化信息
		xbean.RoleLockInfo info = xtable.Rolesafelock.get(roleid);
		if(info == null){
			info = xbean.Pod.newRoleLockInfo();
			info.setPassword("");
			info.setUnlocktime(0);
			info.setForceunlocktime(0);
			info.setErrortimes(0);
			info.setFullerrortime(0);
			xtable.Rolesafelock.insert(roleid, info);
		}
		PropRole role = new PropRole(roleid, true);
		int userid = role.getUserid();
		//强行解锁
		LockService.getInstance().forceUnlock(info,roleid,userid);
		return true;
	}
}
