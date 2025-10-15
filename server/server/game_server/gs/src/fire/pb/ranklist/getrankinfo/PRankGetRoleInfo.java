package fire.pb.ranklist.getrankinfo;

import java.util.List;

import fire.pb.Item;
import fire.pb.battle.pvp5.PvP5RankVisitor;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Module;
import fire.pb.ranklist.RankType;
import mkdb.Procedure;

/***
 * 获取详细信息 by changhao
 * @author 
 *
 */
public class PRankGetRoleInfo extends Procedure{
	private long roleId;
	private int rank;
	private int rankType;
	private long id;
	
	public PRankGetRoleInfo(int rank,long roleId, int rankType, long id){
		this.roleId = roleId;
		this.rank = rank;
		this.rankType = rankType;
		this.id = id;
	}

	@Override
	protected boolean process() throws Exception
	{
		long rankRoleId = roleId;
		if (rank == -1)
		{
			
		}
		else if (rankType == RankType.ROLE_ZONGHE_RANK)
		{
			xbean.RoleZongheRankList list = xtable.Rolezonghelist.select(1);
			if(list == null)
			{
				return false;
			}
			
			int count = 0;
			for (xbean.RoleZongheRankRecord e : list.getRecords())
			{
				if (e.getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.RoleZongheRankRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getRoleid(); //取出对应等级的角色ID by changhao			
		}
		else if (rankType == RankType.PROFESSION_WARRIOR_RANK
				|| rankType == RankType.PROFESSION_MAGIC_RANK
				|| rankType == RankType.PROFESSION_PRIEST_RANK
				|| rankType == RankType.PROFESSION_PALADIN_RANK
				|| rankType == RankType.PROFESSION_HUNTER_RANK
				|| rankType == RankType.PROFESSION_DRUID_RANK
				|| rankType == RankType.PROFESSION_ROGUE_RANK
				|| rankType == RankType.PROFESSION_SAMAN_RANK
				|| rankType == RankType.PROFESSION_WARLOCK_RANK)
		{
			xbean.RoleProfessionRankList list = xtable.Roleprofessionranklist.select(rankType);
			if(list == null)
			{
				return false;
			}
			
			int count = 0;
			for (xbean.RoleProfessionRankRecord e : list.getRecords())
			{
				if (e.getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.RoleProfessionRankRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getRoleid(); //取出对应等级的角色ID by changhao	
		}
		else if (rankType == RankType.ROLE_RANK)
		{
			xbean.RoleRankList list = xtable.Rolerankdatalist.select(1);
			if(list == null)
			{
				return false;
			}
			
			int count = 0;
			for (xbean.RoleRankRecord e : list.getRecords())
			{
				if (e.getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.RoleRankRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getRoleid(); //取出对应等级的角色ID by changhao				
		} else if (rankType == RankType.PVP5_LAST_GRADE1
				|| rankType == RankType.PVP5_LAST_GRADE2
				|| rankType == RankType.PVP5_LAST_GRADE3
				|| rankType == RankType.PVP5_HISTORY_GRADE1
				|| rankType == RankType.PVP5_HISTORY_GRADE2
				|| rankType == RankType.PVP5_HISTORY_GRADE3) {

			xbean.PvP5ScoreRankList rankList = null;
			int grade = PvP5RankVisitor.getLastRankGrade(rankType);
			if (grade != -1) {
				// 上届榜单
				rankList = xtable.Pvp5scorelist.select(grade);
			} else {
				grade = PvP5RankVisitor.getHistoryRankGrade(rankType);
				if (grade != -1) {
					// 历史榜单
					rankList = xtable.Pvp5historyscorelist.select(grade);
				}
			}
			if (rankList != null) {
				List<xbean.PvP5ScoreRecord> records = rankList.getRecords();
				int recordSize = records.size();
				for (int i = 0; i < recordSize; i++) {
					xbean.PvP5ScoreRecord record = records.get(i);
					if (record.getRoleid() == id) {
						rank = i;
						rankRoleId = record.getRoleid();
						break;
					}
				}
			}
		}
		
		xbean.Properties targetRoleProp=xtable.Properties.select(rankRoleId);
		final ItemMaps bag=Module.getInstance().getItemMaps(rankRoleId,BagTypes.EQUIP,true);
		
		final SRankRoleInfo2 res=new SRankRoleInfo2();
		res.roleid = rankRoleId;
		res.rolename = targetRoleProp.getRolename();
		res.shape = targetRoleProp.getShape();
		res.level = targetRoleProp.getLevel();
		res.school = targetRoleProp.getSchool();
		
		Long factionId = targetRoleProp.getClankey();
		if (factionId > 0){
			String clanname = xtable.Clans.selectClanname(factionId);
			if(clanname != null){
				res.factionname = clanname;
			}
		}
		res.baginfo=bag.getPackInfo();
		res.rank = rank;
		for (Item item : res.baginfo.items){
			ItemBase bitem = bag.getItem(item.key);
			if (bitem == null)
				continue;
			res.tips.put( item.key, bitem.getTips() );
		}
		
		res.totalscore = targetRoleProp.getRolezonghemaxscore();
		res.rolescore = targetRoleProp.getRolescore();
		res.xiulianscore = targetRoleProp.getXiulianscroe();
		res.petscore = targetRoleProp.getPetscore(); //单宠物 by changhao
		res.manypetscore = targetRoleProp.getManypetscore(); //多宠物分数 by changhao
		res.skillscore = targetRoleProp.getSkillscore();
		res.equipscore = targetRoleProp.getEquipscore();
		res.levelscore = targetRoleProp.getLevelscore();
		res.ranktype = rankType;
		
		//人物造型
		fire.pb.map.Role.getPlayerComponents(rankRoleId, res.components);
	
		gnet.link.Onlines.getInstance().send(roleId, res);
		
		return true;
	}
}
