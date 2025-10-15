
package xbean;

public interface HuoBanZhenrongInfo extends mkdb.Bean {
	public HuoBanZhenrongInfo copy(); // deep clone
	public HuoBanZhenrongInfo toData(); // a Data instance
	public HuoBanZhenrongInfo toBean(); // a Bean instance
	public HuoBanZhenrongInfo toDataIf(); // a Data instance If need. else return this
	public HuoBanZhenrongInfo toBeanIf(); // a Bean instance If need. else return this

	public int getZhenfa(); // 阵容光环编号
	public java.util.List<Integer> getHuoban(); // value-伙伴id
	public java.util.List<Integer> getHuobanAsData(); // value-伙伴id

	public void setZhenfa(int _v_); // 阵容光环编号
}
