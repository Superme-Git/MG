package fire.pb.buff;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fire.log.Logger;
import fire.pb.attr.AttrType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoAttr;
import fire.pb.battle.DemoBuff;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.single.SingleBuff;
import fire.pb.buff.single.SingleBuffConfig;
import fire.pb.fushi.DayPayManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.skill.Result;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/**
 * 
 *
 */
public class Module implements ModuleInterface
{

	
//	private static Module instance;
	
	public static final String MODULE_NAME = "buff";
	
	java.util.Map<Integer, Class<? extends ConstantlyBuff>> cid2class;
	java.util.Map<Integer, Class<? extends SingleBuff>> iid2class;
	java.util.Map<Integer, ConstantlyBuffConfig> defaultCBuffConfigs = new HashMap<Integer, ConstantlyBuffConfig>();
	java.util.Map<Integer, SingleBuffConfig> defaultIBuffConfigs = new HashMap<Integer, SingleBuffConfig>();
	Map<Integer, BuffClashs> buffClashsMap = new HashMap<Integer, BuffClashs>();
	
	private List<Integer> nullbuffIds = new ArrayList<Integer>();
	private List<Integer> decisionbuffIds = new ArrayList<Integer>();
	private Map<Integer,List<Integer>> limitedBattleOperations = new HashMap<Integer, List<Integer>>();
	public static Class<?> ROUND_BUFF_CLASS;
	
	public static Module getInstance()
	{
		//不再用以上的单例模式，从ModuleManager获得，由其管理
		return ((fire.pb.buff.Module)ModuleManager.getInstance().getModuleByName(MODULE_NAME));
	}

	static public final Logger logger = Logger.getLogger("BATTLE");


