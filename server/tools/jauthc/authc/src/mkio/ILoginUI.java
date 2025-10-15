/**
 * 
 */
package mkio;

/**
 *
 */
public interface ILoginUI 
{
	
		public enum Action
		{
			eConnect, // 连接服务器错误。
			eServer,  // 服务器内部错误，收到gnet::ErrorInfo协议
			eNet,     // 网络错误，连接建立以后收发错误。
			eTimeout, // 超时错误。
			eVersion, // 版本不匹配。
			eProtocol, // 协议处理错误
			eServerAttr, // newLogin 时不提供用户帐号，得到服务器信息以后连接马上会被关闭，用来检测服务器状态。
		};
	
		void onAuthHandSet(int num);
		void onAuthMatrixCard(int[] x, int[] y);
		void onAuthOk(int userid);

		void onAuthError(Action a, int e, String detail);

		void onAnnounceForbidInfo(char type, int time, int createtime, String reason);
		
		
		/************************************************************************/
		/* InstantAddCashRep的retcode含义如下：
		0.成功 
		1.卡号不存在 
		2.用户不存在 
		3.计费区不存在 
		4.密码错误 
		5.该卡已过期 
		6.规定时间内同一张卡充值次数超限（防止试充值卡密码）
		11.用户在该服务器已有消费在等待划拨，请稍候再试 
		12. 用户已有点卡充值未处理完，请稍候再试 
		13. 用户被封禁，不能充值 
		-1.其他错误
	    */
		/************************************************************************/
		void onInstantAddCashRep(int retcode, int userid, int reserved);

		void dispatch(mkio.Manager manager, mkio.Mkio connection, mkio.Protocol p);
		/*{
			mkdb.Xdb.executor().execute(p);
		}*/
}
