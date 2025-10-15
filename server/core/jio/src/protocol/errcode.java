
package protocol;

/*
 * java 版本的错误码定义，为了兼容旧的系统，如au等，全部都复制过来，
 * 添加错误码的时候需要两边同步。
 * share/rpc/errcode.h
 */

public class errcode
{
	public static final int ERR_SUCCESS                = 0;  // 成功
	public static final int ERR_TOBECONTINUE           = 1;  // 成功，并且还有更多数据未传递完，目前未用

	public static final int ERR_INVALID_ACCOUNT        = 2;  // 帐号不存在
	public static final int ERR_INVALID_PASSWORD       = 3;  // 密码错误
	public static final int ERR_TIMEOUT                = 4;  // 超时
	public static final int ERR_INVALID_ARGUMENT       = 5;  // 参数错误
	public static final int ERR_FRIEND_SYNCHRONIZE     = 6;  // 好友信息保存到数据库时无法同步
	public static final int ERR_SERVERNOTSUPPLY        = 7;  // 该服务器不支持该请求
	public static final int ERR_COMMUNICATION          = 8;  // 网络通讯错误
	public static final int ERR_ACCOUNTLOCKED          = 9;  // 多次重复登陆，当前用户的一个登陆正在被处理，处于锁定状态
	public static final int ERR_MULTILOGIN             = 10; // 多次重复登陆，且用户选择自动下线
	// keyexchange
	public static final int ERR_INVALID_NONCE          = 11; //无效的nonce值
	//logout
	public static final int ERR_LOGOUT_FAIL            = 12; //AUTH登出失败
	//login Gameserver
	public static final int ERR_GAMEDB_FAIL            = 13; //游戏服务器获取玩家角色信息失败
	public static final int ERR_ENTERWORLD_FAIL        = 14; //登陆游戏服务器失败
	public static final int ERR_EXCEED_MAXNUM          = 15; //游戏服务器人数已达上限
	public static final int ERR_IN_WORLD               = 16; //玩家角色已经在世界
	public static final int ERR_EXCEED_LINE_MAXNUM	   = 17; //该线角色数已达到上限
	public static final int ERR_INVALID_LINEID	   = 18; //没有这个线号
	public static final int ERR_NO_LINE_AVALIABLE	   = 19; //没有可用的线
	// deliver use
	public static final int ERR_DELIVER_SEND           = 21; // 转发失败
	public static final int ERR_DELIVER_TIMEOUT        = 22; // 转发超时
	//account
	public static final int ERR_ACCOUNTEMPTY           = 23; //帐户余额不足
	public static final int ERR_ACCOUNTFORBID          = 24; //帐号被GameMaster锁定，不允许登陆
	public static final int ERR_INVALIDCHAR            = 25; //文字中含有非法的字符
	// LoginCheck
	public static final int ERR_IP_LOCK		   = 26;
	public static final int ERR_ID_LOCK		   = 27;
	public static final int ERR_MATRIXFAILURE	   = 28;

	//player login
	public static final int ERR_LOGINFAIL              = 31; //登陆游戏失败
	public static final int ERR_KICKOUT                = 32; //被踢下线
	public static final int ERR_CREATEROLE             = 33; //创建角色失败
	public static final int ERR_DELETEROLE             = 34; //删除角色失败
	public static final int ERR_ROLELIST               = 35; //获得角色列表失败
	public static final int ERR_UNDODELROLE            = 36; //撤销删除角色失败
	public static final int ERR_SRVMAINTAIN            = 37; //服务器维护中，暂时不能登陆
	public static final int ERR_ROLEFORBID             = 38; //角色被GM封杀，不允许登陆游戏
	public static final int ERR_SERVEROVERLOAD         = 39; //服务器人数已达上限
	public static final int ERR_ACKICKOUT              = 40; //被反外挂程序踢下线

