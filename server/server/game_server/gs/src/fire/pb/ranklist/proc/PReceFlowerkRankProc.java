package fire.pb.ranklist.proc;

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
 * 收花排行榜  by changhao
 */
public class PReceFlowerkRankProc extends Procedure {
	private long roleId;
	private int itemId;
	private int itemNum;
	public PReceFlowerkRankProc(long roleId, int itemId, int itemNum){
		this.roleId = roleId;
		this.itemId = itemId;
		this.itemNum = itemNum;
	}
	
	public boolean process(){
		if (this.roleId < 1)
			return false;
		
		long t = RankListManager.GetMonday1Millisecond();		
		xbean.ReceFlowerRankList list = xtable.Receflowerlist.get(t);
		if (null == list)
		{
			list = xbean.Pod.newReceFlowerRankList();
			xtable.Receflowerlist.insert(t, list);			
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
		
		Integer num = weekgift.getRecegift().get(itemId);
		if (num != null)
		{
			total = num + itemNum;
		}
		else
		{
			total = itemNum;
		}
		
		weekgift.getRecegift().put(itemId, total);
		
		long giftnum = 0;
		for (Integer n : weekgift.getRecegift().values())
		{
			giftnum += n;
		}
		
		long cur = System.currentTimeMillis();
		
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null)
		{
			return true;
		}
		
		xbean.ReceFlowerRecord record = xbean.Pod.newReceFlowerRecord();
		record.setTriggertime(cur);
		record.getMarshaldata().setRoleid(roleId);
		record.getMarshaldata().setName(prop.getRolename());
		record.getMarshaldata().setSchool(prop.getSchool());
		record.getMarshaldata().setNum(giftnum);
		
		RankListManager.getInstance().tryInsertRecord(RankType.FLOWER_RECEIVE, list.getRecords(), record);	
		
		return true;
	}
}
