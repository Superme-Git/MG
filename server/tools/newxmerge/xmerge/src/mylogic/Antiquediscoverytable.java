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
//		//��������map������,���Կ��ٵ��ҵ��Ŷ���list�����index
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
//		//��dstΪbase,����dst����ĹŶ�,��ͬ�ĹŶ�id�϶���һ����,ֻ�Ƿ���ʱ��ͼ۸�᲻ͬ.�еĹŶ�ֻ��dst���淢����,�ǾͲ���������
//		for (xbean.AntiqueDiscoveryInfo info : dstDiscoveryInfoList.getInfos()) {
//			Integer srcIndex = srcMap.get(info.getMarshaldata().getAntiqueid());
//			if (srcIndex!=null){//�����src���ݿ�����Ҳ������Ŷ�
//				xbean.AntiqueDiscoveryInfo srcInfo = srcDiscoveryInfoList.getInfos().get(srcIndex);
//				if (srcInfo.getMarshaldata().getTime()<info.getMarshaldata().getTime()){
//					info.getMarshaldata().setTime(srcInfo.getMarshaldata().getTime());
//					info.getMarshaldata().setOwnername(srcInfo.getMarshaldata().getOwnername());
//					info.getMarshaldata().setRoleid(srcInfo.getMarshaldata().getRoleid());
//					info.getMarshaldata().setAntiquelevel(srcInfo.getMarshaldata().getAntiquelevel());
//					info.getMarshaldata().setPrice(srcInfo.getMarshaldata().getPrice());
//				}
//				srcMap.remove(info.getMarshaldata().getAntiqueid());//���������ݿ����涼�еĹŶ���srcɾ��,����ʣ�µľ���src���е�
//			}
//		}
//		//����src������еĹŶ�,���뵽dst��
//		if (!srcMap.isEmpty()){
//			for (Integer index : srcMap.values()) {
//				xbean.AntiqueDiscoveryInfo srcInfo = srcDiscoveryInfoList.getInfos().get(index);
//				dstDiscoveryInfoList.getInfos().add(srcInfo);
////				int newIndex = getIndexOfNewAntique(dstDiscoveryInfoList, srcInfo);
////				dstDiscoveryInfoList.getInfos().add(newIndex,srcInfo);
//			}
//		}
//		//���°��۸�����
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

