package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class ChargehistoryTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		xbean.ChargeHistory src_index = xbean.Pod.newChargeHistoryData();
		xbean.ChargeHistory des_index = xbean.Pod.newChargeHistoryData();
		
		src_index.unmarshal(src_value);
		des_index.unmarshal(dest_value);
		
		des_index.getCharges().putAll(src_index.getCharges());
		
		dest_value.clear();
		des_index.marshal(dest_value);
		
		return true;
	}

}
