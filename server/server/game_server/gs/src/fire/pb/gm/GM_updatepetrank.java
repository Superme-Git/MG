package fire.pb.gm;

import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;

/***
 * 更新宠物排行榜 by changhao
 */
public class GM_updatepetrank extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Long roleid = null;
		try {
			roleid = Long.parseLong(args[0]);
			
		} catch (java.lang.NumberFormatException e) {
	
		}
		
		if (roleid != null)
		{
			fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(roleid, PetColumnTypes.PET, true);
			for (Pet pet : petcol.getPets()) 
			{
				if (pet != null)
				{
					new fire.pb.ranklist.proc.PRankInsertPet(pet.getUniqueId(), false).submit();						
				}				
			}	
		}
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
