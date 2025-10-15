package fire.pb.ranklist.provider;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.proc.RankListManager;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/***
 * 宠物排行榜
 * @author changhao
 *
 */
public class PetScoreRankData extends RankData implements IRankListData {

	PetScoreRankData(int rankype, long requestRoleid) {
		super(rankype, requestRoleid);
	}

	@Override
	public List<Octets> getMO(SRequestRankList response, int page) {
		xbean.PetScoreRankList petScoreList = xtable.Petscorelist.select(1);
		
		PropRole prole = new PropRole(requestRoleid, true);
		if(null == petScoreList)
		{	
			Pet pet = Pet.getPet(prole.getProperties().getBestpetid(), true);
			if (pet != null)
			{
				response.mytitle = pet.getName();		
			}
	
			response.extdata = prole.getProperties().getPetscore();
			return null;
		}

		int start = page * RankListManager.PAGE_SIZE;
		int end = (page + 1)* RankListManager.PAGE_SIZE;
		int hasMorePage = (page + 1) * RankListManager.PAGE_SIZE < petScoreList.getRecords().size() ? 0:1;

		boolean isRecordMyRank = false;
		long maxscorepetid = 0;
		List<com.locojoy.base.Octets> octets = new ArrayList<com.locojoy.base.Octets>();
		List<xbean.PetScoreListRecord> records = petScoreList.getRecords();
		for(int i=0; i<records.size(); i++){
			xbean.PetScoreListRecord current = records.get(i);
			if(i >= start && i < end) {
				OctetsStream os = new OctetsStream();
				xbean.MarshalPetScoreRecord marshalRecord = current.getMarshaldata();
				marshalRecord.setRank(i+1);
				octets.add(os.marshal(marshalRecord));
			}
			if(current.getMarshaldata().getRoleid() == requestRoleid && !isRecordMyRank){
				response.myrank = i+1;
				isRecordMyRank = true;
				maxscorepetid = current.getMarshaldata().getUniquepetid(); 
			}
		}
		response.hasmore = hasMorePage;
		response.page = page;
		
		if (maxscorepetid == 0)
		{
			final PetColumn petcol = new PetColumn(requestRoleid, PetColumnTypes.PET, true);			
			int maxpetscore = -1;
			for (Pet pet : petcol.getPets()) 
			{
				if (pet != null)
				{					
					if (pet.getPetInfo().getPetscore() > maxpetscore)
					{
						maxpetscore = pet.getPetInfo().getPetscore();
						maxscorepetid = pet.getPetInfo().getUniqid();
					}
				}
			}			
		}
			
		Pet pet = Pet.getPet(maxscorepetid, true);
		if (pet != null)
		{
			response.mytitle = pet.getName();
			response.extdata = pet.getPetInfo().getPetscore();
		}
		else
		{
			response.mytitle = "";
			response.extdata = 0;			
		}
		
		return octets;
	}
}
