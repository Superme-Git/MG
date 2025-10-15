
package xbean;

public interface Compensation extends mkdb.Bean {
	public Compensation copy(); // deep clone
	public Compensation toData(); // a Data instance
	public Compensation toBean(); // a Bean instance
	public Compensation toDataIf(); // a Data instance If need. else return this
	public Compensation toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmap(); // 领取补偿的id
	public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmapAsData(); // 领取补偿的id

}
