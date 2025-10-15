/**
 * 
 */
package fire.pb.lock.service;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.lock.SAddLockSuc;
import fire.pb.lock.SCancelLockSuc;
import fire.pb.lock.SChangePasswordSuc;
import fire.pb.lock.SForceUnlockSuc;
import fire.pb.lock.SLockInfo;
import fire.pb.lock.SUnlockSuc;
import fire.pb.lock.SUpdateLockInfo;
import fire.pb.lock.constants.LockConstants;

/**
 * 用户安全锁方法服务类
 * @author nobody
 *
 */
public class LockService {
	
	private static final LockService instance = new LockService();

	public static LockService getInstance() {

		return instance;
	}

	private LockService() {

	}

	/**
	 * 检查用户安全锁是否处于未设置或解锁状态
	 * @return
	 */
	public boolean checkLockStatus(long roleId){
		//获取用户安全锁信息,没有初始化信息,直接返回true
		xbean.RoleLockInfo info = xtable.Rolesafelock.get(roleId);
		if(info == null){
			info = xbean.Pod.newRoleLockInfo();
			info.setPassword("");
			info.setUnlocktime(0);
			info.setForceunlocktime(0);
			info.setErrortimes(0);
			info.setFullerrortime(0);
			xtable.Rolesafelock.insert(roleId, info);
			return true;
		}
		//安全锁是处于未设置状态,直接返回
		if(info.getPassword().equals("")){
			return true;
		}
		//判断是否处于强制解锁状态,时间是否到了
		long now = System.currentTimeMillis();
		if(info.getForceunlocktime() > 0){
			//到时间了，强制解锁密码,直接返回true,并发送提示
			if((now - info.getForceunlocktime()) >= LockConstants.FORCE_UNLOCK_TIME){
				info.setForceunlocktime(0);
				info.setPassword("");
				info.setUnlocktime(0);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145382, null);
				return true;
			}
		}
		//判断是否处于解锁状态
		if((now - info.getUnlocktime()) <= LockConstants.UNLOCK_TIME){
			return true;
		}
		return false;
	}
	
	/**
	 * 更新用户安全锁状态,用户登录时调用此方法
	 * @param roleId
	 */
	public void updateLockStatus(long roleId){
		//获取用户安全锁信息,没有初始化信息
		xbean.RoleLockInfo info = xtable.Rolesafelock.get(roleId);
		if(info == null){
			info = xbean.Pod.newRoleLockInfo();
			info.setPassword("");
			info.setUnlocktime(0);
			info.setForceunlocktime(0);
			info.setErrortimes(0);
			info.setFullerrortime(0);
			xtable.Rolesafelock.insert(roleId, info);
		}
		long now = System.currentTimeMillis();
		//每次登录都要将已解锁状态充值未未解锁状态
		if((now - info.getUnlocktime()) <= LockConstants.UNLOCK_TIME){
			info.setUnlocktime(0);
		}
		//判断强制解锁时间是否到
		if(info.getForceunlocktime() > 0){
			if((now - info.getForceunlocktime()) >= LockConstants.FORCE_UNLOCK_TIME){
				info.setForceunlocktime(0);
				info.setPassword("");
				info.setUnlocktime(0);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145382, null);
			}
		}
	}
	
	/**
	 * 同步强行解锁信息,每次返回安全锁信息时都要判断到达时间强制清除密码
	 * @param info
	 */
	public boolean resetForceUnlock(xbean.RoleLockInfo info,long time,long roleId){
		//判断强制解锁时间是否到
		if(info.getForceunlocktime() > 0){
			if((time - info.getForceunlocktime()) >= LockConstants.FORCE_UNLOCK_TIME){
				info.setForceunlocktime(0);
				info.setPassword("");
				info.setUnlocktime(0);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145382, null);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置安全锁
	 * @param info
	 * @param roleId
	 * @return
	 */
	public void addLock(xbean.RoleLockInfo info,long roleId,String password){
		long now = System.currentTimeMillis();
		this.resetForceUnlock(info, now,roleId);
		//已经设置了安全锁
		if(!info.getPassword().equals("")){
			return;
		}
		info.setPassword(password);
		//发送未解锁状态
		SUpdateLockInfo lockInfo = new SUpdateLockInfo(LockConstants.NOT_UNLOCK);
		mkdb.Procedure.psendWhileCommit(roleId, lockInfo);
		//发送提示:设置密码成功
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145349, null);
		//发送设置成功的消息
		SAddLockSuc msg = new SAddLockSuc();
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	/**
	 * 取消安全锁
	 * @param info
	 * @param roleId
	 * @param password
	 */
	public void cancelLock(xbean.RoleLockInfo info,String password,long roleId){
		long now = System.currentTimeMillis();
		boolean flag = this.resetForceUnlock(info,now,roleId);
		//没有设置密码不需要解锁
		if(info.getPassword().equals("")){
			if(flag){
				SLockInfo msg = new SLockInfo(LockConstants.CANCEL_LOCK);
				mkdb.Procedure.psendWhileCommit(roleId, msg);
			}
			return;
		}
		//判断密码是否连续输入错误达上限，并且在冷却时间内
		if(info.getErrortimes() >= LockConstants.ERROR_PASSWORD_TIMES){
			//连续输入5次,并在冷却时间内
			if((now - info.getFullerrortime() <= LockConstants.ERROR_PASSWORD_TIME)){
				//您连续输入错误5次，请等待10分钟后再次取消安全锁。
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145361, null);
				return;
			}else{//过了冷却,重置连续错误次数
				info.setErrortimes(0);
			}
		}
		//密码错误
		if(!info.getPassword().equals(password)){
			//累计错误次数
			info.setErrortimes(info.getErrortimes() + 1);
			if(info.getErrortimes() >= LockConstants.ERROR_PASSWORD_TIMES){
				info.setFullerrortime(now);
			}
			//您输入的密码不正确，请再次输入。
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145358, null);
			return;
		}
		//密码清空,清空解锁时间和强行解锁时间,连续输入错误次数
		info.setPassword("");
		info.setUnlocktime(0);
		info.setForceunlocktime(0);
		info.setErrortimes(0);
		//发送提示:你的安全锁已经取消
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145357, null);
		//重新推送安全锁状态
		SUpdateLockInfo lockInfo = new SUpdateLockInfo(LockConstants.CANCEL_LOCK);
		mkdb.Procedure.psendWhileCommit(roleId, lockInfo);
		//发送取消成功的消息
		SCancelLockSuc msg = new SCancelLockSuc();
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	/**
	 * 解锁
	 * @param info
	 * @param password
	 * @return
	 */
	public void unlock(xbean.RoleLockInfo info,String password,long roleId){
		long now = System.currentTimeMillis();
		boolean flag = this.resetForceUnlock(info,now,roleId);
		//没有设置密码不需要解锁
		if(info.getPassword().equals("")){
			if(flag){
				SLockInfo msg = new SLockInfo(LockConstants.CANCEL_LOCK);
				mkdb.Procedure.psendWhileCommit(roleId, msg);
			}
			return;
		}
		//判断密码是否连续输入错误达上限，并且在冷却时间内
		if(info.getErrortimes() >= LockConstants.ERROR_PASSWORD_TIMES){
			//连续输入5次,并在冷却时间内
			if((now - info.getFullerrortime() <= LockConstants.ERROR_PASSWORD_TIME)){
				//您连续输入错误5次，请等待10分钟后再次解锁。
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145359, null);
				return;
			}else{//过了冷却,重置连续错误次数
				info.setErrortimes(0);
			}
		}
		//密码错误
		if(!info.getPassword().equals(password)){
			//累计错误次数
			info.setErrortimes(info.getErrortimes() + 1);
			if(info.getErrortimes() >= LockConstants.ERROR_PASSWORD_TIMES){
				info.setFullerrortime(now);
			}
			//您输入的密码错误，解锁失败。
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145352, null);
			return;
		}
		//设置解锁时间,清空强行解锁状态和连续错误次数
		info.setUnlocktime(now);
		if(info.getForceunlocktime() > 0){
			info.setForceunlocktime(0);
		}
		info.setErrortimes(0);
		//发送提示:解锁成功
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145351, null);
		//发送已解锁状态
		SUpdateLockInfo lockInfo = new SUpdateLockInfo(LockConstants.HAVE_UNLOCK);
		mkdb.Procedure.psendWhileCommit(roleId, lockInfo);
		//发送解锁成功的消息
		SUnlockSuc msg = new SUnlockSuc();
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	/**
	 * 强行解锁
	 * @param info
	 * @param roleId
	 * @return
	 */
	public void forceUnlock(xbean.RoleLockInfo info,long roleId,int userId){
		long now = System.currentTimeMillis();
		boolean flag = this.resetForceUnlock(info,now,roleId);
		//没有设置密码不需要解锁
		if(info.getPassword().equals("")){
			if(flag){
				SLockInfo msg = new SLockInfo(LockConstants.CANCEL_LOCK);
				mkdb.Procedure.psendWhileCommit(roleId, msg);
			}
			return;
		}
		//已经申请了强制解锁
		if(info.getForceunlocktime() > 0){
			//您已经处于强行解锁队列，请勿再次点击强行解锁。
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145362, null);
			return;
		}
		//扣符石
		if(!FushiManager.subFushiFromUser(userId, roleId,
				LockConstants.FORCE_UNLOCK_NEED_GOLD, 0, 0, FushiConst.REASON_SUB_FORCE_UNLOCK, YYLoggerTuJingEnum.tujing_Value_forceunlock, false)){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145381, null);
			return;
		}
		//设置强行解锁时间
		info.setForceunlocktime(now);
		//发送提示：申请强行解锁成功
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145372, null);
		//获取当前状态并发送
		SUpdateLockInfo lockInfo = this.getSUpdateLockInfo(info, now);
		mkdb.Procedure.psendWhileCommit(roleId, lockInfo);
		//发送成功申请强行解锁的消息
		SForceUnlockSuc msg = new SForceUnlockSuc();
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	/**
	 * 修改密码
	 * @param info
	 * @param oldPassword
	 * @param newPassword
	 * @param roleId
	 */
	public void changePassword(xbean.RoleLockInfo info,String oldPassword,String newPassword,long roleId){
		long now = System.currentTimeMillis();
		boolean flag = this.resetForceUnlock(info,now,roleId);
		//没有设置密码不能修改
		if(info.getPassword().equals("")){
			if(flag){
				SLockInfo msg = new SLockInfo(LockConstants.CANCEL_LOCK);
				mkdb.Procedure.psendWhileCommit(roleId, msg);
			}
			return;
		}
		//新密码不能为空
		if("".equals(newPassword)){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145345, null);
			return;
		}
		//判断密码是否连续输入错误达上限，并且在冷却时间内
		if(info.getErrortimes() >= LockConstants.ERROR_PASSWORD_TIMES){
			//连续输入5次,并在冷却时间内
			if((now - info.getFullerrortime() <= LockConstants.ERROR_PASSWORD_TIME)){
				//您连续输入错误5次，请等待10分钟后再次修改密码。
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145360, null);
				return;
			}else{//过了冷却,重置连续错误次数
				info.setErrortimes(0);
			}
		}
		//旧密码错误
		if(!info.getPassword().equals(oldPassword)){
			//累计错误次数
			info.setErrortimes(info.getErrortimes() + 1);
			if(info.getErrortimes() >= LockConstants.ERROR_PASSWORD_TIMES){
				info.setFullerrortime(now);
			}
			//您输入的旧密码不正确，请再次输入。
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145354, null);
			return;
		}
		//设置新密码，清空连续错误次数
		info.setPassword(newPassword);
		info.setErrortimes(0);
		//发送提示:修改密码成功！
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145356, null);
		//获取当前状态并发送
		SUpdateLockInfo lockInfo = this.getSUpdateLockInfo(info, now);
		mkdb.Procedure.psendWhileCommit(roleId, lockInfo);
		//发送修改密码成功的消息
		SChangePasswordSuc msg = new SChangePasswordSuc();
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	/**
	 * 封装安全锁信息
	 * @param info
	 * @return
	 */
	public SLockInfo getSLockInfo(xbean.RoleLockInfo info,long time){
		int status = -1;
		if(info.getPassword().equals("")){
			status = LockConstants.CANCEL_LOCK;//未设置安全锁状态
		}else{
			if((time - info.getUnlocktime()) >= LockConstants.UNLOCK_TIME){
				status = LockConstants.NOT_UNLOCK;//未解锁
			}else{
				status = LockConstants.HAVE_UNLOCK;//已解锁
			}
		}
		SLockInfo msg = new SLockInfo(status);
		return msg;
	}
	
	/**
	 * 清除解锁时间,供GM使用
	 * @param roleId
	 */
	public void clearUnlockTime(long roleId){
		xbean.RoleLockInfo info = xtable.Rolesafelock.get(roleId);
		if(info == null){
			info = xbean.Pod.newRoleLockInfo();
			info.setPassword("");
			info.setUnlocktime(0);
			info.setForceunlocktime(0);
			info.setErrortimes(0);
			info.setFullerrortime(0);
			xtable.Rolesafelock.insert(roleId, info);
		}
		info.setUnlocktime(0);
	}
	
	/**
	 * 封装更新的安全锁信息
	 * @param info
	 * @return
	 */
	public SUpdateLockInfo getSUpdateLockInfo(xbean.RoleLockInfo info,long time){
		int status = -1;
		if(info.getPassword().equals("")){
			status = LockConstants.CANCEL_LOCK;//未设置安全锁状态
		}else{
			if((time - info.getUnlocktime()) >= LockConstants.UNLOCK_TIME){
				status = LockConstants.NOT_UNLOCK;//未解锁
			}else{
				status = LockConstants.HAVE_UNLOCK;//已解锁
			}
		}
		SUpdateLockInfo msg = new SUpdateLockInfo(status);
		return msg;
	}
}
