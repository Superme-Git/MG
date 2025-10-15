package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

/**
 * ø∆æŸµÒÀ‹≈≈––
 * @author ldf
 *
 */
public class KejutoprecordTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
//		xbean.TopRecords src_index = xbean.Pod.newTopRecordsData();
//		xbean.TopRecords des_index = xbean.Pod.newTopRecordsData();
//		
//		src_index.unmarshal(src_value);
//		des_index.unmarshal(dest_value);
//		
//		des_index.getIds().addAll(src_index.getIds());
//		
//		dest_value.clear();
//		des_index.marshal(dest_value);
		
		return true;
	}

}
