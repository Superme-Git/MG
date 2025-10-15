package fire.pb.clan;

import org.apache.log4j.Logger;

/**
 * 更新处理帮派成员每周逻辑
 * @author
 *
 */
public class PUpdateWeekClansProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("CLAN");
	public static final long KICK_TIME=72*60*60*1000;//踢出时间
	private long clanId;
	public PUpdateWeekClansProc(long factionId){
		this.clanId = factionId;
	}
	
	public boolean process(){
		xbean.ClanWeekNum factionweeknum=xtable.Clanweeknumtab.get(clanId);
		if(factionweeknum==null){
			factionweeknum=xbean.Pod.newClanWeekNum();
			xtable.Clanweeknumtab.insert(clanId, factionweeknum);
		}
		if(factionweeknum.getWeeknum()==-1){
			//这里的初始值，如果这个方法是一天执行一次的，就需要在启动服务器的时候设置一下，要不如果是刚好的周一启动服务器，就不会处理更新逻辑了
			factionweeknum.setWeeknum(fire.pb.util.DateValidate.getWeekNumber());
		}
		//如果不是同一周，就需要处理周更新
		if(factionweeknum.getWeeknum()!=fire.pb.util.DateValidate.getWeekNumber()){
			//这里处理所有和帮派相关的周处理逻辑
			pexecute(new fire.pb.clan.PClearWeekContributionProc(clanId));//处理公会帮贡
			
		}
		
		factionweeknum.setWeeknum(fire.pb.util.DateValidate.getWeekNumber());//设置当前周
		
		return true;
	}


}