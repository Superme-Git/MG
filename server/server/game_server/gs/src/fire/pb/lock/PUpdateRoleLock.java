/**
 * 
 */
package fire.pb.lock;

import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 处理用户登录清空解锁信息
 *
 */
public class PUpdateRoleLock extends Procedure {

	private long roleid;
	
	public PUpdateRoleLock(long roleid){
		this.roleid = roleid;
	}
	
	@Override
	public boolean process(){
		//同步安全锁信息
		LockService.getInstance().updateLockStatus(roleid);
		return true;
	}
}
