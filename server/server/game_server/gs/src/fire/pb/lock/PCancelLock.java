/**
 * 
 */
package fire.pb.lock;

import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 取消密码(安全锁设置)
 *
 */
public class PCancelLock extends Procedure {

	private long roleid;
	private String password;
	
	public PCancelLock(long roleid,String password){
		this.roleid = roleid;
		this.password = password;
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
		//解锁
		LockService.getInstance().cancelLock(info, password, roleid);
		return true;
	}
}
