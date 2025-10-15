package cn.com.zc.db.walker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import xbean.Bag;
import xbean.Item;
import mkdb.util.Dbx;
import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;

import com.goldhuman.Common.Marshal.OctetsStream;

public class RMBItemScaner {
	
	static List<String> bagTypes = new ArrayList<String>();
	static{
		bagTypes.add("bag");
		bagTypes.add("depot");
		bagTypes.add("equip");
	}
	
	public static Map<Integer, String> scanRmbItems(long roleid){
		Map<Integer, String> items = new HashMap<Integer, String>();
		for(String tableName : bagTypes){
			try {
				scanBag(roleid, tableName, items);
				scanShop(roleid, items);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return items;
	}

	private static void scanShop(long roleid, Map<Integer, String> items) throws Exception {
//		 Dbx.Table roleShops = DbxManager.getInstance().getTableByName("role2shopids");
//		 long shop1 = 0;
//		 long shop2 = 0;
//		 if(roleShops != null){
//			 OctetsStream os = new OctetsStream();
//			 OctetsStream result = roleShops.find(os.marshal(roleid));
//			 if(null != result){
//				 xbean.RoleShops xroleShops = xbean.Pod.newRoleShops();
//				 xroleShops.unmarshal(result);
//				 if(xroleShops.getShopidsAsData().size() > 1){
//					 shop1 = xroleShops.getShopidsAsData().get(0);
//				 }
//				 if(xroleShops.getShopidsAsData().size() > 2){
//					 shop2 = xroleShops.getShopids().get(1);
//				 }
//			 }
//		 }
//		 
//		 if(shop1 != 0){
//			 scanShopById(shop1, items);
//		 }
//		 
//		 if(shop2 != 0){
//			 scanShopById(shop2, items);
//		 }
		
	}

	private static void scanShopById(long shopId, Map<Integer, String> items) throws Exception {
//		Dbx.Table cofcitemsshop = DbxManager.getInstance().getTableByName("cofcitemsshop");
//		if(cofcitemsshop != null){
//			 OctetsStream os = new OctetsStream();
//			 OctetsStream result = cofcitemsshop.find(os.marshal(shopId));
//			 if(null != result){
//				 xbean.ItemShop itemShop = xbean.Pod.newItemShop();
//				 itemShop.unmarshal(result);
//				 addShopItems(itemShop.getCounterinfoAsData(), items);
//			 }
//		}
		
	}

//	private static void addShopItems(Map<Integer, CounterItemsInfo> counterinfoAsData,
//			Map<Integer, String> items) {
//		for(CounterItemsInfo info : counterinfoAsData.values()){
//			addItems(info.getBag(), items);
//		}
//		
//	}

	private static void scanBag(long roleid, String tableName, Map<Integer, String> items) throws Exception {
		 Dbx.Table bag = DbxManager.getInstance().getTableByName(tableName);
		 if(bag != null){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = bag.find(os.marshal(roleid));
			 if(result != null){
				 xbean.Bag xbag = xbean.Pod.newBag();
				 xbag.unmarshal(result);
				 addItems(xbag, items);
			 }
		 }
	}

	private static void addItems(Bag xbag, Map<Integer, String> items) {
		Collection<Item> values = xbag.getItemsAsData().values();
		for(Item item : values){
			int id = item.getId();
			int num = item.getNumber();
			int unBindYBBuy = item.getNumbermapAsData().get(Constant.UNBIND_FUSHI_BUY) == null ? 0:item.getNumbermapAsData().get(Constant.UNBIND_FUSHI_BUY);
			int bindYBBuy = item.getNumbermapAsData().get(Constant.BIND_FUSHI_BUY) == null ? 0:item.getNumbermapAsData().get(Constant.BIND_FUSHI_BUY);
			
			if(items.get(id) == null){
				String itemInfo = (num+unBindYBBuy+bindYBBuy) + "," + unBindYBBuy; 
				items.put(id, itemInfo);
			}else{
				String old = items.get(id);
				String[] nums = old.split(",");
				int numTotal = Integer.parseInt(nums[0]);
				int unBindNum = Integer.parseInt(nums[1]);
				String itemInfo = (numTotal+num+unBindYBBuy+bindYBBuy) + "," + (unBindNum+unBindYBBuy);
				items.put(id, itemInfo);
			}
		}
	}
	
	public static boolean isActiveRole(long roleid) throws Exception{
		Dbx.Table properties = DbxManager.getInstance().getTableByName("properties");
		if(properties == null){
			return false;
		}
		
		OctetsStream os = new OctetsStream();
		OctetsStream result = properties.find(os.marshal(roleid));
		if(null == result){
			return false;
		}
		
		xbean.Properties roleInfo = xbean.Pod.newProperties();
		roleInfo.unmarshal(result);
		long lastOffline = roleInfo.getOfflinetime();
		long lastOnline = roleInfo.getOnlinetime();
		
		if(lastOffline > lastOnline){
			if(!inTheSameDay(lastOffline, System.currentTimeMillis()-dayMills)){
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean inTheSameDay(long firstT,long secondT) {
		if (getCurrentDay(firstT) == getCurrentDay(secondT))
			return true;
		return false;
	}
	
	public static final long dayMills = 1000 * 60 * 60 * 24;
	
	public static final long rawTimeOffset = TimeZone.getDefault().getRawOffset();
	
	public static int getCurrentDay(long time) {
		return (int) ((time + rawTimeOffset) / dayMills);
	}
	
	public static boolean isFirstDayOfMonth(){
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if(1 == dayOfMonth){
			return true;
		}
		
		return false;
	}
	
}
