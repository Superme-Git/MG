/**
 * Class: IPreprocess.java
 * Package: xmerge
 *
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2012-5-6 		yesheng
 *
 * Copyright (c) 2012, Perfect World All Rights Reserved.
*/

package xmerge;

import mkdb.util.Dbx;

/**
 * ClassName:IPreprocess
 * Function: ����һЩ��Ҫ�ںϷ�ǰԤ����ı�,ʵ������ӿ�,�����ںϷ�֮ǰ,��Ӧ�ı�ͻ�
 * �Ƚ��д���.����ӿ���Ҫ��������һЩ�Ϸ�Imerge�ӿ��޷�handle�����,����IMergeֻ��
 * �ṩ��ͻ��key��srcvalue,dstvalue,���еı��merge����Ҫ����������м�¼���ŵ�
 *
 * @author   yesheng
 * @version  
 * @since    
 * @Date	 2012-5-6		����08:41:49
 *
 * @see 	 
 */
public interface IPreprocess {

	/**
	 * 
	 * preProcess:
	 *
	 * @param src_table ��ҪԤ����ı�,������dst_table��ͬ,��xmerge.xml�ж���
	 * @param dst_table ��ҪԤ����ı�,������src_table��ͬ,��xmerge.xml�ж���
	 * @return    
	 * boolean   true��ʾԤ����ɹ�, �������false,�Ϸ�ʧ��,�ع�.
	 * @throws 
	 * @since  ��
	 */
	public boolean preProcess(Dbx srcdb,Dbx.Table src_table,Dbx dstdb,Dbx.Table dst_table);
}

