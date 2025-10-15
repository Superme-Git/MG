package fire.pb.school.shouxi;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.msp.npc.GChangeShouxiShape;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.Monster;
import fire.pb.map.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.school.School;
import fire.pb.school.SchoolConst;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.title.Title;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

import org.apache.log4j.Logger;

public class PRefreshAllShouxis extends Procedure {
   Logger logger=Logger.getLogger(PRefreshAllShouxis.class);
	@Override
	protected boolean process() throws Exception
	{
		for (School school : School.values())
		{
			//从选举表中找出票数最高的人,如果没有candidate或者票数都为0，则首席弟子不变 by changhao
			xbean.ProfessionLeaderCand candidates = xtable.Professionleadercandidates.get(school.getValue());
			if(candidates == null)
			{
				candidates=xbean.Pod.newProfessionLeaderCand();
				xtable.Professionleadercandidates.insert(school.getValue(), candidates);
			}
			
			List<Long> cans = candidates.getCandidatelist();
			long professionleaderroleid = 0;
			int tickets = -1;
			for (long candID : cans)
			{
				xbean.ProfessionLeaderTicketInfo candInfo = xtable.Roleid2professionleadertickets.get(candID);
				if(candInfo.getTickets() > tickets)
				{
					professionleaderroleid = candInfo.getRoleid();
					tickets = candInfo.getTickets();
				}
			}
			
			xbean.Monster monsterBean = null;
			//如果找到了一个合格的首席弟子
			if(professionleaderroleid > 0)
			{
				try
				{
					monsterBean = xtable.Professionleaderfightdata.get(professionleaderroleid).copy();
					monsterBean.setMonsterid(School.getBattleNpc(school));
					xbean.ProfessionLeaderInfo sxi = xtable.Professionleader.get(school.getValue());
					if(sxi == null)
					{
						sxi = xbean.Pod.newProfessionLeaderInfo();
						xtable.Professionleader.insert(school.getValue(), sxi);
					}
					
					int titleID = School.getShouxiTitleid(school);
					//去掉上任首席弟子的称谓 by changhao
					if(sxi.getRoleid() > 0)
					{
						long roleid = sxi.getRoleid();
						Title title = new Title(roleid, true);
						if (title.roleHaveTitle(titleID))
						{
							Title.removeTitle(roleid,titleID);
						}
						
						if (StateCommon.isOnline(roleid))
						{
							MessageMgr.psendSystemMessageToRole(roleid, SchoolConst.NOT_BE_SHOUXI, null);
						}
					}
					
					sxi.setRefreshtimes(0);
					sxi.setRoleid(professionleaderroleid);
					
					monsterBean.setFightnpctype(5);
					if(sxi.getMonsterbean().size() == 0)
					{
						sxi.getMonsterbean().add(monsterBean);
					}
					else
					{
						sxi.getMonsterbean().set(0, monsterBean);
					}
					
					//给职业领袖称谓 by changhao
					Title title = new Title(professionleaderroleid, false);
					if (!title.roleHaveTitle(titleID))
					{
						Title.addTitle(professionleaderroleid, titleID, "", -1);
					}
					
					title.onTitle(titleID);
					if (StateCommon.isOnline(professionleaderroleid))
					{
						MessageMgr.psendSystemMessageToRole(professionleaderroleid, SchoolConst.BECOME_SHOUXI, null);
					}
					
					//加成就
//					IAchievementSystem achievementSystem = AchievementManager.getInstance().getAchievementSystem(shouxiID, false);
//					AchievementManager.giveAchievement(achievementSystem, 410501);
					//刷新职业领袖的外形 by changhao
					changeShouxiShape(professionleaderroleid,school.getValue(),sxi.getShapecomponent());
					PropRole role = new PropRole(professionleaderroleid,true);
					List<String> paras = MessageUtil.getMsgParaList(role.getName());
					STransChatMessageNotify2Client ssm=MessageMgr.getMsgNotify(SchoolConst.BE_SHOUXI_SUCC, 0, paras);
				    SceneManager.psendAllBySchoolWhileCommit(ssm, school.getValue());
				    int k = 6;
				}
				catch (Exception e)
				{
					logger.debug("刷新首席失败,启用默认首席.门派id"+school.getValue(), e);
					Monster monster=new Monster(SchoolConst.DEFAULT_SHOUXI_ID);
					monsterBean=monster.getMonsterbean(null);
				}
			}
			else
			{//没找到说明可以连任了
				xbean.ProfessionLeaderInfo sxi = xtable.Professionleader.get(school.getValue());
				if(sxi != null)
				{//如果sxi==null,说明首席弟子不存在,就不用做任何事情了
					if(sxi.getRoleid()>0)
					{
						long roleid = sxi.getRoleid();
						if (StateCommon.isOnline(roleid))
						{
							MessageMgr.psendSystemMessageToRole(roleid, SchoolConst.BECOME_SHOUXI, null);
						}
						
						PropRole role = new PropRole(roleid,true);
						List<String> paras = MessageUtil.getMsgParaList(role.getName());
						STransChatMessageNotify2Client ssm=MessageMgr.getMsgNotify(SchoolConst.BE_SHOUXI_SUCC, 0, paras);
					    SceneManager.psendAllBySchoolWhileCommit(ssm, school.getValue());
					}
				}
			}
			//把参加竞选的人的信息清空
			candidates.getCandidatelist().clear();
		}
		
		STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(170034, 0, null);
		SceneManager.sendAll(ssmg);
		
		return true;
	}
	
	private void changeShouxiShape(long shouxiID,int school,Map<Integer, Integer> component) {
		
		xbean.Properties prop = xtable.Properties.select(shouxiID);
		int shape = prop.getShape();
		//long npckey = ProfessionLeaderManager.getShouxiKey(school);
		
		School s = School.getSchoolBySchoolid(school);
		
		
		java.util.List<NPC> npcs = SceneNpcManager.getInstance().getNpcByID(s.getNpcid());
		if (npcs.get(0) == null)
		{
			return;
		}
		
		long key = npcs.get(0).getNpcKey();
		
		if (key > 0)
		{
			GChangeShouxiShape changeShouxiShape = new GChangeShouxiShape();
			changeShouxiShape.npckey = key;
			changeShouxiShape.shape = shape;
			changeShouxiShape.roleid = shouxiID;

			fire.pb.map.Role.getPlayerComponents(shouxiID, changeShouxiShape.components);
			
			for (Entry<Byte, Integer> entry : changeShouxiShape.components.entrySet()) {
				component.put(entry.getKey().intValue(), entry.getValue());
			}
			changeShouxiShape.name = prop.getRolename();
			GsClient.pSendWhileCommit(changeShouxiShape);
		}
	}
 
}
