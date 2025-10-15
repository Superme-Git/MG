
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceNpcInfo extends mkdb.XBean implements xbean.InstanceNpcInfo {
	private long npckey; // 目的npc的key
	private int npcid; // 目的npc的id
	private int mapid; // 目的npc的mapid
	private int posx; // 目的坐标
	private int posy; // 目的坐标

	@Override
	public void _reset_unsafe_() {
		npckey = 0L;
		npcid = 0;
		mapid = 0;
		posx = 0;
		posy = 0;
	}

	InstanceNpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public InstanceNpcInfo() {
		this(0, null, null);
	}

	public InstanceNpcInfo(InstanceNpcInfo _o_) {
		this(_o_, null, null);
	}

	InstanceNpcInfo(xbean.InstanceNpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceNpcInfo) assign((InstanceNpcInfo)_o1_);
		else if (_o1_ instanceof InstanceNpcInfo.Data) assign((InstanceNpcInfo.Data)_o1_);
		else if (_o1_ instanceof InstanceNpcInfo.Const) assign(((InstanceNpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceNpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		npckey = _o_.npckey;
		npcid = _o_.npcid;
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
	}

	private void assign(InstanceNpcInfo.Data _o_) {
		npckey = _o_.npckey;
		npcid = _o_.npcid;
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(npckey);
		_os_.marshal(npcid);
		_os_.marshal(mapid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		npckey = _os_.unmarshal_long();
		npcid = _os_.unmarshal_int();
		mapid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceNpcInfo copy() {
		_xdb_verify_unsafe_();
		return new InstanceNpcInfo(this);
	}

	@Override
	public xbean.InstanceNpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new InstanceNpcInfo(this); // same as copy()
	}

	@Override
	public xbean.InstanceNpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getNpckey() { // 目的npc的key
		_xdb_verify_unsafe_();
		return npckey;
	}

	@Override
	public int getNpcid() { // 目的npc的id
		_xdb_verify_unsafe_();
		return npcid;
	}

	@Override
	public int getMapid() { // 目的npc的mapid
		_xdb_verify_unsafe_();
		return mapid;
	}

	@Override
	public int getPosx() { // 目的坐标
		_xdb_verify_unsafe_();
		return posx;
	}

	@Override
	public int getPosy() { // 目的坐标
		_xdb_verify_unsafe_();
		return posy;
	}

	@Override
	public void setNpckey(long _v_) { // 目的npc的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npckey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, npckey) {
					public void rollback() { npckey = _xdb_saved; }
				};}});
		npckey = _v_;
	}

	@Override
	public void setNpcid(int _v_) { // 目的npc的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, npcid) {
					public void rollback() { npcid = _xdb_saved; }
				};}});
		npcid = _v_;
	}

	@Override
	public void setMapid(int _v_) { // 目的npc的mapid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mapid) {
					public void rollback() { mapid = _xdb_saved; }
				};}});
		mapid = _v_;
	}

	@Override
	public void setPosx(int _v_) { // 目的坐标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posx) {
					public void rollback() { posx = _xdb_saved; }
				};}});
		posx = _v_;
	}

	@Override
	public void setPosy(int _v_) { // 目的坐标
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
		InstanceNpcInfo _o_ = null;
		if ( _o1_ instanceof InstanceNpcInfo ) _o_ = (InstanceNpcInfo)_o1_;
		else if ( _o1_ instanceof InstanceNpcInfo.Const ) _o_ = ((InstanceNpcInfo.Const)_o1_).nThis();
		else return false;
		if (npckey != _o_.npckey) return false;
		if (npcid != _o_.npcid) return false;
		if (mapid != _o_.mapid) return false;
		if (posx != _o_.posx) return false;
		if (posy != _o_.posy) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npckey;
		_h_ += npcid;
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
		_sb_.append(npckey);
		_sb_.append(",");
		_sb_.append(npcid);
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
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npckey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posy"));
		return lb;
	}

	private class Const implements xbean.InstanceNpcInfo {
		InstanceNpcInfo nThis() {
			return InstanceNpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceNpcInfo copy() {
			return InstanceNpcInfo.this.copy();
		}

		@Override
		public xbean.InstanceNpcInfo toData() {
			return InstanceNpcInfo.this.toData();
		}

		public xbean.InstanceNpcInfo toBean() {
			return InstanceNpcInfo.this.toBean();
		}

		@Override
		public xbean.InstanceNpcInfo toDataIf() {
			return InstanceNpcInfo.this.toDataIf();
		}

		public xbean.InstanceNpcInfo toBeanIf() {
			return InstanceNpcInfo.this.toBeanIf();
		}

		@Override
		public long getNpckey() { // 目的npc的key
			_xdb_verify_unsafe_();
			return npckey;
		}

		@Override
		public int getNpcid() { // 目的npc的id
			_xdb_verify_unsafe_();
			return npcid;
		}

		@Override
		public int getMapid() { // 目的npc的mapid
			_xdb_verify_unsafe_();
			return mapid;
		}

		@Override
		public int getPosx() { // 目的坐标
			_xdb_verify_unsafe_();
			return posx;
		}

		@Override
		public int getPosy() { // 目的坐标
			_xdb_verify_unsafe_();
			return posy;
		}

		@Override
		public void setNpckey(long _v_) { // 目的npc的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNpcid(int _v_) { // 目的npc的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMapid(int _v_) { // 目的npc的mapid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosx(int _v_) { // 目的坐标
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosy(int _v_) { // 目的坐标
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
			return InstanceNpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceNpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceNpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceNpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceNpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceNpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceNpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceNpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceNpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceNpcInfo {
		private long npckey; // 目的npc的key
		private int npcid; // 目的npc的id
		private int mapid; // 目的npc的mapid
		private int posx; // 目的坐标
		private int posy; // 目的坐标

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.InstanceNpcInfo _o1_) {
			if (_o1_ instanceof InstanceNpcInfo) assign((InstanceNpcInfo)_o1_);
			else if (_o1_ instanceof InstanceNpcInfo.Data) assign((InstanceNpcInfo.Data)_o1_);
			else if (_o1_ instanceof InstanceNpcInfo.Const) assign(((InstanceNpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceNpcInfo _o_) {
			npckey = _o_.npckey;
			npcid = _o_.npcid;
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
		}

		private void assign(InstanceNpcInfo.Data _o_) {
			npckey = _o_.npckey;
			npcid = _o_.npcid;
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(npckey);
			_os_.marshal(npcid);
			_os_.marshal(mapid);
			_os_.marshal(posx);
			_os_.marshal(posy);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			npckey = _os_.unmarshal_long();
			npcid = _os_.unmarshal_int();
			mapid = _os_.unmarshal_int();
			posx = _os_.unmarshal_int();
			posy = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceNpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceNpcInfo toData() {
			return new Data(this);
		}

		public xbean.InstanceNpcInfo toBean() {
			return new InstanceNpcInfo(this, null, null);
		}

		@Override
		public xbean.InstanceNpcInfo toDataIf() {
			return this;
		}

		public xbean.InstanceNpcInfo toBeanIf() {
			return new InstanceNpcInfo(this, null, null);
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
		public long getNpckey() { // 目的npc的key
			return npckey;
		}

		@Override
		public int getNpcid() { // 目的npc的id
			return npcid;
		}

		@Override
		public int getMapid() { // 目的npc的mapid
			return mapid;
		}

		@Override
		public int getPosx() { // 目的坐标
			return posx;
		}

		@Override
		public int getPosy() { // 目的坐标
			return posy;
		}

		@Override
		public void setNpckey(long _v_) { // 目的npc的key
			npckey = _v_;
		}

		@Override
		public void setNpcid(int _v_) { // 目的npc的id
			npcid = _v_;
		}

		@Override
		public void setMapid(int _v_) { // 目的npc的mapid
			mapid = _v_;
		}

		@Override
		public void setPosx(int _v_) { // 目的坐标
			posx = _v_;
		}

		@Override
		public void setPosy(int _v_) { // 目的坐标
			posy = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceNpcInfo.Data)) return false;
			InstanceNpcInfo.Data _o_ = (InstanceNpcInfo.Data) _o1_;
			if (npckey != _o_.npckey) return false;
			if (npcid != _o_.npcid) return false;
			if (mapid != _o_.mapid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npckey;
			_h_ += npcid;
			_h_ += mapid;
			_h_ += posx;
			_h_ += posy;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npckey);
			_sb_.append(",");
			_sb_.append(npcid);
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
