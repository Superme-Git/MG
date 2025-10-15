
package xbean;

public interface KickInfo extends mkdb.Bean {
	public KickInfo copy(); // deep clone
	public KickInfo toData(); // a Data instance
	public KickInfo toBean(); // a Bean instance
	public KickInfo toDataIf(); // a Data instance If need. else return this
	public KickInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getKicktime(); // 踢人信息 by changhao
	public java.util.List<Long> getKicktimeAsData(); // 踢人信息 by changhao
	public long getExpire(); // 10分钟内不让登陆 by changhao

	public void setExpire(long _v_); // 10分钟内不让登陆 by changhao
}