	//DB retcode
	public static final int ERR_FAILED                 = 41; //一般错误
	public static final int ERR_EXCEPTION              = 42; //数据库异常
	public static final int ERR_NOTFOUND               = 43; //记录未找到
	public static final int ERR_INVALIDHANDLE          = 44; //错误的Handle
	public static final int ERR_DUPLICATRECORD         = 45; //记录序号重复
	public static final int ERR_NOFREESPACE            = 46; //没有剩余空间
	public static final int ERR_VERIFYFAILED           = 47; //数据校验错误
	public static final int ERR_DUPLICATE_ROLEID       = 48; //角色id重复
	public static final int ERR_AGAIN                  = 49; //数据库繁忙，稍后重试
	public static final int ERR_DATAERROR              = 50; //数据错误

	//add friend
	public static final int ERR_ADDFRD_REQUEST         = 51; //请求加为好友
	public static final int ERR_ADDFRD_REFUSE          = 52; //拒绝加为好友
	public static final int ERR_ADDFRD_AGREE           = 53; //同意加为好友

	public static final int ERR_COMMAND_COOLING        = 55; //命令尚在冷却中
	public static final int ERR_INSTANCE_OVERFLOW      = 56; //登录失败;副本数目达到上限

	//game DB
	public static final int ERR_DATANOTFIND            = 60; //数据不存在

	//other
	public static final int ERR_GENERAL                = 61; //未定义的一般性错误
	public static final int ERR_OUTOFSYNC              = 62; //数据不同步
	public static final int ERR_PERMISSION_DENIED      = 63; //没有权限
	public static final int ERR_DATABASE_TIMEOUT       = 64; //数据库超时
	public static final int ERR_UNAVAILABLE            = 65; //角色已婚，无法删除
	public static final int ERR_CMDCOOLING             = 66; //命令正在冷却中

	//Trade
	public static final int ERR_TRADE_PARTNER_OFFLINE  = 68; //对方已经下线
	public static final int ERR_TRADE_AGREE            = 0;  //同意交易
	public static final int ERR_TRADE_REFUSE           = 69; //对方拒绝交易
	public static final int ERR_TRADE_BUSY_TRADER      = 70; //trader 已经在交易中
	public static final int ERR_TRADE_DB_FAILURE       = 71; //读写数据库失败
	public static final int ERR_TRADE_JOIN_IN          = 72; //加入交易失败，交易对象的双方已经存在
	public static final int ERR_TRADE_INVALID_TRADER   = 73; //无效的交易者
	public static final int ERR_TRADE_ADDGOODS         = 74; //增加交易物品失败
	public static final int ERR_TRADE_RMVGOODS         = 75; //减少交易物品失败
	public static final int ERR_TRADE_READY_HALF       = 76; //提交完成一半，等待对方提交
	public static final int ERR_TRADE_READY            = 77; //提交完成
	public static final int ERR_TRADE_SUBMIT_FAIL      = 78; //提交失败
	public static final int ERR_TRADE_CONFIRM_FAIL     = 79; //确认失败
	public static final int ERR_TRADE_DONE             = 80; //交易完成
	public static final int ERR_TRADE_HALFDONE         = 81; //交易完成一半，等待另一方确认
	public static final int ERR_TRADE_DISCARDFAIL      = 82; //取消交易失败
	public static final int ERR_TRADE_MOVE_FAIL        = 83; //移动物品失败
	public static final int ERR_TRADE_SPACE            = 84; //物品栏空间不足
	public static final int ERR_TRADE_SETPSSN          = 85; //设置交易者财产错误
	public static final int ERR_TRADE_ATTACH_HALF      = 86; //成功加入一个一个交易者
	public static final int ERR_TRADE_ATTACH_DONE      = 87; //成功加入两个交易者
	public static final int ERR_TRADE_PARTNER_FORBID   = 88; //交易对象被GM封禁交易功能

