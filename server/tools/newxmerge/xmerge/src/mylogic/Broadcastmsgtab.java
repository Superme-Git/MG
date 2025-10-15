package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Broadcastmsgtab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.BroadcastMsgList srcBroadcastMsgList = xbean.Pod.newBroadcastMsgListData(); 
		xbean.BroadcastMsgList dstBroadcastMsgList = xbean.Pod.newBroadcastMsgListData(); 
		srcBroadcastMsgList.unmarshal(src_value);
		dstBroadcastMsgList.unmarshal(dest_value);
		
		dstBroadcastMsgList.getMsglist().addAll(srcBroadcastMsgList.getMsglist());
		
		dest_value.clear();
		dstBroadcastMsgList.marshal(dest_value);
		return true;
	}
	
}