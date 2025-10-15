package fire.pb.activity.exchangecode;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fire.pb.game.MoneyType;
//import fire.pb.util.DecodeBase64;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

/**
 * 兑换码
 * 
 * @author user
 *
 */
public class PExchangeCodeAuReturn extends Procedure {
	private Logger logger = Logger.getLogger("SYSTEM");

	private long roleid;
	private String result;
	
	public static final int EXCHANGECODE_REWARD_CURRENCY  = 1;	//货币
	public static final int EXCHANGECODE_REWARD_ITEMS	  = 2;	//道具
	public static final int EXCHANGECODE_REWARD_EQUIP	  = 3;	//装备
	public static final int EXCHANGECODE_REWARD_GEM		  = 4;	//宝石
	public static final int EXCHANGECODE_REWARD_PET_EQUIP = 5;	//宠物装备
	
	public PExchangeCodeAuReturn(long roleid, String result) {
		super();
		this.roleid = roleid;
		this.result = result;
	}

	@Override
	protected boolean process() throws Exception {
		logger.info(roleid + "recv AU return exchangecode info:" + result);
		if (result == null || result.length() == 0) {
			logger.info(roleid + "recv AU return exchangecode info is empty");
			return false;
		}
		JSONObject jobj = JSONObject.fromObject(result);
		SExchangeCode send = new SExchangeCode();
		if (!jobj.has("Code")) {
			send.flag = 99;
			psend(roleid, send);
			return false;
		}

		int code = jobj.getInt("Code");
		send.flag = code;
		if (code != 1) {
			psend(roleid, send);
			return false;
		}

		int count = jobj.getInt("Count");
		JSONArray awards = JSONArray.fromObject(jobj.get("Awards"));
		logger.info(roleid + "recv AU return exchangecode item info: " + awards.toString());
		if (awards.size() != count) {
			send.flag = 99;
			psend(roleid, send);
			return false;
		}

		// 暂时没有活动介绍相关信息
		// String base64Info = jobj.getString("ActivityInfo");
		// String activityInfo =
		// DecodeBase64.transform(base64Info.getBytes()).toString();
		// if(activityInfo.length() != 0){
		// }

		ArrayList<ExchangeCodeRetInfo> itemList = new ArrayList<ExchangeCodeRetInfo>();
		send.iteminfos = itemList;
		psend(roleid, send);
		for (int i = 0; i < awards.size(); i++) {
			jobj = awards.getJSONObject(i);
			String[] award = jobj.getString("Award").split("\\|");
			if (award.length < 4) {
				continue;
			}

			int itemtype = Integer.parseInt(award[0]);
			int itemid = Integer.parseInt(award[1]);
			long itemcount = Long.parseLong(award[2]);
			boolean isbind = (Integer.parseInt(award[3]) == 1) ? true : false;

			if (itemcount <= 0) {
				logger.error(roleid + "recv AU return item count error itemId = " + itemid + "count = " + itemcount);
				continue;
			}

			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			switch (itemtype) {
			case EXCHANGECODE_REWARD_CURRENCY: // 货币
				long addedCurrency = bag.addSysCurrency(itemcount, itemid, "兑换码", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_libaoduihuan, 0);
				// 符石特殊处理[addSysCurrency]内部已经发送过消息给客户端了, 所以排外
				if (itemid != MoneyType.MoneyType_HearthStone && addedCurrency > 0)
					MessageUtil.pSendAddOrRemoveCurrency(roleid, itemcount, itemid);
				break;
			case EXCHANGECODE_REWARD_ITEMS: // 道具
			case EXCHANGECODE_REWARD_EQUIP: // 装备
			case EXCHANGECODE_REWARD_GEM: // 宝石
			case EXCHANGECODE_REWARD_PET_EQUIP: // 宠物装备
				int addedItem = -1;
				if (isbind)
					addedItem = BagUtil.addBindItem(roleid, (int) itemid, (int) itemcount, "兑换码", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_libaoduihuan, 0, true);
				else
					addedItem = BagUtil.addItem(roleid, (int) itemid, (int) itemcount, "兑换码", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_libaoduihuan, 0);

				if (addedItem > 0)
					MessageUtil.psendAddItemWhileCommit(roleid, (int) itemid, (int) itemcount);
				break;
			default:
				logger.error(roleid + "recv AU return non-type item type itemType =" + itemtype);
				break;
			}
		}
		return true;
	}
}