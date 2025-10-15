package fire.pb.tel.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import fire.pb.SCBGUpCheckCode;
import fire.pb.talk.MessageMgr;

/**
 * 验证码工具类
 * 
 * @author yangtao
 * @dateTime 2016年9月18日 下午3:51:05
 * @version 1.0
 */
public class CheckCodeUtils {

	// 日志
	private static final Logger checkCodeLog = Logger.getLogger("SYSTEM");

	// 玩家对应的藏金阁状态
	public static Map<Long, Integer> roleId2CJBMap = new ConcurrentHashMap<Long, Integer>();

	/**
	 * 发送验证码
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月12日 下午4:12:58
	 * @version 1.0
	 * @param roleid
	 * @param checkCodeType
	 */
	public static void getCheckCode(long roleid, byte checkCodeType) {
		long tel = 0l;
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null) {
			tel = prop.getBindtel();
		}
		if (tel <= 0l) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(",没有绑定手机号");
			checkCodeLog.error(sbd.toString());
			return;
		}
		TelBindUtils.sendMsg(roleid, tel, System.currentTimeMillis(), false, checkCodeType);
	}

	/**
	 * 发送藏宝阁短信验证码
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午4:00:23
	 * @version 1.0
	 * @param roleid
	 * @param code
	 */
	public static void sendCBGCheckCode(long roleid, String code) {
		SCBGUpCheckCode sCBGUpCheckCode = new SCBGUpCheckCode();
		sCBGUpCheckCode.status = 0;
		if (checkcode(roleid, code)) {
			roleId2CJBMap.put(roleid, 1);
			sCBGUpCheckCode.status = 1;
			TelBindUtils.clearData(roleid);
		}
		gnet.link.Onlines.getInstance().send(roleid, sCBGUpCheckCode);
	}

	/**
	 * 短信验证码检测
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午4:06:50
	 * @version 1.0
	 * @param roleId
	 * @param code
	 * @return
	 */
	private static boolean checkcode(long roleId, String code) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			long tel = prop.getBindtel();
			CodeBean cb = getCodeBean(roleId, tel);
			if (cb == null) {
				return false;
			}
			if (code == null || !cb.code.equals(code.trim())) {
				MessageMgr.sendMsgNotify(roleId, 191014, null);
				checkCodeLog.info("验证码不对" + roleId);
				return false;
			}
		}
		return true;
	}

	/**
	 * 得到短信验证码缓存数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月19日 下午1:31:43
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @return
	 */
	public static CodeBean getCodeBean(long roleId, long tel) {
		CodeBean cb = getCodeBeanNoMessage(roleId, tel);
		if (cb == null) {
			MessageMgr.sendMsgNotify(roleId, 191012, null);
			checkCodeLog.info("验证码已经失效,请重新获取" + roleId);
			return null;
		}
		return cb;
	}

	/**
	 * 得到短信验证码缓存数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月19日 下午1:31:43
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @return
	 */
	public static CodeBean getCodeBeanNoMessage(long roleId, long tel) {
		Map<Long, CodeBean> tel2CodeBeanMap = TelBindUtils.roleId2TelCodeBeanMap.get(roleId);
		if (tel2CodeBeanMap == null) {
			return null;
		}
		CodeBean cb = tel2CodeBeanMap.get(tel);
		if (cb == null) {
			return null;
		}
		return cb;
	}

	/**
	 * 下线清除数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午6:58:37
	 * @version 1.0
	 * @param roleid
	 */
	public static void cleanData(long roleid) {
		cleanDataByCJG(roleid);
		GoodsSafeLocksUtils.cleanDataByGoodsLock(roleid);
	}

	/**
	 * 下线清除藏金阁临时数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午6:59:22
	 * @version 1.0
	 * @param roleid
	 */
	private static void cleanDataByCJG(long roleid) {
		roleId2CJBMap.remove(roleid);
	}

	/**
	 * 判断藏金阁是否能上架
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午7:02:26
	 * @version 1.0
	 * @param roleid
	 */
	public static boolean isCanUpByCJG(long roleid) {
		if (roleId2CJBMap.containsKey(roleid)) {
			return true;
		}
		StringBuilder sbd = new StringBuilder();
		sbd.append("role=").append(roleid).append(",藏金阁上架的时候没有进行短信验证");
		checkCodeLog.error(sbd.toString());
		return false;
	}
}
