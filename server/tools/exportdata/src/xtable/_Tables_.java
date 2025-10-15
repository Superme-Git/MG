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
		add(locojoyplatusers);
		add(locojoyplatordergametoplat);
		add(locojoyplatorderinfos);
		add(users);
	}

	// visible in package
	xdb.TTable<String, xbean.LocojoyPlatUserInfo> locojoyplatusers = new xdb.TTable<String, xbean.LocojoyPlatUserInfo>() {
		@Override
		public String getName() {
			return "locojoyplatusers";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LocojoyPlatUserInfo value) {
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
		public xbean.LocojoyPlatUserInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LocojoyPlatUserInfo value = xbean.Pod.newLocojoyPlatUserInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LocojoyPlatUserInfo newValue() {
			xbean.LocojoyPlatUserInfo value = xbean.Pod.newLocojoyPlatUserInfo();
			return value;
		}

	};

	xdb.TTable<String, String> locojoyplatordergametoplat = new xdb.TTable<String, String>() {
		@Override
		public String getName() {
			return "locojoyplatordergametoplat";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(String value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public String unmarshalValue(OctetsStream _os_) throws MarshalException {
			String value = "";
			value = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return value;
		}

		@Override
		public String newValue() {
			String value = "";
			return value;
		}

	};

	xdb.TTable<String, xbean.LocojoyPlatOrderInfo> locojoyplatorderinfos = new xdb.TTable<String, xbean.LocojoyPlatOrderInfo>() {
		@Override
		public String getName() {
			return "locojoyplatorderinfos";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.LocojoyPlatOrderInfo value) {
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
		public xbean.LocojoyPlatOrderInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.LocojoyPlatOrderInfo value = xbean.Pod.newLocojoyPlatOrderInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.LocojoyPlatOrderInfo newValue() {
			xbean.LocojoyPlatOrderInfo value = xbean.Pod.newLocojoyPlatOrderInfo();
			return value;
		}

	};

	xdb.TTable<Long, xbean.UserInfo> users = new xdb.TTable<Long, xbean.UserInfo>() {
		@Override
		public String getName() {
			return "users";
		}

		@Override
		protected xdb.util.AutoKey<Long> bindAutoKey() {
			return getInstance().getTableSys().getAutoKeys().getAutoKeyLong(getName());
		}

		@Override
		public OctetsStream marshalKey(Long key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(xbean.UserInfo value) {
			OctetsStream _os_ = new OctetsStream();
			value.marshal(_os_);
			return _os_;
		}

		@Override
		public Long unmarshalKey(OctetsStream _os_) throws MarshalException {
			long key = 0;
			key = _os_.unmarshal_long();
			return key;
		}

		@Override
		public xbean.UserInfo unmarshalValue(OctetsStream _os_) throws MarshalException {
			xbean.UserInfo value = xbean.Pod.newUserInfo();
			value.unmarshal(_os_);
			return value;
		}

		@Override
		public xbean.UserInfo newValue() {
			xbean.UserInfo value = xbean.Pod.newUserInfo();
			return value;
		}

	};


}
