
package xbean;

public interface RollItem extends mkdb.Bean {
	public RollItem copy(); // deep clone
	public RollItem toData(); // a Data instance
	public RollItem toBean(); // a Bean instance
	public RollItem toDataIf(); // a Data instance If need. else return this
	public RollItem toBeanIf(); // a Bean instance If need. else return this

	public int getItemid(); // 道具表id by changhao
	public int getItemnum(); // 数量id by changhao
	public int getCountertype(); // 
	public int getXiangguanid(); // 
	public int getAwardid(); // 

	public void setItemid(int _v_); // 道具表id by changhao
	public void setItemnum(int _v_); // 数量id by changhao
	public void setCountertype(int _v_); // 
	public void setXiangguanid(int _v_); // 
	public void setAwardid(int _v_); // 
}
