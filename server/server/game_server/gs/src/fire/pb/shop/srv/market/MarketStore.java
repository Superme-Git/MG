package fire.pb.shop.srv.market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.shop.GoodsType;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.MarketSearchAttr;
import fire.pb.shop.srv.market.jdbc.ConnectionUtils;
import fire.pb.shop.srv.market.jdbc.MarketDAO;
import fire.pb.shop.srv.market.jdbc.Table;
import fire.pb.shop.srv.market.jdbc.Transaction;
import fire.pb.shop.srv.market.jdbc.dao.EquipDAO;
import fire.pb.shop.srv.market.jdbc.dao.NormalDAO;
import fire.pb.shop.srv.market.jdbc.dao.PetDAO;
import fire.pb.shop.srv.market.pojo.EquipDaoBean;
import fire.pb.shop.srv.market.pojo.NormalDaoBean;
import fire.pb.shop.srv.market.pojo.PetDaoBean;
import fire.pb.shop.utils.BeanCopy;
import fire.pb.shop.utils.MarketUtils;
import mkdb.XError;

public class MarketStore {

	public static boolean addNormal(Long key, NormalDaoBean ndb) {
		// mkdb存储
		xbean.NormalItem mkv = BeanCopy.normalDaoCopyMkdbBean(ndb);
		boolean mksucc = false;
		try {
			MkdbStore.addNormal(key, mkv);
			mksucc = true;
		} catch (XError xe) {
			MarketManager.LOG.error(xe.getMessage());
		}
		// h2存储
		boolean dbsucc = NormalDAO.getInstance().addNormal(ndb);
		return mksucc && dbsucc;
	}

	public static boolean addEquip(Long key, EquipDaoBean edb) {
		// mkdb存储
		xbean.EquipItem mkv = BeanCopy.equipDaoCopyMkdbBean(edb);
		boolean mksucc = false;
		try {
			MkdbStore.addEquip(key, mkv);
			mksucc = true;
		} catch (XError xe) {
			MarketManager.LOG.error(xe.getMessage());
		}
		// h2存储
		boolean dbsucc = EquipDAO.getInstance().addEquip(edb);
		return mksucc && dbsucc;
	}

	public static boolean addPet(Long key, PetDaoBean pdb) {
		// mkdb存储
		xbean.PetItem value = BeanCopy.petDaoCopyMkdbBean(pdb);
		boolean mksucc = false;
		try {
			MkdbStore.addPet(key, value);
			mksucc = true;
		} catch (XError xe) {
			MarketManager.LOG.error(xe.getMessage());
		}
		// h2存储
		boolean dbsucc = PetDAO.getInstance().addPet(pdb);
		return mksucc && dbsucc;
	}

	public static boolean removeNormal(Long key) {
		// mkdb删除数据和dao删除数据
		return MkdbStore.removeNormal(key) && NormalDAO.getInstance().removeNormal(key);
	}

	public static boolean removeEquip(Long key) {
		// mkdb删除数据和dao删除数据
		return MkdbStore.removeEquip(key) && EquipDAO.getInstance().removeEquip(key);
	}

	public static boolean removePet(Long key) {
		// mkdb删除数据和dao删除数据
		return MkdbStore.removePet(key) && PetDAO.getInstance().removePet(key);
	}
	
	/**
	 * 更新道具数量
	 * @param key
	 * @param newNumber
	 * @return trues成功, false失败
	 */
	public static boolean updateNumber(Table table, Long key, int newSaleNumber) {
		return MkdbStore.updateNumber(table, key, newSaleNumber)
				&& MarketDAO.getInstance().updateNumber(table, key, newSaleNumber);
	}
	
	/**
	 * 更新价格
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newShowTime 新的公示时间
	 * @return @return trues成功, false失败
	 */
	public static boolean updatePrice(Table table, long key, int newPrice) {
		return MkdbStore.updatePrice(table, key, newPrice)
				&& MarketDAO.getInstance().updatePrice(table, key, newPrice);
	}
	
	/**
	 * 更新公示时间
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newShowTime 新的公示时间
	 * @return @return trues成功, false失败
	 */
	public static boolean updateShowTime(Table table, long key, long newShowTime) {
		return MkdbStore.updateShowTime(table, key, newShowTime)
				&& MarketDAO.getInstance().updateShowTime(table, key, newShowTime);
	}
	
	/**
	 * 更新过期时间
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newExpireTime 新的过期时间
	 * @return @return trues成功, false失败
	 */
	public static boolean updateExpireTime(Table table, long key, long newExpireTime) {
		return MkdbStore.updateExpireTime(table, key, newExpireTime)
				&& MarketDAO.getInstance().updateExpireTime(table, key, newExpireTime);
	}
	
