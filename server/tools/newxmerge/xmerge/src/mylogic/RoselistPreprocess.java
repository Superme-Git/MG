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
	public final static int TODAY_FLOWER_RANK = 3; // �����ʻ���
	public final static int YESTERDAY_FLOWER_RANK = 4; // �����ʻ���
	public final static int LASTWEEK_FLOWER_RANK = 33; // �����ʻ���
	public final static int HISTORY_FLOWER_RANK = 5; // ��ʷ�ʻ���
	public final static int MENG_HUAN_BABY = 40;  //�λñ���
	public final static int REN_QI_BABY = 41;  //��������
	public final static int MEI_LI_BABY = 42;  //��������
	public final static int XIAN_HUA_BABY_1 = 43;  //�ʻ�����
	public final static int XIAN_HUA_BABY_2_3 = 212;  //�ʻ�����
	public final static int XIAN_HUA_BABY_4_10 = 213;  //�ʻ�����
	@Override
	public boolean preProcess(Dbx srcdb, Table src_table, Dbx dstdb,
			Table dst_table) {
		mkdb.Trace.info("RoselistPreprocess start");
		Dbx.Table srcRoleListTable = srcdb.openTable("roselist");
		Dbx.Table dstRoleListTable = dstdb.openTable("roselist");
		
		Map<Integer,ArrayList<Long>> thisIsIWant = new HashMap<Integer,ArrayList<Long>>();  //key: titleid ;   value: roleids
		
		/////////////////////�����ȡ����ʷ�ʻ����ʻ���������������Щ���ʻ����ν��////////////////////////
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
//			ArrayList<Long> historyTop1Roleids = new ArrayList<Long>();  //��ʷ���1���Ľ�ɫ������������
//			if(!srcRoseRecordList.isEmpty())
//				historyTop1Roleids.add(srcRoseRecordList.get(0).getMarshaldata().getRoleid());
//			if(!dstRoseRecordList.isEmpty())
//				historyTop1Roleids.add(dstRoseRecordList.get(0).getMarshaldata().getRoleid());
//			thisIsIWant.put(REN_QI_BABY, historyTop1Roleids);
//			
//			int srcListSize = srcRoseRecordList.size();
//			int dstListSize = dstRoseRecordList.size();
//			
//			ArrayList<Long> history2To3Roles = new ArrayList<Long>();   //��ʷ���2~3��������������
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
//		// ///////////////////�����ȡ�����ʻ����ʻ���������������Щ���ʻ����ν��////////////////////////
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
//			ArrayList<Long> weekTop1Roleids = new ArrayList<Long>(); // �ܰ��1���Ľ�ɫ
//			if (!srcRoseRecordList.isEmpty())
//				weekTop1Roleids.add(srcRoseRecordList.get(0).getMarshaldata().getRoleid());
//			if (!dstRoseRecordList.isEmpty())
//				weekTop1Roleids.add(dstRoseRecordList.get(0).getMarshaldata().getRoleid());
//			thisIsIWant.put(XIAN_HUA_BABY_1, weekTop1Roleids);
//
//			int srcListSize = srcRoseRecordList.size();
//			int dstListSize = dstRoseRecordList.size();
//
//			ArrayList<Long> week2To3Roles = new ArrayList<Long>(); // �ܰ��2~3��
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
//			ArrayList<Long> week4To10Roles = new ArrayList<Long>();   //�ܰ��4~10��
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
//		//////////////////////����Ϊ��ȡ�������ʻ��񣬲����λñ�����roleid��������////////////////////////
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
//			ArrayList<Long> yesterdayTop1Roleids = new ArrayList<Long>();  //���հ��1���Ľ�ɫ�����λñ���
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
//		////////////////////////���²���Ϊ��ʼ����ڶ�����Ҫ�����titleid����Ӧ��roleids////////////////////////////
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
//				 * �������Ƿ�ֹ�Ϸ���γ����⡣���� ���������A,B,C,D
//				 *   ����A,B-->A
//				 *   ��C,D-->C
//				 *   ���ʱ����A,C�ٺϷ���������������������Ļ������Ծɻ���һЩ�õ���
//				 *   �ʻ����ν�Ľ�ɫ���ܱ��棬�����ڵ�������23:59�������ν��Ҳ���ǳ�ν����һֱ�ڡ�
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
