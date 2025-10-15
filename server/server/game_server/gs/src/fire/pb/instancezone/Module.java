package fire.pb.instancezone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.log.Logger;
import fire.pb.instance.SInstaceConfig;
import fire.pb.instance.SInstaceEvent;
import fire.pb.instance.SInstaceNpc;
import fire.pb.instance.SInstanceAct;
import fire.pb.instance.SInstanceChildSave;
import fire.pb.instance.SInstanceSave;
import fire.pb.instance.SInstanceService;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.EventConfig;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.conf.NpcConfig;
import fire.pb.instancezone.conf.NpcServiceConfig;
import fire.pb.instancezone.conf.SubSaveConfig;
import fire.pb.instancezone.event.Event;
import fire.pb.instancezone.faction.FactionInstAskQuestionTimer;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.npc.SNpcShare;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;

public class Module implements ModuleInterface {
	public final static String MODULE_NAME = "instancezone";

	public final static int ASK_QUESTION_TIMEOUT = 30;
	public final static int INSTANCE_MAP_TIMER = 10;
	public static Logger logger = Logger.getLogger("INSTANCEZONE");

	private Map<Integer, Class<? extends InstanceZone>> id2class = new java.util.HashMap<Integer, Class<? extends InstanceZone>>();

	private Map<Integer, InstanceZoneConfig> instanceZoneConfigs = new HashMap<Integer, InstanceZoneConfig>();

	public final Map<Integer, Integer> map2Instzone = new HashMap<Integer, Integer>();
	public Map<Long, Map<Long, NpcState>> dynamicNpcList = new HashMap<Long, Map<Long, NpcState>>();

	public Map<Long, Map<Long, Integer>> submitNpcItemIds = new HashMap<Long, Map<Long, Integer>>();

	public Map<Long, Boolean> instInit = new ConcurrentHashMap<Long, Boolean>();

	public Map<Long, Map<Long, ScheduledFuture<?>>> npcTimerFuture = new HashMap<Long, Map<Long, ScheduledFuture<?>>>();
	public Map<Long, ScheduledFuture<?>> instMapFuture = new ConcurrentHashMap<Long, ScheduledFuture<?>>();

	public static Module getInstance() {
		return (Module) ModuleManager.getInstance().getModuleByName(
				"instancezone");
	}

