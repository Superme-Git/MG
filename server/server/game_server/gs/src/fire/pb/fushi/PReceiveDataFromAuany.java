package fire.pb.fushi;

import gnet.DataBetweenAuAnyAndGS;
import gnet.DeliveryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.RoleConfigManager;
import fire.pb.common.SCommon;
import fire.pb.game.MoneyType;
import fire.pb.item.Pack;
import fire.pb.main.Gs;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import xbean.User;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;


public class PReceiveDataFromAuany extends mkdb.Procedure
{
	private static Logger logger=Logger.getLogger("RECHARGE");
	
	private static Map<Long, Integer> tempGet = new ConcurrentHashMap<Long, Integer>(1000);
	
	private DataBetweenAuAnyAndGS protocol;

	public PReceiveDataFromAuany(DataBetweenAuAnyAndGS protocol) {
		this.protocol = protocol;
	}
	
	@Override
	protected boolean process() throws Exception {
		logger.info("PReceiveDataFromAuany.qtype:" + protocol.qtype + ",user:" + protocol.userid);
		xbean.User xuser = xtable.User.select(protocol.userid);
		if(xuser == null || xuser.getIdlist().isEmpty()) {
			logger.error("PReceiveDataFromAuany.xdb.User dont have the user:" + protocol.userid);
			return false;
		}
		xbean.AUUserInfo userinfo = xtable.Auuserinfo.select(protocol.userid);
		if(userinfo == null) {
			logger.error("PReceiveDataFromAuany.xdb.AUUserInfo dont have the user:" + protocol.userid);
			return false;
		}
		//long roleid = xuser.getIdlist().get(0); //这里获取角色id便于后面各个分支使用回客户端消息
		long roleid = xuser.getPrevloginroleid();
		if(protocol.qtype == 9002) {
			final SCommon cc = RoleConfigManager.getRoleCommonConfig(309);
			if(cc == null){
				logger.error("PReceiveDataFromAuany.充值返还等级配置309为空！roleid:" + roleid);
				return false;
			}
			int rolelevel = xtable.Properties.selectLevel(roleid);
			if(rolelevel < (cc == null ? 0 : Integer.parseInt(cc.getValue()))) {
				logger.error("PReceiveDataFromAuany.角色" + roleid + "等级不符合配置!roleid:" + roleid);
				return false;
			}
			//发消息给客户端
			String str = "";
			if (protocol.info instanceof OctetsStream) {
				OctetsStream stream = new OctetsStream(protocol.info);
				str = stream.unmarshal_String("UTF-8");
			}else if (protocol.info instanceof Octets) {
				str = protocol.info.getString("UTF-8");
			}
			String[] items = str.split("#");
			logger.info("PReceiveDataFromAuany.user:" + protocol.userid + ",role:" + roleid + ",info:" + str);
			if(items.length < 1) {
				logger.error("PReceiveDataFromAuany.角色" + roleid + ",sdkserver传输参数错误<1!");
				return false;
			}
			else {
				SReqChargeRefundsInfo snd = new SReqChargeRefundsInfo();
				if(items[0].equals("1")){
					//可以领取
					snd.result = 1;
					snd.chargevalue = Integer.parseInt(items[1]);
					snd.charge = Integer.parseInt(items[2]) * 10;
				} else if(items[0].equals("2")){
					//领取过，不能领取
					snd.result = 2;
					snd.chargevalue = Integer.parseInt(items[1]);
					snd.charge = Integer.parseInt(items[2]) * 10;
				} else if(items[0].equals("0")){
					//无资格，不能领取
					snd.result = 0;
					snd.chargevalue = 0;
				} else {
					//未知错误
					snd.result = 3;
					snd.chargevalue = 0;
				}
				logger.info("PReceiveDataFromAuany.send SReqChargeRefundsInfo.result=" + snd.result + ",roleid:" + roleid);
				psendWhileCommit(roleid, snd);
			}
		} else if (protocol.qtype == 9004) {
			final SCommon cc = RoleConfigManager.getRoleCommonConfig(309);
			if(cc == null){
				logger.error("PReceiveDataFromAuany.充值返还等级配置309为空！");
				return false;
			}
			int rolelevel = xtable.Properties.selectLevel(roleid);
			if(rolelevel < (cc == null ? 0 : Integer.parseInt(cc.getValue()))) {
				logger.error("PReceiveDataFromAuany.角色" + roleid + "等级不符合配置!");
				return false;
			}
			//加东西，发消息客户端
			String str = "";
			if (protocol.info instanceof OctetsStream) {
				OctetsStream stream = new OctetsStream(protocol.info);
				str = stream.unmarshal_String("UTF-8");
			}else if (protocol.info instanceof Octets) {
				str = protocol.info.getString("UTF-8");
			}
			String[] items = str.split("#");
			logger.info("PReceiveDataFromAuany.user:" + protocol.userid + ",role:" + roleid + ",info:" + str);
			if(items.length < 1) {
				logger.error("PReceiveDataFromAuany.角色" + roleid + ",sdkserver传输参数错误<1!");
				return false;
			}
			else {
				SGetChargeRefunds snd = new SGetChargeRefunds();
				if(items[0].equals("1")){
					//加符石
					int chargeRe = Integer.parseInt(items[1]);
					int charge = Integer.parseInt(items[2]);
					xbean.Properties rp = xtable.Properties.get(roleid);//锁一下角色，保证后面的逻辑只有一个玩家线程执行
					if(!PReceiveDataFromAuany.tempGet.containsKey(roleid)){
						if(!new PAddFuShi(protocol.userid, roleid, chargeRe, FushiConst.CASH_FUSHI, YYLoggerTuJingEnum.tujing_Value_xitongzengsong).call()){
							snd.result = 3;
							logger.error("PReceiveDataFromAuany.user:" + protocol.userid + ",role:" + roleid + ",PAddFuShi失败!");
						}
						else {
							DataBetweenAuAnyAndGS ausnd = new DataBetweenAuAnyAndGS();
							ausnd.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
							ausnd.qtype = 9005;
							ausnd.userid = protocol.userid;
							OctetsStream temp = new OctetsStream();
							temp.marshal(userinfo.getUsername());
							temp.marshal(Gs.serverid);
							ausnd.info = temp;
							DeliveryManager.getInstance().send(ausnd);
							logger.info("PReceiveDataFromAuany.send DataBetweenAuAnyAndGS.9005! roleid:" + roleid);
							
							//领取成功 
							snd.result = 1;
							logger.info("PReceiveDataFromAuany.user:" + protocol.userid + ",role:" + roleid + ",领取返还成功!返还额:" + chargeRe + ",曾经充值额:" + charge);
							PReceiveDataFromAuany.tempGet.put(roleid, 1);
							
							final fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
							bag.addSysCurrency((long)(chargeRe * fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_EXCHANGE))
									, MoneyType.MoneyType_EreditPoint, "符石返还", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, 0);
							
							new PAddVipExp(roleid, charge * 10, PAddVipExp.REASON_ADD_VIP_EXP_SYSTEM_RET).call();
						}
					} else {
						logger.warn("PReceiveDataFromAuany.多次收到领取返还符石消息！roleid:" + roleid + "," + rp.getRolename());
						return false;
					}
				} else if(items[0].equals("2")){
					//领取过，不能领取
					snd.result = 2;
				} else if(items[0].equals("0")){
					//无资格，不能领取
					snd.result = 0;
				} else {
					//未知错误
					snd.result = 3;
				}
				logger.info("PReceiveDataFromAuany.send SGetChargeRefunds.result=" + snd.result + " roleid:" + roleid);
				psendWhileCommit(roleid, snd);
			}
		}
		return true;
	}
	
	/**
	 * @author cn
	 */
	protected Pack getBag(long roleid) {
		return new fire.pb.item.Pack(roleid, false);
	}

	/**
	 * @author cn
	 */
	protected User getUser() {
		return xtable.User.select(protocol.userid);
	}


	/**
	 * @author cn
	 */
	protected List<String> getPreGiftKeys(JSONObject json, int size) {
		int i = 0;
		List<String> giftKeysList = new ArrayList<String>();
		for (Object key : json.keySet()) {
			if (i++ >= size) {
				break;
			}
			giftKeysList.add((String) key);

		}
		return giftKeysList;
	}


	protected void requestGetGfit(String info) {
		DataBetweenAuAnyAndGS msg = new DataBetweenAuAnyAndGS();
		msg.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
		msg.userid = protocol.userid;
		msg.qtype = 1007;
		msg.info = Octets.wrap(info, "UTF-8");
		DeliveryManager.getInstance().send(msg);
		logger.debug("userId "+protocol.userid + "requestPlatGift "+ info);
	}
	
}
