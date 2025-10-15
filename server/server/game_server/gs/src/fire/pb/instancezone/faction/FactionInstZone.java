package fire.pb.instancezone.faction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fire.msp.npc.GCountNpcSize;
import fire.pb.GsClient;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.BattleEndHandler;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.InstanceText;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.act.Act;
import fire.pb.instancezone.act.BattleAct;
import fire.pb.instancezone.act.GotoAct;
import fire.pb.instancezone.act.TaskAct;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.conf.NpcConfig;
import fire.pb.instancezone.event.Event;
import fire.pb.instancezone.event.OtherEvent;
import fire.pb.instancezone.event.SubmitItemEvent;
import fire.pb.instancezone.timer.PNextTimerProcess;
import fire.pb.instancezone.timer.TimerAddBaoxiang;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.map.MapConfig;
import fire.pb.map.Npc;
import fire.pb.map.SceneManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.npc.ChestLib;
import fire.pb.npc.ChestManager;
import fire.pb.npc.SBaoxiang;
import fire.pb.npc.SubmitUnit;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Lockeys;
import mkdb.Procedure;

/**
 *	公会副本
 */
public class FactionInstZone extends InstanceZone {
	private xbean.ClanInstances clanInst;

	public FactionInstZone(long landkey, xbean.InstanceZone xzone,
			InstanceZoneConfig zoneconfig, boolean readonly) {
		super(landkey, xzone, zoneconfig, readonly);

		if (xzone.getOwnerid() > 0) {
			if (readonly) {
				clanInst = xtable.Claninstances.select(xzone.getOwnerid());
			} else {
				clanInst = xtable.Claninstances.get(xzone.getOwnerid());
			}
		} else
			throw new IllegalArgumentException();

		if (clanInst == null) {
			clanInst = xbean.Pod.newClanInstances();
			xtable.Claninstances.insert(xzone.getOwnerid(), clanInst);

			clanInst.setClanid(xzone.getOwnerid());
			clanInst.setCurrentzonekey(landkey);

			clanInst.getInstkey().put(getZoneConfig().instZoneId, landkey);
			clanInst.getInststate().put(getZoneConfig().instZoneId, 0);
		}
	}

	public xbean.ClanInstances getClanInstData() {
		return clanInst;
	}
	
	/**
	 * 获取公会的key
	 * @return
	 */
	public Long getClanId() {
		return clanInst.getClanid();
	}
	
	/**
	 * 获取公会的名字
	 * @return
	 */
	public String getClanName() {
		xbean.ClanInfo clanInfo = xtable.Clans.select(getZoneBean().getOwnerid());
		if (clanInfo == null) {
			return "";
		}
		return clanInfo.getClanname();
	}

	public xbean.ClanSaveInfo getClanSaveInfo() {
		xbean.ClanSaveInfo clanSaveInfo = clanInst.getSaveinfo().get(getZoneConfig().instZoneId);
		if (clanSaveInfo == null) {
			if (readonly) {
				clanSaveInfo = xbean.Pod.newClanSaveInfoData();
			} else {
				clanSaveInfo = xbean.Pod.newClanSaveInfo();
				clanInst.getSaveinfo().put(getZoneConfig().instZoneId, clanSaveInfo);
			}
		}

		return clanSaveInfo;
	}

	public void updateSaveTime(final int saveid) {
		xbean.ClanSaveInfo clanSaveInfo = getClanSaveInfo();
		final long now = Calendar.getInstance().getTimeInMillis();
		if (!fire.pb.util.DateValidate.inTheSameDay(clanSaveInfo.getRefreshtime(), now)) {
			clanSaveInfo.getSavestate().clear();
		}

		clanSaveInfo.getSavestate().put(saveid, now);
		clanSaveInfo.setRefreshtime(now);
		
		if (saveid != getZoneConfig().cdsave) {
			//更新公会副本最优榜
			xbean.ClanInstBestLevel bestLv = xtable.Claninstbestlevel.get(getZoneConfig().instZoneId);
			if (bestLv == null) {
				bestLv = xbean.Pod.newClanInstBestLevel();
				xtable.Claninstbestlevel.insert(getZoneConfig().instZoneId, bestLv);
			}
			
			Long clanId = bestLv.getInstsave().get(saveid);
			if (clanId == null) {
				Long clanidd = getClanId();
				bestLv.getInstsave().put(saveid, clanidd);
				
				//发全服公告
				xbean.ClanInfo memClan = xtable.Clans.select(clanidd);
				List<String> param = new ArrayList<String>(3);
				param.add(memClan.getClanname());
				param.add(getZoneConfig().name);
				param.add(String.valueOf(saveid));
				SceneManager.sendAll(MessageMgr.getMsgNotify(166095, 0, param));
			}
		}
	}

