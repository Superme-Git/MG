/**
 * 
 */
package fire.pb.lock;

import fire.pb.lock.constants.LockConstants;
import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 返回角色安全锁信息
 *
 */
public class PSendLockInfo extends Procedure {

	private long roleid;
	
	public PSendLockInfo(long roleid){
		this.roleid = roleid;
	}
	
	@Override
	public boolean process(){
		//获取用户安全锁信息,没有初始化信息,直接返回true
		xbean.RoleLockInfo info = xtable.Rolesafelock.get(roleid);
		if(info == null){
			info = xbean.Pod.newRoleLockInfo();
			info.setPassword("");
			info.setUnlocktime(0);
			info.setForceunlocktime(0);
			info.setErrortimes(0);
			info.setFullerrortime(0);
			xtable.Rolesafelock.insert(roleid, info);
			SLockInfo msg = new SLockInfo(LockConstants.CANCEL_LOCK);
			mkdb.Procedure.psendWhileCommit(roleid, msg);
			return true;
		}
		//先同步强行解锁信息
		long now = System.currentTimeMillis();
		LockService.getInstance().resetForceUnlock(info, now, roleid);
		//发送安全锁信息
		SLockInfo msg = LockService.getInstance().getSLockInfo(info, now);
		mkdb.Procedure.psendWhileCommit(roleid, msg);
		return true;
	}
}
