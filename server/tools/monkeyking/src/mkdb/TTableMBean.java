package mkdb;

/**
 * TTable jmx ����ӿ� 
 * @author cn
 *
 */
public interface TTableMBean {
	public String getTableName();
	public String getLockName();
	public String getPersistenceName();

	// cache info
	public int getCacheCapacity();
	public void setCacheCapacity(int capacity); // only for debug.
	public int getCacheSize();
	public String getCacheClassName();

	// table access counter
	public long getCountAdd();               // add ����
	public long getCountAddMiss();           // add ������ cache ����
	public long getCountAddStorageMiss();    // add ���ݿ����Ѿ����� key ����

	public long getCountGet();               // get ����
	public long getCountGetMiss();           // get ������ cache ����
	public long getCountGetStorageMiss();    // get ���ݿ��в����� key ����

	public long getCountRemove();            // remove ����
	public long getCountRemoveMiss();        // remove ������ cache ����
	public long getCountRemoveStorageMiss(); // remove ���ݿ��в����� key ����

	// ����ֵ������쿴
	public String getPercentAddHit();        // AddHit = Add - AddMiss, AddHit / Add
	public String getPercentGetHit();        // GetHit = Get - GetMiss, GetHit / Get
	public String getPercentRemoveHit();     // RemoveHit = Remove - RemoveMiss, RemoveHit / Remove
	public String getPercentCacheHit();      // (AddHit + RemoveHit + GetHit) / (Add + Remove + Get)

	// Storage
	public long getStorageCountMarshal0();
	public long getStorageCountMarshalN();
	public long getStorageCountMarshalNTryFail();
	public long getStorageCountFlush();
	public long getStorageCountSnapshot();
	public long getStorageFlushKeySize();
	public long getStorageFlushValueSize();
}
