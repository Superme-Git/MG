package fire.pb.team.teammelon;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardMgr.MsgInfos;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.UpdateCircleTaskState;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.map.SceneManager;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.util.MessageUtil;

/***
 * 向客户端发送最终ROLL点数据 by changhao
 *
 */
public class PTeamRollMelonInfo extends mkdb.Procedure
{
	private final long battlemelonid;
	private final int end;
	
	public PTeamRollMelonInfo(final long battlemelonid, final int end)
	{
		this.battlemelonid = battlemelonid;
		this.end = end;
	}
	
	@Override  /*加锁顺序 team->roleid by changhao */
	//发送分赃结果 by changhao
	protected boolean process() throws Exception
	{	
		xbean.ETeamMelon eteammelon = xtable.Battlemelonid2melon.get(battlemelonid); //锁住team by changhao
		if (eteammelon == null)
		{
			return true;
		}
		
		final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
		//锁住 roleids by changhao
		//检查这里的顺序是否满足 by changhao
		for (Long e : eteammelon.getMelonerlist())
		{
			roleids.add(e);
		}
		
		this.lock(xtable.Locks.ROLELOCK, roleids);	
		
		for (java.util.Map.Entry<Long, xbean.TeamMelon> e : eteammelon.getMelonid2melons().entrySet())
		{
			fire.pb.team.teammelon.STeamRollMelonInfo msg = new fire.pb.team.teammelon.STeamRollMelonInfo();
			
			msg.melonid = e.getKey();
			
			xbean.TeamMelon teammelon = e.getValue();
			
			if (end == 0 && teammelon.getOpnum() < teammelon.getMelonroleids().size())
			{
				continue;
			}
			
			MsgInfos msgInfos = RewardMgr.getInstance().getMsgInfos(teammelon.getAwardid());
			
			int max = 0;
			long grab = 0;
			for (java.util.Map.Entry<Long, Integer> e1 : teammelon.getMelonroleids().entrySet())
			{
				fire.pb.team.teammelon.RoleRollInfo roleinfo = new fire.pb.team.teammelon.RoleRollInfo();
				roleinfo.roleid = e1.getKey();
				roleinfo.roll = e1.getValue();
				roleinfo.rolename = xtable.Properties.selectRolename(e1.getKey());
				msg.rollinfolist.add(roleinfo);
				
				if (e1.getValue() > max)
				{
					max = e1.getValue();
					grab = e1.getKey();
				}
				
				if (end == 1) //发送还没ROLL点的玩家自动分配点数 by changhao
				{
					Integer alreadroll = teammelon.getOpmelonroleids().get(e1.getKey());
					if (alreadroll == null)
					{
						xbean.Properties roleprop = xtable.Properties.select(roleinfo.roleid);
						fire.pb.team.teammelon.SOneTeamRollMelonInfo msg1 = new fire.pb.team.teammelon.SOneTeamRollMelonInfo();
						msg1.itemid = teammelon.getItemid();
						msg1.melonid = e.getKey();
						msg1.rollinfo.roleid = roleinfo.roleid;
						msg1.rollinfo.rolename = roleprop.getRolename();
						msg1.rollinfo.roll = roleinfo.roll;
						
						for (Long roleid2 : eteammelon.getMelonerlist()) //发给所有队员最终的这个人ROLL点情况 by changhao
						{
							mkdb.Procedure.psendWhileCommit(roleid2, msg1);			
						}
						
						for (Long roleid2 : eteammelon.getWatchmelonerlist()) //发给所有队员最终的这个人ROLL点情况 by changhao
						{
							if (roleid2 != null)
								mkdb.Procedure.psendWhileCommit(roleid2, msg1);			
						}		
					}
				}
			}					
			
			if (grab != 0) //此人获得道具给他背包里加道具 by changhao
			{							
				xbean.Item rollitem1 = teammelon.getItemdata();
				xbean.Item rollitem2 = xbean.Pod.newItem();
				CopyItemData(rollitem2, rollitem1);
				
				fire.pb.item.ItemBase item = Module.getInstance().getItemManager().toItemBase(rollitem2, 0, 0, 0); 
				Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(grab, fire.pb.item.BagTypes.BAG, false);
				int bagid = bag.getPackid();
				int itemkey = item.getKey();
				if (bag.isFull())
				{
					fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(grab, BagTypes.TEMP, false);
					if (tempBag.doAddItem(item, -1, LiveSkillManager.Roll, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_battle, 2) != AddItemResult.SUCC)
					{
						//return false;
					}
					else
					{
						sendAwardMsg(grab, item.getItemId(), rollitem1.getNumber(), msgInfos);
						fire.pb.util.MessageUtil.psendAddItemWhileCommit(grab, item.getItemId(), rollitem1.getNumber());						
					}
				}
				else
				{
					if(bag.doAddItem(item, -1, LiveSkillManager.Roll, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_battle, 2) != AddItemResult.SUCC)
					{
						//return false;
					}
					else
					{
						sendAwardMsg(grab, item.getItemId(), rollitem1.getNumber(), msgInfos);
						fire.pb.util.MessageUtil.psendAddItemWhileCommit(grab, item.getItemId(), rollitem1.getNumber());						
					}
				}
				
				itemkey = item.getKey();
				
				MelonItemBagInfo melonitem = new MelonItemBagInfo();
				melonitem.bagid = bagid;
				melonitem.itemkey = itemkey;
				
				msg.melonitemlist.add(melonitem);
			}
			
			msg.grabroleid = grab;
			if (msg.grabroleid != 0)
			{
				msg.grabrolename = xtable.Properties.selectRolename(grab);			
			}
						
			for (Long roleid : roleids) //发给所有队员最终的ROLL点情况 by changhao
			{
				mkdb.Procedure.psendWhileCommit(roleid, msg);			
			}
			
			for (Long roleid : eteammelon.getWatchmelonerlist()) //发给所有观察者最终的ROLL点情况 by changhao
			{
				if (roleid != null)
					mkdb.Procedure.psendWhileCommit(roleid, msg);			
			}
			
			if (end == 0)
			{				
				eteammelon.getMelonid2melons().remove(e.getKey());
				
				if (eteammelon.getMelonid2melons().isEmpty())//所有赃物分配完毕 by changhao
				{
					for (Long roleid : roleids)
					{
						xtable.Roleid2battlemelonid.remove(roleid);
					}
					
					xtable.Battlemelonid2melon.remove(battlemelonid); //删除分赃 by changhao
					if (eteammelon.getMelontype() == 1) { //处理循环任务相关
						pexecuteWhileCommit(new UpdateCircleTaskState(eteammelon.getDataid2(), eteammelon.getDataid(), SpecialQuestState.DONE));
					}
					else if (eteammelon.getMelontype() == 2) { //精英副本
						fire.pb.mission.instance.line.LineInstManager.getInstance().doNextStep(eteammelon.getDataid(), eteammelon.getDataid2());
					}
				}
				
				return true;
			}
		}
		
		if (end == 1) //倒计时结束触发ROLL点完毕 by changhao
		{
			for (Long roleid : roleids)
			{
				xtable.Roleid2battlemelonid.remove(roleid);
			}
			
			xtable.Battlemelonid2melon.remove(battlemelonid); //删除分赃 by changhao
			if (eteammelon.getMelontype() == 1) { //处理循环任务相关
				pexecuteWhileCommit(new UpdateCircleTaskState(eteammelon.getDataid2(), eteammelon.getDataid(), SpecialQuestState.DONE));
			}
			else if (eteammelon.getMelontype() == 2) { //精英副本
				fire.pb.mission.instance.line.LineInstManager.getInstance().doNextStep(eteammelon.getDataid(), eteammelon.getDataid2());
			}
		}
		
		return true;
	}
	
	public void CopyItemData(xbean.Item out, xbean.Item in)
	{
		out.setExtid(in.getExtid());
		out.setFlags(in.getFlags());
		out.setId(in.getId());
		out.setLoseeffecttime(in.getLoseeffecttime());
		out.setNumber(in.getNumber());
		out.setPosition(in.getPosition());
		out.setTimeout(in.getTimeout());
		out.setUniqueid(in.getUniqueid());
		out.getNumbermap().putAll(in.getNumbermap());		
	}
	
	private void sendAwardMsg(long roleid, int itemID, int itemNum, MsgInfos msgInfos) {
		if (msgInfos == null)
			return;
		if (itemNum <= 0)
			return;
		ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemID);
		if (attr == null) {
			return;
		}
		
		if (msgInfos.getMsgid() > 0) {
			if (attr.rare == 1) {
				List<String> paras = new ArrayList<String>();
				PropRole pRole = new PropRole(roleid, true);
				paras.add(pRole.getName());
				paras.addAll(MessageUtil.getItemMsgParas(itemID, itemNum));
				STransChatMessageNotify2Client ssmn = MessageMgr.getMsgNotify(msgInfos.getMsgid(), 0, paras);
				SceneManager.sendAll(ssmn);
			}
		}
	}
}
