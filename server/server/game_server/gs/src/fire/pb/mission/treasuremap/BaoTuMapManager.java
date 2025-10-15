package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.PropRole;
import fire.pb.item.ItemShuXing;
import fire.pb.main.ConfigManager;
import fire.pb.map.EventTimerNpc;
import fire.pb.map.FindTreasureEvent;
import fire.pb.map.FindTreasureGift;
import fire.pb.map.FindTreasureResult;
import fire.pb.map.MapConfig;
import fire.pb.map.SEventTimerNpc;
import fire.pb.map.SEventTimerNpcDianKa;
import fire.pb.map.SFindTreasureEvent;
import fire.pb.map.SFindTreasureEventDianKa;
import fire.pb.map.SFindTreasureGift;
import fire.pb.map.SFindTreasureGiftDianKa;
import fire.pb.map.SFindTreasureResult;
import fire.pb.map.SFindTreasureResultDianKa;
import fire.pb.mission.instance.LevelGroupAward;

public class BaoTuMapManager {

	private BaoTuMapManager() {
		
	}
	
	public static BaoTuMapManager getInstance() {
		if (_Instance == null)
			_Instance = new BaoTuMapManager();
		
		return _Instance;
	}
	
	public void Init() {
		//初始awardMap
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {
			mapGift.putAll(ConfigManager.getInstance().getConf(SFindTreasureGift.class));
			mapEvent.putAll(ConfigManager.getInstance().getConf(SFindTreasureEvent.class));
			mapResult.putAll(ConfigManager.getInstance().getConf(SFindTreasureResult.class));
		}
		else {
			mapGift.putAll(ConfigManager.getInstance().getConf(SFindTreasureGiftDianKa.class));
			mapEvent.putAll(ConfigManager.getInstance().getConf(SFindTreasureEventDianKa.class));
			mapResult.putAll(ConfigManager.getInstance().getConf(SFindTreasureResultDianKa.class));
		}
		
		List<GiftByMap> listGift = null;
		for (FindTreasureGift gift : mapGift.values()) {
			GiftByMap giftMap = new GiftByMap();
			
			listGift = AWARD_Map.get(gift.awardId);
			if (listGift == null) {
				listGift = new ArrayList<GiftByMap>();
				AWARD_Map.put(gift.getAwardId(), listGift);
			}
			
			giftMap.mapId = gift.mapId;
			
			if (gift.itemList != null) {
				String[] str = gift.itemList.split(";");
				for (String item : str) {
					String[] strTmp = item.split("@");
					giftMap.giftId.add(Integer.parseInt(strTmp[0]));
					giftMap.giftRate.add(Integer.parseInt(strTmp[1]));
				}
			}
			
			if (gift.noticeItemList != null) {
				String[] str = gift.noticeItemList.split(";");
				for (String item : str) {
					giftMap.noticeItemId.add(Integer.parseInt(item));
				}
			}
			
			if (gift.awardids != null) {
				for (String awardStr : gift.awardids.split(";")) {
					String [] awardSub = awardStr.split(",");
					LevelGroupAward group = new LevelGroupAward(gift.mapId, Integer.parseInt(awardSub[0]), 
							Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
					giftMap.groupAward.add(group);
				}
			}
			
			giftMap.noticeId = gift.noticeId;
			giftMap.moneyAward = gift.moneyAwardId;
			giftMap.noticeGetItemId = gift.getItemMessageId;
			giftMap.noticeGetMoneyId = gift.getMoneyMessageId;
			
			giftMap.id = gift.getId();
			listGift.add(giftMap);
		}
		
		//初始awardEventMap
		for (FindTreasureEvent event : mapEvent.values()) {
			GiftByEvent giftEvent = new GiftByEvent();
			giftEvent.id = event.getId();
			giftEvent.name = event.getName();
			giftEvent.type = event.getType();
			giftEvent.group = event.getGroup();
			giftEvent.battleId = event.getBattleId();
			giftEvent.skillId = event.getSkillId();
			giftEvent.messageId = event.getMessageId();
			giftEvent.noticeId = event.getNoticeId();
			
			if (event.getBattleAward() != null) {
				String [] str = event.getBattleAward().split(";");
				for (String award : str) {
					String[] strTmp = award.split("@");
					giftEvent.battleAwardId.add(Integer.parseInt(strTmp[0]));
					giftEvent.battleAwardRate.add(Integer.parseInt(strTmp[1]));
				}
			}
			
			AWARD_EVENT_Map.put(event.getId(), giftEvent);
		}
		
		//初始化awardResultMap
		for (FindTreasureResult result : mapResult.values()) {
			List<GiftResult> giftResultList = AWARD_RESULT_Map.get(result.itemId);
			if (giftResultList == null) {
				giftResultList = new ArrayList<GiftResult>();
				AWARD_RESULT_Map.put(result.itemId, giftResultList);
			}
			
			GiftResult giftResult =  new GiftResult();
			giftResult.id = result.id;
			giftResult.itemId = result.itemId;
			giftResult.levelgroup = result.group;
			giftResult.minlevel = result.minlevel;
			giftResult.maxlevel = result.maxlevel;
			
			if (result.getAwardId() != null) {
				String [] str = result.getAwardId().split(";");
				for (String award : str) {
					String [] strTmp = award.split("@");
					giftResult.awardId.add(Integer.parseInt(strTmp[0]));
					giftResult.awardRate.add(Integer.parseInt(strTmp[1]));
				}
			}
			
			giftResultList.add(giftResult);
		}
		
		//初始化eventTimerData
		int nGroupId = 0;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			for (SEventTimerNpc timerNpc : mapEventTimerNpc.values()) {
				EventTimerNpcData data = EVENT_TIMER_Map.get(timerNpc.getRefreshGroup());
				if (data == null) {
					data = new EventTimerNpcData(timerNpc);
					EVENT_TIMER_Map.put(timerNpc.getRefreshGroup(), data);
				}
				data.AddTimerGroupData(timerNpc);
				
				if (timerNpc.getAwardids() != null) {
					String [] awardLst = timerNpc.getAwardids().split(";");
					List<LevelGroupAward> groupAward = new ArrayList<LevelGroupAward>();
					for (String awardStr : awardLst) {
						String [] awardSub = awardStr.split(",");
						LevelGroupAward group = new LevelGroupAward(nGroupId, Integer.parseInt(awardSub[0]), 
								Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
						groupAward.add(group);
					}
					
					mapEventGroupAward.put(timerNpc.refreshGroup, groupAward);
				}
			}
		}
		else { //点卡服
			for (SEventTimerNpcDianKa timerNpc : mapEventTimerNpcDianKa.values()) {
				EventTimerNpcData data = EVENT_TIMER_Map.get(timerNpc.getRefreshGroup());
				if (data == null) {
					data = new EventTimerNpcData(timerNpc);
					EVENT_TIMER_Map.put(timerNpc.getRefreshGroup(), data);
				}
				data.AddTimerGroupData(timerNpc);
				
				if (timerNpc.getAwardids() != null) {
					String [] awardLst = timerNpc.getAwardids().split(";");
					List<LevelGroupAward> groupAward = new ArrayList<LevelGroupAward>();
					for (String awardStr : awardLst) {
						String [] awardSub = awardStr.split(",");
						LevelGroupAward group = new LevelGroupAward(nGroupId, Integer.parseInt(awardSub[0]), 
								Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
						groupAward.add(group);
					}
					
					mapEventGroupAward.put(timerNpc.refreshGroup, groupAward);
				}
			}
		}
	}
	
	
	public GiftResult getGiftResult(int itemId, int level) {
		List<GiftResult> giftResultList = AWARD_RESULT_Map.get(itemId);
		if (giftResultList != null) {
			for (GiftResult giftResult : giftResultList) {
				if (level >= giftResult.minlevel && level <= giftResult.maxlevel) {
					return giftResult;
				}
			}
		}
		
		return null;
	}
	
	
	public List<GiftByMap> getGiftMap(int awardId) {
		if (AWARD_Map.containsKey(awardId)) {
			return AWARD_Map.get(awardId);
		}
		
		return null;
	}
	
	
	public GiftByEvent getEventGift(int eventId) {
		if (AWARD_EVENT_Map.containsKey(eventId)) {
			return AWARD_EVENT_Map.get(eventId);
		}
		
		return null;
	}
	
	public EventTimerNpcData getEventTimerNpcData(int groupId) {
		if (EVENT_TIMER_Map.containsKey(groupId)) {
			return EVENT_TIMER_Map.get(groupId);
		}
		
		return null;
	}
	
	public EventTimerGroupData getEventTimerGroupData(EventTimerNpcData npcData, long roleId) {
		PropRole pRole = new PropRole(roleId, true);
		for (EventTimerGroupData eventGroupData : npcData.groupData) {
			if (pRole.getLevel() <= eventGroupData.levelMax && pRole.getLevel() >= eventGroupData.levelMin) {
				return eventGroupData;
			}
		}
		
		return null;
	}
	
	public EventTimerNpc getTimerNpc(int eventId) {
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			if (!mapEventTimerNpc.containsKey(eventId))
				return null;
			
			return mapEventTimerNpc.get(eventId);
		}
		else {
			if (!mapEventTimerNpcDianKa.containsKey(eventId))
				return null;
			
			return mapEventTimerNpcDianKa.get(eventId);
		}
	}
	
	public List<LevelGroupAward> getAwardGroup(int eventId) {
		if (!mapEventGroupAward.containsKey(eventId))
			return null;
		
		return mapEventGroupAward.get(eventId);
	}
	
	
	public List<String> getItemNotice(final int itemId, final long roleId) {
		List<String> param = new ArrayList<String>();
		
		PropRole pRole = new PropRole(roleId, true);
		param.add(pRole.getName());
		ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
		if (attr == null) {
			return param;
		}
		
		param.add("1");
		String unit=attr.getUnit();
		param.add(unit);
		String name=attr.getName();
		param.add(name);
		
		return param;
	}
	
	
	public List<String> getMapNotice(final List<Integer> mapList) {
		List<String> param = new ArrayList<String>();
		StringBuffer strParam = new StringBuffer();
		int nIndex = 0;
		for (Integer mapId : mapList) {
			MapConfig conf =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(mapId);
			if (conf != null) {
				if (nIndex != (mapList.size() - 1)) {
					strParam.append(conf.getMapName()).append(",");
				}
				else {
					strParam.append(conf.getMapName());
				}
				nIndex ++;
			}
		}
		param.add(strParam.toString());
		
		return param;
	}
	
	private static BaoTuMapManager _Instance;
	
	public Map<Integer, FindTreasureGift> mapGift = new HashMap<Integer, FindTreasureGift>();
	public Map<Integer, FindTreasureEvent> mapEvent = new HashMap<Integer, FindTreasureEvent>();
	public Map<Integer, FindTreasureResult> mapResult = new HashMap<Integer, FindTreasureResult>();
	
	public Map<Integer, SEventTimerNpc> mapEventTimerNpc = ConfigManager.getInstance().getConf(SEventTimerNpc.class);
	
	public Map<Integer, SEventTimerNpcDianKa> mapEventTimerNpcDianKa = ConfigManager.getInstance().getConf(SEventTimerNpcDianKa.class);
	
	//Key 奖励id, value GiftByMap的list
	public Map<Integer, List<GiftByMap>> AWARD_Map = new HashMap<Integer, List<GiftByMap>>();
	//Key 事件Id, value GiftByEvent
	public Map<Integer, GiftByEvent> AWARD_EVENT_Map = new HashMap<Integer, GiftByEvent>();
	//Key 编号Id, value GiftResult; 根据itemid从这张表里随机出奖励id,根据这些id在AWARD_Map和AWARD_EVENT_Map里查找奖励结果
	public Map<Integer, List<GiftResult>> AWARD_RESULT_Map = new HashMap<Integer, List<GiftResult>>();
	//Key 编号Id, value EventTimerNpcData;
	public Map<Integer, EventTimerNpcData> EVENT_TIMER_Map = new HashMap<Integer, EventTimerNpcData>();
	public Map<Integer, List<LevelGroupAward>> mapEventGroupAward = new HashMap<Integer, List<LevelGroupAward>>();
	
}
