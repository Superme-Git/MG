package xmerge;

import com.locojoy.base.Marshal.OctetsStream;

/**
 *����Known_key_conflict�ı���ʵ������ӿ�
 *<li>�ӿ�ģʽ�ݶ����
 *<li>��src_value�ϲ���dest_value�У�dest_value����Ϊ�ϲ������ݱ��е�value
 *<li>����true���ʾmerge�ɹ�
 *<li>����false���ʾmergeʧ�ܣ��ݽ�dest_value��Ϊ�ϲ����value����¼��־
 *<li>�׳��쳣����Ϊmerge�߼��쳣���ж������ϲ�����
 * */
public interface IMerge {
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception;
}
