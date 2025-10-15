
package protocol;

/*
 * java �汾�Ĵ����붨�壬Ϊ�˼��ݾɵ�ϵͳ����au�ȣ�ȫ�������ƹ�����
 * ��Ӵ������ʱ����Ҫ����ͬ����
 * share/rpc/errcode.h
 */

public class errcode
{
	public static final int ERR_SUCCESS                = 0;  // �ɹ�
	public static final int ERR_TOBECONTINUE           = 1;  // �ɹ������һ��и�������δ�����꣬Ŀǰδ��

	public static final int ERR_INVALID_ACCOUNT        = 2;  // �ʺŲ�����
	public static final int ERR_INVALID_PASSWORD       = 3;  // �������
	public static final int ERR_TIMEOUT                = 4;  // ��ʱ
	public static final int ERR_INVALID_ARGUMENT       = 5;  // ��������
	public static final int ERR_FRIEND_SYNCHRONIZE     = 6;  // ������Ϣ���浽���ݿ�ʱ�޷�ͬ��
	public static final int ERR_SERVERNOTSUPPLY        = 7;  // �÷�������֧�ָ�����
	public static final int ERR_COMMUNICATION          = 8;  // ����ͨѶ����
	public static final int ERR_ACCOUNTLOCKED          = 9;  // ����ظ���½����ǰ�û���һ����½���ڱ�������������״̬
	public static final int ERR_MULTILOGIN             = 10; // ����ظ���½�����û�ѡ���Զ�����
	// keyexchange
	public static final int ERR_INVALID_NONCE          = 11; //��Ч��nonceֵ
	//logout
	public static final int ERR_LOGOUT_FAIL            = 12; //AUTH�ǳ�ʧ��
	//login Gameserver
	public static final int ERR_GAMEDB_FAIL            = 13; //��Ϸ��������ȡ��ҽ�ɫ��Ϣʧ��
	public static final int ERR_ENTERWORLD_FAIL        = 14; //��½��Ϸ������ʧ��
	public static final int ERR_EXCEED_MAXNUM          = 15; //��Ϸ�����������Ѵ�����
	public static final int ERR_IN_WORLD               = 16; //��ҽ�ɫ�Ѿ�������
	public static final int ERR_EXCEED_LINE_MAXNUM	   = 17; //���߽�ɫ���Ѵﵽ����
	public static final int ERR_INVALID_LINEID	   = 18; //û������ߺ�
	public static final int ERR_NO_LINE_AVALIABLE	   = 19; //û�п��õ���
	// deliver use
	public static final int ERR_DELIVER_SEND           = 21; // ת��ʧ��
	public static final int ERR_DELIVER_TIMEOUT        = 22; // ת����ʱ
	//account
	public static final int ERR_ACCOUNTEMPTY           = 23; //�ʻ�����
	public static final int ERR_ACCOUNTFORBID          = 24; //�ʺű�GameMaster�������������½
	public static final int ERR_INVALIDCHAR            = 25; //�����к��зǷ����ַ�
	// LoginCheck
	public static final int ERR_IP_LOCK		   = 26;
	public static final int ERR_ID_LOCK		   = 27;
	public static final int ERR_MATRIXFAILURE	   = 28;

	//player login
	public static final int ERR_LOGINFAIL              = 31; //��½��Ϸʧ��
	public static final int ERR_KICKOUT                = 32; //��������
	public static final int ERR_CREATEROLE             = 33; //������ɫʧ��
	public static final int ERR_DELETEROLE             = 34; //ɾ����ɫʧ��
	public static final int ERR_ROLELIST               = 35; //��ý�ɫ�б�ʧ��
	public static final int ERR_UNDODELROLE            = 36; //����ɾ����ɫʧ��
	public static final int ERR_SRVMAINTAIN            = 37; //������ά���У���ʱ���ܵ�½
	public static final int ERR_ROLEFORBID             = 38; //��ɫ��GM��ɱ���������½��Ϸ
	public static final int ERR_SERVEROVERLOAD         = 39; //�����������Ѵ�����
	public static final int ERR_ACKICKOUT              = 40; //������ҳ���������

