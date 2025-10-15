
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPlayPKFightView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPlayPKFightView extends __SPlayPKFightView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793684;

	public int getType() {
		return 793684;
	}

	public int modeltype; // 模块类型
	public int school; // 删选的职业 -1表示全职业
	public int levelindex; // 删选的角色等级区间 0表示全选
	public java.util.ArrayList<fire.pb.battle.QCRoleInfoDes> rolelist;
	public java.util.ArrayList<fire.pb.battle.QCRoleInfoWatchDes> rolewatchlist; // 观战对手信息

	public SPlayPKFightView() {
		rolelist = new java.util.ArrayList<fire.pb.battle.QCRoleInfoDes>();
		rolewatchlist = new java.util.ArrayList<fire.pb.battle.QCRoleInfoWatchDes>();
	}

	public SPlayPKFightView(int _modeltype_, int _school_, int _levelindex_, java.util.ArrayList<fire.pb.battle.QCRoleInfoDes> _rolelist_, java.util.ArrayList<fire.pb.battle.QCRoleInfoWatchDes> _rolewatchlist_) {
		this.modeltype = _modeltype_;
		this.school = _school_;
		this.levelindex = _levelindex_;
		this.rolelist = _rolelist_;
		this.rolewatchlist = _rolewatchlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.QCRoleInfoDes _v_ : rolelist)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.QCRoleInfoWatchDes _v_ : rolewatchlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(school);
		_os_.marshal(levelindex);
		_os_.compact_uint32(rolelist.size());
		for (fire.pb.battle.QCRoleInfoDes _v_ : rolelist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(rolewatchlist.size());
		for (fire.pb.battle.QCRoleInfoWatchDes _v_ : rolewatchlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		levelindex = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.QCRoleInfoDes _v_ = new fire.pb.battle.QCRoleInfoDes();
			_v_.unmarshal(_os_);
			rolelist.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.QCRoleInfoWatchDes _v_ = new fire.pb.battle.QCRoleInfoWatchDes();
			_v_.unmarshal(_os_);
			rolewatchlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPlayPKFightView) {
			SPlayPKFightView _o_ = (SPlayPKFightView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (school != _o_.school) return false;
			if (levelindex != _o_.levelindex) return false;
			if (!rolelist.equals(_o_.rolelist)) return false;
			if (!rolewatchlist.equals(_o_.rolewatchlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += school;
		_h_ += levelindex;
		_h_ += rolelist.hashCode();
		_h_ += rolewatchlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(school).append(",");
		_sb_.append(levelindex).append(",");
		_sb_.append(rolelist).append(",");
		_sb_.append(rolewatchlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

