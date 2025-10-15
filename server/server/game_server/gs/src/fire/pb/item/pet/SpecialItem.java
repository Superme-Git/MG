package fire.pb.item.pet;


import java.util.HashMap;
import java.util.Map;

import fire.pb.PropRole;
import fire.pb.item.Commontext;
import fire.pb.item.ItemMgr;
import fire.pb.item.PetItem;
import fire.pb.item.Commontext.UseResult;
import fire.pb.pet.CalcPetAttr;
import fire.pb.pet.Module;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetTypeEnum;


public class SpecialItem extends PetItem {
	
	public static final int LOW_LEVEL_HUAN_TONG_DAN = 31002;
	public static final int HIGH_LEVEL_HUAN_TONG_DAN = 31087;
	
	public SpecialItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}
	
	public SpecialItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}
	
	public SpecialItem( ItemMgr im, int itemid, int genType ) {
		this(im, itemid);
	}
	

	@Override
	public UseResult appendToPet(int petkey, int usednum) {
		if (usednum!=1)
			return Commontext.UseResult.FAIL;
		final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(roleid, PetColumnTypes.PET, false);
		fire.pb.pet.Pet pet = petcol.getPet(petkey);
		if (pet == null)
			return Commontext.UseResult.FAIL;
		final xbean.PetInfo petinfo = pet.getPetInfo();
		if (pet.isLocked() != -1)
			return Commontext.UseResult.FAIL;
		PropRole prole = new PropRole(roleid, false);
		if(prole.getFightpetkey() == pet.getPetkey())
			return Commontext.UseResult.FAIL;
		if(prole.getShowpetkey() == pet.getPetkey())
			return Commontext.UseResult.FAIL;
		PetAttr petattr = Module.getInstance().getPetManager().getAttr(pet.getBaseId());
		if (petattr.uselevel < 95 && getItemId() != LOW_LEVEL_HUAN_TONG_DAN)
			return Commontext.UseResult.FAIL;
		if (petattr.uselevel >= 95 && getItemId() != HIGH_LEVEL_HUAN_TONG_DAN)
			return Commontext.UseResult.FAIL;
		
//		Map<String, Object> param = new HashMap<String, Object>();
//		LogUtil.putRoleBasicParams(roleId, param);
//		pet.fillLogParams(param);
		
		//修改为与宠物生成的流程相同
		
		Map<Integer,Object> initattrs = new HashMap<Integer, Object>();
		initattrs.put(CalcPetAttr.PET_NAME, petinfo.getName());
		
		int color = -1; 
		if (petinfo.getKind() == PetTypeEnum.BABY)
		{
			color = Pet.getClour(PetColour.WHITE);
		}
		int oldlevel = petinfo.getLevel();
		long oldexp = petinfo.getExp();
		xbean.PetInfo newpetinfo = PetColumn.createPet(roleid, petinfo.getId(), oldlevel, null, PetTypeEnum.BABY, color, petinfo.getIsbinded(), initattrs,petinfo.getUniqid());
		newpetinfo.setKey(petinfo.getKey());
		newpetinfo.setExp(oldexp);
		petcol.getPetsMap().put(newpetinfo.getKey(), newpetinfo);
		//do log
		Pet newpet = Pet.getPet(newpetinfo);
		try{
			newpet.updatePetScoreWhileChange();
			fire.pb.course.CourseManager.checkAchieveCourse(roleid, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());
		}catch(Exception e){
			e.printStackTrace();
		}
		fire.pb.pet.SRefreshPetInfo send = new fire.pb.pet.SRefreshPetInfo(newpet.getProtocolPet());
		mkdb.Procedure.psendWhileCommit(roleid, send);
		return Commontext.UseResult.SUCC;
	}
}