
package fire.pb.title;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 称谓操作错误代号
*/
public class TitleError implements Marshal , Comparable<TitleError>{
	public final static int Title_Err_Unknown = -1;
	public final static int Title_Err_Unexist = -2; // 系统中不存在这个称谓
	public final static int Title_Err_Full = -3;
	public final static int Title_Err_Nuowned = -4; // 用户没有这个称谓


	public TitleError() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TitleError) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(TitleError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

