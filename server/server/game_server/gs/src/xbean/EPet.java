
package xbean;

public interface EPet extends mkdb.Bean {
	public EPet copy(); // deep clone
	public EPet toData(); // a Data instance
	public EPet toBean(); // a Bean instance
	public EPet toDataIf(); // a Data instance If need. else return this
	public EPet toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Float> getEffects(); // key = effect type id
	public java.util.Map<Integer, Float> getEffectsAsData(); // key = effect type id
	public java.util.Map<Integer, Float> getFinalattrs(); // key = attr type
	public java.util.Map<Integer, Float> getFinalattrsAsData(); // key = attr type

}
