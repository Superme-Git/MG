package fire.pb.timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import fire.pb.clan.PRequestImpeachMent;
import fire.pb.util.DateUtil;

/**
 * 刷新公会药房物品
 *
 */
public class ClanYaofangHourTask  extends TimerTask{
	//公会药房每日刷新5次，分别为7、11、15、19、23整点刷新
	public static int REFRESH_TIME[]={7,11,15,19,23};
	
	public static int yaofangRefresh=-1;//上次tick时间点
	
	public static long ticknum=0;//心跳次数用于药房
	
	@Override
	public void run() {
		if(ticknum%10==0){
			tickYaofangRefresh();
		}
		ticknum++;
		
		refreshImpeachMent();//处理弹劾心跳
	}
	
	
	public void tickYaofangRefresh(){
		if(yaofangRefresh==-1){
			yaofangRefresh=DateUtil.getCurrentHour();
		}
		for(int i=0;i<REFRESH_TIME.length;i++){
			int starttime=REFRESH_TIME[i]-1;
			int endtime=REFRESH_TIME[i];
			if(yaofangRefresh==starttime&&DateUtil.getCurrentHour()==endtime){
				//刷新
				new fire.pb.clan.PClanMedicItem().submit();
				break;
			}
		}
		yaofangRefresh=DateUtil.getCurrentHour();
	}
	
	
	/**
	 * 定时处理弹劾逻辑
	 */
	public void refreshImpeachMent(){
		List<Long> dellist=new ArrayList<Long>();
		ConcurrentHashMap<Long,Long> impeachmentclans = PRequestImpeachMent.impeachmentclans;
		long curtime=System.currentTimeMillis();
		for(Entry<Long,Long> entry:impeachmentclans.entrySet()){
			Long clankey=entry.getKey();
			Long time=entry.getValue();
			if(clankey==null || time==null){
				continue;
			}
			//处理过期的弹劾
			if(curtime-time>PRequestImpeachMent.IMPEACHMENT_TIME){
				dellist.add(clankey);
				new mkdb.Procedure() {
					@Override
					protected boolean process() throws Exception {
						//弹劾失败
						return PRequestImpeachMent.dealImpeachTimeout(clankey);
					}
				}.submit();
			}
		}
		//删除过期数据
		for(Long clankey:dellist){
			impeachmentclans.remove(clankey);
		}
	}
	
	
	
}