	/**
	 * 更新关注数量
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newNumber 新数量
	 * @return @return trues成功, false失败
	 */
	public static boolean updateAttentionNumber(Table table, long key, int newNumber) {
		return MkdbStore.updateAttentionNumber(table, key, newNumber)
				&& MarketDAO.getInstance().updateAttentionNumber(table, key, newNumber);
	}

	/**
	 * @return 搜索装备
	 */
	public static Object[] searchEquip(int euqipType, int level, int priceMin, int priceMax, int effect,
			int skill, List<Integer> colors, List<MarketSearchAttr> basicAttrs,
			List<Integer> additionalAttrs, int totalAttr, int score, int sellState) {

		List<Object[]> searchResult = EquipDAO.getInstance().searchEquip(euqipType, level, priceMin, priceMax, effect,
				skill, colors, totalAttr, score, sellState);

		if (null == searchResult || searchResult.size() <= 0) {
			return null;
		}
		
		Integer firstNo = 0;
		Integer twoNo = 0;
		Integer threeNo = 0;
		
		ArrayList<MarketGoods> results = new ArrayList<>();
		
		for (Object[] keys : searchResult) {
			xbean.EquipItem equip = MkdbStore.selectEquip((Long)keys[0]);
			firstNo = equip.getFirstno();
			twoNo = equip.getTwono();
			threeNo = equip.getThreeno();
			
			// 基础属性不为空
			boolean basicAttrSuccessFind = false;
			int basicAttrConditionCount = basicAttrs.size();
			int basicAttrSuccessCount = 0;
			if (basicAttrConditionCount > 0) {
				Map<Integer, Integer> attrMap = equip.getAttr();
				for (MarketSearchAttr attr : basicAttrs) {
					Integer val = attrMap.get(attr.attrid);
					if (null == val) 
						continue;
					if (val >= attr.attrval) {
						basicAttrSuccessCount++;
					}
				}
				if (basicAttrConditionCount == basicAttrSuccessCount)
					basicAttrSuccessFind = true;
			}
			
			// 附加属性不为空 
			boolean additionalAttrSuccessFind = false;
			int additionalAttrConditionCount = additionalAttrs.size();
			int additionalAttrSuccessCount = 0;
			if (additionalAttrConditionCount > 0) {
				Map<Integer, Integer> additionalAttrMap = equip.getAddattr();
				for (Integer additionalAttrId : additionalAttrs) {
					Integer val = additionalAttrMap.get(additionalAttrId);
					if (null != val) {
						additionalAttrSuccessCount++;
					}
				}
				if (additionalAttrConditionCount == additionalAttrSuccessCount)
					additionalAttrSuccessFind = true;
			}
			
			if (basicAttrConditionCount > 0 && additionalAttrConditionCount > 0) {// 两种属性都不为空
				if (basicAttrSuccessFind && additionalAttrSuccessFind) {
					results.add(copyEquipBean(equip));
				}
			} else if (basicAttrConditionCount > 0 && additionalAttrConditionCount == 0) {// 基础属性不为空
				if (basicAttrSuccessFind) {
					results.add(copyEquipBean(equip));
				}
			} else if (basicAttrConditionCount == 0 && additionalAttrConditionCount > 0) {// 附加属性不为空 
				if (additionalAttrSuccessFind) {
					results.add(copyEquipBean(equip));
				}
			} else {// 两种属性都为空
				results.add(copyEquipBean(equip));
			}
			
			if (results.size() >= MarketUtils.MAX_SEARCH_RESULT) {
				break;
			}
			
		}
		
		return new Object[] {firstNo, twoNo, threeNo, results};
	}
	
	/**
	 * @return 宠物搜索
	 */
	public static Object[] searchPet(int petType, int levelMin, int levelMax, int priceMin, int priceMax,
			List<MarketSearchAttr> talents, List<Integer> skills, int skillNumber, List<MarketSearchAttr> attrs, int score, int sellState) {

		List<Object[]> searchResult = PetDAO.getInstance().searchPet(petType, levelMin, levelMax, priceMin, priceMax,
				talents, attrs, skillNumber, score, sellState);
		
		Integer firstNo = 0;
		Integer twoNo = 0;
		Integer threeNo = 0;
		
		ArrayList<MarketGoods> results = new ArrayList<>();
		
		for (Object[] keys : searchResult) {
			xbean.PetItem pet = MkdbStore.selectPet((Long)keys[0]);
			firstNo = pet.getFirstno();
			twoNo = pet.getTwono();
			threeNo = pet.getThreeno();
			
			// 技能不为空
			int skillConditionCount = skills.size();
			int skillSuccessCount = 0;
			if (skillConditionCount > 0) {
				for (Integer skillId : skills) {
					if (!pet.getSkills().contains(skillId)) 
						break;
					skillSuccessCount++;
				}
				if (skillConditionCount == skillSuccessCount) {
					results.add(copyPetBean(pet));
				}
			} else {
				results.add(copyPetBean(pet));
			}
			
			if (results.size() >= MarketUtils.MAX_SEARCH_RESULT) {
				break;
			}
		}

		return new Object[] {firstNo, twoNo, threeNo, results};
	}
	
