/**
 * 
 */
package xio;

/**
 *
 */
public interface ILoginIns 
{
		public	class Param extends Object
		{
			// 网络参数
			public String host;
			public String address;
			public String port;

			// 登陆验证参数
			public String username;
			public String password;
			public boolean  isCheckVersion;
			public int      version;
			public boolean  iskickuser; // 是否强制登陆。
			public int		logintype;  //登录类型 0:normal login  1:auany login
			public String	mid;//账号的媒体标识，默认为locojoy
			public int		reserved1;//保留字段1
			public String 	reserved2;//保留字段2
			public Param()
			{
				version = 0;
				isCheckVersion = false;
				iskickuser = false;
				logintype = 0;
				mid = "locojoy";
				reserved1 = 0;
				reserved2 = "";
			}
			public Param( Param pa )
			{
				this.host = pa.host;
				this.address = pa.address;
				this.port = pa.port;
				this.username = pa.username;
				this.password = pa.password;
				this.isCheckVersion = pa.isCheckVersion;
				this.version = pa.version;
				this.iskickuser = pa.iskickuser;
				this.logintype = pa.logintype;
				this.mid = pa.mid;
				this.reserved1 = pa.reserved1;
				this.reserved2 = pa.reserved2;
			}
			
			public void set(Param pa)
			{
				this.host = pa.host;
				this.address = pa.address;
				this.port = pa.port;
				this.username = pa.username;
				this.password = pa.password;
				this.isCheckVersion = pa.isCheckVersion;
				this.version = pa.version;
				this.iskickuser = pa.iskickuser;
				this.logintype = pa.logintype;
				this.mid = pa.mid;
				this.reserved1 = pa.reserved1;
				this.reserved2 = pa.reserved2;
			}
		};
		
		void start();
		
		public void start(int inputBufferSize, int outputBufferSize, 
				int receiveBufferSize, int sendBufferSize);
		
		void close();
		
		void sendMatrixCardResponse(int[] v);
		
		void sendHandSetResponse(int num);

		void instantAddCash(String cardnum, String cardpasswd);

		boolean send(xio.Protocol p);

		xio.Connector getConnector();
		
		xio.Xio getXio();
		// 防沉迷
		public class Addiction
		{
			public enum Rate
			{
				eRate100, // 正常游戏，100%收益。
				eRate50,  // 沉迷状态，50%收益。
				eRate0    // 沉迷状态，0%收益。
			};
			int rate;        // 当前所处的沉迷状态。
			int msg;         // 未用。
			int timeplay;    // 已经在线的时间。
			java.util.Date time50;   // 到达50%收益的时间。如果rate已经是50%经验，那么这个时间点将小于当前时间。
			java.util.Date time0;    // 到达0%收益的时间。如果rate已经是0%经验，那么这个时间点将小于当前时间。

			Addiction() 
			{ 
				rate = 0; msg = 0; timeplay = 0; 
				time50 = new java.util.Date(); 
				time0 = new java.util.Date(); 
			}
		};
		boolean getAddiction(Addiction adict);

		//服务器信息
		public class ServerInfo 
		{
			public enum Award
			{
				eDoubleExp,	//双倍经验
				eDoubleMoney, //双倍金钱
				eDoubleDrop,	//双倍掉落
				eDoubleSp,	//双倍技能点
			};
			int flags; //当前服务器状态（即上面列出的状态）
			char load; //当前服务器负载 1-green 2-yellow 3-red

			
			java.util.Map<Integer, Integer> extra;

			ServerInfo() 
			{
				flags = 0; load = 0;
			}
		};

		boolean getServerInfo(ServerInfo info); // onAuthError(eServerAttr, ...) 以后信息就准备好了。
		int getServerVersion(); // 与 getServerInfo 同样的时机调用。

		public enum Algorithm
		{
			ALGORITHM_NONE, //        =  0x0, //只有密码
			ALGORITHM_PHONE, //		  =  0x00000100,//电话密保
			ALGORITHM_CARD, //        =  0x00010000,//密码+密保卡
			ALGORITHM_HANDSET, //     =  0x00020000,//密码+手机密保
			ALGORITHM_USBKEY, //      =  0x00030000,//usbkey即神盾保护

			ALGORITHM_NEED_MODIFY_PASSWD, // = 0x00000200,//需要提示更改密码
			ALGORITHM_NEED_MODIFY_MATRIX, // = 0x00000400,//需要提示更换密保卡

		};
		//if(getSecureLevel() & ALGORITHM_NEED_MODIFY_PASSWD == ALGORITHM_NEED_MODIFY_PASSWD) 则提示更改密码
		//if(getSecureLevel() & ALGORITHM_NEED_MODIFY_MATRIX == ALGORITHM_NEED_MODIFY_PASSWD) 则提示更换密保卡
		Algorithm getSecureLevel(); //获取帐号的密保等级  

		// 返回当前连接的ping。内部定时检查。
		int getServerPing();

		// 设置保活间隔。单位毫秒。默认值为5000。
		//   间隔最小值为1000，内部实现时修正太小的间隔。
		//   当 period <= 0     停止保活发送。同时停止检测 ServerPing。
		void setupKeepAlive(int period);

		// 登陆成功。返回当前服务器的地址。
		String getHostAddress();

		boolean isCanEnterWorld();
		//public aio::Protocol::Manager::Session::ID GetSessionID() = 0;
		
		public ILoginUI getLoginUI();
		
		public Param getParam();
}
