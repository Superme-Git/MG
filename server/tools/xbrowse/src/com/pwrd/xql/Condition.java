package com.pwrd.xql;

import java.util.Map;

/**
 *<code>Condition</code>���ڱ�ʾһ���������������
 *�ȿ�����һ���򵥵�������䣬Ҳ�������ɶ���������
 *��϶��ɵ�������䡣
 *<p>
 *<code>Condition</code>�������﷨�������ڽ���XQL���
 *��ʱ���Ե�����������ɡ�
 *<p>
 *������Ӧ���������׶���ɣ���һ���׶����������������
 *ȷ�������г��ֵı�ʶ���Ƿ���ںͱ�ʶ��֮�������֮��
 *�Ƿ�ƥ�� �ȣ��ڶ����׶���Ӧ�����������XQLִ�еĲ���
 *�ǲ�ѯ����ô���ڷ��ص�ÿһ����¼Ӧ�������жϣ��������
 *�����򷵻أ���������������ʱ������Add, Update��remove
 *��������
 * 
 * @author ��ƽ(wangping101853)
 *
 */
public interface Condition extends SemanticAnalyzer
{
	/**
	 * ���ø�����Ӧ�õ�Ŀ���¼��
	 * 
	 * @param _record
	 */
	@SuppressWarnings("rawtypes")
	public void setRecord(Map.Entry _record);
	
	/**
	 * ִ���������㡣
	 * 
	 * @return true�����¼�������������򷵻�false
	 */
	public boolean isTrue();
}
