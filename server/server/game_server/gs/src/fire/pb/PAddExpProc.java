package fire.pb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fire.log.beans.OpLvlBean;
import fire.pb.event.LevelupEvent;
import fire.pb.fushi.DayPayManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.ranklist.proc.RankListManager;
import fire.pb.talk.SExpMessageTips;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PAddExpProc extends mkdb.Procedure {
	public final static int LEVEL_UP = 4;
	public final static int UPDATE_AP = 5;// 技能升级
	public final static int GUAJI_AWARD = 10;// 挂机场景
	public final static int INST_MAP = 23;
	public final static int INST_ZONE = 24;
	public final static int CLAN_FIGHT = 25;
	public final static int GM = 0;

	public final static int SCENARIO_QUEST = 4002;
	public final static int OTHER_QUEST = 4002; // 其他任务
	public final static int BATTLE_AWARD = 4001;// 其他战斗奖励
	public final static int OTHER = 4003; // 其他
	public final static int USE_MONEY_ITEM = 4004;
	
	public final static int ZHIYE = 1001; 
	public final static int RICHANG = 1002;
	public final static int JINGYING = 1003;
	public final static int BINGFENG = 1004;
	public final static int GUSHENZHUAYA = 1005;
	public final static int GONGHUI_RENWU = 1006;
	public final static int GONGHUI_FUBEN = 1007;
	public final static int JULONGJUNTUAN = 1008;
	public final static int GUSHEN = 1009;
	public final static int GUAJI = 1010;
	public final static int ZHIHUISHILIAN = 1011;
	public final static int ANYEMAXITUAN = 1012;
	public final static int KAOGU = 1013;
	public final static int YINGXIONGSILIAN = 1014;

	protected final long roleid;

	private final long addexp;
	
	private final long yingfuexp;

	private boolean showMsg = true;

	private int reason;

	private final boolean isTeamLeader;
	
	/**
	 * 正常经验修正
	 */
	private long reviseExp = 0;
	/**
	 * 累积经验衰减数量
	 */
	private long pileExp = 0;

	public PAddExpProc(long roleid, long addexp, int reason, String hint) {

		this.roleid = roleid;
		this.addexp = addexp;
		this.reason = reason;
		this.isTeamLeader = false;
		this.yingfuexp = 0;
	}

	public PAddExpProc(final long roleid, final long addexp,
			final boolean showMsg, final int reason, final String hint,
			final boolean isTeamLeader, final long reviseExp, final long pileExp) {

		this.roleid = roleid;
		this.addexp = addexp;
		this.reason = reason;
		this.showMsg = showMsg;
		this.isTeamLeader = isTeamLeader;
		this.yingfuexp = 0;
		this.reviseExp = reviseExp;
		this.pileExp = pileExp;
	}

	public PAddExpProc(long roleid, long addexp, boolean showMsg, int reason,
			String hint) {
		this(roleid, addexp, reason, hint);
		this.showMsg = showMsg;
	}
	
	public PAddExpProc(long roleid, long addexp, boolean showMsg, int reason,
			String hint, final long reviseExp, final long pileExp) {

		this(roleid, addexp, reason, hint);
		this.showMsg = showMsg;
		this.reviseExp = reviseExp;
		this.pileExp = pileExp;
	}
	
	public PAddExpProc(final long roleid, final long addexp,
			final boolean showMsg, final int reason, final String hint,
			final boolean isTeamLeader, final long yingfu, final long reviseExp, final long pileExp) {

		this.roleid = roleid;
		this.addexp = addexp;
		this.reason = reason;
		this.showMsg = showMsg;
		this.isTeamLeader = isTeamLeader;
		this.yingfuexp = yingfu;
		this.reviseExp = reviseExp;
		this.pileExp = pileExp;
	}


	@Override
	public boolean process() {
		if (addexp == 0)
			return false;
		
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
		
		long addexpall = addexp;
		final xbean.Properties prop = xtable.Properties.get(roleid);
		if (null == prop)
			return false;
		final long allexp = prop.getAllexp();
		final long curexp = prop.getExp();
		int BfLvl=prop.getLevel();
		long BfExp=prop.getExp();
		boolean islevelup = false;//是否升级标志
		long res = (Long.MAX_VALUE - curexp <= addexpall) ? Long.MAX_VALUE
				: curexp + addexpall;// 防止溢出
		if (prop.getExp() < 0)
			prop.setExp(0);
		
		final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
		if (addexpall < 0) {
			if (res <= 0)
				prop.setExp(0);
			else
				prop.setExp(res);
			sendExp2Role(addexpall, yingfuexp);
		} else {
			if (res >= CalcRoleAttr.ROLE_MAX_CUREXP
					&& addexpall + allexp >= CalcRoleAttr.ROLE_MAX_ALLEXP)
				return false;
			// 存储的经验超过升级所需的20倍时,不再获得经验
			fire.pb.effect.Role roleImpl = new fire.pb.effect.RoleImpl(roleid, true);
			long nextExp = roleImpl.getNextExp();
			if (nextExp != 0 && curexp > nextExp * 31 && prop.getLevel() >= 10) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142508, null);
				return false;
			}
			long resumeexp = addexpall;
			if (res <= CalcRoleAttr.ROLE_MAX_CUREXP
					&& allexp + resumeexp <= CalcRoleAttr.ROLE_MAX_ALLEXP) {
				prop.setExp(res);
				prop.setAllexp(allexp + resumeexp);
				sendExp2Role(addexpall, yingfuexp);
			} else if (res > CalcRoleAttr.ROLE_MAX_CUREXP
					&& resumeexp + allexp <= CalcRoleAttr.ROLE_MAX_ALLEXP) {
				prop.setExp(CalcRoleAttr.ROLE_MAX_CUREXP);
				prop.setAllexp(allexp + CalcRoleAttr.ROLE_MAX_CUREXP - curexp);
				sendExp2Role(CalcRoleAttr.ROLE_MAX_CUREXP - curexp, yingfuexp);
			} else if (allexp + resumeexp > CalcRoleAttr.ROLE_MAX_ALLEXP
					&& res <= CalcRoleAttr.ROLE_MAX_CUREXP) {
				prop.setAllexp(CalcRoleAttr.ROLE_MAX_ALLEXP);
				prop.setExp(res);
				sendExp2Role(res - curexp, yingfuexp);
			} else
				return false;

			// 自动升级并且自动加点儿
			List<LevelupEvent> eventList = new ArrayList<LevelupEvent>();

			fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(roleid,	false);
			
			while (prop.getExp() >= erole.getNextExp()) {
				if (prop.getLevel() >= PLevelUpProc.getMaxLevel()) {
					break;
				}
				
				//停等级 69和89
				if (prop.getLevel() == 69 || prop.getLevel() == 89) {
					Integer tupotips = prop.getTupotips().get(prop.getLevel());
					if (tupotips == null) {
						//提示角色领取突破任务
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 180015, null);
						fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.LevelBreakEvent(roleid, prop.getLevel()));
						prop.getTupotips().put(prop.getLevel(), 1);
					}
					
					break;
				}
				
				if (!new PLevelUpProc(prop.getUserid(), roleid).call()) {
					fire.log.LogManager.logger.error("自动升级出现问题!");
					return false;
				}
				
				eventList.add(new fire.pb.event.LevelupEvent(roleid, prop.getLevel()));
				islevelup=true;
			}

			if (eventList.size() > 0) {
				for (LevelupEvent event : eventList) {
					//fire.pb.event.Poster.getPoster().dispatchEvent(event);
					mkdb.Procedure.pexecuteWhileCommit(
							new mkdb.Procedure() {
								@Override
								protected boolean process() {
									TeamManager.getTeamByRoleId(roleid);//强行锁队伍
									fire.pb.event.Poster.getPoster().dispatchEvent(event);
									return true;
								}
						} );
				}
			}
		}

		if (islevelup) {
			int AfLvl=prop.getLevel();
			long AfExp=prop.getExp();
			OpLvlBean opLvlBean = new OpLvlBean(OpLvlBean.Op_LvlBean_LvlType_Game,
					BfLvl, AfLvl, BfExp, AfExp, reason);
			fire.log.YYLogger.OpLvlLog(roleid, opLvlBean);
			
			
			//人物属性排行榜 by changhao
			final int level = AfLvl;
			if(level >= 30)
			{
				mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure(){
					@Override
					public boolean process(){
						xbean.RoleLevelListRecord record = xbean.Pod.newRoleLevelListRecord();
						record.setTime(System.currentTimeMillis());
						
						xbean.MarshalRoleLevelRecord marshRecord = record.getMarshaldata();
						marshRecord.setLevel(level);
						marshRecord.setRoleid(roleid);
						PropRole pRole = new PropRole(roleid, true);
						marshRecord.setRolename(pRole.getName());
						marshRecord.setSchool(pRole.getSchool());
						
						xbean.RoleLevelRankList list = xtable.Rolelevellist.get(1);
						if(null == list){
							list = xbean.Pod.newRoleLevelRankList();
							xtable.Rolelevellist.insert(1, list);
						}
						
						RankListManager.getInstance().tryInsertRecord(RankType.LEVEL_RANK, list.getRecords(), record);
						
						return true;
					}
				});
			}
			
			mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleid));
			
			gnet.link.Role linkrole = gnet.link.Onlines.getInstance().find(roleid);
			if (linkrole != null)
			{
				int userid = linkrole.getUserid();
				if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
				{
					DayPayManager.getInstance().ProcessDayPay(userid, roleid);
					DayPayManager.getInstance().Prompt(roleid); //提醒 by changhao
					
					boolean subscribe = DayPayManager.getInstance().haveSubscribeAndNoExpire(roleid);
					if (subscribe == true) //如果有订阅并且到达收费级别弹提示 by changhao
					{
						DayPayManager.getInstance().CostDayPayPrompt(roleid);					
					}
				}					
			}
		}

		// 通知客户端刷新经验
		mkdb.Procedure.psendWhileCommit(roleid,
				new SRefreshUserExp(prop.getExp()));
		if (send.datas.size() > 0)
			Procedure.psendWhileCommit(roleid, send);
		
		if (actrole != null) {
			if (yingfuexp > 0) {
				actrole.decYingFuExp(yingfuexp);
			}
		}

		return true;
	}


	private void sendExp2Role(long exp, long yingfuExpVal) {
		if (!showMsg) return;
		
		//专用的经验提示协议
		if (exp > 0) {
			HashMap<Integer, Long> msgtips = new HashMap<Integer, Long>();
			if (isTeamLeader) msgtips.put(180002, 0L);
			if (yingfuExpVal > 0) msgtips.put(180003, yingfuExpVal);
			if (reviseExp > 0) msgtips.put(180004, reviseExp);
			if (reviseExp < 0) msgtips.put(180005, -reviseExp);
			if (pileExp > 0) msgtips.put(180006, pileExp);
			
			SExpMessageTips expMsg = new SExpMessageTips(180001, exp, msgtips);
			mkdb.Procedure.psendWhileCommit(roleid, expMsg);
			
			/*List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(Math.abs(exp)));
			if (isTeamLeader) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160063, paras);
			} else {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 190024, paras);
			}*/
		}
		
		/*if (showMsg) {
			List<String> paras = null;
			if (yingfuExpVal > 0) {
				paras = MessageUtil.getMsgParaList(Long.toString(Math.abs(exp)), Long.toString(Math.abs(yingfuExpVal)));
			}
			else
				paras = MessageUtil.getMsgParaList(Long.toString(Math.abs(exp)));
			
			if (exp > 0) {
				if (isTeamLeader) {
					if (yingfuExpVal > 0) {
						MessageMgr.psendMsgNotifyWhileCommit(roleid, 160347, paras);
					}
					else
						MessageMgr.psendMsgNotifyWhileCommit(roleid, 160063, paras);
				} else {
					if (yingfuExpVal > 0) {
						MessageMgr.psendMsgNotifyWhileCommit(roleid, 160344, paras);
					}
					else
						MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_EXP, paras);
				}
			} else {
				MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.LOSE_EXP,	paras);
			}
		}*/
	}

}
