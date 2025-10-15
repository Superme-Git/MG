
package xbean;

public interface HasApprent extends mkdb.Bean {
	public HasApprent copy(); // deep clone
	public HasApprent toData(); // a Data instance
	public HasApprent toBean(); // a Bean instance
	public HasApprent toDataIf(); // a Data instance If need. else return this
	public HasApprent toBeanIf(); // a Bean instance If need. else return this

	public String getName(); // 
	public com.locojoy.base.Octets getNameOctets(); // 
	public int getLevel(); // 
	public int getSchool(); // 
	public long getRoleid(); // 
	public int getCamp(); // 
	public int getShap(); // 

	public void setName(String _v_); // 
	public void setNameOctets(com.locojoy.base.Octets _v_); // 
	public void setLevel(int _v_); // 
	public void setSchool(int _v_); // 
	public void setRoleid(long _v_); // 
	public void setCamp(int _v_); // 
	public void setShap(int _v_); // 
}