	//faction error code (101-200) 
	public static final int ERR_FC_NETWORKERR          = 101; //服务器网络通讯错误
	public static final int ERR_FC_INVALID_OPERATION   = 102; //无效的操作类型
	public static final int ERR_FC_OP_TIMEOUT          = 103; //操作超时
	public static final int ERR_FC_CREATE_ALREADY      = 104; //玩家已经是某个公会的成员，不能再创建公会
	public static final int ERR_FC_CREATE_DUP          = 105; //公会名称重复
	public static final int ERR_FC_DBFAILURE           = 106; //数据库IO错误
	public static final int ERR_FC_NO_PRIVILEGE        = 107; //没有相关操作的权限
	public static final int ERR_FC_INVALIDNAME         = 108; //不能使用此名称
	public static final int ERR_FC_FULL                = 109; //成员已满
	public static final int ERR_FC_APPLY_REJOIN        = 110; //已经是某个公会的成员，申请失败
	public static final int ERR_FC_JOIN_SUCCESS        = 111; //成功加入公会
	public static final int ERR_FC_JOIN_REFUSE         = 112; //申请被拒绝
	public static final int ERR_FC_ACCEPT_REACCEPT     = 113; //被批准加入公会的玩家已经加入公会
	public static final int ERR_FC_FACTION_NOTEXIST    = 114; //公会不存在or玩家没有申请过加入本公会
	public static final int ERR_FC_NOTAMEMBER          = 115; //玩家不是本公会的帮众
	public static final int ERR_FC_CHECKCONDITION      = 116; //不满足操作条件，如SP不够，资金不够
	public static final int ERR_FC_DATAERROR           = 117; //操作参数类型错误，客户端提交的操作参数格式错误
	public static final int ERR_FC_OFFLINE             = 118; //玩家不在线
	public static final int ERR_FC_OUTOFSERVICE        = 119; //服务暂时不可用
	public static final int ERR_FC_INVITEELEVEL        = 120; //被邀请方级别不够，不能加入
	public static final int ERR_FC_PREDELSUCCESS       = 121; //公会解散成功，七天后正式解散
	public static final int ERR_FC_DISMISSWAITING      = 122; //公会正在解散中
	public static final int ERR_FC_INVITEENOFAMILY     = 123; //被邀请人没有加入，不能邀请进入公会
	public static final int ERR_FC_LEAVINGFAMILY       = 124; //被邀请者离开不足七天，不能加入新的  

	// AU New Error Code
	public static final int ERR_PHONE_LOCK             = 130; //电话密保处于锁定中
	public static final int ERR_NOT_ACTIVED            = 131; //帐号未激活

	public static final int ERR_CHAT_CREATE_FAILED     = 151; //创建失败
	public static final int ERR_CHAT_INVALID_SUBJECT   = 152; //非法主题
	public static final int ERR_CHAT_ROOM_NOT_FOUND    = 153; //聊天室不存在
	public static final int ERR_CHAT_JOIN_REFUSED      = 154; //加入请求被拒绝
	public static final int ERR_CHAT_INVITE_REFUSED    = 155; //聊天邀请被拒绝
	public static final int ERR_CHAT_INVALID_PASSWORD  = 156; //聊天室密码错误
	public static final int ERR_CHAT_INVALID_ROLE      = 157; //角色未找到
	public static final int ERR_CHAT_PERMISSION_DENY   = 158; //没有权限
	public static final int ERR_CHAT_EXCESSIVE         = 159; //加入聊天室过多
	public static final int ERR_CHAT_ROOM_FULL         = 160; //人数已达上限
	public static final int ERR_CHAT_SEND_FAILURE      = 161; //发送失败

	//set custom data (201-210)
	public static final int ERR_NOFACETICKET           =   201; //没有美容券

