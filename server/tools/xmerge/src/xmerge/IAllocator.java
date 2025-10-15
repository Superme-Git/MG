package xmerge;

/*
 * 	���в���AutoKey���͵ı�����ҪΪ�ñ�ʵ����Ӧ��IAllocator����ʵ��������ñ���
 * 	���ܴ��ڵ�key��ͻ����ͨ��Xmerge.addAllocator(String tableName, IAllocator allocator)ע��
 */
public interface IAllocator {
	/**
	 * ���ݱ�����oldKey������key
	 * @param tableName
	 * @param oldKey
	 * @return newKey
	 */
	public Object allocateKey(String tableName, Object oldKey); 
}