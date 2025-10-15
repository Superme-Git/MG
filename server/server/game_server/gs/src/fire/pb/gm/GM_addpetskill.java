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
import fire.pb.skill.fight.FightSkillConfig;

public class GM_addpetskill extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 3) {
			sendToGM(usage());
			return true;
		}

		try {
			final int skillId = Integer.valueOf(args[0]);
			final int exp = Integer.valueOf(args[1]);
			final int skillType = Integer.valueOf(args[1]);
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					int type = 0;
					if (skillType > 0) {
						type = 1;
					}
					PropRole prole = new PropRole(getGmroleid(), true);
					Integer fightpet = prole.getFightpetkey();
					if (fightpet < 0) {
						sendToGM("没有参战宠物，不能学习技能");
						return true;
					}
					if (skillId / 100000 != 2) {
						sendToGM("技能ID错误：不是宠物技能");
						return true;
					}
					FightSkillConfig skillcfg = fire.pb.skill.Module
							.getInstance().getSkillId2configs().get(skillId);
					if (skillcfg == null) {
						sendToGM("技能ID错误：没有该技能");
						return true;
					}
					PetColumn petColumn = new PetColumn(getGmroleid(),
							PetColumnTypes.PET, false);
					Pet pet = petColumn.getPet(fightpet);
					SkillPet spet = new SkillPet(pet.getPetInfo(), getGmroleid());
					pet.addSkill(skillId, -1, exp, type);
					Result result = spet.updateSkillBuffWhileOut(null);
					BuffAgent bagent = new BuffPetImpl(getGmroleid(), fightpet);
					bagent.psendSBuffChangeResult(result);
					SRefreshPetSkill sRefreshPetSkill = new SRefreshPetSkill();
					sRefreshPetSkill.petkey = fightpet;
					pet.fillSRefreshPetSkill(sRefreshPetSkill);
					psendWhileCommit(getGmroleid(), sRefreshPetSkill);
					sendToGM("参战宠物已学习技能，ID：" + skillId);
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
		return "给参战宠物添加宠物技能：//addpetskill [skillId] [exp] [skillType 0/1]";
	}

}
