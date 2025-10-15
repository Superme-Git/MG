
package xbean;

public interface ERole extends mkdb.Bean {
	public ERole copy(); // deep clone
	public ERole toData(); // a Data instance
	public ERole toBean(); // a Bean instance
	public ERole toDataIf(); // a Data instance If need. else return this
	public ERole toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Float> getFinalattrs(); // 最终属性 key = attr type
	public java.util.Map<Integer, Float> getFinalattrsAsData(); // 最终属性 key = attr type
	public java.util.Map<Integer, Float> getEffects(); // key = effect type id
	public java.util.Map<Integer, Float> getEffectsAsData(); // key = effect type id

}
