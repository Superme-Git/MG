package fire.pb.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.WorldEventTab;
import fire.pb.attr.AttrType;
import fire.pb.attr.ScoreType;
import fire.pb.blackmarket.srv.BlackMarket;
import fire.pb.common.SCommon;
import fire.pb.effect.SEquipAbilityScore;
import fire.pb.event.DeleteItemEvent;
import fire.pb.item.equip.diamond.EquipDiamondMgr;
import fire.pb.item.make.ItemMakeUtil;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.shop.srv.market.Market;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import mkdb.Procedure;

public class Module implements ModuleInterface {
	static public final Logger logger = Logger.getLogger("ITEM");

	public static Map<Integer, ResolveItemData> resolveItemData = new HashMap<Integer, ResolveItemData>();
	
	public static Map<Integer, List<Integer>> itemSchoolData = new HashMap<Integer, List<Integer>>();

	public static Module getInstance() {
		return (Module) fire.pb.main.ModuleManager.getInstance().getModuleByName("item");
	}

	static Properties property = ConfigManager.getInstance().getPropConf("item");

	public final static BuffUnit GetItemBuff(int skill_id) throws Exception {
		if (skill_id < 1)
			return null;
		FightSkillConfig skillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(skill_id);
		if (skillConfig == null || skillConfig.getSubSkills().length < 1
				|| skillConfig.getSubSkills()[0].getBuffUnits() == null
				|| skillConfig.getSubSkills()[0].getBuffUnits().length < 1)
			return null;
		return skillConfig.getSubSkills()[0].getBuffUnits()[0];
	}

	public static List<BuffUnit> getItemBuffs(int skill_id) throws Exception {
		if (skill_id < 1)
			return null;
		List<BuffUnit> tmpBuffUnits = new LinkedList<BuffUnit>();
		FightSkillConfig skillConfig = fire.pb.skill.Module.getInstance()
				.getFightSkillConfig(skill_id);
		if (skillConfig != null && skillConfig.getSubSkills().length > 0) {
			for (SubSkillConfig subskill : skillConfig.getSubSkills()) {
				if (subskill.getBuffUnits() != null
						&& subskill.getBuffUnits().length > 0) {
					for (BuffUnit buff : subskill.getBuffUnits()) {
						if (buff != null && buff.buffIndex > 0)
							tmpBuffUnits.add(buff);
					}
				}
			}
			if (tmpBuffUnits.size() > 0)
				return tmpBuffUnits;
		}
		return null;
	}
	RecycleItemThread clearitemthread = new RecycleItemThread();
	private UniqueIdFactory idCreater = null;
	private ItemMgr itemManager = null;

	ItemIdToClassString classTypeLibrary = null;

	public int addItemToPack(final long roleId, final int itemId,
			final int number, final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype) {
		if (mkdb.Transaction.current() == null) {
			return 0;
		}
		logger.trace("Module.java: addItemToPack id=" + itemId + ","
				+ number + "个 " + reason);
		return new Pack(roleId, false).doAddItem(itemId, number, reason,
				countertype, 0);
	}

	public int addItemToTemp(final long roleId, final int itemid,
			final int number, final String reason) {
		if (mkdb.Transaction.current() == null) {
			return 0;
		}
		logger.trace("Module.java: addItemToTemp id=" + itemid + ","
				+ number + "个 " + reason);
		return new Temp(roleId, false).doAddItem(itemid, number, reason, fire.log.enums.YYLoggerTuJingEnum.GENERAL, 0);
	}

	private int getBasicScore(EquipItem item) {
		int baseAttrValue = 0;
		Double score = 0.0;

		Iterator<Entry<Integer, Integer>> attr = item.getEquipAttr().getAttr().entrySet().iterator();
		while (attr.hasNext()) {
			Entry<Integer, Integer> current = attr.next();
			int id = current.getKey() - current.getKey() % 10;
			switch (id) {
			case Commontext.CONS:
			case Commontext.IQ:
			case Commontext.STR:
			case Commontext.ENDU:
			case Commontext.AGI:
				baseAttrValue += current.getValue();
				break;
			case AttrType.MAX_HP:
			case AttrType.MAX_MP:
			case AttrType.ATTACK:
			case AttrType.DEFEND:
			case AttrType.MAGIC_ATTACK:
			case AttrType.MAGIC_DEF:
			case AttrType.SPEED:
			case AttrType.MEDICAL:
			case AttrType.SEAL:
			case AttrType.UNSEAL: {
				SEquipAbilityScore AbilityScore = fire.pb.scoremanager.Module
						.getInstance().getEquipAbility(id);
				if (AbilityScore == null)
					return 0;

				int pos = item.getEquipPos();
				switch (pos) {
				case EquipItemType.ARMS: {
					score += current.getValue() * AbilityScore.coefficient
							/ AbilityScore.weapon;
					break;
				}
				case EquipItemType.LORICAE: {
					score += current.getValue() * AbilityScore.coefficient
							/ AbilityScore.armor;
					break;
				}
				case EquipItemType.TIRE: {
					score += current.getValue() * AbilityScore.coefficient
							/ AbilityScore.headdress;
					break;
				}
				case EquipItemType.ADORN: {
					score += current.getValue() * AbilityScore.coefficient
							/ AbilityScore.accessory;
					break;
				}
				case EquipItemType.WAISTBAND: {
					score += current.getValue() * AbilityScore.coefficient
							/ AbilityScore.belt;
					break;
				}
				case EquipItemType.BOOT: {
					score += current.getValue() * AbilityScore.coefficient
							/ AbilityScore.boot;
					break;
				}
				}
				break;
			}
			default:
				break;
			}
		}

		Iterator<Entry<Integer, Integer>> addAttr = item.getEquipAttr()
				.getAddattr().entrySet().iterator();
		while (addAttr.hasNext()) {
			Entry<Integer, Integer> current = addAttr.next();
			int id = current.getKey() - current.getKey() % 10;
			switch (id) {
			case Commontext.CONS:
			case Commontext.IQ:
			case Commontext.STR:
			case Commontext.ENDU:
			case Commontext.AGI:
				baseAttrValue += current.getValue();
				break;
			default:
				break;
			}
		}

		Double dScore = fire.pb.scoremanager.Module.getInstance().getCoe(
				ScoreType.EQUIP_LEVEL_SCORE)
				* baseAttrValue;
		score += dScore;
		return score.intValue();
	}

