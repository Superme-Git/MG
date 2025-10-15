
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ArchMap extends mkdb.XBean implements xbean.ArchMap {
	private int mapid; // 藏宝图上记录的地图id
	private int posx; // 藏宝图上记录的x坐标
	private int posy; // 藏宝图上记录的y坐标

	@Override
	public void _reset_unsafe_() {
		mapid = 0;
		posx = 0;
		posy = 0;
	}

	ArchMap(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ArchMap() {
		this(0, null, null);
	}

	public ArchMap(ArchMap _o_) {
		this(_o_, null, null);
	}

	ArchMap(xbean.ArchMap _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ArchMap) assign((ArchMap)_o1_);
		else if (_o1_ instanceof ArchMap.Data) assign((ArchMap.Data)_o1_);
		else if (_o1_ instanceof ArchMap.Const) assign(((ArchMap.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ArchMap _o_) {
		_o_._xdb_verify_unsafe_();
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
	}

	private void assign(ArchMap.Data _o_) {
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(mapid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		mapid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ArchMap copy() {
		_xdb_verify_unsafe_();
		return new ArchMap(this);
	}

	@Override
	public xbean.ArchMap toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ArchMap toBean() {
		_xdb_verify_unsafe_();
		return new ArchMap(this); // same as copy()
	}

	@Override
	public xbean.ArchMap toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ArchMap toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getMapid() { // 藏宝图上记录的地图id
		_xdb_verify_unsafe_();
		return mapid;
	}

	@Override
	public int getPosx() { // 藏宝图上记录的x坐标
		_xdb_verify_unsafe_();
		return posx;
	}

	@Override
	public int getPosy() { // 藏宝图上记录的y坐标
		_xdb_verify_unsafe_();
		return posy;
	}

	@Override
	public void setMapid(int _v_) { // 藏宝图上记录的地图id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mapid) {
					public void rollback() { mapid = _xdb_saved; }
				};}});
		mapid = _v_;
	}

	@Override
	public void setPosx(int _v_) { // 藏宝图上记录的x坐标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posx) {
					public void rollback() { posx = _xdb_saved; }
				};}});
		posx = _v_;
	}

	@Override
	public void setPosy(int _v_) { // 藏宝图上记录的y坐标
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
		ArchMap _o_ = null;
		if ( _o1_ instanceof ArchMap ) _o_ = (ArchMap)_o1_;
		else if ( _o1_ instanceof ArchMap.Const ) _o_ = ((ArchMap.Const)_o1_).nThis();
		else return false;
		if (mapid != _o_.mapid) return false;
		if (posx != _o_.posx) return false;
		if (posy != _o_.posy) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
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
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posy"));
		return lb;
	}

	private class Const implements xbean.ArchMap {
		ArchMap nThis() {
			return ArchMap.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ArchMap copy() {
			return ArchMap.this.copy();
		}

		@Override
		public xbean.ArchMap toData() {
			return ArchMap.this.toData();
		}

		public xbean.ArchMap toBean() {
			return ArchMap.this.toBean();
		}

		@Override
		public xbean.ArchMap toDataIf() {
			return ArchMap.this.toDataIf();
		}

		public xbean.ArchMap toBeanIf() {
			return ArchMap.this.toBeanIf();
		}

		@Override
		public int getMapid() { // 藏宝图上记录的地图id
			_xdb_verify_unsafe_();
			return mapid;
		}

		@Override
		public int getPosx() { // 藏宝图上记录的x坐标
			_xdb_verify_unsafe_();
			return posx;
		}

		@Override
		public int getPosy() { // 藏宝图上记录的y坐标
			_xdb_verify_unsafe_();
			return posy;
		}

		@Override
		public void setMapid(int _v_) { // 藏宝图上记录的地图id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosx(int _v_) { // 藏宝图上记录的x坐标
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosy(int _v_) { // 藏宝图上记录的y坐标
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
			return ArchMap.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ArchMap.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ArchMap.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ArchMap.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ArchMap.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ArchMap.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ArchMap.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ArchMap.this.hashCode();
		}

		@Override
		public String toString() {
			return ArchMap.this.toString();
		}

	}

	public static final class Data implements xbean.ArchMap {
		private int mapid; // 藏宝图上记录的地图id
		private int posx; // 藏宝图上记录的x坐标
		private int posy; // 藏宝图上记录的y坐标

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ArchMap _o1_) {
			if (_o1_ instanceof ArchMap) assign((ArchMap)_o1_);
			else if (_o1_ instanceof ArchMap.Data) assign((ArchMap.Data)_o1_);
			else if (_o1_ instanceof ArchMap.Const) assign(((ArchMap.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ArchMap _o_) {
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
		}

		private void assign(ArchMap.Data _o_) {
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(mapid);
			_os_.marshal(posx);
			_os_.marshal(posy);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			mapid = _os_.unmarshal_int();
			posx = _os_.unmarshal_int();
			posy = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ArchMap copy() {
			return new Data(this);
		}

		@Override
		public xbean.ArchMap toData() {
			return new Data(this);
		}

		public xbean.ArchMap toBean() {
			return new ArchMap(this, null, null);
		}

		@Override
		public xbean.ArchMap toDataIf() {
			return this;
		}

		public xbean.ArchMap toBeanIf() {
			return new ArchMap(this, null, null);
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
		public int getMapid() { // 藏宝图上记录的地图id
			return mapid;
		}

		@Override
		public int getPosx() { // 藏宝图上记录的x坐标
			return posx;
		}

		@Override
		public int getPosy() { // 藏宝图上记录的y坐标
			return posy;
		}

		@Override
		public void setMapid(int _v_) { // 藏宝图上记录的地图id
			mapid = _v_;
		}

		@Override
		public void setPosx(int _v_) { // 藏宝图上记录的x坐标
			posx = _v_;
		}

		@Override
		public void setPosy(int _v_) { // 藏宝图上记录的y坐标
			posy = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ArchMap.Data)) return false;
			ArchMap.Data _o_ = (ArchMap.Data) _o1_;
			if (mapid != _o_.mapid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += mapid;
			_h_ += posx;
			_h_ += posy;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
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
