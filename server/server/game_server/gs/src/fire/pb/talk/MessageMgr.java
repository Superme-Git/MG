package fire.pb.talk;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.locojoy.base.Octets;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.clan.ClanUtils;
import fire.pb.friends.OffLineMessageManager;
import fire.pb.friends.SSendSystemMessageToRole;
import fire.pb.gm.GMInteface;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SceneManager;
import fire.pb.move.SceneState;
import fire.pb.util.FireProp;
import gnet.link.Onlines;
import mkdb.Transaction;

/**
 * 服务器向客户发送消息工具类
 */
public class MessageMgr {

	public static final int ADD_CONTRIBUTION = FireProp.getIntValue("message", "mtgserv.addContribution");
	public static final int ADD_EXP = FireProp.getIntValue("message", "mtgserv.addExp");
	public static final int ADD_GOLD = FireProp.getIntValue("message", "mtgserv.addGold");
	public static final int ADD_ITEM = FireProp.getIntValue("message", "mtgserv.addItem");
	public static final int ADD_MONEY = FireProp.getIntValue("message", "mtgserv.addMoney");
	public static final int ADD_SMONEY = FireProp.getIntValue("message", "mtgserv.addSMoney");
	public static final int LOSE_CONTRIBUTION = FireProp.getIntValue("message", "mtgserv.loseContribution");
	public static final int LOSE_EXP = FireProp.getIntValue("message", "mtgserv.loseExp");
	public static final int LOSE_GOLD = FireProp.getIntValue("message", "mtgserv.loseGold");
	public static final int LOSE_ITEM = FireProp.getIntValue("message", "mtgserv.loseItem");
	public static final int LOSE_MONEY = FireProp.getIntValue("message", "mtgserv.loseMoney");
	public static final int LOSE_SMONEY = FireProp.getIntValue("message", "mtgserv.loseSMoney");
	public static final int MSG_CONTENT_LEN_MAX = FireProp.getIntValue("message", "mtgserv.msgContentLenMax");
	public final static Properties msgprop = fire.pb.main.ConfigManager.getInstance().getPropConf("message");

	
	/**
	 * 向在线角色广播消息 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId 不是npc提示消息时填0
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void broadcastMsgNotify(int msgId, int npcBaseId, List<String> parameters) {
		gnet.link.Onlines.getInstance().broadcast(getMsgNotify(msgId, npcBaseId, parameters));
	}
	
	public static com.locojoy.base.Octets convertString2LogOctets(String str) {
		try {
			return new Octets(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();// 捕获这个exception，因为"UTF-16LE"不能写错
		}
		return null;
	}

	public static Octets convertString2Octets(String str) {
		try {
			return new Octets(str.getBytes("UTF-16LE"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();// 吞掉这个exception，因为"UTF-16LE"不能写错
		}
		return null;
	}

	public static String getCurrTime(boolean isSave) {
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = fomat.format(System.currentTimeMillis());
		return currentTime;
	}

	/**
	 * 构造STransChatMessageNotify2Client消息,自己发
	 * 
	 * @param npcBaseId 如果不是npc消息,填0即可
	 * @param parameters 可以为null
	 * @return STransChatMessageNotify2Client
	 */
	public static STransChatMessageNotify2Client getMsgNotify(int msgId, int npcBaseId, List<String> parameters) {
		ArrayList<com.locojoy.base.Octets> octetsparas = new ArrayList<com.locojoy.base.Octets>();
		if (parameters != null) {
			for (String parameter : parameters) {
				octetsparas.add(convertString2Octets(parameter));
			}
		}
		return new STransChatMessageNotify2Client(msgId, npcBaseId, octetsparas);
	}

	/**
	 * 传入多个string，生成List
	 */
	public static List<String> getStringList(Object... strs) {
		List<String> list = new ArrayList<String>();
		for (Object str : strs)
			list.add(str.toString());
		return list;
	}

	public static int getTitle(xbean.Properties role) {
		int titleid = role.getTitle();
		if (titleid < 0)
			titleid = 0;
		if (GMInteface.gmPriv(role.getUserid()))
			titleid += 10000;// gm加10000称谓字段
		return titleid;
	}

