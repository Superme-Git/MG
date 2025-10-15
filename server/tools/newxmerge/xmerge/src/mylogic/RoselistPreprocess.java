package mylogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;
import xmerge.IPreprocess;

import com.locojoy.base.Marshal.OctetsStream;

public class RoselistPreprocess implements IPreprocess {
	public final static int TODAY_FLOWER_RANK = 3; // 今日鲜花榜
	public final static int YESTERDAY_FLOWER_RANK = 4; // 昨日鲜花榜
	public final static int LASTWEEK_FLOWER_RANK = 33; // 上周鲜花榜
	public final static int HISTORY_FLOWER_RANK = 5; // 历史鲜花榜
	public final static int MENG_HUAN_BABY = 40;  //梦幻宝贝
	public final static int REN_QI_BABY = 41;  //人气宝贝
	public final static int MEI_LI_BABY = 42;  //魅力宝贝
	public final static int XIAN_HUA_BABY_1 = 43;  //鲜花宝贝
	public final static int XIAN_HUA_BABY_2_3 = 212;  //鲜花宝贝
	public final static int XIAN_HUA_BABY_4_10 = 213;  //鲜花宝贝
	@Override
	public boolean preProcess(Dbx srcdb, Table src_table, Dbx dstdb,
			Table dst_table) {
		mkdb.Trace.info("RoselistPreprocess start");
		Dbx.Table srcRoleListTable = srcdb.openTable("roselist");
		Dbx.Table dstRoleListTable = dstdb.openTable("roselist");
		
		Map<Integer,ArrayList<Long>> thisIsIWant = new HashMap<Integer,ArrayList<Long>>();  //key: titleid ;   value: roleids
		
		/////////////////////这里获取到历史鲜花榜鲜花榜排名，保存那些有鲜花榜称谓的////////////////////////
//		try {
//			OctetsStream srcOs = srcRoleListTable.find(new OctetsStream().marshal(HISTORY_FLOWER_RANK));
//			xbean.RoseRankList srcRoseRankList = xbean.Pod.newRoseRankListData();
//			if(srcOs != null)
//				srcRoseRankList.unmarshal(srcOs);
//			OctetsStream dstOs = dstRoleListTable.find(new OctetsStream().marshal(HISTORY_FLOWER_RANK));
//			xbean.RoseRankList dstRoseRankList = xbean.Pod.newRoseRankListData();
//			if(dstOs != null)
//				dstRoseRankList.unmarshal(dstOs);
//			List<xbean.RoseListRecord> srcRoseRecordList = srcRoseRankList.getRecords();
//			List<xbean.RoseListRecord> dstRoseRecordList = dstRoseRankList.getRecords();
//			
//			ArrayList<Long> historyTop1Roleids = new ArrayList<Long>();  //历史榜第1名的角色，即人气宝贝
//			if(!srcRoseRecordList.isEmpty())
//				historyTop1Roleids.add(srcRoseRecordList.get(0).getMarshaldata().getRoleid());
//			if(!dstRoseRecordList.isEmpty())
//				historyTop1Roleids.add(dstRoseRecordList.get(0).getMarshaldata().getRoleid());
//			thisIsIWant.put(REN_QI_BABY, historyTop1Roleids);
//			
//			int srcListSize = srcRoseRecordList.size();
//			int dstListSize = dstRoseRecordList.size();
//			
//			ArrayList<Long> history2To3Roles = new ArrayList<Long>();   //历史榜第2~3名，即魅力宝贝
//			if(srcListSize >= 2)
//				history2To3Roles.add(srcRoseRecordList.get(1).getMarshaldata().getRoleid());
//			if(srcListSize >= 3)
//				history2To3Roles.add(srcRoseRecordList.get(2).getMarshaldata().getRoleid());
//			if(dstListSize >= 2)
//				history2To3Roles.add(dstRoseRecordList.get(1).getMarshaldata().getRoleid());
//			if(dstListSize >= 3)
//				history2To3Roles.add(dstRoseRecordList.get(2).getMarshaldata().getRoleid());
//			thisIsIWant.put(MEI_LI_BABY, history2To3Roles);
//			
//		} catch (Exception e) {
//			mkdb.Trace.info("RoselistPreprocess history record error");
//			e.printStackTrace();
//		}
//		
//		
//		// ///////////////////这里获取到周鲜花榜鲜花榜排名，保存那些有鲜花榜称谓的////////////////////////
//		try {
//			OctetsStream srcOs = srcRoleListTable.find(new OctetsStream().marshal(LASTWEEK_FLOWER_RANK));
//			xbean.RoseRankList srcRoseRankList = xbean.Pod.newRoseRankListData();
//			if (srcOs != null)
//				srcRoseRankList.unmarshal(srcOs);
//			OctetsStream dstOs = dstRoleListTable.find(new OctetsStream().marshal(LASTWEEK_FLOWER_RANK));
//			xbean.RoseRankList dstRoseRankList = xbean.Pod.newRoseRankListData();
//			if (dstOs != null)
//				dstRoseRankList.unmarshal(dstOs);
//			List<xbean.RoseListRecord> srcRoseRecordList = srcRoseRankList.getRecords();
//			List<xbean.RoseListRecord> dstRoseRecordList = dstRoseRankList.getRecords();
//
//			ArrayList<Long> weekTop1Roleids = new ArrayList<Long>(); // 周榜第1名的角色
//			if (!srcRoseRecordList.isEmpty())
//				weekTop1Roleids.add(srcRoseRecordList.get(0).getMarshaldata().getRoleid());
//			if (!dstRoseRecordList.isEmpty())
//				weekTop1Roleids.add(dstRoseRecordList.get(0).getMarshaldata().getRoleid());
//			thisIsIWant.put(XIAN_HUA_BABY_1, weekTop1Roleids);
//
//			int srcListSize = srcRoseRecordList.size();
//			int dstListSize = dstRoseRecordList.size();
//
//			ArrayList<Long> week2To3Roles = new ArrayList<Long>(); // 周榜第2~3名
//			if (srcListSize >= 2)
//				week2To3Roles.add(srcRoseRecordList.get(1).getMarshaldata().getRoleid());
//			if (srcListSize >= 3)
//				week2To3Roles.add(srcRoseRecordList.get(2).getMarshaldata().getRoleid());
//			if (dstListSize >= 2)
//				week2To3Roles.add(dstRoseRecordList.get(1).getMarshaldata().getRoleid());
//			if (dstListSize >= 3)
//				week2To3Roles.add(dstRoseRecordList.get(2).getMarshaldata().getRoleid());
//			thisIsIWant.put(XIAN_HUA_BABY_2_3, week2To3Roles);
//			
//			ArrayList<Long> week4To10Roles = new ArrayList<Long>();   //周榜第4~10名
//			int min1 = Math.min(srcListSize, 10);
//			int min2 = Math.min(dstListSize, 10);
//			if(srcListSize >= 4)
//				for(int i = 3; i < min1 ; i++)
//					week4To10Roles.add(srcRoseRecordList.get(i).getMarshaldata().getRoleid());
//			if(dstListSize >= 4)
//				for(int i = 3; i < min2 ; i++)
//					week4To10Roles.add(dstRoseRecordList.get(i).getMarshaldata().getRoleid());
//			thisIsIWant.put(XIAN_HUA_BABY_4_10, week4To10Roles);
//
//		} catch (Exception e) {
//			mkdb.Trace.info("RoselistPreprocess week record error");
//			e.printStackTrace();
//		}
//		
//		//////////////////////以下为获取到昨日鲜花榜，并把梦幻宝贝的roleid保存起来////////////////////////
//		try {
//			OctetsStream srcOs = srcRoleListTable.find(new OctetsStream().marshal(YESTERDAY_FLOWER_RANK));
//			xbean.RoseRankList srcRoseRankList = xbean.Pod.newRoseRankListData();
//			if(srcOs != null)
//				srcRoseRankList.unmarshal(srcOs);
//			OctetsStream dstOs = dstRoleListTable.find(new OctetsStream().marshal(YESTERDAY_FLOWER_RANK));
//			xbean.RoseRankList dstRoseRankList = xbean.Pod.newRoseRankListData();
//			if(dstOs != null)
//				dstRoseRankList.unmarshal(dstOs);
//			List<xbean.RoseListRecord> srcRoseRecordList = srcRoseRankList.getRecords();
//			List<xbean.RoseListRecord> dstRoseRecordList = dstRoseRankList.getRecords();
//			
//			ArrayList<Long> yesterdayTop1Roleids = new ArrayList<Long>();  //昨日榜第1名的角色，即梦幻宝贝
//			if(!srcRoseRecordList.isEmpty())
//				yesterdayTop1Roleids.add(srcRoseRecordList.get(0).getMarshaldata().getRoleid());
//			if(!dstRoseRecordList.isEmpty())
//				yesterdayTop1Roleids.add(dstRoseRecordList.get(0).getMarshaldata().getRoleid());
//			thisIsIWant.put(MENG_HUAN_BABY, yesterdayTop1Roleids);
//		} catch (Exception e) {
//			mkdb.Trace.info("RoselistPreprocess yesterday record error");
//			e.printStackTrace();
//		}
//		
//		////////////////////////以下部分为开始保存第二天需要清除的titleid及对应的roleids////////////////////////////
//		try {
//			Dbx.Table srcTable = srcdb.openTable("roserankingtitles");
//			Dbx.Table dstTable = dstdb.openTable("roserankingtitles");
//			for(Map.Entry<Integer, ArrayList<Long>> entry : thisIsIWant.entrySet()){
//				final int titleId = entry.getKey();
//				final ArrayList<Long> roleids = entry.getValue();
//				
//				xbean.RankingTitleInfo srcRankingTitleInfo = xbean.Pod.newRankingTitleInfoData();
//				xbean.RankingTitleInfo dstRankingTitleInfo = xbean.Pod.newRankingTitleInfoData();
//				
//				/*
//				 * 这样做是防止合服多次出问题。例如 四组服务器A,B,C,D
//				 *   首先A,B-->A
//				 *   再C,D-->C
//				 *   这个时候若A,C再合服，如果不像底下这样处理的话，将仍旧会有一些得到了
//				 *   鲜花榜称谓的角色不能保存，不能在当天晚上23:59分清除称谓，也就是称谓将会一直在。
//				 */
//				OctetsStream srcOs = srcTable.find(new OctetsStream().marshal(titleId));
//				OctetsStream dstOs = dstTable.find(new OctetsStream().marshal(titleId));
//				if(srcOs != null) srcRankingTitleInfo.unmarshal(srcOs);
//				if(dstOs != null) dstRankingTitleInfo.unmarshal(dstOs);
//				
//				if(srcOs != null){
//					dstRankingTitleInfo.getTitleroles().addAll(srcRankingTitleInfo.getTitleroles());
//					srcTable.remove(new OctetsStream().marshal(titleId));
//				}
//				if(dstOs != null){
//					srcRankingTitleInfo.getTitleroles().addAll(dstRankingTitleInfo.getTitleroles());
//					dstTable.remove(new OctetsStream().marshal(titleId));
//				}
//				
//				srcRankingTitleInfo.getTitleroles().addAll(roleids);
//				dstRankingTitleInfo.getTitleroles().addAll(roleids);
//				
//				srcTable.insert(new OctetsStream().marshal(titleId), srcRankingTitleInfo.marshal(new OctetsStream()));
//				dstTable.insert(new OctetsStream().marshal(titleId), dstRankingTitleInfo.marshal(new OctetsStream()));
//			}
//		} catch (Exception e) {
//			mkdb.Trace.info("RoselistPreprocess save error");
//			e.printStackTrace();
//		}
		
		return true;
	}

}
