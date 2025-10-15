
package xbean;

public interface MedicItemList extends mkdb.Bean {
	public MedicItemList copy(); // deep clone
	public MedicItemList toData(); // a Data instance
	public MedicItemList toBean(); // a Bean instance
	public MedicItemList toDataIf(); // a Data instance If need. else return this
	public MedicItemList toBeanIf(); // a Bean instance If need. else return this

	public long getMedicitemrefreshtime(); // 刷新时间
	public int getSelecttype(); // 选择几倍产药类型   0正常  1双倍    2三倍
	public java.util.Map<Integer, xbean.MedicItem> getMedicitems(); // 道具
	public java.util.Map<Integer, xbean.MedicItem> getMedicitemsAsData(); // 道具

	public void setMedicitemrefreshtime(long _v_); // 刷新时间
	public void setSelecttype(int _v_); // 选择几倍产药类型   0正常  1双倍    2三倍
}
