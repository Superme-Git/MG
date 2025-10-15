package fire.pb.item;

import fire.pb.attr.EffectType;

public class QualityToEffect {
	private QualityToEffect( fire.pb.main.ConfigManager cm ) {
		formulaMap = cm.getConf(fire.pb.item.QualityFormula.class);
	}
	private static QualityToEffect instance = new QualityToEffect( 
			fire.pb.main.ConfigManager.getInstance() );
	private java.util.Map<Integer, QualityFormula> formulaMap = new java.util.TreeMap<Integer, QualityFormula>();
	public static QualityToEffect getInstance() {
		return instance;
	}
	public java.util.List<Effect> getEffect( int itemid, int quality ) {
		java.util.List<Effect> effects = new java.util.Vector<Effect>();
		QualityFormula qualityFormula = formulaMap.get( itemid );
		if ( qualityFormula == null ) {
			return null;
		}
		
		float effectvalue1 = (float)(quality * qualityFormula.品质系数1 + qualityFormula.截距系数1);
		float effectvalue2 = (float)(quality * qualityFormula.品质系数2 + qualityFormula.截距系数2);
		if ( qualityFormula.效果1id != 0 ) {
			effects.add( new Effect( qualityFormula.效果1id, effectvalue1 ) );
		}
		if ( qualityFormula.效果2id != 0 ) {
			effects.add( new Effect( qualityFormula.效果2id, effectvalue2 ) );
		}
		if ( fire.pb.util.Misc.getRandomBetween( 1, 100 ) <= qualityFormula.中毒概率 ) {
			effects.add( new Effect( EffectType.PET_ATTACK_APT_ABL, -15 ) );
			effects.add( new Effect( EffectType.PET_DEFEND_APT_ABL, -15 ) );
			effects.add( new Effect( EffectType.PET_DODGE_APT_ABL, -15 ) );
			effects.add( new Effect( EffectType.PET_MAGIC_APT_ABL, -15 ) );
			effects.add( new Effect( EffectType.PET_PHYFORCE_APT_ABL, -15 ) );
			effects.add( new Effect( EffectType.PET_SPEED_APT_ABL, -15 ) );
		}
		return effects;
	}
}
