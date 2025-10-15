
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPlayPKFightView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPlayPKFightView extends __CPlayPKFightView__ {
	@Override
	protected void process() {
		//擂台寻找对手界面
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid < 0) {
			return;
		}
		new PPlayPKFightView(roleid, modeltype, school, levelindex).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793683;

	public int getType() {
		return 793683;
	}

	public int modeltype; // 模块类型
	public int school; // 删选的职业 -1表示全职业
	public int levelindex; // 删选的角色等级区间 0表示全选

	public CPlayPKFightView() {
	}

	public CPlayPKFightView(int _modeltype_, int _school_, int _levelindex_) {
		this.modeltype = _modeltype_;
		this.school = _school_;
		this.levelindex = _levelindex_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(school);
		_os_.marshal(levelindex);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		levelindex = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPlayPKFightView) {
			CPlayPKFightView _o_ = (CPlayPKFightView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (school != _o_.school) return false;
			if (levelindex != _o_.levelindex) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += school;
		_h_ += levelindex;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(school).append(",");
		_sb_.append(levelindex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPlayPKFightView _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = modeltype - _o_.modeltype;
		if (0 != _c_) return _c_;
		_c_ = school - _o_.school;
		if (0 != _c_) return _c_;
		_c_ = levelindex - _o_.levelindex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

