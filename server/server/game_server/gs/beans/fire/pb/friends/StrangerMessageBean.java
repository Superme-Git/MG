
package fire.pb.friends;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class StrangerMessageBean implements Marshal {
	public fire.pb.friends.InfoBean friendinfobean;
	public java.lang.String content;
	public java.util.ArrayList<com.locojoy.base.Octets> details; // 展示品信息
	public java.util.ArrayList<fire.pb.talk.DisplayInfo> displayinfo; // 展示的物品key与type

	public StrangerMessageBean() {
		friendinfobean = new fire.pb.friends.InfoBean();
		content = "";
		details = new java.util.ArrayList<com.locojoy.base.Octets>();
		displayinfo = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
	}

	public StrangerMessageBean(fire.pb.friends.InfoBean _friendinfobean_, java.lang.String _content_, java.util.ArrayList<com.locojoy.base.Octets> _details_, java.util.ArrayList<fire.pb.talk.DisplayInfo> _displayinfo_) {
		this.friendinfobean = _friendinfobean_;
		this.content = _content_;
		this.details = _details_;
		this.displayinfo = _displayinfo_;
	}

	public final boolean _validator_() {
		if (!friendinfobean._validator_()) return false;
		for (fire.pb.talk.DisplayInfo _v_ : displayinfo)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(friendinfobean);
		_os_.marshal(content, "UTF-16LE");
		_os_.compact_uint32(details.size());
		for (com.locojoy.base.Octets _v_ : details) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(displayinfo.size());
		for (fire.pb.talk.DisplayInfo _v_ : displayinfo) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		friendinfobean.unmarshal(_os_);
		content = _os_.unmarshal_String("UTF-16LE");
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			details.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.talk.DisplayInfo _v_ = new fire.pb.talk.DisplayInfo();
			_v_.unmarshal(_os_);
			displayinfo.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof StrangerMessageBean) {
			StrangerMessageBean _o_ = (StrangerMessageBean)_o1_;
			if (!friendinfobean.equals(_o_.friendinfobean)) return false;
			if (!content.equals(_o_.content)) return false;
			if (!details.equals(_o_.details)) return false;
			if (!displayinfo.equals(_o_.displayinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friendinfobean.hashCode();
		_h_ += content.hashCode();
		_h_ += details.hashCode();
		_h_ += displayinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendinfobean).append(",");
		_sb_.append("T").append(content.length()).append(",");
		_sb_.append(details).append(",");
		_sb_.append(displayinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

