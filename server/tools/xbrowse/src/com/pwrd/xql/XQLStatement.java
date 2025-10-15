package com.pwrd.xql;

import java.util.List;
import java.util.Map;

/**
 *<code>XQLStatement</code>��XQL���ĳ�������XQL���
 *���඼��Ҫʵ�ָýӿڡ�
 * 
 * @author ��ƽ(wangping101853)
 *
 */
public interface XQLStatement extends SemanticAnalyzer
{
	/**
	 *��������ö�٣�Ŀǰxbrowse����֧�ֲ�ѯ�����º�ɾ��
	 *���ֲ����� 
	 */
	public static enum XQLOperationType
	{
		XQL_OPERATION_SELECT,
		XQL_OPERATION_UPDATE,
		XQL_OPERATION_DELETE
	}
	
	/**
	 * ����XQL����������������ָwhere����Ĳ��֣�����
	 * ָ����������Ŀǰ�÷������﷨���������﷨�����ɹ�
	 * ����ã�
	 * <p/>
	 * <b>ע��: ���﷨�������У�һ��Ҫ��֤�ȵ��ñ�������Ȼ���ٵ���
	 * {@link #setTable(String)}��</b>
	 * 
	 * @param cond - ������XQL�����where����Ĳ��֣�
	 */
	public void setCondition(Condition cond);
	
	/**
	 *�ڽ����﷨����֮ǰ�������޷�֪��XQL Statement�Ĳ������ͣ�
	 *�﷨�������֮��XQL Statement���������ˣ���ʱ�������
	 *����Ҳ�Ѿ�ȷ������Ӧ�ù����У���Ҫ�����������ͽ��к����Ĳ�
	 *����e.g.����ǲ�ѯ����Ҫ�ڽ�������ʾ��������ڸ��º�ɾ����
	 *ֻ��Ҫ֪�������˶�������¼���ɡ�
	 * 
	 * @return XQL Statement�Ĳ������͡�
	 */
	public XQLOperationType getOperationType();
	
	/**
	 *ִ��XQL Statement��
	 *<p/>
	 *һ��Ҫ�ڴʷ��������﷨�����������������֮��ִ�иò�������
	 *Ϊִ�й������Ѿ������˳������˼ٶ�������ʡȥ�˺ܶ�û��Ҫ
	 *�ļ����������û�������˳����в�����ִ�й����лᷢ����
	 *��Ԥ�ϵĴ��� 
	 * 
	 * @return true if execute successfully, otherwise false.
	 */
	public boolean execute();
	
	/**
	 *�õ�XQL Statement��ִ�н����ֻ�в�ѯ�����Ҫʵ�ָ÷�����
	 *���ڸ��º�ɾ������׳�{@link java.lang.UnsupportedOperationException}
	 *���ɡ�
	 * 
	 * @return ��ѯ����ִ�н����
	 */
	@SuppressWarnings("rawtypes")
	public List<Map.Entry> getResultList();
	
	/**
	 *�õ�XQL Statement��ִ�н����ֻ�и��º�ɾ�������Ҫʵ�ָ�
	 *���������ڲ�ѯ����׳�{@link java.lang.UnsupportedOperationException}
	 *���ɡ�
	 * 
	 * @return ���º�ɾ����ִ�н����
	 */
	public int getCount();
}
