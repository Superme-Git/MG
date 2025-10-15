package mylogic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.locojoy.base.Marshal.OctetsStream;

public class Antiquediscoverytable implements xmerge.IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//		xbean.AntiqueDiscoveryInfoList srcDiscoveryInfoList = xbean.Pod.newAntiqueDiscoveryInfoListData();
//		xbean.AntiqueDiscoveryInfoList dstDiscoveryInfoList = xbean.Pod.newAntiqueDiscoveryInfoListData();
//		
//		srcDiscoveryInfoList.unmarshal(src_value);
//		dstDiscoveryInfoList.unmarshal(dest_value);
//		
//		//生成两个map的索引,可以快速的找到古董在list里面的index
//		Map<Integer, Integer> srcMap = new HashMap<Integer, Integer>();
//		for (int i=0; i< srcDiscoveryInfoList.getInfos().size();i++) {
//			xbean.AntiqueDiscoveryInfo info = srcDiscoveryInfoList.getInfos().get(i);
//			srcMap.put(info.getMarshaldata().getAntiqueid(), i);
//		}
//		Map<Integer, Integer> dstMap = new HashMap<Integer, Integer>();
//		for (int i = 0; i < dstDiscoveryInfoList.getInfos().size(); i++) {
//			xbean.AntiqueDiscoveryInfo info = dstDiscoveryInfoList.getInfos().get(i);
//			dstMap.put(info.getMarshaldata().getAntiqueid(), i);
//		}
//		//以dst为base,遍历dst里面的古董,相同的古董id肯定是一样的,只是发现时间和价格会不同.有的古董只在dst里面发现了,那就不动就行了
//		for (xbean.AntiqueDiscoveryInfo info : dstDiscoveryInfoList.getInfos()) {
//			Integer srcIndex = srcMap.get(info.getMarshaldata().getAntiqueid());
//			if (srcIndex!=null){//如果在src数据库里面也有这个古董
//				xbean.AntiqueDiscoveryInfo srcInfo = srcDiscoveryInfoList.getInfos().get(srcIndex);
//				if (srcInfo.getMarshaldata().getTime()<info.getMarshaldata().getTime()){
//					info.getMarshaldata().setTime(srcInfo.getMarshaldata().getTime());
//					info.getMarshaldata().setOwnername(srcInfo.getMarshaldata().getOwnername());
//					info.getMarshaldata().setRoleid(srcInfo.getMarshaldata().getRoleid());
//					info.getMarshaldata().setAntiquelevel(srcInfo.getMarshaldata().getAntiquelevel());
//					info.getMarshaldata().setPrice(srcInfo.getMarshaldata().getPrice());
//				}
//				srcMap.remove(info.getMarshaldata().getAntiqueid());//把两个数据库里面都有的古董从src删除,这样剩下的就是src独有的
//			}
//		}
//		//处理src里面独有的古董,并入到dst中
//		if (!srcMap.isEmpty()){
//			for (Integer index : srcMap.values()) {
//				xbean.AntiqueDiscoveryInfo srcInfo = srcDiscoveryInfoList.getInfos().get(index);
//				dstDiscoveryInfoList.getInfos().add(srcInfo);
////				int newIndex = getIndexOfNewAntique(dstDiscoveryInfoList, srcInfo);
////				dstDiscoveryInfoList.getInfos().add(newIndex,srcInfo);
//			}
//		}
//		//重新按价格排序
//		Collections.sort(dstDiscoveryInfoList.getInfos(), new Comparator<xbean.AntiqueDiscoveryInfo>() {
//
//			@Override
//			public int compare(AntiqueDiscoveryInfo o1, AntiqueDiscoveryInfo o2) {
//
//				if (o1.getMarshaldata().getPrice()<o2.getMarshaldata().getPrice())
//					return 1;
//				else if (o1.getMarshaldata().getPrice()==o2.getMarshaldata().getPrice()) {
//					return 0;
//				}else
//				    return -1;
//			}
//		});
//		
//		dest_value.clear();
//		dstDiscoveryInfoList.marshal(dest_value);
		return true;
	}
//	private int getIndexOfNewAntique(xbean.AntiqueDiscoveryInfoList adil,
//			xbean.AntiqueDiscoveryInfo adi) {
//		int size = adil.getInfos().size();
//		if (size==0) {
//			return 0;
//		}
//		int price = adi.getMarshaldata().getPrice();
//		long time = adi.getMarshaldata().getTime();
//		for (int i = 0; i < size; i++) {
//			if (adil.getInfos().get(i).getMarshaldata().getPrice()>price) {
//				continue;
//			}else if (adil.getInfos().get(i).getMarshaldata().getPrice()==price) {
//				if (time>adil.getInfos().get(i).getMarshaldata().getTime()) {
//					continue;
//				}else {
//					return i;
//				}
//			}else {
//				return i;
//			}
//		}
//		return size;
//	}
}

