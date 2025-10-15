
package xbean;

public interface BJTeamInfo extends mkdb.Bean {
	public BJTeamInfo copy(); // deep clone
	public BJTeamInfo toData(); // a Data instance
	public BJTeamInfo toBean(); // a Bean instance
	public BJTeamInfo toDataIf(); // a Data instance If need. else return this
	public BJTeamInfo toBeanIf(); // a Bean instance If need. else return this

	public int getServiceid(); // 
	public java.util.List<Long> getBjdata(); // 
	public java.util.List<Long> getBjdataAsData(); // 

	public void setServiceid(int _v_); // 
}
