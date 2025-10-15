package robot.bingfeng;

import java.util.HashMap;
import java.util.Map;

import robot.ConfigMgr;
import fire.pb.instance.SBingFengCfg;
import fire.pb.instancezone.bingfeng.BingFengWangZuoConfig;

public class BingFengUtils {
	public static final Map<Integer, Map<Integer, BingFengWangZuoConfig>> bingFengWangZuoNewConfig = new HashMap<Integer, Map<Integer, BingFengWangZuoConfig>>();

	public static BingFengUtils _instance = new BingFengUtils();

	public static BingFengUtils getInstance() {
		return _instance;
	}

	// public void init() {
	// SBingFengCfgData sfc = new SBingFengCfgData(90, 99, 3, 0, 1831, 33, 12,
	// 183101, 1538);
	// id2InfoMap.put("3_0", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 1, 1831, 33, 12, 183102, 1539);
	// id2InfoMap.put("3_1", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 2, 1831, 33, 12, 183103, 1540);
	// id2InfoMap.put("3_2", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 3, 1831, 33, 12, 183104, 1541);
	// id2InfoMap.put("3_3", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 4, 1831, 33, 12, 183105, 1542);
	// id2InfoMap.put("3_4", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 5, 1832, 68, 18, 183201, 1543);
	// id2InfoMap.put("3_5", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 6, 1832, 68, 18, 183202, 1544);
	// id2InfoMap.put("3_6", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 7, 1832, 68, 18, 183203, 1545);
	// id2InfoMap.put("3_7", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 8, 1832, 68, 18, 183204, 1546);
	// id2InfoMap.put("3_8", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 9, 1832, 68, 18, 183205, 1547);
	// id2InfoMap.put("3_9", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 10, 1833, 31, 29, 183301, 1548);
	// id2InfoMap.put("3_10", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 11, 1833, 31, 29, 183302, 1549);
	// id2InfoMap.put("3_11", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 12, 1833, 31, 29, 183303, 1550);
	// id2InfoMap.put("3_12", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 13, 1833, 31, 29, 183304, 1551);
	// id2InfoMap.put("3_13", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 14, 1833, 31, 29, 183305, 1552);
	// id2InfoMap.put("3_14", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 15, 1834, 80, 39, 183401, 1553);
	// id2InfoMap.put("3_15", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 16, 1834, 80, 39, 183402, 1554);
	// id2InfoMap.put("3_16", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 17, 1834, 80, 39, 183403, 1555);
	// id2InfoMap.put("3_17", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 18, 1834, 80, 39, 183404, 1556);
	// id2InfoMap.put("3_18", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 19, 1834, 80, 39, 183405, 1557);
	// id2InfoMap.put("3_19", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 20, 1835, 54, 44, 183501, 1558);
	// id2InfoMap.put("3_20", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 21, 1835, 54, 44, 183502, 1559);
	// id2InfoMap.put("3_21", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 22, 1835, 54, 44, 183503, 1560);
	// id2InfoMap.put("3_22", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 23, 1835, 54, 44, 183504, 1561);
	// id2InfoMap.put("3_23", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 24, 1835, 54, 44, 183505, 1562);
	// id2InfoMap.put("3_24", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 25, 1836, 32, 69, 183601, 1563);
	// id2InfoMap.put("3_25", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 26, 1836, 32, 69, 183602, 1564);
	// id2InfoMap.put("3_26", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 27, 1836, 32, 69, 183603, 1565);
	// id2InfoMap.put("3_27", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 28, 1836, 32, 69, 183604, 1566);
	// id2InfoMap.put("3_28", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 29, 1836, 32, 69, 183605, 1567);
	// id2InfoMap.put("3_29", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 30, 1837, 72, 77, 183701, 1568);
	// id2InfoMap.put("3_30", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 31, 1837, 72, 77, 183702, 1569);
	// id2InfoMap.put("3_31", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 32, 1837, 72, 77, 183703, 1570);
	// id2InfoMap.put("3_32", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 33, 1837, 72, 77, 183704, 1571);
	// id2InfoMap.put("3_33", sfc);
	// sfc = new SBingFengCfgData(90, 99, 3, 34, 1837, 72, 77, 183705, 1572);
	// id2InfoMap.put("3_34", sfc);
	//
	// }

	public void init() {
		for (SBingFengCfg sBingFengWangZuoConfig : ConfigMgr.getInstance()
				.getConfig(SBingFengCfg.class).values()) {
			Map<Integer, BingFengWangZuoConfig> confs = bingFengWangZuoNewConfig
					.get(sBingFengWangZuoConfig.instzoneid);
			if (confs == null) {
				confs = new HashMap<Integer, BingFengWangZuoConfig>();
				bingFengWangZuoNewConfig.put(sBingFengWangZuoConfig.instzoneid,
						confs);
			}
			confs.put(sBingFengWangZuoConfig.state, new BingFengWangZuoConfig(
					sBingFengWangZuoConfig));

		}
	}

	/**
	 * 得到配置数据
	 * 
	 * @param instzoneid
	 * @param state
	 * @return
	 */
	public BingFengWangZuoConfig getData(int instzoneid, int state) {
		Map<?, ?> state2DataMap = bingFengWangZuoNewConfig.get(instzoneid);
		if (state2DataMap != null)
			return (BingFengWangZuoConfig) state2DataMap.get(state);
		return null;

	}

}
