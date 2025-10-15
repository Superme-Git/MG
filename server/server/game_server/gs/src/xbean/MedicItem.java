
package xbean;

public interface MedicItem extends mkdb.Bean {
	public MedicItem copy(); // deep clone
	public MedicItem toData(); // a Data instance
	public MedicItem toBean(); // a Bean instance
	public MedicItem toDataIf(); // a Data instance If need. else return this
	public MedicItem toBeanIf(); // a Bean instance If need. else return this

	public int getItemid(); // 药品id
	public int getItemnum(); // 药品数量

	public void setItemid(int _v_); // 药品id
	public void setItemnum(int _v_); // 药品数量
}
