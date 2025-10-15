package fire.pb.ranklist.getrankinfo;

import fire.pb.PropRole;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.ranklist.proc.RankListManager;
import mkdb.Procedure;

/***
 * 获取排行榜信息
 * @author changhao
 *
 */
public class PRoleInfoGet extends Procedure{
	private int rank;
	private long roleId;
	private long id;
	private int rankType;
	
	public PRoleInfoGet(int rank,long roleId, long id, int rankType){
		this.rank = rank;
		this.roleId = roleId;
		this.id = id;
		this.rankType = rankType;
	}

	@Override
	protected boolean process() throws Exception
	{
		long rankRoleId = -1;
		
		if (rank == -1)
		{
			rankRoleId = roleId;
		}
		else if (rankType == fire.pb.ranklist.RankType.RED_PACK_1
				||rankType == fire.pb.ranklist.RankType.RED_PACK_2)
		{
			xbean.RedPackRankList list = xtable.Redpacklist.select(rankType);
			if(list == null)
			{
				return false;
			}
			
			int count = 0;
			for (xbean.RedPackRecord e : list.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.RedPackRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getMarshaldata().getRoleid();			
		}
		else if (rankType == fire.pb.ranklist.RankType.FLOWER_RECEIVE)
		{
			long t = RankListManager.GetMonday1Millisecond();
			xbean.ReceFlowerRankList list = xtable.Receflowerlist.select(t);
			if(list == null)
			{
				return false;
			}
			
			int count = 0;
			for (xbean.ReceFlowerRecord e : list.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.ReceFlowerRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getMarshaldata().getRoleid();			
		}
		else if (rankType == fire.pb.ranklist.RankType.FLOWER_GIVE)
		{
			long t = RankListManager.GetMonday1Millisecond();
			xbean.GiveFlowerRankList list = xtable.Giveflowerlist.select(t);
			if(list == null)
			{
				return false;
			}
			
			int count = 0;
			for (xbean.GiveFlowerRecord e : list.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.GiveFlowerRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getMarshaldata().getRoleid();			
		}
		else
		{
			xbean.RoleLevelRankList list = xtable.Rolelevellist.select(1);
			if(list == null){
				return false;
			}
			
			int count = 0;
			for (xbean.RoleLevelListRecord e : list.getRecords())
			{
				if (e.getMarshaldata().getRoleid() == id)
				{
					rank = count;
					break;
				}
				
				count ++;
			}
			
			xbean.RoleLevelListRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getMarshaldata().getRoleid();
		}
		
		PropRole prop = new PropRole(rankRoleId,true);
		
		SRankRoleInfo send = new SRankRoleInfo();
		send.roleid = rankRoleId;
		send.shape = prop.getShape();
		send.rolename = prop.getName();
		send.level = prop.getLevel();
		send.zonghescore = prop.getZhongheScore();
		send.school = prop.getSchool();
		Long factionId = prop.getClanKey();
		if (factionId > 0){
			xbean.ClanInfo info = xtable.Clans.select(factionId);
			if(info != null && info.getClanname() != null){
				send.factionname = info.getClanname();
			}else{
				send.factionname = "";
			}
		}else{
			send.factionname = "";
		}
		
		if(prop.getFightpetkey() > 0){
			final PetColumn petcol = new PetColumn(rankRoleId, PetColumnTypes.PET, true);
			Pet pet = petcol.getPet(prop.getFightpetkey());
			if(pet!=null){
				send.petscore = pet.getPetInfo().getPetscore();
			}else{
				send.petscore = 0;
			}
		}else{
			send.petscore = 0;
		}
		
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
		return true;
	}
	
}
