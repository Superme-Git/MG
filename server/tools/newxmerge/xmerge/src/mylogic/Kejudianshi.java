/**
 * Class: KejudianshiMergeImplement.java
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
 * ClassName:KejudianshiMergeImplement
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2011-10-17		ÉÏÎç11:54:22
 *
 * @see 	 
 */
public class Kejudianshi implements xmerge.IMerge{

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

//		xbean.KejuDianshiRecord srcDianshiRecord = xbean.Pod.newKejuDianshiRecordData();
//		xbean.KejuDianshiRecord dstDianshiRecord = xbean.Pod.newKejuDianshiRecordData();
//		
//		srcDianshiRecord.unmarshal(src_value);
//		dstDianshiRecord.unmarshal(dest_value);
//		dstDianshiRecord.getPassidslist().addAll(srcDianshiRecord.getPassidslist());
//		
//		
//		
//		dest_value.clear();
//		dstDianshiRecord.marshal(dest_value);
		
		return true;
	}

}

