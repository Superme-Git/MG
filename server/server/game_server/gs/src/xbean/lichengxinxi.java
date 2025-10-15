
package xbean;

public interface lichengxinxi extends mkdb.Bean {
	public lichengxinxi copy(); // deep clone
	public lichengxinxi toData(); // a Data instance
	public lichengxinxi toBean(); // a Bean instance
	public lichengxinxi toDataIf(); // a Data instance If need. else return this
	public lichengxinxi toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.Course> getLicheng(); // 
	public java.util.Map<Integer, xbean.Course> getLichengAsData(); // 

}
