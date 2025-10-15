package mylogic;

import java.util.Map.Entry;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Wheelitemlimit implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.WheelItemLimit srcxbean = xbean.Pod.newWheelItemLimitData();
		xbean.WheelItemLimit dstxbean = xbean.Pod.newWheelItemLimitData();
		srcxbean.unmarshal(src_value);
		dstxbean.unmarshal(dest_value);
		
		for (Entry<Integer, Integer> entry : srcxbean.getLimitmap().entrySet()) {
			Integer dstValue = dstxbean.getLimitmap().get(entry.getKey());
			if (dstValue == null)
				dstxbean.getLimitmap().put(entry.getKey(), entry.getValue());
			else {
				int newvalue = entry.getValue()+dstValue;
				if (newvalue%2==1)
					dstxbean.getLimitmap().put(entry.getKey(), (entry.getValue()+dstValue)/2+1);
				else
					dstxbean.getLimitmap().put(entry.getKey(), (entry.getValue()+dstValue)/2);
			}
		}
		
		dest_value.clear();
		dstxbean.marshal(dest_value);
		return true;
	}

}

