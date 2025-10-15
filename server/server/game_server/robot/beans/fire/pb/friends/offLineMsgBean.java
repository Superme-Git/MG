
package fire.pb.friends;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class offLineMsgBean implements Marshal {
	public fire.pb.friends.StrangerMessageBean strangermessage;
	public java.lang.String time; // 离线消息的服务器时间

	public offLineMsgBean() {
		strangermessage = new fire.pb.friends.StrangerMessageBean();
		time = "";
	}

	public offLineMsgBean(fire.pb.friends.StrangerMessageBean _strangermessage_, java.lang.String _time_) {
		this.strangermessage = _strangermessage_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		if (!strangermessage._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(strangermessage);
		_os_.marshal(time, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		strangermessage.unmarshal(_os_);
		time = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof offLineMsgBean) {
			offLineMsgBean _o_ = (offLineMsgBean)_o1_;
			if (!strangermessage.equals(_o_.strangermessage)) return false;
			if (!time.equals(_o_.time)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += strangermessage.hashCode();
		_h_ += time.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(strangermessage).append(",");
		_sb_.append("T").append(time.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

