package fire.pb.clan;

import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.clan.srv.ClanManage;


public class PClearWeekContributionProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("CLAN");
	private final long clanId;
	
	public PClearWeekContributionProc(long clanId) {
		super();
		this.clanId = clanId;
	}
	
	@Override
	protected boolean process() throws Exception {
		xbean.ClanInfo clanInfo = xtable.Clans.get(clanId);
		if(clanInfo == null) return false;
		int goldlevel=clanInfo.getHouse().get(ClanManage.GoldBank);//公会等级
		fire.pb.clan.SClanGoldBank bank =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanGoldBank.class).get(goldlevel);//金库
		int bankbonus=0;
		int bankbonusbase=0;
		if(bank!=null){
			bankbonus=bank.getAllbonus();
			bankbonusbase=bank.getBonus();
		}
		//处理公会dkp
		clanInfo.setLastclandkp(clanInfo.getClandkp());
		clanInfo.setClandkp(0);
		//成员处理
		for(Map.Entry<Long, xbean.ClanMemberInfo> entry : clanInfo.getMembers().entrySet()){
			long roleId = entry.getKey();
			xbean.ClanMemberInfo clanMemberInfo=entry.getValue();
			//设置公会分红
			int oldclanpoint=clanMemberInfo.getWeekclanpoint();
			int value=getRoleBonus(bankbonus,bankbonusbase,clanMemberInfo.getWeekclanpoint());
			//设置上周公会贡献度
			clanMemberInfo.setPreweekclanpoint(clanMemberInfo.getWeekclanpoint());
			clanMemberInfo.setOwnbonus(value);
			clanMemberInfo.setWeekclanpoint(0);
			clanMemberInfo.setClanhelpnum(0);
			//推送红点消息
			SBonusQuery sBonusQuery=new SBonusQuery();
			sBonusQuery.bonus=value;
			Procedure.psendWhileCommit(roleId, sBonusQuery);
			logger.info("角色id"+roleId+"获得公会工资"+value+"上周公会贡献dkp"+oldclanpoint);
		}
		return true;
	}
	
	
	public int getRoleBonus(int bankbonus,int bankbonusbase, int clanweekpoint){
		//公会工资公式修改为:1000+金库系数*上周累计公会dkp/1000
		int value=(int)(bankbonusbase+clanweekpoint/1000.0*bankbonus);
		if(value<0){
			value=0;
		}
		return value;
	}
	
	

}
