/**
 * Class: Apprenticelist.java
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
 * ClassName:Apprenticelist
 * Function: ADD FUNCTION HERE
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-3-24		ÏÂÎç07:46:46
 *
 * @see 	 
 */
public class Apprenticelist extends RankListSortProcess implements IMerge {

	/**
	 * (non-Javadoc)
	 * @see xmerge.IMerge#merge(com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream, com.locojoy.base.Marshal.OctetsStream)
	 */
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//		xbean.ApprenticeRankList src_list = xbean.Pod.newApprenticeRankListData();
//		xbean.ApprenticeRankList dest_list = xbean.Pod.newApprenticeRankListData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		dest_list.getRecords().addAll(src_list.getRecords());
//		sortCurrentRankList(dest_list.getRecords());
//		
//		int size = dest_list.getRecords().size();
//		if(dest_list.getRecords().size() > xmerge.Constant.MASTER_RANK_LIST_NUM){
//			for(int i=xmerge.Constant.MASTER_RANK_LIST_NUM; i<size; i++){
//				dest_list.getRecords().remove(xmerge.Constant.MASTER_RANK_LIST_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(dest_list);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
		xbean.ApprenticelListRecord src = (xbean.ApprenticelListRecord)srcO;
		xbean.ApprenticelListRecord dest = (xbean.ApprenticelListRecord)destO;
		
		if(src.getMarshaldata().getApprenticenum() > dest.getMarshaldata().getApprenticenum()){
			return true;
		}else if(src.getMarshaldata().getApprenticenum() < dest.getMarshaldata().getApprenticenum()){
			return false;
		}else{
			if(src.getTime() <= dest.getTime()){
				return true;
			}
			
			return false;
		}
		
	}

}

