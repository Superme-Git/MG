package mylogic;

import java.util.Iterator;
import java.util.Map.Entry;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class UsercompensationTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.Compensation src_index = xbean.Pod.newCompensationData();
		xbean.Compensation des_index = xbean.Pod.newCompensationData();

		src_index.unmarshal(src_value);
		des_index.unmarshal(dest_value);

		Iterator<Entry<Integer, xbean.CompensationInfo>> iterator = src_index.getCompensationmap().entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Integer, xbean.CompensationInfo> current = iterator.next();
			if(!des_index.getCompensationmap().containsKey(current.getKey())){
				des_index.getCompensationmap().put(current.getKey(), current.getValue().toBean());
			}
		}

		dest_value.clear();
		des_index.marshal(dest_value);

		return true;
	}
}
