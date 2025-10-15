package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Redpackinfoviewlisttab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		xbean.RedPackInfoViewList srcRedPackInfoViewList = xbean.Pod.newRedPackInfoViewListData(); 
		xbean.RedPackInfoViewList dstRedPackInfoViewList = xbean.Pod.newRedPackInfoViewListData(); 
		srcRedPackInfoViewList.unmarshal(src_value);
		dstRedPackInfoViewList.unmarshal(dest_value);
		
		dstRedPackInfoViewList.getRedpackinfoviewlist().putAll(srcRedPackInfoViewList.getRedpackinfoviewlist());
		
		dest_value.clear();
		dstRedPackInfoViewList.marshal(dest_value);
		
		
		return true;
	}
	
}