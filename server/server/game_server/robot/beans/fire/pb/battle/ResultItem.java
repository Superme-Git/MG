
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ResultItem implements Marshal {
	public fire.pb.battle.DemoExecute execute;
	public java.util.LinkedList<fire.pb.battle.SubResultItem> subresultlist;
	public java.util.LinkedList<fire.pb.battle.FighterInfo> newfighter;
	public java.util.HashMap<Integer,Float> rolechangedattrs; // 战斗脚本接收者，此动作后属性的变化（战斗中属性实时变化）
	public java.util.HashMap<Integer,Float> petchangedattrs; // 战斗脚本接收者的宠物，此动作后属性的变化

	public ResultItem() {
		execute = new fire.pb.battle.DemoExecute();
		subresultlist = new java.util.LinkedList<fire.pb.battle.SubResultItem>();
		newfighter = new java.util.LinkedList<fire.pb.battle.FighterInfo>();
		rolechangedattrs = new java.util.HashMap<Integer,Float>();
		petchangedattrs = new java.util.HashMap<Integer,Float>();
	}

	public ResultItem(fire.pb.battle.DemoExecute _execute_, java.util.LinkedList<fire.pb.battle.SubResultItem> _subresultlist_, java.util.LinkedList<fire.pb.battle.FighterInfo> _newfighter_, java.util.HashMap<Integer,Float> _rolechangedattrs_, java.util.HashMap<Integer,Float> _petchangedattrs_) {
		this.execute = _execute_;
		this.subresultlist = _subresultlist_;
		this.newfighter = _newfighter_;
		this.rolechangedattrs = _rolechangedattrs_;
		this.petchangedattrs = _petchangedattrs_;
	}

	public final boolean _validator_() {
		if (!execute._validator_()) return false;
		for (fire.pb.battle.SubResultItem _v_ : subresultlist)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.FighterInfo _v_ : newfighter)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(execute);
		_os_.compact_uint32(subresultlist.size());
		for (fire.pb.battle.SubResultItem _v_ : subresultlist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(newfighter.size());
		for (fire.pb.battle.FighterInfo _v_ : newfighter) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(rolechangedattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : rolechangedattrs.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(petchangedattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : petchangedattrs.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		execute.unmarshal(_os_);
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.SubResultItem _v_ = new fire.pb.battle.SubResultItem();
			_v_.unmarshal(_os_);
			subresultlist.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.FighterInfo _v_ = new fire.pb.battle.FighterInfo();
			_v_.unmarshal(_os_);
			newfighter.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			rolechangedattrs.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			petchangedattrs.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ResultItem) {
			ResultItem _o_ = (ResultItem)_o1_;
			if (!execute.equals(_o_.execute)) return false;
			if (!subresultlist.equals(_o_.subresultlist)) return false;
			if (!newfighter.equals(_o_.newfighter)) return false;
			if (!rolechangedattrs.equals(_o_.rolechangedattrs)) return false;
			if (!petchangedattrs.equals(_o_.petchangedattrs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += execute.hashCode();
		_h_ += subresultlist.hashCode();
		_h_ += newfighter.hashCode();
		_h_ += rolechangedattrs.hashCode();
		_h_ += petchangedattrs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(execute).append(",");
		_sb_.append(subresultlist).append(",");
		_sb_.append(newfighter).append(",");
		_sb_.append(rolechangedattrs).append(",");
		_sb_.append(petchangedattrs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

