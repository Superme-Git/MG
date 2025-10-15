package fire.pb.battle.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import fire.pb.battle.BattleField;
import fire.pb.battle.BattleSequenceHelper;
import fire.pb.battle.BattleType;
import fire.pb.battle.DecisionProcesser;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.FormationConfig;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.option.BasicOperation;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Result;
import fire.pb.skill.SkillAgent;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.fight.FightSkillConfig;
import fire.script.JavaScript;
/**战斗动作*/
public abstract class FighterOperate extends Operate
{
	private final Fighter fighter;
	public FighterOperate(Fighter fighter)
	{
		this.fighter = fighter;
	}

	
	public Fighter getFighter()
	{
		return fighter;
	}
	
	public abstract BasicOperation getBasicOperation(ListIterator<Operate> lit);
	
	public ResultItem process(ListIterator<Operate> lit)
	{
		BasicOperation basicOperation = getBasicOperation(lit);
		if(basicOperation == null)
			return null;
		BattleField.logger.debug(new StringBuilder("施法者Id: ").append(basicOperation.getOperator()).append("; 承受者ID：").append(basicOperation.getAim()).append("; 释放动作类型：").append(basicOperation.getType()).append("; 释放动作id：").append(basicOperation.getOperateID()));
		Fighter fighteropt = getBattle().getFighterobjects().get(basicOperation.getOperator());
		if(fighteropt != null && fighteropt.getFighterBean().getRound() == 0 && fighteropt.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			
			if(!(basicOperation.getType() == OperationType.ACTION_SKILL&&((basicOperation.getOperateID() == SkillConstant.BATTLE_ROLE_BEHUNTER||
					basicOperation.getOperateID() == SkillConstant.BATTLE_HUOBAN_BEHUNTER_1||
					basicOperation.getOperateID() == SkillConstant.BATTLE_HUOBAN_BEHUNTER_2||
					basicOperation.getOperateID() == SkillConstant.BATTLE_HUOBAN_BEHUNTER_3||
					basicOperation.getOperateID() == SkillConstant.BATTLE_HUOBAN_BEHUNTER_4||
					basicOperation.getOperateID() == SkillConstant.BATTLE_PET_HIDDEN_FIRST||
					basicOperation.getOperateID() == SkillConstant.BATTLE_PET_HIDDEN_SECOND))))
				return null;
		}
		
		// 如果是防御或者保护动作，则将行动前的AI动作放到前一个出手的后面或者后一个出手的前面
		if (basicOperation.getType() == OperationType.ACTION_DEFEND || basicOperation.getType() == OperationType.ACTION_PROTECT 
				|| basicOperation.getType() == OperationType.ACTION_BATTLE_END )
		{
			BattleSequenceHelper.processAIActionsForDefendAndProtect(getScript(), getBeforeAIActions());
		}

		if (!basicOperation.checkEffective()&&(!(basicOperation.getType() == OperationType.ACTION_REST ||basicOperation.getType() == OperationType.ACTION_DEFEND 
												|| basicOperation.getType() == OperationType.ACTION_PROTECT|| basicOperation.getType() == OperationType.ACTION_BATTLE_END )))
		{
			ResultItem item = new ResultItem();
			item.execute.attackerid = basicOperation.getOperator();
			item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
			item.execute.msgid = BasicOperation.FAIL_CANNOT_ACTION;
			if(basicOperation.getType() == OperationType.ACTION_CATHCH && getBattle().getBattletype() == BattleType.BATTLE_LINE)
				item.execute.msgid = 166131;
			SubResultItem subitem = new SubResultItem();
			subitem.subskillid = 0;
			subitem.subskillstarttime = 0;
			item.subresultlist.add(subitem);
			BattleField.logger.debug(new StringBuilder("动作无法释放!"));
			return item;
		}
		
		basicOperation.process();

		ResultItem item = basicOperation.getResultItem();
		if(item!=null && item.newfighter!=null && basicOperation.getResultType()==ResultType.RESULT_SUMMONPET)
		{
			for(fire.pb.battle.FighterInfo newfighter: item.newfighter)
			{
				//新加入的moster添加行动。 // 这段在前。后面添加的会加到这段之前
				Fighter fighter = getBattle().getFighterobjects().get(newfighter.index);
				{
					getOriginSequence().add(fighter.getFighterBean());
					int nskill = 0;
					if(fighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_ROLE_BEHUNTER))
						nskill = SkillConstant.BATTLE_ROLE_BEHUNTER;
					if(fighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_1))
						nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_1;
					else if(fighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_2))
						nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_2;
					else if(fighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_3))
						nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_3;
					else if(fighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_4))
						nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_4;

					if(nskill != 0&&getBattle().getRound()>0)
					{
						List<Operate> extActs = new ArrayList<Operate>();
						xbean.Decision decision = xbean.Pod.newDecision();
						
						decision.setAim(fighter.getFighterId());
						decision.setOperateid(nskill);
						decision.setOperatetype(OperationType.ACTION_SKILL);
						decision.setOperator(fighter.getFighterId());		
						
						extActs.add(new DoubleSkillOperate(fighter, decision));
						BattleSequenceHelper.insertExtraActs(getBattle(), lit, fighter, extActs);
					}					
					if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PASSIVE_HIDDEN_FIRST )&&getBattle().getRound()>0)
					{
						List<Operate> extActs = new ArrayList<Operate>();
						xbean.Decision decision = xbean.Pod.newDecision();
						decision.setAim(fighter.getFighterId());
						decision.setOperateid(SkillConstant.BATTLE_PET_HIDDEN_FIRST);
						decision.setOperatetype(OperationType.ACTION_SKILL);
						decision.setOperator(fighter.getFighterId());		
						
						extActs.add(new DoubleSkillOperate(fighter, decision));
						BattleSequenceHelper.insertExtraActs(getBattle(), lit, fighter, extActs);
					}
					else if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PASSIVE_HIDDEN_SECOND )&&getBattle().getRound()>0)
					{
						List<Operate> extActs = new ArrayList<Operate>();
						xbean.Decision decision = xbean.Pod.newDecision();

						decision.setAim(fighter.getFighterId());
						decision.setOperateid(SkillConstant.BATTLE_PET_HIDDEN_SECOND);
						decision.setOperatetype(OperationType.ACTION_SKILL);
						decision.setOperator(fighter.getFighterId());		
						
						extActs.add(new DoubleSkillOperate(fighter, decision));
						BattleSequenceHelper.insertExtraActs(getBattle(), lit, fighter, extActs);
					}					
				}	
				//&& fighter.getFightertype() !=  xbean.Fighter.FIGHTER_PET
				if(basicOperation.getType() == OperationType.ACTION_SUMMON_INSTANT&&getBattle().getRound()>0)
				{
					List<Operate> extActs = new ArrayList<Operate>();
					xbean.Decision decision = BattleAIManager.getInstance().getDecision(getBattle(), newfighter.index);				
					if (decision == null)
						decision = DecisionProcesser.getMonsterRandomDecision(getBattle(), fighter);				
					extActs.add(new DecisionOperate( fighter,decision));
					BattleSequenceHelper.insertExtraActs(getBattle(), lit ,fighter, extActs);
				}	
				
				int addhalobuffcount = 0;
				DemoResult addhalobuffdemoresult = new DemoResult();
				//如果有光环技能
				SkillAgent skillagent = fighter.getSkillAgent();
				if(skillagent != null)
				{
					for(int skillId : skillagent.getAllBattleSkills())
					{
						FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
						if(sconf == null)
						{
							if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("怪物 "+fighter.getFighterBean().getFightername()+" FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
							continue;
						}
						if(!sconf.isActiveSkill() && (sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO||sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2) 
								&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
						{//光环技能，且有buff(一定是ConstantlyBuff)
							getBattle().getEngine().setSkillLevel(skillagent.getLevel(skillId));
							BuffUnit buffUnit = sconf.getSubSkills()[0].getBuffUnits()[0];
							if(buffUnit==null) continue;
							ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffUnit.buffIndex);

							int nround = buffUnit.roundJavascript.eval(getBattle().getEngine(),fighter,null).intValue();
							if(nround != 0 )
								buff.setRound(nround);//被动战斗技能buff只有回合，时间和量不设置

							//	
							buff.getBuffBean().setFighterkey(fighter.getFighterId());
							if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO)
							{
								for (int friend : BattleField.getFriends(getBattle(), fighter.getFighterId()))
								{
									if(!BattleField.validateaim(getBattle(), sconf.getSubSkills()[0], newfighter.index, friend))
										continue;									
									Fighter f = getBattle().getFighterobjects().get(friend);
									if (f != null)
									{
										// 2.1.4 复制buff
										ConstantlyBuff buffcopy = buff.copy();
										for(Map.Entry<Integer, JavaScript> entry : buffUnit.effectJavascriptMap.entrySet())
										{//buff效果
											buffcopy.getEffects().put(entry.getKey(), entry.getValue().eval(getBattle().getEngine(),fighter,f).floatValue());
										}										
										Result result = new Result(true);										
										result.updateResult(f.getBuffAgent().addCBuff(buffcopy));
										if(result.isSuccess())
										{
											fire.pb.buff.Module.updateDemoResultFromResult(addhalobuffdemoresult, result, f);
											addhalobuffcount ++;
										}
										//f.getBuffAgent().psendSBuffChangeResult(result);
									}
								}
							}
							else if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2)
							{
								List<Integer> poss = fire.pb.battle.Module.getInstance().getEffectPoss(
										FormationConfig.GetPosbyFighteid(fighter.getFighterId()<15? getBattle().getHostform():getBattle().getGuestform(),fighter.getFighterId()));
								poss = FormationConfig.GetPosbyFighteids(fighter.getFighterId()<15? getBattle().getHostform():getBattle().getGuestform(),poss,fighter.getFighterId()<15);
								if(poss != null)
								{
									poss.add(fighter.getFighterId());
									for (int friend : poss)
									{
										if(!BattleField.validateaim(getBattle(), sconf.getSubSkills()[0], newfighter.index, friend))
											continue;									
										Fighter f = getBattle().getFighterobjects().get(friend);
										if (f != null)
										{
											// 2.1.4 复制buff
											ConstantlyBuff buffcopy = buff.copy();
											for(Map.Entry<Integer, JavaScript> entry : buffUnit.effectJavascriptMap.entrySet())
											{//buff效果
												buffcopy.getEffects().put(entry.getKey(), entry.getValue().eval(getBattle().getEngine(),fighter,f).floatValue());
											}										
											Result result = new Result(true);
											result.updateResult(f.getBuffAgent().addCBuff(buffcopy));
											if(result.isSuccess())
											{
												fire.pb.buff.Module.updateDemoResultFromResult(addhalobuffdemoresult, result, f);
												addhalobuffcount ++;
											}
										}
									}
								}
							}
						}				
					}
				}
				if(addhalobuffcount > 0)
				{
					SubResultItem lastsubitem = item.subresultlist.get(item.subresultlist.size()-1);
					lastsubitem.resultlist.add(addhalobuffdemoresult);
				}

				//加载伙伴的光环技能
				Result result2 = new Result(true);
				for (int friend : BattleField.getFriends(getBattle(), newfighter.index))
				{
					Fighter f = getBattle().getFighterobjects().get(friend);
					if(f == null)
						continue;
					SkillAgent skillagent1 = f.getSkillAgent();
					if(skillagent1 != null)
					{
						for(int skillId : skillagent1.getAllBattleSkills())
						{
							FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
							if(sconf == null)
							{
								if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("怪物 "+f.getFighterBean().getFightername()+" FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
								continue;
							}
							if(!sconf.isActiveSkill() && (sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO||sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2) 
									&& sconf.getSubSkills()[0]!=null 
									&& sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
							{//光环技能，且有buff(一定是ConstantlyBuff)
								getBattle().getEngine().setSkillLevel(skillagent1.getLevel(skillId));
								
								BuffUnit buffUnit = sconf.getSubSkills()[0].getBuffUnits()[0];
								if(buffUnit==null) continue;
								ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffUnit.buffIndex);
								if(f.getBuffAgent().existBuff(buffUnit.buffIndex))	
								{
									buff.setRound(f.getBuffAgent().getBuffBean(buffUnit.buffIndex).getRound());//被动战斗技能buff只有回合，时间和量不设置
								}
								else
								{
									int nround = buffUnit.roundJavascript.eval(getBattle().getEngine(),f,null).intValue();
									if(nround != 0 )
										buff.setRound(nround);//被动战斗技能buff只有回合，时间和量不设置
								}
								for(Map.Entry<Integer, JavaScript> entry : buffUnit.effectJavascriptMap.entrySet())
								{//buff效果
									buff.getEffects().put(entry.getKey(), entry.getValue().eval(getBattle().getEngine(),f,fighter).floatValue());
								}
								//	
								if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO)
								{
									if(!BattleField.validateaim(getBattle(), sconf.getSubSkills()[0], friend, newfighter.index))
										continue;									
									buff.getBuffBean().setFighterkey(f.getFighterId());								
									result2.updateResult(fighter.getBuffAgent().addCBuff(buff));
								}
								else if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2)
								{
									List<Integer> poss = fire.pb.battle.Module.getInstance().getEffectPoss(
											FormationConfig.GetPosbyFighteid(friend<15? getBattle().getHostform():getBattle().getGuestform(),friend));
									poss = FormationConfig.GetPosbyFighteids(friend<15? getBattle().getHostform():getBattle().getGuestform(),poss,friend<15);
//									List<Integer> poss = fire.pb.battle.Module.getInstance().getEffectPoss(f.getFighterId());
									if(poss != null)
									{
										if(poss.contains(fighter.getFighterId()))
										{
											if(!BattleField.validateaim(getBattle(), sconf.getSubSkills()[0], friend, newfighter.index))
												continue;
											buff.getBuffBean().setFighterkey(f.getFighterId());								
											result2.updateResult(fighter.getBuffAgent().addCBuff(buff));
										}
									}
								}
							}				
						}
					}
				}
				if(result2.isSuccess())
				{
					DemoResult addhalobuffdemoresult2 = new DemoResult();
					fire.pb.buff.Module.updateDemoResultFromResult(addhalobuffdemoresult2, result2, fighter);
					SubResultItem lastsubitem = item.subresultlist.get(item.subresultlist.size()-1);
					lastsubitem.resultlist.add(addhalobuffdemoresult2);
				}
//				fighter.getBuffAgent().psendSBuffChangeResult(result2);
			}			
		}
		BattleField.logger.debug(new StringBuilder("施法者Id: ").append(basicOperation.getOperator()).append("; 释放动作类型：").append(basicOperation.getType()).append("; 释放动作id：").append(basicOperation.getOperateID()).append("结束！"));
		return item;
	}
	
}
