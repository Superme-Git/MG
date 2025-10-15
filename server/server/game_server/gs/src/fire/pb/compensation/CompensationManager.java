
package fire.pb.compensation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import xbean.Compensation;
import fire.pb.PCheckUserAccountInfo;
import fire.pb.login.SCompensationConfig;
import fire.pb.main.ConfigManager;
import fire.pb.npc.NpcServiceCond.Condition;

public class CompensationManager {

	private static Map<Integer, List<CompensationConfig>> configMaps = new HashMap<Integer, List<CompensationConfig>>();

	private static Collection<List<CompensationConfig>> configSets = null;

	// private static Set<Integer> compensationKeys = null;

	// 用来发送特殊奖励给某个团体的,比如封测老玩家 用roleid做key
	private static Map<Long, List<SpecialCompensationConfig>> specialMaps = new HashMap<Long, List<SpecialCompensationConfig>>();

	// private static Set<Long> specialMapKeys = null;
	// 有时候roleid为0，需要按照userid来发放(同一userid只能领一次奖励),这种情况用userid做key
	private static Map<Integer, List<SpecialCompensationConfig>> useridSpecialMaps = new HashMap<Integer, List<SpecialCompensationConfig>>();

	// private static Set<Integer> useridSpecialMapKeys = null;

	public static Set<Integer> NPC_IDS = new HashSet<Integer>();
	static {
		NPC_IDS.add(10963);
		NPC_IDS.add(10964);
		NPC_IDS.add(10965);
		NPC_IDS.add(10966);
	}

	private static final CompensationManager instance = new CompensationManager();

	private CompensationManager() {
	}

	public static CompensationManager getInstance() {
		return instance;
	}

	public void init() throws Exception {

		configMaps.clear();
		specialMaps.clear();
		useridSpecialMaps.clear();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
		java.util.NavigableMap<Integer, SCompensationConfig> compensationConfs = ConfigManager
				.getInstance().getConf(fire.pb.login.SCompensationConfig.class);

		for (Entry<Integer, SCompensationConfig> entry : compensationConfs.entrySet()) {
			SCompensationConfig conf = entry.getValue();
			String startTimeStr = conf.startTime;
			Date startDate = sdf.parse(startTimeStr);
			String endTimeStr = conf.endTime;
			Date endDate = sdf.parse(endTimeStr);
			long startTime = startDate.getTime();
			long endTime = endDate.getTime();
			List<Long> roleids = new ArrayList<Long>();
			List<Integer> userids = new ArrayList<Integer>();
			getLongFromString(roleids, conf.roleid);
			getIntergerFromString(userids, conf.userid);

			if (!roleids.isEmpty()) {
				for (Long rid : roleids) {
					SpecialCompensationConfig scc = new SpecialCompensationConfig(
							conf.getCompensationid(), rid, 0, conf.itemid,
							startTime, endTime, conf.getConditionid(),
							conf.getMsgid(), conf.getServiceName(),
							getIntergerFromString(new ArrayList<Integer>(),
									conf.serverid));
					addSpecialCompensationConfig(rid, scc, specialMaps);
				}
			} else if (!userids.isEmpty()) {
				for (Integer uid : userids) {
					SpecialCompensationConfig scc = new SpecialCompensationConfig(
							conf.getCompensationid(), 0, uid, conf.itemid,
							startTime, endTime, conf.getConditionid(),
							conf.getMsgid(), conf.getServiceName(),
							getIntergerFromString(new ArrayList<Integer>(),
									conf.serverid));
					addSpecialCompensationConfig(uid, scc, useridSpecialMaps);
				}
			} else {
				CompensationConfig compenConfig = new CompensationConfig(
						startTime, endTime, conf.getItemid(),
						conf.getCompensationid(), conf.getMsgid(),
						conf.getServiceName(), conf.getConditionid(),
						getIntergerFromString(new ArrayList<Integer>(),
								conf.serverid), conf.onChannel, conf.offChannel);
				addCompensationConfig(compenConfig);
			}
		}
		setConfigSet();

	}

