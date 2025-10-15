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
 * 
 * 人物综合实力排行榜插入处理 by changhao
 *
 */
public class PRoleZongheRankProc extends Procedure {
	private long roleId;
	public PRoleZongheRankProc(long roleId){
		this.roleId = roleId;
	}
	
	public boolean process(){
		if (this.roleId < 1)
			return false;
		
		if(!StateCommon.isOnline(roleId))
		{
			return true;
		}
		
		final long now = java.util.Calendar.getInstance().getTimeInMillis();
		
		Long clanid = xtable.Roleidclan.select(this.roleId);
		String clanname = "";
		xbean.ClanInfo clanInfo = null;
		if (clanid != null)
		{
			clanInfo = Clans.get(clanid);
			if (clanInfo != null)
			{
				clanname = clanInfo.getClanname();				
			}					
		}
		
		PropRole prole = new PropRole(roleId, false);
		fire.pb.effect.RoleImpl roleImpl = new fire.pb.effect.RoleImpl(
				roleId, true);
		
		int oldscore = prole.getProperties().getRolezonghemaxscore();
		
		xbean.RoleZongheRankRecord record = xbean.Pod.newRoleZongheRankRecord();
		record.setRoleid(this.roleId);
		record.setRolename(prole.getName());
		record.setSchool(prole.getSchool());
		record.setTriggertime(now);
		record.setLevel(prole.getLevel());
		
		xbean.RoleProfessionRankRecord professionrecord = xbean.Pod
				.newRoleProfessionRankRecord();
		professionrecord.setRoleid(this.roleId);
		professionrecord.setRolename(prole.getName());		
		professionrecord.setSchool(prole.getSchool());
		professionrecord.setLevel(prole.getLevel());
		
		professionrecord.setClanname(clanname);
		professionrecord.setTriggertime(now);
		
		xbean.RoleRankRecord rolerecord = xbean.Pod.newRoleRankRecord();
		rolerecord.setRoleid(this.roleId);
		rolerecord.setRolename(prole.getName());		
		rolerecord.setSchool(prole.getSchool());
		rolerecord.setLevel(prole.getLevel());
		rolerecord.setTriggertime(now);
		
		/*
		 * 综合实力评分计算 综合评分=角色总评分+宠物总评分 角色总评分=装备评分*0.2+技能总等级*2.5+修炼总等级*20
		 * 宠物总评分=现有评分 注意：服务器计算的装备评分需与目前的客户端算法一致
		 */
		//计算人物技能评分
		int skillScore = 0;
		int xiulianScore = 0;
		SkillRole srole = new SkillRole(roleId);
		skillScore = srole.getInbornScore();
		skillScore += srole.getLiveSkillScore();
		xiulianScore = srole.getPracticeskillScore();
		
		//装备评分
		int equipScore = fire.pb.item.Module.getInstance().getEquipTotalScore(roleId);
		
		//宠物评分
		int manypetScore = 0;
		
		final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, false);
		java.util.List<Pet> petscores = new java.util.ArrayList<Pet>();
		
		int maxpetscore = -1;
		long bestpetid = 0;
		for (Pet pet : petcol.getPets()) 
		{
			if (pet != null)
			{
				if (prole.getProperties().getLevel() < Pet.getPetUseLevelConfig(pet.getPetInfo().getId()))
				{
					// 人物等级小于宠物参战等级 by changhao
					continue;
				}
				
				if (pet.getLevel() > 10 + prole.getProperties().getLevel())
				{
					continue;
				}
				
				manypetScore = pet.getPetInfo().getPetscore();
				//sortscore.put(manypetScore, pet.getPetInfo().getUniqid());
				petscores.add(pet);
				
				if (pet.getPetInfo().getPetscore() > maxpetscore)
				{
					maxpetscore = pet.getPetInfo().getPetscore();
					bestpetid = pet.getPetInfo().getUniqid();
				}
			}
		}
		
		//分数从大到小排序 by changhao
		petscores.sort(new Comparator<Pet>(){
			@Override
			public int compare(Pet o1, Pet o2)
			{
				return o2.getPetInfo().getPetscore() - o1.getPetInfo().getPetscore();
			}
		});
			
		int num = 3;
		fire.pb.role.NextExpAndResMoney roleconfig = ConfigManager.getInstance().getConf(fire.pb.role.NextExpAndResMoney.class).get(prole.getProperties().getLevel());
		if (roleconfig != null)
		{
			num = roleconfig.petfightnum;
		}
		
		manypetScore = 0;
		int count = 0;
		int singlePetScore = 0;
		for (Pet e : petscores)
		{
			manypetScore += e.getPetInfo().getPetscore();
			if (count == 0)
			{
				singlePetScore = manypetScore;
				//bestpetid = e.getValue();
			}
			
			count ++;
			
			if (count >= num) {
				break;
			}
		}
		
		//玩家总评分
		int levelScore = roleImpl.updateScore();
		int roleTotalScore = equipScore	+ skillScore + manypetScore + levelScore + xiulianScore;
		
