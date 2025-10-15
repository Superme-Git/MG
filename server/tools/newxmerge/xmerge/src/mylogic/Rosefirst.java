package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Rosefirst implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.RoseWinnerList srcList = xbean.Pod.newRoseWinnerList();
//		xbean.RoseWinnerList desList = xbean.Pod.newRoseWinnerList();
//		
//		srcList.unmarshal(src_value);
//		desList.unmarshal(dest_value);
//		
//		desList.getWinnerlist().addAll(srcList.getWinnerlist());
//		
		return true;
	}

}