	public long getSaveTime(final int saveid) {
		xbean.ClanSaveInfo clanSaveInfo = getClanSaveInfo();
		final long now = Calendar.getInstance().getTimeInMillis();
		if (!fire.pb.util.DateValidate.inTheSameDay(clanSaveInfo.getRefreshtime(), now)) {
			clanSaveInfo.getSavestate().clear();
		}

		Long time = clanSaveInfo.getSavestate().get(saveid);
		if (time == null)
			return -1;

		return time;
	}

	public void setCurrentStep(final int saveid) {
		if (readonly)
			return;
		clanInst.getInststep().put(getZoneConfig().instZoneId, saveid);
	}

	public int getCurrentStep() {
		if (readonly)
			return 1;

		return clanInst.getInststep().get(getZoneConfig().instZoneId);
	}

	@Override
	public boolean enter(long roleId, boolean autoSync) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if (clanInfo == null) {
			MessageMgr.sendMsgNotify(roleId, 145077, null);
			return false;
		}

		final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
		if (team != null) {
			if (team.isTeamLeader(roleId)) {
				List<String> levelApplyNames = new ArrayList<String>(5);
				for (long id : team.getAllMemberIds()) {
					xbean.ClanInfo memClan = ClanUtils.getClanInfoById(id, true);
					if (memClan == null
							|| clanInfo.getKey() != memClan.getKey()) {
						MessageMgr.sendMsgNotify(roleId, 140657, null);
						return false;
					}

					final xbean.Properties prop = xtable.Properties.select(id);
					if (prop.getLevel() < getZoneConfig().minlevel) {
						levelApplyNames.add(prop.getRolename());
					}
				}

				if (levelApplyNames.size() > 0) {
					StringBuffer strParam = new StringBuffer();
					int nIndex = 0;
					for (String name : levelApplyNames) {
						if (nIndex != (levelApplyNames.size() - 1)) {
							strParam.append(name).append(",");
						} else {
							strParam.append(name);
						}
					}

					List<String> paras = new ArrayList<String>(2);
					paras.add(strParam.toString());
					paras.add(String.valueOf(getZoneConfig().minlevel));
					STransChatMessageNotify2Client notify = MessageMgr
							.getMsgNotify(160102, 0, paras);
					gnet.link.Onlines.getInstance().send(roleId, notify);
					return false;
				}
			} else {
				final xbean.Properties prop = xtable.Properties.select(roleId);
				if (prop.getLevel() < getZoneConfig().minlevel) {
					List<String> paras = new ArrayList<String>(1);
					paras.add(String.valueOf(getZoneConfig().minlevel));
					MessageMgr.sendMsgNotify(roleId, 160179, paras);
					MessageMgr.sendMsgNotify(team.getTeamLeaderId(), 160178,
							paras);
					return false;
				}
			}
		} else {
			final xbean.Properties prop = xtable.Properties.select(roleId);
			if (prop.getLevel() < getZoneConfig().minlevel) {
				List<String> paras = new ArrayList<String>(1);
				paras.add(String.valueOf(getZoneConfig().minlevel));
				MessageMgr.sendMsgNotify(roleId, 160100, paras);
				return false;
			}
		}
		return super.enter(roleId, autoSync);
	}

	@Override
	public boolean beforeEventProcess(Event event) {
		return super.beforeEventProcess(event);
	}

	@Override
	public boolean afterEventProcess(Event event) {
		return super.afterEventProcess(event);
	}

	@Override
	public boolean processOtherEvent(OtherEvent event) {
		return super.processOtherEvent(event);
	}

	public boolean processSubmitItem(SubmitItemEvent event) {
		Pack bag = new Pack(event.getRoleId(), false);
		Object npcid = event.getParams().get(ConfigParamName.NPC_ID);
		Long npcKey = (Long) event.getParams().get(ConfigParamName.NPC_KEY);
		if (npcKey == null)
			return false;
		if (npcid == null)
			return false;

		Npc npc = SceneNpcManager.selectNpcByKey(npcKey);
		if (npc == null) {
			MessageMgr.psendMsgNotify(event.getRoleId(), 160176, null);
			return false;
		}

		for (SubmitUnit unit : event.getThings()) {
			ItemBase item = bag.getItem(unit.key);
			if (item == null)
				return false;

			int removed = bag.removeItemWithKey(unit.key, 1,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifubenshangjiao,
					(Integer) npcid, "submit item in claninstZone");
			if (removed != 1)
				return false;
		}

		Module.getInstance().removeNpcSubmit(getLandkey(), npcKey);

		return true;
	}

	@Override
	protected void onSubSaveCompleted(int saveId, int subsaveId) {
	}

	@Override
	protected void onSaveCompleted(int saveId) {
		super.onSaveCompleted(saveId);
		updateSaveTime(saveId);
		
		//发通关奖励
		Procedure.pexecuteWhileCommit(new PGiveTongGuanAward(getLandkey(), saveId, getClanId()));

		int curActiveSave = getCurActiveSave();
		if (0 >= curActiveSave || curActiveSave == saveId)
			return;

		setCurrentStep(curActiveSave);

		InstanceSaveConfig cursaveconf = getZoneConfig().saves.get(curActiveSave);
		if (null == cursaveconf)
			return;
		int mapId = cursaveconf.mapId;
		int posx = cursaveconf.gposx;
		int posy = cursaveconf.gposy;

		if (mapId > 0 && posx > 0 && posy > 0) {
			MapConfig mapcfg = getZoneConfig().maps.get(cursaveconf.mapId);
			if (null == mapcfg)
				return;

			xbean.ClanInfo clanInfo = xtable.Clans.select(getZoneBean().getOwnerid());
			if (clanInfo != null) {
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers()
						.keySet(), 160351, 0, Arrays.asList(cursaveconf.name));
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers()
						.keySet(), 160376, 0, Arrays.asList(cursaveconf.name));
			}

			PNextTimerProcess timerProcess = new PNextTimerProcess(mapId, posx, posy, getLandkey());
			mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					timerProcess.submit();
				}
			}, 5, TimeUnit.SECONDS);
			logger.debug("go to next floor:" + curActiveSave);
		}
		
		long starttime = getStartTime();
		
		float bosshp = 1.0f;
		if (curActiveSave == 11)
		{
			bosshp = 0.0f;
		}
		logger.info(new StringBuilder().append("更新排行榜奖励:").append(getClanId()).append(" 进度:").append(saveId).append(" landkey:").append(getLandkey())
				.append(" 公会名字:").append(getClanName()));
		fire.pb.ranklist.proc.PFactionProgressUpdateProc clanprogressupdateproc = new fire.pb.ranklist.proc.PFactionProgressUpdateProc(
				getZoneBean().getOwnerid(), saveId, getZoneConfig().instZoneId,
				starttime, bosshp, getZoneConfig().name);

		mkdb.Procedure.pexecuteWhileCommit(clanprogressupdateproc);
	}

	@Override
	protected void onZoneCompleted() {
		super.onZoneCompleted();
		
		mkdb.Procedure saveProcedrue = new Procedure() {
			@Override
			protected boolean process() throws Exception {
				final long ownerid = getZoneBean().getOwnerid();
				xbean.ClanInstances xclaninst = xtable.Claninstances
						.get(ownerid);
				if (xclaninst != null) {
					xclaninst.setCurrentzonekey(0L);
					xclaninst.getInststate().put(getZoneConfig().instZoneId, 1);
				}

				InstanceZoneConfig afterzoneconfig = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs().get(getZoneConfig().instZoneId + 1);
				if (afterzoneconfig == null) {
					xbean.ClanInfo claninfo = xtable.Clans.select(ownerid);
					if (claninfo != null) {
						MessageMgr.psendMsgNotifyWhileCommit(claninfo.getMembers().keySet(), 160386, 0, Arrays.asList(getZoneConfig().name));
						MessageMgr.psendMsgNotifyWhileCommit(claninfo.getMembers().keySet(), 160385, 0, Arrays.asList(getZoneConfig().name));
					}
					return true;
				}

				if (afterzoneconfig.beforeZoneId == getZoneConfig().instZoneId) {
					xbean.ClanInfo clanInfo = xtable.Clans.select(ownerid);
					if (clanInfo != null)
						MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160482, 0, Arrays.asList(afterzoneconfig.name));
				} else {
					xbean.ClanInfo clan = xtable.Clans.select(ownerid);
					if (clan != null) {
						MessageMgr.psendMsgNotifyWhileCommit(clan.getMembers().keySet(), 160386, 0, Arrays.asList(getZoneConfig().name));
						MessageMgr.psendMsgNotifyWhileCommit(clan.getMembers().keySet(), 160385, 0, Arrays.asList(getZoneConfig().name));
					}
				}
				return true;
			}
		};

		if (mkdb.Transaction.current() != null) {
			saveProcedrue.call();
		} else {
			saveProcedrue.submit();
		}
	}

	@Override
	protected void playCG(Collection<Long> roleIds, int cgId) {
		super.playCG(roleIds, cgId);
	}

	@Override
	protected void onLeave(long roleId) {
		super.onLeave(roleId);
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if (clanInfo == null)
			return;
		Module.logger.info(new StringBuilder().append("角色离开副本:").append(roleId)
				.append(",公会名字:").append(clanInfo.getClanname())
				.append(",公会id:").append(clanInfo.getIndex()));
	}

	@Override
	protected void onEmpty(long lastroleId, boolean offline) {
	}

	@Override
	public boolean processEvent(Event event) {
		if (getZoneBean().getZonestate() != xbean.InstanceZone.STATE_ACTIVE)
			return false;
		int curActiveSave = getCurActiveSave();
		event.getParams().put(ConfigParamName.SAVE_ID, curActiveSave);
		if (!beforeEventProcess(event))
			return false;
		if (!event.process(this))
			return false;
		if (!afterEventProcess(event))
			return false;

		return true;
	}

	@Override
	protected void onCreated() {
		super.onCreated();
		setCurrentStep(getCurActiveSave());
		if (ClanUtils.getClanInstById(getZoneBean().getOwnerid(), getZoneConfig().instZoneId) <= 0L	|| ClanUtils.getClanInstById(getZoneBean().getOwnerid(),
						getZoneConfig().instZoneId) != getLandkey()) {
			ClanUtils.setClanInstById(getZoneBean().getOwnerid(), getLandkey(),	getZoneConfig().instZoneId);
		}
		
		clanInst.getBossinfo().clear();
	}

	public void giveNpcAward(final long roleid, final long npckey,
			final int saveId) {
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (npc == null)
			return;

		final int npcid = npc.getNpcID();
		NpcConfig npcCfg = getZoneConfig().npcs.get(npcid);

		if (npcCfg.awardid > 0) {
			Map<String, Object> paras = new HashMap<String, Object>(10);
			paras.put(RewardMgr.SAVE_ID, saveId);
			paras.put(RewardMgr.TEAM_NUM, 1);
			paras.put(RewardMgr.FUBEN_ID, getZoneConfig().instZoneId);
			
			fire.pb.activity.award.RewardMgr.getInstance()
					.distributeAllAward(roleid, npcCfg.awardid, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben, npcCfg.awardid,
							PAddExpProc.INST_ZONE, getZoneConfig().name);
		}
	}

	public void giveNpcAward(final List<Long> roleids, final long npckey,
			final int saveId) {
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (npc == null)
			return;

		final int npcid = npc.getNpcID();
		NpcConfig npcCfg = getZoneConfig().npcs.get(npcid);

		if (npcCfg.awardid > 0) {
			Map<String, Object> paras = new HashMap<String, Object>(10);
			paras.put(RewardMgr.SAVE_ID, saveId);
			paras.put(RewardMgr.TEAM_NUM, roleids.size());
			paras.put(RewardMgr.FUBEN_ID, getZoneConfig().instZoneId);
			for (Long roleid : roleids) {
				fire.pb.activity.award.RewardMgr.getInstance()
						.distributeAllAward(roleid, npcCfg.awardid, paras,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben,
								npcCfg.awardid, PAddExpProc.INST_ZONE,
								getZoneConfig().name);
			}
		}
	}

	public void checkFreshBaoXiang(final long npckey, final int saveId) {
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (npc == null)
			return;

		InstanceSaveConfig savecfg = getZoneConfig().saves.get(saveId);
		if (savecfg == null)
			return;

		int npcid = npc.getNpcID();
		NpcConfig npcCfg = getZoneConfig().npcs.get(npcid);

		if (npcCfg.baoxiangid != 0) {
			int baoxiangnum = getZoneBean().getRoleids().size() * npcCfg.baoxiangnumber / 100;
			if (baoxiangnum > 0) {
				mkdb.Executor.getInstance().schedule(new TimerAddBaoxiang(npcCfg.baoxiangid,
						getZoneBean().getRoleids().size(),
						savecfg.mapId, getLandkey(),
						getClanName(),
						getZoneConfig().instZoneId,
						npcCfg.baoxiangnumber), 10, TimeUnit.SECONDS);
				
				MessageMgr.psendMsgNotifyWhileCommit(getZoneBean().getRoleids(), 166130, 0, null);
			}
			
			Module.logger.info(new StringBuilder().append("公会副本结束:").append(getClanName()).append(",npc战斗结束刷新宝箱:").append(getZoneConfig().instZoneId)
					.append(",数量").append(baoxiangnum).append(",宝箱id:").append(npcCfg.baoxiangid));
		}
	}
	
	public boolean setSaveCompleted(final int saveId) {
		InstanceSaveConfig savecfg = getZoneConfig().saves.get(saveId);
		if (savecfg == null) {
			logger.info(new StringBuilder().append("setSaveCompleted savecfg == null").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName()));
			return false;
		}

		xbean.InstanceSave xsave = getZoneBean().getSaves().get(saveId);
		if (xsave == null) {
			logger.info(new StringBuilder().append("setSaveCompleted xsave == null").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName())
					.append(" 进入下一层失败"));
			return false;
		}
		if (xsave.getState() == InstanceText.COMPLETE) {
			logger.info(new StringBuilder().append("setSaveCompleted xsave.getState() == InstanceText.COMPLETE").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName())
					.append(" 进入下一层失败"));
			return false;
		}
		
		if (xsave.getState() == InstanceText.INACTIVE) {
			logger.info(new StringBuilder().append("setSaveCompleted xsave.getState() == InstanceText.INACTIVE").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName())
					.append(" 进入下一层失败"));
			return false;
		}

		if (xsave.getState() == InstanceText.ACTIVE) {
			xsave.setState(InstanceText.COMPLETE);
			return true;
		}

		return false;
	}

	public boolean updateSubSaveByNpcKey(final List<Long> roleIds,
			final long npckey, final int saveId, final int subsaveId,
			final int addcount) {
		giveNpcAward(roleIds, npckey, saveId);
		checkFreshBaoXiang(npckey, saveId);
		SceneNpcManager.premoveNpcWhileCommit(npckey);
		Module.getInstance().removeDynamicNpc(getLandkey(), npckey);
		if (triggerSaveCompleted(saveId)) {
			onSaveCompleted(saveId);
		}
		else {
			//这个地方加一下二次确认,确认场景里确实没有怪了
			InstanceSaveConfig savecfg = getZoneConfig().saves.get(saveId);
			if (savecfg == null)
				return false;
			final long landkey = getLandkey();
			final int mapid = savecfg.mapId;
			
			//发到场景线程
			GsClient.pSendWhileCommit(new GCountNpcSize(landkey, mapid, saveId));
		}
		return true;
	}

	public boolean updateSubSaveByNpcKey(final long roleId, final long npckey,
			final int saveId, final int subsaveId, final int addcount) {
		giveNpcAward(roleId, npckey, saveId);
		checkFreshBaoXiang(npckey, saveId);
		SceneNpcManager.premoveNpcWhileCommit(npckey);
		Module.getInstance().removeDynamicNpc(getLandkey(), npckey);
		if (triggerSaveCompleted(saveId)) {
			onSaveCompleted(saveId);
		}
		else {
			//这个地方加一下二次确认,确认场景里确实没有怪了
			InstanceSaveConfig savecfg = getZoneConfig().saves.get(saveId);
			if (savecfg == null)
				return false;
			final long landkey = getLandkey();
			final int mapid = savecfg.mapId;
			
			//发到场景线程
			GsClient.pSendWhileCommit(new GCountNpcSize(landkey, mapid, saveId));
		}
		return true;
	}

	@Override
	public boolean updateSubSave(final List<Long> roleIds, final int saveId,
			final int subsaveId, final int addcount) {
		if (triggerSaveCompleted(saveId))
			onSaveCompleted(saveId);
		return true;
	}

	@Override
	public boolean updateSubSave(final long roleId, final int saveId,
			final int subsaveId, final int addcount) {
		if (triggerSaveCompleted(saveId))
			onSaveCompleted(saveId);
		return true;
	}

	@Override
	public boolean triggerSaveCompleted(int saveId) {
		InstanceSaveConfig savecfg = getZoneConfig().saves.get(saveId);
		if (savecfg == null) {
			logger.info(new StringBuilder().append("savecfg == null").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName()));
			return false;
		}

		xbean.InstanceSave xsave = getZoneBean().getSaves().get(saveId);
		if (xsave == null) {
			logger.info(new StringBuilder().append("xsave == null").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName())
					.append(" 进入下一层失败"));
			return false;
		}
		if (xsave.getState() == InstanceText.COMPLETE) {
			logger.info(new StringBuilder().append("xsave.getState() == InstanceText.COMPLETE").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName())
					.append(" 进入下一层失败"));
			return false;
		}
		if (xsave.getState() == InstanceText.INACTIVE) {
			logger.info(new StringBuilder().append("xsave.getState() == InstanceText.INACTIVE").append(getClanId())
					.append(" 进度:").append(getCurrentStep())
					.append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName())
					.append(" 进入下一层失败"));
			return false;
		}

		if (!Module.getInstance().checkDynamicNpc(getLandkey())) {
			return false;
		}

		if (xsave.getState() == InstanceText.ACTIVE) {
			if (savecfg.chestLibId != 0) {
				ChestLib chestlib = ChestManager.getInstance().getChestLib(
						savecfg.chestLibId);
				if (chestlib == null)
					return true;
				List<SBaoxiang> chestcfgs = chestlib.getRandomChests(getZoneBean().getRoleids().size() * savecfg.chestNum, false);
				if (chestcfgs.isEmpty())
					return true;

				for (SBaoxiang chestcfg : chestcfgs) {
					final long npcKey = fire.pb.map.SceneNpcManager
							.getInstance().getNextId();
					SceneNpcManager.createNpcByMap(npcKey, chestcfg.id, "",
							savecfg.mapId, getLandkey(), 3, 0);
					Module.getInstance().setDynamicNpc(getLandkey(), npcKey, 0, 0);
				}
				logger.info(new StringBuilder().append("有宝箱了").append(getClanId())
						.append(" 进度:").append(getCurrentStep())
						.append(" landkey:").append(getLandkey())
						.append(" 公会名字:").append(getClanName())
						.append(" 进入下一层"));
				return true;
			} else {
				xsave.setState(InstanceText.COMPLETE);
				logger.debug("save complete:" + saveId);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean destroy(boolean force, boolean shutdown) {
		super.destroy(true, shutdown);
		
		logger.info(new StringBuilder().append("公会副本完成:").append(getClanId())
				.append(" 进度:").append(getCurrentStep())
				.append(" landkey:").append(getLandkey())
				.append(" 公会名字:").append(getClanName()));
		
		xbean.ClanInstances clanInst = getClanInstData();
		for (xbean.ClanBossInfo bossinfo : clanInst.getBossinfo().values()) {
			float hpcent = (float) bossinfo.getHp()	/ (float) bossinfo.getMaxhp();
			
			logger.info(new StringBuilder().append("公会副本boss进度:").append(getClanId()).append(" 进度:").append(1 - hpcent).
					append(" landkey:").append(getLandkey())
					.append(" 公会名字:").append(getClanName()));
		}
		
		return true;
	}

	@Override
	protected void onDestroy(boolean shutdown) {
		if (shutdown) {
			Module.getInstance().setInstInit(getLandkey(), false);
		} else
			clanInst.getInststep().put(getZoneConfig().instZoneId, 0);

		super.onDestroy(shutdown);
		Module.getInstance().clearDynamicNpc(getLandkey());
		
		if (getZoneBean().getZonestate() == xbean.InstanceZone.STATE_CLOSING) {
			Procedure.pexecuteWhileCommit(new PSetFactionInst(getClanId(), 
					getZoneConfig().instZoneId, 
					getZoneConfig().name));
		}
	}
	
	@Override
	public BattleEndHandler getBattleEndHandler(final long npckey) {
		return super.getBattleEndHandler(npckey);
	}

	@Override
	public Act.Result act(Act act) {
		if (act instanceof BattleAct) {

			return Act.Result.NULL;
		} else if (act instanceof TaskAct) {
			return Act.Result.FAIL;
		} else if (act instanceof GotoAct) {
			return Act.Result.FAIL;
		}
		return super.act(act);
	}

	public boolean openChest(final long roleId, final long npckey) {
		Npc npc = SceneNpcManager.getNpcByKey(npckey);
		if (npc == null) {
			MessageMgr.psendMsgNotify(roleId, 166111, null);
			return false;
		}
		SBaoxiang chestcfg = ChestManager.getInstance().getChestConfig(npc.getNpcID());
		if (chestcfg == null)
			return false;

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("MonsterLv", 1);
		para.put(RewardMgr.TEAM_NUM, 1);
		
		List<Long> allRoleIDs = new ArrayList<Long>();
		Team team = TeamManager.getTeamByRoleId(roleId);
		if (team == null) {
			allRoleIDs.add(roleId);
		}
		else {
			allRoleIDs.addAll(team.getNormalMemberIds());
		}
		
		int opentimes = chestcfg.getOpentimes();
		final long currentTime = System.currentTimeMillis();
		
		if (null != team && team.isTeamLeader(roleId) && chestcfg.getOpenteam() == 1) {
			// 全队正常状态成员 加锁
			Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
			{
				xbean.GongHuiBaoXiangInfo baoxiangInfo = xtable.Rolekaigonghuibaoxiang.select(roleId);
				if (baoxiangInfo != null) {
					if (DateValidate.inTheSameDay(baoxiangInfo.getLastopentime(), currentTime)) {
							if (baoxiangInfo.getOpentimes() >= opentimes) {
								MessageMgr.psendMsgNotifyWhileRollback(roleId, 166004, null);
								return false;
							}
					}	
				}
			}
			
			for (Long rid : allRoleIDs) {
				xbean.GongHuiBaoXiangInfo baoxiangInfo = xtable.Rolekaigonghuibaoxiang.get(rid);
				if (baoxiangInfo == null) {
					baoxiangInfo = xbean.Pod.newGongHuiBaoXiangInfo();
					xtable.Rolekaigonghuibaoxiang.insert(rid, baoxiangInfo);
				}
				if (!DateValidate.inTheSameDay(baoxiangInfo.getLastopentime(), currentTime)) {
					baoxiangInfo.setOpentimes(0);
				}
				
				if (baoxiangInfo.getOpentimes() >= opentimes) {
					MessageMgr.psendMsgNotify(rid, 166003, null);
					continue;
				}
				
				// 给予奖励
				RewardMgr.getInstance().distributeAllAward(rid,
						chestcfg.rewardid, para, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben,
						chestcfg.rewardid, PAddExpProc.INST_ZONE, getZoneConfig().name);
				
				baoxiangInfo.setOpentimes(baoxiangInfo.getOpentimes() + 1);
				baoxiangInfo.setLastopentime(currentTime);
			}
		}
		else {
			xbean.GongHuiBaoXiangInfo baoxiangInfo = xtable.Rolekaigonghuibaoxiang.get(roleId);
			if (baoxiangInfo == null) {
				baoxiangInfo = xbean.Pod.newGongHuiBaoXiangInfo();
				xtable.Rolekaigonghuibaoxiang.insert(roleId, baoxiangInfo);
			}
			
			if (!DateValidate.inTheSameDay(baoxiangInfo.getLastopentime(), currentTime)) {
				baoxiangInfo.setOpentimes(0);
			}
			
			if (baoxiangInfo.getOpentimes() >= opentimes) {
				MessageMgr.psendMsgNotifyWhileRollback(roleId, chestcfg.getOpenovermsg(), null);
				return false;
			}
			
			// 给予奖励
			RewardMgr.getInstance().distributeAllAward(roleId,
					chestcfg.rewardid, para, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben,
					chestcfg.rewardid, PAddExpProc.INST_ZONE, getZoneConfig().name);
			
			baoxiangInfo.setOpentimes(baoxiangInfo.getOpentimes() + 1);
			baoxiangInfo.setLastopentime(currentTime);
		}

		SceneNpcManager.removeNpc(npckey);
//		Module.getInstance().removeDynamicNpc(getLandkey(), npckey);

//		int curActiveSave = getCurActiveSave();
//		if (triggerSaveCompleted(curActiveSave))
//			onSaveCompleted(curActiveSave);
		return true;
	}
	
	public boolean checkBattleid(final int battleid) {
		if (clanInst.getBossinfo().containsKey(battleid)) {
			return true;
		}
		
		return false;
	}
}
