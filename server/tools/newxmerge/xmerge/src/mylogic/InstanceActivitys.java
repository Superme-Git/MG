package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class InstanceActivitys implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.InstanceActivity src_instanceActivitys = xbean.Pod.newInstanceActivityData();
		xbean.InstanceActivity dest_instanceActivitys = xbean.Pod.newInstanceActivityData();
		src_instanceActivitys.unmarshal(src_value);
		dest_instanceActivitys.unmarshal(dest_value);
		
		dest_instanceActivitys.setInstanceid(src_instanceActivitys.getInstanceid()); // ����id,����������.xlsx�е�id��Ӧ
		dest_instanceActivitys.setState(src_instanceActivitys.getState()); // ״̬,0=δ��ʼ��1=�ѿ�ʼ��2=�ѽ���
		dest_instanceActivitys.setBaseid(src_instanceActivitys.getBaseid()); // �����id
		dest_instanceActivitys.setStarttime(src_instanceActivitys.getStarttime()); // ��ʼʱ��
		dest_instanceActivitys.setEndtime(src_instanceActivitys.getEndtime()); // ����ʱ��
		
		dest_value.clear();
		dest_instanceActivitys.marshal(dest_value);
		
		return true;
	}
	
}