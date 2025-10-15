package fire.pb.ranklist.getrankinfo;

import fire.pb.PropRole;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/***
 * 得到宠物信息
 * @author changhao
 *
 */
public class PGetPetData extends Procedure{
	private int rank;//rank=-1表示从综合实力榜界面上点进来的 rank=-2 自己  rank==-3他人宠物by changhao
	private long roleId;
	private long rankRoleId;
	private long id;
	
	public PGetPetData(int rank, long roleId, long rankRoleId, long id){
		this.rank = rank;
		this.roleId = roleId;
		this.rankRoleId = rankRoleId;
		this.id = id;
	}

	@Override
	protected boolean process() throws Exception {
		boolean fromZonghe = false;
		
		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(rankRoleId);
		if (rankRoleId != 0 && role == null && rank != -3){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146501, null);//玩家不在线，宠物数据可能不对
		}
		
		Pet pet = null;
		if(rank == -1 && rankRoleId != 0)
		{
			fromZonghe = true;
			
			PropRole prop = new PropRole(rankRoleId,true);
			if(prop.getFightpetkey() > 0){
				final PetColumn petcol = new PetColumn(rankRoleId, PetColumnTypes.PET, true);
				pet = petcol.getPet(prop.getFightpetkey());
				
				xbean.PetScoreRankList list = xtable.Petscorelist.select(1);
				if(list == null){
					return false;
				}
				
				int i = 0;
				for(xbean.PetScoreListRecord record : list.getRecords()){
					if(record.getMarshaldata().getUniquepetid() == pet.getUniqueId()){
						rank = i;
						break;
					}
					i++;
				}
			}
		}
		else if (rank == -2)
		{			
			final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, true);			
			int maxpetscore = -1;
			long maxscorepetid = 0;
			for (Pet petinfo : petcol.getPets()) 
			{
				if (petinfo != null)
				{					
					if (petinfo.getPetInfo().getPetscore() > maxpetscore)
					{
						maxpetscore = petinfo.getPetInfo().getPetscore();
						maxscorepetid = petinfo.getPetInfo().getUniqid();
					}
				}
			}	
			
			pet = Pet.getPet(maxscorepetid, true);
			rankRoleId = roleId;
		}
		else if (rank == -3)
		{
			PropRole prop = new PropRole(rankRoleId, true);
			PetColumn petcol = new PetColumn(rankRoleId, PetColumnTypes.PET, true);
			if (petcol != null)
				pet = petcol.getPet(prop.getProperties().getFightpetkey());
			
			rankRoleId = roleId;
		}		
		else if(rank >= 0 && rankRoleId == 0){
			xbean.PetScoreRankList list = xtable.Petscorelist.select(1);
			if(list == null){
				return false;
			}
			
			int count = 0;
			boolean find = false;
			for (xbean.PetScoreListRecord e : list.getRecords())
			{
				if (e.getMarshaldata().getUniquepetid() == id)
				{
					rank = count;
					find = true;
					break;
				}
				
				count ++;
			}
			
			if (find == false)
			{
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 166010, null);
				return false;				
			}
						
			if (rank >= list.getRecords().size())
			{
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 166010, null);
				return false;
			}
			
			xbean.PetScoreListRecord record = list.getRecords().get(rank);
			if(record == null){
				return false;
			}
			
			rankRoleId = record.getMarshaldata().getRoleid();
			long uniquePetId = record.getMarshaldata().getUniquepetid();
			pet = Pet.getPet(uniquePetId, true);
			
			if (pet == null) //取摆摊宠物 by changhao
			{
				fire.pb.pet.UniquePetImpl uniquePetWrap = new fire.pb.pet.UniquePetImpl(uniquePetId, true);
				if (uniquePetWrap != null)
				{
					pet = uniquePetWrap.getPetFromMarket(rankRoleId);
				}
			}
		}
		
		if(pet == null || rankRoleId == 0){
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 146426, null);
			return false;
		}
				
		fire.pb.Pet petMarshal = pet.getProtocolPet();
		fire.pb.shop.SMarketPetTips send = new fire.pb.shop.SMarketPetTips(petMarshal, 5);
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
		return true;
	}
	
}
