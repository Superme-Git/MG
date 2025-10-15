package fire.pb.buff.single;

import java.util.Map;

import fire.pb.PropConf;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public class EPFixedInjure extends SingleBuff
{
	
	public EPFixedInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		init(battleInfo, opfighter, aimfighter, effects);

		FixedEP();
		
		end();
		
		return demoresult;
	}
	
	protected void FixedEP()
	{
		JavaScript changeepscript = effects.get(EffectType.EFFECT_POINT_ABL);
		if (changeepscript != null)
		{
			int changeep = changeepscript.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			if(changeep!=0)
			{
				demoresult.epchange = changeep;
				xbean.Fighter fighter = opfighter.getFighterBean();
				if(fighter != null)
				{				
					if(PropConf.Battle.PROP_EP_MAX < fighter.getInitattrs().get(AttrType.EFFECT_POINT)+changeep)
					{
						demoresult.epchange = (int) (PropConf.Battle.PROP_EP_MAX - fighter.getInitattrs().get(AttrType.EFFECT_POINT));
						fighter.getInitattrs().put(AttrType.EFFECT_POINT, (float) PropConf.Battle.PROP_EP_MAX);
					}
					else if( fighter.getInitattrs().get(AttrType.EFFECT_POINT)+changeep < 0)
					{
						demoresult.epchange = (int) (-fighter.getInitattrs().get(AttrType.EFFECT_POINT));
						fighter.getInitattrs().put(AttrType.EFFECT_POINT, 0.0f);
					}
					else	
						fighter.getInitattrs().put(AttrType.EFFECT_POINT, fighter.getInitattrs().get(AttrType.EFFECT_POINT)+changeep);				
				}
			}
		}
	}
}
