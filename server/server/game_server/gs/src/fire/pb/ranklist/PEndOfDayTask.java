

package fire.pb.ranklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.RemoteLogParam;
import fire.pb.PropRole;
import fire.pb.main.ConfigManager;
import mkdb.Procedure;
import xbean.PetScoreListRecord;
import xbean.PetScoreRankList;
import xbean.RoleLevelListRecord;
import xbean.RoleLevelRankList;


/**
 * @author   cn	 
 */

public class PEndOfDayTask extends Procedure {

	@Override
	protected boolean process() throws Exception {
		RoleLevelRankList roleLevelRankList = xtable.Rolelevellist.select(1);
		if (roleLevelRankList != null){
			mkdb.Trace.info("print roleLevelRankList");
			int rank = 1;
			List<RoleLevelListRecord> records = roleLevelRankList.getRecords();
			for (RoleLevelListRecord record : records) {
				Map<String, Object> paras = new HashMap<String, Object>();
				xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(new PropRole(record.getMarshaldata().getRoleid(), true).getUserid());
				paras.put(RemoteLogParam.OS, auUserInfo!=null?auUserInfo.getOs():"");
				String platformid = xtable.Properties.selectPlatformuid(record.getMarshaldata().getRoleid());
				if(platformid.length()>4){
					paras.put(RemoteLogParam.PLATFORM, platformid.substring(platformid.length()-4));
				}else{
					paras.put(RemoteLogParam.PLATFORM, auUserInfo ==null?"":auUserInfo.getNickname().substring(0, Math.min(4, auUserInfo.getNickname().length())));
				}
//				LogUtil.putRoleBasicParams(record.getMarshaldata().getRoleid(), paras );
				paras.put(RemoteLogParam.ROLENAME, record.getMarshaldata().getRolename());
				paras.put(RemoteLogParam.RANK, rank);
//				LogManager.getInstance().doLog(RemoteLogID.LEV_RANK, paras);
				rank++;
				if (rank > 100)
					break;
			}
		}
		PetScoreRankList petScoreRankList = xtable.Petscorelist.select(1);
		if (petScoreRankList != null){
			mkdb.Trace.info("print petscoreRankList");
			int rank = 1;
			List<PetScoreListRecord> records = petScoreRankList.getRecords();
			for (PetScoreListRecord record : records) {
				Map<String, Object> paras = new HashMap<String, Object>();
				xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(new PropRole(record.getMarshaldata().getRoleid(), true).getUserid());
				paras.put(RemoteLogParam.OS, auUserInfo!=null?auUserInfo.getOs():"");
				String platformid = xtable.Properties.selectPlatformuid(record.getMarshaldata().getRoleid());
				if(platformid.length()>4){
					paras.put(RemoteLogParam.PLATFORM, platformid.substring(platformid.length()-4));
				}else{
					paras.put(RemoteLogParam.PLATFORM, auUserInfo ==null?"":auUserInfo.getNickname().substring(0, Math.min(4, auUserInfo.getNickname().length())));
				}
//				LogUtil.putRoleBasicParams(record.getMarshaldata().getRoleid(), paras );
				paras.put(RemoteLogParam.ROLENAME, record.getMarshaldata().getNickname());
				paras.put(RemoteLogParam.PETNAME, record.getMarshaldata().getPetname());
				paras.put(RemoteLogParam.RANK, rank);
				paras.put(RemoteLogParam.PET_SCORE, record.getMarshaldata().getPetgrade());	
//				LogManager.getInstance().doLog(RemoteLogID.PET_RANK, paras);
				rank++;
				if (rank > 20)
					break;
			}
		}
		
		xbean.ClanRankList clanranklist = xtable.Clanranklist.select(1);
		if(clanranklist!=null && clanranklist.getRecords()!=null){
			mkdb.Trace.info("print FactionRankList");
			int rank = 1;
			for(xbean.ClanRankRecord record : clanranklist.getRecords()){
				Map<String, Object> paras = new HashMap<String, Object>();
				xbean.ClanInfo clanInfo = xtable.Clans.select(record.getClankey());
				if(clanInfo == null) continue;
				paras.put(RemoteLogParam.FROM, ConfigManager.getGsZoneId());  
				paras.put(RemoteLogParam.FACTIONID, record.getClankey());
				paras.put(RemoteLogParam.FACTIONNAME, record.getClanname());
				paras.put(RemoteLogParam.LEADERID, clanInfo.getClanmaster());
				paras.put(RemoteLogParam.FACLEVEL, record.getLevel());
				paras.put(RemoteLogParam.FACMEMBERCOUNT, record.getMembernum());
				paras.put(RemoteLogParam.RANK, rank);

//				LogManager.getInstance().doLog(RemoteLogID.FACTION_RANK, paras);
				
				rank++;
				
				if (rank > 50)
					break;
			}
		}
		
		xbean.RoleZongheRankList rolezonghelist = xtable.Rolezonghelist.select(1);
		if(rolezonghelist!=null && rolezonghelist.getRecords()!=null){
			mkdb.Trace.info("print rolezonghelist");
			int rank = 1;
			for(xbean.RoleZongheRankRecord record : rolezonghelist.getRecords()){
				Map<String, Object> paras = new HashMap<String, Object>();
				xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(new PropRole(record.getRoleid(), true).getUserid());
				paras.put(RemoteLogParam.OS, auUserInfo!=null?auUserInfo.getOs():"");
				String platformid = xtable.Properties.selectPlatformuid(record.getRoleid());
				if(platformid.length()>4){
					paras.put(RemoteLogParam.PLATFORM, platformid.substring(platformid.length()-4));
				}else{
					paras.put(RemoteLogParam.PLATFORM, auUserInfo ==null?"":auUserInfo.getNickname().substring(0, Math.min(4, auUserInfo.getNickname().length())));
				}
//				LogUtil.putRoleBasicParams(record.getRoleid(), paras);
				paras.put(RemoteLogParam.SCORE, record.getScore());
				paras.put(RemoteLogParam.ROLENAME, record.getRolename());
				paras.put(RemoteLogParam.RANK, rank);

//				LogManager.getInstance().doLog(RemoteLogID.ZONGHE_RANK, paras);
				
				rank++;
				
				if (rank > 50)
					break;
			}
		}
		
		return true;
	}

}

