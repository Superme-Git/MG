
package xbean;

public interface WeiBoNotify extends mkdb.Bean {
	public WeiBoNotify copy(); // deep clone
	public WeiBoNotify toData(); // a Data instance
	public WeiBoNotify toBean(); // a Bean instance
	public WeiBoNotify toDataIf(); // a Data instance If need. else return this
	public WeiBoNotify toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.WeiBoChoice> getWeibo(); // key=类型 value=微博相关数据
	public java.util.Map<Integer, xbean.WeiBoChoice> getWeiboAsData(); // key=类型 value=微博相关数据
	public int getTakeawardflag(); // 1=可以领奖  2=领取过了

	public void setTakeawardflag(int _v_); // 1=可以领奖  2=领取过了
}
