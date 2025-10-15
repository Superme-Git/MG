package fire.pb.ranklist.proc;
import fire.pb.ranklist.RankType;
import mkdb.Procedure;

/***
 * 插入排行榜role名字变化
 * @author changhao
 *
 */
public class PRankInsertRoleChangeName extends Procedure {
	
	long roleid;
	
	public PRankInsertRoleChangeName(long roleid) {
		this.roleid = roleid;
	}
	
	@Override
	public boolean process(){
		
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null)
		{
			return false;
		}

		long t = RankListManager.GetMonday1Millisecond();
		xbean.GiveFlowerRankList list = xtable.Giveflowerlist.get(t);
		if(null != list)
		{
			for (xbean.GiveFlowerRecord e : list.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == roleid)
				{
					e.getMarshaldata().setName(prop.getRolename());
					break;
				}
			}
		}
		
		xbean.ReceFlowerRankList list1 = xtable.Receflowerlist.get(t);
		if(null != list1)
		{
			for (xbean.ReceFlowerRecord e : list1.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == roleid)
				{
					e.getMarshaldata().setName(prop.getRolename());
					break;
				}
			}
		}
		
		int type = RankType.RED_PACK_1;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			type = RankType.RED_PACK_2;
		}
		
		xbean.RedPackRankList list2 = xtable.Redpacklist.get(type);
		if (null != list2)
		{
			for (xbean.RedPackRecord e : list2.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == roleid)
				{
					e.getMarshaldata().setName(prop.getRolename());
					break;
				}
			}
		}
		
		return true;
	}
}
