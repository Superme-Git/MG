package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class FashionSellList extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.FashionDressList src_list = xbean.Pod.newFashionDressListData();
//		xbean.FashionDressList dest_list = xbean.Pod.newFashionDressListData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		//把两个服务器相同id的物品先累加起来再重新排序
//		updateFashionRecords(dest_list.getRecords(), src_list.getRecords());
//		sortCurrentRankList(dest_list.getRecords());
//		
//		int size = dest_list.getRecords().size();
//		if(size > xmerge.Constant.FASHION_SELL_LIST_NUM){
//			for(int i=xmerge.Constant.FASHION_SELL_LIST_NUM; i<size; i++){
//				dest_list.getRecords().remove(xmerge.Constant.FASHION_SELL_LIST_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(dest_list);
		return true;
	}

//	private void updateFashionRecords(List<FashionDressListRecord> records,
//			List<FashionDressListRecord> records2) {
//		List<FashionDressListRecord> tmp = new ArrayList<FashionDressListRecord>();
//		for(FashionDressListRecord record : records2){
//			boolean isContains = false;
//			for(FashionDressListRecord current : records){
//				if(current.getMarshaldata().getDressid() == record.getMarshaldata().getDressid()){
//					isContains = true;
//					current.getMarshaldata().setSaletimes(current.getMarshaldata().getSaletimes()+record.getMarshaldata().getSaletimes());
//					break;
//				}
//			}
//			if(!isContains){
//				tmp.add(record);
//			}
//		}
//		
//		records.addAll(tmp);
//	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.FashionDressListRecord src = (xbean.FashionDressListRecord)srcO;
//		xbean.FashionDressListRecord dest = (xbean.FashionDressListRecord)destO;
//		
//		if(src.getMarshaldata().getSaletimes() >= dest.getMarshaldata().getSaletimes()){
//			return true;
//		}
		return false;
	}

}
