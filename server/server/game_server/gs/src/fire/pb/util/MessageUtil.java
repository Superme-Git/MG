package fire.pb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mkdb.Transaction;
import fire.pb.PropRole;
import fire.pb.activity.award.Items;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardObjs;
import fire.pb.game.MoneyType;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.map.SceneManager;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;

public class MessageUtil {

	public static long sendRepeatableMsg(int msgid, long startTime,
			long endTime, long delay, List<String> paras) {

		long initialDelay = startTime - System.currentTimeMillis();
		if (initialDelay <= 0) {
			initialDelay = 1000;
		}
		ScheduledFuture<?> future = mkdb.Mkdb.executor().scheduleAtFixedRate(
				new SendMsg(msgid, paras), initialDelay, delay,
				TimeUnit.MILLISECONDS);
		long key = XdbUtil.addScheduleTask(future);
		// 添加停止发送公告的定时任务
		initialDelay = endTime - System.currentTimeMillis();
		if (initialDelay <= 0) {
			initialDelay = 1000;
		}
		mkdb.Mkdb.executor().schedule(new CancelMsg(key), initialDelay,
				TimeUnit.MILLISECONDS);
		return key;

	}

	public static void sendBroadcastMsg(int msgid, long delay,
			List<String> paras) {
		if (delay <= 0)
			delay = 1000;
		mkdb.Mkdb.executor().schedule(new SendMsg(msgid, paras), delay,
				TimeUnit.MILLISECONDS);
	}