	/**
	 * 
	 * @param result
	 * @param roleids
	 */
	private void getLongFromString(List<Long> result, String roleids) {

		if (roleids.length() > 3) {
			String[] strs = roleids.split(";");
			for (String str : strs) {
				result.add(Long.parseLong(str));
			}
		}

	}

//	private List<String> getStringFromString(List<String> result, String roleids) {
//
//		if (roleids != null && roleids.length() > 3) {
//			String[] strs = roleids.split(";");
//			for (String str : strs) {
//				result.add(str);
//			}
//		}
//		return result;
//	}

	private List<Integer> getIntergerFromString(List<Integer> result, String roleids) {

		if (roleids != null && roleids.length() > 3) {
			String[] strs = roleids.split(";");
			for (String str : strs) {
				result.add(Integer.valueOf(str));
			}
		}
		return result;
	}

	private <T extends Object> void addSpecialCompensationConfig(T id, SpecialCompensationConfig scc, Map<T, List<SpecialCompensationConfig>> map) {

		List<SpecialCompensationConfig> configs = map.get(id);
		if (configs == null) {
			configs = new ArrayList<SpecialCompensationConfig>();
			configs.add(scc);
			map.put(id, configs);
		} else {
			configs.add(scc);
		}
	}

	/**
	 * 增加NPC补偿服务
	 * @param roleid
	 * @param npcid
	 * @param result
	 */
	public void addDynamicServices(long roleid, int npcid, ArrayList<Integer> result) {

		if (NPC_IDS.contains(Integer.valueOf(npcid))) {

			long curTime = System.currentTimeMillis();

			xbean.Compensation compensation = xtable.Compensation.select(roleid);

			// 遍历补偿配置
			for (List<CompensationConfig> configList : configSets) {
				//
				for (CompensationConfig config : configList) {
					// 检查时间
					if (config.isInCompensationTime(curTime)) {
						// 检查条件
						if (checkCondition(config.getCondtions(), roleid)) {
							// 检查服务器
							if (checkZoneids(config.getZoneids(), ConfigManager.getGsZoneId())) {
								// 是否已领取过
								if (compensation == null || !alreadyReceiveAward(compensation, config)) {
									// 检查渠道
									if (checkPlats(config, roleid)) {
										result.add(config.getCompensationid());
									}
								}
							}
						}
					}
				}
			}

			// 给角色ID的补偿
			List<SpecialCompensationConfig> configs = specialMaps.get(roleid);
			if (configs != null) {
				// 遍历
				for (SpecialCompensationConfig scc : configs) {
					// 检查时间
					if (scc.isInTime(curTime)) {
						// 检查条件
						if (checkCondition(scc.getCondtions(), roleid)) {
							// 检查服务器
							if (checkZoneids(scc.getZoneids(), ConfigManager.getGsZoneId())) {
								// 是否已领取过
								if (compensation == null || !alreadyReceiveAward(compensation, scc)) {
									result.add(scc.getId());
								}
							}
						}
					}
				}
			}

			// 给USERID的补偿
			int userId = xtable.Properties.selectUserid(roleid);
			List<SpecialCompensationConfig> confs = useridSpecialMaps.get(userId);
			if (confs != null) {
				// 遍历
				for (SpecialCompensationConfig scc : confs) {
					// 检查时间
					if (scc.isInTime(curTime)) {
						// 检查条件
						if (checkCondition(scc.getCondtions(), roleid)) {
							// 检查服务器
							if (checkZoneids(scc.getZoneids(), ConfigManager.getGsZoneId())) {
								//
								xbean.Compensation usercompensation = xtable.Usercompensation.select(String.valueOf(userId));
								if (usercompensation == null) {
									result.add(scc.getId());
								} else {
									xbean.CompensationInfo compensationInfo = usercompensation.getCompensationmap().get(scc.getId());
									// 没有领过才加服务
									if (compensationInfo == null || !scc.isInTime(compensationInfo.getReceiveawardtime()))
										result.add(scc.getId());
								}
							}
						}
					}
				}
			}

			// 补填账号的奖励
			PCheckUserAccountInfo.addDynamicService(roleid, result);
		}
	}

