package fire.pb.npc;

import java.util.Map;

import fire.pb.util.DateValidate;

public class FestivalGiftProcessor extends SpecialNpcDialogProcessor {
	
	public static final int NONE_FESTIVAL_CHAT = 143410;
	public static final int FESTIVAL_CHAT = 143411;
	
	static Map<Integer, SFestivalGift> giftConfig = fire.pb.main.ConfigManager.getInstance().getConf(SFestivalGift.class);

	public FestivalGiftProcessor(long roleid, long npcKey) {
		super(roleid, npcKey);
	}

	@Override
	public void onVisitNpc() {
//		chatMsg = new SVisitNpcContainChatMsg();
//		int festivalId = getTodayFestivalID();
//		List<Integer> serviceList = new ArrayList<Integer>();
//		
//		if(-1 == festivalId){
//			chatMsg.msgid = NONE_FESTIVAL_CHAT;
//			serviceList.add(NpcServices.QUERY_NEXT_FESTIVAL);
//		}else{
//			if(isDrawGiftAlready()){
//				chatMsg.msgid = 143391;
//				serviceList.add(NpcServices.QUERY_NEXT_FESTIVAL);
//				serviceList.add(NpcServices.QUERY_FESTIVAL_SOURCE);
//			}else{
//				chatMsg.msgid = FESTIVAL_CHAT;
//				serviceList.add(NpcServices.DRAW_FESTIVAL_GIFT);
//			}
//		}
//		
//		chatMsg.npckey = npcKey;
//		chatMsg.services.addAll(serviceList);
		//父类里边发送协议
		super.onVisitNpc();
	}
	
	public boolean isDrawGiftAlready(){
		xbean.FestivalGift festivalGift = xtable.Festival.select(roleid);
		if(null == festivalGift)
			return false;
		
		if(!DateValidate.inTheSameDay(System.currentTimeMillis(), festivalGift.getTime())){
			return false;
		}
		
		return true;
	}
	
	public static int getTodayFestivalID(){
		int today = (int) ((System.currentTimeMillis()+DateValidate.TIME_ZONE_OFFSET)/(1000*60*60*24));
		
		Integer festivalID = Module.festivalGiftConfig.get(today);
		if(null == festivalID)
			return -1;
		
		return festivalID;
	}
	
	
}
