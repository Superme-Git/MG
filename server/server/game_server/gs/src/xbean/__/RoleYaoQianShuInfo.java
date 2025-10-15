
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleYaoQianShuInfo extends mkdb.XBean implements xbean.RoleYaoQianShuInfo {
	private java.util.ArrayList<Long> lookroleids; // 照顾过的玩家id
	private int mapid; // 
	private int posx; // 
	private int posy; // 

	@Override
	public void _reset_unsafe_() {
		lookroleids.clear();
		mapid = 0;
		posx = 0;
		posy = 0;
	}

	RoleYaoQianShuInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		lookroleids = new java.util.ArrayList<Long>();
	}

	public RoleYaoQianShuInfo() {
		this(0, null, null);
	}

	public RoleYaoQianShuInfo(RoleYaoQianShuInfo _o_) {
		this(_o_, null, null);
	}

	RoleYaoQianShuInfo(xbean.RoleYaoQianShuInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleYaoQianShuInfo) assign((RoleYaoQianShuInfo)_o1_);
		else if (_o1_ instanceof RoleYaoQianShuInfo.Data) assign((RoleYaoQianShuInfo.Data)_o1_);
		else if (_o1_ instanceof RoleYaoQianShuInfo.Const) assign(((RoleYaoQianShuInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleYaoQianShuInfo _o_) {
		_o_._xdb_verify_unsafe_();
		lookroleids = new java.util.ArrayList<Long>();
		lookroleids.addAll(_o_.lookroleids);
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
	}

	private void assign(RoleYaoQianShuInfo.Data _o_) {
		lookroleids = new java.util.ArrayList<Long>();
		lookroleids.addAll(_o_.lookroleids);
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(lookroleids.size());
		for (Long _v_ : lookroleids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(mapid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			lookroleids.add(_v_);
		}
		mapid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleYaoQianShuInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleYaoQianShuInfo(this);
	}

	@Override
	public xbean.RoleYaoQianShuInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleYaoQianShuInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleYaoQianShuInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleYaoQianShuInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleYaoQianShuInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getLookroleids() { // 照顾过的玩家id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "lookroleids"), lookroleids);
	}

	public java.util.List<Long> getLookroleidsAsData() { // 照顾过的玩家id
		_xdb_verify_unsafe_();
		java.util.List<Long> lookroleids;
		RoleYaoQianShuInfo _o_ = this;
		lookroleids = new java.util.ArrayList<Long>();
		lookroleids.addAll(_o_.lookroleids);
		return lookroleids;
	}

	@Override
	public int getMapid() { // 
		_xdb_verify_unsafe_();
		return mapid;
	}

	@Override
	public int getPosx() { // 
		_xdb_verify_unsafe_();
		return posx;
	}

	@Override
	public int getPosy() { // 
		_xdb_verify_unsafe_();
		return posy;
	}

	@Override
	public void setMapid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mapid) {
					public void rollback() { mapid = _xdb_saved; }
				};}});
		mapid = _v_;
	}

	@Override
	public void setPosx(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posx) {
					public void rollback() { posx = _xdb_saved; }
				};}});
		posx = _v_;
	}

	@Override
	public void setPosy(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posy") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posy) {
					public void rollback() { posy = _xdb_saved; }
				};}});
		posy = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleYaoQianShuInfo _o_ = null;
		if ( _o1_ instanceof RoleYaoQianShuInfo ) _o_ = (RoleYaoQianShuInfo)_o1_;
		else if ( _o1_ instanceof RoleYaoQianShuInfo.Const ) _o_ = ((RoleYaoQianShuInfo.Const)_o1_).nThis();
		else return false;
		if (!lookroleids.equals(_o_.lookroleids)) return false;
		if (mapid != _o_.mapid) return false;
		if (posx != _o_.posx) return false;
		if (posy != _o_.posy) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += lookroleids.hashCode();
		_h_ += mapid;
		_h_ += posx;
		_h_ += posy;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lookroleids);
		_sb_.append(",");
		_sb_.append(mapid);
		_sb_.append(",");
		_sb_.append(posx);
		_sb_.append(",");
		_sb_.append(posy);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lookroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posy"));
		return lb;
	}

	private class Const implements xbean.RoleYaoQianShuInfo {
		RoleYaoQianShuInfo nThis() {
			return RoleYaoQianShuInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleYaoQianShuInfo copy() {
			return RoleYaoQianShuInfo.this.copy();
		}

		@Override
		public xbean.RoleYaoQianShuInfo toData() {
			return RoleYaoQianShuInfo.this.toData();
		}

		public xbean.RoleYaoQianShuInfo toBean() {
			return RoleYaoQianShuInfo.this.toBean();
		}

		@Override
		public xbean.RoleYaoQianShuInfo toDataIf() {
			return RoleYaoQianShuInfo.this.toDataIf();
		}

		public xbean.RoleYaoQianShuInfo toBeanIf() {
			return RoleYaoQianShuInfo.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getLookroleids() { // 照顾过的玩家id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(lookroleids);
		}

		public java.util.List<Long> getLookroleidsAsData() { // 照顾过的玩家id
			_xdb_verify_unsafe_();
			java.util.List<Long> lookroleids;
			RoleYaoQianShuInfo _o_ = RoleYaoQianShuInfo.this;
		lookroleids = new java.util.ArrayList<Long>();
		lookroleids.addAll(_o_.lookroleids);
			return lookroleids;
		}

		@Override
		public int getMapid() { // 
			_xdb_verify_unsafe_();
			return mapid;
		}

		@Override
		public int getPosx() { // 
			_xdb_verify_unsafe_();
			return posx;
		}

		@Override
		public int getPosy() { // 
			_xdb_verify_unsafe_();
			return posy;
		}

		@Override
		public void setMapid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosx(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosy(int _v_) { // 
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
			return RoleYaoQianShuInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleYaoQianShuInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleYaoQianShuInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleYaoQianShuInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleYaoQianShuInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleYaoQianShuInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleYaoQianShuInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleYaoQianShuInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleYaoQianShuInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleYaoQianShuInfo {
		private java.util.ArrayList<Long> lookroleids; // 照顾过的玩家id
		private int mapid; // 
		private int posx; // 
		private int posy; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			lookroleids = new java.util.ArrayList<Long>();
		}

		Data(xbean.RoleYaoQianShuInfo _o1_) {
			if (_o1_ instanceof RoleYaoQianShuInfo) assign((RoleYaoQianShuInfo)_o1_);
			else if (_o1_ instanceof RoleYaoQianShuInfo.Data) assign((RoleYaoQianShuInfo.Data)_o1_);
			else if (_o1_ instanceof RoleYaoQianShuInfo.Const) assign(((RoleYaoQianShuInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleYaoQianShuInfo _o_) {
			lookroleids = new java.util.ArrayList<Long>();
			lookroleids.addAll(_o_.lookroleids);
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
		}

		private void assign(RoleYaoQianShuInfo.Data _o_) {
			lookroleids = new java.util.ArrayList<Long>();
			lookroleids.addAll(_o_.lookroleids);
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(lookroleids.size());
			for (Long _v_ : lookroleids) {
				_os_.marshal(_v_);
			}
			_os_.marshal(mapid);
			_os_.marshal(posx);
			_os_.marshal(posy);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				lookroleids.add(_v_);
			}
			mapid = _os_.unmarshal_int();
			posx = _os_.unmarshal_int();
			posy = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleYaoQianShuInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleYaoQianShuInfo toData() {
			return new Data(this);
		}

		public xbean.RoleYaoQianShuInfo toBean() {
			return new RoleYaoQianShuInfo(this, null, null);
		}

		@Override
		public xbean.RoleYaoQianShuInfo toDataIf() {
			return this;
		}

		public xbean.RoleYaoQianShuInfo toBeanIf() {
			return new RoleYaoQianShuInfo(this, null, null);
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
		public java.util.List<Long> getLookroleids() { // 照顾过的玩家id
			return lookroleids;
		}

		@Override
		public java.util.List<Long> getLookroleidsAsData() { // 照顾过的玩家id
			return lookroleids;
		}

		@Override
		public int getMapid() { // 
			return mapid;
		}

		@Override
		public int getPosx() { // 
			return posx;
		}

		@Override
		public int getPosy() { // 
			return posy;
		}

		@Override
		public void setMapid(int _v_) { // 
			mapid = _v_;
		}

		@Override
		public void setPosx(int _v_) { // 
			posx = _v_;
		}

		@Override
		public void setPosy(int _v_) { // 
			posy = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleYaoQianShuInfo.Data)) return false;
			RoleYaoQianShuInfo.Data _o_ = (RoleYaoQianShuInfo.Data) _o1_;
			if (!lookroleids.equals(_o_.lookroleids)) return false;
			if (mapid != _o_.mapid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += lookroleids.hashCode();
			_h_ += mapid;
			_h_ += posx;
			_h_ += posy;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(lookroleids);
			_sb_.append(",");
			_sb_.append(mapid);
			_sb_.append(",");
			_sb_.append(posx);
			_sb_.append(",");
			_sb_.append(posy);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
