package fire.pb.team.teammelon;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import fire.pb.team.teammelon.PEndMelonTask;
import fire.pb.util.Misc;

/***
 * 告诉客户端开始ROLL点 by changhao
 *
 */
public class PTeamRollMelon extends mkdb.Procedure
{
	private final java.util.List<fire.pb.activity.award.RolledAwardItem> itemids;
	
	private final List<Long> fighterroleids; //参加战斗的IDS by changhao
	
	private final List<Long> watcherids;//观看者ids by changhao
	
	private static AtomicLong melonid = new AtomicLong(1);
	/**
	 * 分赃类型:0表示默认,1表示循环明雷怪任务,2表示精英副本
	 */
	private final int melontype;
	/**
	 * 结合分赃类型:表示子类型,精英副本里表示副本id
	 */
	private final int dataid;
	
	/**
	 * 明雷怪用的角色id,如果是在队伍内,则是队长id,如果是精英副本,则表示副本的唯一id
	 */
	private final long dataid2;
	
	/**
	 * 战场ID by changhao
	 */
	private final long battleid;
		
	public PTeamRollMelon(java.util.List<fire.pb.activity.award.RolledAwardItem> items, List<Long> roleids, long battleid, List<Long> watcherids)
	{
		this.itemids = items;
		this.fighterroleids = roleids;
		this.melontype = 0;
		this.dataid = 0;
		this.battleid = battleid;
		this.dataid2 = 0;
		this.watcherids = watcherids;
	}
	
	public PTeamRollMelon(
			final java.util.List<fire.pb.activity.award.RolledAwardItem> items,
			final List<Long> roleids, final long dataid2, final int melonType, final int questid, long battleid, List<Long> watcherids) {
		this.itemids = items;
		this.fighterroleids = roleids;
		this.melontype = melonType;
		this.dataid = questid;
		this.battleid = battleid;
		this.dataid2 = dataid2;
		this.watcherids = watcherids;
	}
	
	@Override  /*加锁顺序 team->roleid by changhao */
	protected boolean process() throws Exception
	{			
		xbean.ETeamMelon eteammelon = xtable.Battlemelonid2melon.get(this.battleid); //锁住team by changhao
		if (eteammelon == null)
		{
			eteammelon = xbean.Pod.newETeamMelon();
			xtable.Battlemelonid2melon.insert(this.battleid, eteammelon);
		}
		else
		{
			return true;
		}
		
		eteammelon.setMelontype(melontype);
		eteammelon.setDataid(dataid);
		if (dataid2 != 0)
			eteammelon.setDataid2(dataid2);
			
		final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
		
		java.util.Set<Long> sets = new java.util.HashSet<Long>();
		sets.addAll(fighterroleids);
		for (Long roleid : sets)
		{
			eteammelon.getMelonerlist().add(roleid);
			roleids.add(roleid);
		}
		
		//锁住 roleids by changhao
		this.lock(xtable.Locks.ROLELOCK, roleids);
		
		fire.pb.team.teammelon.STeamRollMelon msg = new fire.pb.team.teammelon.STeamRollMelon();
		
		for (fire.pb.activity.award.RolledAwardItem award : itemids) //遍历所有需要分赃的道具 by changhao
		{
			fire.pb.item.ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(award.itemid);
			if ( attr == null )
			{
				fire.pb.team.TeamManager.logger.debug("FAIL:PTeamRollMelon:道具ID不存在 "+award.itemid);
				continue;
			}
			
			xbean.TeamMelon teammelon = xbean.Pod.newTeamMelon();
			
			//award.itemid = 35001;
			teammelon.setItemid(award.itemid);
			teammelon.setItemnum(award.itemnum);
			teammelon.setMaxrollpoint(0);
			teammelon.setOpnum(0);
			teammelon.setAwardid(award.awardid);
			
			int max = 0;
			for (Long roleid : roleids) //提前ROLL好  by changhao
			{
				int r = Misc.getRandomBetween(1, 100);
				
				while (r == max) //只能有一个最大的 by changhao
				{
					r = Misc.getRandomBetween(1, 100);
				}
				
				if (r > max)
				{
					max = r;
				}
				
				teammelon.getMelonroleids().put(roleid, r);
			}
			
			fire.pb.item.ItemBase item = fire.pb.item.Module.getInstance().getItemManager().genItemBase(teammelon.getItemid(), teammelon.getItemnum());
			if (item == null)
			{
				return false;
			}
			
			CopyItemData(teammelon.getItemdata(), item.getDataItem());
			
			eteammelon.getMelonid2melons().put(PTeamRollMelon.melonid.get(), teammelon);
					
			fire.pb.team.teammelon.RollMelon rollmelon = new fire.pb.team.teammelon.RollMelon();
			rollmelon.itemid = award.itemid;
			rollmelon.itemnum = award.itemnum;
			rollmelon.melonid = PTeamRollMelon.melonid.get();
			com.locojoy.base.Octets itemdata = item.getTips();
			if (itemdata != null)
				rollmelon.itemdata = item.getTips();
			msg.melonlist.add(rollmelon);
			
			PTeamRollMelon.melonid.addAndGet(1);
		}
		
		for (Long roleid : roleids) //发给所有队员 by changhao
		{
			xtable.Roleid2battlemelonid.insert(roleid, this.battleid);			
			mkdb.Procedure.psendWhileCommit(roleid, msg);				
		}
		
		//可以看但不能roll的人员 by changhao
		if (this.watcherids != null)
		{
			fire.pb.team.teammelon.STeamRollMelon msg1 = new fire.pb.team.teammelon.STeamRollMelon();
			msg1.watcher = 1;
			msg1.melonlist.addAll(msg.melonlist);
			for (Long roleid : this.watcherids) //发给所有观看人员 by changhao
			{	
				mkdb.Procedure.psendWhileCommit(roleid, msg1);				
			}			
			
			eteammelon.getWatchmelonerlist().addAll(this.watcherids);
		}
		
		//10秒到计时 by changhao
		mkdb.Executor.getInstance().schedule(new PEndMelonTask(battleid), fire.pb.team.TeamManager.MAX_ROLL_MELON_TIME, TimeUnit.MILLISECONDS);

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
}
