
package openau;
import com.locojoy.sdk.PExeChargeRefund;
import com.locojoy.sdk.PGetChargeRefundInfo;
import com.locojoy.sdk.PGsHttpRequest;
import com.locojoy.sdk.PUpdateUserRolesInfo;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __DataBetweenAuAnyAndGS__ extends mkio.Protocol { }

/** qtype:	(8001)GS_TO_AuAny更新玩家的服务器角色信息,此时info是玩家角色信息
				(10001)GS_TO_AuAny代理http请求,此时info是http请求地址及参数
				(10002)AuAny_TO_GS代理http请求的返回结果
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class DataBetweenAuAnyAndGS extends __DataBetweenAuAnyAndGS__ {
	@Override
	protected void process() {
		if(flag != GS_TO_AuAny)
			return;
		
		if(this.qtype == 8001){
			//8001:更新玩家的服务器角色信息
			final PUpdateUserRolesInfo proc = new PUpdateUserRolesInfo(this);
			proc.submit();
		} else if(this.qtype == 9001){
			//9001:查询充值返还状态 -> 9002:返回GS
			final PGetChargeRefundInfo proc = new PGetChargeRefundInfo(this);
			proc.submit();
		} else if(this.qtype == 9003){
			//9003:执行充值返还 -> 9004:返回GS
			final PExeChargeRefund proc = new PExeChargeRefund(this, 0);
			proc.submit();
		} else if(this.qtype == 9005){
			//9005:执行充值返还入库
			final PExeChargeRefund proc = new PExeChargeRefund(this, 1);
			proc.submit();
		} else if(this.qtype == 10001){
			//10001:gs发出的http请求，代理处理，目前供兑换码使用
			//LocojoyPlat.httpRequestFromGS(this);
			new PGsHttpRequest(this).submit();
		}
		else
			return;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8920;

	public int getType() {
		return 8920;
	}

	public final static int AuAny_TO_GS = 1;
	public final static int GS_TO_AuAny = 2;

	public int userid;
	public int qtype;
	public byte flag;
	public com.locojoy.base.Octets info;
	public int reserved;

	public DataBetweenAuAnyAndGS() {
		info = new com.locojoy.base.Octets();
	}

	public DataBetweenAuAnyAndGS(int _userid_, int _qtype_, byte _flag_, com.locojoy.base.Octets _info_, int _reserved_) {
		this.userid = _userid_;
		this.qtype = _qtype_;
		this.flag = _flag_;
		this.info = _info_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(userid);
		_os_.marshal(qtype);
		_os_.marshal(flag);
		_os_.marshal(info);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		qtype = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		info = _os_.unmarshal_Octets();
		reserved = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DataBetweenAuAnyAndGS) {
			DataBetweenAuAnyAndGS _o_ = (DataBetweenAuAnyAndGS)_o1_;
			if (userid != _o_.userid) return false;
			if (qtype != _o_.qtype) return false;
			if (flag != _o_.flag) return false;
			if (!info.equals(_o_.info)) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += qtype;
		_h_ += (int)flag;
		_h_ += info.hashCode();
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(qtype).append(",");
		_sb_.append(flag).append(",");
		_sb_.append("B").append(info.size()).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

