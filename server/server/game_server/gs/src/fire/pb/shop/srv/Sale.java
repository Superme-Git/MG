package fire.pb.shop.srv;

/**
 * 
 * 售卖
 * 
 * @author liangyanpeng
 *
 */
public interface Sale {

	
	/**
	 * @return 删除商品成功true,失败false 
	 */
	public boolean removeGoods();
	
	/**
	 * @return 增加收益成功true,失败false
	 */
	public boolean add();
	
	/**
	 * @return 验证成功true,失败false
	 */
	public boolean verify();
	
	/**
	 * 成功
	 */
	public void success();

	/**
	 * 失败
	 */
	public void fail();

	/**
	 * 执行各个类别具体业务
	 */
	public boolean exc();
}