	/**
	 * 是否已经领取
	 * @param compensation
	 * @param scc
	 * @return
	 */
	public static boolean alreadyReceiveAward(Compensation compensation, SpecialCompensationConfig scc) {

		xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(scc.getId());

		// 如果已经领过了
		if (compensationInfo != null && scc.isInTime(compensationInfo.getReceiveawardtime())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否已经领取
	 * @param compensation
	 * @param config
	 * @return
	 */
	public static boolean alreadyReceiveAward(Compensation compensation, CompensationConfig config) {

		xbean.CompensationInfo compensationInfo = compensation.getCompensationmap().get(config.getCompensationid());

		// 如果已经领过了
		if (compensationInfo != null && config.isInCompensationTime(compensationInfo.getReceiveawardtime())) {
			return true;
		}
		return false;
	}

	public void addCompensationConfig(CompensationConfig config) {

		int compensationid = config.getCompensationid();
		List<CompensationConfig> configs = configMaps.get(compensationid);
		if (configs == null) {
			configs = new ArrayList<CompensationConfig>();
			configs.add(config);
			configMaps.put(compensationid, configs);
		} else {
			configs.add(config);
		}
	}

	public void setConfigSet() {
		configSets = configMaps.values();
	}

	/**
	 * 获得指定时间的补偿
	 * @param roleid
	 * @param time
	 * @return
	 */
	public List<CompensationConfig> getCompensationConfigsInTime(long roleid, long time) {

		List<CompensationConfig> result = new ArrayList<CompensationConfig>();
		for (List<CompensationConfig> configList : configSets) {
			for (CompensationConfig config : configList) {
				// 检查时间
				if (config.isInCompensationTime(time)) {
					// 检查条件
					if (checkCondition(config.getCondtions(), roleid)) {
						// 检查服务器
						if (checkZoneids(config.getZoneids(), ConfigManager.getGsZoneId())) {
							// 检查渠道
							if (checkPlats(config, roleid)) {
								result.add(config);
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获得角色的补偿
	 * @param time
	 * @param roleid
	 * @return
	 */
	public List<SpecialCompensationConfig> getCompensationConfigsInTimeAndAvailable(long time, long roleid) {

		List<SpecialCompensationConfig> result = new ArrayList<SpecialCompensationConfig>();
		List<SpecialCompensationConfig> configList = specialMaps.get(roleid);
		if (configList != null && !configList.isEmpty()) {
			for (SpecialCompensationConfig config : configList) {
				// 检查时间
				if (config.isInTime(time)) {
					// 检查条件
					if (checkCondition(config.getCondtions(), roleid))
						// 检查服务器
						if (checkZoneids(config.getZoneids(), ConfigManager.getGsZoneId())) {
							result.add(config);
						}
				}
			}
		}
		return result;
	}

	/**
	 * 获得UserId的补偿
	 * @param roleid
	 * @param time
	 * @param platformUid
	 * @return
	 */
	public List<SpecialCompensationConfig> getUseridCompensationConfigsInTimeAndAvailable(long roleid, long time, String platformUid) {

		List<SpecialCompensationConfig> result = new ArrayList<SpecialCompensationConfig>();
		List<SpecialCompensationConfig> configList = useridSpecialMaps.get(Integer.valueOf(platformUid));
		if (configList != null && !configList.isEmpty()) {
			for (SpecialCompensationConfig config : configList) {
				// 检查时间
				if (config.isInTime(time)) {
					// 检查条件
					if (checkCondition(config.getCondtions(), roleid)) {
						// 检查服务器
						if (checkZoneids(config.getZoneids(), ConfigManager.getGsZoneId())) {
							result.add(config);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获得符合服务ID的补偿
	 * @param roleid
	 * @param serviceid
	 * @return
	 */
	public List<CompensationConfig> getConfigFromServiceid(long roleid, int serviceid) {

		List<CompensationConfig> result = new ArrayList<CompensationConfig>();
		List<CompensationConfig> configs = configMaps.get(serviceid);
		if (configs != null) {
			for (CompensationConfig compensationConfig : configs) {
				// 检查条件
				if (checkCondition(compensationConfig.getCondtions(), roleid)) {
					// 检查服务器
					if (checkZoneids(compensationConfig.getZoneids(), ConfigManager.getGsZoneId())) {
						// 检查渠道
						if (checkPlats(compensationConfig, roleid)) {
							result.add(compensationConfig);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 检查渠道
	 * @param compensationConfig
	 * @param roleid
	 * @return
	 */
	public boolean checkPlats(CompensationConfig compensationConfig, long roleid) {

		if (compensationConfig.getPlats().equals("0") && compensationConfig.getNonPlats().equals("0"))
			return true;

		// 获得角色渠道
		xbean.Properties properties = xtable.Properties.select(roleid);
		String platUid = properties.getPlatformuid();
		String platName = platUid.substring(platUid.indexOf("$") + 1);
		if (platName.length() != 4)
			return false;

		if (compensationConfig.getPlats().length() > 1) {
			if (compensationConfig.getPlats().contains(platName)) {
				return true;
			} else {
				return false;
			}
		} else if (compensationConfig.getNonPlats().length() > 1) {
			if (compensationConfig.getNonPlats().contains(platName)) {
				return false;
			} else {
				return true;
			}
		}

		return false;
	}

	/**
	 * 检查服务器
	 * @param zoneids
	 * @param gsZoneId
	 * @return
	 */
	public boolean checkZoneids(List<Integer> zoneids, int gsZoneId) {

		if (zoneids == null || zoneids.isEmpty()) {
			return true;
		} else {
			return zoneids.contains(gsZoneId);
		}
	}
	
	/**
	 * 获得符合服务ID和角色ID的补偿
	 * @param serviceid
	 * @param roleid
	 * @return
	 */
	public List<SpecialCompensationConfig> getSpecialConfigFromServiceid(int serviceid, long roleid) {

		List<SpecialCompensationConfig> result = new ArrayList<SpecialCompensationConfig>();
		List<SpecialCompensationConfig> configs = specialMaps.get(roleid);
		if (configs != null) {
			for (SpecialCompensationConfig scc : configs) {
				// 服务ID
				if (scc.getId() == serviceid) {
					// 检查条件
					if (checkCondition(scc.getCondtions(), roleid)) {
						// 检查服务器
						if (checkZoneids(scc.getZoneids(), ConfigManager.getGsZoneId())) {
							result.add(scc);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获得符合服务ID和UserID的补偿
	 * @param roleid
	 * @param serviceid
	 * @param platformUid
	 * @return
	 */
	public List<SpecialCompensationConfig> getUseridSpecialConfigFromServiceid(long roleid, int serviceid, String platformUid) {

		List<SpecialCompensationConfig> result = new ArrayList<SpecialCompensationConfig>();
		List<SpecialCompensationConfig> configs = useridSpecialMaps.get(Integer.parseInt(platformUid));
		if (configs != null) {
			for (SpecialCompensationConfig scc : configs) {
				// 服务ID
				if (scc.getId() == serviceid) {
					// 检查条件
					if (checkCondition(scc.getCondtions(), roleid)) {
						// 检查服务器
						if (checkZoneids(scc.getZoneids(), ConfigManager.getGsZoneId())) {
							result.add(scc);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获得RoleId补偿
	 * @return
	 */
	public static Map<Long, List<SpecialCompensationConfig>> getSpecialCompensationConfMap() {
		return specialMaps;
	}

	/**
	 * 获得UserId补偿
	 * @return
	 */
	public static Map<Integer, List<SpecialCompensationConfig>> getUseridSpecialMaps() {
		return useridSpecialMaps;
	}

	/**
	 * 检查条件
	 * @param conditions
	 * @param roleid
	 * @return
	 */
	public boolean checkCondition(List<CompensationCondition> conditions, long roleid) {

		if (conditions == null || conditions.isEmpty())
			return true;

		for (CompensationCondition cond : conditions) {
			Condition icc = CompensationConditionFactory.getCondition(cond.getConditionid());
			if (icc == null)
				return false;
			if (!icc.CheckCond(roleid, cond.getArgs()[0], cond.getArgs()[1]))
				return false;
		}
		return true;
	}
}
