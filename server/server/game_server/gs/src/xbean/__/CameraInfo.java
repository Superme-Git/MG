
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CameraInfo extends mkdb.XBean implements xbean.CameraInfo {
	private long endtime; // 
	private int sizebeforezip; // 
	private int sizeafterzip; // 
	private String camerafileurl; // 

	@Override
	public void _reset_unsafe_() {
		endtime = 0;
		sizebeforezip = 0;
		sizeafterzip = 0;
		camerafileurl = "";
	}

	CameraInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		endtime = 0;
		sizebeforezip = 0;
		sizeafterzip = 0;
		camerafileurl = "";
	}

	public CameraInfo() {
		this(0, null, null);
	}

	public CameraInfo(CameraInfo _o_) {
		this(_o_, null, null);
	}

	CameraInfo(xbean.CameraInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CameraInfo) assign((CameraInfo)_o1_);
		else if (_o1_ instanceof CameraInfo.Data) assign((CameraInfo.Data)_o1_);
		else if (_o1_ instanceof CameraInfo.Const) assign(((CameraInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CameraInfo _o_) {
		_o_._xdb_verify_unsafe_();
		endtime = _o_.endtime;
		sizebeforezip = _o_.sizebeforezip;
		sizeafterzip = _o_.sizeafterzip;
		camerafileurl = _o_.camerafileurl;
	}

	private void assign(CameraInfo.Data _o_) {
		endtime = _o_.endtime;
		sizebeforezip = _o_.sizebeforezip;
		sizeafterzip = _o_.sizeafterzip;
		camerafileurl = _o_.camerafileurl;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(endtime);
		_os_.marshal(sizebeforezip);
		_os_.marshal(sizeafterzip);
		_os_.marshal(camerafileurl, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		endtime = _os_.unmarshal_long();
		sizebeforezip = _os_.unmarshal_int();
		sizeafterzip = _os_.unmarshal_int();
		camerafileurl = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.CameraInfo copy() {
		_xdb_verify_unsafe_();
		return new CameraInfo(this);
	}

	@Override
	public xbean.CameraInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CameraInfo toBean() {
		_xdb_verify_unsafe_();
		return new CameraInfo(this); // same as copy()
	}

	@Override
	public xbean.CameraInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CameraInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getEndtime() { // 
		_xdb_verify_unsafe_();
		return endtime;
	}

	@Override
	public int getSizebeforezip() { // 
		_xdb_verify_unsafe_();
		return sizebeforezip;
	}

	@Override
	public int getSizeafterzip() { // 
		_xdb_verify_unsafe_();
		return sizeafterzip;
	}

	@Override
	public String getCamerafileurl() { // 
		_xdb_verify_unsafe_();
		return camerafileurl;
	}

	@Override
	public com.locojoy.base.Octets getCamerafileurlOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getCamerafileurl(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setEndtime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, endtime) {
					public void rollback() { endtime = _xdb_saved; }
				};}});
		endtime = _v_;
	}

	@Override
	public void setSizebeforezip(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sizebeforezip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sizebeforezip) {
					public void rollback() { sizebeforezip = _xdb_saved; }
				};}});
		sizebeforezip = _v_;
	}

	@Override
	public void setSizeafterzip(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sizeafterzip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sizeafterzip) {
					public void rollback() { sizeafterzip = _xdb_saved; }
				};}});
		sizeafterzip = _v_;
	}

	@Override
	public void setCamerafileurl(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "camerafileurl") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, camerafileurl) {
					public void rollback() { camerafileurl = _xdb_saved; }
				};}});
		camerafileurl = _v_;
	}

	@Override
	public void setCamerafileurlOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setCamerafileurl(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CameraInfo _o_ = null;
		if ( _o1_ instanceof CameraInfo ) _o_ = (CameraInfo)_o1_;
		else if ( _o1_ instanceof CameraInfo.Const ) _o_ = ((CameraInfo.Const)_o1_).nThis();
		else return false;
		if (endtime != _o_.endtime) return false;
		if (sizebeforezip != _o_.sizebeforezip) return false;
		if (sizeafterzip != _o_.sizeafterzip) return false;
		if (!camerafileurl.equals(_o_.camerafileurl)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += endtime;
		_h_ += sizebeforezip;
		_h_ += sizeafterzip;
		_h_ += camerafileurl.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(endtime);
		_sb_.append(",");
		_sb_.append(sizebeforezip);
		_sb_.append(",");
		_sb_.append(sizeafterzip);
		_sb_.append(",");
		_sb_.append("'").append(camerafileurl).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sizebeforezip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sizeafterzip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("camerafileurl"));
		return lb;
	}

	private class Const implements xbean.CameraInfo {
		CameraInfo nThis() {
			return CameraInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CameraInfo copy() {
			return CameraInfo.this.copy();
		}

		@Override
		public xbean.CameraInfo toData() {
			return CameraInfo.this.toData();
		}

		public xbean.CameraInfo toBean() {
			return CameraInfo.this.toBean();
		}

		@Override
		public xbean.CameraInfo toDataIf() {
			return CameraInfo.this.toDataIf();
		}

		public xbean.CameraInfo toBeanIf() {
			return CameraInfo.this.toBeanIf();
		}

		@Override
		public long getEndtime() { // 
			_xdb_verify_unsafe_();
			return endtime;
		}

		@Override
		public int getSizebeforezip() { // 
			_xdb_verify_unsafe_();
			return sizebeforezip;
		}

		@Override
		public int getSizeafterzip() { // 
			_xdb_verify_unsafe_();
			return sizeafterzip;
		}

		@Override
		public String getCamerafileurl() { // 
			_xdb_verify_unsafe_();
			return camerafileurl;
		}

		@Override
		public com.locojoy.base.Octets getCamerafileurlOctets() { // 
			_xdb_verify_unsafe_();
			return CameraInfo.this.getCamerafileurlOctets();
		}

		@Override
		public void setEndtime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSizebeforezip(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSizeafterzip(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCamerafileurl(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCamerafileurlOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean toConst() {
			_xdb_verify_unsafe_();
			return this;
		}

		@Override
		public boolean isConst() {
			_xdb_verify_unsafe_();
			return true;
		}

		@Override
		public boolean isData() {
			return CameraInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CameraInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CameraInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CameraInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CameraInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CameraInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CameraInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CameraInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return CameraInfo.this.toString();
		}

	}

	public static final class Data implements xbean.CameraInfo {
		private long endtime; // 
		private int sizebeforezip; // 
		private int sizeafterzip; // 
		private String camerafileurl; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			endtime = 0;
			sizebeforezip = 0;
			sizeafterzip = 0;
			camerafileurl = "";
		}

		Data(xbean.CameraInfo _o1_) {
			if (_o1_ instanceof CameraInfo) assign((CameraInfo)_o1_);
			else if (_o1_ instanceof CameraInfo.Data) assign((CameraInfo.Data)_o1_);
			else if (_o1_ instanceof CameraInfo.Const) assign(((CameraInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CameraInfo _o_) {
			endtime = _o_.endtime;
			sizebeforezip = _o_.sizebeforezip;
			sizeafterzip = _o_.sizeafterzip;
			camerafileurl = _o_.camerafileurl;
		}

		private void assign(CameraInfo.Data _o_) {
			endtime = _o_.endtime;
			sizebeforezip = _o_.sizebeforezip;
			sizeafterzip = _o_.sizeafterzip;
			camerafileurl = _o_.camerafileurl;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(endtime);
			_os_.marshal(sizebeforezip);
			_os_.marshal(sizeafterzip);
			_os_.marshal(camerafileurl, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			endtime = _os_.unmarshal_long();
			sizebeforezip = _os_.unmarshal_int();
			sizeafterzip = _os_.unmarshal_int();
			camerafileurl = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.CameraInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.CameraInfo toData() {
			return new Data(this);
		}

		public xbean.CameraInfo toBean() {
			return new CameraInfo(this, null, null);
		}

		@Override
		public xbean.CameraInfo toDataIf() {
			return this;
		}

		public xbean.CameraInfo toBeanIf() {
			return new CameraInfo(this, null, null);
		}

		// mkdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public mkdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public mkdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public long getEndtime() { // 
			return endtime;
		}

		@Override
		public int getSizebeforezip() { // 
			return sizebeforezip;
		}

		@Override
		public int getSizeafterzip() { // 
			return sizeafterzip;
		}

		@Override
		public String getCamerafileurl() { // 
			return camerafileurl;
		}

		@Override
		public com.locojoy.base.Octets getCamerafileurlOctets() { // 
			return com.locojoy.base.Octets.wrap(getCamerafileurl(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setEndtime(long _v_) { // 
			endtime = _v_;
		}

		@Override
		public void setSizebeforezip(int _v_) { // 
			sizebeforezip = _v_;
		}

		@Override
		public void setSizeafterzip(int _v_) { // 
			sizeafterzip = _v_;
		}

		@Override
		public void setCamerafileurl(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			camerafileurl = _v_;
		}

		@Override
		public void setCamerafileurlOctets(com.locojoy.base.Octets _v_) { // 
			this.setCamerafileurl(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CameraInfo.Data)) return false;
			CameraInfo.Data _o_ = (CameraInfo.Data) _o1_;
			if (endtime != _o_.endtime) return false;
			if (sizebeforezip != _o_.sizebeforezip) return false;
			if (sizeafterzip != _o_.sizeafterzip) return false;
			if (!camerafileurl.equals(_o_.camerafileurl)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += endtime;
			_h_ += sizebeforezip;
			_h_ += sizeafterzip;
			_h_ += camerafileurl.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(endtime);
			_sb_.append(",");
			_sb_.append(sizebeforezip);
			_sb_.append(",");
			_sb_.append(sizeafterzip);
			_sb_.append(",");
			_sb_.append("'").append(camerafileurl).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
