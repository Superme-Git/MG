package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Serviceinfos implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ServiceInfo srcServiceInfo = xbean.Pod.newServiceInfoData(); 
		xbean.ServiceInfo dstServiceInfo = xbean.Pod.newServiceInfoData(); 
		srcServiceInfo.unmarshal(src_value);
		dstServiceInfo.unmarshal(dest_value);
		
		if(srcServiceInfo.getDays()>dstServiceInfo.getDays()){
			dstServiceInfo.setYear(srcServiceInfo.getYear()); // ��
			dstServiceInfo.setWeeks(srcServiceInfo.getWeeks()); // ��ǰ�����ڼ���(����ĵڼ���)
			dstServiceInfo.setActiverecommend(srcServiceInfo.getActiverecommend()); // ��ǰ�Ƽ��
			dstServiceInfo.setUpdatetime(srcServiceInfo.getUpdatetime()); // �ϴθ���������ʱ��
			dstServiceInfo.setDays(srcServiceInfo.getDays()); // �ۼƿ�������
			
		}
		dest_value.clear();
		dstServiceInfo.marshal(dest_value);
		
		
		
		return true;
	}
	
}