package mkdb;

/**
 * Cache �����ص��ӿڡ�
 * 
 * ��ֻ�ڻص�ʱ���֣�Ȼ���ڻص�֮���ͷŵ���
 * 
 * �����޸ġ�
 * ���ܱ���ǳ����������á�
 * 
 * @author cn
 *
 */
public interface CacheQuery<K, V> {
	public void onQuery(K key, V value);
}
