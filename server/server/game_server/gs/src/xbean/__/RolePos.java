
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RolePos extends mkdb.XBean implements xbean.RolePos {
	private int mapid; // 
	private int posx; // 
	private int posy; // 
	private long ownerid; // 这个幻境是属于哪个玩家的
	private int dynamicmap; // 
	private int dynamicposx; // 
	private int dynamicposy; // 
	private int status; // 
	private int hastask; // 玩家是否有幻境任务 0没有,1有

	@Override
	public void _reset_unsafe_() {
		mapid = 0;
		posx = 0;
		posy = 0;
		ownerid = 0L;
		dynamicmap = 0;
		dynamicposx = 0;
		dynamicposy = 0;
		status = 0;
		hastask = 0;
	}

	RolePos(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RolePos() {
		this(0, null, null);
	}

	public RolePos(RolePos _o_) {
		this(_o_, null, null);
	}

	RolePos(xbean.RolePos _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RolePos) assign((RolePos)_o1_);
		else if (_o1_ instanceof RolePos.Data) assign((RolePos.Data)_o1_);
		else if (_o1_ instanceof RolePos.Const) assign(((RolePos.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RolePos _o_) {
		_o_._xdb_verify_unsafe_();
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
		ownerid = _o_.ownerid;
		dynamicmap = _o_.dynamicmap;
		dynamicposx = _o_.dynamicposx;
		dynamicposy = _o_.dynamicposy;
		status = _o_.status;
		hastask = _o_.hastask;
	}

	private void assign(RolePos.Data _o_) {
		mapid = _o_.mapid;
		posx = _o_.posx;
		posy = _o_.posy;
		ownerid = _o_.ownerid;
		dynamicmap = _o_.dynamicmap;
		dynamicposx = _o_.dynamicposx;
		dynamicposy = _o_.dynamicposy;
		status = _o_.status;
		hastask = _o_.hastask;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(mapid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(ownerid);
		_os_.marshal(dynamicmap);
		_os_.marshal(dynamicposx);
		_os_.marshal(dynamicposy);
		_os_.marshal(status);
		_os_.marshal(hastask);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		mapid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		dynamicmap = _os_.unmarshal_int();
		dynamicposx = _os_.unmarshal_int();
		dynamicposy = _os_.unmarshal_int();
		status = _os_.unmarshal_int();
		hastask = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RolePos copy() {
		_xdb_verify_unsafe_();
		return new RolePos(this);
	}

	@Override
	public xbean.RolePos toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RolePos toBean() {
		_xdb_verify_unsafe_();
		return new RolePos(this); // same as copy()
	}

	@Override
	public xbean.RolePos toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RolePos toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
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
	public long getOwnerid() { // 这个幻境是属于哪个玩家的
		_xdb_verify_unsafe_();
		return ownerid;
	}

	@Override
	public int getDynamicmap() { // 
		_xdb_verify_unsafe_();
		return dynamicmap;
	}

	@Override
	public int getDynamicposx() { // 
		_xdb_verify_unsafe_();
		return dynamicposx;
	}

	@Override
	public int getDynamicposy() { // 
		_xdb_verify_unsafe_();
		return dynamicposy;
	}

	@Override
	public int getStatus() { // 
		_xdb_verify_unsafe_();
		return status;
	}

	@Override
	public int getHastask() { // 玩家是否有幻境任务 0没有,1有
		_xdb_verify_unsafe_();
		return hastask;
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
	public void setOwnerid(long _v_) { // 这个幻境是属于哪个玩家的
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ownerid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, ownerid) {
					public void rollback() { ownerid = _xdb_saved; }
				};}});
		ownerid = _v_;
	}

	@Override
	public void setDynamicmap(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dynamicmap") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dynamicmap) {
					public void rollback() { dynamicmap = _xdb_saved; }
				};}});
		dynamicmap = _v_;
	}

	@Override
	public void setDynamicposx(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dynamicposx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dynamicposx) {
					public void rollback() { dynamicposx = _xdb_saved; }
				};}});
		dynamicposx = _v_;
	}

	@Override
	public void setDynamicposy(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dynamicposy") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dynamicposy) {
					public void rollback() { dynamicposy = _xdb_saved; }
				};}});
		dynamicposy = _v_;
	}

	@Override
	public void setStatus(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "status") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, status) {
					public void rollback() { status = _xdb_saved; }
				};}});
		status = _v_;
	}

	@Override
	public void setHastask(int _v_) { // 玩家是否有幻境任务 0没有,1有
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hastask") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hastask) {
					public void rollback() { hastask = _xdb_saved; }
				};}});
		hastask = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RolePos _o_ = null;
		if ( _o1_ instanceof RolePos ) _o_ = (RolePos)_o1_;
		else if ( _o1_ instanceof RolePos.Const ) _o_ = ((RolePos.Const)_o1_).nThis();
		else return false;
		if (mapid != _o_.mapid) return false;
		if (posx != _o_.posx) return false;
		if (posy != _o_.posy) return false;
		if (ownerid != _o_.ownerid) return false;
		if (dynamicmap != _o_.dynamicmap) return false;
		if (dynamicposx != _o_.dynamicposx) return false;
		if (dynamicposy != _o_.dynamicposy) return false;
		if (status != _o_.status) return false;
		if (hastask != _o_.hastask) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += mapid;
		_h_ += posx;
		_h_ += posy;
		_h_ += ownerid;
		_h_ += dynamicmap;
		_h_ += dynamicposx;
		_h_ += dynamicposy;
		_h_ += status;
		_h_ += hastask;
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
		_sb_.append(",");
		_sb_.append(ownerid);
		_sb_.append(",");
		_sb_.append(dynamicmap);
		_sb_.append(",");
		_sb_.append(dynamicposx);
		_sb_.append(",");
		_sb_.append(dynamicposy);
		_sb_.append(",");
		_sb_.append(status);
		_sb_.append(",");
		_sb_.append(hastask);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posy"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ownerid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dynamicmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dynamicposx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dynamicposy"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("status"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hastask"));
		return lb;
	}

	private class Const implements xbean.RolePos {
		RolePos nThis() {
			return RolePos.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RolePos copy() {
			return RolePos.this.copy();
		}

		@Override
		public xbean.RolePos toData() {
			return RolePos.this.toData();
		}

		public xbean.RolePos toBean() {
			return RolePos.this.toBean();
		}

		@Override
		public xbean.RolePos toDataIf() {
			return RolePos.this.toDataIf();
		}

		public xbean.RolePos toBeanIf() {
			return RolePos.this.toBeanIf();
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
		public long getOwnerid() { // 这个幻境是属于哪个玩家的
			_xdb_verify_unsafe_();
			return ownerid;
		}

		@Override
		public int getDynamicmap() { // 
			_xdb_verify_unsafe_();
			return dynamicmap;
		}

		@Override
		public int getDynamicposx() { // 
			_xdb_verify_unsafe_();
			return dynamicposx;
		}

		@Override
		public int getDynamicposy() { // 
			_xdb_verify_unsafe_();
			return dynamicposy;
		}

		@Override
		public int getStatus() { // 
			_xdb_verify_unsafe_();
			return status;
		}

		@Override
		public int getHastask() { // 玩家是否有幻境任务 0没有,1有
			_xdb_verify_unsafe_();
			return hastask;
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
		public void setOwnerid(long _v_) { // 这个幻境是属于哪个玩家的
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDynamicmap(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDynamicposx(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDynamicposy(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStatus(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHastask(int _v_) { // 玩家是否有幻境任务 0没有,1有
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
			return RolePos.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RolePos.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RolePos.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RolePos.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RolePos.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RolePos.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RolePos.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RolePos.this.hashCode();
		}

		@Override
		public String toString() {
			return RolePos.this.toString();
		}

	}

	public static final class Data implements xbean.RolePos {
		private int mapid; // 
		private int posx; // 
		private int posy; // 
		private long ownerid; // 这个幻境是属于哪个玩家的
		private int dynamicmap; // 
		private int dynamicposx; // 
		private int dynamicposy; // 
		private int status; // 
		private int hastask; // 玩家是否有幻境任务 0没有,1有

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RolePos _o1_) {
			if (_o1_ instanceof RolePos) assign((RolePos)_o1_);
			else if (_o1_ instanceof RolePos.Data) assign((RolePos.Data)_o1_);
			else if (_o1_ instanceof RolePos.Const) assign(((RolePos.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RolePos _o_) {
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
			ownerid = _o_.ownerid;
			dynamicmap = _o_.dynamicmap;
			dynamicposx = _o_.dynamicposx;
			dynamicposy = _o_.dynamicposy;
			status = _o_.status;
			hastask = _o_.hastask;
		}

		private void assign(RolePos.Data _o_) {
			mapid = _o_.mapid;
			posx = _o_.posx;
			posy = _o_.posy;
			ownerid = _o_.ownerid;
			dynamicmap = _o_.dynamicmap;
			dynamicposx = _o_.dynamicposx;
			dynamicposy = _o_.dynamicposy;
			status = _o_.status;
			hastask = _o_.hastask;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(mapid);
			_os_.marshal(posx);
			_os_.marshal(posy);
			_os_.marshal(ownerid);
			_os_.marshal(dynamicmap);
			_os_.marshal(dynamicposx);
			_os_.marshal(dynamicposy);
			_os_.marshal(status);
			_os_.marshal(hastask);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			mapid = _os_.unmarshal_int();
			posx = _os_.unmarshal_int();
			posy = _os_.unmarshal_int();
			ownerid = _os_.unmarshal_long();
			dynamicmap = _os_.unmarshal_int();
			dynamicposx = _os_.unmarshal_int();
			dynamicposy = _os_.unmarshal_int();
			status = _os_.unmarshal_int();
			hastask = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RolePos copy() {
			return new Data(this);
		}

		@Override
		public xbean.RolePos toData() {
			return new Data(this);
		}

		public xbean.RolePos toBean() {
			return new RolePos(this, null, null);
		}

		@Override
		public xbean.RolePos toDataIf() {
			return this;
		}

		public xbean.RolePos toBeanIf() {
			return new RolePos(this, null, null);
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
		public long getOwnerid() { // 这个幻境是属于哪个玩家的
			return ownerid;
		}

		@Override
		public int getDynamicmap() { // 
			return dynamicmap;
		}

		@Override
		public int getDynamicposx() { // 
			return dynamicposx;
		}

		@Override
		public int getDynamicposy() { // 
			return dynamicposy;
		}

		@Override
		public int getStatus() { // 
			return status;
		}

		@Override
		public int getHastask() { // 玩家是否有幻境任务 0没有,1有
			return hastask;
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
		public void setOwnerid(long _v_) { // 这个幻境是属于哪个玩家的
			ownerid = _v_;
		}

		@Override
		public void setDynamicmap(int _v_) { // 
			dynamicmap = _v_;
		}

		@Override
		public void setDynamicposx(int _v_) { // 
			dynamicposx = _v_;
		}

		@Override
		public void setDynamicposy(int _v_) { // 
			dynamicposy = _v_;
		}

		@Override
		public void setStatus(int _v_) { // 
			status = _v_;
		}

		@Override
		public void setHastask(int _v_) { // 玩家是否有幻境任务 0没有,1有
			hastask = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RolePos.Data)) return false;
			RolePos.Data _o_ = (RolePos.Data) _o1_;
			if (mapid != _o_.mapid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (ownerid != _o_.ownerid) return false;
			if (dynamicmap != _o_.dynamicmap) return false;
			if (dynamicposx != _o_.dynamicposx) return false;
			if (dynamicposy != _o_.dynamicposy) return false;
			if (status != _o_.status) return false;
			if (hastask != _o_.hastask) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += mapid;
			_h_ += posx;
			_h_ += posy;
			_h_ += ownerid;
			_h_ += dynamicmap;
			_h_ += dynamicposx;
			_h_ += dynamicposy;
			_h_ += status;
			_h_ += hastask;
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
			_sb_.append(",");
			_sb_.append(ownerid);
			_sb_.append(",");
			_sb_.append(dynamicmap);
			_sb_.append(",");
			_sb_.append(dynamicposx);
			_sb_.append(",");
			_sb_.append(dynamicposy);
			_sb_.append(",");
			_sb_.append(status);
			_sb_.append(",");
			_sb_.append(hastask);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
