package fire.pb.ranklist.proc;

import java.util.Comparator;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.attr.SRefreshRoleScore;
import fire.pb.fushi.redpack.RedPackType;
import fire.pb.main.ConfigManager;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.ranklist.RankType;
import fire.pb.skill.SkillRole;
import mkdb.Procedure;
import xtable.Clans;

/**
 * 红包排行榜 by changhao
 */
public class PWorldRedPackRankProc extends Procedure {
	private long roleId;
	private long num;
	public PWorldRedPackRankProc(long roleId, long num)
	{
		this.roleId = roleId;
		this.num = num;
		
	}
	
	public boolean process(){
		if (this.roleId < 1)
			return false;
		
		if(!StateCommon.isOnline(roleId))
		{
			return true;
		}
		
		if (this.num  <= 0)
		{
			return true;
		}
		
		int type = RankType.RED_PACK_1;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			type = RankType.RED_PACK_2;
		}
			
		xbean.RedPackRankList list = xtable.Redpacklist.get(type);
		if (null == list)
		{
			list = xbean.Pod.newRedPackRankList();
			xtable.Redpacklist.insert(type, list);			
		}
		
		long cur = System.currentTimeMillis();
		
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null)
		{
			return true;
		}
		
		xbean.RedPack c = xtable.Roleid2worldredpacksendtotal.get(roleId);
		if (c == null)
		{
			c = xbean.Pod.newRedPack();
			xtable.Roleid2worldredpacksendtotal.insert(roleId, c);
		}
		
		long count = this.num;
		if (c != null)
		{
			count += c.getWorldredpack();
			c.setWorldredpack(count);
		}
		
		xbean.RedPackRecord record = xbean.Pod.newRedPackRecord();
		record.setTriggertime(cur);
		record.getMarshaldata().setRoleid(roleId);
		record.getMarshaldata().setName(prop.getRolename());
		record.getMarshaldata().setSchool(prop.getSchool());
		record.getMarshaldata().setNum(count);
		
		//不管什么服数据库只是一个 by changhao
		RankListManager.getInstance().tryInsertRecord(type, list.getRecords(), record);	
		
		return true;
	}
}
