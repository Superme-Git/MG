package mylogic;

import java.util.Map.Entry;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Lianyunaward implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.LianyunAwardInfo srclianyunAwardInfo = xbean.Pod.newLianyunAwardInfoData();
		xbean.LianyunAwardInfo dstlianyunAwardInfo = xbean.Pod.newLianyunAwardInfoData();
		srclianyunAwardInfo.unmarshal(src_value);
		dstlianyunAwardInfo.unmarshal(dest_value);
		
		for (Entry<Integer, Long> entry : srclianyunAwardInfo.getAwards().entrySet()) {
			Long dstValue = dstlianyunAwardInfo.getAwards().get(entry.getKey());
			if (dstValue == null)
				dstlianyunAwardInfo.getAwards().put(entry.getKey(), entry.getValue());
			else {
				if (dstValue < entry.getValue())//只有当dstvalue小的时候才需要处理,否则直接用dstvalue的数据即可
					dstlianyunAwardInfo.getAwards().put(entry.getKey(), entry.getValue());
			}
		}
		
		dest_value.clear();
		dstlianyunAwardInfo.marshal(dest_value);
		return true;
	}

}

