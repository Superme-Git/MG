
package xbean;

public interface AttentionGoods extends mkdb.Bean {
	public AttentionGoods copy(); // deep clone
	public AttentionGoods toData(); // a Data instance
	public AttentionGoods toBean(); // a Bean instance
	public AttentionGoods toDataIf(); // a Data instance If need. else return this
	public AttentionGoods toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.AttentionGoodsBean> getBuyattentions(); // 购买关注,数据库表中的id,关注数量最多8个
	public java.util.List<xbean.AttentionGoodsBean> getBuyattentionsAsData(); // 购买关注,数据库表中的id,关注数量最多8个
	public java.util.List<xbean.AttentionGoodsBean> getPublicityattentions(); // 公示关注,数据库表中的id,关注数量最多8个
	public java.util.List<xbean.AttentionGoodsBean> getPublicityattentionsAsData(); // 公示关注,数据库表中的id,关注数量最多8个

}
