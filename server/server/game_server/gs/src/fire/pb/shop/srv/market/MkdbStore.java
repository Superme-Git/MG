package fire.pb.shop.srv.market;

import fire.pb.shop.srv.market.jdbc.Table;
import xbean.EquipItem;
import xbean.NormalItem;
import xbean.PetItem;

public class MkdbStore {

	public static void addNormal(Long key, NormalItem value) {
		xtable.Normaltab.insert(key, value);
	}
	
	public static void addEquip(Long key, EquipItem value) {
		xtable.Equiptab.insert(key, value);
	}

	public static void addPet(Long key, PetItem value) {
		xtable.Pettab.insert(key, value);
	}

	public static boolean removeNormal(Long key) {
		return xtable.Normaltab.remove(key);
	}

	public static boolean removeEquip(Long key) {
		return xtable.Equiptab.remove(key);
	}

	public static boolean removePet(Long key) {
		return xtable.Pettab.remove(key);
	}
	
	public static NormalItem selectNormal(Long key) {
		return xtable.Normaltab.select(key);
	}
	
	public static EquipItem selectEquip(Long key) {
		return xtable.Equiptab.select(key);
	}
	
	public static PetItem selectPet(Long key) {
		return xtable.Pettab.select(key);
	}
	
	/**
	 * 更新道具数量
	 * @param key
	 * @param newNumber
	 * @return trues成功, false失败
	 */
	public static boolean updateNumber(Table table, Long key, int newNumber) {
		switch (table.toString()) {
		case "ITEM_NORMAL":
			xbean.NormalItem ni = xtable.Normaltab.get(key);
			if (ni == null)
				return false;
			ni.setNumber(newNumber);
			break;
		case "ITEM_EQUIP":
			xbean.EquipItem ei = xtable.Equiptab.get(key);
			if (ei == null)
				return false;
			ei.setNumber(newNumber);
			break;
		case "ITEM_PET":
			xbean.PetItem pi = xtable.Pettab.get(key);
			if (pi == null)
				return false;
			pi.setNumber(newNumber);
			break;
		}
		return true;
	}
	
	/**
	 * 更新价格
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newShowTime 新的公示时间
	 * @return @return trues成功, false失败
	 */
	public static boolean updatePrice(Table table, long key, int newPrice) {
		switch (table.toString()) {
		case "ITEM_NORMAL":
			xbean.NormalItem ni = xtable.Normaltab.get(key);
			if (ni == null)
				return false;
			ni.setPrice(newPrice);
			break;
		case "ITEM_EQUIP":
			xbean.EquipItem ei = xtable.Equiptab.get(key);
			if (ei == null)
				return false;
			ei.setPrice(newPrice);
			break;
		case "ITEM_PET":
			xbean.PetItem pi = xtable.Pettab.get(key);
			if (pi == null)
				return false;
			pi.setPrice(newPrice);
			break;
		}
		return true;
	}
	
	/**
	 * 更新公示时间
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newShowTime 新的公示时间
	 * @return @return trues成功, false失败
	 */
	public static boolean updateShowTime(Table table, long key, long newShowTime) {
		switch (table.toString()) {
		case "ITEM_NORMAL":
			xbean.NormalItem ni = xtable.Normaltab.get(key);
			if (ni == null)
				return false;
			ni.setShowtime(newShowTime);
			break;
		case "ITEM_EQUIP":
			xbean.EquipItem ei = xtable.Equiptab.get(key);
			if (ei == null)
				return false;
			ei.setShowtime(newShowTime);
			break;
		case "ITEM_PET":
			xbean.PetItem pi = xtable.Pettab.get(key);
			if (pi == null)
				return false;
			pi.setShowtime(newShowTime);
			break;
		}
		return true;
	}
	
	/**
	 * 更新过期时间
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newExpireTime 新的过期时间
	 * @return @return trues成功, false失败
	 */
	public static boolean updateExpireTime(Table table, long key, long newExpireTime) {
		switch (table.toString()) {
		case "ITEM_NORMAL":
			xbean.NormalItem ni = xtable.Normaltab.get(key);
			if (ni == null)
				return false;
			ni.setExpiretime(newExpireTime);
			break;
		case "ITEM_EQUIP":
			xbean.EquipItem ei = xtable.Equiptab.get(key);
			if (ei == null)
				return false;
			ei.setExpiretime(newExpireTime);
			break;
		case "ITEM_PET":
			xbean.PetItem pi = xtable.Pettab.get(key);
			if (pi == null)
				return false;
			pi.setExpiretime(newExpireTime);
			break;
		}
		return true;
	}
	
	/**
	 * 更新关注数量
	 * @param table 表名
	 * @param key 数据库和mkdb的key
	 * @param newNumber 新数量
	 * @return @return trues成功, false失败
	 */
	public static boolean updateAttentionNumber(Table table, long key, int newNumber) {
		switch (table.toString()) {
		case "ITEM_NORMAL":
			xbean.NormalItem ni = xtable.Normaltab.get(key);
			if (ni == null)
				return false;
			ni.setAttentionnumber(newNumber);
			break;
		case "ITEM_EQUIP":
			xbean.EquipItem ei = xtable.Equiptab.get(key);
			if (ei == null)
				return false;
			ei.setAttentionnumber(newNumber);
			break;
		case "ITEM_PET":
			xbean.PetItem pi = xtable.Pettab.get(key);
			if (pi == null)
				return false;
			pi.setAttentionnumber(newNumber);
			break;
		}
		return true;
	}

	
}
