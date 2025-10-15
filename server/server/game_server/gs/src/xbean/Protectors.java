
package xbean;

public interface Protectors extends mkdb.Bean {
	public Protectors copy(); // deep clone
	public Protectors toData(); // a Data instance
	public Protectors toBean(); // a Bean instance
	public Protectors toDataIf(); // a Data instance If need. else return this
	public Protectors toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getProtectorlist(); // 
	public java.util.List<Integer> getProtectorlistAsData(); // 

}
