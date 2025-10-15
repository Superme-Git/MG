
package xbean;

public interface Pets extends mkdb.Bean {
	public Pets copy(); // deep clone
	public Pets toData(); // a Data instance
	public Pets toBean(); // a Bean instance
	public Pets toDataIf(); // a Data instance If need. else return this
	public Pets toBeanIf(); // a Bean instance If need. else return this

	public int getCapacity(); // 
	public int getNextid(); // 下一个id
	public java.util.Map<Integer, xbean.PetInfo> getPetmap(); // 
	public java.util.Map<Integer, xbean.PetInfo> getPetmapAsData(); // 

	public void setCapacity(int _v_); // 
	public void setNextid(int _v_); // 下一个id
}
