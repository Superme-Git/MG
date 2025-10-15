/**
 * Class: Yuanbaoitemcreated.java
 * Package: mylogic
 *
 *
 *   ver     date      		author
 * ©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤
 *   		 2012-11-20 		yesheng
 *
 * Copyright (c) 2012, Perfect World All Rights Reserved.
*/

package mylogic;

import java.util.Map.Entry;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


/**
 * ClassName:Yuanbaoitemcreated
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-11-20		ÏÂÎç02:26:42
 *
 * @see 	 
 */
public class Yuanbaoitemcreated implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

//		xbean.ItemCreateInfo srcInfo = xbean.Pod.newItemCreateInfoData();
//		xbean.ItemCreateInfo dstInfo = xbean.Pod.newItemCreateInfoData();
//		srcInfo.unmarshal(src_value);
//		dstInfo.unmarshal(dest_value);
//		
//        for (Entry<Integer, Integer> entry : srcInfo.getNuminfo().entrySet()) {
//			Integer tmp = dstInfo.getNuminfo().get(entry.getKey());
//			if (tmp == null)
//				dstInfo.getNuminfo().put(entry.getKey(), entry.getValue());
//			else
//				dstInfo.getNuminfo().put(entry.getKey(), entry.getValue() + tmp);
//		}
//		
//		dest_value.clear();
//		dstInfo.marshal(dest_value);
		return true;
	}

}

