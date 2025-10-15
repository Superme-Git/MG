/**
 * Class: Solicit.java
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
 * ClassName:Solicit
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-3-24		ÏÂÎç07:51:14
 *
 * @see 	 
 */
public class Solicit implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		return true;
	}

}

