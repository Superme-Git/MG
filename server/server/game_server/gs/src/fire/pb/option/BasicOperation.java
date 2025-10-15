package fire.pb.option;

import java.util.LinkedList;

import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.SubResultItem;
import fire.pb.buff.BuffConstant;

/**操作*/
public abstract class BasicOperation {
	BasicOperation(final xbean.BattleInfo battle, final int operator, final int aim){
		this.battle = battle;
		this.operator = operator;
		this.aim = aim;
		if(operator == 0)
			this.opfighter = battle.getFieldfighter();
		else
			this.opfighter = battle.getFighterobjects().get(operator);
	}
	
	protected boolean isFailed = false;
	protected int failreason = 0;
	
	public abstract int getType();
	public abstract int getResultType();
	public abstract int getOperateID();
	public abstract boolean canChangeAim();

	public final xbean.BattleInfo getBattle(){return battle;}
	public final int getOperator(){return operator;}
	public final int getAim(){return aim;}
	public final void setAim(final int aim){this.aim = aim;}
	
	public void process(){
		battle.getRoundresult().put(getAim(), getResultType());
	}
	
	public int getHPconsume(){return 0;}
	public int getMPconsume(){return 0;}
	public int getSPconsume(){return 0;}
	
	public java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		final LinkedList<DemoResult> list = new LinkedList<DemoResult>();
		final DemoResult r = new DemoResult();
		r.targetid = getAim();
		r.targetresult = getResultType();
		list.add(r);
		return list;
	}

	public java.util.LinkedList<fire.pb.battle.SubResultItem> getSubResult(){
		final LinkedList<SubResultItem> list = new LinkedList<SubResultItem>();
		final SubResultItem r = new SubResultItem();
		r.resultlist.addAll(getDemoResult());
		list.add(r);
		return list;
	}
	
	public boolean checkOperator()
	{
		if(!battle.getFighters().containsKey(getOperator()))
			return false;
		if(BattleField.checkOutBattle(battle, operator))
			return false;
		if(opfighter == null)
			return false;
		if(!opfighter.canAction(getType(),getOperateID()))
			return false;
		
		return true;
	}
	
	protected boolean checkAndChangeAim()
	{
		if(checkAim())
			return true;
		else if(!canChangeAim())
			return false;
		setAim(0);
		return true;
	}
	
	/**
	 * 现在动作需要换目标的一般都是针对敌人的，技能物品等的换目标不在此列
	 * @return
	 */
	protected java.util.List<Integer> getAims()
	{
		return BattleField.getEnemies(battle, getOperator());
	}
	
	protected boolean checkAim()
	{
		if(! battle.getFighters().containsKey(getAim()))
			return false;
		if(BattleField.checkOutBattle(battle, getAim()))
			return false;
		Fighter fighter = battle.getFighterobjects().get(aim);
		if(fighter == null || !fighter.canBeAction(getType()))
			return false;
		if(!checkHidden())
			return false;
		return true;
	}
	
	
	public ResultItem getResultItem(){
		if(resultItem!= null)
		{
			resultItem.execute.attackerid = operator;
			resultItem.execute.hpconsume = getHPconsume();
			resultItem.execute.mpconsume = getMPconsume();
			if(resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER)
				return resultItem;
			resultItem.execute.operationtype = getType();
			resultItem.execute.operationid = getOperateID();
			processActiveHidden();
			resultItem.subresultlist = getSubResult();
		}
		return resultItem;
	}
	
	/**
	 * 检查operator和aim状态的合法性，可能会换目标
	 * @return
	 */
	public boolean checkEffective(){
		
		if(!checkOperator())
			return false;
		if(!checkAndChangeAim())
			return false;
		
		return true;
	}
	
	public final java.util.List<Integer> getProtectors(int index){
		final xbean.Protectors ps = battle.getProtectormap().get(index);
		return (null == ps) ? new java.util.LinkedList<Integer>() : ps.getProtectorlist();
	}
	
	/**
	 * 检查隐身与感知的对应关系
	 * 技能和使用物品不在此检测
	 * @return
	 */
	protected boolean checkHidden()
	{
		Fighter aimfighter = battle.getFighterobjects().get(aim);
		if(aimfighter == null)
			return false;
		if(!aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_FIRST) && !aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_SECOND)&&!aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ROLE_HIDDEN))
			return true;//目标没有隐身
		if(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_FIRST)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_SECOND)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN))
			return true;//动作者有反隐
/*		if (opfighter.getSkillAgent().hasAnySkill(SkillConstant.BATTLE_PET_DETECT_HIDDEN_FIRST,SkillConstant.BATTLE_PET_DETECT_HIDDEN_SECOND))
			return true;// 宠物怪物有反隐
*/		
		return false;
	}
	
