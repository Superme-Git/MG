package fire.pb.ranklist.provider;

import fire.pb.ranklist.RankType;



/***
 * 排行榜数据工厂
 * @author changhao
 *
 */
public class RankDataFactory {
	
	private static RankDataFactory _instance;
	
	private RankDataFactory(){}
	
	public static RankDataFactory getInstance(){
		synchronized (RankData.class) {
			if(null == _instance){
				_instance = new RankDataFactory();
			}
		}
		
		return _instance;
	}

	public IRankListData createRankProvider(int rankType, long requestRoleid){
		IRankListData provider = null;
		
		switch (rankType) {
		case RankType.LEVEL_RANK:
			provider = new LevelRankData(rankType, requestRoleid);
			break;
		case RankType.PET_GRADE_RANK:
			provider = new PetScoreRankData(rankType, requestRoleid);
			break;
		case RankType.FACTION_RANK:
			provider = new ClanRankData(rankType, requestRoleid);
			break;
		case RankType.ROLE_ZONGHE_RANK:
			provider = new RoleZongheRankData(rankType, requestRoleid);
			break;
        case RankType.PROFESSION_WARRIOR_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;	
            
        case RankType.PROFESSION_MAGIC_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;	
            
        case RankType.PROFESSION_PRIEST_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;	
            
        case RankType.PROFESSION_PALADIN_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;	
            
        case RankType.PROFESSION_HUNTER_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;	
            
        case RankType.PROFESSION_DRUID_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;
            
        case RankType.PROFESSION_ROGUE_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;
            
        case RankType.PROFESSION_SAMAN_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;
            
        case RankType.PROFESSION_WARLOCK_RANK:
            provider = new ProfessionZongheRankData(rankType, requestRoleid);
            break;
            
        case RankType.ROLE_RANK:
            provider = new RoleRankData(rankType, requestRoleid);
            break;
            
        case RankType.FACTION_RANK_LEVEL:
            provider = new ClanRankData(rankType, requestRoleid);
            break;            
            
        case RankType.FACTION_ZONGHE:
            provider = new ClanZongHeRankData(rankType, requestRoleid);
            break;
            
        case RankType.SINGLE_COPY_RANK1:
        case RankType.SINGLE_COPY_RANK2:
        case RankType.SINGLE_COPY_RANK3:
        case RankType.SINGLE_COPY_RANK4:
        	provider = new BingFengRankData(rankType, requestRoleid);
        	break;
        	
        case RankType.FACTION_COPY:
            provider = new ClanProgressRankData(rankType, requestRoleid);
            break;
        case RankType.PVP5_LAST_GRADE1:
        case RankType.PVP5_LAST_GRADE2:
        case RankType.PVP5_LAST_GRADE3:
        case RankType.PVP5_HISTORY_GRADE1:
        case RankType.PVP5_HISTORY_GRADE2:
        case RankType.PVP5_HISTORY_GRADE3:
        	provider = new PvP5RankData(rankType, requestRoleid);
        	break;
        	
        case RankType.RED_PACK_1:
        case RankType.RED_PACK_2:    
        	provider = new RedPackRankData(rankType, requestRoleid);
        	break;
        	
        case RankType.FLOWER_RECEIVE:
        	provider = new ReceFlowerRankData(rankType, requestRoleid);
        	break;
        	
        case RankType.FLOWER_GIVE:
        	provider = new GiveFlowerRankData(rankType, requestRoleid);
        	break;
        	
        case RankType.CLAN_FIGHT_2:
        case RankType.CLAN_FIGHT_4:
        case RankType.CLAN_FIGHT_WEEK:
            provider = new ClanFightRaceRankData(rankType, requestRoleid);
            break;
            
        case RankType.CLAN_FIGHT_HISTROY:
            provider = new ClanFightHistroyRankData(rankType, requestRoleid);
            break;
		}
		return provider;
	}
	
}
