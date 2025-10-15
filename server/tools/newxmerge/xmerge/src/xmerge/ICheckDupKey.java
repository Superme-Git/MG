/**
 * 
 */
package xmerge;

/**
 * @author CaijiaCheng
 * ���÷�ʽ��
 * 				ICheckDupKey check = new CheckDupKey();
 * 				check.checkDB( src, dst );
 * 				check.getDupKeyDB();
 */
	
public interface ICheckDupKey {
	/**
	 *  ���Դ���ݿ���Ŀ�����ݿ�����ͬ���ظ���key
	 *  note: ��ⲻ��ı�srcDB, dstDb�еı��������errDb�еı�. ֻ���!known_key_conflict
	 * @param srcDb : Դ���ݿ� 
	 * @param dstDb : Ŀ�����ݿ�
	 * @return ���ɹ�true
	 * 			ʧ��: false����û�м�⵽�κε�dupkey
	 * 			
	 */
	public boolean checkDB( String srcDb, String dstDb );
	
	/**
	 * 
	 * @return �趨errDB��·����(Ĭ��Ϊ���·��: errDB)��
	 */
	public void setDupKeyDBPath(String path);
			
	/**
	 * 
	 * @return ����errDB��·����(���·��)��
	 */
	public String getDupKeyDBPath();
		
	/**
	 * @param tablename : ���ݿ����
	 * @return : ȡ����Ӧ���ݿ�������ظ�DupKey
	 * 			 �����������Ӧ��tablename�����׳��쳣
	 * @throws Exception 
	 */
	public long getDupKeyCount( String tablename ) throws Exception;
	
	
	
}
