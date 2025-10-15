
package gnet;


import fire.pb.fushi.FushiManager;
import fire.pb.main.Gs;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __QueryOrderResponse__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class QueryOrderResponse extends __QueryOrderResponse__ {
	@Override
	protected void process() {
		fire.log.LogManager.logger.info("receive QueryOrderResponse.platid:"+platid+",platsn:"+orderserialplat+",gamesn:"+orderserialgame+",errorcode:"+errorcode);
		if (fire.pb.fushi.Module.getIsYYBUser(userid)) {
			FushiManager.logger.error("应用宝服务器，符石完全托管，禁用原来订单充值逻辑！");
			return;
		}
		new fire.pb.fushi.PQueryOrderResponseNew(this).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8905;

	public int getType() {
		return 8905;
	}

	public final static int restype_query = 0; // 查询返回结果
	public final static int restype_notify = 0; // 主动通告返回结果

	public int errorcode;
	public int restype;
	public java.lang.String platid; // 平台唯一标识
	public java.lang.String orderserialplat; // 平台用的订单号
	public java.lang.String orderserialgame; // 游戏内自己用的订单号
	public int userid; // 如果返回的结果中，并没有userid的信息，该值为-1
	public com.locojoy.base.Octets vars;

	public QueryOrderResponse() {
		errorcode = ErrorCodes.error_succeed;
		restype = restype_query;
		platid = "";
		orderserialplat = "";
		orderserialgame = "";
		userid = -1;
		vars = new com.locojoy.base.Octets();
	}

	public QueryOrderResponse(int _errorcode_, int _restype_, java.lang.String _platid_, java.lang.String _orderserialplat_, java.lang.String _orderserialgame_, int _userid_, com.locojoy.base.Octets _vars_) {
		this.errorcode = _errorcode_;
		this.restype = _restype_;
		this.platid = _platid_;
		this.orderserialplat = _orderserialplat_;
		this.orderserialgame = _orderserialgame_;
		this.userid = _userid_;
		this.vars = _vars_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(errorcode);
		_os_.marshal(restype);
		_os_.marshal(platid, "UTF-16LE");
		_os_.marshal(orderserialplat, "UTF-16LE");
		_os_.marshal(orderserialgame, "UTF-16LE");
		_os_.marshal(userid);
		_os_.marshal(vars);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		errorcode = _os_.unmarshal_int();
		restype = _os_.unmarshal_int();
		platid = _os_.unmarshal_String("UTF-16LE");
		orderserialplat = _os_.unmarshal_String("UTF-16LE");
		orderserialgame = _os_.unmarshal_String("UTF-16LE");
		userid = _os_.unmarshal_int();
		vars = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof QueryOrderResponse) {
			QueryOrderResponse _o_ = (QueryOrderResponse)_o1_;
			if (errorcode != _o_.errorcode) return false;
			if (restype != _o_.restype) return false;
			if (!platid.equals(_o_.platid)) return false;
			if (!orderserialplat.equals(_o_.orderserialplat)) return false;
			if (!orderserialgame.equals(_o_.orderserialgame)) return false;
			if (userid != _o_.userid) return false;
			if (!vars.equals(_o_.vars)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += errorcode;
		_h_ += restype;
		_h_ += platid.hashCode();
		_h_ += orderserialplat.hashCode();
		_h_ += orderserialgame.hashCode();
		_h_ += userid;
		_h_ += vars.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(errorcode).append(",");
		_sb_.append(restype).append(",");
		_sb_.append("T").append(platid.length()).append(",");
		_sb_.append("T").append(orderserialplat.length()).append(",");
		_sb_.append("T").append(orderserialgame.length()).append(",");
		_sb_.append(userid).append(",");
		_sb_.append("B").append(vars.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