	public static MarketGoods copyEquipBean(xbean.EquipItem equip) {
		MarketGoods bean = new MarketGoods();
		bean.id = equip.getId();
		bean.saleroleid = equip.getRoleid();
		bean.itemid = equip.getItemid();
		bean.itemtype = GoodsType.NORMAL_ITEM;
		bean.key = equip.getKey();
		bean.price = equip.getPrice();
		bean.showtime = equip.getShowtime();
		bean.expiretime = equip.getExpiretime();
		bean.num = equip.getNumber();
		bean.level = equip.getLevel();
		bean.attentionnumber = equip.getAttentionnumber();
		return bean;
	}
	
	public static MarketGoods copyPetBean(xbean.PetItem pet) {
		MarketGoods bean = new MarketGoods();
		bean.id = pet.getId();
		bean.saleroleid = pet.getRoleid();
		bean.itemid = pet.getItemid();
		bean.itemtype = GoodsType.PET;
		bean.key = pet.getKey();
		bean.price = pet.getPrice();
		bean.showtime = pet.getShowtime();
		bean.expiretime = pet.getExpiretime();
		bean.num = pet.getNumber();
		bean.level = pet.getLevel();
		bean.attentionnumber = pet.getAttentionnumber();
		return bean;
	}
	
	/**
	 * <h1>在事务中执行,结束公示时间</h1>
	 * @param newShowTime 公示结束时间, 如果不指定默认是当前时间
	 */
	public static boolean overShowTime(long newShowTime) {
		
		long now = System.currentTimeMillis();
		if (newShowTime == 0L) { 
			newShowTime = now;
		}
		
		List<MarketGoods> normals = MarketDAO.getInstance().getAll(Table.ITEM_NORMAL);
		for (MarketGoods n : normals) {
			MkdbStore.updateShowTime(Table.ITEM_NORMAL, n.id, now);
		}
		
		List<MarketGoods> equips = MarketDAO.getInstance().getAll(Table.ITEM_EQUIP);
		for (MarketGoods e : equips) {
			MkdbStore.updateShowTime(Table.ITEM_EQUIP, e.id, now);
		}
		
		List<MarketGoods> pets = MarketDAO.getInstance().getAll(Table.ITEM_PET);
		for (MarketGoods p : pets) {
			MkdbStore.updateShowTime(Table.ITEM_PET, p.id, now);
		}
		
		Transaction.begin();
		
		MarketDAO.getInstance().overShowTime(Table.ITEM_NORMAL, now);
		MarketDAO.getInstance().overShowTime(Table.ITEM_EQUIP, now);
		MarketDAO.getInstance().overShowTime(Table.ITEM_PET, now);
		
		Transaction.commit();
		
		ConnectionUtils.closeConnetion();
		
		return true;
	}
	
	/**
	 * <h1>在事务中执行,延长过期时间</h1>
	 * @param newExpireTime 延长过期时间, 如果不指定默认是当前时间
	 */
	public static boolean extendExpireTime(long newShowTime) {
		
		long now = System.currentTimeMillis();
		if (newShowTime == 0L) { 
			newShowTime = now;
		}
		
		List<MarketGoods> normals = MarketDAO.getInstance().getAll(Table.ITEM_NORMAL);
		for (MarketGoods n : normals) {
			MkdbStore.updateExpireTime(Table.ITEM_NORMAL, n.id, now);
		}
		
		List<MarketGoods> equips = MarketDAO.getInstance().getAll(Table.ITEM_EQUIP);
		for (MarketGoods e : equips) {
			MkdbStore.updateExpireTime(Table.ITEM_EQUIP, e.id, now);
		}
		
		List<MarketGoods> pets = MarketDAO.getInstance().getAll(Table.ITEM_PET);
		for (MarketGoods p : pets) {
			MkdbStore.updateExpireTime(Table.ITEM_PET, p.id, now);
		}
		
		Transaction.begin();
		
		MarketDAO.getInstance().extendExprieTime(Table.ITEM_NORMAL, now);
		MarketDAO.getInstance().extendExprieTime(Table.ITEM_EQUIP, now);
		MarketDAO.getInstance().extendExprieTime(Table.ITEM_PET, now);
		
		Transaction.commit();
		
		ConnectionUtils.closeConnetion();
		
		return true;
	}

}
