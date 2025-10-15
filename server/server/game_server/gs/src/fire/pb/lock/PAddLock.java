/**
 * 
 */
package fire.pb.lock;

import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 设置安全锁
 * @author nobody
 *
 */
public class PAddLock extends Procedure {

	private long roleid;
	private String password;
	
	public PAddLock(long roleid,String password){
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
		//设置安全锁
		LockService.getInstance().addLock(info, roleid, password);
		return true;
	}
}
