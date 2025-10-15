package fire.pb.npc;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import fire.pb.util.DateValidate;

import org.apache.log4j.Logger;

public class SpecialVisitProcessCreator {
	
	private static SpecialVisitProcessCreator _instance;
	
	protected static final Logger logger = Logger.getLogger("MAPMAIN");
	
	static Map<Integer, SFestivalGift> giftConfig = fire.pb.main.ConfigManager.getInstance().getConf(SFestivalGift.class);
	
	private SpecialVisitProcessCreator(){}
	
	public static SpecialVisitProcessCreator getInstance(){
		synchronized (SpecialVisitProcessCreator.class) {
			if(null == _instance){
				_instance = new SpecialVisitProcessCreator();
			}
			
			return _instance;
		}
	}
	
	public void transFestivalGift(Map<Integer, Integer> config){
		Collection<SFestivalGift> festivalList = giftConfig.values();
		for(SFestivalGift current : festivalList){
			Integer time = null;
			try {
				time = getDayByLongTime(current.getTime());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("节日礼物时间转换的时候出错：  ", e);
				continue;
			}
			config.put(time, current.getId());
		}
	}

	private Integer getDayByLongTime(String time) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(time);
		return (int) ((startDate.getTime()+DateValidate.TIME_ZONE_OFFSET)/(1000*60*60*24));
	}

	public SpecialNpcDialogProcessor createNpcDialogProcessor(long roleid, long npcKey){

		SpecialNpcDialogProcessor processor = null;
		return processor;
		
	}

}
