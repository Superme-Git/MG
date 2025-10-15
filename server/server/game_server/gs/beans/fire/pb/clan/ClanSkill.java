
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanSkill implements Marshal , Comparable<ClanSkill>{
	public int clanskillid; // 技能id
	public int clanskilllevel; // 技能当前等级
	public int clanskillmaxlevel; // 技能等级上限
	public int clanskillcurexp; // 技能当前经验
	public int clanskilllevelupexp; // 技能升级所需经验

	public ClanSkill() {
	}

	public ClanSkill(int _clanskillid_, int _clanskilllevel_, int _clanskillmaxlevel_, int _clanskillcurexp_, int _clanskilllevelupexp_) {
		this.clanskillid = _clanskillid_;
		this.clanskilllevel = _clanskilllevel_;
		this.clanskillmaxlevel = _clanskillmaxlevel_;
		this.clanskillcurexp = _clanskillcurexp_;
		this.clanskilllevelupexp = _clanskilllevelupexp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(clanskillid);
		_os_.marshal(clanskilllevel);
		_os_.marshal(clanskillmaxlevel);
		_os_.marshal(clanskillcurexp);
		_os_.marshal(clanskilllevelupexp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanskillid = _os_.unmarshal_int();
		clanskilllevel = _os_.unmarshal_int();
		clanskillmaxlevel = _os_.unmarshal_int();
		clanskillcurexp = _os_.unmarshal_int();
		clanskilllevelupexp = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanSkill) {
			ClanSkill _o_ = (ClanSkill)_o1_;
			if (clanskillid != _o_.clanskillid) return false;
			if (clanskilllevel != _o_.clanskilllevel) return false;
			if (clanskillmaxlevel != _o_.clanskillmaxlevel) return false;
			if (clanskillcurexp != _o_.clanskillcurexp) return false;
			if (clanskilllevelupexp != _o_.clanskilllevelupexp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanskillid;
		_h_ += clanskilllevel;
		_h_ += clanskillmaxlevel;
		_h_ += clanskillcurexp;
		_h_ += clanskilllevelupexp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanskillid).append(",");
		_sb_.append(clanskilllevel).append(",");
		_sb_.append(clanskillmaxlevel).append(",");
		_sb_.append(clanskillcurexp).append(",");
		_sb_.append(clanskilllevelupexp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ClanSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = clanskillid - _o_.clanskillid;
		if (0 != _c_) return _c_;
		_c_ = clanskilllevel - _o_.clanskilllevel;
		if (0 != _c_) return _c_;
		_c_ = clanskillmaxlevel - _o_.clanskillmaxlevel;
		if (0 != _c_) return _c_;
		_c_ = clanskillcurexp - _o_.clanskillcurexp;
		if (0 != _c_) return _c_;
		_c_ = clanskilllevelupexp - _o_.clanskilllevelupexp;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