	/**
	 * 向在线角色广播消息 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId 不是npc提示消息时填0
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void pbroadcastMsgNotify(int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.pbroadcast(getMsgNotify(msgId, npcBaseId, parameters), 999);
	}

	/**
	 * 向在线角色广播消息 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId 不是npc提示消息时填0
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void pbroadcastMsgNotifyWhileCommit(int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.pbroadcastWhileCommit(getMsgNotify(msgId, npcBaseId, parameters), 999);
	}

	/**
	 * 向在线角色广播消息 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId 不是npc提示消息时填0
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void pbroadcastMsgNotifyWhileRollback(int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.pbroadcastWhileRollback(getMsgNotify(msgId, npcBaseId, parameters), 999);
	}

	public static void psendMsgNotify(Collection<Long> roleids, int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.psend(roleids, getMsgNotify(msgId, npcBaseId, parameters));
	}

	/**
	 * 向角色推送消息提示,当推送npc消息时使用,带npcBaseId 
	 * 只能在Prcedure中调用
	 * @param npcBaseId npcBaseId
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotify(long roleId, int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.psend(roleId, getMsgNotify(msgId, npcBaseId, parameters));
	}

	/**
	 * 向角色推送消息提示 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcId
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotify(long roleId, int msgId, List<String> parameters) {
		mkdb.Procedure.psend(roleId, getMsgNotify(msgId, 0, parameters));
	}

	public static void psendMsgNotifyWhileCommit(Collection<Long> roleIds, int msgId, int npcBaseId) {
		mkdb.Procedure.psendWhileCommit(roleIds, getMsgNotify(msgId, npcBaseId, null));
	}

	/**
	 * 向roleId角色推送消息提示,当推送npc消息时使用，带npcBaseId 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId，没有填0即可
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotifyWhileCommit(Collection<Long> roleIds, int msgId, int npcBaseId,
			List<String> parameters) {
		mkdb.Procedure.psendWhileCommit(roleIds, getMsgNotify(msgId, npcBaseId, parameters));
	}

	public static void psendMsgNotifyWhileCommit(long roleId, int msgId) {
		mkdb.Procedure.psendWhileCommit(roleId, getMsgNotify(msgId, 0, null));
	}

	public static void psendMsgNotifyWhileCommit(long roleId, int msgId, int npcbaseid) {
		mkdb.Procedure.psendWhileCommit(roleId, getMsgNotify(msgId, npcbaseid, null));
	}

	/**
	 * 向角色推送消息提示,当推送npc消息时使用,带npcBaseId 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotifyWhileCommit(long roleId, int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.psendWhileCommit(roleId, getMsgNotify(msgId, npcBaseId, parameters));
	}

	/**
	 * 向角色推送消息提示 
	 * 在Prcedure中调用
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotifyWhileCommit(long roleId, int msgId, List<String> parameters) {
		mkdb.Procedure.psendWhileCommit(roleId, getMsgNotify(msgId, 0, parameters));
	}

	public static void psendMsgNotifyWhileRollback(Collection<Long> roleIds, int msgId, int npcBaseId,
			List<String> parameters) {
		mkdb.Procedure.psendWhileRollback(roleIds, getMsgNotify(msgId, npcBaseId, parameters));
	}

	/**
	 * 向角色推送消息提示,当推送npc消息时使用,带npcBaseId 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotifyWhileRollback(long roleId, int msgId, int npcBaseId, List<String> parameters) {
		mkdb.Procedure.psendWhileRollback(roleId, getMsgNotify(msgId, npcBaseId, parameters));
	}

	/**
	 * 向角色推送消息提示 
	 * 只能在Prcedure中调用
	 * 
	 * @param npcBaseId npcBaseId
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void psendMsgNotifyWhileRollback(long roleId, int msgId, List<String> parameters) {
		mkdb.Procedure.psendWhileRollback(roleId, getMsgNotify(msgId, 0, parameters));
	}

	/**
	 * 通过好友聊天框发送系统消息
	 */
	public static void psendSystemMessageToRole(final long roleid, final int contentId, final List<String> param) {
		ArrayList<Octets> octetsList = new ArrayList<Octets>();
		if (param != null) {
			for (String s : param) {
				octetsList.add(convertString2Octets(s));
			}
		}
		SSendSystemMessageToRole message = new SSendSystemMessageToRole();
		message.systemroleid = 0;
		message.contentid = contentId;
		message.contentparam = octetsList;
		if (!OffLineMessageManager.getOffLineMsgManager().isOnline(roleid)) {
			message.time = getCurrTime(true);
			OffLineMessageManager.getOffLineMsgManager().inserteOfflineMsgById(roleid, message);
		} else {
			message.time = getCurrTime(false);
			if (mkdb.Transaction.current() != null) {
				mkdb.Procedure.psendWhileCommit(roleid, message);
			} else {
				gnet.link.Onlines.getInstance().send(roleid, message);
			}
		}
	}