	//mail system (211-219)
	public static final int ERR_MS_DBSVR_INV        =   211;      //数据库服务不可连接
	public static final int ERR_MS_MAIL_INV         =   212;      //邮件不存在
	public static final int ERR_MS_ATTACH_INV       =   213;      //错误的附件信息
	public static final int ERR_MS_SEND_SELF        =   214;      //禁止给自己发送邮件
	public static final int ERR_MS_ACCOUNTFROZEN    =   215;      //帐号已经冻结
	public static final int ERR_MS_AGAIN            =   216;      //暂时不可用
	public static final int ERR_MS_BOXFULL          =   217;      //目标邮箱已满
	// auction system(220-230)
	public static final int ERR_AS_MAILBOXFULL      =   220;      //玩家信箱已满
	public static final int ERR_AS_ITEM_INV         =   221;      //错误的拍卖物品信息
	public static final int ERR_AS_MARKET_UNOPEN    =   222;      //拍卖场未开放（未完成初始化）
	public static final int ERR_AS_ID_EXHAUSE       =   223;      //拍卖号用尽
	public static final int ERR_AS_ATTEND_OVF       =   224;      //参与的拍卖数达到上限
	public static final int ERR_AS_BID_LOWBID       =   225;      //出价过低竞拍失败
	public static final int ERR_AS_BID_NOTFOUND     =   226;      //未找到该拍卖事件
	public static final int ERR_AS_BID_BINSUCCESS   =   227;      //一口价买断
	public static final int ERR_AS_BID_UNREDEEMABLE =   228;      //不能赎回
	public static final int ERR_AS_BID_INVALIDPRICE =   229;      //竞拍价格高于安全设定
	// sell point system(231;241)
	public static final int ERR_SP_NOT_INIT         =   231;       //系统没有初始化完成
	public static final int ERR_SP_SPARETIME        =   232;       //剩余时间不满足挂售条件
	public static final int ERR_SP_INVA_POINT       =   233;       //无效的挂售点数，必须是30元的整数倍
	public static final int ERR_SP_EXPIRED          =   234;       //该点卡已经过期
	public static final int ERR_SP_DBNOTSYNC        =   235;       //Delivery与GameDBD挂售信息不同步
	public static final int ERR_SP_DBDEADLOCK       =   236;       //GameDBD处理协议时发生死锁
	public static final int ERR_SP_NOMONEY          =   237;       //虚拟币不足
	public static final int ERR_SP_INVA_STATUS      =   238;       //错误的事务状态
	public static final int ERR_SP_SELLING          =   239;       //点卡已经处于销售状态
	public static final int ERR_SP_ABORT            =   240;       //AU返回Abort; Do not change; authd is hardcoded
	public static final int ERR_SP_COMMIT           =   241;       //AU返回Commit; Do not change;authd is hardcoded
	public static final int ERR_SP_MONEYEXCEED      =   242;       //金钱数达到上限
	public static final int ERR_SP_BUYSELF	        =   243;       //不能购买自己挂售的点卡
	public static final int ERR_SP_FORBID	        =   244;       //挂售点卡功能已关闭
	public static final int ERR_SP_EXCESS	        =   245;       //禁止过度交易炒卖点卡; Do not change; authd is hardcoded

	// Battle System  260-280
	public static final int ERR_BS_INVALIDROLE      =   260;       //角色身份不符合
	public static final int ERR_BS_FAILED           =   261;       //失败
	public static final int ERR_BS_OUTOFSERVICE     =   262;       //服务暂时不可用
	public static final int ERR_BS_NEWBIE_BANNED    =   263;       //加入公会72小时内不允许进入城战
	public static final int ERR_BS_ALREADYSENT      =   264;       //本周收益已经发送过一次
	public static final int ERR_BS_ALREADYBID       =   265;       //不能多次竞价
	public static final int ERR_BS_NOTBATTLECITY    =   266;       //该地图没有开启城战
	public static final int ERR_BS_PROCESSBIDDING   =   267;       //正在处理请求，请重试
	public static final int ERR_BS_BIDSELF          =   268;       //不能对自己的领地竞价
	public static final int ERR_BS_BIDNOOWNERCITY   =   269;       //有领地公会不能对无主领地竞价
	public static final int ERR_BS_NOMONEY          =   270;       //金币数不足
	public static final int ERR_BS_LEVELNOTENOUGH   =   271;       //公会级别不足
	public static final int ERR_BS_RANKNOTENOUGH    =   272;       //公会排名不足
	public static final int ERR_BS_CREDITNOTENOUGH  =   273;       //公会友好度少于已宣战公会友好度
	public static final int ERR_BS_CREDITLIMIT	=   274;       //公会友好度不足最低要求
}