	@Override
	public void exit()
	{

	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws Exception
	{
		logger.info("BUFF模块初始化开始");
		ROUND_BUFF_CLASS = Class.forName("fire.pb.buff.continual.RoundBuff");
		// 持续性buff Config初始化
		cid2class = new java.util.HashMap<Integer, Class<? extends ConstantlyBuff>>();
		cid2class.clear();
		defaultCBuffConfigs.clear();
		nullbuffIds.clear();
		decisionbuffIds.clear();
		initLimitedBattleOperation();
		for (fire.pb.buff.SCBuffConfig buffconfig : fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.buff.SCBuffConfig.class).values())
		{
			String c = buffconfig.classname;
			if (c == null || c.isEmpty())
				c = "fire.pb.buff.continual.NullBuff";
			try
			{
				if (c == "fire.pb.buff.continual.NullBuff")
				{
					nullbuffIds.add(buffconfig.getId());
				} else if (c == "fire.pb.buff.continual.DecisionBuff")
				{
					decisionbuffIds.add(buffconfig.getId());
				}

				Class<?> cls = Class.forName(c);
				cid2class.put(buffconfig.id, (Class<? extends ConstantlyBuff>) cls);
			} catch (ClassNotFoundException ex)
			{
				logger.error("载入持续性BUFF " + buffconfig.id + "失败，找不到类" + c);
				continue;
			}

			// 初始化defaultCBuffConfigs
			try
			{
				ConstantlyBuffConfig cbuffConfig = new ConstantlyBuffConfig(buffconfig);
				for (int operationtype : cbuffConfig.getLimitedBattleOperations())
				{
					List<Integer> buffIds = limitedBattleOperations.get(operationtype);
					if (buffIds == null)
						continue;
					buffIds.add(cbuffConfig.getBuffTypeId());
				}
				defaultCBuffConfigs.put(cbuffConfig.getBuffTypeId(), cbuffConfig);
			} catch (Exception ex)
			{
				logger.error("初始化ConstantlyBuffConfig" + buffconfig.id + "失败，持续性buff表错误。", ex);
				continue;
			}
		}

		// 一次性buff Config初始化
		iid2class = new java.util.HashMap<Integer, Class<? extends SingleBuff>>();
		iid2class.clear();
		defaultIBuffConfigs.clear();
		for (fire.pb.buff.SIBuffConfig buffconfig : fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.buff.SIBuffConfig.class).values())
		{
			String c = buffconfig.classname;
			if (c == null || c.isEmpty())
				c = "fire.pb.buff.continual.NullBuff";
			try
			{
				Class<?> cls = Class.forName(c);
				iid2class.put(buffconfig.id, (Class<? extends SingleBuff>) cls);
			} catch (ClassNotFoundException ex)
			{
				logger.error("载入一次性BUFF " + buffconfig.id + "失败，找不到类" + c);
				continue;
			}
			// 初始化defaultIBuffConfigs
			try
			{
				SingleBuffConfig ibuffConfig = new SingleBuffConfig(buffconfig);
				defaultIBuffConfigs.put(ibuffConfig.getBuffTypeId(), ibuffConfig);
			} catch (Exception e)
			{
				logger.error("Initial SingleBuff Error, SingleBuff Id = " + buffconfig.getId());
			}

		}
		buffClashsMap.clear();
		// buff冲突的初始化
		for (fire.pb.buff.SBuffClashs sbuffclashs : fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.buff.SBuffClashs.class).values())
		{
			BuffClashs buffclashs = new BuffClashs(sbuffclashs.getId(), sbuffclashs.getName());
			try
			{
				if (sbuffclashs.getClashbuffs() != null && !sbuffclashs.getClashbuffs().equals(""))
				{
					String[] clashs = sbuffclashs.getClashbuffs().split(";");
					for (String clash : clashs)
					{
						ConstantlyBuffConfig cbuffcfg = defaultCBuffConfigs.get(Float.valueOf(clash).intValue());
						if(cbuffcfg == null)
						{//这里有可能是一次性buff
							buffclashs.getClashBuffs().add(Float.valueOf(clash).intValue());
						}
						else
						{							
							buffclashs.addClashBuff(cbuffcfg);
						}
					}
				}
				if (sbuffclashs.getOverridebuffs() != null && !sbuffclashs.getOverridebuffs().equals(""))
				{
					String[] overrides = sbuffclashs.getOverridebuffs().split(";");
					for (String override : overrides)
						buffclashs.getOverrideBuffs().add(Float.valueOf(override).intValue());
				}
				if (sbuffclashs.getInvalidbuffs() != null && !sbuffclashs.getInvalidbuffs().equals(""))
				{
					String[] invalids = sbuffclashs.getInvalidbuffs().split(";");
					for (String invalid : invalids)
						buffclashs.getInvalidBuffs().add(Float.valueOf(invalid).intValue());
				}
				if (sbuffclashs.getTips() != null && !sbuffclashs.getTips().equals(""))
				{
					String[] tips = sbuffclashs.getTips().split(";");
					for (String tip : tips)
					{
						String[] strs = tip.split("=");
						buffclashs.getClashMsgIds().put(Float.valueOf(strs[0]).intValue(), Float.valueOf(strs[1]).intValue());
					}
				}
				if (sbuffclashs.getClashmapid() != null && !sbuffclashs.getClashmapid().equals(""))
				{
					String[] mapids = sbuffclashs.getClashmapid().split(";");
					for (String mapid : mapids)
						buffclashs.getClashMapIds().add(Float.valueOf(mapid).intValue());
				}
			} catch (Exception e)
			{
				logger.error("Initial Buff Clashs Error, Buff Id = " + sbuffclashs.getId());
				e.printStackTrace();
			}
			
			buffClashsMap.put(buffclashs.getBuffId(), buffclashs);
		}

		logger.info("BUFF模块初始化完成");

	}

	private void initLimitedBattleOperation()
	{
		limitedBattleOperations.clear();
		Field[] fields = OperationType.class.getFields();
		for (int i = 0; i < fields.length; i++)
		{
			try
			{
				limitedBattleOperations.put(fields[i].getInt(null),new ArrayList<Integer>());
			} catch (IllegalArgumentException e)
			{
				logger.error("初始化limitedBattleOperations错误",e);
			} catch (IllegalAccessException e)
			{
				logger.error("初始化limitedBattleOperations错误",e);
			}
		}
	}
	
	/**
	 * 只是用buffId构造，取表内的默认参数
	 * 根据buffID 来决定构造的是一次性还是持续性Buff
	 * @param buffId
	 * @return IBuff
	 */
	public IBuff createBuff(int buffId)
	{
		if(buffId/10000 == 51)
			return createInstantBuff(buffId);
		else
			return createConstantlyBuff(buffId);
	}

	public SingleBuff createInstantBuff(SingleBuffConfig buffConfig)
	{
		Class<? extends SingleBuff> buffClass = iid2class.get(buffConfig.getBuffTypeId());
		if (buffClass == null)
			return null;
		try
		{
			return buffClass.getConstructor(SingleBuffConfig.class).newInstance(buffConfig);
		} catch (NoSuchMethodException ex)
		{
			logger.error(buffClass.getCanonicalName() + "缺少BuffConfig的构造函数");
			return null;
		} catch (java.lang.reflect.InvocationTargetException ex)
		{
			logger.error(buffClass.getCanonicalName() + "输入参数错误，错误的buff id为" + buffConfig.getBuffTypeId());
			return null;
		} catch (IllegalAccessException ex)
		{
			return null;
		} catch (InstantiationException ex)
		{
			return null;
		}
	}
	
	
	public ConstantlyBuff createConstantlyBuff(ConstantlyBuffConfig buffConfig)
	{
		Class<? extends ConstantlyBuff> buffClass = cid2class.get(buffConfig.getBuffTypeId());
		//ConstantlyBuffConfig scconf = getConstantlyBuffConfig(buffConfig.getBuffTypeId());
		//buffConfig.setOriginBuffConfig(scconf);
		if (buffClass == null)
			return null;
		try
		{
			return buffClass.getConstructor(ConstantlyBuffConfig.class).newInstance(buffConfig);
		} catch (NoSuchMethodException ex)
		{
			logger.error(buffClass.getCanonicalName() + "缺少BuffConfig的构造函数");
			return null;
		} catch (java.lang.reflect.InvocationTargetException ex)
		{
			logger.error(buffClass.getCanonicalName() + "输入参数错误，错误的buff id为" + buffConfig.getBuffTypeId());
			return null;
		} catch (IllegalAccessException ex)
		{
			return null;
		} catch (InstantiationException ex)
		{
			return null;
		}
	}
	public SingleBuff createInstantBuff(int buffTypeId)
	{
		SingleBuffConfig buffConfig = getInstance().getDefaultIBuffConfig(buffTypeId);
		Class<? extends SingleBuff> buffClass = iid2class.get(buffTypeId);
		if (buffClass == null)
			return null;
		try
		{
			return buffClass.getConstructor(SingleBuffConfig.class).newInstance(buffConfig);
		} catch (NoSuchMethodException ex)
		{
			logger.error(buffClass.getCanonicalName() + "缺少BuffConfig的构造函数");
			return null;
		} catch (java.lang.reflect.InvocationTargetException ex)
		{
			logger.error(buffClass.getCanonicalName() + "输入参数错误，错误的buff id为" + buffConfig.getBuffTypeId());
			return null;
		} catch (IllegalAccessException ex)
		{
			return null;
		} catch (InstantiationException ex)
		{
			return null;
		}
	}
	
	/**
	 * 根据buffId生成buff，buff配置取buff表里填的默认的持续性buff配置
	 * 
	 * @param buffTypeId
	 * @return ConstantlyBuffConfig，不存在该buff配置时返回null
	 */
	public ConstantlyBuff createConstantlyBuff(int buffTypeId)
	{
		ConstantlyBuffConfig buffConfig = getInstance().getDefaultCBuffConfig(buffTypeId);
		if(buffConfig == null)
			return null;
		Class<? extends ConstantlyBuff> buffClass = cid2class.get(buffTypeId);
		if (buffClass == null)
			return null;
		try
		{
			return buffClass.getConstructor(ConstantlyBuffConfig.class).newInstance(buffConfig);
		} catch (NoSuchMethodException ex)
		{
			logger.error(buffClass.getCanonicalName() + "缺少BuffConfig的构造函数");
			return null;
		} catch (java.lang.reflect.InvocationTargetException ex)
		{
			logger.error(buffClass.getCanonicalName() + "输入参数错误，错误的buff id为" + buffConfig.getBuffTypeId());
			return null;
		} catch (IllegalAccessException ex)
		{
			return null;
		} catch (InstantiationException ex)
		{
			return null;
		}
	}
	
	/**
	 * 根据buffId和buffbean生成buff，用于身上的已有的buff
	 * 
	 * @param buffTypeId
	 * @return ConstantlyBuffConfig，不存在该buff配置时返回null
	 */
	public ConstantlyBuff createConstantlyBuff(xbean.Buff buffbean)
	{
		//ConstantlyBuffConfig buffConfig = getInstance().getDefaultCBuffConfig(buffId);
		int buffId = buffbean.getIndex();
		Class<? extends ConstantlyBuff> buffClass = cid2class.get(buffId);
		if (buffClass == null)
			return null;
		try
		{
			return buffClass.getConstructor(xbean.Buff.class).newInstance(buffbean);
		} catch (NoSuchMethodException ex)
		{
			logger.error(buffClass.getCanonicalName() + "缺少BuffConfig的构造函数");
			return null;
		} catch (java.lang.reflect.InvocationTargetException ex)
		{
			logger.error(buffClass.getCanonicalName() + "输入参数错误，错误的buff id为" + buffId);
			return null;
		} catch (IllegalAccessException ex)
		{
			return null;
		} catch (InstantiationException ex)
		{
			return null;
		}
	}
	
	/**
	 * 根据buffTypeId获取buff表里填的默认的持续性buff配置
	 * 
	 * @param buffTypeId
	 * @return ConstantlyBuffConfig，不存在该buff配置时返回null
	 */
	public ConstantlyBuffConfig getDefaultCBuffConfig(int buffId)
	{
		return defaultCBuffConfigs.get(buffId);
	}
	
	/**
	 * 根据buffTypeId获取buff表里填的默认的一次性buff配置
	 * 
	 * @param buffTypeId
	 * @return SingleBuffConfig，不存在该buff配置时返回null
	 */
	public SingleBuffConfig getDefaultIBuffConfig(int buffId)
	{
		return defaultIBuffConfigs.get(buffId);
	}
	
	/**
	 * 战斗中使用，从Result转化为战斗脚本DemoResult
	 * @param demoResult
	 * @param aimfighter
	 * @param result
	 */
	public static void updateDemoResultFromResult(DemoResult demoresult,Result result,Fighter fighter)
	{
		if(result != null && result.isSuccess())
		{
			for (Integer buffId : result.getAddedBuffMap().keySet())
			{
				ConstantlyBuffConfig conf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
				if (conf.isSnedWithScript())
				{
					fire.pb.buff.Buff buff = result.getAddedBuffMap().get(buffId);
					demoresult.demobuffs.add(new DemoBuff(fighter.getFighterId(), buffId, buff.count));
				}
			}
			for (Integer buffId : result.getDeletedBuffs())
			{
				ConstantlyBuffConfig conf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
				if (conf.isSnedWithScript())
				{
					demoresult.demobuffs.add(new DemoBuff(fighter.getFighterId(), buffId, -1));
				}
			}
			//移除已经随战斗脚本发送的buff
			for(DemoBuff demobuff : demoresult.demobuffs)
			{
				result.getAddedBuffMap().remove(demobuff.buffid);
				result.getDeletedBuffs().remove((Integer)demobuff.buffid);
			}
			//FIXME 删除这些属性不用再放到fighter.updateRoundBuffResult中去了
			int[] attrids = new int[]{AttrType.HP,AttrType.MP,AttrType.SP,AttrType.UP_LIMITED_HP};
			for(int attrid : attrids)
			{
				Float value = result.getChangedAttrs().get(attrid);
				if(value != null)
					demoresult.demoattrs.add(new DemoAttr(fighter.getFighterId(), attrid, value.intValue()));
			}
			
			Float value = result.getChangedAttrs().get(FightAttrType.SHAPE_ID);
			if(value != null)
			{
				demoresult.shapechange = value.intValue();
				demoresult.targetresult |=  ResultType.RESULT_SHAPECHAGE;
			}
			//demoresult.demoattrs.add(new DemoAttr(fighter.getFighterId(), FightAttrType.SHAPE_ID, value.intValue()));			
			fighter.updateRoundBuffResult(result);
			//这个是为啥改的已经找不到了，但是加这个会照成其他的bug！
			//demoresult.targetid = fighter.getFighterId(); 
		}
	}
	
	/**
	 * 当通过战斗计算出来的ResultItem无法发给客户端时，更新为Result
	 * @param result
	 * @param item
	 */
	public static Map<Integer,Result> getResultsFromItem(ResultItem item)
	{
		Map<Integer,Result> results = new HashMap<Integer, Result>();
		if(item == null)
			return results;
		if (item.execute.attackerid > 0 || !(item.execute.operationtype == OperationType.ACTION_FAILURE||item.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER))
		{
			if (item.execute.hpconsume != 0 || item.execute.mpconsume != 0 || item.execute.spconsume != 0)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("getResultsFromItem时，Attacker有HpMpSp变化：Operationid = " + item.execute.operationid);}
			}
			updateResultBuff(results, item.execute.demobuffs);
		}
		updateResultBuff(results, item.execute.demobuffs);
		
		for(SubResultItem subresult : item.subresultlist)
		{
			for(DemoResult demoresult : subresult.resultlist)
			{
				if(demoresult.targetid <= 0)
					continue;
				if(demoresult.targetresult !=0 || demoresult.attackerresult != 0 || demoresult.assisterid != 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("getResultsFromItem时，有result变化：Operationid = " + item.execute.operationid);}
				}
				updateResultBuff(results, demoresult.demobuffs);
			}
		}
		return results;
	}
	
	private static void updateResultBuff(Map<Integer,Result> results, List<fire.pb.battle.DemoBuff> demobuffs)
	{
		for(fire.pb.battle.DemoBuff demobuff : demobuffs)
		{
			Result result = results.get(demobuff.fighterid);
			if(result == null)
			{
				result = new Result(true);
				results.put(demobuff.fighterid, result);
			}
			if(demobuff.round == -1)
				result.getDeletedBuffs().add(demobuff.buffid);
			else
			{
				fire.pb.buff.Buff prtlbuff = new fire.pb.buff.Buff();
				prtlbuff.count = demobuff.round;
				result.getAddedBuffMap().put(demobuff.buffid,prtlbuff);
			}
		}
	}
	
	/**
	 * 战斗中使用，为
	 * @param demoResult
	 * @param aimfighter
	 * @param result
	 */
	public static void updateResultItemFromResult(ResultItem item,Result result,Fighter fighter)
	{
		if(result != null && result.isSuccess())
		{
			for (Integer buffId : result.getAddedBuffMap().keySet())
			{
				ConstantlyBuffConfig conf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
				if (conf.isSnedWithScript())
				{
					fire.pb.buff.Buff buff = result.getAddedBuffMap().get(buffId);
					item.execute.demobuffs.add(new DemoBuff(fighter.getFighterId(), buffId, buff.count));
				}
			}
			for (Integer buffId : result.getDeletedBuffs())
			{
				ConstantlyBuffConfig conf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
				if (conf.isSnedWithScript())
					item.execute.demobuffs.add(new DemoBuff(fighter.getFighterId(), buffId, -1));
					//item.execute.deletedbuffs.add(buffId);
			}
			//移除已经随战斗脚本发送的buff
			for(DemoBuff demobuff : item.execute.demobuffs)
			{
				result.getAddedBuffMap().remove(demobuff.buffid);
				result.getDeletedBuffs().remove((Integer)demobuff.buffid);
			}
			fighter.updateRoundBuffResult(result);
		}
	}
	
	/**
	 * 把旧的合到新的去
	 * @param olditem
	 * @param newitem
	 * @return
	 */
	public static ResultItem updateResultItemFromAnother(ResultItem olditem, ResultItem newitem)
	{
		if(newitem == null )
		{
			if (olditem != null)
				return olditem;
			else
				return null;
		}
		else
		{
/* lxh与15-6-11修改前代码
			if (olditem == null)
				return newitem;
			// 将old detach的结果加入到新的里面
			for (DemoResult demo : olditem.resultlist)
			{
				if (demo.targetresult != 0)
				{// 有其他变化（血蓝）
					newitem.resultlist.add(demo);
					continue;
				}
				// 无其他变化，只有buff变化
				if (demo.demobuffs.size() != 0)
				{
					for (DemoResult existDemo : newitem.resultlist)
					{
						// 找到前面已经存在的同一目标的demo
						if (existDemo.targetid == demo.targetid)
						{
							existDemo.demobuffs.addAll(demo.demobuffs);
							demo = null;
							break;
						}
					}
					if (demo != null)
						newitem.resultlist.add(demo);
				}
			}
			return newitem;
 */	
//////////////////////////////////////////////////////////////////////////////////////////
//lxh与15-6-11修改后代码			
			System.out.println("函数 updateResultItemFromAnother 没查到调用的地方，");
			System.out.println("函数 updateResultItemFromAnother 没查到调用的地方。");
			return newitem;

/*			if (olditem == null)
				return newitem;
			// 将old detach的结果加入到新的里面
			for (SubResultItem subitem : olditem.subresultlist)
			{
				for (DemoResult demo : subitem.resultlist)
				{
					if (demo.targetresult != 0)
					{// 有其他变化（血蓝）
						subitem.resultlist.add(demo);
						continue;
					}
					// 无其他变化，只有buff变化
					if (demo.demobuffs.size() != 0)
					{
						for (SubResultItem existsubitem : newitem.subresultlist)
						{
							for (DemoResult existDemo : existsubitem.resultlist)
							{
								// 找到前面已经存在的同一目标的demo
								if (existDemo.targetid == demo.targetid)
								{
									existDemo.demobuffs.addAll(demo.demobuffs);
									demo = null;
									break;
								}
							}
						}
						if (demo != null)
							subitem.resultlist.add(demo);
					}
				}
			}
			return newitem;
*/////////////////////////////////////////////////////////////////////////////////////////			
		}
	}
	
	/**
	 * 把newresult里面的内容，更新到oldresult中去
	 * @param oldresult
	 * @param newresult
	 * @return DemoResult
	 */
	public static DemoResult updateDemoResult(DemoResult oldresult, DemoResult newresult)
	{
		if(newresult.hpchange != 0)
			oldresult.hpchange += newresult.hpchange;
		if(newresult.mpchange != 0)
			oldresult.mpchange += newresult.mpchange;
		if(newresult.epchange != 0)
			oldresult.epchange += newresult.epchange;
		if(newresult.shapechange != 0)
			oldresult.shapechange += newresult.shapechange;		
		if(newresult.uplimithpchange != 0)
			oldresult.uplimithpchange += newresult.uplimithpchange;
		if(newresult.targetresult != 0)
			oldresult.targetresult |= newresult.targetresult;
		if(newresult.returnhurt != 0)
			oldresult.returnhurt += newresult.returnhurt;
		if(newresult.attackback != 0)
			oldresult.attackback += newresult.attackback;
		if(newresult.stealhp != 0)
			oldresult.stealhp += newresult.stealhp;
		if(newresult.attackerresult != 0)
			oldresult.attackerresult |= newresult.attackerresult;
		if(newresult.attackerulhpchange != 0)
			oldresult.attackerulhpchange += newresult.attackerulhpchange;
		if(newresult.protecterulhpchange != 0)
			oldresult.protecterulhpchange += newresult.protecterulhpchange;
		if(newresult.protecterid != 0)
			oldresult.protecterid = newresult.protecterid;
		if(newresult.protecterhpchange != 0)
			oldresult.protecterhpchange += newresult.protecterhpchange;
		if(newresult.protecterresult != 0)
			oldresult.protecterresult |= newresult.protecterresult;
		if(newresult.assisterid != 0)
			oldresult.assisterid = newresult.assisterid;
		if(newresult.demobuffs.size() != 0)
			oldresult.demobuffs.addAll(newresult.demobuffs);
		if(newresult.demoattrs.size() != 0)
			oldresult.demoattrs.addAll(newresult.demoattrs);
		return oldresult;
	}
	
	/**
	 * 获取buff冲突列表
	 * @param buffId
	 * @return
	 */
	public BuffClashs getBuffClashs(int buffId)
	{
		return buffClashsMap.get(buffId);
	}
	
	
	public static boolean existState(final long roleId , final int buffId)
	{
		switch(buffId)
		{
		case BuffConstant.StateType.STATE_BATTLE_FIGHTER:
			if(xtable.Roleid2battleid.select(roleId) !=null)
				return true;
			else
				return false;
		case BuffConstant.StateType.STATE_BATTLE_WATCHER:
			if(xtable.Watcherid2battleid.select(roleId) !=null)
				return true;
			else
			    return false;
		case BuffConstant.StateType.STATE_TEAM:
			return TeamManager.isInTeam(roleId);
			
		case BuffConstant.StateType.STATE_REPLAY:
			BuffAgent hostAgent = new BuffRoleImpl(roleId, true);
			return hostAgent.existBuffBean(BuffConstant.StateType.STATE_REPLAY);
			//existBuff(BuffConstant.StateType.STATE_REPLAY);
		case BuffConstant.StateType.STATE_TEAM_LEADER:
			Team team = TeamManager.selectTeamByRoleId(roleId);
			if(team == null)
				return false;
			return team.isTeamLeader(roleId);
		case BuffConstant.StateType.STATE_TEAM_MEMBER:
			Team team2 = TeamManager.selectTeamByRoleId(roleId);
			if(team2 == null)
				return false;
			return team2.isTeamMember(roleId);
		case BuffConstant.StateType.STATE_TEAM_MEMBER_ABSENT:
			Team team3 = TeamManager.selectTeamByRoleId(roleId);
			if(team3 == null)
				return false;
			return team3.isAbsentMember(roleId);
		case BuffConstant.StateType.STATE_TEAM_MEMBER_NORMAL:
			Team team4 = TeamManager.selectTeamByRoleId(roleId);
			if(team4 == null)
				return false;
			return team4.isNormalMember(roleId);
		case BuffConstant.StateType.STATE_TEAM_MEMBER_OFFLINE:
			Team team5 = TeamManager.selectTeamByRoleId(roleId);
			if(team5 == null)
				return false;
			return team5.isOfflineMember(roleId);
		case BuffConstant.StateType.STATE_TEAM_MEMBER_RETURN:
			Team team6 = TeamManager.selectTeamByRoleId(roleId);
			if(team6 == null)
				return false;
			return team6.isReturnMember(roleId);
//		case BuffConstant.StateType.STATE_MINI_GAME:
//			return fire.pb.minigame.BaseGame.isPlayinggame( roleId );
		
		case BuffConstant.StateType.STATE_ROLL:
			Long battlemelonid = xtable.Roleid2battlemelonid.select(roleId);
			return battlemelonid != null ? true : false;
		case BuffConstant.StateType.STATE_INSTANCE_ZONE: {
			Role memberRole = RoleManager.getInstance().getRoleByID(roleId);
			if (fire.pb.mission.instance.InstanceManager.getInstance().isInstanceCopy(memberRole.getMapId())) {
				return true;
			}
			if (fire.pb.mission.instance.line.LineInstManager.getInstance().isInstanceCopy(memberRole.getMapId())) {
				return true;
			}
			
			if (fire.pb.instancezone.bingfeng.BingFengLandMgr.bingFengMaps.contains(memberRole.getMapId())) {
				return true;
			}
			return false;
		}
		case BuffConstant.StateType.STATE_NO_DAY_PAY:
			
			if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
			{
				boolean ok = DayPayManager.getInstance().CheckDayPayWithLevel(roleId);
				return !ok;				
			}
			else
			{
				return false;
			}
			
		case BuffConstant.StateType.STATE_IN_CLAN_FIGHT_BATTLE_FIELD: //是否在公会战场中 by changhao
		{
			Long clanfightid = xtable.Roleid2clanfightid.select(roleId);
			if (clanfightid != null)
			{
				return true;
			}
			
			return false;
			
		}
			
			
			
		default:
			return false;
		}
	}
	
	/**
	 * 为动作和添加buff时检查buff冲突
	 * @param roleId
	 * @param buffOpId buffId/状态ID/操作ID
	 * @param readonly 如果已锁过rolelock，则用false;如果不想上锁用true
	 * @return  true = 没有冲突，可以加载 ；false = 有冲突，不合法
	 */
	public static boolean checkBuffClashAndSendMsg(long roleId , int buffOpId , boolean readonly)
	{
		int clashId = checkBuffClashWithId(roleId, buffOpId, readonly);
		if(clashId == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 为动作和添加buff时检查buff冲突
	 * @param roleId
	 * @param buffOpId buffId/状态ID/操作ID
	 * @return 返回0则没有冲突，其他为冲突的ID（1个，并未检测全部）
	 */
	public static int checkBuffClashWithId(long roleId , int buffOpId , boolean readonly)
	{
		BuffAgent buffagent = new BuffRoleImpl(roleId,readonly);
		
		return buffagent.checkBuffClash(buffOpId);
	}
	
	
	public List<Integer> getStateBuffIds()
	{
		return nullbuffIds;
	}
	
	
	public List<Integer> getDecisionBuffIds()
	{
		return decisionbuffIds;
	}
	
	public List<Integer> getLimitBuffIdsByOperType(int opeartionType)
	{
		return limitedBattleOperations.get(opeartionType);
	}
	
	/**
	 * 是否是持续性buff
	 * @param buffId
	 * @return true持续性buff；false一次性buff
	 */
	public static boolean isConstantlyBuff(int buffId)
	{
		if(buffId/10000 == 50)
			return true;
		else
			return false;
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		ReloadResult result = fire.pb.effect.Module.getInstance().reload();
		if(!result.isSuccess())
			return result.appendMsg("buff module reload failed /n");
		try
		{
			Module m = new Module();
			m.init();
			ModuleManager.getInstance().putModuleByName(MODULE_NAME,m);
			return result;
		}
		catch(Exception e)
		{
			logger.error(e);
			return result.appendMsg("buff module reload failed /n");
		}
		
	}
	
}
