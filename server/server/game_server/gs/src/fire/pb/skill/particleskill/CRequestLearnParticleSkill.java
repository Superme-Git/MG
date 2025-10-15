
package fire.pb.skill.particleskill;

import java.util.HashMap;
import java.util.Map;

import fire.log.beans.RoleSkillUpBean;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.BuffPetImpl;
import fire.pb.course.CourseType;
import fire.pb.item.GroceryItemShuXing;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Module;
import fire.pb.skill.Result;
import fire.pb.skill.SkillPet;
import fire.pb.skill.SkillRole;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.talk.MessageMgr;
import fire.script.FightJSEngine;
import fire.script.JavaScript;









// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestLearnParticleSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 学习修炼技能
 * @author changhao
 *
 */
public class CRequestLearnParticleSkill extends __CRequestLearnParticleSkill__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestlearnparticleskill = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				Long battleid = xtable.Roleid2battleid.select(roleid);
				if (battleid != null)
				{
					MessageMgr.psendMsgNotify(roleid, 131451, null);	
					return true;						
				}
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
				
				fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(id);
				
				if (config == null)
				{
					return true;						
				}
												
				SkillRole skillrole = new SkillRole(roleid);
				xbean.PracticeSkill practiceskill = skillrole.getPracticeSkill(id);
				if (practiceskill == null)
				{
					return true;
				}
				
				int curskilllevel = practiceskill.getLevel(); //需要学习到的等级 by changhao
				
				if (curskilllevel >= config.skillLevelMax) //是否已经达到最大等级 by changhao
				{
					MessageMgr.psendMsgNotify(roleid, 160107, null);	
					return true;						
				}
				
				xbean.Properties roleproperty = xtable.Properties.select(roleid);
				
				xbean.Properties prop = xtable.Properties.select(roleid);
				xbean.RoleClanPoint contribution = xtable.Roleclanpoint.select(roleid);
				
				
			
				int rolelevel = prop.getLevel();
				int contri = 0;
				if (contribution != null)
				{
					contri = contribution.getHistoryclanpoint();
				}

				//得到能学的最大等级 by changhao
				int maxlevel = 0;
						
				java.util.Map<Integer, Integer> ret = LiveSkillManager.getInstance().GetPracticeSkillMaxLevel(rolelevel, factionlevel, contri, config.skillLevelMax);
				if (ret.size() == 0)
				{
					return false;
				}
				
				java.util.Iterator<java.util.Map.Entry<Integer, Integer>> it = ret.entrySet().iterator();
				java.util.Map.Entry<Integer, Integer> entry = it.next();
				maxlevel = entry.getValue();
								
				if (curskilllevel >= maxlevel)
				{
					int msgid = 160106;
					if (entry.getKey() == 1)
					{
						msgid = 160487;
					}
					else if (entry.getKey() == 2)
					{
						msgid = 160488;
					}
					else if (entry.getKey() == 3)
					{
						msgid = 160489;
					}
					
					MessageMgr.psendMsgNotify(roleid, msgid, null);	
					return true;
				}
				
				if (config.needGuild == 1 && roleproperty.getClankey() <= 0) //是否需要公会 by changhao
				{
					return true;	
				}
				
				int addexp = 0;
				
				Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(
						roleid, fire.pb.item.BagTypes.BAG, false);
											
				int which = LiveSkillManager.getInstance().GetPracticleSkillPlayerOrPet(id); //得到这个修炼技能对应的对象 by changhao
			
				if (itemid != 0) //消耗道具升级 by changhao
				{
					GroceryItemShuXing grocery = (GroceryItemShuXing) fire.pb.item.Module.getInstance().getItemManager().getAttr(itemid);
					if (grocery == null)
					{
						return true;
					}
										
					//角色 by changhao
					if (grocery.typeid == LiveSkillManager.ITEM_TYPE_PRACTICE_ROLE_ITEM && which == 2)
					{
						//成功消耗道具 by changhao
						if(bag.removeItemById(itemid, times, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuixiulian, 0, LiveSkillManager.Practice) != times)
						{
							MessageMgr.psendMsgNotify(roleid, 160105, null);
							return false;
						}
						
						fire.pb.skill.SPracticeItemExp itemexp = ConfigManager.getInstance().getConf(fire.pb.skill.SPracticeItemExp.class).get(itemid);
						addexp = itemexp.exp * times;
						
					}//宠物 by changhao
					else if (grocery.typeid == LiveSkillManager.ITEM_TYPE_PRACTICE_PET_ITEM && which == 1)
					{
						//成功消耗道具 by changhao
						if(bag.removeItemById(itemid, times, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuixiulian, 0, LiveSkillManager.Practice) != times)
						{
							MessageMgr.psendMsgNotify(roleid, 160104, null);
							return false;
						}
						
						fire.pb.skill.SPracticeItemExp itemexp = ConfigManager.getInstance().getConf(fire.pb.skill.SPracticeItemExp.class).get(itemid);
						addexp = itemexp.exp * times;
					}
					else
					{
						return true;
					}
				}
				else //花钱升级 by changhao
				{
					fire.pb.common.SCommon costconfig = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(165);
					
					int costmoney = Integer.parseInt(costconfig.value) * times;
									
					//扣钱 by changhao
					if(bag.subMoney(-costmoney, LiveSkillManager.Practice, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuixiulian, 0) != -costmoney)
					{
						return false;	
					}					
					
					addexp = 10 * times;
				}
				
				AddExp(curskilllevel, practiceskill.getExp() + addexp, practiceskill, maxlevel); //升级 by changhao
				
				SUpdateLearnParticleSkill msg = new SUpdateLearnParticleSkill();
				msg.skill.level = practiceskill.getLevel();
				msg.skill.id = id;
				msg.skill.exp = practiceskill.getExp();
				msg.skill.maxlevel = maxlevel;
				
				if (msg.skill.level > 0)
				{
					msg.skill.effects.putAll(CalcSkillEffect(config.skillId, msg.skill.level));
					msg.skill.nexteffect.putAll(CalcSkillEffect(config.skillId, msg.skill.level + 1));
					if (practiceskill.getLevel() > curskilllevel) //升级刷新属性 by changhao
					{
						Result result = new Result(true);
						
						//刷新BUFF by changhao
						Module.logger.info("玩家:" + roleid + "修炼专精技能后加载PracticleSkill技能");
						if(which == 2)
							result.updateResult(skillrole.addPracticleSkillBuff());
						else if(which == 1)
						{
							PetColumn petColumn = new PetColumn(roleid, PetColumnTypes.PET, false);
							for(Pet pet : petColumn.getPets())
							{
								SkillPet spet = new SkillPet(pet.getPetInfo(), roleid);
								Result resultpet = spet.addSkillBuffWhileOnline(null);
								BuffPetImpl buffpet = new BuffPetImpl(roleid, pet.getPetInfo().getKey());
								buffpet.psendSBuffChangeResult(resultpet);
								spet.updateSkillBuffWhileOut(null);
							}							
						}
					
						if(!result.getChangedAttrs().isEmpty())//通知属性改动 by changhao
						{
							mkdb.Procedure.psendWhileCommit(roleid,new SRefreshRoleData((HashMap<Integer, Float>)result.getChangedAttrs()));
						}		
						fire.log.YYLogger.roleSkillUPLog(roleid, new RoleSkillUpBean(id, practiceskill.getLevel()));
					}					
				}
				else if (msg.skill.level == 0)
				{
					msg.skill.nexteffect.putAll(CalcSkillEffect(config.skillId, 1));
				}
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);				
				mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRoleZongheRankProc(roleid));
				
				if (practiceskill.getLevel() > curskilllevel) {
					fire.pb.course.CourseManager.checkAchieveCourse(roleid, CourseType.XIULIAN_COURSE, practiceskill.getLevel());
				}
				
				return true;
			}
		};
		
		requestlearnparticleskill.submit();	
	}
	
	/***
	 * 修炼等级加经验 by changhao
	 * @param requirelevel
	 * @param curexp
	 * @param skill
	 */
	public void AddExp(int curskilllevel, int curexp, xbean.PracticeSkill skill, int curmaxlevel)
	{
		int maxexp = LiveSkillManager.getInstance().GetPracticleSkillNeedExp(id, curskilllevel); //得到升这级需要的经验 by changhao
		int exp = curexp;
		while (exp >= maxexp)
		{
			exp -= maxexp;
			
			curskilllevel ++;
			maxexp = LiveSkillManager.getInstance().GetPracticleSkillNeedExp(id, curskilllevel);
			if (maxexp == -1) //升级到最大了 by changhao
			{
				skill.setLevel(curskilllevel);
				skill.setExp(0);
				return;
			}
		}
		
		if (curskilllevel > curmaxlevel)
		{
			curskilllevel = curmaxlevel;
		}
		
		skill.setLevel(curskilllevel);
		skill.setExp(exp);
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
						if(buffarg==null)
						continue;
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
	public static final int PROTOCOL_TYPE = 800505;

	public int getType() {
		return 800505;
	}

	public int id; // 技能ID by changhao
	public int times; // 学习次数 by changhao
	public int itemid; // 使用的道具 by changhao

	public CRequestLearnParticleSkill() {
	}

	public CRequestLearnParticleSkill(int _id_, int _times_, int _itemid_) {
		this.id = _id_;
		this.times = _times_;
		this.itemid = _itemid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(id);
		_os_.marshal(times);
		_os_.marshal(itemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestLearnParticleSkill) {
			CRequestLearnParticleSkill _o_ = (CRequestLearnParticleSkill)_o1_;
			if (id != _o_.id) return false;
			if (times != _o_.times) return false;
			if (itemid != _o_.itemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += times;
		_h_ += itemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(times).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestLearnParticleSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = times - _o_.times;
		if (0 != _c_) return _c_;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

