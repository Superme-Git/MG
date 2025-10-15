
package xbean;

public interface LianyunAwardInfo extends mkdb.Bean {
	public LianyunAwardInfo copy(); // deep clone
	public LianyunAwardInfo toData(); // a Data instance
	public LianyunAwardInfo toBean(); // a Bean instance
	public LianyunAwardInfo toDataIf(); // a Data instance If need. else return this
	public LianyunAwardInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getAwards(); // 
	public java.util.Map<Integer, Long> getAwardsAsData(); // 

}
