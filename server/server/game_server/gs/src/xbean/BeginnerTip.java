
package xbean;

public interface BeginnerTip extends mkdb.Bean {
	public BeginnerTip copy(); // deep clone
	public BeginnerTip toData(); // a Data instance
	public BeginnerTip toBean(); // a Bean instance
	public BeginnerTip toDataIf(); // a Data instance If need. else return this
	public BeginnerTip toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getTips(); // 
	public java.util.Map<Integer, Integer> getTipsAsData(); // 

}
