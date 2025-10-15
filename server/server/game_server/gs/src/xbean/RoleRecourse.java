
package xbean;

public interface RoleRecourse extends mkdb.Bean {
	public RoleRecourse copy(); // deep clone
	public RoleRecourse toData(); // a Data instance
	public RoleRecourse toBean(); // a Bean instance
	public RoleRecourse toDataIf(); // a Data instance If need. else return this
	public RoleRecourse toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetask(); // 
	public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetaskAsData(); // 

}
