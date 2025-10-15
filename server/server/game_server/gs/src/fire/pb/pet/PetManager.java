package fire.pb.pet;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.main.ConfigManager;
import fire.pb.skill.SPetSkillitem;
import fire.pb.skill.SPetSkillupgrade;
import fire.pb.skill.SSkillConfig;

/**
 * 宠物配置
 */
public class PetManager {

	java.util.NavigableMap<Integer, PetAttr> petAttrConfigMap = new java.util.TreeMap<Integer, PetAttr>();

	java.util.NavigableMap<Integer, ArrayList<SShenShouInc>> shenshouIncConfigMap = new java.util.TreeMap<Integer, ArrayList<SShenShouInc>>();
	java.util.ArrayList<PetAttr> shenshouAttrConfigList = new java.util.ArrayList<PetAttr>();

	static class SingletonHolder {
		static PetManager singleton = new PetManager();
	}

	public static PetManager getInstance() {
		return SingletonHolder.singleton;
	}

	private PetManager() {
		reload();
	}

	public void reload() {
		petAttrConfigMap = fire.pb.main.ConfigManager.getInstance().getConf(PetAttr.class);
		Map<Integer, SShenShouInc> shenshouInc= fire.pb.main.ConfigManager.getInstance().getConf(SShenShouInc.class);
		for (Entry<Integer, SShenShouInc> entry : shenshouInc.entrySet()) {
			int petId = entry.getValue().getPetid();
			if(!shenshouIncConfigMap.containsKey(petId)){
				shenshouIncConfigMap.put(petId, new ArrayList<SShenShouInc>());
			}
			
			ArrayList<SShenShouInc> listShenShouInc = shenshouIncConfigMap.get(petId);
			listShenShouInc.add(entry.getValue());
		}
		
		for (PetAttr pa : petAttrConfigMap.values()) {
			if(pa.getKind() == PetTypeEnum.SACREDANIMAL){
				shenshouAttrConfigList.add(pa);
			}
		}
		
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("PetAttr load size=" + petAttrConfigMap.size());
		}
	}

	public PetAttr getAttr(int petId) {
		return petAttrConfigMap.get(petId);
	}

	public SPetSkillupgrade getSkillUpGrade(int skillId) {
		Map<Integer, SPetSkillupgrade> map = fire.pb.main.ConfigManager.getInstance().getConf(SPetSkillupgrade.class);
		if (map != null) {
			return map.get(skillId);
		}
		return null;
	}

	public int getSkillScore(int skillId) {
		SPetSkillitem skillItem = ConfigManager.getInstance().getConf(SPetSkillitem.class).get(skillId);
		if (skillItem != null) {
			return skillItem.getScore();
		}
		return 0;
	}

	public boolean isActiveSkill(int skillId) {
		SSkillConfig cfg = ConfigManager.getInstance().getConf(SSkillConfig.class).get(skillId);
		if (cfg == null) {
			return false;
		}
		return cfg.isActive;
	}
	
	public PetAttr randGetOneShenShou(){
		final int randIndex = (int)(Math.random() * shenshouAttrConfigList.size());
		return shenshouAttrConfigList.get(randIndex);
	}
	
	public final SShenShouInc getShenShouIncConfig(int petId, int hasIncCount) {
		ArrayList<SShenShouInc> incInfo = shenshouIncConfigMap.get(petId);
		for (SShenShouInc sShenShouInc : incInfo) {
			if(sShenShouInc.getInccount() == (hasIncCount + 1)){
				return sShenShouInc;
			}
		}
		return null;
	}
	
	/**
	 * 是否宠物参战的寿命是永久的
	 */
	public boolean isPetLifeForever(int petId) {
		PetAttr petAttr = getAttr(petId);
		if(petAttr != null && petAttr.getLife() == -1){
			return true;
		}
		return false;
	}
}