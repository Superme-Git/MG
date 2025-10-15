package cn.com.zc.db.walker;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xbean.ActiveRoleInfo;
import xbean.Bag;
import xbean.Equip;
import xbean.Properties;
import mkdb.util.DatabaseMetaData.Table;
import mkdb.util.Dbx;
import cn.com.zc.db.BeanCollection;
import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;
import cn.com.zc.db.Main;
import cn.com.zc.db.ScoreSnapShotHelper;
import cn.com.zc.db.bean.RoleLevelBean;
import cn.com.zc.db.bean.RoleMoneyBean;
import cn.com.zc.db.bean.ScoreSnapShotBean;
import cn.com.zc.db.util.FileManager;

import com.goldhuman.Common.Marshal.MarshalException;
import com.goldhuman.Common.Marshal.OctetsStream;

/**
 * 人物属性表遍历操作
 * 
 */
public class RolePropertyWalker extends Walker {

	private Dbx.Table bag = DbxManager.getInstance().getTableByName("bag");

	private Dbx.Table depot = DbxManager.getInstance().getTableByName("depot");

	private Dbx.Table equips = DbxManager.getInstance().getTableByName("equips");

	final static String separator = ",";

	public RolePropertyWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		Long roleid = (Long) key;
		xbean.Properties properties = (Properties) value;

		if ((getQueryType() & Constant.SORT_ROLE_LEVEL) == Constant.SORT_ROLE_LEVEL
				|| getQueryType() == 0) {
			if (properties.getLevel() >= 30) {
				RoleLevelBean levelBean = new RoleLevelBean();
				levelBean.setLevel(properties.getLevel());
				levelBean.setRoleid(roleid);
				levelBean.setSchool(properties.getSchool());
				levelBean.setExp(properties.getAllexp());
				levelBean.setName(properties.getRolename());

				BeanCollection.getInstance().toInsertRoleLevelRank(levelBean,
						BeanCollection.getRoleLevelList());
			}
		}

