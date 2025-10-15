package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Ldvideoroleinfodeslisttab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		xbean.LDVideoRoleInfoDesList srcLDVideoRoleInfoDesList = xbean.Pod.newLDVideoRoleInfoDesListData(); 
		xbean.LDVideoRoleInfoDesList dstLDVideoRoleInfoDesList = xbean.Pod.newLDVideoRoleInfoDesListData(); 
		srcLDVideoRoleInfoDesList.unmarshal(src_value);
		dstLDVideoRoleInfoDesList.unmarshal(dest_value);
		
		dstLDVideoRoleInfoDesList.getLdvideoroleinfodeslistall().addAll(srcLDVideoRoleInfoDesList.getLdvideoroleinfodeslistall());
		
		dest_value.clear();
		dstLDVideoRoleInfoDesList.marshal(dest_value);
		
		
		return true;
	}
	
}