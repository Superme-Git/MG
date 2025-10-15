package fire.pb.school.shouxi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.PropRole;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.battle.Monster;
import fire.pb.battle.PNewBattle;
import fire.pb.huoban.HuoBan;
import fire.pb.huoban.HuoBanColumn;
import fire.pb.huoban.HuoBanInfo;
import fire.pb.huoban.Module;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SHeroBaseInfo;
import fire.pb.npc.SHeroWeekFreeInfo;
import fire.pb.school.School;
import fire.pb.school.SchoolConst;
import fire.pb.util.Misc;
import mkdb.Procedure;

/***
 * 挑战职业领袖 by changhao
 */
public class PChallengeShouxi extends Procedure {
   private final long roleid;
   private final long npckey;
   private final int npcid;
	public PChallengeShouxi(long roleid,long npckey,int npcid) {
		this.roleid=roleid;
		this.npcid = npcid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {

		PropRole prop=new PropRole(roleid,true);
		int school=prop.getSchool();
		xbean.ProfessionLeaderInfo professionleader = xtable.Professionleader.select(school);
		List<Monster> monsters=new ArrayList<Monster>();
		 
		//如果职业领袖是玩家 by changhao
		if(professionleader != null)
		{
			Monster monster=new Monster(professionleader.getMonsterbean().get(0).toBean());
			PropRole role = new PropRole(professionleader.getRoleid(), true);
			
			School s = School.getSchoolBySchoolid(school);
			int battleid = School.getBattleIdBySchool(s);
		    Monster m = BattleField.getMonsterFromBattleConfig(battleid, BattleField.GUEST_FIRST_ID, role.getName());
		    if (m != null)
		    {
		    	m.setShape(monster.getShape());
		    	m.setMonsterId(monster.getMonsterId());
		    	monsters.add(m);
		    }
						
			final int roleLevel = xtable.Properties.selectLevel(professionleader.getRoleid());
			
			//上几个伙伴 by changhao
			HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(professionleader.getRoleid(), true);
			if (huobancol != null)
			{
				java.util.List<Integer> list = Misc.getRandomList(huobancol.getAllHuoBans().keySet(), 4);
				
				for (Integer key : list)
				{
					xbean.HuoBanshuxing huobanshuxing = fire.pb.huoban.Module.getHuobanshuxing(roleLevel, key);
					if (huobanshuxing != null)
					{
						HuoBan huoban = huobancol.getHuoBan(key);
						if (huoban != null)
						{
							Map<Integer,Float> huobanattrs = huoban.getL2Attrs(roleid);
							if (huobanattrs != null)
							{
								xbean.Monster xmonster = Monster.createHuobanMonster(huoban.getId(), huobanshuxing.getLevel(), null, 1.0, huobanattrs, huobanshuxing, huoban.getHuoBanInfo().getColor());
								if (xmonster != null)
								{
									Integer aiid = huobanshuxing.getBattleai().get(3);
									if (aiid == null) {
										aiid = huobanshuxing.getBattleai().get(0);
									}									
									if (aiid > 0) {
										xbean.BattleAI ai = xbean.Pod.newBattleAI();
										ai.setId(aiid);
										xmonster.getAis().add(ai);
									}
									
									Monster monster1 = new Monster(xmonster);
									monsters.add(monster1);			
								}								
							}					
						}							
					}			
				}				
			}
		}
		
		PNewBattle pNewBattle = null;
		if (professionleader != null)
		{
			pNewBattle=new PNewBattle(roleid, monsters,new ProfessionLeaderEndHandler(npckey,npcid));
		}
		else
		{
			School s = School.getSchoolBySchoolid(school);
			
			int battleid = School.getBattleIdBySchool(s);			
			pNewBattle = new PNewBattle(roleid, battleid, BattleField.GUEST_FIRST_ID, "", new ProfessionLeaderEndHandler(npckey,npcid));
		}

		pNewBattle.setBattleType(BattleType.BATTLE_SHOUXI);
		pNewBattle.call();
				
		return true;
	}
    private int getbattlePetID(){
    	return Misc.getRandomBetween(SchoolConst.BATTLE_PET_NPC_1, SchoolConst.BATTLE_PET_NPC_4);
    }
}
