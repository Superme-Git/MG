package mylogic;


public class UniquepetsDel {
	
	public static void remove(long roleId){
		//����ֿ�
		xbean.Pets pets=xtable.Petdepot.get(roleId);
		if(pets!=null){
			for(xbean.PetInfo info:pets.getPetmap().values()){
				if(info == null){
					continue;
				}
				xtable.Uniquepets.remove(info.getUniqid());
			}
		}
		//pet	����
		xbean.Pets pets2=xtable.Pet.get(roleId);
		if(pets2!=null){
			for(xbean.PetInfo info:pets2.getPetmap().values()){
				if(info == null){
					continue;
				}
				xtable.Uniquepets.remove(info.getUniqid());
			}
		}
		xtable.Petdepot.remove(roleId);//petdepot	����ֿ�
		xtable.Pet.remove(roleId);//pet	����
	}

}
