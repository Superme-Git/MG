
package fire.pb.ranklist.proc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.item.SRankListNumConfig;
import fire.pb.main.ConfigManager;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.SRequestRankList;
import fire.pb.ranklist.provider.IRankListData;
import fire.pb.ranklist.provider.RankDataFactory;
import fire.pb.title.STitleConfig;
import fire.pb.title.TitleManager;
import mkdb.Bean;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;

/***
 * 排行榜管理
 * @author changhao
 *
 */
public class RankListManager {
	
	private static RankListManager _instance;
	
	public final static Logger logger = Logger.getLogger("RANKLIST"); 
	public static final int PAGE_SIZE = 20;
	
	
	private static Map<Long, Long> petOnRanklistMsgTimecheck = new HashMap<Long, Long>();
	private static Object timeCheckLock = new Object();
	public boolean checkPetOnRanklistMsgTimeSpace(long roleid){
		synchronized (timeCheckLock) {
			Long lastTime = petOnRanklistMsgTimecheck.get(roleid);
			if(null == lastTime){
				petOnRanklistMsgTimecheck.put(roleid, System.currentTimeMillis());
				return true;
			}
			
			long timeSpace = System.currentTimeMillis() - lastTime;
			if(timeSpace < 1000*60*5){
				return false;
			}
			if(petOnRanklistMsgTimecheck.size() > 100){
				clearPetOnRankListTick();
			}
			petOnRanklistMsgTimecheck.put(roleid, System.currentTimeMillis());
		}
		
		return true;
	}
	
	private void clearPetOnRankListTick() {
		Iterator<Entry<Long, Long>> iterator = petOnRanklistMsgTimecheck.entrySet().iterator();
		List<Long> removeList = new ArrayList<Long>();
		while(iterator.hasNext()){
			Entry<Long, Long> current = iterator.next();
			long timeSpace = System.currentTimeMillis() - current.getValue();
			if(timeSpace > 1000*60*5){
				removeList.add(current.getKey());
			}
		}
		for(Long roleid : removeList){
			petOnRanklistMsgTimecheck.remove(roleid);
		}
	}

	private RankListManager(){}
	
	public static RankListManager getInstance(){
		 synchronized(RankListManager.class){
			 if(null == _instance){
				 _instance = new RankListManager();
			 }
		 }
		 
		 return _instance;
	}

