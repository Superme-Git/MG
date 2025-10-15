package fire.pb.scoremanager;

import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.effect.SAbilityScore;
import fire.pb.effect.SEquipAbilityScore;
import fire.pb.effect.SPetAbilityScore;

public class Module {

	private static Module _instance;
	Logger logger = Logger.getLogger("SYSTEM");
	
	public static Module getInstance() {
		if (_instance == null)
			_instance = new Module();
		
		return _instance;
	}
	
	private Module() {}
	
	public static final Map<Integer, SAbilityScore> AbilityScore = fire.pb.main.ConfigManager.getInstance().getConf(SAbilityScore.class);
	public static final Map<Integer, SPetAbilityScore> PetAbilityScore = fire.pb.main.ConfigManager.getInstance().getConf(SPetAbilityScore.class);
	public static final Map<Integer, SEquipAbilityScore> EquipAbilityScore = fire.pb.main.ConfigManager.getInstance().getConf(SEquipAbilityScore.class);
	
	public static void Init() {
		
	}
	
	/**
	 * 获取能力评分参数
	 * @param id 跟表里的id对应
	 * @return
	 */
	public double getCoe(final int id) {
		if (!AbilityScore.containsKey(id)) {
			return 0.0;
		}
		
		SAbilityScore score = AbilityScore.get(id);
		return score.coefficient;
	}
	
	/**
	 * 获取装备二级属性评分参数
	 * @param id 跟表里的id对应
	 * @return
	 */
	public double getEquipCoe(final int id) {
		if (!EquipAbilityScore.containsKey(id)) {
			return 0.0;
		}
		
		SEquipAbilityScore score = EquipAbilityScore.get(id);
		return score.coefficient;
	}
	
	public SEquipAbilityScore getEquipAbility(final int id) {
		if (!EquipAbilityScore.containsKey(id)) {
			return null;
		}
		
		SEquipAbilityScore AbilityScore = EquipAbilityScore.get(id);
		return AbilityScore;
	}
	
	public Map<Integer, SPetAbilityScore> getPetAbility() {
		return PetAbilityScore;
	}
	
}