	public static void cancelRepeatablemsg(final List<Long> keys) {

		mkdb.Procedure proc = new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				for (Long key : keys) {
					XdbUtil.cancelScheduleTask(key);
				}
				return true;
			}

		};
		if (Transaction.current() == null)
			proc.submit();
		else
			proc.call();

	}

	public static String getAwardString(Map<Integer, RewardObjs> awardItems,
			boolean onlyItem) {
		StringBuffer result = new StringBuffer();
		for (Entry<Integer, RewardObjs> awarditem : awardItems.entrySet()) {
			switch (awarditem.getKey()) {
			case RewardMgr.EXP_AWARD:
				if (!onlyItem)
					result.append(awarditem.getValue().getValue())
							.append("点经验").append(",");
				break;
			case RewardMgr.MONEY_AWARD:
				if (!onlyItem)
					result.append(awarditem.getValue().getValue())
							.append("两金钱").append(",");
				break;
			case RewardMgr.FIRSTC_AWARD: {
				for (Items item : awarditem.getValue().getItems()) {
					String num = String.valueOf(item.getItemNum());
					ItemShuXing attr = Module.getInstance().getItemManager()
							.getAttr(item.getItemID());
					if (attr == null) {
						continue;
					}
					String unit = attr.getUnit();
					String name = attr.getName();
					result.append(num).append(unit).append(name).append(",");
				}
				break;
			}
			case RewardMgr.SECONDC_AWARD:
				for (Items item : awarditem.getValue().getItems()) {
					String num = String.valueOf(item.getItemNum());
					ItemShuXing attr = Module.getInstance().getItemManager()
							.getAttr(item.getItemID());
					if (attr == null) {
						continue;
					}
					String unit = attr.getUnit();
					String name = attr.getName();
					result.append(num).append(unit).append(name).append(",");
				}
				break;
			default:
				break;
			}
		}
		if (result.length() > 0)
			result.deleteCharAt(result.length() - 1);// 去掉最后一个逗号
		return result.toString();
	}

	public static List<String> getMsgParaList(String... paras) {
		List<String> result = new ArrayList<String>();
		for (String para : paras)
			result.add(para);
		return result;
	}

	public static void sendServantMsg(long roleid, int msgid, long guestroleid,
			List<String> paras) {
		java.util.ArrayList<com.locojoy.base.Octets> octetsparas = new java.util.ArrayList<com.locojoy.base.Octets>();
		if (paras != null) {
			for (String parameter : paras) {
				octetsparas.add(MessageMgr.convertString2Octets(parameter));
			}
		}
	}

	public static void sendServantMsgToRoles(Set<Long> roleids, int msgid,
			long guestroleid, List<String> paras) {
		java.util.ArrayList<com.locojoy.base.Octets> octetsparas = new java.util.ArrayList<com.locojoy.base.Octets>();
		if (paras != null) {
			for (String parameter : paras) {
				octetsparas.add(MessageMgr.convertString2Octets(parameter));
			}
		}
	}

	public static void pSendServantMsgWhileCommit(long roleid, int msgid,
			List<String> paras) {
		java.util.ArrayList<com.locojoy.base.Octets> octetsparas = new java.util.ArrayList<com.locojoy.base.Octets>();
		if (paras != null) {
			for (String parameter : paras) {
				octetsparas.add(MessageMgr.convertString2Octets(parameter));
			}
		}
	}

	public static void psendAddItemWhileCommit(long roleid, int itemid,
			int added) {
		List<String> paras = getItemMsgParas(itemid, added);
		MessageMgr
				.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_ITEM, paras);
	}

	public static List<String> getItemMsgParas(final int itemid, final int num) {
		List<String> paras = new ArrayList<String>(4);

		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemid);
		if (attr == null) {
			return paras;
		}

		String name = attr.getName();
		String unit = attr.getUnit();
		paras.add(Integer.toString(num));
		paras.add(unit);
		paras.add(name);
		if (attr.getNamecolor() != null) {
			paras.add(attr.getNamecolor());
		}

		return paras;
	}

	public static String getItemMsgParas2(int itemid, int num) {
		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemid);
		if (attr == null) {
			return "";
		}
		String name = attr.getName();
		String unit = attr.getUnit();

		return Integer.toString(num) + unit + name;
	}

	public static String getItemName(int itemid) {
		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemid);
		if (attr == null) {
			return "";
		} else {
			return attr.getName();
		}
	}

	public static void psendRemoveItemWhileCommit(long roleid, int itemid,
			int removed) {

		List<String> paras = getItemMsgParas(itemid, removed);
		MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.LOSE_ITEM,
				paras);
	}

	public static void psendAddorRemoveMoney(long roleid, long num) {
		if (num == 0) {
			return;
		}
		String str = Long.toString(Math.abs(num));
		List<String> paras = getMsgParaList(str);
		if (num > 0) {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_MONEY,
					paras);
		} else {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.LOSE_MONEY,
					paras);
		}
	}

	public static void psendAddorRemoveGold(long roleid, long num) {
		if (num == 0) {
			return;
		}
		String str = Long.toString(Math.abs(num));
		List<String> paras = getMsgParaList(str);
		if (num > 0) {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_GOLD,
					paras);
		} else {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.LOSE_GOLD,
					paras);
		}
	}

	public static void psendAddorRemoveContribution(long roleid, long num) {
		if (num == 0) {
			return;
		}
		String str = Long.toString(Math.abs(num));
		List<String> paras = getMsgParaList(str);
		if (num > 0) {
			MessageMgr.psendMsgNotifyWhileCommit(roleid,
					MessageMgr.ADD_CONTRIBUTION, paras);
		} else {
			MessageMgr.psendMsgNotifyWhileCommit(roleid,
					MessageMgr.LOSE_CONTRIBUTION, paras);
		}
	}

	public static void psendAddorRemoveSMoney(long roleid, long num) {
		if (num == 0) {
			return;
		}
		String str = Long.toString(Math.abs(num));
		List<String> paras = getMsgParaList(str);
		if (num > 0) {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_SMONEY,
					paras);
		} else {
			MessageMgr.psendMsgNotifyWhileCommit(roleid,
					MessageMgr.LOSE_SMONEY, paras);
		}
	}

	public static void pSendAddOrRemoveCurrency(final long roleid,
			final long num, final int type) {
		if (num == 0) {
			return;
		}
		String str = Long.toString(Math.abs(num));
		List<String> paras = getMsgParaList(str);

		int messageAddId = 0;
		int messageDecId = 0;

		switch (type) {
		case MoneyType.MoneyType_SilverCoin:
			messageAddId = 160049;
			messageDecId = 160052;
			break;
		case MoneyType.MoneyType_GoldCoin:
			messageAddId = 160050;
			messageDecId = 160053;
			break;
		case MoneyType.MoneyType_HearthStone:
			messageAddId = 160120;
			messageDecId = 160129;
			break;
		case MoneyType.MoneyType_ProfContribute:
			messageAddId = 160051;
			messageDecId = 160054;
			break;
		case MoneyType.MoneyType_RongYu:
			messageAddId = 160121;
			messageDecId = 160130;
			break;
		case MoneyType.MoneyType_FactionContribute:
			messageAddId = 160064;
			messageDecId = 160065;
			break;
		case MoneyType.MoneyType_ShengWang:
			messageAddId = 160123;
			messageDecId = 160132;
			break;
		case MoneyType.MoneyType_FestivalPoint:
			messageAddId = 160124;
			messageDecId = 160133;
			break;
		case MoneyType.MoneyType_GoodTeacherVal:
			messageAddId = 160125;
			messageDecId = 160134;
			break;
		case MoneyType.MoneyType_RoleExp:
			messageAddId = 160126;
			messageDecId = 160135;
			break;
		case MoneyType.MoneyType_Activity:
			messageAddId = 160127;
			messageDecId = 160136;
			break;
		case MoneyType.MoneyType_Energy:
			messageAddId = 160128;
			messageDecId = 160137;
			break;
		case MoneyType.MoneyType_EreditPoint:
			messageAddId = 191023;
			break;
		default:
			break;
		}

		if (num > 0) {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, messageAddId, paras);
		} else {
			MessageMgr.psendMsgNotifyWhileCommit(roleid, messageDecId, paras);
		}
	}

	public static String getRoleNames(Collection<Long> roleids) {
		StringBuilder sb = new StringBuilder();
		for (Long rid : roleids) {
			PropRole pRole = new PropRole(rid, true);
			sb.append(pRole.getName());
			sb.append("、");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}

class SendMsg implements Runnable {

	private final int msgid;
	private final List<String> paras;

	public SendMsg(int msgid, List<String> paras) {

		this.msgid = msgid;
		this.paras = paras;
	}

	@Override
	public void run() {
		int msgidMorethan20 = FireProp.getIntValue("activity",
				"mtgserv.impexam.msg.impexamStart");
		if (msgid == 142527) {
			STransChatMessageNotify2Client ssmgLess20 = MessageMgr
					.getMsgNotify(142527, 0, null);
			SceneManager
					.psendAllByConditionWhileCommit(ssmgLess20, -1, -1, -1,
							FireProp.getIntValue("activity",
									"mtgserv.impexam.minLevel") - 1);
		} else if (msgid == msgidMorethan20) {
			STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(
					msgidMorethan20, 0, null);
			SceneManager.psendAllByConditionWhileCommit(ssmg, -1, -1,
					FireProp.getIntValue("activity", "mtgserv.impexam.minLevel"),
					-1);
		} else {
			STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(
					msgid, 0, paras);
			SceneManager.sendAll(ssmg);
		}
	}

}

class CancelMsg implements Runnable {

	private final long key;

	public CancelMsg(long key) {

		this.key = key;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				return XdbUtil.cancelScheduleTask(key);
			}

		}.submit();

	}

}