	@Override
	public void init() throws Exception {
		try {
			Map<Integer, SInstaceConfig> sInstaceConfig = ConfigManager
					.getInstance().getConf(SInstaceConfig.class);
			Map<Integer, SInstanceSave> sInstanceSave = ConfigManager
					.getInstance().getConf(SInstanceSave.class);
			Map<Integer, SInstanceChildSave> sInstanceChildSave = ConfigManager
					.getInstance().getConf(SInstanceChildSave.class);
			Map<Integer, SInstaceNpc> sInstaceNpc = ConfigManager.getInstance()
					.getConf(SInstaceNpc.class);
			Map<Integer, SInstanceService> sInstanceService = ConfigManager
					.getInstance().getConf(SInstanceService.class);
			Map<Integer, SInstanceAct> sInstanceAct = ConfigManager.getInstance()
					.getConf(SInstanceAct.class);
			Map<Integer, SInstaceEvent> sInstaceEvent = ConfigManager
					.getInstance().getConf(SInstaceEvent.class);

			Map<Integer, Class<? extends InstanceZone>> id2class = new java.util.HashMap<Integer, Class<? extends InstanceZone>>();
			Map<Integer, InstanceZoneConfig> instanceZoneConfigs = new HashMap<Integer, InstanceZoneConfig>();
			for (SInstaceConfig sFubenconfig : sInstaceConfig.values()) {
				InstanceZoneConfig instzonecfg = new InstanceZoneConfig(
						sFubenconfig);
				instanceZoneConfigs.put(instzonecfg.instZoneId, instzonecfg);
				for (int mapId : instzonecfg.maps.keySet())
					map2Instzone.put(mapId, instzonecfg.instZoneId);
				try {
					@SuppressWarnings("unchecked")
					Class<? extends InstanceZone> c = (Class<? extends InstanceZone>) Class
							.forName(instzonecfg.classname);
					id2class.put(instzonecfg.instZoneId, c);
				} catch (Exception e) {
					logger.error(e);
				}
			}
			for (SInstanceSave sFubenjindu : sInstanceSave.values()) {
				InstanceSaveConfig instsavecfg = new InstanceSaveConfig(
						sFubenjindu);
				InstanceZoneConfig instzonecfg = instanceZoneConfigs
						.get(instsavecfg.landId);
				if (instzonecfg == null) {
					throw new NullPointerException("进度属于的副本不存在，进度Id = "
							+ instsavecfg.saveId + ", 所属副本Id = "
							+ instsavecfg.landId);
				}
				instzonecfg.saves.put(instsavecfg.saveId, instsavecfg);
			}
			for (SInstanceChildSave sFubenchildrenjindu : sInstanceChildSave
					.values()) {
				SubSaveConfig subsavecfg = new SubSaveConfig(
						sFubenchildrenjindu);
				InstanceZoneConfig instzonecfg = instanceZoneConfigs
						.get(subsavecfg.belongInstzoneId);
				if (instzonecfg == null) {
					throw new NullPointerException("子进度属于的副本不存在，子进度Id = "
							+ subsavecfg.subSaveId + ", 所属副本Id = "
							+ subsavecfg.belongInstzoneId);
				}
				InstanceSaveConfig instsavecfg = instzonecfg.saves
						.get(subsavecfg.belongSaveId);
				if (instsavecfg == null) {
					throw new NullPointerException("子进度属于的进度不存在，子进度Id = "
							+ subsavecfg.subSaveId + ", 所属进度Id = "
							+ subsavecfg.belongSaveId);
				}
				instsavecfg.subsaves.put(subsavecfg.subSaveId, subsavecfg);
			}
			for (SInstaceNpc sFubennpc : sInstaceNpc.values()) {
				NpcConfig npccfg = new NpcConfig(sFubennpc);
				InstanceZoneConfig instzonecfg = instanceZoneConfigs
						.get(npccfg.landId);
				if (instzonecfg == null) {
					throw new NullPointerException("npc属于的副本不存在，定时器Id = "
							+ npccfg.npcbaseId + ", 所属副本Id = " + npccfg.landId);
				}
				instzonecfg.npcs.put(npccfg.npcbaseId, npccfg);

				if (npccfg.npctype == 1 || npccfg.npctype == 2) {
					for (Integer saveid : npccfg.saveId) {
						List<NpcConfig> npcListConfig = instzonecfg.npcDynamic
								.get(saveid);
						if (npcListConfig == null) {
							npcListConfig = new ArrayList<NpcConfig>();
							instzonecfg.npcDynamic.put(saveid, npcListConfig);
						}
						npcListConfig.add(npccfg);
					}
				}
			}
			for (SInstanceService sFubennpcserv : sInstanceService.values()) {
				NpcServiceConfig servcfg = new NpcServiceConfig(sFubennpcserv);
				InstanceZoneConfig instzonecfg = instanceZoneConfigs
						.get(servcfg.landId);
				instzonecfg.npcServices.put(servcfg.serviceId, servcfg);
			}
			for (SInstanceAct sFubenact : sInstanceAct.values()) {
				ActionConfig actcfg = new ActionConfig(sFubenact);
				InstanceZoneConfig instzonecfg = instanceZoneConfigs
						.get(actcfg.landId);
				instzonecfg.acts.put(actcfg.servActId, actcfg);
			}
			for (SInstaceEvent sFubenthing : sInstaceEvent.values()) {
				EventConfig eventcfg = new EventConfig(sFubenthing);
				InstanceZoneConfig instzonecfg = instanceZoneConfigs
						.get(eventcfg.landId);
				instzonecfg.events.put(eventcfg.eventId, eventcfg);
			}

			this.id2class = id2class;
			this.instanceZoneConfigs = instanceZoneConfigs;

			BingFengLandMgr.getInstance().init();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public ReloadResult reload() throws Exception {
		Module m = new Module();
		m.init();
		ModuleManager.getInstance().putModuleByName(MODULE_NAME, m);
		return new ReloadResult(true);
	}

	@Override
	public void exit() {
		try {
			serverShutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, Class<? extends InstanceZone>> getId2Class() {
		return id2class;
	}

	public Map<Integer, InstanceZoneConfig> getInstanceZoneConfigs() {
		return instanceZoneConfigs;
	}

	public static boolean isSubSaveCompleted(xbean.InstanceSave xsave,
			InstanceSaveConfig savecfg, int subsaveId) {
		Integer count = xsave.getSubsaves().get(subsaveId);
		if (count == null)
			return false;

		SubSaveConfig sbsavecfg = savecfg.subsaves.get(subsaveId);
		if (sbsavecfg == null)
			return false;

		if (count >= sbsavecfg.maxcount)
			return true;

		return false;
	}

	public static boolean visitNpc(long roleId, long npckey, SNpcShare npcshare) {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, true);
		if (instzone == null) {
			logger.error(new StringBuilder().append("roleid:").append(roleId).append(",npcid:").append(npcshare.id)
					.append(",Instance Is Null"));
			return false;
		}
		
		if (instzone instanceof FactionInstZone) {
			if (!Module.getInstance().checkDynamicNpcFree(instzone.getLandkey(), npckey)) {
				
				if (Module.getInstance().getBattleid(instzone.getLandkey(), npckey) > 0) {
					return instzone.sendNpcServices(roleId, npckey);
				}
				else {
					STransChatMessageNotify2Client ssmn = MessageMgr.getMsgNotify(160185, npcshare.id, null);
					gnet.link.Onlines.getInstance().send(roleId, ssmn);
					return false;
				}
			}
		}
		return instzone.sendNpcServices(roleId, npckey, npcshare.id);
	}

	public static void doNpcService(long roleId, long npckey,
			SNpcShare npcshare, int serviceId) {
		new PActNpcService(roleId, npckey, npcshare, serviceId).submit();
	}

	public static void dispatchEvent(Event event) {
	}

	private static void serverShutdown() {

		final List<Long> alllandkey = new LinkedList<Long>();
		try {
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					mkdb.TTableCache<Long, xbean.InstanceZone> btable = xtable.Instancezones
							.getCache();
					btable.walk(new mkdb.CacheQuery<Long, xbean.InstanceZone>() {
						@Override
						public void onQuery(Long landkey,
								xbean.InstanceZone xzone) {
							alllandkey.add(landkey);
						}

					});
					return true;
				};
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (final long landkey : alllandkey) {
			try {
				new mkdb.Procedure() {
					protected boolean process() throws Exception {
						InstanceZone instzone = InstanceZoneFactory
								.getInstanceZone(landkey, false);
						if (instzone == null)
							return false;
						instzone.destroy(true, true);
						return true;
					}
				}.submit().get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setDynamicNpc(final long landkey, final long npckey,
			final int state, final int multibattle) {
		synchronized (dynamicNpcList) {
			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo == null) {
				npcInfo = new HashMap<Long, NpcState>();
				dynamicNpcList.put(landkey, npcInfo);
			}
			
			NpcState npcState = new NpcState(state, multibattle);
			npcInfo.put(npckey, npcState);
			logger.info(new StringBuilder().append("add npc tolist:").append(dynamicNpcList.size()
					).append(",npcInfo size:").append(npcInfo.size()).append(",landkey:").append(landkey).append(",npckey:").append(npckey));
		}
	}

	public void removeDynamicNpc(final long landkey, final long npckey) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return;
			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo != null) {
				npcInfo.remove(npckey);
				logger.info(new StringBuilder().append("npc listsize:").append(dynamicNpcList.size())
						.append(",npcInfo size:").append(npcInfo.size()).append(",landkey:").append(landkey).append(",npckey:").append(npckey));
			}
		}
	}

	public void clearDynamicNpc(final long landkey) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return;
			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo != null) {
				npcInfo.clear();
				logger.info("npc listsize:" + dynamicNpcList.size()
						+ ",npcInfo size:" + npcInfo.size() + ",landkey:" + landkey);
			}
		}
	}

	public boolean checkDynamicNpc(final long landkey) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return false;
			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo == null)
				return false;
	
			logger.info(new StringBuilder().append("check npc list:").append(dynamicNpcList.size()
					).append(",npcInfo size:").append(npcInfo.size()).append(",landkey:").append(landkey));
			return npcInfo.size() == 0;
		}
	}

	public void setDynamicNpcState(final long landkey, final long npcKey,
			final int state) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return;

			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo != null) {
				NpcState npcState = npcInfo.get(npcKey);
				if (npcState == null) {
					String trace = fire.pb.util.Parser.convertfireStackTrace2String(Thread.currentThread().getStackTrace());
					logger.info(new StringBuilder().append("重新设置的副本怪的状态1:")
							.append(",landkey:")
							.append(landkey)
							.append(",npckey:")
							.append(npcKey)
							.append(",trace:")
							.append(trace));
					
					return;
				}
				npcState.setNpcstate(state);
				npcState.setBattleid(0L);
				
				logger.info(new StringBuilder().append("setDynamic1 npc list:").append(dynamicNpcList.size()
						).append(",npcInfo size:").append(npcInfo.size()).append(",landkey:").append(landkey).append(",npckey:").append(npcKey));
			}
		}
	}
	
	public void setDynamicNpcState(final long landkey, final long npcKey,
			final int state, final long battleid) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return;

			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo != null) {
				NpcState npcState = npcInfo.get(npcKey);
				if (npcState == null) {
					String trace = fire.pb.util.Parser.convertfireStackTrace2String(Thread.currentThread().getStackTrace());
					logger.info(new StringBuilder().append("重新设置的副本怪的状态2:")
							.append(",landkey:")
							.append(landkey)
							.append(",npckey:")
							.append(npcKey)
							.append(",trace:")
							.append(trace));
					
					return;
				}
				npcState.setNpcstate(state);
				npcState.setBattleid(battleid);
				
				logger.info(new StringBuilder().append("setDynamic2 npc list:").append(dynamicNpcList.size()
						).append(",npcInfo size:").append(npcInfo.size()).append(",landkey:").append(landkey).append(",npckey:").append(npcKey));
			}
		}
	}

	public boolean checkDynamicNpcFree(final long landkey, final long npcKey) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return false;
			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo == null)
				return false;
			NpcState npcState = npcInfo.get(npcKey);
			if (npcState == null)
				return false;
			
			if (npcState.getMultibattle() == 1)
				return true;
			
			return npcState.getNpcstate() == 0;
		}
	}
	
	/**
	 * 获取npc对应的战斗id
	 * @param landkey
	 * @param npckey
	 * @return
	 */
	public long getBattleid(final long landkey, final long npckey) {
		synchronized (dynamicNpcList) {
			if (!dynamicNpcList.containsKey(landkey))
				return 0L;
			Map<Long, NpcState> npcInfo = dynamicNpcList.get(landkey);
			if (npcInfo == null)
				return 0L;
			
			NpcState npcState = npcInfo.get(npckey);
			if (npcState == null)
				return 0L;
			
			if (npcState.getBattleid() > 0)
				return npcState.getBattleid();
			
			return 0L;
		}
	}

	public void createNpcTimerSchedule(final long landkey, final long npckey) {
		synchronized (npcTimerFuture) {
			Map<Long, ScheduledFuture<?>> taskInfo = npcTimerFuture.get(landkey);
			if (taskInfo == null) {
				taskInfo = new HashMap<Long, ScheduledFuture<?>>();
				npcTimerFuture.put(landkey, taskInfo);
			}

			ScheduledFuture<?> npcTimerTask = mkdb.Executor.getInstance().schedule(
					new FactionInstAskQuestionTimer(landkey, (Long) npckey),
					Module.ASK_QUESTION_TIMEOUT, TimeUnit.SECONDS);
			taskInfo.put(npckey, npcTimerTask);
		}
	}

	public void cancleNpcTimerSchedule(final long landkey, final long npckey) {
		synchronized (npcTimerFuture) {
			if (!npcTimerFuture.containsKey(landkey))
				return;

			Map<Long, ScheduledFuture<?>> taskInfo = npcTimerFuture.get(landkey);
			if (taskInfo == null)
				return;

			ScheduledFuture<?> npcTimerTask = taskInfo.get(npckey);
			if (npcTimerTask == null)
				return;
			
			npcTimerTask.cancel(true);
		}
	}

	public void createInstMapFuture(final long landkey,
			final ScheduledFuture<?> future) {
		if (instMapFuture.containsKey(landkey)) {
			ScheduledFuture<?> futurets = instMapFuture.get(landkey);
			futurets.cancel(true);

			instMapFuture.remove(landkey);
		}

		instMapFuture.put(landkey, future);
	}

	public void cancleInstMapFuture(final long landkey) {
		if (!instMapFuture.containsKey(landkey)) {
			return;
		}

		ScheduledFuture<?> future = instMapFuture.get(landkey);
		future.cancel(true);

		instMapFuture.remove(landkey);
	}

	public int getNpcSubmitItemId(final long landkey, final long npckey) {
		synchronized (submitNpcItemIds) {
			if (submitNpcItemIds.containsKey(landkey)) {
				Map<Long, Integer> npcInfo = submitNpcItemIds.get(landkey);
				if (npcInfo.containsKey(npckey)) {
					return npcInfo.get(npckey);
				}
			}

			return -1;
		}
	}

	public void setNpcSubmitItemId(final long landkey, final long npckey,
			final int itemid) {
		synchronized (submitNpcItemIds) {
			Map<Long, Integer> npcInfo = submitNpcItemIds.get(landkey);
			if (npcInfo == null) {
				npcInfo = new HashMap<Long, Integer>(1);
				submitNpcItemIds.put(landkey, npcInfo);
			}

			npcInfo.put(npckey, itemid);
		}
	}

	public void removeNpcSubmit(final long landkey, final long npckey) {
		synchronized (submitNpcItemIds) {
			Map<Long, Integer> npcInfo = submitNpcItemIds.get(landkey);
			if (npcInfo == null)
				return;

			npcInfo.remove(npckey);
		}
	}

	public boolean getInstInit(final long landkey) {
		if (!instInit.containsKey(landkey)) {
			return false;
		}

		return instInit.get(landkey);
	}

	public void setInstInit(final long landkey) {
		if (!instInit.containsKey(landkey)) {
			instInit.put(landkey, true);
			return;
		}

		instInit.put(landkey, true);
	}

	public void setInstInit(final long landkey, final boolean state) {
		if (!instInit.containsKey(landkey)) {
			instInit.put(landkey, state);
			return;
		}

		instInit.put(landkey, state);
	}
}
