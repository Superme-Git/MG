/**
 * Class: ShouxicandidatesMergeImplementation.java
 * Package: mylogic
 *
 *
 *   ver     date      		author
 * ©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤
 *   		 2011-10-17 		yesheng
 *
 * Copyright (c) 2011, Perfect World All Rights Reserved.
*/

package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * ClassName:ShouxicandidatesMergeImplementation
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2011-10-17		ÏÂÎç12:11:00
 *
 * @see 	 
 */
public class Shouxicandidates implements xmerge.IMerge{

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//       xbean.ShouxiCand srcShouxiCand = xbean.Pod.newShouxiCandData();
//       xbean.ShouxiCand dstShouxiCand = xbean.Pod.newShouxiCandData();
//       
//       srcShouxiCand.unmarshal(src_value);
//       srcShouxiCand.unmarshal(dest_value);
//       
//       dstShouxiCand.getCandidatelist().addAll(srcShouxiCand.getCandidatelist());
//       
//       dest_value.clear();
//       dstShouxiCand.marshal(dest_value);
		
		return true;
	}

}

