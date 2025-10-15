package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class ImpexamStateTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ImpExamStateRecord srcImpExamStateRecord = xbean.Pod.newImpExamStateRecordData(); 
		xbean.ImpExamStateRecord dstImpExamStateRecord = xbean.Pod.newImpExamStateRecordData(); 
		srcImpExamStateRecord.unmarshal(src_value);
		dstImpExamStateRecord.unmarshal(dest_value);
		
		dstImpExamStateRecord.getIdslist().addAll(srcImpExamStateRecord.getIdslist());
		dstImpExamStateRecord.getPassidslist().addAll(srcImpExamStateRecord.getPassidslist());
		dstImpExamStateRecord.getPassrolelist().addAll(srcImpExamStateRecord.getPassrolelist());
		
		dest_value.clear();
		dstImpExamStateRecord.marshal(dest_value);
		
		return true;
	}
	
}