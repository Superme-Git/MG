package fire.pb.tel.utils;

/**
 * 验证码
 * @author yangtao
 * @dateTime 2016年8月15日 下午10:58:39
 * @version 1.0
 */
public class CodeBean {

	/**
	 * 创建时间
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:58:12
	 * @version 1.0
	 */
	public long createTime = 0l;

	/**
	 * 验证码
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:58:32
	 * @version 1.0
	 */
	public String code;
	
	/**
	 * 倒计时完成时间
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午6:37:58
	 * @version 1.0
	 */
	public long finishTime;

	/**
	 * 绑定的手机号
	 * @author yangtao
	 * @dateTime 2016年8月29日 下午12:46:51
	 * @version 1.0
	 */
	public long bindTel;
}
