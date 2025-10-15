/**
 * 
 */
package fire.pb.lock;

import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 修改安全锁密码
 *
 */
public class PChangePassword extends Procedure {

	private long roleid;
	private String oldPassword;
	private String newPassword;
	
	public PChangePassword(long roleid,String oldPassword,String newPassword){
		this.roleid = roleid;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
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
		LockService.getInstance().changePassword(info, oldPassword, newPassword,roleid);
		return true;
	}
}
