package mylogic;


public class UniquepetsDel {
	
	public static void remove(long roleId){
		//³èÎï²Ö¿â
		xbean.Pets pets=xtable.Petdepot.get(roleId);
		if(pets!=null){
			for(xbean.PetInfo info:pets.getPetmap().values()){
				if(info == null){
					continue;
				}
				xtable.Uniquepets.remove(info.getUniqid());
			}
		}
		//pet	³èÎï
		xbean.Pets pets2=xtable.Pet.get(roleId);
		if(pets2!=null){
			for(xbean.PetInfo info:pets2.getPetmap().values()){
				if(info == null){
					continue;
				}
				xtable.Uniquepets.remove(info.getUniqid());
			}
		}
		xtable.Petdepot.remove(roleId);//petdepot	³èÎï²Ö¿â
		xtable.Pet.remove(roleId);//pet	³èÎï
	}

}
