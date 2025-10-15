package xtable;

import com.goldhuman.Common.Marshal.OctetsStream;
import com.goldhuman.Common.Marshal.MarshalException;

public class _Tables_ extends xdb.Tables {
	static volatile boolean isExplicitLockCheck = false;

	public static void startExplicitLockCheck() {
		isExplicitLockCheck = true;
	}

	public static _Tables_ getInstance() {
		return (_Tables_)xdb.Xdb.getInstance().getTables();
	}

	public _Tables_() {
		add(commongroup);
		add(activeuser);
		add(role);
		add(shop);
		add(faction);
		add(family);
		add(cdkey);
	}

	// visible in package
	xdb.TTable<String, xbean.NameState> commongroup = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "commongroup";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};

	xdb.TTable<String, xbean.NameState> activeuser = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "activeuser";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};

	xdb.TTable<String, xbean.NameState> role = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "role";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};

	xdb.TTable<String, xbean.NameState> shop = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "shop";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};

	xdb.TTable<String, xbean.NameState> faction = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "faction";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};

	xdb.TTable<String, xbean.NameState> family = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "family";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};

	xdb.TTable<String, xbean.NameState> cdkey = new xdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "cdkey";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.NameState value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public xbean.NameState unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.NameState value = xbean.Pod.newNameState();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.NameState newValue() {
			xbean.NameState value = xbean.Pod.newNameState();
			return value;
		}

	};


}
