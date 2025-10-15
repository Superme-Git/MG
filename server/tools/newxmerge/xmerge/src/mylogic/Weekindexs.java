package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Weekindexs implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.WeekIndex srcWeekIndex = xbean.Pod.newWeekIndexData(); 
		xbean.WeekIndex dstWeekIndex = xbean.Pod.newWeekIndexData(); 
		srcWeekIndex.unmarshal(src_value);
		dstWeekIndex.unmarshal(dest_value);
		
		if(srcWeekIndex.getIndex()>dstWeekIndex.getIndex()){
			dstWeekIndex.setIndex(srcWeekIndex.getIndex()); // �������ֻ�����
			dstWeekIndex.setInstanceindex(srcWeekIndex.getInstanceindex()); // �����ֻ�����
		}
		dest_value.clear();
		dstWeekIndex.marshal(dest_value);
		
		
		
		return true;
	}
	
}