
package xbean;

public interface DebugFlag extends mkdb.Bean {
	public DebugFlag copy(); // deep clone
	public DebugFlag toData(); // a Data instance
	public DebugFlag toBean(); // a Bean instance
	public DebugFlag toDataIf(); // a Data instance If need. else return this
	public DebugFlag toBeanIf(); // a Bean instance If need. else return this

	public final static int BATTLE = 1; // 

	public java.util.List<Integer> getDebugs(); // 不在list中的为非调试状态
	public java.util.List<Integer> getDebugsAsData(); // 不在list中的为非调试状态

}
