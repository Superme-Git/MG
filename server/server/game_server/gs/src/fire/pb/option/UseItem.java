package fire.pb.option;

import java.util.LinkedList;
import java.util.List;

import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.SubResultItem;
import fire.pb.item.BagTypes;
import fire.pb.item.FoodItem;
import fire.pb.item.IDurableItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.SItemBuff;
import fire.pb.skill.fight.FightUseItem;

/**使用道具*/
public class UseItem extends BasicOperation {
	
	public static final int ITEM_USER_ROLE = 1;
	public static final int ITEM_USER_PET = 2;
	
	public static final int ITEM_USE_NO_CONSUME = 0;//物品使用后不消耗
	public static final int ITEM_USE_CONSUME = 1;//物品使用后消耗
	public static final int ITEM_USE_CONSUME_ENDU = 2;//物品使用后消耗耐久
	
	public UseItem(final xbean.BattleInfo battle, final int operator, final int aim, final int itemKey){
		super(battle, operator, aim);
		this.itemKey = itemKey;
	}
	@Override
	public int getOperateID() {return itemKey;}

	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString(){return new StringBuffer().append(getOperator()).append("\t使用物品（itemkey = \t" +itemKey +")给\t").append(getAim()).toString();}
	@Override
	public ResultItem getResultItem(){
		if(resultItem != null)
		{
			if(resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER)
				return resultItem;
			processActiveHidden();
		}
		return resultItem;
	}

	@Override
	public boolean checkEffective()
	{
		return true;
	}
	
	@Override
	public int getResultType() {return 0;}

	@Override
	public int getType() {return OperationType.ACTION_USEITEM;}
	
	@Override
	public void process(){

		if(!checkOperator()){
			resultItem.execute.attackerid = getOperator();
			resultItem.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
			SubResultItem subitem = new SubResultItem();
			subitem.subskillid = 0;
			subitem.subskillstarttime = 0;
			resultItem.subresultlist.add(subitem);
			return ;
		}
		
		resultItem = null;
		Fighter opfighter = getBattle().getFighterobjects().get(getOperator());
		//Fighter aimfighter = getBattle().getFighterobjects().get(getAim());
		int usertype = 0;
		long roleId = 0;
		xbean.Fighter owner = null;
		if(opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			roleId = opfighter.getFighterBean().getUniqueid();//角色使用物品
			usertype = 1;
			owner = opfighter.getFighterBean();
		}
		else if(opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET)
		{//宠物使用物品
			int masterfid = getOperator() - 5;
			xbean.Fighter masterfighter = getBattle().getFighters().get(masterfid);
			if(masterfighter!=null)
				roleId = masterfighter.getUniqueid();
			usertype = 2;
			owner = masterfighter;
		}
		if(roleId <= 0)
			return;
		int aimRelation = BattleField.getItemAimRelation(getBattle(), getOperator(), getAim());
		try
		{
			ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
			ItemBase item = bag.getItem(itemKey);
			if (item == null)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用物品：找不到物品。RoleId：").append(roleId).append(" ;ItemKey: ").append(itemKey));}
				return;
			}
			
			if(!checkRoleLevel(roleId, item))
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用物品：使用者等级不符。RoleId：").append(roleId).append(" ;ItemId: ").append(item.getItemId()));}
				return;
			}
			
			if (opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				if ((item.getItemAttr().getInbattleuseto() & ITEM_USER_ROLE) == 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用物品：使用者不能是角色。RoleId：").append(roleId).append(" ;ItemId: ").append(item.getItemId()));}
					return;
				}
			} else if (opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET)
			{
				if ((item.getItemAttr().getInbattleuseto() & ITEM_USER_PET) == 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用物品：使用者不能是宠物。RoleId：").append(roleId).append(" ;ItemId: ").append(item.getItemId()));}
					return;
				}
			}

			if (!item.onUseInBattle(usertype, aimRelation))
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用物品：物品使用对象不合法。RoleId：").append(roleId).append(" ;ItemId: ").append(item.getItemId()));}
				return;
			}
//			SubSkillConfig[] subSkills = null;
			List<Integer> monsterIds = new LinkedList<Integer>();
/*			if (item.getItemid() == MING_JING_CAO_ITEM_ID)
				buffArgs = genMingJingCaoBuffArg();// 明镜草buff特殊处理
			else
			{*/
			SItemBuff sItemBuff = fire.pb.main.ConfigManager.getInstance().getConf(SItemBuff.class).get(item.getItemId());
			if (sItemBuff == null)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用物品：物品buff不存在。RoleId：").append(roleId).append(" ;ItemId: ").append(item.getItemId()));}
//				subSkills = new SubSkillConfig[4];
				return;
			}
			else
			{
				if (sItemBuff.monsterids != null)
					monsterIds.addAll(sItemBuff.monsterids);
//				subSkills = fire.pb.item.Module.parseItemSubSkills(sItemBuff);
			}
			//记录道具类型为药材/食品药品时
