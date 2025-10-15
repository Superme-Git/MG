package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Roserankingtitles implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.RankingTitleInfo srcRecord = xbean.Pod.newRankingTitleInfoData();
//		xbean.RankingTitleInfo dstRecord = xbean.Pod.newRankingTitleInfoData();
//		src_key.unmarshal(srcRecord);
//		dest_value.unmarshal(dstRecord);
//		
//		dstRecord.getTitleroles().addAll(srcRecord.getTitleroles());
//		dest_value.clear();
//		dest_value.marshal(dstRecord);
		return true;
	}

}
