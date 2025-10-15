package fire.pb.clan;

import java.util.ArrayList;
import java.util.List;

import fire.pb.clan.srv.ClanManage;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/***
 * 
 * 通过公会ID加公会资金 by changhao
 */
public class PAddClanMoneyByClanid extends Procedure {
	private final long clanid;
	private final int money;
	
	public PAddClanMoneyByClanid(final long clanid, final int money)
	{
		this.clanid = clanid;
		this.money = money;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		xbean.ClanInfo clanInfo = xtable.Clans.get(clanid);
		if (clanInfo == null)
		{
			return false;
		}
		
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, clanInfo.getMembers().keySet())); //锁定角色 by changhao		
		
		for (Long r : clanInfo.getMembers().keySet())
		{
			xbean.Properties prop = xtable.Properties.get(r);
			if (prop != null)
			{
				xbean.ClanMemberInfo info = clanInfo.getMembers().get(r);
				if (info != null)
				{
					int cur = info.getOwnbonus();
					info.setOwnbonus(cur + money * prop.getLevel());
				}
			}
		}
		
		return true;
	}
}