	//DB retcode
	public static final int ERR_FAILED                 = 41; //һ�����
	public static final int ERR_EXCEPTION              = 42; //���ݿ��쳣
	public static final int ERR_NOTFOUND               = 43; //��¼δ�ҵ�
	public static final int ERR_INVALIDHANDLE          = 44; //�����Handle
	public static final int ERR_DUPLICATRECORD         = 45; //��¼����ظ�
	public static final int ERR_NOFREESPACE            = 46; //û��ʣ��ռ�
	public static final int ERR_VERIFYFAILED           = 47; //����У�����
	public static final int ERR_DUPLICATE_ROLEID       = 48; //��ɫid�ظ�
	public static final int ERR_AGAIN                  = 49; //���ݿⷱæ���Ժ�����
	public static final int ERR_DATAERROR              = 50; //���ݴ���

	//add friend
	public static final int ERR_ADDFRD_REQUEST         = 51; //�����Ϊ����
	public static final int ERR_ADDFRD_REFUSE          = 52; //�ܾ���Ϊ����
	public static final int ERR_ADDFRD_AGREE           = 53; //ͬ���Ϊ����

	public static final int ERR_COMMAND_COOLING        = 55; //����������ȴ��
	public static final int ERR_INSTANCE_OVERFLOW      = 56; //��¼ʧ��;������Ŀ�ﵽ����

	//game DB
	public static final int ERR_DATANOTFIND            = 60; //���ݲ�����

	//other
	public static final int ERR_GENERAL                = 61; //δ�����һ���Դ���
	public static final int ERR_OUTOFSYNC              = 62; //���ݲ�ͬ��
	public static final int ERR_PERMISSION_DENIED      = 63; //û��Ȩ��
	public static final int ERR_DATABASE_TIMEOUT       = 64; //���ݿⳬʱ
	public static final int ERR_UNAVAILABLE            = 65; //��ɫ�ѻ飬�޷�ɾ��
	public static final int ERR_CMDCOOLING             = 66; //����������ȴ��

	//Trade
	public static final int ERR_TRADE_PARTNER_OFFLINE  = 68; //�Է��Ѿ�����
	public static final int ERR_TRADE_AGREE            = 0;  //ͬ�⽻��
	public static final int ERR_TRADE_REFUSE           = 69; //�Է��ܾ�����
	public static final int ERR_TRADE_BUSY_TRADER      = 70; //trader �Ѿ��ڽ�����
	public static final int ERR_TRADE_DB_FAILURE       = 71; //��д���ݿ�ʧ��
	public static final int ERR_TRADE_JOIN_IN          = 72; //���뽻��ʧ�ܣ����׶����˫���Ѿ�����
	public static final int ERR_TRADE_INVALID_TRADER   = 73; //��Ч�Ľ�����
	public static final int ERR_TRADE_ADDGOODS         = 74; //���ӽ�����Ʒʧ��
	public static final int ERR_TRADE_RMVGOODS         = 75; //���ٽ�����Ʒʧ��
	public static final int ERR_TRADE_READY_HALF       = 76; //�ύ���һ�룬�ȴ��Է��ύ
	public static final int ERR_TRADE_READY            = 77; //�ύ���
	public static final int ERR_TRADE_SUBMIT_FAIL      = 78; //�ύʧ��
	public static final int ERR_TRADE_CONFIRM_FAIL     = 79; //ȷ��ʧ��
	public static final int ERR_TRADE_DONE             = 80; //�������
	public static final int ERR_TRADE_HALFDONE         = 81; //�������һ�룬�ȴ���һ��ȷ��
	public static final int ERR_TRADE_DISCARDFAIL      = 82; //ȡ������ʧ��
	public static final int ERR_TRADE_MOVE_FAIL        = 83; //�ƶ���Ʒʧ��
	public static final int ERR_TRADE_SPACE            = 84; //��Ʒ���ռ䲻��
	public static final int ERR_TRADE_SETPSSN          = 85; //���ý����߲Ʋ�����
	public static final int ERR_TRADE_ATTACH_HALF      = 86; //�ɹ�����һ��һ��������
	public static final int ERR_TRADE_ATTACH_DONE      = 87; //�ɹ���������������
	public static final int ERR_TRADE_PARTNER_FORBID   = 88; //���׶���GM������׹���

