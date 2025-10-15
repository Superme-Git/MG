package fire.pb.shop.srv;

/**
 * 购买
 * 
 * @author liangyanpeng
 *
 */
public interface Buy {

	/**
	 * 扣除
	 */
	public boolean cost();

	/**
	 * 成功
	 */
	public void success();

	/**
	 * 失败
	 */
	public void fail();

	/**
	 * 验证
	 */
	public boolean verify();
	
	/**
	 * @return 增加成功true，失败false
	 */
	public boolean add();
	
	/**
	 * 执行各个类别具体业务
	 */
	public boolean exc();

}
