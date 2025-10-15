package fire.pb.shop.srv.market.jdbc;

/**
 * 
 * 嵌入式关系型数据库访问接口
 * 
 * @author liangyanpeng
 *
 */
public interface DAO {
	
	/**
	 * 系统启动时初始化数据表
	 */
	public void createTable();
	
	/**
	 * 从xdb中将数据初始化到内存中
	 * @return 成功true
	 */
	public boolean initializationDataFromXdb();
	
	/**
	 * 将内存中数据持久到xdb中
	 * @return 成功true
	 */
	public boolean persistenceDataToXdb();
	
	
}
