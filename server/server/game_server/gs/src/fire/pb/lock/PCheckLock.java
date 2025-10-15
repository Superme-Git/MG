/**
 * 
 */
package fire.pb.lock;

import fire.pb.lock.service.LockService;
import mkdb.Procedure;

/**
 * 检查安全锁状态
 * @author nobody
 *
 */
public class PCheckLock extends Procedure {

	private long roleid;
	
	public PCheckLock(long roleid){
		this.roleid = roleid;
	}
	
	@Override
	public boolean process(){
		//安全锁必须要解锁,发送需要解锁的消息
		if(!LockService.getInstance().checkLockStatus(roleid)){
			SNeedUnlock msg = new SNeedUnlock();
			mkdb.Procedure.psend(roleid, msg);
			return false;
		}
		return true;
	}
}
