/**
 * Class: Yuanbaosuishenskill.java
 * Package: mylogic
 *
 *
 *   ver     date      		author
 * ©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤©¤
 *   		 2012-10-8 		yesheng
 *
 * Copyright (c) 2012, Perfect World All Rights Reserved.
*/

package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


/**
 * ClassName:Yuanbaosuishenskill
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-10-8		ÏÂÎç01:31:58
 *
 * @see 	 
 */
public class Yuanbaosuishenskill implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		int srcSkill = src_value.unmarshal_int();
		int dstSkill = dest_value.unmarshal_int();
		
		if (srcSkill>dstSkill){
			dest_value.clear();
			dest_value.marshal(srcSkill);
		}
		
		return true;
	}

}

