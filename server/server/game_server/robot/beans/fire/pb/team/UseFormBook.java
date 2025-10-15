
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class UseFormBook implements Marshal , Comparable<UseFormBook>{
	public int bookid;
	public int num;

	public UseFormBook() {
	}

	public UseFormBook(int _bookid_, int _num_) {
		this.bookid = _bookid_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(bookid);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		bookid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof UseFormBook) {
			UseFormBook _o_ = (UseFormBook)_o1_;
			if (bookid != _o_.bookid) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += bookid;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(bookid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(UseFormBook _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = bookid - _o_.bookid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