	/**
	 * 通过好友聊天框发送系统消息
	 * <B>存储过程之内调用</B> 
	 */
	public static void psendSystemMessageToRoles(final Set<Long> roleids, final int contentId, final List<String> param) {

		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				ArrayList<Octets> octetsList = new ArrayList<Octets>();
				if (param != null) {
					for (String s : param) {
						octetsList.add(convertString2Octets(s));
					}
				}
				SSendSystemMessageToRole message = new SSendSystemMessageToRole();
				message.systemroleid = 0;
				message.contentid = contentId;
				message.contentparam = octetsList;
				for (long roleid : roleids) {
					if (!OffLineMessageManager.getOffLineMsgManager().isOnline(roleid)) {
						message.time = getCurrTime(true);
						OffLineMessageManager.getOffLineMsgManager().inserteOfflineMsgById(roleid, message);
					} else {
						message.time = getCurrTime(false);
						if (mkdb.Transaction.current() != null) {
							mkdb.Procedure.psendWhileCommit(roleid, message);
						} else {
							gnet.link.Onlines.getInstance().send(roleid, message);
						}
					}
				}
				return true;
			}
		};

		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(proc);
		} else {
			proc.submit();
		}
	}

	/**
	 * 通过好友聊天框发送系统消息
	 */
	public static void sendSystemMessageToRole(long roleid, int contentId, List<String> param) {
		psendSystemMessageToRole(roleid, contentId, param);
	}

	/**
	 * 向角色推送消息提示,当推送npc消息时使用,带npcBaseId
	 * 
	 * @param npcBaseId npcBaseId
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void sendMsgNotify(long roleId, int msgId, int npcBaseId, List<String> parameters) {
		gnet.link.Onlines.getInstance().send(roleId, getMsgNotify(msgId, npcBaseId, parameters));
	}

	/**
	 * 向角色推送消息提示
	 * 
	 * @param parameters 顺序放置的字符参数,可以为null
	 */
	public static void sendMsgNotify(long roleId, int msgId, List<String> parameters) {
		gnet.link.Onlines.getInstance().send(roleId, getMsgNotify(msgId, 0, parameters));
	}

	/**
	 * 向帮派频道发送消息
	 */
	public static void sendMsgToFaction(long roleId, String message, ArrayList<DisplayInfo> displayinfos) {
		xbean.Properties role = xtable.Properties.select(roleId);
		final xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if (null == clanInfo)
			return;
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.roleid = roleId;
		snd.rolename = role.getRolename();
		snd.shapeid = role.getShape();
		snd.messagetype = ChannelType.CHANNEL_CLAN;
		snd.message = message;
		snd.displayinfos = displayinfos;
		snd.titleid = getTitle(role);
		Set<Long> roleids = new HashSet<Long>();
		roleids = clanInfo.getMembers().keySet();
		// 所有成员信息
		boolean inTransaction = Transaction.current() == null ? false : true;
		if (inTransaction) {
			mkdb.Procedure.psendWhileCommit(roleids, snd);
		} else {
			gnet.link.Onlines.getInstance().send(roleids, snd);
		}
	}

	/**
	 * 向角色发送弹出消息
	 */
	public static void sendMsgToPop(long roleId, String message) {
		Role who = RoleManager.getInstance().getRoleByID(roleId);
		if (null == who)
			return;
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.messagetype = ChannelType.CHANNEL_SYSTEM;
		snd.message = message;
		Onlines.getInstance().send(roleId, snd);
	}

	/**
	 * 向职业频道发送消息
	 */
	public static void sendMsgToProfession(long roleId, String message, ArrayList<DisplayInfo> displayinfos) {
		xbean.Properties role = xtable.Properties.select(roleId);
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.roleid = roleId;
		snd.rolename = role.getRolename();
		snd.shapeid = role.getShape();
		snd.messagetype = ChannelType.CHANNEL_PROFESSION;
		snd.message = message;
		snd.displayinfos = displayinfos;
		snd.titleid = getTitle(role);
		SceneManager.sendAllBySchool(snd, role.getSchool());
	}

	/**
	 * 向当前频道发送消息
	 */
	public static void sendMsgToRoleAroundScreen(long roleId, String message, ArrayList<DisplayInfo> displayinfos) {
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.roleid = roleId;
		xbean.Properties role = xtable.Properties.select(roleId);
		snd.rolename = role.getRolename();
		snd.shapeid = role.getShape();
		snd.messagetype = ChannelType.CHANNEL_CURRENT;
		snd.message = message;
		snd.displayinfos = displayinfos;
		snd.titleid = getTitle(role);
		final BuffAgent agent = new BuffRoleImpl(roleId, true);
		if (agent.existState(BuffConstant.StateType.STATE_BATTLE_FIGHTER) || agent.existState(BuffConstant.StateType.STATE_BATTLE_WATCHER)) {
			Long battleid = xtable.Roleid2battleid.select(roleId);
			if (battleid == null) {
				battleid = xtable.Watcherid2battleid.select(roleId);
				if (battleid == null)
					throw new RuntimeException("角色状态战斗中,但找不到战斗id");
			}
			if (Transaction.current() != null) {
				new fire.pb.battle.PSendAllBattleMembers(battleid, snd).call();
			} else {
				new fire.pb.battle.PSendAllBattleMembers(battleid, snd).submit();
			}
		} else {
			List<Integer> exceptstates = new ArrayList<Integer>();
			exceptstates.add(SceneState.BATTLE_WATCHER);
			exceptstates.add(SceneState.BATTLE_FIGHTER);
			exceptstates.add(SceneState.BATTLE_REPLAY);
			SceneManager.sendAroundByCondition(roleId, exceptstates, snd);
		}

	}

	/**
	 * 向系统频道发送消息
	 */
	public static void sendMsgToSysem(String message) {
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.messagetype = ChannelType.CHANNEL_SYSTEM;
		snd.message = message;
		SceneManager.sendAll(snd);
	}

	/**
	 * 向队伍频道发送消息
	 */
	public static void sendMsgToTeam(long roleId, String message, ArrayList<DisplayInfo> displayinfos) {
		final fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
		if (team == null) {
			MessageMgr.sendMsgNotify(roleId, ChatMsgId.CANNOT_USE_TEAM_CHANNEL, null);
			return;
		}
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.roleid = roleId;
		xbean.Properties role = xtable.Properties.select(roleId);
		snd.rolename = role.getRolename();
		snd.shapeid = role.getShape();
		snd.messagetype = ChannelType.CHANNEL_TEAM;
		snd.message = message;
		snd.displayinfos = displayinfos;
		List<Long> allmembers = team.getAllMemberIds();
		allmembers.removeAll(team.getOfflineMemberIds());
		Set<Long> rids = new HashSet<Long>(allmembers);
		snd.titleid = getTitle(role);
		gnet.link.Onlines.getInstance().send(rids, snd);
	}

	/**
	 * 向组队申请频道发送消息
	 */
	public static void sendMsgToTeamApply(long who, String message, ArrayList<DisplayInfo> displayinfos) {
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.roleid = who;
		xbean.Properties role = xtable.Properties.select(who);
		snd.rolename = role.getRolename();
		snd.shapeid = role.getShape();
		snd.messagetype = ChannelType.CHANNEL_TEAM_APPLY;
		snd.message = message;
		snd.displayinfos = displayinfos;
		snd.titleid = getTitle(role);
		fire.pb.map.SceneManager.sendAll(snd);
	}

	/**
	 * 向世界频道发送消息
	 */
	public static void sendMsgToWorld(long roleId, String message, ArrayList<DisplayInfo> displayinfos) {
		final STransChatMessage2Client snd = new STransChatMessage2Client();
		snd.roleid = roleId;
		xbean.Properties role = xtable.Properties.select(roleId);
		snd.rolename = role.getRolename();
		snd.shapeid = role.getShape();
		snd.messagetype = ChannelType.CHANNEL_WORLD;
		snd.message = message;
		snd.displayinfos = displayinfos;
		snd.titleid = getTitle(role);
		fire.pb.map.SceneManager.sendAll(snd);
	}

	/**
	 * 向系统频道发消息
	 */
	public static void sendSystemMsg(int msgId, List<String> param){
		STransChatMessageNotify2Client message = getMsgNotify(msgId, 0, param);
		SceneManager.sendAll(message);
	}

	public static void sendHelpMsg(final long roleid, final int taskid,
			final String taskname, final String rolename) {
		mkdb.Procedure sendHelp = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.RoleRecourse roleRec = xtable.Rolerecourse.get(roleid);
				if (roleRec == null) return false;
				
				//taskid
				xbean.TaskRecourse taskRec = roleRec.getRecoursetask().get(taskid);
				if (taskRec == null) return false;
				
				//content1 = "[任务名字]"
				//content2 = "已经在"
				//content3 = "玩家名字"
				//content4 = "的帮助下完成了。"
				//para2 = 181181
				//para3 = 4
				
				for (Integer msgtype : taskRec.getChannellist()) {
					//组合字符串发送
					StringBuilder sb = new StringBuilder().append("<T t=\"").append(taskname).append("\" key=\"").append(taskid)
							.append("\" channelid=\"").append(msgtype).append("\" c=\"ff00ff00\"></T>").append("<T t=\"").append("已经在")
							.append("\" c=\"FF693F00\"></T>").append("<T t=\"").append(rolename).append("\" c=\"ffff00ff\"></T>")
							.append("<T t=\"").append("的帮助下完成了。").append("\" c=\"FF693F00\"></T>");
					final java.util.ArrayList<fire.pb.talk.DisplayInfo> showinfos = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
					ChatChannel.getInstance().process(roleid, msgtype, sb.toString(), "", showinfos, 2);
				}
				
				roleRec.getRecoursetask().remove(Integer.valueOf(taskid));
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(sendHelp);
		}
		else {
			sendHelp.submit();
		}
	}
}
