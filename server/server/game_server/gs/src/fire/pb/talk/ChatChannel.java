package fire.pb.talk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.log.YYLogger;
import fire.pb.clan.ClanUtils;
import fire.pb.common.SCommon;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.util.CalendarUtil;
import fire.pb.util.FireProp;
import mkdb.Procedure;
import mkdb.Transaction;
import xbean.ClanMemberInfo;

/**
 * 聊天频道
 */
public class ChatChannel {
	
	public Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
	
	private static ChatChannel instance = null;
	private static Object lock = new Object();
	public static final Logger LOG = Logger.getLogger("CHAT");

	private ChatChannel() {}
	
	public static ChatChannel getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ChatChannel();
				}
			}
		}
		return instance;
	}

	public static boolean isShutUp(long roleId) {
		final Integer userid = xtable.Properties.selectUserid(roleId);
		if (userid == null)
			return true;
		final xbean.forbidinfo forbidinfo = xtable.Userid2forbidtime.select(userid);
		if (forbidinfo != null) {

			long now = System.currentTimeMillis();
			if (forbidinfo.getForbidtime() > now) {
				long ret = forbidinfo.getForbidtime() - now;
				String time = CalendarUtil.getPeriodShortFormat(ret);
				MessageMgr.sendMsgNotify(roleId, 144097, MessageMgr.getStringList(time));
				return true;
			} else {
				mkdb.Procedure p = new mkdb.Procedure() {
					protected boolean process() throws Exception {
						xtable.Userid2forbidtime.remove(userid);
						return true;
					}
				};
				if (Transaction.current() != null)
					mkdb.Procedure.pexecute(p);
				else
					p.submit();
				return false;
			}
		}

		return false;
	}
	
	/**
	 * 当前频道,角色发言等级检查
	 */
	public boolean verifyLevelCurrentChat(final long senderId) {
		xbean.Properties role = xtable.Properties.select(senderId);
		if (role.getLevel() < getCurrentChatLevel()) {
			MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_CURRENT_CHANNEL_LEVEL_LIMIT, null);
			return false;
		}
		return true;
	}

	/**
	 * 当前频道,角色发言间隔时间
	 */
	public boolean verifyDelayTimeCurrentChat(final long senderId) {
		LastChatTime rctime = new LastChatTime(senderId, true);
		long now = System.currentTimeMillis();
		long delaytime = FireProp.getLongValue(MessageMgr.msgprop, "currentchat.delaytime");
		if (rctime.getLastCurrentChatTime() + delaytime > now) {
			MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_SPEED_LIMIT, null);
			return false;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				LastChatTime rctime = new LastChatTime(senderId, false);
				rctime.setLastCurrentChatTime(now);
				return true;
			}
		}.submit();
		return true;
	}

	public boolean verifyClanChat(final long senderId) {
		final xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(senderId, true);
		if (null == clanInfo) {
			MessageMgr.sendMsgNotify(senderId, ChatMsgId.CANNOT_USE_FACTION_CHANNEL, null);
			return false;
		}
		// 判断是否被禁言
		ClanMemberInfo member = clanInfo.getMembers().get(senderId);
		if (member.getIsbannedtalk()) {
			long bannedtalktime = member.getBannedtalktime();
			long leveltime = bannedtalktime - System.currentTimeMillis();
			if (leveltime > 0) {
				// 计算时间
				leveltime = leveltime / 1000 / 60;// 分钟
				if (leveltime == 0) {
					leveltime = 1;// 如果是0分钟，处理为1分钟
				}
				MessageMgr.sendMsgNotify(senderId, 141161, Arrays.asList(leveltime + ""));
				return false;
			}
			member.setIsbannedtalk(false);// 解除禁言
			member.setBannedtalktime(0L);// 重置禁言时间
		}
		/*
		 * // 发言速度控制 final RoleChatMsgTime rctime = new
		 * RoleChatMsgTime(senderId, true); final long now =
		 * System.currentTimeMillis(); final long delaytime =
		 * FireProp.getLongValue(Message.msgprop, "factionchat.delaytime"); if
		 * (rctime.getLastfactionchattime() + delaytime > now) {
		 * Message.sendMsgNotify(senderId, ChatMsgId.CHAT_SPEED_LIMIT, null);
		 * return false; } new xdb.Procedure() {
		 * 
		 * @Override protected boolean process() { final RoleChatMsgTime rctime
		 * = new RoleChatMsgTime(senderId, false);
		 * rctime.setLastfactionchattime(now); return true; } }.submit();
		 */
		return true;
	}

	/**
	 * 职业频道,角色发言频率检查
	 */
	public boolean verifyProfessionChat(final long senderId) {
		xbean.Properties role = xtable.Properties.select(senderId);
		if (role.getLevel() < getSchoolChatLevel()) {
			MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_SCHOOL_CHANNEL_LEVEL_LIMIT, null);
			return false;
		}
		final LastChatTime rctime = new LastChatTime(senderId, true);
		final long now = System.currentTimeMillis();
		final long delaytime = getSchoolChatDelayTime();
		if (rctime.getLastProfessionChatTime() + delaytime > now) {
			MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_SPEED_LIMIT, null);
			return false;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				LastChatTime rctime = new LastChatTime(senderId, false);
				rctime.setLastProfessionChatTime(now);
				return true;
			}
		}.submit();
		return true;
	}
	
	public boolean verifyTeamApplyChat(final long senderId) {
		final long now = System.currentTimeMillis();
		final long delaytime = getTeamApplyChatDelayTime();
		mkdb.Procedure procedure = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				LastChatTime rctime = new LastChatTime(senderId, false);
				if (rctime.getLastTeamApplyChatTime() + delaytime > now) {
					List<String> args = new ArrayList<String>();
					args.add("" + (rctime.getLastTeamApplyChatTime() + delaytime - now) / 1000);
					MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_TEAM_APPLY_CHANNEL_TIME_LIMIT, args);
					return false;
				}
				rctime.setLastTeamApplyChatTime(now);
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			return procedure.call();
		}
		else {
			Future<Procedure> p = procedure.submit();
			boolean succ = false;
			try {
				succ = p.get(2, TimeUnit.SECONDS).isSuccess();
				return succ;
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				LOG.error("", e);
			}
			return succ;
		}
	}

	/***
	 * subType == 1 的时候忽略等级检测 时间频率检测  by changhao
	 * subType == 2的时候忽略等级检测时间频率检测
	 */
	public boolean verifyWorldChatAndSend(final long senderId, final String msg,
			final ArrayList<DisplayInfo> showinfos, int subType) {
		xbean.Properties role = xtable.Properties.select(senderId);
		if (role.getLevel() < getWorldChatLevel()) {
			MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_WORLD_CHANNEL_LEVEL_LIMIT, null);
			return false;
		}
		mkdb.Procedure procedure = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// 发言消耗银币
				if (subType != 2) {
					final long now = System.currentTimeMillis();
					final long delaytime = getWorldChatDelayTime();
					final LastChatTime rctime = new LastChatTime(senderId, false);
					if (rctime.getLastWorldChatTime() + delaytime > now) {
						java.util.ArrayList<String> args = new java.util.ArrayList<String>();
						args.add("" + (rctime.getLastWorldChatTime() + delaytime - now) / 1000);
						MessageMgr.sendMsgNotify(senderId, ChatMsgId.CHAT_WORLD_CHANNEL_TIME_LIMIT, args);
						return false;
					}
					
					if (getCostMoney() > 0)
					{
						Pack bag = new Pack(senderId, false);
						long ret = bag.subMoney(-getCostMoney(), "世界发言消耗银币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_worldchat, 0);
						if (ret == 0) {
							MessageMgr.sendMsgNotify(senderId, 160473, null);
							return false;
						}						
					}
					
					if (getCostEnergy() > 0)
					{
						xbean.Properties prop = xtable.Properties.get(senderId);
						if (prop != null) //消耗活力 by changhao
						{
							if (prop.getEnergy() < getCostEnergy())
							{
								java.util.List<String> p = new java.util.ArrayList<String>();
								Integer v = getCostEnergy();
								p.add(v.toString());
								MessageMgr.sendMsgNotify(senderId, 143432, p);
								return false;
							}
							
							fire.pb.skill.liveskill.LiveSkillManager.getInstance().AddEnergy(senderId, prop, -getCostEnergy(), fire.log.enums.YYLoggerTuJingEnum.tujing_Value_worldchat_enery);
							
							java.util.List<String> p = new java.util.ArrayList<String>();
							Integer v = getCostEnergy();
							p.add(v.toString());							
							MessageMgr.sendMsgNotify(senderId, 160137, p);
						}						
					}

					rctime.setLastWorldChatTime(now);
					
					
					if (getCostMoney() > 0)
					{
						MessageMgr.psendMsgNotifyWhileCommit(senderId, 160375, Arrays.asList(Integer.toString(getCostMoney())));						
					}
				}
				
				MessageMgr.sendMsgToWorld(senderId, msg, showinfos);
				fire.pb.course.CourseManager.achieveUpdate(senderId, fire.pb.course.CourseType.WORLD_SPEAKING_COURSE);
				return true;
			}
		};
		
		boolean succ = false;
		Future<Procedure> p = null;
		if (Transaction.current() != null) 
		{
			succ = procedure.call();		
		}
		else
		{
			p = procedure.submit();
			
			try {
				succ = p.get(2, TimeUnit.SECONDS).isSuccess();
				return succ;
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				LOG.error("如果有超时异常应该是此时比较繁忙,我设定了个2秒等待超时,为了不阻塞长时间占用线程影响其它逻辑.", e);
			}
		}
		
		return succ;
	}

	/**
	 * @return 发言扣除银币数量,获取失败返回0
	 */
	private int getCostMoney() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(271);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 世界频道发言 活力消耗 by changhao
	 */
	private int getCostEnergy() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(337);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 当前频道发言需要等级
	 */
	private int getCurrentChatLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(356);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 世界频道发言需要等级
	 */
	private int getWorldChatLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(357);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 世界频道发言频率
	 */
	public int getWorldChatDelayTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(200);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 职业频道发言需要等级
	 */
	private int getSchoolChatLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(355);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 职业频道发言频率
	 */
	private int getSchoolChatDelayTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(201);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
	
	/**
	 * @return 组队申请频道发言频率
	 */
	private int getTeamApplyChatDelayTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(361);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}

	public Octets getOctets(final long senderId, DisplayInfo showinfo) {
		Octets detailTip = new Octets();
		OctetsStream tip = null;
		switch (showinfo.displaytype) {
		case DisplayInfo.DISPLAY_ITEM:
			if (showinfo.shopid == 0) {
				if (showinfo.counterid != fire.pb.item.BagTypes.BAG
						&& showinfo.counterid != fire.pb.item.BagTypes.EQUIP) {
					if (senderId == showinfo.roleid)
						MessageMgr.sendMsgNotify(senderId, 142599, null);
					else
						MessageMgr.sendMsgNotify(senderId, 142603, null);
					LOG.error("不能获得消息提示信息容器id:" + showinfo.counterid);
					return null;
				}
				final fire.pb.item.ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(showinfo.roleid,
						showinfo.counterid, true);
				final fire.pb.item.ItemBase item = bag.getItem(showinfo.uniqid);
				if (item == null) {
					if (senderId == showinfo.roleid)
						MessageMgr.sendMsgNotify(senderId, 142599, null);
					else
						MessageMgr.sendMsgNotify(senderId, 142603, null);
					LOG.error("不能获得消息提示信息角色id:" + showinfo.roleid + " 容器id:" + showinfo.counterid);
					return null;
				} else {
					// 屏蔽查看聊天频道饰品具体信息
					Octets tipsdata = item.getTips();
					if (tipsdata == null) {
						tipsdata = new Octets();
					}
					detailTip = tipsdata;
				}
			}
			break;

		case DisplayInfo.DISPLAY_PET:
			tip = new OctetsStream();
			final PetColumn petcol = new PetColumn(showinfo.roleid, PetColumnTypes.PET, true);
			final fire.pb.pet.Pet pet = petcol.getPet(showinfo.uniqid);
			if (pet == null) {
				if (senderId == showinfo.roleid)
					MessageMgr.sendMsgNotify(senderId, 142599, null);
				else
					MessageMgr.sendMsgNotify(senderId, 142607, null);
				return null;
			}
			pet.getProtocolPet().marshal(tip);
			detailTip = tip;
			break;

		default:
			break;
		}
		return detailTip;
	}

	public boolean process(final long senderId, final int type, String message, final String checkShiedMessage,
			ArrayList<DisplayInfo> displayInfos, int subType) {
//		if (!fire.pb.util.StringValidateUtil.checkIllegalWord(checkShiedMessage)) {
//			fire.pb.talk.MessageMgr.sendMsgNotify(senderId, 142261, null);
//			return;
//		}
		
		boolean ret = true;
		switch (type) {
		case ChannelType.CHANNEL_WORLD:
			ret = verifyWorldChatAndSend(senderId, message, displayInfos, subType);
			break;
		case ChannelType.CHANNEL_CURRENT:
			if (verifyLevelCurrentChat(senderId)) {
				MessageMgr.sendMsgToRoleAroundScreen(senderId, message, displayInfos);
			}
			break;

		case ChannelType.CHANNEL_PROFESSION:
			if (verifyProfessionChat(senderId)) {
				MessageMgr.sendMsgToProfession(senderId, message, displayInfos);
			}
			break;

		case ChannelType.CHANNEL_CLAN:
			ret = verifyClanChat(senderId);
			if (subType == 1 || ret) {
				MessageMgr.sendMsgToFaction(senderId, message, displayInfos);
				mkdb.Procedure chatprocedure = new mkdb.Procedure() {
					@Override
					protected boolean process() throws Exception {
						fire.pb.course.CourseManager.achieveUpdate(senderId,
								fire.pb.course.CourseType.GUILD_SPEAKING_COURSE);
						return true;
					}
				};
				if (mkdb.Transaction.current() != null) {
					chatprocedure.call();
				} else {
					chatprocedure.submit();
				}
			}
			break;

		case ChannelType.CHANNEL_TEAM:
			MessageMgr.sendMsgToTeam(senderId, message, displayInfos);
			break;

		case ChannelType.CHANNEL_TEAM_APPLY:
			// subType === 1 表示跳过检查，上层已经做了时间检测了 by changhao
			if (subType == 1 || subType == 2 || verifyTeamApplyChat(senderId)) {
				MessageMgr.sendMsgToTeamApply(senderId, message, displayInfos);
			} else {
				ret = false;
			}
			break;

		default:
			break;
		}

		// 运营日志
		writeYYLogger(senderId, type);
		return ret;
	}
	
	/**
	 * 运营日志相关
	 */
	void writeYYLogger(long senderId, int type){
		if(ChannelType.CHANNEL_TEAM == type || ChannelType.CHANNEL_CLAN == type)
			YYLogger.chatLog(senderId, type);
	}
	
//	void transMsgDataForLog(String src, OpChatBean opChatBean){
//		if(src.indexOf("voice link=\"") != -1){		// 语音
//			int index = src.indexOf("time=") + 5;
//			opChatBean.setSendType(OpChatBean.Op_ChatBean_SendType_Voice);
//			opChatBean.setVoiDur(src.substring(index));
//			return;
//		}
//		
//		int index = src.indexOf("e=\"") + 3;
//		int endIndex = src.indexOf("\"></E>");
//		if(index != -1){							// 表情
//			opChatBean.setSendType(OpChatBean.Op_ChatBean_SendType_Emoj);
//			
//			if(endIndex > index){
//				opChatBean.setEmoId(src.substring(index, endIndex));
//			}
//		}
//		
//		// 文字
//		index = src.indexOf("t=\"") + 3;
//		endIndex = src.indexOf("\" c=\"");
//		if(endIndex > index){
//			opChatBean.setMsgLen(src.substring(index, endIndex).length());
//		}
//	}
}
