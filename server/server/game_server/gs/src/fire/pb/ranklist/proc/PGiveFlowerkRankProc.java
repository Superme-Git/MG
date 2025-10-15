package fire.pb.ranklist.proc;

import java.util.Calendar;
import java.util.Comparator;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.attr.SRefreshRoleScore;
import fire.pb.main.ConfigManager;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.ranklist.RankType;
import fire.pb.skill.SkillRole;
import mkdb.Procedure;
import xtable.Clans;

/**
 * 送花排行榜  by changhao
 */
public class PGiveFlowerkRankProc extends Procedure {
	private long roleId;
	private int itemId;
	private int itemNum;
	public PGiveFlowerkRankProc(long roleId, int itemId, int itemNum)
	{
		this.roleId = roleId;
		this.itemId = itemId;
		this.itemNum = itemNum;
	}
	
	public boolean process(){
		if (this.roleId < 1)
			return false;
				
		long t = RankListManager.GetMonday1Millisecond();
		xbean.GiveFlowerRankList list = xtable.Giveflowerlist.get(t);
		if (null == list)
		{
			list = xbean.Pod.newGiveFlowerRankList();
			xtable.Giveflowerlist.insert(t, list);			
		}
		
		xbean.WeekGiveReceGift gift = xtable.Roleid2weekgiverecegift.get(roleId);
		if (gift == null)
		{
			gift = xbean.Pod.newWeekGiveReceGift();
			xtable.Roleid2weekgiverecegift.insert(roleId, gift);
		}
		
		int total = 0;
		
		xbean.GiveReceGift weekgift = gift.getWeekdata().get(t);
		if (weekgift == null)
		{
			weekgift = xbean.Pod.newGiveReceGift();
			gift.getWeekdata().put(t, weekgift);
		}
		
		Integer num = weekgift.getGivegift().get(itemId);
		if (num != null)
		{
			total = num + itemNum;
		}
		else
		{
			total = itemNum;
		}
		
		weekgift.getGivegift().put(itemId, total);
		
		long giftnum = 0;
		for (Integer n : weekgift.getGivegift().values())
		{
			giftnum += n;
		}
		
		long cur = System.currentTimeMillis();
		
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null)
		{
			return true;
		}
		
		xbean.GiveFlowerRecord record = xbean.Pod.newGiveFlowerRecord();
		record.setTriggertime(cur);
		record.getMarshaldata().setRoleid(roleId);
		record.getMarshaldata().setName(prop.getRolename());
		record.getMarshaldata().setSchool(prop.getSchool());
		record.getMarshaldata().setNum(giftnum);
		
		RankListManager.getInstance().tryInsertRecord(RankType.FLOWER_GIVE, list.getRecords(), record);	
				
		return true;
	}
}