		//更新玩家的最好评分 by changhao
		prole.getProperties().setRolezonghemaxscore(roleTotalScore);
		prole.getProperties().setEquipscore(equipScore); 
		prole.getProperties().setManypetscore(manypetScore);
		prole.getProperties().setPetscore(singlePetScore);
		prole.getProperties().setXiulianscroe(xiulianScore);
		prole.getProperties().setSkillscore(skillScore);
		prole.getProperties().setLevelscore(levelScore);
		prole.getProperties().setRolescore(roleTotalScore - manypetScore);
		prole.getProperties().setBestpetid(bestpetid);
		
		//if(prole.getLevel() < 30) return true; 不要等级限制 by changhao
		
		SRefreshRoleScore send = new SRefreshRoleScore();
		send.datas.put(SRefreshRoleScore.TOTAL_SCORE, prole.getProperties()
				.getRolezonghemaxscore());
		send.datas.put(SRefreshRoleScore.EQUIP_SCORE, prole.getProperties()
				.getEquipscore());
		send.datas.put(SRefreshRoleScore.MANY_PET_SCORE, prole.getProperties()
				.getManypetscore());
		send.datas.put(SRefreshRoleScore.PET_SCORE, prole.getProperties()
				.getPetscore());
		send.datas.put(SRefreshRoleScore.LEVEL_SCORE, prole.getProperties()
				.getLevelscore());
		send.datas.put(SRefreshRoleScore.ROLE_SCORE, prole.getProperties()
				.getRolescore());
		send.datas.put(SRefreshRoleScore.XIULIAN_SCORE, prole.getProperties()
				.getXiulianscroe());
		send.datas.put(SRefreshRoleScore.SKILL_SCORE, prole.getProperties()
				.getSkillscore());
		
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
		xbean.RoleZongheRankList list = xtable.Rolezonghelist.get(1);
		if (null == list) {
			list = xbean.Pod.newRoleZongheRankList();
			xtable.Rolezonghelist.insert(1, list);
		}
		
		record.setScore(roleTotalScore);
		RankListManager.getInstance().tryInsertRecord(
				RankType.ROLE_ZONGHE_RANK, list.getRecords(), record);
		
		int ranktype = ProfessionToRankType(prole.getProperties().getSchool());
		
		//如果还没有角色职业表创建 by changhao
		xbean.RoleProfessionRankList professionlist = xtable.Roleprofessionranklist
				.get(ranktype);
		if (professionlist == null) {
			professionlist = xbean.Pod.newRoleProfessionRankList();
			xtable.Roleprofessionranklist.insert(ranktype, professionlist);			
		}
		
		professionrecord.setScore(roleTotalScore); //职业榜 by changhao
		RankListManager.getInstance().tryInsertRecord(ranktype,
				professionlist.getRecords(), professionrecord);
		
		//如果还没有人物表创建 by changhao
		xbean.RoleRankList rolelist = xtable.Rolerankdatalist.get(1);
		if (rolelist == null) {
			rolelist = xbean.Pod.newRoleRankList();
			xtable.Rolerankdatalist.insert(1, rolelist);			
		}
		
		rolerecord.setScore(roleTotalScore - manypetScore);
		RankListManager.getInstance().tryInsertRecord(RankType.ROLE_RANK,
				rolelist.getRecords(), rolerecord);
		
		int delta = roleTotalScore - oldscore;
		
		if (clanInfo != null) //如果玩家有公会更新公会的综合实力 by changhao
		{
			int oldfactionscore = clanInfo.getTotalscore();
			oldfactionscore += delta;
			if (oldfactionscore < 0)
				oldfactionscore = 0;
			
			clanInfo.setTotalscore(oldfactionscore);
			
			//更新公会综合实力排行 by changhao
			mkdb.Procedure.pexecuteWhileCommit(new PFactionZongHeProc(
					clanInfo.getKey(), false));	
		}
		
		return true;
	}
	
	public int ProfessionToRankType(int school)
	{
		if (school == 11)
		{
			return RankType.PROFESSION_WARRIOR_RANK;
		}
		else if (school == 12)
		{
			return RankType.PROFESSION_PALADIN_RANK;			
		}
		else if (school == 13)
		{
			return RankType.PROFESSION_HUNTER_RANK;			
		}
		else if (school == 14)
		{
			return RankType.PROFESSION_DRUID_RANK;			
		}
		else if (school == 15)
		{
			return RankType.PROFESSION_MAGIC_RANK;			
		}
		else if (school == 16)
		{
			return RankType.PROFESSION_PRIEST_RANK;	
		}
		else if (school == 17)
		{
			return RankType.PROFESSION_SAMAN_RANK;			
		}
		else if (school == 18)
		{
			return RankType.PROFESSION_ROGUE_RANK;			
		}
		else if (school == 19)
		{
			return RankType.PROFESSION_WARLOCK_RANK;	
		}
		
		return 0;
	}

}
