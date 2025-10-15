
package fire.pb.skill.particleskill;

import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.SkillRole;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.script.FightJSEngine;
import fire.script.JavaScript;





// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestParticleSkillList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请求修炼技能信息 by changhao
 *
 */

public class CRequestParticleSkillList extends __CRequestParticleSkillList__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestparticleskilllist = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{		
				SRequestParticleSkillList msg = new SRequestParticleSkillList();
				
				xbean.Properties prop = xtable.Properties.select(roleid);
				xbean.RoleClanPoint clanpoint = xtable.Roleclanpoint.select(roleid);
				int factionlevel = 0;
				
				Long factionid = xtable.Roleidclan.select(roleid);
				if (factionid != null)
				{
					xbean.ClanInfo clanInfo = xtable.Clans.select(factionid);
					if (clanInfo != null)
					{
						factionlevel = clanInfo.getClanlevel();					
					}
				}
				
				msg.factionlevel = factionlevel;
				
				int rolelevel = prop.getLevel();
				int contri = 0;
				
				if (clanpoint != null)
				{
					contri = clanpoint.getHistoryclanpoint();					
				}
			
				SkillRole skillrole = new SkillRole(roleid);
				int size = skillrole.getPracticeSkills().size();
							
				if (size == 0) //开始初始化 by changhao
				{										
					java.util.Map<Integer, fire.pb.skill.SLifeSkill> config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class);
					
					for (java.util.Map.Entry<Integer, fire.pb.skill.SLifeSkill> e1 : config.entrySet())
					{
						if (e1.getValue().skillType == 3)
						{
							xbean.PracticeSkill practiceskill = xbean.Pod.newPracticeSkill();
							practiceskill.setExp(0);
							practiceskill.setLevel(0);		
						
							skillrole.getPracticeSkills().put(e1.getKey(), practiceskill);							
						}
					}
				}
				
				for(java.util.Map.Entry<Integer, xbean.PracticeSkill> e : skillrole.getPracticeSkills().entrySet())
				{
					fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(e.getKey());

					fire.pb.skill.particleskill.ParticleSkill skill = new fire.pb.skill.particleskill.ParticleSkill();
					skill.level = e.getValue().getLevel();
					skill.id = e.getKey();
					skill.exp = e.getValue().getExp();
					//skill.maxlevel = LiveSkillManager.getInstance().GetPracticeSkillMaxLevel(rolelevel, factionlevel, contri, config.skillLevelMax);
					
					java.util.Map<Integer, Integer> ret = LiveSkillManager.getInstance().GetPracticeSkillMaxLevel(rolelevel, factionlevel, contri, config.skillLevelMax);
					if (ret.size() == 0)
					{
						return false;
					}
					
					java.util.Iterator<java.util.Map.Entry<Integer, Integer>> it = ret.entrySet().iterator();
					java.util.Map.Entry<Integer, Integer> entry = it.next();
					skill.maxlevel = entry.getValue();					
					
					if (skill.level > 0)
					{
						skill.effects.putAll(CalcSkillEffect(config.skillId, skill.level));
						skill.nexteffect.putAll(CalcSkillEffect(config.skillId, skill.level + 1));
					}
					else if (skill.level == 0)
					{
						skill.nexteffect.putAll(CalcSkillEffect(config.skillId, 1));						
					}

					msg.skilllist.add(skill);
				}
				
				msg.curcontribution = contri;
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);				
				
				return true;
			}
		};
		
		requestparticleskilllist.submit();	
	}
	
	public java.util.HashMap<Integer,Float> CalcSkillEffect(int skillid, int level)
	{
		java.util.HashMap<Integer,Float> effect = new java.util.HashMap<Integer,Float>();
		
		FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillid);
		if (sconf != null)
		{
			FightJSEngine engine = new FightJSEngine();
			engine.setSkillLevel(level);
			
			for (SubSkillConfig subskill : sconf.getSubSkills())
			{
				for (BuffUnit buffarg : subskill.getBuffUnits())
				{
					if(buffarg==null){
						continue;
					}
					for(Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
					{
						effect.put(entry.getKey(), entry.getValue().eval(engine,null,null).floatValue());
					}
				}
			}				
		}					
		
		return effect;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800503;

	public int getType() {
		return 800503;
	}


	public CRequestParticleSkillList() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestParticleSkillList) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestParticleSkillList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

