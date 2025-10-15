
package xbean;

public interface SRRedPackNumList extends mkdb.Bean {
	public SRRedPackNumList copy(); // deep clone
	public SRRedPackNumList toData(); // a Data instance
	public SRRedPackNumList toBean(); // a Bean instance
	public SRRedPackNumList toDataIf(); // a Data instance If need. else return this
	public SRRedPackNumList toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlist(); // 
	public java.util.Map<Integer, xbean.SRRedPackNum> getSrredpacknumlistAsData(); // 

}
