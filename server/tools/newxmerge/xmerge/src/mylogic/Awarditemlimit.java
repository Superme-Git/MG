package mylogic;

import java.util.Map.Entry;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;


public class Awarditemlimit implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.ActivityItemLimit srcbean = xbean.Pod.newActivityItemLimitData();
		xbean.ActivityItemLimit dstbean = xbean.Pod.newActivityItemLimitData();
		srcbean.unmarshal(src_value);
		dstbean.unmarshal(dest_value);
		
		for (Entry<Integer, xbean.LimitItemInfo> entry : srcbean.getLimititemmap().entrySet()) {
			xbean.LimitItemInfo limitItemInfo = dstbean.getLimititemmap().get(entry.getKey());
			if (limitItemInfo == null){
				dstbean.getLimititemmap().put(entry.getKey(), entry.getValue());
			}else {
				int newvalue = limitItemInfo.getNum()+entry.getValue().getNum();
				if (newvalue%2==1) 
				    limitItemInfo.setNum((limitItemInfo.getNum()+entry.getValue().getNum())/2+1);
				else
					limitItemInfo.setNum((limitItemInfo.getNum()+entry.getValue().getNum())/2);
			}
		}
		return true;
	}

}