	@SuppressWarnings("rawtypes")
	public boolean tryInsertRecord(int recordType, List listRecord, mkdb.Bean newRecord) {
		
		if(newRecord instanceof xbean.RoleLevelListRecord){
			if(recordType != RankType.LEVEL_RANK){
				return false;
			}
		}
		
		if(newRecord instanceof xbean.RoleZongheRankRecord){
			if(recordType != RankType.ROLE_ZONGHE_RANK){
				return false;
			}
		}
		
		if(newRecord instanceof xbean.RoleRankRecord){
			if(recordType != RankType.ROLE_RANK){
				return false;
			}
		}
		
		
		if(newRecord instanceof xbean.ClanRankRecord){
			if(recordType != RankType.FACTION_RANK_LEVEL){
				return false;
			}
		}
		
		if(newRecord instanceof xbean.ClanZongHeRankRecord){
			if(recordType != RankType.FACTION_ZONGHE){
				return false;
			}
		}
		
		if(newRecord instanceof xbean.ClanProgressRankRecord){
			if(recordType != RankType.FACTION_COPY) 
			{
				return false;
			}
		}
		
		int personNum = getPersonNumMaxByType(recordType);
		//尝试插入记录
		return tryInsertRoseRecord(recordType, personNum, listRecord, newRecord);
			
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean tryInsertRoseRecord(int recordType, int personNum, List listRecord, mkdb.Bean newRecord) {
		//方法的返回值为true是之前不再榜上而本次登榜了
		boolean removeMyself = false;
		boolean onRanklist = true;
		
		int rankSize = listRecord.size();
		//排行榜为空，肯定是新上榜的
		if (rankSize == 0) {
			
			if (recordType == RankType.PET_GRADE_RANK)
			{
				xbean.PetScoreListRecord r = (xbean.PetScoreListRecord)newRecord;
				if (r.getMarshaldata().getPetgrade() >= 2000)
				{
					listRecord.add(newRecord);
				}
				
				return true;
			}
			else
			{
				listRecord.add(newRecord);
				return true;
			}
		}
		
		//循环遍历查看这个记录是否之前就在榜上
		int myIndex = -1;
		for(int i=0; i<rankSize; i++){
			mkdb.Bean current = (Bean) listRecord.get(i);
			if(RankCompare.isSame(current, newRecord)){
				myIndex = i;
				break;
			}
		}
		
		mkdb.Bean oldMyRecord = null;
		//如果之前就在榜上先删除
		if(myIndex != -1)
		{
			if(recordType == RankType.FACTION_COPY )
			{
				oldMyRecord = (Bean)listRecord.get(myIndex);
			}
			
			mkdb.Bean temp = (Bean)listRecord.get(myIndex);
			if(!RankCompare.isReplace(temp, newRecord)){
				return false;
			}
			
			listRecord.remove(myIndex);
			removeMyself = true;
		}
		
		boolean del = false;
		if (recordType == RankType.PET_GRADE_RANK)
		{
			xbean.PetScoreListRecord r = (xbean.PetScoreListRecord)newRecord;
			if (r.getMarshaldata().getPetgrade() < 2000)
			{
				onRanklist = false;
				del = true;
			}
		}
		
		if (del == false)
		{
			//如果排行榜被删除空了，肯定不是新上榜的
			if(listRecord.size() == 0)
			{
				if (recordType == RankType.FACTION_COPY)
				{
					if(!RankCompare.CompareTo(newRecord, oldMyRecord))
					{
						listRecord.add(oldMyRecord);
						return false;
					}				
				}
				
				listRecord.add(newRecord);
				return false;
			}			

			for(int i = listRecord.size()-1; i>=0; i--)
			{
				mkdb.Bean befor = (Bean) listRecord.get(i);
				
				if (recordType == RankType.FACTION_COPY)
				{
					if(oldMyRecord != null && !RankCompare.CompareTo(newRecord, oldMyRecord))
					{
						newRecord = oldMyRecord;
					}					
				}
				
				if(!RankCompare.CompareTo(newRecord, befor))
				{ //当前大于老的 返回 true by changhao
					listRecord.add(i+1, newRecord); //插入他的后面 by changhao
					//可能排行榜已经满了，而这条记录也就插在最后
					if((i+2)>personNum)
					{
						onRanklist = false;
					}
					break;
				}
				else
				{
					if(i == 0)
					{
						listRecord.add(i, newRecord);
					}
					else
					{
						continue;
					}
				}
			}			
		}

		if(listRecord.size() > personNum){
			listRecord.remove(personNum);
		}
		return !removeMyself && onRanklist;
	}
	
	private static Map<Integer, SRankListNumConfig> personNumConfig = ConfigManager.getInstance().getConf(SRankListNumConfig.class);
	
	public int getPersonNumMaxByType(int type){

		SRankListNumConfig config = personNumConfig.get(type);
		if(null == config)
			return 0;
		
		return config.getPersonNum();
	}
	
	public String getNameByType(int type){
		SRankListNumConfig config = personNumConfig.get(type);
		if(null == config)
			return null;
		
		return config.getNamename();
	}

	public SRequestRankList getRankListResponse(int ranktype, long requestRole, int page) {
		SRequestRankList response = new SRequestRankList();
		response.ranktype = ranktype;
		setRankListOctets(response, ranktype, requestRole, page);
		return response;
	}

	private void setRankListOctets(SRequestRankList response, int ranktype, long requestRole, int page) {
		IRankListData provider = RankDataFactory.getInstance().createRankProvider(ranktype, requestRole);
		if(null == provider){
			response.hasmore = 1;
			return;
		}
		
		List<Octets> octets = provider.getMO(response, page);
		if(null == octets){
			response.hasmore = 1;
			return;
		}
		response.list.addAll(octets);
	}

	public String getRankTitleByRank(long roleid, int rank, int ranktype){
		
		int titleId = 0; 
		
		if(0 != titleId){
			 STitleConfig title = TitleManager.getTitleConfigById(titleId);
			 if(null == title)
				 return "----";
			 
			 return title.getTitlename();
		}
		return "----";
	}
	
	public static boolean isAddRankList(long roleid, boolean isAdd){
		xbean.Properties role = xtable.Properties.get(roleid);
		if(null == role)
			return false;
		
		role.setAddranklist(isAdd);
		return true;
	}
	
	public static boolean isPetRanklistRecord(long uniquePetId, int rankNum){
		int rankid = getPetRankidByUniqueID(uniquePetId);
		if(-1 == rankid)
			return false;
		
		if(rankid <= rankNum)
			return true;
		
		return false;
		
	}

	public static int getPetRankidByUniqueID(long uniquiPetId){
		xbean.PetScoreRankList list = xtable.Petscorelist.select(1);
		if(null == list)
			return -1;
		
		int counter = 1;
		for(xbean.PetScoreListRecord record : list.getRecords()){
			if(record.getMarshaldata().getUniquepetid() == uniquiPetId)
				return counter;
			
			counter ++;
		}
		return -1;
	}
	
	/***
	 * 得到周一第一毫秒 by changhao
	 * @return
	 */
	public static long GetMonday1Millisecond()
	{
		Calendar cal = Calendar.getInstance();
		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -6);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 1);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 5;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 1);
		}
		
		return cal.getTimeInMillis();
	}
}
