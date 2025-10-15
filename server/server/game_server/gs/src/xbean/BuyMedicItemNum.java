
package xbean;

public interface BuyMedicItemNum extends mkdb.Bean {
	public BuyMedicItemNum copy(); // deep clone
	public BuyMedicItemNum toData(); // a Data instance
	public BuyMedicItemNum toBean(); // a Bean instance
	public BuyMedicItemNum toDataIf(); // a Data instance If need. else return this
	public BuyMedicItemNum toBeanIf(); // a Bean instance If need. else return this

	public int getBuynum(); // 药房道具购买数量
	public int getImpeachdaynum(); // 发起弹劾次数

	public void setBuynum(int _v_); // 药房道具购买数量
	public void setImpeachdaynum(int _v_); // 发起弹劾次数
}
