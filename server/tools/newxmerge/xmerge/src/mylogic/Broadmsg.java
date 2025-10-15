/**
 * Class: Achievementlist.java
 * Package: mylogic
 *
 *
 *   ver     date      		author
 * ©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤
 *   		 2012-3-24 		yesheng
 *
 * Copyright (c) 2012, Perfect World All Rights Reserved.
*/

package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


/**
 * ClassName:Achievementlist
 * Function: ADD FUNCTION HERE
 *
 * @author   liuchen
 * @version  
 * @since    
 * @Date	
 *
 * @see 	 
 */
public class Broadmsg implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.BroadcastMsgList srclist = xbean.Pod.newBroadcastMsgListData();
		xbean.BroadcastMsgList dstlist = xbean.Pod.newBroadcastMsgListData();
		srclist.unmarshal(src_value);
		dstlist.unmarshal(dest_value);
		dstlist.getMsglist().addAll(srclist.getMsglist());
		dest_value.clear();
		dest_value.marshal(dstlist);
		return true;
	}

}