	//faction error code (101-200) 
	public static final int ERR_FC_NETWORKERR          = 101; //����������ͨѶ����
	public static final int ERR_FC_INVALID_OPERATION   = 102; //��Ч�Ĳ�������
	public static final int ERR_FC_OP_TIMEOUT          = 103; //������ʱ
	public static final int ERR_FC_CREATE_ALREADY      = 104; //����Ѿ���ĳ������ĳ�Ա�������ٴ�������
	public static final int ERR_FC_CREATE_DUP          = 105; //���������ظ�
	public static final int ERR_FC_DBFAILURE           = 106; //���ݿ�IO����
	public static final int ERR_FC_NO_PRIVILEGE        = 107; //û����ز�����Ȩ��
	public static final int ERR_FC_INVALIDNAME         = 108; //����ʹ�ô�����
	public static final int ERR_FC_FULL                = 109; //��Ա����
	public static final int ERR_FC_APPLY_REJOIN        = 110; //�Ѿ���ĳ������ĳ�Ա������ʧ��
	public static final int ERR_FC_JOIN_SUCCESS        = 111; //�ɹ����빫��
	public static final int ERR_FC_JOIN_REFUSE         = 112; //���뱻�ܾ�
	public static final int ERR_FC_ACCEPT_REACCEPT     = 113; //����׼���빫�������Ѿ����빫��
	public static final int ERR_FC_FACTION_NOTEXIST    = 114; //���᲻����or���û����������뱾����
	public static final int ERR_FC_NOTAMEMBER          = 115; //��Ҳ��Ǳ�����İ���
	public static final int ERR_FC_CHECKCONDITION      = 116; //�����������������SP�������ʽ𲻹�
	public static final int ERR_FC_DATAERROR           = 117; //�����������ʹ��󣬿ͻ����ύ�Ĳ���������ʽ����
	public static final int ERR_FC_OFFLINE             = 118; //��Ҳ�����
	public static final int ERR_FC_OUTOFSERVICE        = 119; //������ʱ������
	public static final int ERR_FC_INVITEELEVEL        = 120; //�����뷽���𲻹������ܼ���
	public static final int ERR_FC_PREDELSUCCESS       = 121; //�����ɢ�ɹ����������ʽ��ɢ
	public static final int ERR_FC_DISMISSWAITING      = 122; //�������ڽ�ɢ��
	public static final int ERR_FC_INVITEENOFAMILY     = 123; //��������û�м��룬����������빫��
	public static final int ERR_FC_LEAVINGFAMILY       = 124; //���������뿪�������죬���ܼ����µ�  

	// AU New Error Code
	public static final int ERR_PHONE_LOCK             = 130; //�绰�ܱ�����������
	public static final int ERR_NOT_ACTIVED            = 131; //�ʺ�δ����

	public static final int ERR_CHAT_CREATE_FAILED     = 151; //����ʧ��
	public static final int ERR_CHAT_INVALID_SUBJECT   = 152; //�Ƿ�����
	public static final int ERR_CHAT_ROOM_NOT_FOUND    = 153; //�����Ҳ�����
	public static final int ERR_CHAT_JOIN_REFUSED      = 154; //�������󱻾ܾ�
	public static final int ERR_CHAT_INVITE_REFUSED    = 155; //�������뱻�ܾ�
	public static final int ERR_CHAT_INVALID_PASSWORD  = 156; //�������������
	public static final int ERR_CHAT_INVALID_ROLE      = 157; //��ɫδ�ҵ�
	public static final int ERR_CHAT_PERMISSION_DENY   = 158; //û��Ȩ��
	public static final int ERR_CHAT_EXCESSIVE         = 159; //���������ҹ���
	public static final int ERR_CHAT_ROOM_FULL         = 160; //�����Ѵ�����
	public static final int ERR_CHAT_SEND_FAILURE      = 161; //����ʧ��

	//set custom data (201-210)
	public static final int ERR_NOFACETICKET           =   201; //û������ȯ

