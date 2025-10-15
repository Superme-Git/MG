
package gnet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.state.StateManager;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __UserInfoRep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class UserInfoRep extends __UserInfoRep__ {
	
	static String osType = "";
	static Map<String, String> ostypes = new HashMap<String, String>();
	static{
		Properties propConf = ConfigManager.getInstance().getPropConf("sys");
		if (propConf != null)
			osType = propConf.getProperty("sys.os.type");
		String[] tmps = osType.split(";");
		for (String str : tmps) {
			ostypes.put(str, str.substring(str.length()-1));
		}
		
	}
	@Override
	protected void process() {
		new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
				xbean.AUUserInfo info=xbean.Pod.newAUUserInfo();
				info.setRetcode(retcode);
				info.setFunc(func);
				info.setFuncparm(funcparm);
				info.setLoginip(loginip);
				info.setBlisgm(blisgm);
				info.getAuth().addAll(auth);
				info.setAlgorithm(algorithm);
				info.setNicknameOctets(nickname);
				//xdb.Trace.info("UserInfoRep:nickname=" + new String(nickname.getBytes(), "ISO-8859-1"));
				OctetsStream octstream = new OctetsStream(nickname);
				String nickNameStr = octstream.unmarshal_String("ISO-8859-1");
				info.setUsername(octstream.unmarshal_String("UTF-8"));
				info.setNickname(nickNameStr);
				
				//解析出渠道名字
				String userNameStr = info.getUsername();
//				String userNameStr = "1234567890@@yingyongbao@2";
				String [] str = userNameStr.split("@");
				if (str.length == 4) {
					if (!str[2].equals("")) {
						info.setPlatname(str[2]);
					}
					else {
						StateManager.logger.info(new StringBuilder().append("userid:")
								.append(userid).append(",username is:").append(userNameStr));
					}
				}
				else {
					StateManager.logger.info(new StringBuilder().append("userid:")
							.append(userid).append(",username is:").append(userNameStr));
				}
				
				Properties prop = ConfigManager.getInstance().getPropConf("sys");
				if (prop != null) {
					String value = prop.getProperty("sys.gm.userids");
					String value2 = prop.getProperty("sys.lowlvgm.userids");
					if(value!=null){
						String gms[]=value.split(";");
						if(gms!=null){
							for(String gmstr:gms){
								if(gmstr!=null){
									String gm[]=gmstr.split("\\$");
									if(gm!=null&&gm.length==2&&info.getUsername().equals(gm[0])){
										info.setBlisgm(1);
										break;
									}
								}
							}
						}
					}
					if(value2!=null){
						String gms[]=value2.split(";");
						if(gms!=null){
							for(String gmstr:gms){
								if(gmstr!=null){
									String gm[]=gmstr.split("\\$");
									if(gm!=null&&gm.length==2&&info.getUsername().equals(gm[0])){
										info.setBlisgm(2);
										break;
									}
								}
							}
						}
					}
				}

				StringBuilder sb = new StringBuilder();
				sb.append("UserInfoRep:").append(",userid:").append(userid);
				sb.append(",func:").append(func);
				sb.append(",username:").append(info.getUsername());
				sb.append(",plattype:").append(info.getNickname());
				sb.append(",blisgm:"+info.getBlisgm());
				sb.append(",loginip:"+loginip);
				//xdb.Trace.info(sb.toString());
				StateManager.logger.info(sb.toString());
				//插入会重复
				xtable.Auuserinfo.remove(userid);
				xtable.Auuserinfo.add(userid, info);
				return true;
			}
			/*
			private String getOsType(String key) {

				for (Entry<String, String> entry : ostypes.entrySet()) {
					if (entry.getKey().contains(key))
						return entry.getValue();
				}
			
				//如果没有找到,默认是android
				return "2";

			}
			*/
			
		}.submit();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 211;

	public int getType() {
		return 211;
	}

	public final static int ALGORITHM_NONE = 0x0; // 仅密码
	public final static int ALGORITHM_CARD = 0x00010000; // 密保卡
	public final static int ALGORITHM_HANDSET = 0x00020000; // 手机密保
	public final static int ALGORITHM_USBKEY = 0x00030000; // usbkey
	public final static int ALGORITHM_PHONE = 0x00040100; // 电话密保
	public final static int ALGORITHM_USBKEY2 = 0x00050000; // usbkey2

	public int userid;
	public int retcode;
	public int func; // 新手卡字段
	public int funcparm; // 新手卡附属字段
	public int loginip; // 客户端登录ip
	public byte blisgm; // 是否为GM
	public java.util.ArrayList<Integer> auth; // GM权限列表，具体权限见相关文档
	public int algorithm; // 账号安全级别
	public byte gender; // 0-female,1-male,2-unknown
	public com.locojoy.base.Octets nickname; // 昵称

	public UserInfoRep() {
		auth = new java.util.ArrayList<Integer>();
		nickname = new com.locojoy.base.Octets();
	}

	public UserInfoRep(int _userid_, int _retcode_, int _func_, int _funcparm_, int _loginip_, byte _blisgm_, java.util.ArrayList<Integer> _auth_, int _algorithm_, byte _gender_, com.locojoy.base.Octets _nickname_) {
		this.userid = _userid_;
		this.retcode = _retcode_;
		this.func = _func_;
		this.funcparm = _funcparm_;
		this.loginip = _loginip_;
		this.blisgm = _blisgm_;
		this.auth = _auth_;
		this.algorithm = _algorithm_;
		this.gender = _gender_;
		this.nickname = _nickname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(userid);
		_os_.marshal(retcode);
		_os_.marshal(func);
		_os_.marshal(funcparm);
		_os_.marshal(loginip);
		_os_.marshal(blisgm);
		_os_.compact_uint32(auth.size());
		for (Integer _v_ : auth) {
			_os_.marshal(_v_);
		}
		_os_.marshal(algorithm);
		_os_.marshal(gender);
		_os_.marshal(nickname);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		retcode = _os_.unmarshal_int();
		func = _os_.unmarshal_int();
		funcparm = _os_.unmarshal_int();
		loginip = _os_.unmarshal_int();
		blisgm = _os_.unmarshal_byte();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			auth.add(_v_);
		}
		algorithm = _os_.unmarshal_int();
		gender = _os_.unmarshal_byte();
		nickname = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof UserInfoRep) {
			UserInfoRep _o_ = (UserInfoRep)_o1_;
			if (userid != _o_.userid) return false;
			if (retcode != _o_.retcode) return false;
			if (func != _o_.func) return false;
			if (funcparm != _o_.funcparm) return false;
			if (loginip != _o_.loginip) return false;
			if (blisgm != _o_.blisgm) return false;
			if (!auth.equals(_o_.auth)) return false;
			if (algorithm != _o_.algorithm) return false;
			if (gender != _o_.gender) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += retcode;
		_h_ += func;
		_h_ += funcparm;
		_h_ += loginip;
		_h_ += (int)blisgm;
		_h_ += auth.hashCode();
		_h_ += algorithm;
		_h_ += (int)gender;
		_h_ += nickname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(retcode).append(",");
		_sb_.append(func).append(",");
		_sb_.append(funcparm).append(",");
		_sb_.append(loginip).append(",");
		_sb_.append(blisgm).append(",");
		_sb_.append(auth).append(",");
		_sb_.append(algorithm).append(",");
		_sb_.append(gender).append(",");
		_sb_.append("B").append(nickname.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

