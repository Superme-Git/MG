package fire.pb.item;

import java.util.Calendar;

import fire.pb.WorldTime;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.mission.Module;
import mkdb.Procedure;

public abstract class DPUseItem extends Procedure {
	protected final long roleId;
	protected final int keyinpack;
	protected final int usenum;
	private final boolean singleplayer;
	private ItemBase bi;
	public DPUseItem(final long roleId, final int keyinpack, final int usenum) {
		this(roleId, keyinpack, usenum, true);
	}
	
	public DPUseItem(final long roleId, final int keyinpack, final int usenum, final boolean singleplayer) {
		this.roleId = roleId;
		this.keyinpack = keyinpack;
		this.usenum = usenum;
		this.singleplayer = singleplayer;
	}
	
	protected UseItemHandler getUseItemHandler() {
		if (bi == null)
			return null;
		return bi.getUseItemHandler();
	}
	
	@Override
	protected final boolean process() {
		/*if (!singleplayer) {
			fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByRoleId(roleId);
			if (team != null && !team.getAbsentMemberIds().contains(roleId)){
				this.lock(xtable.Locks.ROLELOCK, team.getNormalMemberIds());
			}
		}*/
		
		fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByRoleId(roleId);
		if (team != null && !team.getAbsentMemberIds().contains(roleId)){
			this.lock(xtable.Locks.ROLELOCK, team.getNormalMemberIds());
		}
		
		Pack pack = new Pack(roleId, false);
		bi = pack.getItem(keyinpack);
		if (bi == null) {
			return false;
		}
		
		Module.logger.info(new StringBuffer().append("角色[").append(roleId).append("]准备使用道具[").append(bi.getItemId()).append("]*").append(usenum));
		
		PCheckUseNum checkUseNum = new PCheckUseNum(roleId, bi.getItemId());
		if (!checkUseNum.call())
			return false;
		
		java.util.Map<Integer, SBattleUseItem> battleUseItem = fire.pb.main.ConfigManager.getInstance().getConf(SBattleUseItem.class);
		if (!battleUseItem.containsKey(bi.getItemId())) {
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 142383, 0, null);
				return false;
			}
		}
		
		final Integer roleLevel = xtable.Properties.selectLevel(roleId);
		if (roleLevel == null || roleLevel < bi.itemAttr.needlevel) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144815, 0, null);
			return false;
		}
		
		BuffAgent roleImpl = new BuffRoleImpl(roleId, true);
        if (!battleUseItem.containsKey(bi.getItemId())) {
        	if (roleImpl.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER)){
        		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 142383, 0, null);
            	return false;
            }
        }
        
		Module.logger.info(new StringBuffer().append("角色[").append(roleId).append("]使用道具[").append(bi.getItemId()).append("]handle:").append(getUseItemHandler().getClass().toString()));
		Commontext.UseResult ur = getUseItemHandler().onUse(roleId, bi, usenum);
		Module.logger.info(new StringBuffer().append("角色[").append(roleId).append("]使用道具[").append(bi.getItemId()).append("]UseResult:").append(ur.toString()));
		
		if (ur == Commontext.UseResult.SUCC) {
			if (bi.getItemAttr().useup == 1) {
				if (pack.removeItemWithKey(keyinpack, usenum,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 0, "使用消耗") == usenum)
				{
					/*
					if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
					{
						boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleId);
						if (ok == false)
						{
							Module.logger.info("DPUseItem:角色[" + roleId + "]不能使用任务道具[" + bi.getItemId() + "]-nodaypay:");
							return false;
						}
					}
					*/	
					
					Commontext.UseResult ur2 = fire.pb.mission.Module.getInstance().onUseMissionItem(roleId, bi.getItemId());
					if (ur2 == Commontext.UseResult.FAIL) {
					}
					return true;
				} else {
					return false;
				}
			} else if (bi.getItemAttr().useup == 2) {
				return true;
			} else {
				return true;
			}

		} else if (ur == Commontext.UseResult.AWAIT) {
			// 锁定物品
			if (!lockItem(roleId, bi.getKey(), usenum)) {
				return false;
			}
			return true;
		} else if (ur == Commontext.UseResult.SUCC_NODEL)
		{
			/*
			if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
			{
				boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleId);
				if (ok == false)
				{
					Module.logger.info("DPUseItem:角色[" + roleId + "]不能使用任务道具[" + bi.getItemId() + "]-nodaypay:");
					return false;
				}
			}
			*/			
			
			// I feel sick! nausea code! by dc
			Commontext.UseResult ur2 = fire.pb.mission.Module.getInstance()
					.onUseMissionItem(roleId, bi.getItemId());
			if (ur2 == Commontext.UseResult.FAIL)
			{
				// TODO:return false;
			}
			return true;
		}
		return false;
	}
	
	private static boolean lockItem(final long roleId, final int keyinpack, int num) {
		xbean.BagTimeLock btl = xtable.Bagtimelock.get(roleId);
		if (btl == null) {
			btl = xbean.Pod.newBagTimeLock();
			xtable.Bagtimelock.insert(roleId, btl);
		}
		WorldTime now = WorldTime.getInstance();
		now.add(Calendar.SECOND, 10);
		if (btl.getTimeout() > now.getTimeInMillis()) {
			return false;
		}
		btl.setItemkey(keyinpack);
		btl.setNum(num);
		btl.setTimeout(now.getTimeInMillis());
		return true;
	}
}
