package mylogic;

import java.util.ArrayList;
import java.util.List;

import xbean.RoleLevelListRecord;
import mkdb.Trace;
import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;
import xmerge.IPreprocess;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public class RoleLevelPreProcess implements IPreprocess {

	@Override
	public boolean preProcess(Dbx srcdb, Table src_table, Dbx dstdb, Table dst_table) {
		OctetsStream srcOs = new OctetsStream();
		OctetsStream srcValue = src_table.find(srcOs.marshal(1));
		xbean.RoleLevelRankList src_list = xbean.Pod.newRoleLevelRankListData();
		if(srcValue != null){
			try {
				src_list.unmarshal(srcValue);
			} catch (MarshalException e) {
				e.printStackTrace();
				Trace.error(e.getMessage());
			}
		}
		
		OctetsStream desOs = new OctetsStream();
		OctetsStream desValue = dst_table.find(desOs.marshal(1));
		xbean.RoleLevelRankList des_list = xbean.Pod.newRoleLevelRankListData();
		if(desValue != null){
			try {
				des_list.unmarshal(desValue);
			} catch (MarshalException e) {
				e.printStackTrace();
				Trace.error(e.getMessage());
			}
		}
		
		List<RoleLevelWrap> result_list = new ArrayList<RoleLevelPreProcess.RoleLevelWrap>();
		if(src_list.getRecords().size() > 0){
			for(RoleLevelWrap current : wrapRoleLevelRecords(src_list.getRecords(), srcdb)){
				tryInsertToDes(current, result_list);
				
			}
		}
		
		if(des_list.getRecords().size() > 0){
			for(RoleLevelWrap current : wrapRoleLevelRecords(des_list.getRecords(), dstdb)){
				tryInsertToDes(current, result_list);
			}
		}
		
		List<xbean.RoleLevelListRecord> result_xbean = unWrapRoleLevelRecords(result_list); 
		
		int size = result_xbean.size();
		if(result_xbean.size() > xmerge.Constant.LEVEL_RANK_LIST_NUM){
			for(int i=xmerge.Constant.LEVEL_RANK_LIST_NUM; i<size; i++){
				result_xbean.remove(xmerge.Constant.LEVEL_RANK_LIST_NUM);
			}
		}
		
		xbean.RoleLevelRankList result = xbean.Pod.newRoleLevelRankListData();
		result.getRecords().addAll(result_xbean);
		dst_table.replace(new OctetsStream().marshal(1), ((xbean.RoleLevelRankList)result).marshal(new OctetsStream()));
		
		src_table.remove(new OctetsStream().marshal(1));
		
		return true;
	}

	private List<RoleLevelListRecord> unWrapRoleLevelRecords(List<RoleLevelWrap> result_list) {
		List<RoleLevelListRecord> result = new ArrayList<RoleLevelListRecord>();
		for(RoleLevelWrap wrap : result_list){
			result.add(wrap.getRecord());
		}
		return result;
	}

	private List<RoleLevelWrap> wrapRoleLevelRecords(List<RoleLevelListRecord> records, Dbx currentDB) {
		List<RoleLevelWrap> result = new ArrayList<RoleLevelPreProcess.RoleLevelWrap>();
		for(RoleLevelListRecord record : records){
			RoleLevelWrap wrap = new RoleLevelWrap(record, getExpTotal(record.getMarshaldata().getRoleid(), currentDB));
			result.add(wrap);
		}
		return result;
	}

	private long getExpTotal(long roleid, Dbx currentDB) {
		OctetsStream os = new OctetsStream();
		Dbx.Table table = currentDB.openTable("properties");
		if(null != table){
			OctetsStream value = table.find(os.marshal(roleid));
			if(null != value){
				xbean.Properties properties = xbean.Pod.newProperties();
				try {
					properties.unmarshal(value);
				} catch (MarshalException e) {
					e.printStackTrace();
				}
				
				return properties.getExp();
			}
		}
		return 0;
	}

	private void tryInsertToDes(RoleLevelWrap current, List<RoleLevelWrap> records) {
		if(records.size() == 0){
			records.add(current);
			return;
		}
		for(int i=records.size()-1; i>=0; i--){
			if(compareTo(current, records.get(i))){
				if(i==0){
					records.add(0, current);
				}
				continue;
			}else{
				records.add(i+1, current);
				break;
			}
		}
		
	}

	private boolean compareTo(RoleLevelWrap current, RoleLevelWrap other) {
		if(current.getRecord().getMarshaldata().getLevel() > other.getRecord().getMarshaldata().getLevel()){
			return true;
		}else if(current.getRecord().getMarshaldata().getLevel() < other.getRecord().getMarshaldata().getLevel()){
			return false;
		}else{
			if(current.getExpTotal() > other.getExpTotal()){
				return true;
			}else if(current.getExpTotal() < other.getExpTotal()){
				return false;
			}else{
				if(current.getRecord().getTime() <= other.getRecord().getTime())
					return true;
			}
		}
		return false;
	}
	
	class RoleLevelWrap{
		xbean.RoleLevelListRecord record;
		long expTotal;
		public RoleLevelWrap(RoleLevelListRecord record, long expTotal) {
			this.record = record;
			this.expTotal = expTotal;
		}
		public xbean.RoleLevelListRecord getRecord() {
			return record;
		}
		public long getExpTotal() {
			return expTotal;
		}
	}
	
	
}
