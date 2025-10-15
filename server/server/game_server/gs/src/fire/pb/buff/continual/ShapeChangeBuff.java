package fire.pb.buff.continual;

import java.util.Map;

import xbean.Buff;
import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public class ShapeChangeBuff extends RoundBuff {

	public ShapeChangeBuff(Buff buffBean)
	{
		super(buffBean);
	}
	
	public ShapeChangeBuff(ConstantlyBuffConfig config)
	{
		super(config);
	}
	/**
	 * 战斗中使用的attach
	 */
/*	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter, FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		return super.impact(battleInfo, opfighter, aimfighter,battleskill, effects);
		DemoResult demoResult = super.impact(battleInfo, opfighter, aimfighter,battleskill, effects);
		JavaScript shapeidscript = effects.get(EffectType.SHAPE_ID);
		if (shapeidscript != null)
		{
			int shapeid = shapeidscript.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			if(shapeid!=0)
			{	
				demoResult.shapechange = shapeid;
				demoResult.targetresult |=  ResultType.RESULT_SHAPECHAGE;
			}
		}
		return demoResult;
	}*/
	
	@Override
	public Result closure(BuffAgent buffAgent)
	{
		Result result = super.closure(buffAgent);
		if(result!=null&&result.getChangedAttrs()!=null)
		{
			if(buffAgent instanceof BuffRoleImpl)
			{
				long roleId = ((BuffRoleImpl)buffAgent).getRoleId();
				int nshapeid =  fire.pb.mission.UtilHelper.getRoleShapeID(roleId);
				result.getChangedAttrs().put(FightAttrType.SHAPE_ID, (float) nshapeid);
			}
			else
				result.getChangedAttrs().put(FightAttrType.SHAPE_ID, -1.0f);
		}
		return result;
	}
}