//			xbean.Fighter owner = getBattle().getFighters().get(roleId);
			
			Integer usednum = owner.getUseditems().get(item.getItemId());
			int itemtype = item.getItemAttr().getTypeid();
			int drugStuffTime = 0;
			int drugFoodTime = 0;
			
			if(itemtype ==  xbean.Fighter.DrugStuffID||itemtype ==  xbean.Fighter.DrugStuffID_LV2)
			{
				drugStuffTime = owner.getUseditems().get(0);
				if(drugStuffTime <= 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用道具类型为").append(itemtype).append("的物品超出"));}
					return;
				}	
			}
			else if(itemtype ==  xbean.Fighter.FoodID ||itemtype ==  xbean.Fighter.DrugID)
			{
				drugFoodTime = owner.getUseditems().get(1);
				if(drugFoodTime <= 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("ERROR 战斗内使用道具类型为").append(itemtype).append("的物品超出"));}
					return;
				}					
			}
			
			//}
/*			if (subSkills == null)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("ERROR 战斗内使用物品：物品buff解析错误。RoleId：" + roleId + " ;ItemId: " + item.getItemid());
				return;
			}
*/			// 设置物品品质
			if (item instanceof FoodItem)
				getBattle().getEngine().setItemQuality(((FoodItem) item).getQuality());
//			else if (item instanceof Level3Item)
//				getBattle().getEngine().setItemQuality(((Level3Item) item).getQuality());

			// 战斗内使用物品的技能流程
			FightUseItem skill = new FightUseItem(getBattle(), getOperator(), getAim(), sItemBuff.inskill_id, OperationType.ACTION_USEITEM, item.getItemAttr()
					.getInbattle(), item, monsterIds);
			resultItem = skill.process();

			if (resultItem != null && resultItem.execute.operationtype == 0)
				resultItem.execute.operationtype = getType();

			if (resultItem != null && !(resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER))
			{// 使用成功
				resultItem.execute.operationid = item.getItemId();
				// 使用成功，消耗物品 else
				 if (item.getItemAttr().getUseup() == ITEM_USE_CONSUME_ENDU)
				{
					if (item instanceof IDurableItem)
					{
						((IDurableItem) item).addDurabilityWithSP(-1);
					}
				} else if (item.getItemAttr().getUseup() == ITEM_USE_CONSUME)
					bag.removeItemWithKey(itemKey, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_itemuse, 0, "战斗使用物品");

				if (usednum == null)
					usednum = 0;
				usednum++;
				owner.getUseditems().put(item.getItemId(), usednum);				
				if(itemtype ==  xbean.Fighter.DrugStuffID||itemtype ==  xbean.Fighter.DrugStuffID_LV2)
				{
					drugStuffTime--;
					owner.getUseditems().put(0, drugStuffTime);
				}
				else if(itemtype ==  xbean.Fighter.FoodID ||itemtype ==  xbean.Fighter.DrugID)
				{
					drugFoodTime--;
					owner.getUseditems().put(1, drugFoodTime);
				}
				
				Fighter ownerfighter = getBattle().getFighterobjects().get((opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET)?getOperator() - 5:getOperator());
				ownerfighter.sendAlreadyUsedItems();
				processItemPoison(resultItem, item);//处理物品中毒
				//处理历程
				fire.pb.course.CourseManager.achieveCourse(ownerfighter.getFighterBean().getUniqueid(), fire.pb.course.CourseType.USE_YAOPIN_COURSE, item.getItemId(), 0);
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(getOperator() + "\t成功对\t" + getAim() + "\t使用物品\t" + item.getItemAttr().getName());}
			}
			else if(resultItem == null)
			{
				resultItem = new ResultItem();
				resultItem.execute.attackerid = getOperator();
				resultItem.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
				resultItem.execute.operationid = BasicOperation.FAIL_ITEM_AIM;
			}
			if(resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER)
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(getOperator() + "\t对\t" + getAim() + "\t使用物品\t" + item.getItemAttr().getName() + "\t失败");}

		} catch (Exception e)
		{
			BattleField.logger.error("战斗内使用物品出错", e);
		}
		getBattle().getEngine().setItemQuality(0);// 重置品质
	}
	
	private void processItemPoison(ResultItem resultItem, ItemBase item) {
	}
	
	private boolean checkRoleLevel(long roleId, ItemBase item)
	{
		int rolelevel = xtable.Properties.selectLevel(roleId);
		if (rolelevel < item.getItemAttr().getNeedlevel())
			return false;
		
		return true;
	}
	
	
	@Override
	public boolean canChangeAim(){
		return false;
	}
	
	private final int itemKey;
	
}
