
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MazeInfo extends mkdb.XBean implements xbean.MazeInfo {
	private int mapid; // 地图id
	private int pos; // 位置编号或者npcid，待定

	@Override
	public void _reset_unsafe_() {
		mapid = 0;
		pos = 0;
	}

	MazeInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public MazeInfo() {
		this(0, null, null);
	}

	public MazeInfo(MazeInfo _o_) {
		this(_o_, null, null);
	}

	MazeInfo(xbean.MazeInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MazeInfo) assign((MazeInfo)_o1_);
		else if (_o1_ instanceof MazeInfo.Data) assign((MazeInfo.Data)_o1_);
		else if (_o1_ instanceof MazeInfo.Const) assign(((MazeInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MazeInfo _o_) {
		_o_._xdb_verify_unsafe_();
		mapid = _o_.mapid;
		pos = _o_.pos;
	}

	private void assign(MazeInfo.Data _o_) {
		mapid = _o_.mapid;
		pos = _o_.pos;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(mapid);
		_os_.marshal(pos);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		mapid = _os_.unmarshal_int();
		pos = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MazeInfo copy() {
		_xdb_verify_unsafe_();
		return new MazeInfo(this);
	}

	@Override
	public xbean.MazeInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MazeInfo toBean() {
		_xdb_verify_unsafe_();
		return new MazeInfo(this); // same as copy()
	}

	@Override
	public xbean.MazeInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MazeInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getMapid() { // 地图id
		_xdb_verify_unsafe_();
		return mapid;
	}

	@Override
	public int getPos() { // 位置编号或者npcid，待定
		_xdb_verify_unsafe_();
		return pos;
	}

	@Override
	public void setMapid(int _v_) { // 地图id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mapid) {
					public void rollback() { mapid = _xdb_saved; }
				};}});
		mapid = _v_;
	}

	@Override
	public void setPos(int _v_) { // 位置编号或者npcid，待定
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pos") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, pos) {
					public void rollback() { pos = _xdb_saved; }
				};}});
		pos = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MazeInfo _o_ = null;
		if ( _o1_ instanceof MazeInfo ) _o_ = (MazeInfo)_o1_;
		else if ( _o1_ instanceof MazeInfo.Const ) _o_ = ((MazeInfo.Const)_o1_).nThis();
		else return false;
		if (mapid != _o_.mapid) return false;
		if (pos != _o_.pos) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += mapid;
		_h_ += pos;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(mapid);
		_sb_.append(",");
		_sb_.append(pos);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pos"));
		return lb;
	}

	private class Const implements xbean.MazeInfo {
		MazeInfo nThis() {
			return MazeInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MazeInfo copy() {
			return MazeInfo.this.copy();
		}

		@Override
		public xbean.MazeInfo toData() {
			return MazeInfo.this.toData();
		}

		public xbean.MazeInfo toBean() {
			return MazeInfo.this.toBean();
		}

		@Override
		public xbean.MazeInfo toDataIf() {
			return MazeInfo.this.toDataIf();
		}

		public xbean.MazeInfo toBeanIf() {
			return MazeInfo.this.toBeanIf();
		}

		@Override
		public int getMapid() { // 地图id
			_xdb_verify_unsafe_();
			return mapid;
		}

		@Override
		public int getPos() { // 位置编号或者npcid，待定
			_xdb_verify_unsafe_();
			return pos;
		}

		@Override
		public void setMapid(int _v_) { // 地图id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPos(int _v_) { // 位置编号或者npcid，待定
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
			return MazeInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MazeInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MazeInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MazeInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MazeInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MazeInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MazeInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MazeInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return MazeInfo.this.toString();
		}

	}

	public static final class Data implements xbean.MazeInfo {
		private int mapid; // 地图id
		private int pos; // 位置编号或者npcid，待定

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.MazeInfo _o1_) {
			if (_o1_ instanceof MazeInfo) assign((MazeInfo)_o1_);
			else if (_o1_ instanceof MazeInfo.Data) assign((MazeInfo.Data)_o1_);
			else if (_o1_ instanceof MazeInfo.Const) assign(((MazeInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MazeInfo _o_) {
			mapid = _o_.mapid;
			pos = _o_.pos;
		}

		private void assign(MazeInfo.Data _o_) {
			mapid = _o_.mapid;
			pos = _o_.pos;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(mapid);
			_os_.marshal(pos);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			mapid = _os_.unmarshal_int();
			pos = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MazeInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.MazeInfo toData() {
			return new Data(this);
		}

		public xbean.MazeInfo toBean() {
			return new MazeInfo(this, null, null);
		}

		@Override
		public xbean.MazeInfo toDataIf() {
			return this;
		}

		public xbean.MazeInfo toBeanIf() {
			return new MazeInfo(this, null, null);
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
		public int getMapid() { // 地图id
			return mapid;
		}

		@Override
		public int getPos() { // 位置编号或者npcid，待定
			return pos;
		}

		@Override
		public void setMapid(int _v_) { // 地图id
			mapid = _v_;
		}

		@Override
		public void setPos(int _v_) { // 位置编号或者npcid，待定
			pos = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MazeInfo.Data)) return false;
			MazeInfo.Data _o_ = (MazeInfo.Data) _o1_;
			if (mapid != _o_.mapid) return false;
			if (pos != _o_.pos) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += mapid;
			_h_ += pos;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(mapid);
			_sb_.append(",");
			_sb_.append(pos);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
