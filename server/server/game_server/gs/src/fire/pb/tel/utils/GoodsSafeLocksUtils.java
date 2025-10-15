package fire.pb.tel.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mkdb.Procedure;
import mkdb.Procedure.Task;
import mkdb.Transaction;
import mkio.Protocol;

import org.apache.log4j.Logger;

import fire.pb.SCancelForceDelPassword;
import fire.pb.SChangeGoodLockState;
import fire.pb.SCloseGoodLocks;
import fire.pb.SDelPassword;
import fire.pb.SForceDelPassword;
import fire.pb.SForceUnlockTimeExpire;
import fire.pb.SGetGoodLocksInfo;
import fire.pb.SGoodUnLock;
import fire.pb.SOpenGoodLocks;
import fire.pb.SResetPassword;
import fire.pb.SSetPassword;
import fire.pb.common.SCommon;
import fire.pb.item.ItemBase;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;

/**
 * 道具安全锁逻辑处理
 * 
 * @author yangtao
 * @dateTime 2016年9月12日 下午1:28:53
 * @version 1.0
 */
public class GoodsSafeLocksUtils {

	// 日志
	private static final Logger GoodsSafeLocksLog = Logger.getLogger("SYSTEM");

	/** 强行解锁有效时间 */
	public static final long FORCE_UNLOCK_TIME = 10 * 24 * 60 * 60 * 1000;

	/** 连续输入密码错误不能再输入密码的有效时间 */
	public static final long ERROR_PASSWORD_TIME = 24 * 60 * 60 * 1000;

	// 玩家对应的道具解锁状态状态
	public static Map<Long, Integer> roleId2LockStateMap = new ConcurrentHashMap<Long, Integer>();

