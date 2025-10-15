package fire.pb.item.equip.diamond;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fire.log.Logger;
import fire.pb.item.SBaoShiBiao;
import fire.pb.item.SEquipLvGemInfo;
import fire.pb.main.ConfigManager;

public class EquipDiamondMgr {

	static Logger logger = Logger.getLogger("ITEM");

	private static EquipDiamondMgr instance = new EquipDiamondMgr();

	static Map<Integer, SBaoShiBiao> diamondPropMap = ConfigManager.getInstance().getConf(fire.pb.item.SBaoShiBiao.class);

	static Map<Integer, DiamondTrade> diamondTradeMap = new ConcurrentHashMap<Integer, DiamondTrade>();

	static Map<Integer, DiamondTrade> curDiamonds = new ConcurrentHashMap<Integer, DiamondTrade>();// 当前的在出售的3种宝石

	static Map<Integer, SEquipLvGemInfo> equipLvGemMap = null;
	
	public static int DIAMOND_MAX_NUM = 15;

	private EquipDiamondMgr() {

	}

	public void init() throws Exception {
		if(fire.pb.fushi.Module.GetPayServiceType() == 0){
			equipLvGemMap = ConfigManager.getInstance().getConf(fire.pb.item.SEquipLvGemInfo.class);
		}else {
			equipLvGemMap = new ConcurrentHashMap<>(ConfigManager.getInstance().getConf(fire.pb.item.DSEquipLvGemInfo.class));
		}
	}

	public static EquipDiamondMgr getInstance() {
		return instance;
	}

	public static Map<Integer, DiamondTrade> getCurDiamonds() {
		return curDiamonds;
	}

	public static Map<Integer, SBaoShiBiao> getDiamondPropMap() {
		return diamondPropMap;
	}
	
	public static Map<Integer, SEquipLvGemInfo> getEquipLvGemMap() {
		return equipLvGemMap;
	}
	
	public static SEquipLvGemInfo getEquipLvGemInfoByLv(int equipLv){
		if (equipLv != 1 && equipLv % 10 != 0) {
			logger.error("EquipDiamondMgr getEquipLvGemInfoByLv error level: " + equipLv);
			return null;
		}
		return equipLvGemMap.get(equipLv);
	}
}
