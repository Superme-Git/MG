
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 时光之穴副本
*/
public class LineInfo implements Marshal , Comparable<LineInfo>{
	public int id; // 副本id
	public int state; // 0 未开启; 1 可以进入, 2 已经完成
	public int finish; // 完成的次数

	public LineInfo() {
	}

	public LineInfo(int _id_, int _state_, int _finish_) {
		this.id = _id_;
		this.state = _state_;
		this.finish = _finish_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(state);
		_os_.marshal(finish);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		finish = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LineInfo) {
			LineInfo _o_ = (LineInfo)_o1_;
			if (id != _o_.id) return false;
			if (state != _o_.state) return false;
			if (finish != _o_.finish) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += state;
		_h_ += finish;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(state).append(",");
		_sb_.append(finish).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(LineInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		_c_ = finish - _o_.finish;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

