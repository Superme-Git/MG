
package xbean;

public interface BingFengWangZuo extends mkdb.Bean {
	public BingFengWangZuo copy(); // deep clone
	public BingFengWangZuo toData(); // a Data instance
	public BingFengWangZuo toBean(); // a Bean instance
	public BingFengWangZuo toDataIf(); // a Data instance If need. else return this
	public BingFengWangZuo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Integer> getDeathtimes(); // 死亡次数 key为角色id
	public java.util.Map<Long, Integer> getDeathtimesAsData(); // 死亡次数 key为角色id

}
