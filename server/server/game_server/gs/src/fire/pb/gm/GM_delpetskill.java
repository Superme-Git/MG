package fire.pb.gm;

import fire.pb.PropRole;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.SRefreshPetSkill;
import fire.pb.skill.Result;
import fire.pb.skill.SkillPet;

public class GM_delpetskill extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		try {
			final int skillId = Integer.valueOf(args[0]);
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					PropRole prole = new PropRole(getGmroleid(), true);
					Integer fightpet = prole.getFightpetkey();
					if (fightpet < 0) {
						sendToGM("没有参战宠物，不能学习技能");
						return true;
					}
					PetColumn petColumn = new PetColumn(getGmroleid(),
							PetColumnTypes.PET, false);
					Pet pet = petColumn.getPet(fightpet);
					if (pet == null)
						return false;
					if (!pet.hasSkill(skillId)) {
						sendToGM("参战宠物没有此技能");
						return true;
					}
					pet.removeSkillById(skillId);
					SkillPet spet = new SkillPet(pet.getPetInfo(), getGmroleid());
					Result result = spet.addSkillBuffWhileOnline(null);
					BuffAgent bagent = new BuffPetImpl(getGmroleid(), fightpet);
					bagent.psendSBuffChangeResult(result);
					SRefreshPetSkill sRefreshPetSkill = new SRefreshPetSkill();
					sRefreshPetSkill.petkey = fightpet;
					pet.fillSRefreshPetSkill(sRefreshPetSkill);
					psend(getGmroleid(), sRefreshPetSkill);
					sendToGM("参战宠物已删除技能，ID：" + skillId);
					return true;
				}
			}.submit().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	String usage() {
		return null;
	}

}
