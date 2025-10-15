package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Limitcard implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.LimitCardInfo src_list = xbean.Pod.newLimitCardInfoData();
//		xbean.LimitCardInfo dest_list = xbean.Pod.newLimitCardInfoData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		dest_list.getCards().addAll(src_list.getCardsAsData());
//		
//		dest_value.clear();
//		dest_list.marshal(dest_value);
		return true;
	}

}
