
package xbean;

public interface VecList extends mkdb.Bean {
	public VecList copy(); // deep clone
	public VecList toData(); // a Data instance
	public VecList toBean(); // a Bean instance
	public VecList toDataIf(); // a Data instance If need. else return this
	public VecList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.MazeInfo> getList(); // 
	public java.util.List<xbean.MazeInfo> getListAsData(); // 

}
