
package xbean;

public interface Item extends mkdb.Bean {
	public Item copy(); // deep clone
	public Item toData(); // a Data instance
	public Item toBean(); // a Bean instance
	public Item toDataIf(); // a Data instance If need. else return this
	public Item toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 物品编号
	public int getFlags(); // 标志，叠加的时候，flags 也 OR 叠加
	public int getPosition(); // 背包属性，位置。从0开始编号
	public int getNumber(); // 数量
	public java.util.Map<Integer, Integer> getNumbermap(); // 数量
	public java.util.Map<Integer, Integer> getNumbermapAsData(); // 数量
	public long getTimeout(); // 到期时间。如果为0，代表没有时间限制
	public long getExtid(); // 扩展动态属性
	public long getUniqueid(); // 物品的唯一id
	public long getLoseeffecttime(); // 物品失效时间
	public long getMarkettime(); // 摆摊道具冻结时间
	public int getTypeid(); // 物品类型

	public void setId(int _v_); // 物品编号
	public void setFlags(int _v_); // 标志，叠加的时候，flags 也 OR 叠加
	public void setPosition(int _v_); // 背包属性，位置。从0开始编号
	public void setNumber(int _v_); // 数量
	public void setTimeout(long _v_); // 到期时间。如果为0，代表没有时间限制
	public void setExtid(long _v_); // 扩展动态属性
	public void setUniqueid(long _v_); // 物品的唯一id
	public void setLoseeffecttime(long _v_); // 物品失效时间
	public void setMarkettime(long _v_); // 摆摊道具冻结时间
	public void setTypeid(int _v_); // 物品类型
}
