package mkdb;

/**
 * 
 * �ڴ����������������.�������Ʊ�ɾ���ļ�¼ͨ������ӿڻص���
 * 
 * ���ݿ����Զ������ص�����ӿڡ�
 * 
 * ע��ӿ������ɳ����ı�ӿ����档
 * 
 * @author cn
 *
 * @param <K>
 * @param <V>
 */
public interface CacheRemovedHandle<K, V> {
	public void recordRemoved(K key, V value);
}
