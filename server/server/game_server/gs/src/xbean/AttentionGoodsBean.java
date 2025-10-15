
package xbean;

public interface AttentionGoodsBean extends mkdb.Bean {
	public AttentionGoodsBean copy(); // deep clone
	public AttentionGoodsBean toData(); // a Data instance
	public AttentionGoodsBean toBean(); // a Bean instance
	public AttentionGoodsBean toDataIf(); // a Data instance If need. else return this
	public AttentionGoodsBean toBeanIf(); // a Bean instance If need. else return this

	public String getId(); // 物品类型+数据库id
	public com.locojoy.base.Octets getIdOctets(); // 物品类型+数据库id
	public long getShowtime(); // 公示时间
	public long getExpiretime(); // 物品过期时间

	public void setId(String _v_); // 物品类型+数据库id
	public void setIdOctets(com.locojoy.base.Octets _v_); // 物品类型+数据库id
	public void setShowtime(long _v_); // 公示时间
	public void setExpiretime(long _v_); // 物品过期时间
}
