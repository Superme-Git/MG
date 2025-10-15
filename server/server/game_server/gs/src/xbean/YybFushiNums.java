
package xbean;

public interface YybFushiNums extends mkdb.Bean {
	public YybFushiNums copy(); // deep clone
	public YybFushiNums toData(); // a Data instance
	public YybFushiNums toBean(); // a Bean instance
	public YybFushiNums toDataIf(); // a Data instance If need. else return this
	public YybFushiNums toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.YybFushiNum> getRolefushi(); // 
	public java.util.Map<Long, xbean.YybFushiNum> getRolefushiAsData(); // 

}
