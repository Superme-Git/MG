
package xbean;

public interface BiaoQing extends mkdb.Bean {
	public BiaoQing copy(); // deep clone
	public BiaoQing toData(); // a Data instance
	public BiaoQing toBean(); // a Bean instance
	public BiaoQing toDataIf(); // a Data instance If need. else return this
	public BiaoQing toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getBiaoqinginfos(); // 
	public java.util.Map<Integer, Long> getBiaoqinginfosAsData(); // 

}
