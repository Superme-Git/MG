package xmerge;

/*
 * 	所有不是AutoKey类型的表，都需要为该表实现相应的IAllocator具体实现来解决该表中
 * 	可能存在的key冲突，并通过Xmerge.addAllocator(String tableName, IAllocator allocator)注册
 */
public interface IAllocator {
	/**
	 * 根据表名及oldKey分配新key
	 * @param tableName
	 * @param oldKey
	 * @return newKey
	 */
	public Object allocateKey(String tableName, Object oldKey); 
}