//	public final int getAProtector(int index){
//		
//		final java.util.List<Integer> ps = getProtectors(index);
//		if(ps.size() == 0) return 0;
//		
//		
//		for(int i=0; i<ps.size(); i++){
//			final int ret = ps.get(i);
//			
//			if(! BattleField.checkLeaveBattle(getBattle(), ret)){
//				return ret;
//			}
//		}
//		
//		
//		return 0;
//	}
	

	/**
	 * 行动则驱散主动隐身，保护出隐身在保护生效的时候才解除
	 */
	protected void processActiveHidden()
	{
/*		if(resultItem != null)
		{
			if(opfighter  == null)
				return;
			if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ACTIVE_HIDDEN))
			{
				// 看这个技能是否给自己加了隐身
				for(SubResultItem subitem : resultItem.subresultlist)
				{
					for (DemoResult demo : subitem.resultlist)
					{
						if (demo.targetid == resultItem.execute.attackerid)
						{
							for(DemoBuff demobuff : demo.demobuffs)
							{
								if(demobuff.buffid == BuffConstant.CONTINUAL_ACTIVE_HIDDEN)
									return;// 如果这个技能给施法者加了隐身（例如楚楚可怜/五行遁），则不驱散隐身
							}
//							if (demo.demobuffs.containsKey(BuffConstant.CONTINUAL_ACTIVE_HIDDEN))
//								return;// 如果这个技能给施法者加了隐身（例如楚楚可怜/五行遁），则不驱散隐身
						}
					}
				}
				//否则驱散隐身
				Result result = opfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_ACTIVE_HIDDEN);
				fire.pb.buff.Module.updateResultItemFromResult(resultItem, result, opfighter);
//				if (result.isSuccess())
//					resultItem.execute.deletedbuffs.add(BuffConstant.CONTINUAL_ACTIVE_HIDDEN);
			}
		}
		
		if(resultItem != null && resultItem.execute.operationid != SkillConstant.BATTLE_ROLE_ACTIVE_HIDDEN)
		{
			//动作成功才驱散主动隐身，如果是楚楚可怜技能则不驱散隐身
			Result result = opfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_ACTIVE_HIDDEN);
			if (result.isSuccess())
				resultItem.execute.deletedbuffs.add(BuffConstant.CONTINUAL_ACTIVE_HIDDEN);
		}*/
	}
	
	public int getProtectorResult(int proIndex){
		return 0;
	}
	
	protected ResultItem resultItem = new ResultItem();
	private final xbean.BattleInfo battle;
	private final int operator;		// 动作的使用者
	private int aim;				// 动作的作用者
	protected final Fighter opfighter;
	
	
	public static final int FAIL_HP_NOT_ENOUGH = 141164;//你的生命值不足，施展招式失败。
	public static final int FAIL_MP_NOT_ENOUGH = 141165;//你的真气值不足，施展招式失败。
	public static final int FAIL_SP_NOT_ENOUGH = 141166;//你的怒气值不足，施展招式失败。
	public static final int FAIL_EP_NOT_ENOUGH = 160001;//你的效果值不足，施展招式失败。
	public static final int FAIL_HP_NOT_ENOUGH_WARRIOR = 160004;//生命不足50%，毁灭打击行动失败
	public static final int FAIL_ZITAI_XIULUO = 141167;//施展招式失败，需要在修罗姿态下才可释放。
	public static final int FAIL_ZITAI_LUOSHA = 141168;//施展招式失败，需要在罗刹姿态下才可释放。
	public static final int FAIL_ZITAI_JINGANG = 141170;//施展招式失败，需要在金刚姿态下才可释放。
	//public static final int FAIL_SKILL_MISS = 141171;//招式效果被抵抗，客户端自己提示
	public static final int FAIL_CATCH_PET = 141173;//捕捉宠物失败。
	public static final int FAIL_SUMMON_PET = 141185;//召唤宠物失败。
	public static final int FAIL_FETCH_PET = 141186;//收回宠物失败。
	
	public static final int FAIL_CANNOT_ACTION = 141178;//当前无法行动，施展招式失败。
	public static final int FAIL_ITEM_AIM = 141179;//所选目标不能被此道具作用，指向性道具
	public static final int FAIL_SKILL_AIM = 141180;//所选目标不能被此招式作用，指向性技能
	public static final int FAIL_ZITAI = 141181;//招式施展失败，没有所需要的状态
	public static final int FAIL_NO_AIM = 141182;//没有可以被作用的目标
	public static final int FAIL_ITEM_TO_DEATH = 142494;//不能对死亡的目标使用
	public static final int FAIL_PET_MP_NOT_ENOUGH = 141506;//宠物的真气值不足，施展招式失败。
	public static final int FAIL_SOMMON_NO_POS = 160600;//招图腾没位置
	public static final int FAIL_CANNOT_RELIVE_WITH_DIE_FORBID = 162003;//有死亡禁锢不能复活
}
