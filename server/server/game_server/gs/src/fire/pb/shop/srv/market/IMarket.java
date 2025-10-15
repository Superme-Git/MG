package fire.pb.shop.srv.market;

import java.util.ArrayList;
import java.util.List;

import fire.pb.shop.MarketSearchAttr;

/**
 * 拍卖接口
 * 
 * @author liangyanpeng
 */
public interface IMarket {

	/**
	 * <b>事务内调用</b>
	 * 物品上架(宠物和普通道具)
	 * @param upType 上架类型     1 背包容器    2 宠物栏容器
	 * @param roleId 角色id
	 * @param key  容器中的key
	 * @param number  道具数量
	 * @param moeny 金额
	 * @return 成功true
	 */
	public boolean up(int upType, long roleId, int key, int number, int money);
	
	/**
	 * <b>事务内调用</b>
	 * 物品下架(宠物、装备、普通道具)
	 * @param downType 下架类型  1 道具     2 宠物    3 装备
	 * @param roleId 角色id
	 * @param key  容器中的key
	 * @param number  道具数量
	 * @return 成功true, 失败false
	 */
	public boolean down(int downType, long roleId, int key);
	
	/**
	 * <b>事务内调用</b>
	 * 拍卖购买
	 * @param buyRoleId  买角色id
	 * @param id    数据库唯一id
	 * @param saleRoleId 卖角色id
	 * @param itemid   道具id
	 * @param number   道具数量
	 * @return  成功true, 失败false
	 */
	public boolean buy(long buyRoleId, long id, long saleRoleId, int itemid, int number);
	
	/**
	 * <b>事务内调用</b>
	 * 点卡拍卖购买
	 * @param buyRoleId  买角色id
	 * @param id    数据库唯一id
	 * @param saleRoleId 卖角色id
	 * @param itemid   道具id
	 * @param number   道具数量
	 * @return  成功true, 失败false
	 */
	public boolean normalBuy(long buyRoleId, long id, long saleRoleId, int itemid, int number);
	
	/**
	 * <b>事务内调用</b>
	 * 物品重新上架(宠物和普通道具)
	 * @param itemType 道具类型(装备,普通道具,宠物)用来区分定位查找相应的表
	 * @param roleId 角色id
	 * @param uniqueId 道具唯一id
	 * @param itemId  道具id
	 * @param key  容器中的key
	 * @return 成功true, 失败false
	 */
	public boolean remarketUp(int itemType, long roleId, long id, int money);
	
	/**
	 * <b>事务内调用</b>
	 * 关注或取消关注商品
	 * @param itemType 道具类型(1普通道具,2宠物,3装备)用来区分定位查找相应的表
	 * @param attentiontype  关注类型 1关注  2取消关注
	 * @param roleId
	 * @param id    数据库唯一id
	 * @param attentype 关注状态 1购买，2公示
	 * @return  成功true, 失败false
	 */
	public boolean attention(int itemType, int attentiontype, long roleId, long id, int attentype);
	
	/**
	 * @param roleId
	 * @param euqiptype	类型
	 * @param level	等级
	 * @param pricemin 最小价格
	 * @param pricemax 最大价格
	 * @param effect 特效
	 * @param skill 特技
	 * @param colors  品质, 4紫, 5橙
	 * @param basicattrs 基础属性
	 * @param additionalattrs 附加属性
	 * @param totalattr 属性总和
	 * @param score 评分
	 * @param sellstate 出售状态 1上架,2公示
	 * @return 装备搜索结果
	 */
	public boolean searchEquip(long roleId, int euqipType, int level, int priceMin, int priceMax, int effect, int skill,
			List<Integer> colors, List<fire.pb.shop.MarketSearchAttr> basicAttrs,
			List<Integer> additionalAttrs, int totalAttr, int score, int sellState);
	
	/**
	 * @param roleId
	 * @param petType  宠物道具id
	 * @param levelMin 最低等级
	 * @param levelMax 最高等级
	 * @param priceMin 最低价格
	 * @param priceMax 最高价格
	 * @param talents 资质成长
	 * @param attrs 基础属性
	 * @param skills 包含技能
	 * @param skillNumber 技能总数
	 * @param score 评分
	 * @param sellState 出售状态 1上架，2公示
	 * @return 宠物搜索结果
	 */
	public boolean searchPet(long roleId, int petType, int levelMin, int levelMax, int priceMin, int priceMax,
			List<MarketSearchAttr> talents, List<Integer> skills, int skillNumber, List<MarketSearchAttr> attrs,
			int score, int sellState);
	
	/**
	 * 浏览拍卖
	 * 
	 * @param firstno 一级
	 * @param twono 二级
	 * @param threeno 三级
	 * @param itemtype 类型
	 * @param limitmin 条件下限
	 * @param limitmax 条件上限
	 * @param browsetype 浏览类型 1 我要购买 2公示物品
	 * @param priceSort 价格排序  1升序  2降序
	 */
	public void marketBrowse(long roleId, int firstno, int twono, ArrayList<Integer> threeno, int itemtype,
			int limitmin, int limitmax, int browsetype, int currpage, int priceSort, int issearch);
	
	/**
	 * 聊天点击拍卖的道具, 定位道具所在位置浏览
	 * @param roleId 
	 * @param id 数据库唯一ID
	 * @return  返回浏览道具的定位数据
	 */
	public void chatBrowseMarketItem(long roleId, long id, int itemType);
}