	//mail system (211-219)
	public static final int ERR_MS_DBSVR_INV        =   211;      //���ݿ���񲻿�����
	public static final int ERR_MS_MAIL_INV         =   212;      //�ʼ�������
	public static final int ERR_MS_ATTACH_INV       =   213;      //����ĸ�����Ϣ
	public static final int ERR_MS_SEND_SELF        =   214;      //��ֹ���Լ������ʼ�
	public static final int ERR_MS_ACCOUNTFROZEN    =   215;      //�ʺ��Ѿ�����
	public static final int ERR_MS_AGAIN            =   216;      //��ʱ������
	public static final int ERR_MS_BOXFULL          =   217;      //Ŀ����������
	// auction system(220-230)
	public static final int ERR_AS_MAILBOXFULL      =   220;      //�����������
	public static final int ERR_AS_ITEM_INV         =   221;      //�����������Ʒ��Ϣ
	public static final int ERR_AS_MARKET_UNOPEN    =   222;      //������δ���ţ�δ��ɳ�ʼ����
	public static final int ERR_AS_ID_EXHAUSE       =   223;      //�������þ�
	public static final int ERR_AS_ATTEND_OVF       =   224;      //������������ﵽ����
	public static final int ERR_AS_BID_LOWBID       =   225;      //���۹��;���ʧ��
	public static final int ERR_AS_BID_NOTFOUND     =   226;      //δ�ҵ��������¼�
	public static final int ERR_AS_BID_BINSUCCESS   =   227;      //һ�ڼ����
	public static final int ERR_AS_BID_UNREDEEMABLE =   228;      //�������
	public static final int ERR_AS_BID_INVALIDPRICE =   229;      //���ļ۸���ڰ�ȫ�趨
	// sell point system(231;241)
	public static final int ERR_SP_NOT_INIT         =   231;       //ϵͳû�г�ʼ�����
	public static final int ERR_SP_SPARETIME        =   232;       //ʣ��ʱ�䲻�����������
	public static final int ERR_SP_INVA_POINT       =   233;       //��Ч�Ĺ��۵�����������30Ԫ��������
	public static final int ERR_SP_EXPIRED          =   234;       //�õ㿨�Ѿ�����
	public static final int ERR_SP_DBNOTSYNC        =   235;       //Delivery��GameDBD������Ϣ��ͬ��
	public static final int ERR_SP_DBDEADLOCK       =   236;       //GameDBD����Э��ʱ��������
	public static final int ERR_SP_NOMONEY          =   237;       //����Ҳ���
	public static final int ERR_SP_INVA_STATUS      =   238;       //���������״̬
	public static final int ERR_SP_SELLING          =   239;       //�㿨�Ѿ���������״̬
	public static final int ERR_SP_ABORT            =   240;       //AU����Abort; Do not change; authd is hardcoded
	public static final int ERR_SP_COMMIT           =   241;       //AU����Commit; Do not change;authd is hardcoded
	public static final int ERR_SP_MONEYEXCEED      =   242;       //��Ǯ���ﵽ����
	public static final int ERR_SP_BUYSELF	        =   243;       //���ܹ����Լ����۵ĵ㿨
	public static final int ERR_SP_FORBID	        =   244;       //���۵㿨�����ѹر�
	public static final int ERR_SP_EXCESS	        =   245;       //��ֹ���Ƚ��׳����㿨; Do not change; authd is hardcoded

	// Battle System  260-280
	public static final int ERR_BS_INVALIDROLE      =   260;       //��ɫ��ݲ�����
	public static final int ERR_BS_FAILED           =   261;       //ʧ��
	public static final int ERR_BS_OUTOFSERVICE     =   262;       //������ʱ������
	public static final int ERR_BS_NEWBIE_BANNED    =   263;       //���빫��72Сʱ�ڲ���������ս
	public static final int ERR_BS_ALREADYSENT      =   264;       //���������Ѿ����͹�һ��
	public static final int ERR_BS_ALREADYBID       =   265;       //���ܶ�ξ���
	public static final int ERR_BS_NOTBATTLECITY    =   266;       //�õ�ͼû�п�����ս
	public static final int ERR_BS_PROCESSBIDDING   =   267;       //���ڴ�������������
	public static final int ERR_BS_BIDSELF          =   268;       //���ܶ��Լ�����ؾ���
	public static final int ERR_BS_BIDNOOWNERCITY   =   269;       //����ع��᲻�ܶ�������ؾ���
	public static final int ERR_BS_NOMONEY          =   270;       //���������
	public static final int ERR_BS_LEVELNOTENOUGH   =   271;       //���ἶ����
	public static final int ERR_BS_RANKNOTENOUGH    =   272;       //������������
	public static final int ERR_BS_CREDITNOTENOUGH  =   273;       //�����Ѻö���������ս�����Ѻö�
	public static final int ERR_BS_CREDITLIMIT	=   274;       //�����ѺöȲ������Ҫ��
}


