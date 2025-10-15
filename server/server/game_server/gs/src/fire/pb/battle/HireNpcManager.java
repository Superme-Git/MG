package fire.pb.battle;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.huoban.HuoBan;
import fire.pb.huoban.HuoBanColumn;
import fire.pb.npc.SHireNpc;

public class HireNpcManager
{
	
	public static Set<Integer> getExistHiredNpcBuffIds(BuffAgent buffrole)
	{
		Set<Integer> buffids = new HashSet<Integer>();
		for(int buffId : buffrole.getAllBuffBeans().keySet())
		{
			SHireNpc shirenpc = Module.getInstance().getSHireNpcByBuffId(buffId);
			if(shirenpc == null)
				continue;
			buffids.add(buffId);
		}
		return buffids;
	}
	
	public static int processBuffMonsters(xbean.BattleInfo battle,List<Long> roleids,int xiezhannum, boolean bhost)
	{
		if(xiezhannum <= 0)
			return 0;
		int i = 0;
		for(long roleId : roleids)
		{
			
			int fighterId = battle.getRoleids().get(roleId);
			Fighter fighter = battle.getFighterobjects().get(fighterId);
			if(fighter == null)
				continue;
			if(fighterId != BattleField.HOST_FIRST_ID && fighterId != BattleField.GUEST_FIRST_ID)
				continue;
			boolean ishost = fighter.getFighterBean().getIshost();
			if(bhost != ishost)
				continue;			
			HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(roleId, false);
			
			for(Integer key : huobancol.getFightHuobanKeys())
			{
				HuoBan huoban = huobancol.getHuoBan(key);
				if(huoban == null){
					continue;
				}
				if(i >= xiezhannum)
					break;
				i++;
				
				final int rolelv = xtable.Properties.selectLevel(roleId);
				xbean.HuoBanshuxing huobanshuxing = fire.pb.huoban.Module.getHuobanshuxing(rolelv, key);
				if (huobanshuxing == null)
					continue;
				
				Map<Integer,Float> huobanattrs = huoban.getL2Attrs(roleId);
				xbean.Monster xmonster = Monster.createHuobanMonster(huoban.getId(), huobanshuxing.getLevel(), battle, 1.0, huobanattrs, huobanshuxing, huoban.getHuoBanInfo().getColor());
				Monster monster = new Monster(xmonster);
				int index = ishost ? BattleField.HOST_FIRST_ID : BattleField.GUEST_FIRST_ID;
				int form = ishost ? battle.getHostform() : battle.getGuestform();
				int formLevel = ishost ? battle.getHostformlevel() : battle.getGuestformlevel();
				
				ConstantlyBuff tempBuff = null;
				PNewBattle.enterBattleMonster(battle, monster, index, ishost, form, formLevel, tempBuff);
			}
		}
		return i;
	}
}