	/**
	 * 设置密码
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午1:27:11
	 * @version 1.0
	 * @param roleid
	 * @param initpd
	 * @param repeatpd
	 */
	public static void setPassword(long roleid, String initpd, String repeatpd) {
		// 保护期内
		if (isInForceDelPassword(roleid)) {
			return;
		}
		SSetPassword sSetPassword = new SSetPassword();
		sSetPassword.status = 0;
		// 验证密码格式
		boolean isTrue = regexPassword(initpd);
		if (!isTrue) {
			MessageMgr.sendMsgNotify(roleid, 191024, null);
			gnet.link.Onlines.getInstance().send(roleid, sSetPassword);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",密码格式不对，应该是字母或者数字组成的4到12位");
			GoodsSafeLocksLog.error(sbd.toString());
			return;
		}
		if (initpd == null || repeatpd == null || !initpd.equals(repeatpd)) {
			MessageMgr.sendMsgNotify(roleid, 191025, null);
			gnet.link.Onlines.getInstance().send(roleid, sSetPassword);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",密码不一致");
			GoodsSafeLocksLog.error(sbd.toString());
			return;
		}
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					String password = prop.getPassword();
					if (!"".equals(password)) {
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleid).append(",密码已经设置了");
						GoodsSafeLocksLog.error(sbd.toString());
						mkdb.Procedure.psendWhileCommit(roleid, sSetPassword);
						return false;
					}
					prop.setPassword(initpd);
					prop.setIsopensafelock(1);
					sSetPassword.status = 1;
					mkdb.Procedure.psendWhileCommit(roleid, sSetPassword);
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 密码格式验证
	 *
	 * @param number
	 * @return
	 */
	private static boolean regexPassword(String password) {
		if (StrNULL(password)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{4,12}$");
		Matcher matcher = pattern.matcher(password);
		return matcher.find();
	}

	/**
	 * 非空验证
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:15:06
	 * @version 1.0
	 * @param str
	 * @return
	 */
	private static boolean StrNULL(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 重新设置密码
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午2:19:34
	 * @version 1.0
	 * @param roleid
	 * @param initpd
	 * @param repeatpd
	 * @param repeatpd2
	 */
	public static void reSetPassword(long roleid, String initpd, String newtpd, String repeatpd) {
		// 保护期内
		if (isInForceDelPassword(roleid)) {
			return;
		}
		SResetPassword sResetPassword = new SResetPassword();
		sResetPassword.status = 0;
		// 验证密码格式
		boolean isTrue = regexPassword(newtpd);
		if (!isTrue) {
			MessageMgr.sendMsgNotify(roleid, 191024, null);
			gnet.link.Onlines.getInstance().send(roleid, sResetPassword);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",重新设置新密码格式不对，应该是字母或者数字组成的4到12位");
			GoodsSafeLocksLog.error(sbd.toString());
			return;
		}
		if (newtpd == null || repeatpd == null || !newtpd.equals(repeatpd)) {
			MessageMgr.sendMsgNotify(roleid, 191025, null);
			gnet.link.Onlines.getInstance().send(roleid, sResetPassword);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",密码不一致");
			GoodsSafeLocksLog.error(sbd.toString());
			return;
		}
		if(initpd.equals(newtpd)){
			MessageMgr.sendMsgNotify(roleid, 191043, null);
			gnet.link.Onlines.getInstance().send(roleid, sResetPassword);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",新密码与原密码不可一致");
			GoodsSafeLocksLog.error(sbd.toString());
			return;
		}
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					boolean isTrue = checkPassword(roleid, initpd, sResetPassword, prop);
					if (isTrue) {
						prop.setPassword(newtpd);
						prop.setIsopensafelock(1);
						sResetPassword.status = 1;
						mkdb.Procedure.psendWhileCommit(roleid, sResetPassword);
					}
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 自动开始安全模式
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午2:26:14
	 * @version 1.0
	 * @param roleid
	 */
	public static void autoOpenGoodLocks(long roleid) {
		xbean.Properties prop = xtable.Properties.get(roleid);
		if (prop != null) {
			long forcedelendtime = prop.getForcedelendtime();
			// 保护期内
			if (System.currentTimeMillis() - forcedelendtime < 0) {
				return;
			}
			System.out.println(prop.getIsopensafelock());
			//如果没有设置密码，并且已经开启了就不用处理
			String password = prop.getPassword();
			if (password == null || "".equals(password) || prop.getIsopensafelock() == 1) {
				return;
			}
			SOpenGoodLocks sOpenGoodLocks = new SOpenGoodLocks();
			prop.setIsopensafelock(1);
			sOpenGoodLocks.status = 1;
			mkdb.Procedure.psendWhileCommit(roleid, sOpenGoodLocks);
		}
	}

	/**
	 * 开始安全模式
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午2:26:14
	 * @version 1.0
	 * @param roleid
	 */
	public static void openGoodLocks(long roleid) {
		// 保护期内
		if (isInForceDelPassword(roleid)) {
			return;
		}
		SOpenGoodLocks sOpenGoodLocks = new SOpenGoodLocks();
		sOpenGoodLocks.status = 0;
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					String password = prop.getPassword();
					if (password == null || "".equals(password)) {
						MessageMgr.sendMsgNotify(roleid, 191029, null);
						mkdb.Procedure.psendWhileCommit(roleid, sOpenGoodLocks);
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleid).append(",你还没有设置密码，不能开始安全模式");
						GoodsSafeLocksLog.error(sbd.toString());
						return false;
					}
					prop.setIsopensafelock(1);
					sOpenGoodLocks.status = 1;
					mkdb.Procedure.psendWhileCommit(roleid, sOpenGoodLocks);
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 关闭道具安全模式
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午2:38:54
	 * @version 1.0
	 * @param roleid
	 * @param password
	 */
	public static void closeGoodLocks(long roleid, String password, byte closetype) {
		// 保护期内
		if (isInForceDelPassword(roleid)) {
			return;
		}
		SCloseGoodLocks sCloseGoodLocks = new SCloseGoodLocks();
		sCloseGoodLocks.status = 0;
		sCloseGoodLocks.closetype = closetype;
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					String oldPassword = prop.getPassword();
					if (oldPassword == null || password == null || !oldPassword.equals(password)) {
						MessageMgr.sendMsgNotify(roleid, 191026, null);
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleid).append(",密码输入不对，不能关闭安全锁定模式");
						GoodsSafeLocksLog.error(sbd.toString());
						mkdb.Procedure.psendWhileCommit(roleid, sCloseGoodLocks);
						return false;
					}
					prop.setIsopensafelock(0);
					sCloseGoodLocks.status = 1;
					mkdb.Procedure.psendWhileCommit(roleid, sCloseGoodLocks);
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 解除安全锁
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午2:50:14
	 * @version 1.0
	 * @param roleid
	 * @param initpd
	 * @param repeatpd
	 */
	public static void unSafelock(long roleid, String initpd, String repeatpd) {
		// 保护期内
		if (isInForceDelPassword(roleid)) {
			return;
		}
		SDelPassword cDelPassword = new SDelPassword();
		cDelPassword.status = 0;
		if (initpd == null || repeatpd == null || !initpd.equals(repeatpd)) {
			MessageMgr.sendMsgNotify(roleid, 191025, null);
			gnet.link.Onlines.getInstance().send(roleid, cDelPassword);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",密码不一致");
			GoodsSafeLocksLog.error(sbd.toString());
			return;
		}
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					boolean isTrue = checkPassword(roleid, initpd, cDelPassword, prop);
					if (isTrue) {
						prop.setPassword("");
						cDelPassword.status = 1;
						mkdb.Procedure.psendWhileCommit(roleid, cDelPassword);
					}
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 取消强制删除密码申请
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午3:06:59
	 * @version 1.0
	 * @param code
	 */
	public static void cancelForceUnSafelock(long roleid) {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					SCancelForceDelPassword sCancelForceDelPassword = new SCancelForceDelPassword();
					prop.setForcedelpdtime(0l);
					prop.setForcedelendtime(0l);
					sCancelForceDelPassword.status = 1;
					mkdb.Procedure.psendWhileCommit(roleid, sCancelForceDelPassword);
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 强制删除密码
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午3:34:05
	 * @version 1.0
	 * @param roleid
	 * @param code
	 */
	public static void forceUnSafelock(long roleid, String code) {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					long tel = prop.getBindtel();
					CodeBean codeBean = null;
					String checkCode = null;
					if (tel > 0l) {
						codeBean = CheckCodeUtils.getCodeBean(roleid, tel);
						if (codeBean == null) {
							return false;
						}
						checkCode = codeBean.code;
					}
					if (tel > 0l && (checkCode == null || code == null || !checkCode.equals(code))) {
						MessageMgr.sendMsgNotify(roleid, 191014, null);
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleid).append(",验证码不对");
						GoodsSafeLocksLog.error(sbd.toString());
						return false;
					}
					long time = System.currentTimeMillis();
					SForceDelPassword sForceDelPassword = new SForceDelPassword();
					prop.setForcedelpdtime(time);
					prop.setForcedelendtime(time + FORCE_UNLOCK_TIME);
					sForceDelPassword.starttimepoint = time;
					sForceDelPassword.finishtimepoint = time + FORCE_UNLOCK_TIME;
					mkdb.Procedure.psendWhileCommit(roleid, sForceDelPassword);
					TelBindUtils.clearData(roleid);
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 输入错误密码最大次数
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午3:57:00
	 * @version 1.0
	 * @return
	 */
	private static int getMaxTimesByErrorPassword() {
		SCommon conf = ConfigManager.getInstance().getConf(SCommon.class).get(444);
		if (conf == null) {
			GoodsSafeLocksLog.error("通用数据表数据错误，不存在编号" + 444);
			return -1;
		}
		return Integer.parseInt(conf.getValue());
	}

	/**
	 * 输入密码达到最大次数后锁定时间
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午3:57:15
	 * @version 1.0
	 * @return
	 */
	private static int getErrorPasswordLockTime() {
		SCommon conf = ConfigManager.getInstance().getConf(SCommon.class).get(445);
		if (conf == null) {
			GoodsSafeLocksLog.error("通用数据表数据错误，不存在编号" + 445);
			return -1;
		}
		return Integer.parseInt(conf.getValue());
	}

	/**
	 * 得到道具安全锁信息
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午4:49:08
	 * @version 1.0
	 * @param roleid
	 */
	public static void getGoodLocksInfo(long roleid) {
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null) {
			SGetGoodLocksInfo sGetBindTel = new SGetGoodLocksInfo(prop.getPassword(), prop.getForcedelpdtime(),
					prop.getForcedelendtime(), (byte) (prop.getLogintimesofday() > 1 ? 0 : 1), (byte) (prop.getErrortimes()),
					prop.getLockendtime(), (byte) prop.getIsopensafelock());
			gnet.link.Onlines.getInstance().send(roleid, sGetBindTel);
		}
	}

	/**
	 * 是否在强制删除密码保护期内
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午5:11:36
	 * @version 1.0
	 * @param roleid
	 * @return
	 */
	public static boolean isInForceDelPassword(long roleid) {
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null) {
			long forcedelendtime = prop.getForcedelendtime();
			if (System.currentTimeMillis() - forcedelendtime >= 0) {
				return false;
			}
		}
		MessageMgr.sendMsgNotify(roleid, 191030, null);
		StringBuilder sbd = new StringBuilder();
		sbd.append("role=").append(roleid).append(",强制删除密码保护期内,不能做此操作");
		GoodsSafeLocksLog.error(sbd.toString());
		return true;
	}

	/**
	 * 道具解锁
	 * 
	 * @param roleId
	 * @param password
	 * @return
	 */
	public static void unlockGoods(long roleId, String password) {
		SGoodUnLock msg = new SGoodUnLock();
		msg.status = 0;
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleId);
				if (prop != null) {
					boolean isTrue = checkPassword(roleId, password, msg, prop);
					if (isTrue) {
						msg.status = 1;
						// 发送解锁成功的消息
						mkdb.Procedure.psendWhileCommit(roleId, msg);
					}
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 验证密码正确性
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 上午10:01:02
	 * @version 1.0
	 * @param roleId
	 * @param password
	 * @param msg
	 * @param prop
	 * @return
	 */
	private static boolean checkPassword(long roleId, String password, Protocol msg, xbean.Properties prop) {
		long now = System.currentTimeMillis();
		// 没有设置密码不需要解锁
		if (prop.getPassword().equals("")) {
			mkdb.Procedure.psendWhileCommit(roleId, msg);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(",没有设置密码");
			GoodsSafeLocksLog.error(sbd.toString());
			return false;
		}
		int maxErrorTimes = getMaxTimesByErrorPassword();
		// 判断密码是否连续输入错误达上限，并且在冷却时间内
		if (prop.getErrortimes() >= maxErrorTimes) {
			// 连续输入5次,并在冷却时间内
			if ((now - prop.getLockendtime() <= getErrorPasswordLockTime() * DateValidate.hourMills)) {
				// 您连续输入错误5次，请等待一天后再次解锁。
				MessageMgr.sendMsgNotify(roleId, 191027, null);
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(",您输入错误的次数已经超过5次，24小时内不可继续输入也不可进行该操作");
				GoodsSafeLocksLog.error(sbd.toString());
				mkdb.Procedure.psendWhileCommit(roleId, msg);
				return false;
			} else {// 过了冷却,重置连续错误次数
				prop.setErrortimes(0);
			}
		}
		// 密码错误
		if (!prop.getPassword().equals(password)) {
			int errorTimes = prop.getErrortimes() + 1;
			// 累计错误次数
			prop.setErrortimes(errorTimes);
			if (prop.getErrortimes() >= maxErrorTimes) {
				prop.setLockendtime(now);
				// 您输入的密码错误，解锁失败。
				MessageMgr.sendMsgNotify(roleId, 191027, null);
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(",您输入错误的次数已经超过5次，24小时内不可继续输入也不可进行该操作");
				GoodsSafeLocksLog.error(sbd.toString());
			} else {
				// 您输入的密码错误，解锁失败。
				List<String> parameters = new ArrayList<>();
				parameters.add(String.valueOf(maxErrorTimes - errorTimes));
				MessageMgr.sendMsgNotify(roleId, 191028, parameters);
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(",您输入错误的次数已经超过5次，24小时内不可继续输入也不可进行该操作");
				GoodsSafeLocksLog.error(sbd.toString());
			}
			mkdb.Procedure.psendWhileCommit(roleId, msg);
			return false;
		}
		// 设置解锁时间,清空强行解锁状态和连续错误次数
		prop.setLockendtime(0);
		prop.setErrortimes(0);
		return true;
	}

	/**
	 * 通知强制解除安全锁时间到期
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月19日 上午10:37:53
	 * @version 1.0
	 * @param roleid
	 */
	public static void forceUnlockTimeExpire(long roleid) {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					long now = System.currentTimeMillis();
					SForceUnlockTimeExpire sForceUnlockTimeExpire = new SForceUnlockTimeExpire();
					// 一分钟误差
					if (now + 60000 >= prop.getForcedelendtime()) {
						sForceUnlockTimeExpire.status = 1;
						prop.setPassword("");
						prop.setForcedelpdtime(0l);
						prop.setForcedelendtime(0l);
					} else {
						sForceUnlockTimeExpire.status = 0;
						GoodsSafeLocksLog.error("强制解除安全锁时间未到" + roleid);
					}
					mkdb.Procedure.psendWhileCommit(roleid, sForceUnlockTimeExpire);
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 检查用户安全锁是否处于未设置或解锁状态
	 * 
	 * @return
	 */
	public static boolean checkLockStatus(long roleId, xbean.PetInfo petInfo) {
		if (petInfo.getTreasure() == 1) {
			return checkLockStatus(roleId);
		}
		return false;
	}

	/**
	 * 检查用户安全锁是否处于未设置或解锁状态
	 * 
	 * @return
	 */
	public static boolean checkLockStatus(long roleId, ItemBase item) {
		if (item.isTreasure()) {
			return checkLockStatus(roleId);
		}
		return false;
	}

	/**
	 * 检查用户安全锁是否处于未设置或解锁状态
	 * 
	 * @return
	 */
	public static boolean checkLockStatus(long roleId) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			// 安全锁是处于未设置状态,直接返回
			if (prop.getPassword().equals("") || prop.getIsopensafelock() == 0) {
				return false;
			}
			if (roleId2LockStateMap.containsKey(roleId)) {
				return false;
			}
		}
		GoodsSafeLocksLog.error("开启了道具安全锁，需要输入密码" + roleId);
		return true;
	}

	/**
	 * 下线清除道具锁临时数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午6:59:22
	 * @version 1.0
	 * @param roleid
	 */
	public static void cleanDataByGoodsLock(long roleid) {
		roleId2LockStateMap.remove(roleid);
	}

	/**
	 * 存储过程提交后清除数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月20日 上午10:40:15
	 * @version 1.0
	 * @param roleid
	 */
	public static void doClearDataWhileCommit(long roleid) {
		if (roleId2LockStateMap.containsKey(roleid)) {
			SChangeGoodLockState sChangeGoodLockState = new SChangeGoodLockState();
			if (Transaction.current() != null) {
				Procedure.ppostWhileCommit(new LogWhileProcedureEnd(roleid));
				mkdb.Procedure.psendWhileCommit(roleid, sChangeGoodLockState);
			} else {
				cleanDataByGoodsLock(roleid);
				gnet.link.Onlines.getInstance().send(roleid, sChangeGoodLockState);
			}
		}
	}

	public static class LogWhileProcedureEnd extends Task {

		private long roleid;

		public LogWhileProcedureEnd(long roleid) {
			super();
			this.roleid = roleid;
		}

		@Override
		public void run() {
			cleanDataByGoodsLock(roleid);
		}
	}
}
