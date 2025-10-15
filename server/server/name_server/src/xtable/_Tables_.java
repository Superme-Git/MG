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
		add(role);
		add(clan);
	}

	// visible in package
	mkdb.TTable<String, xbean.NameState> role = new mkdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "role";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
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
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
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

	mkdb.TTable<String, xbean.NameState> clan = new mkdb.TTable<String, xbean.NameState>() {
		@Override
		public String getName() {
			return "clan";
		}

		@Override
		public OctetsStream marshalKey(String key) {
			OctetsStream _os_ = new OctetsStream();
			_os_.marshal(key, mkdb.Const.IO_CHARSET);
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
			key = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
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
