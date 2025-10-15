package fire.pb.instancezone.bingfeng;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBingFengLandInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBingFengLandInfo extends __SBingFengLandInfo__ {
	@Override
	protected void process() {
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 804552;

	public int getType() {
		return 804552;
	}

	public int ranktype; // 参加RankType
	public int landid;
	public int lastrank; // 上次的排名
	public int receiveaward; // 如果已经领过奖励,为1，客户端应该把领取奖励灰掉
	public int stage; // 我的进度,根据landId和stage可以确定已经挑战过的npc
	public int yesterdaystage; // 昨日进度
	public int entertimes; // 还能进入的次数
	public int vip; // vip等级

	public SBingFengLandInfo() {
	}

	public SBingFengLandInfo(int _ranktype_, int _landid_, int _lastrank_, int _receiveaward_, int _stage_, int _yesterdaystage_, int _entertimes_, int _vip_) {
		this.ranktype = _ranktype_;
		this.landid = _landid_;
		this.lastrank = _lastrank_;
		this.receiveaward = _receiveaward_;
		this.stage = _stage_;
		this.yesterdaystage = _yesterdaystage_;
		this.entertimes = _entertimes_;
		this.vip = _vip_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ranktype);
		_os_.marshal(landid);
		_os_.marshal(lastrank);
		_os_.marshal(receiveaward);
		_os_.marshal(stage);
		_os_.marshal(yesterdaystage);
		_os_.marshal(entertimes);
		_os_.marshal(vip);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ranktype = _os_.unmarshal_int();
		landid = _os_.unmarshal_int();
		lastrank = _os_.unmarshal_int();
		receiveaward = _os_.unmarshal_int();
		stage = _os_.unmarshal_int();
		yesterdaystage = _os_.unmarshal_int();
		entertimes = _os_.unmarshal_int();
		vip = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBingFengLandInfo) {
			SBingFengLandInfo _o_ = (SBingFengLandInfo)_o1_;
			if (ranktype != _o_.ranktype) return false;
			if (landid != _o_.landid) return false;
			if (lastrank != _o_.lastrank) return false;
			if (receiveaward != _o_.receiveaward) return false;
			if (stage != _o_.stage) return false;
			if (yesterdaystage != _o_.yesterdaystage) return false;
			if (entertimes != _o_.entertimes) return false;
			if (vip != _o_.vip) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ranktype;
		_h_ += landid;
		_h_ += lastrank;
		_h_ += receiveaward;
		_h_ += stage;
		_h_ += yesterdaystage;
		_h_ += entertimes;
		_h_ += vip;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ranktype).append(",");
		_sb_.append(landid).append(",");
		_sb_.append(lastrank).append(",");
		_sb_.append(receiveaward).append(",");
		_sb_.append(stage).append(",");
		_sb_.append(yesterdaystage).append(",");
		_sb_.append(entertimes).append(",");
		_sb_.append(vip).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBingFengLandInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ranktype - _o_.ranktype;
		if (0 != _c_) return _c_;
		_c_ = landid - _o_.landid;
		if (0 != _c_) return _c_;
		_c_ = lastrank - _o_.lastrank;
		if (0 != _c_) return _c_;
		_c_ = receiveaward - _o_.receiveaward;
		if (0 != _c_) return _c_;
		_c_ = stage - _o_.stage;
		if (0 != _c_) return _c_;
		_c_ = yesterdaystage - _o_.yesterdaystage;
		if (0 != _c_) return _c_;
		_c_ = entertimes - _o_.entertimes;
		if (0 != _c_) return _c_;
		_c_ = vip - _o_.vip;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
	
}

