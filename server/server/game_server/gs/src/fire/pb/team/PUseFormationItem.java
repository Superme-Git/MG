package fire.pb.team;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.PropRole;
import fire.pb.battle.SFormationConfig;
import fire.pb.item.GroceryItemShuXing;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

/**
 * 使用光环书升级 by changhao
 */
public class PUseFormationItem extends mkdb.Procedure{
	
	private final long roleId;
	private final int formationId;
	private final int itemId; //使用的光环道具 by changhao
	private final int itemnum; //数量 by changhao
	
	public PUseFormationItem(long roleId, int formId, int itemId, int itemnum){
		this.roleId = roleId;
		this.formationId = formId;
		this.itemId = itemId;
		this.itemnum = itemnum;
	}

	@Override
	protected boolean process() throws Exception {
		if(formationId <= 0 || formationId > TeamManager.MAX_FORM_NUM)
		{
			psend(roleId, new STeamError(TeamError.FormIdError));
			TeamManager.logger.debug("CFormationMakeBook:光环id错误 "+roleId);
			return true;
		}
		int maxFormLevel = getMaxLevel ();
		if(maxFormLevel <0){
			
			psend(roleId, new STeamError(TeamError.FromLevelMax));
			TeamManager.logger.debug("CFormationMakeBook:光环等级已经最大了 "+roleId);
			return true;
		}
		
		GroceryItemShuXing itemconfig = (GroceryItemShuXing) fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
		if(itemconfig == null)
		{
			psend(roleId, new STeamError(TeamError.UnKnuownFormBook));
			TeamManager.logger.debug("CFormationMakeBook:不知道的光环书 "+roleId);
			return true;
		}
		
		if (itemconfig.getTypeid() != TeamManager.ITEMTYPE_FORMBOOK_HALF_ID && itemconfig.getTypeid() != TeamManager.ITEMTYPE_FORMBOOK_ID)
		{
			psend(roleId, new STeamError(TeamError.UnKnuownFormBook));
			TeamManager.logger.debug("CFormationMakeBook:不知道的光环书 "+roleId);
			return true;
		}
		
		//roleId
		
		Long teamId = xtable.Roleid2teamid.select(roleId);
		Team team = null;
		//先验证队伍是否为空 by changhao
		
		final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();		
		if(teamId != null)
		{			
			team = new Team(teamId, false);
			
			//锁队伍内所有人的锁rolelock by changhao
			roleids.addAll(team.getAllMemberIds());
			this.lock(xtable.Locks.ROLELOCK, roleids);
		}
		else
		{
			roleids.add(roleId);
		}
				
		PropRole prole = new PropRole(roleId, false);
		
		Map<Integer,SFormationConfig> formations = ConfigManager.getInstance().getConf(fire.pb.battle.SFormationConfig.class);
		if(formations == null)
		{
			psend(roleId, new STeamError(TeamError.FormIdError));
			TeamManager.logger.debug("CFormationMakeBook:光环id错误 "+roleId);
			return true;
		}
		
		SFormationConfig config = formations.get(formationId);
		
		if (config == null)
		{
			psend(roleId, new STeamError(TeamError.FormIdError));
			TeamManager.logger.debug("CFormationMakeBook:光环id错误 "+roleId);
			return true;			
		}
		
		Map<Integer, xbean.FormBean> formMap = prole.getFormtions();
		xbean.FormBean fmb  = formMap.get(formationId);
		if(fmb == null)
		{
			fmb = xbean.Pod.newFormBean();
			fmb.setActivetimes(0);
			fmb.setLevel(0);
			fmb.setExp(0);
			formMap.put(formationId, fmb);
		}
				
		int formlevel = fmb.getLevel();
		
		if (formlevel >= getMaxLevel())
		{
			psend(roleId, new STeamError(TeamError.FromLevelMax));
			TeamManager.logger.debug("CFormationMakeBook:光环等级已经最大了 "+roleId);
			return false;			
		}
		
		Pack bag = new Pack(roleId, false);
		
		int number = bag.removeItemById(itemId, itemnum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guanghuanshengji, 0, TeamManager.USE_FORMBOOK);
		if(number != itemnum){
			
			psend(roleId, new STeamError(TeamError.FormBookHalfNotEnough));
			TeamManager.logger.debug("CFormationMakeBook:光环书不足 "+roleId);		
			return false;
		}
				
		boolean newformation = false;
		boolean levelup = false;
		if (formlevel == 0)
		{
			if (config.getMatchid() == itemId)
			{
				fmb.setLevel(1);
				fmb.setExp(0);
				newformation = true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			int addexp = calcAddExp(itemconfig, config) * itemnum;
			
			int nextformLevel = formlevel + 1; 
			
			int bfexp = fmb.getExp();
			
			fire.pb.battle.FormationConfig myformconfig = fire.pb.battle.Module.getInstance().getFormationById(formationId, nextformLevel);
			
			int levelupexp = myformconfig.getExp(); //升当前级别需要的总经验 by changhao
			
			int exp = bfexp + addexp;
		
			//可能连续升级 by changhao
			while (exp >= levelupexp)
			{
				levelup = true;				
				exp -= levelupexp;
				formlevel ++;
				nextformLevel ++;
				
				if (formlevel >= maxFormLevel)
				{
					exp = 0;
					break;
				}
				
				fire.pb.battle.FormationConfig myformconfignextlevel = fire.pb.battle.Module.getInstance().getFormationById(formationId, nextformLevel);
				
				levelupexp = myformconfignextlevel.getExp();
			}
			
			fmb.setExp(exp);
			fmb.setLevel(formlevel);
			
			// 运营日志
			fire.log.beans.AddZhenfaExpBean bean = new fire.log.beans.AddZhenfaExpBean(formationId, fmb.getLevel(), addexp, bfexp, fmb.getExp());
			fire.log.YYLogger.addTeamzhenfaExpLog(roleId, bean);
		}
		
		Map<Integer, xbean.FormBean> out = new java.util.HashMap<Integer, xbean.FormBean>();
		out.put(formationId, fmb);
		
		notify(out);
		
		if (newformation)
		{
			List<String> parameters = new java.util.ArrayList<String>();
			parameters.add(config.name);
			MessageMgr.sendMsgNotify(roleId, 160006, parameters);
		}
		
		if (newformation == false && levelup == true) //如果不是新光环,并且升级了那么通知队伍内所有人 by changhao
		{
			SSetTeamFormation msg = new SSetTeamFormation();
			msg.formation = formationId;
			msg.formationlevel = formlevel;
			msg.msg = 1;
			for (Long roleid : roleids)
			{
				mkdb.Procedure.psendWhileCommit(roleid, msg);
			}
			if(team!=null && team.getTeamInfo().getFormation() == formationId)
			{
				team.changeFormationWithSP(formationId, formlevel, false);
			}
		}

		return true;
	}
	
	private void notify(Map<Integer, xbean.FormBean> formMap){
		SFormationsMap send = new SFormationsMap();
		Set<Integer> set = formMap.keySet();
		if(set.size() > 0){
			for(int formId : set){
				xbean.FormBean bean = formMap.get(formId);
				fire.pb.FormBean temp = new fire.pb.FormBean();
				temp.activetimes =bean.getActivetimes(); 
				temp.level = bean.getLevel();
				temp.exp = bean.getExp();
				send.formationmap.put(formId, temp);
			}
		}
		mkdb.Procedure.psendWhileCommit(roleId, send);
	}
	
	private int getMaxLevel (){
		Map<Integer,SGuangHuanLevelup> formations = ConfigManager.getInstance().getConf(fire.pb.team.SGuangHuanLevelup.class);
		if(formations == null){
			return -1;
		}
		return formations.size();
	}
	
	public int calcAddExp(fire.pb.item.GroceryItemShuXing itemconfig, fire.pb.battle.SFormationConfig formconfig)
	{
		//如果光环和光环书相匹配 by changhao
		if (formconfig.matchid == itemconfig.getId())
		{
			return formconfig.matchidaddexp;
		}
		else if (itemconfig.getTypeid() == TeamManager.ITEMTYPE_FORMBOOK_HALF_ID)
		{			
			fire.pb.common.SCommon itemidconfig = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(TeamManager.FORMBOOKHALF_ADD_EXP);
			int value = Integer.parseInt(itemidconfig.getValue());
			return value;				
		}
		
		fire.pb.common.SCommon itemidconfig = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(TeamManager.FORMBOOK_NO_MATCH_ADD_EXP);
		int value = Integer.parseInt(itemidconfig.getValue());
		return value;	
	}
}
