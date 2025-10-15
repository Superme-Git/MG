package xtable;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class _Tables_ extends mkdb.Tables {
	static volatile boolean isExplicitLockCheck = false;

	public static void startExplicitLockCheck() {
		isExplicitLockCheck = true;
	}

	public static _Tables_ getInstance() {
		return (_Tables_)mkdb.Mkdb.getInstance().getTables();
	}

	public _Tables_() {
		add(locojoyplatusers);
		add(locojoyplatordergametoplat);
		add(locojoyplatorderinfos);
		add(userschargerefund);
		add(users);
	}

	// visible in package
	mkdb.TTable<String, xbean.LocojoyPlatUserInfo> locojoyplatusers = new mkdb.TTable<String, xbean.LocojoyPlatUserInfo>() {
		@Override
		public String getName() {
			return "locojoyplatusers";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
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
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
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

	mkdb.TTable<String, String> locojoyplatordergametoplat = new mkdb.TTable<String, String>() {
		@Override
		public String getName() {
			return "locojoyplatordergametoplat";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(String value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public String unmarshalValue(OctetsStream _os_) throws MarshalException {
			String value = "";
			value = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return value;
		}

		@Override
		public String newValue() {
			String value = "";
			return value;
		}

	};

	mkdb.TTable<String, xbean.LocojoyPlatOrderInfo> locojoyplatorderinfos = new mkdb.TTable<String, xbean.LocojoyPlatOrderInfo>() {
		@Override
		public String getName() {
			return "locojoyplatorderinfos";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
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
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
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

	mkdb.TTable<String, Integer> userschargerefund = new mkdb.TTable<String, Integer>() {
		@Override
		public String getName() {
			return "userschargerefund";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public OctetsStream marshalValue(Integer value) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(value);
			return _os_;
		}

		@Override
		public String unmarshalKey(OctetsStream _os_) throws MarshalException {
			String key = "";
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return key;
		}

		@Override
		public Integer unmarshalValue(OctetsStream _os_) throws MarshalException {
			int value = 0;
			value = _os_.unmarshal_int();
			return value;
		}

		@Override
		public Integer newValue() {
			int value = 0;
			return value;
		}

	};

	mkdb.TTable<Long, xbean.UserInfo> users = new mkdb.TTable<Long, xbean.UserInfo>() {
		@Override
		public String getName() {
			return "users";
		}

		@Override
		protected mkdb.util.AutoKey<Long> bindAutoKey() {
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