		if ((getQueryType() & Constant.QUICK_PHOTO) == Constant.QUICK_PHOTO
				|| getQueryType() == 0) {
			
			//因为是凌晨12点以后（已经是新的一天）开始执行，需要判断，玩家前一天是否登陆，跨天不计算在内，只记载等过的玩家
			if (RMBItemScaner.inTheSameDay(System.currentTimeMillis()-RMBItemScaner.dayMills,properties.getOnlinetime())) {
				//设置综合实力
				ScoreSnapShotHelper.initScoreSnapShotBean(roleid,
						properties.getRolename(),
						properties.getLevel()).
						setRolezonghemaxscore(properties.getRolezonghemaxscore());
			}
			// 快照
			try {
				doQuickPhoto(roleid, properties);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

		if ((getQueryType() & Constant.SORT_MONEY) == Constant.SORT_MONEY
				|| getQueryType() == 0) {
			RoleMoneyBean moneyBean = new RoleMoneyBean();
			try {
				long money = getMoneyTotal(roleid, properties);
				if (money > 0) {
					moneyBean.setMoney(money);
					moneyBean.setRoleid(roleid);
					moneyBean.setSchool(properties.getSchool());
					moneyBean.setName(properties.getRolename());

					BeanCollection.getInstance().toInsertRoleMoneyRank(
							moneyBean, BeanCollection.getRoleMoneyList());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// test
		// TestUtil.checkRoleFamily(roleid, properties.getFamilykey());

		return true;
	}
	
	
//	private boolean isSameDay(){
//		
//	}

	private xbean.Bag getMyBag(Long roleid) throws MarshalException {
		if (bag == null)
			return null;

		OctetsStream os = new OctetsStream();
		OctetsStream result = bag.find(os.marshal(roleid));
		if (result != null) {
			xbean.Bag xbag = xbean.Pod.newBag();
			xbag.unmarshal(result);
			return xbag;
		}
		return null;
	}

	private xbean.Bag getMyDepot(Long roleid) throws MarshalException {
		if (bag == null)
			return null;

		OctetsStream os = new OctetsStream();
		OctetsStream result = depot.find(os.marshal(roleid));
		if (result != null) {
			xbean.Bag xbag = xbean.Pod.newBag();
			xbag.unmarshal(result);
			return xbag;
		}
		return null;
	}

//	Long[] getMyShopids(Long roleid) throws MarshalException {
//		if (roleShops == null || shopComm == null)
//			return null;
//
//		long shop1 = 0;
//		long shop2 = 0;
//		OctetsStream os = new OctetsStream();
//		OctetsStream result = roleShops.find(os.marshal(roleid));
//		if (null != result) {
//			xbean.RoleShops xroleShops = xbean.Pod.newRoleShops();
//			xroleShops.unmarshal(result);
//			if (xroleShops.getShopidsAsData().size() > 0) {
//				shop1 = xroleShops.getShopidsAsData().get(0);
//			}
//			if (xroleShops.getShopidsAsData().size() > 1) {
//				shop2 = xroleShops.getShopidsAsData().get(1);
//			}
//		}
//
//		Long[] myShops = new Long[2];
//		myShops[0] = shop1;
//		myShops[1] = shop2;
//		return myShops;
//	}
	
	void printItemShopInfo(Long roleid, String head) throws MarshalException {
//		Long[] myShops = getMyShopids(roleid);
//		if (null == myShops)
//			return;
//		String myHead = head + separator + 4 + separator;
//		for(int i=0; i<myShops.length; i++){
//			OctetsStream comm1 = new OctetsStream();
//			OctetsStream myItemShop = itemShop.find(comm1.marshal(myShops[i]));
//			if(null == myItemShop)
//				continue;
//			xbean.ItemShop itemShop = xbean.Pod.newItemShop();
//			myItemShop.unmarshal(itemShop);
//			if(itemShop == null)
//				continue;
//			for(xbean.CounterItemsInfo info : itemShop.getCounterinfoAsData().values()){
//				xbean.Bag bag = info.getBag();
//				printItemQuickPhoto(bag, myHead);
//			}
//		}
		
	}

//	xbean.ShopCommonProp[] getMyShopCommon(Long roleid) throws MarshalException {
//		Long[] myShops = getMyShopids(roleid);
//		if (null == myShops)
//			return null;
//
//		int size = 0;
//		if(myShops[0] != 0 && myShops[1] != 0){
//			size = 2;
//		}else if(myShops[0] != 0 && myShops[1] == 0){
//			size = 1;
//		}else{
//			return null;
//		}
//		
//		xbean.ShopCommonProp[] shops = new xbean.ShopCommonProp[size];
//		OctetsStream comm1 = new OctetsStream();
//		OctetsStream myShop1 = shopComm.find(comm1.marshal(myShops[0]));
//		if (null != myShop1) {
//			xbean.ShopCommonProp common1 = xbean.Pod.newShopCommonProp();
//			common1.unmarshal(myShop1);
//			shops[0] = common1;
//		}
//
//		if (size == 2) {
//			OctetsStream comm2 = new OctetsStream();
//			OctetsStream myShop2 = shopComm.find(comm2.marshal(myShops[1]));
//			if (null != myShop2) {
//				xbean.ShopCommonProp common2 = xbean.Pod.newShopCommonProp();
//				common2.unmarshal(myShop2);
//				shops[1] = common2;
//			}
//		}
//		return shops;
//	}

	private long getMoneyTotal(Long roleid, Properties properties)
			throws Exception {
		long money = 0;
		xbean.Bag myBag = getMyBag(roleid);

		long depotMoney = 0;
		xbean.Bag myDepot = getMyDepot(roleid);

		long shop1M = 0;
		long shop2M = 0;
//		xbean.ShopCommonProp[] common = getMyShopCommon(roleid);
//		if (null != common) {
//			if (common.length > 0) {
//				shop1M = common[0].getShopcapital();
//			}
//			if (common.length > 1) {
//				shop2M = common[1].getShopcapital();
//			}
//		}

		return shop1M + shop2M + money + depotMoney;

	}

	private void doQuickPhoto(Long roleid, Properties properties)
			throws Exception {
		int userid = properties.getUserid();
		int shapeId = properties.getShape();
		int school = properties.getSchool();
		int level = properties.getLevel();
		long lastOnline = properties.getOnlinetime();
		long todayOnline = getTodayOnlineTimes(lastOnline,
				properties.getOfflinetime());
		long sumOnline = properties.getSumonlinetime();
		long createTime = properties.getCreatetime();


		long money = 0;
		if (bag != null) {
			OctetsStream os = new OctetsStream();
			OctetsStream result = bag.find(os.marshal(roleid));
			if (result != null) {
				xbean.Bag xbag = xbean.Pod.newBag();
				xbag.unmarshal(result);
			}
		}

		long depotMoney = 0;
		if (depot != null) {
			OctetsStream os = new OctetsStream();
			OctetsStream result = depot.find(os.marshal(roleid));
			if (result != null) {
				xbean.Bag xbag = xbean.Pod.newBag();
				xbag.unmarshal(result);
			}
		}
		
		

		StringBuffer sb = new StringBuffer();
		String separator = ",";
		sb.append(Main.serverid+separator);
		sb.append(userid + separator);
		sb.append(properties.getPlatformuid()+ separator);
		sb.append(roleid + separator);
		sb.append(shapeId + separator);
		sb.append(school + separator);
		sb.append(level + separator);
		sb.append(money + separator);
		sb.append(depotMoney + separator);
		sb.append(properties.getExp() + separator);

		xbean.ActiveRoleInfo activeRoleInfo = getActiveRoleInfo(roleid);
		long ybgdmoney = 0;
		long ybzcmoney = 0;
		if (null != activeRoleInfo) {
			ybgdmoney = activeRoleInfo.getDealmoneyinplatform();
			ybzcmoney = activeRoleInfo.getTmpmoneyinplatform();
		}

		sb.append(ybgdmoney + separator);
		sb.append(ybzcmoney + separator);
		sb.append(createTime + separator);
		sb.append((sumOnline / (1000 * 60)) + separator);
		sb.append(todayOnline + separator);
		if(lastOnline != -1){
			sb.append(lastOnline + separator);
		}else{
			sb.append(createTime + separator);
		}
		sb.append(properties.getRolename() + separator);

		
		FileManager.getInstance().setQuickPhotoRecord(Constant.ROLE_PROPERTY,
				sb.toString());

		StringBuffer sb3 = new StringBuffer();
//		if (-2 != (familyKey + factionKey + shop1 + shop2)) {
//			sb3.append(userid + separator);
//			sb3.append(roleid + separator);
//			if (familyKey == -1) {
//				sb3.append(0 + separator);
//			} else {
//				sb3.append(familyKey + separator);
//			}
//			if (factionKey == -1) {
//				sb3.append(0 + separator);
//			} else {
//				sb3.append(factionKey + separator);
//			}
//			sb3.append(shop1 + separator);
//			sb3.append(shop2);
//		}

		if (sb3.length() != 0) {
			FileManager.getInstance().setQuickPhotoRecord(
					Constant.ROLE_RELATION_PROPERTY, sb3.toString());
		}

		// 角色付费物品信息
		boolean isFirstDayOfMonth = RMBItemScaner.isFirstDayOfMonth();
		boolean doCurrentRolesItemDetail = RMBItemScaner.isActiveRole(roleid)
				|| isFirstDayOfMonth;

		if (doCurrentRolesItemDetail) {
			StringBuffer sb2 = new StringBuffer();
			sb2.append(userid + separator);
			sb2.append(roleid + separator);
			if(lastOnline == -1){
				sb2.append(createTime + separator);
			}else{
				sb2.append(lastOnline + separator);
			}
			int _360Flag = get360Flag(properties.getOfflinetime(), lastOnline);
			sb2.append(_360Flag + separator);
			addItems(sb2, RMBItemScaner.scanRmbItems(roleid));

			FileManager.getInstance().setQuickPhotoRecord(Constant.ITEM_DETAIL,
					sb2.toString());
		}

		// 扫描活跃角色的装备信息
		if (RMBItemScaner.isActiveRole(roleid)) {
			String userName = "";
			Dbx.Table userInfo = DbxManager.getInstance().getTableByName(
					"auuserinfo");
			if (null != userInfo) {
				userName = userInfo.getName();
			}
			String head = properties.getUserid() + separator + userName
					+ separator + roleid + separator + properties.getRolename();
			for (int i = 1; i < 5; i++) {
				createEquipQuickPhoto(roleid, head, i);
			}
			
			
		}

	}

	private void createEquipQuickPhoto(Long roleid, String head, int type) throws MarshalException{
		String myHead = head + separator + type + separator;
		if(type == 1 || type == 2 || type == 3){
			Dbx.Table equipTable = null;
			if(type == 1){
				equipTable = DbxManager.getInstance().getTableByName("equip");
			}else if(type == 2){
				equipTable = bag;
			}else if(type == 3){
				equipTable = depot;
			}
				
			if(null == equipTable)
				return;
		
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = equipTable.find(os.marshal(roleid));
			 if(null == result)
				 return;
			 xbean.Bag equip = xbean.Pod.newBag();
			 equip.unmarshal(result);
			 
			 printItemQuickPhoto(type,roleid,equip, myHead);
			 
			
		}else if(type == 4){
			printItemShopInfo(roleid, head);
		}
		
	}

	private void printItemQuickPhoto(int bagType,long roleId,Bag equip, String myHead) throws MarshalException {
		//评分
		 ScoreSnapShotBean bean=ScoreSnapShotHelper.getScoreSnapShotBean(roleId);
		 
		 for(xbean.Item xItem : equip.getItemsAsData().values()){
			 boolean isEquip = (xItem.getId()/10000) == 4;
			 if(!isEquip)
				 continue;
			 
			 StringBuffer sb = new StringBuffer();
			 Long extKey = xItem.getExtid();
			 OctetsStream keyOs = new OctetsStream();
			 OctetsStream extResult = equips.find(keyOs.marshal(extKey));
			 if(extResult == null)
				 continue;
			 xbean.Equip equipAttr = xbean.Pod.newEquip();
			 equipAttr.unmarshal(extResult);
			 sb.append(xItem.getId() + separator);
//			 sb.append(equipAttr.getEndure() + separator);
//			 sb.append(equipAttr.getCurmaxendure() + separator);
//			 sb.append(equipAttr.getMaxendure() + separator);
			 int isBind = ((xItem.getFlags() & ~fire.pb.Item.BIND) != 0) ? 1 : 0;
			 sb.append(isBind + separator);
			 //sb.append(equipAttr.getPrefixtype() + separator);
			 printBaseAttr(equipAttr, sb);
			 //sb.append(equipAttr.getRefinelevel() + separator);
			 printAppentAttr(equipAttr, sb);
			 printDiamonds(equipAttr, sb);
			 sb.append(equipAttr.getSkill() + separator);
			 sb.append(equipAttr.getEffect() + separator);
			 String producer = equipAttr.getProducer().equals("") ? String.valueOf(0) : equipAttr.getProducer();
			 sb.append(producer);
			 FileManager.getInstance().setQuickPhotoRecord(Constant.ROLE_EQUIP_DETAIL, myHead + sb.toString());
			 
			 //非装备的，未装备
			 if (bean==null||bagType!=1) {
				continue;
			 }
//			 ItemAttr itemAttr=Main.itemManager.getAttr(xItem.getId());
			 bean.addEquip(xItem.getUniqueid(),xItem.getId(),xItem.getPosition(),equipAttr.getEquipscore());
		 }
		
	}

	private void printDiamonds(Equip equipAttr, StringBuffer sb) {
		List<Integer> diamonds = equipAttr.getDiamondsAsData();
		if (diamonds != null) 
		for (Integer value : diamonds) {
			sb.append(value + separator);
		}
	}

	private void printAppentAttr(Equip equipAttr, StringBuffer sb) {
		int counter = 0;

		int tmp = 3 - counter;
		if (tmp > 0) {
			for (int i = 0; i < tmp; i++) {
				sb.append(0 + separator);
				sb.append(0 + separator);
				sb.append(0 + separator);
			}
		}

	}

	private void printBaseAttr(Equip equipAttr, StringBuffer sb) {
/*		Iterator<Entry<Integer, Integer>> iterator = equipAttr.getGrowattrAsData()
				.entrySet().iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			if (counter >= 4)
				break;
			Entry<Integer, Integer> current = iterator.next();
			sb.append(current.getKey() + separator);
			sb.append(current.getValue() + separator);
			counter++;
		}
		int tmp = 4 - counter;
		if (tmp > 0) {
			for (int i = 0; i < tmp; i++) {
				sb.append(0 + separator);
				sb.append(0 + separator);
			}
		}*/

	}

	private ActiveRoleInfo getActiveRoleInfo(Long roleid)
			throws MarshalException {
		xbean.ActiveRoleInfo activeRoleInfo = null;
		Dbx.Table activeRole = DbxManager.getInstance().getTableByName(
				"activeroletable");
		if (null == activeRole)
			return null;

		OctetsStream os = new OctetsStream();
		OctetsStream result = activeRole.find(os.marshal(roleid));
		if (null == result)
			return null;

		activeRoleInfo = xbean.Pod.newActiveRoleInfo();
		activeRoleInfo.unmarshal(result);

		return activeRoleInfo;

	}

	private void addItems(StringBuffer sb2, Map<Integer, String> scanRmbItems) {
		if (scanRmbItems.size() == 0 || null == scanRmbItems)
			return;

		Iterator<Entry<Integer, String>> iterator = scanRmbItems.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, String> current = iterator.next();
			sb2.append(current.getKey() + ",");
			sb2.append(current.getValue() + ";");
		}

	}

	private int get360Flag(long offlinetime, long lastOnline) {
		if (lastOnline == -1)
			return 0;

		if (lastOnline > offlinetime) {
			return 0;
		} else {
			long timeSpace = System.currentTimeMillis() - offlinetime;
			long _360Time = 365L * 24 * 60 * 60 * 1000;
			if (timeSpace >= _360Time) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private long getTodayOnlineTimes(long lastOnline, long offlinetime) {
		int yesterday = (int) (System.currentTimeMillis()
				/ (1000 * 60 * 60 * 24) - 1);
		if (offlinetime > lastOnline) {
			if (offlinetime / (1000 * 60 * 60 * 24) != yesterday) {
				return 0;
			}
			return offlinetime - lastOnline;
		} else {
			if (lastOnline / (1000 * 60 * 60 * 24) != yesterday) {
				return 0;
			}
			return System.currentTimeMillis() - lastOnline;
		}
	}

}