	public int getEquipScore(ItemBase ib) {
		int score = 0;
		if (ib instanceof EquipItem) {
			EquipItem item = (EquipItem) ib;

			score += getBasicScore(item);
			score += getSkillScore(item);
			score += getDiamondScore(item);
		}
		return score;
	}

	public int getEquipScoreAll(long roleId, ItemBase ib) {
		int score = 0;
		if (ib instanceof EquipItem) {
			EquipItem ei = (EquipItem) ib;

			score = Module.getInstance().getEquipScore(ib);
			if (score != ei.getEquipAttr().getEquipscore()) {
				Procedure.pexecute(new PupdateEquipScore(roleId, ei.getKey(),
						score));
			}
		}
		return score;
	}

	private int getDiamondScore(EquipItem item) {
		Double baseScore = 0.0;
		int diamondsNum = item.getEquipAttr().getDiamonds().size();
		if (diamondsNum > 0) {
			for (Integer gemID : item.getEquipAttr().getDiamonds()) {
				int gemLevel = EquipDiamondMgr.getDiamondPropMap().get(gemID).level;
				Double xs = fire.pb.scoremanager.Module.getInstance().getCoe(ScoreType.EQUIP_GEM);
				baseScore += gemLevel * xs;
			}
		}

		return baseScore.intValue();
	}

	private int getSkillScore(EquipItem item) {
		Double baseScore = 0.0;
		int nSkillId = item.getEquipAttr().getSkill();
		int nEffectId = item.getEquipAttr().getEffect();
		int nEquipLevel = item.getEquipAttr().getEquiplevel();

		if (nSkillId != 0) {
			Double xs = fire.pb.scoremanager.Module.getInstance().getCoe(
					ScoreType.EQUIP_SKILL);
			baseScore += xs * nEquipLevel;
		}

		if (nEffectId != 0) {
			Double xs = fire.pb.scoremanager.Module.getInstance().getCoe(
					ScoreType.EQUIP_EFFCT);
			baseScore += xs * nEquipLevel;
		}

		return baseScore.intValue();
	}

	@Override
	public void exit() {
	}

	public int getEquipTotalScore(final long roleId) {
		Equip equip = new Equip(roleId, true);
		int totalScore = 0;

		for (ItemBase ib : equip) {
			if (ib instanceof EquipItem) {
				int score = getEquipScoreAll(roleId, ib);
				totalScore += score;
			}
		}

		return totalScore;
	}

	public ItemMaps getItemMaps(final long roleId, final int packid,
			final boolean readonly) {
		switch (packid) {
		case BagTypes.BAG:
			return new Pack(roleId, readonly);
		case BagTypes.DEPOT:
			return new Depot(roleId, readonly);
		case BagTypes.EQUIP:
			return new Equip(roleId, readonly);
		case BagTypes.TEMP:
			return new Temp(roleId, readonly);
		case BagTypes.QUEST:
			return new Pack(roleId, readonly).new QuestItemColumn();
		case BagTypes.MARKET:
			return new Market(roleId, readonly);
		case BagTypes.BLACKMARKET:
			return new BlackMarket(roleId, readonly);
		}
		return null;
	}

	public ItemMgr getItemManager() {
		return itemManager;
	}

	public UniqueIdFactory getItemUniqueIdFactory() {
		return idCreater;
	}

	public Logger getLogger() {
		return logger;
	}

	public int getMaxOwn(final long roleId, final int itemid) {
		ItemShuXing attr = itemManager.getAttr(itemid);
		if (attr == null)
			return 0;
		return attr.maxown;
	}

	public long getMoney(final long roleId) {
		return new Pack(roleId, true).getMoney();
	}

	public Properties getProp() {
		return property;
	}

