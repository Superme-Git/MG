/**
 * Class: Diamond.java
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
 * ClassName:Diamond
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-3-24		ÏÂÎç07:43:29
 *
 * @see 	 
 */
public class Diamond implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

//		xbean.DiamondInfo srcDiamondInfo = xbean.Pod.newDiamondInfoData();
//		xbean.DiamondInfo dstDiamondInfo = xbean.Pod.newDiamondInfoData();
//		
//		srcDiamondInfo.unmarshal(src_value);
//		dstDiamondInfo.unmarshal(dest_value);
//		
//		if (srcDiamondInfo.getPrice() < dstDiamondInfo.getPrice()){
//			dstDiamondInfo.setIndex(srcDiamondInfo.getIndex());
//			dstDiamondInfo.setPrice(srcDiamondInfo.getPrice());
//		}
//		
//		dest_value.clear();
//		dstDiamondInfo.marshal(dest_value);
		return true;
	}

}

