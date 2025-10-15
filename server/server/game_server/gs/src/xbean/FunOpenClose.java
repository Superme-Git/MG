
package xbean;

public interface FunOpenClose extends mkdb.Bean {
	public FunOpenClose copy(); // deep clone
	public FunOpenClose toData(); // a Data instance
	public FunOpenClose toBean(); // a Bean instance
	public FunOpenClose toDataIf(); // a Data instance If need. else return this
	public FunOpenClose toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getFunmap(); // 
	public java.util.Map<Integer, Integer> getFunmapAsData(); // 

}