	@Override
	public void init() throws Exception {
		logger.info("物品模块开始初始化");
		final fire.pb.main.ConfigManager cm = fire.pb.main.ConfigManager.getInstance();
		classTypeLibrary = new ItemIdToClassString(cm);
		itemManager = new ItemMgrImp(cm);
		final javax.management.MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
		server.registerMBean(itemManager, new javax.management.ObjectName("bean:type=item,name=itemManager"));
		idCreater = new ItemUniqueIdFactory();
		WorldEventTab.getInstance().add(clearitemthread, "30 3 * * *");

		Map<Integer, EquipItemShuXing> equipItemAttrConfig = null;
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){
			java.util.Map<Integer, DEquipItemShuXing> dEquipItemShuXing = ConfigManager.getInstance().getConf(DEquipItemShuXing.class);
			equipItemAttrConfig = new java.util.TreeMap<>(dEquipItemShuXing);
		}else {
			equipItemAttrConfig = ConfigManager.getInstance().getConf(EquipItemShuXing.class);
		}
		
		for (EquipItemShuXing equipItemAttr : equipItemAttrConfig.values()) {
			ResolveItemData resolveData = new ResolveItemData(equipItemAttr);
			resolveItemData.put(equipItemAttr.getId(), resolveData);
			
			String[] strSchool = equipItemAttr.getNeedSchool().split(";");
			List<Integer> schoolLst = new ArrayList<Integer>();
			
			for (String sss : strSchool) {
				schoolLst.add(Integer.parseInt(sss));
			}
			
			itemSchoolData.put(equipItemAttr.getId(), schoolLst);
		}
		
		/*try {
			new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					xtable.Timeoutitem.getTable().browse(
						new mkdb.TTable.IWalk<Integer, xbean.TimeoutItemInfo>() {
							@Override
							public boolean onRecord(Integer k, xbean.TimeoutItemInfo v) {
								//遍历表中的需要建立定时器的物品
								return true;
							}
							
						});
					return true;
				}
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		logger.info("物品模块初始化完成");
		DeleteItemEvent.init();
		ItemMakeUtil.Init();
	}

	public boolean marshalData(OctetsStream os, Object tipsdata) {
		if (tipsdata instanceof Octets) {
			os.marshal((Octets) tipsdata);
		} else if (tipsdata instanceof mkdb.Bean) {
			os.marshal((mkdb.Bean) tipsdata);
		} else if (tipsdata instanceof String) {
			os.marshal((String) tipsdata);
		} else if (tipsdata instanceof Integer) {
			os.marshal((Integer) tipsdata);
		} else if (tipsdata instanceof Long) {
			os.marshal((Long) tipsdata);
		} else if (tipsdata instanceof Double) {
			os.marshal((Double) tipsdata);
		} else if (tipsdata instanceof Float) {
			os.marshal((Float) tipsdata);
		} else if (tipsdata instanceof Boolean) {
			os.marshal((Boolean) tipsdata);
		} else {
			mkdb.Trace.debug("didnot");
			return false;
		}
		return true;
	}

	@Override
	public ReloadResult reload() throws Exception {
		ItemMgr temp = new ItemMgrImp(fire.pb.main.ConfigManager.getInstance());
		itemManager = temp;
		return new ReloadResult(true);
	}

	public boolean unlockAndDelItem(final long roleId, final int itemid) {
		xbean.BagTimeLock btl = xtable.Bagtimelock.get(roleId);
		if (btl == null)
			return false;
		fire.pb.item.Pack bag = (fire.pb.item.Pack) fire.pb.item.Module
				.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		ItemBase ib = bag.getItem(btl.getItemkey());
		if (ib == null || ib.getItemId() != itemid) {
			logger.error("找不到该物品");
			return false;
		}
		xtable.Bagtimelock.remove(roleId);
		fire.pb.item.ItemShuXing attr = fire.pb.item.Module.getInstance()
				.getItemManager().getAttr(itemid);
		if (attr.useup == 1) {
			if (bag.removeItemWithKey(btl.getItemkey(), 1,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 0, "使用道具") != 1) {
				return false;
			}
		}
		return true;
	}
	
	public int getMaxBagCapacity(final long roleid, final int bagtype) {
		Map<Integer, SCommon> Commons = ConfigManager.getInstance().getConf(SCommon.class);
		if (Commons == null) {
			return 0;
		}
		
		int viplevel = xtable.Properties.selectViplevel(roleid);
		if (viplevel > 0) {
			if (bagtype == BagTypes.BAG) {
				SCommon common = Commons.get(321);
				return Integer.parseInt(common.getValue());
			}
			else if (bagtype == BagTypes.DEPOT) {
				SCommon common = Commons.get(322);
				return Integer.parseInt(common.getValue());
			}
		}
		else {
			if (bagtype == BagTypes.BAG) {
				SCommon common = Commons.get(Commontext.MAX_BAG_SIZE);
				return Integer.parseInt(common.getValue());
			}
			else if (bagtype == BagTypes.DEPOT) {
				SCommon common = Commons.get(Commontext.MAX_DEPOT_SIZE);
				return Integer.parseInt(common.getValue());
			}
		}
		
		return 0;
	}
}
