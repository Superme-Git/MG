package mylogic;

import java.util.List;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * 活动鲜花榜处理
 * @author wupanliang
 * @date 2014-6-13
 */
public class ActiveRoselist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.activeroserecords src_list = xbean.Pod.newactiveroserecordsData();
//		xbean.activeroserecords des_list = xbean.Pod.newactiveroserecordsData();
//		
//		src_list.unmarshal(src_value);
//		des_list.unmarshal(dest_value);
//		
//		java.util.List<xbean.activeroserecord> recordList = src_list.getRecords();
//		for(xbean.activeroserecord current : recordList){
//			tryInsertToDes(current, des_list.getRecords());
//		}
//		
//		int size = des_list.getRecords().size();
//		if(size > 20){
//			for(int i=20; i<size; i++){
//				des_list.getRecords().remove(20);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(des_list);
		
		return true;
	}

//	private void tryInsertToDes(xbean.activeroserecord current,	List<xbean.activeroserecord> records) {
//		for(int i=records.size()-1; i>=0; i--){
//			if(compareTo(current, records.get(i))){
//				if(i==0){
//					records.add(0, current);
//				}
//				continue;
//			}else{
//				records.add(i+1, current);
//				break;
//			}
//		}
//		
//	}

//	private boolean compareTo(xbean.activeroserecord current, xbean.activeroserecord roseListRecord) {
//		if(current.getNum() > roseListRecord.getNum()){
//			return true;
//		}
//		return false;
//	}

}
