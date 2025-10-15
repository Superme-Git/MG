/**
 * 
 */
package xmerge;

/**
 * @author CaijiaCheng
 * 调用方式：
 * 				ICheckDupKey check = new CheckDupKey();
 * 				check.checkDB( src, dst );
 * 				check.getDupKeyDB();
 */
	
public interface ICheckDupKey {
	/**
	 *  检测源数据库与目标数据库中相同表重复的key
	 *  note: 检测不会改变srcDB, dstDb中的表，但会清空errDb中的表. 只检测!known_key_conflict
	 * @param srcDb : 源数据库 
	 * @param dstDb : 目标数据库
	 * @return ：成功true
	 * 			失败: false代表没有检测到任何的dupkey
	 * 			
	 */
	public boolean checkDB( String srcDb, String dstDb );
	
	/**
	 * 
	 * @return 设定errDB的路径。(默认为相对路径: errDB)。
	 */
	public void setDupKeyDBPath(String path);
			
	/**
	 * 
	 * @return 返回errDB的路径。(相对路径)。
	 */
	public String getDupKeyDBPath();
		
	/**
	 * @param tablename : 数据库表名
	 * @return : 取得相应数据库表名的重复DupKey
	 * 			 如果不存在相应的tablename，将抛出异常
	 * @throws Exception 
	 */
	public long getDupKeyCount( String tablename ) throws Exception;
	
	
	
}
