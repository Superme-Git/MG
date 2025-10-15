/**
 * Class: Roleidtoyuanbaosell.java
 * Package: mylogic
 *
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-3-24 		yesheng
 *
 * Copyright (c) 2012, Perfect World All Rights Reserved.
*/

package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


/**
 * ClassName:Roleidtoyuanbaosell
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-3-24		下午07:40:01
 *
 * @see 	 
 */
public class Roleidtoyuanbaosell implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		long roleid = src_key.unmarshal_long();
		if (roleid!=100)
			throw new RuntimeException("only system yuanbao need to be merged!");
		//对于系统挂的元宝单,可以直接保留dest_value,src_value的单子不要了,所以直接return true即可
		return true;
	}

}

