/**
 * Class: YuanbaonumMergeImplementation.java
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
 * ClassName:YuanbaonumMergeImplementation
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2011-10-17		ÉÏÎç11:59:14
 *
 * @see 	 
 */
public class Yuanbaonum implements xmerge.IMerge{

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

       xbean.YbNums srcYbNums = xbean.Pod.newYbNumsData();
       xbean.YbNums dstYbNums = xbean.Pod.newYbNumsData();
       srcYbNums.unmarshal(src_value);
       dstYbNums.unmarshal(dest_value);
       
       dstYbNums.getRoleyb().putAll(srcYbNums.getRoleyb());
      
       
       dest_value.clear();
       dstYbNums.marshal(dest_value);
		
		return true;
	}

}

