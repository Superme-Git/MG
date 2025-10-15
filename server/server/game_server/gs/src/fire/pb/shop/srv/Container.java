package fire.pb.shop.srv;

/**
 * 存储容器
 * 
 * @author liangyanpeng
 *
 */
public interface Container {
	
	/**
	 * 添加到容器中
	 * @param isBind 是否绑定
	 * @return 成功true,失败false
	 */
	public boolean add(boolean isBind);
	
	/**
	 * @param key 宠物栏、背包中的位置
	 * @return 从容器中移除
	 */
	public boolean remove(int key);

}
