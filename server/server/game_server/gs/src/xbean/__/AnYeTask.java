
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AnYeTask extends mkdb.XBean implements xbean.AnYeTask {
	private int id; // 任务id
	private int kind; // 任务类型
	private int state; // 任务状态
	private int dstitemid; // 目的道具的id
	private int dstitemnum; // 目的道具的数量
	private long dstnpckey; // 目的npc的key
	private int dstnpcid; // 目的npc的id
	private long param1; // 保留参数1
	private long param2; // 保留参数2
	private long param3; // 保留参数3
	private int legend; // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
	private int legendtime; // 传说持续时间
	private long legendend; // 传说结束时间
	private int legenditem; // 传说物品

	@Override
	public void _reset_unsafe_() {
		id = 0;
		kind = 0;
		state = 0;
		dstitemid = 0;
		dstitemnum = 0;
		dstnpckey = 0L;
		dstnpcid = 0;
		param1 = 0L;
		param2 = 0L;
		param3 = 0L;
		legend = 0;
		legendtime = 0;
		legendend = 0L;
		legenditem = 0;
	}

	AnYeTask(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		legend = 0;
		legendtime = 0;
	}

	public AnYeTask() {
		this(0, null, null);
	}

	public AnYeTask(AnYeTask _o_) {
		this(_o_, null, null);
	}

	AnYeTask(xbean.AnYeTask _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AnYeTask) assign((AnYeTask)_o1_);
		else if (_o1_ instanceof AnYeTask.Data) assign((AnYeTask.Data)_o1_);
		else if (_o1_ instanceof AnYeTask.Const) assign(((AnYeTask.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AnYeTask _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		kind = _o_.kind;
		state = _o_.state;
		dstitemid = _o_.dstitemid;
		dstitemnum = _o_.dstitemnum;
		dstnpckey = _o_.dstnpckey;
		dstnpcid = _o_.dstnpcid;
		param1 = _o_.param1;
		param2 = _o_.param2;
		param3 = _o_.param3;
		legend = _o_.legend;
		legendtime = _o_.legendtime;
		legendend = _o_.legendend;
		legenditem = _o_.legenditem;
	}

	private void assign(AnYeTask.Data _o_) {
		id = _o_.id;
		kind = _o_.kind;
		state = _o_.state;
		dstitemid = _o_.dstitemid;
		dstitemnum = _o_.dstitemnum;
		dstnpckey = _o_.dstnpckey;
		dstnpcid = _o_.dstnpcid;
		param1 = _o_.param1;
		param2 = _o_.param2;
		param3 = _o_.param3;
		legend = _o_.legend;
		legendtime = _o_.legendtime;
		legendend = _o_.legendend;
		legenditem = _o_.legenditem;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(kind);
		_os_.marshal(state);
		_os_.marshal(dstitemid);
		_os_.marshal(dstitemnum);
		_os_.marshal(dstnpckey);
		_os_.marshal(dstnpcid);
		_os_.marshal(param1);
		_os_.marshal(param2);
		_os_.marshal(param3);
		_os_.marshal(legend);
		_os_.marshal(legendtime);
		_os_.marshal(legendend);
		_os_.marshal(legenditem);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		kind = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		dstitemid = _os_.unmarshal_int();
		dstitemnum = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		dstnpcid = _os_.unmarshal_int();
		param1 = _os_.unmarshal_long();
		param2 = _os_.unmarshal_long();
		param3 = _os_.unmarshal_long();
		legend = _os_.unmarshal_int();
		legendtime = _os_.unmarshal_int();
		legendend = _os_.unmarshal_long();
		legenditem = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.AnYeTask copy() {
		_xdb_verify_unsafe_();
		return new AnYeTask(this);
	}

	@Override
	public xbean.AnYeTask toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AnYeTask toBean() {
		_xdb_verify_unsafe_();
		return new AnYeTask(this); // same as copy()
	}

	@Override
	public xbean.AnYeTask toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AnYeTask toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 任务id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getKind() { // 任务类型
		_xdb_verify_unsafe_();
		return kind;
	}

	@Override
	public int getState() { // 任务状态
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public int getDstitemid() { // 目的道具的id
		_xdb_verify_unsafe_();
		return dstitemid;
	}

	@Override
	public int getDstitemnum() { // 目的道具的数量
		_xdb_verify_unsafe_();
		return dstitemnum;
	}

	@Override
	public long getDstnpckey() { // 目的npc的key
		_xdb_verify_unsafe_();
		return dstnpckey;
	}

	@Override
	public int getDstnpcid() { // 目的npc的id
		_xdb_verify_unsafe_();
		return dstnpcid;
	}

	@Override
	public long getParam1() { // 保留参数1
		_xdb_verify_unsafe_();
		return param1;
	}

	@Override
	public long getParam2() { // 保留参数2
		_xdb_verify_unsafe_();
		return param2;
	}

	@Override
	public long getParam3() { // 保留参数3
		_xdb_verify_unsafe_();
		return param3;
	}

	@Override
	public int getLegend() { // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
		_xdb_verify_unsafe_();
		return legend;
	}

	@Override
	public int getLegendtime() { // 传说持续时间
		_xdb_verify_unsafe_();
		return legendtime;
	}

	@Override
	public long getLegendend() { // 传说结束时间
		_xdb_verify_unsafe_();
		return legendend;
	}

	@Override
	public int getLegenditem() { // 传说物品
		_xdb_verify_unsafe_();
		return legenditem;
	}

	@Override
	public void setId(int _v_) { // 任务id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setKind(int _v_) { // 任务类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "kind") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, kind) {
					public void rollback() { kind = _xdb_saved; }
				};}});
		kind = _v_;
	}

	@Override
	public void setState(int _v_) { // 任务状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setDstitemid(int _v_) { // 目的道具的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstitemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstitemid) {
					public void rollback() { dstitemid = _xdb_saved; }
				};}});
		dstitemid = _v_;
	}

	@Override
	public void setDstitemnum(int _v_) { // 目的道具的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstitemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstitemnum) {
					public void rollback() { dstitemnum = _xdb_saved; }
				};}});
		dstitemnum = _v_;
	}

	@Override
	public void setDstnpckey(long _v_) { // 目的npc的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstnpckey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, dstnpckey) {
					public void rollback() { dstnpckey = _xdb_saved; }
				};}});
		dstnpckey = _v_;
	}

	@Override
	public void setDstnpcid(int _v_) { // 目的npc的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstnpcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstnpcid) {
					public void rollback() { dstnpcid = _xdb_saved; }
				};}});
		dstnpcid = _v_;
	}

	@Override
	public void setParam1(long _v_) { // 保留参数1
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "param1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, param1) {
					public void rollback() { param1 = _xdb_saved; }
				};}});
		param1 = _v_;
	}

	@Override
	public void setParam2(long _v_) { // 保留参数2
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "param2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, param2) {
					public void rollback() { param2 = _xdb_saved; }
				};}});
		param2 = _v_;
	}

	@Override
	public void setParam3(long _v_) { // 保留参数3
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "param3") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, param3) {
					public void rollback() { param3 = _xdb_saved; }
				};}});
		param3 = _v_;
	}

	@Override
	public void setLegend(int _v_) { // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "legend") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, legend) {
					public void rollback() { legend = _xdb_saved; }
				};}});
		legend = _v_;
	}

	@Override
	public void setLegendtime(int _v_) { // 传说持续时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "legendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, legendtime) {
					public void rollback() { legendtime = _xdb_saved; }
				};}});
		legendtime = _v_;
	}

	@Override
	public void setLegendend(long _v_) { // 传说结束时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "legendend") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, legendend) {
					public void rollback() { legendend = _xdb_saved; }
				};}});
		legendend = _v_;
	}

	@Override
	public void setLegenditem(int _v_) { // 传说物品
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "legenditem") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, legenditem) {
					public void rollback() { legenditem = _xdb_saved; }
				};}});
		legenditem = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AnYeTask _o_ = null;
		if ( _o1_ instanceof AnYeTask ) _o_ = (AnYeTask)_o1_;
		else if ( _o1_ instanceof AnYeTask.Const ) _o_ = ((AnYeTask.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (kind != _o_.kind) return false;
		if (state != _o_.state) return false;
		if (dstitemid != _o_.dstitemid) return false;
		if (dstitemnum != _o_.dstitemnum) return false;
		if (dstnpckey != _o_.dstnpckey) return false;
		if (dstnpcid != _o_.dstnpcid) return false;
		if (param1 != _o_.param1) return false;
		if (param2 != _o_.param2) return false;
		if (param3 != _o_.param3) return false;
		if (legend != _o_.legend) return false;
		if (legendtime != _o_.legendtime) return false;
		if (legendend != _o_.legendend) return false;
		if (legenditem != _o_.legenditem) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += kind;
		_h_ += state;
		_h_ += dstitemid;
		_h_ += dstitemnum;
		_h_ += dstnpckey;
		_h_ += dstnpcid;
		_h_ += param1;
		_h_ += param2;
		_h_ += param3;
		_h_ += legend;
		_h_ += legendtime;
		_h_ += legendend;
		_h_ += legenditem;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(kind);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(dstitemid);
		_sb_.append(",");
		_sb_.append(dstitemnum);
		_sb_.append(",");
		_sb_.append(dstnpckey);
		_sb_.append(",");
		_sb_.append(dstnpcid);
		_sb_.append(",");
		_sb_.append(param1);
		_sb_.append(",");
		_sb_.append(param2);
		_sb_.append(",");
		_sb_.append(param3);
		_sb_.append(",");
		_sb_.append(legend);
		_sb_.append(",");
		_sb_.append(legendtime);
		_sb_.append(",");
		_sb_.append(legendend);
		_sb_.append(",");
		_sb_.append(legenditem);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("kind"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstitemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstitemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstnpckey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstnpcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("param1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("param2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("param3"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("legend"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("legendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("legendend"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("legenditem"));
		return lb;
	}

	private class Const implements xbean.AnYeTask {
		AnYeTask nThis() {
			return AnYeTask.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AnYeTask copy() {
			return AnYeTask.this.copy();
		}

		@Override
		public xbean.AnYeTask toData() {
			return AnYeTask.this.toData();
		}

		public xbean.AnYeTask toBean() {
			return AnYeTask.this.toBean();
		}

		@Override
		public xbean.AnYeTask toDataIf() {
			return AnYeTask.this.toDataIf();
		}

		public xbean.AnYeTask toBeanIf() {
			return AnYeTask.this.toBeanIf();
		}

		@Override
		public int getId() { // 任务id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getKind() { // 任务类型
			_xdb_verify_unsafe_();
			return kind;
		}

		@Override
		public int getState() { // 任务状态
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public int getDstitemid() { // 目的道具的id
			_xdb_verify_unsafe_();
			return dstitemid;
		}

		@Override
		public int getDstitemnum() { // 目的道具的数量
			_xdb_verify_unsafe_();
			return dstitemnum;
		}

		@Override
		public long getDstnpckey() { // 目的npc的key
			_xdb_verify_unsafe_();
			return dstnpckey;
		}

		@Override
		public int getDstnpcid() { // 目的npc的id
			_xdb_verify_unsafe_();
			return dstnpcid;
		}

		@Override
		public long getParam1() { // 保留参数1
			_xdb_verify_unsafe_();
			return param1;
		}

		@Override
		public long getParam2() { // 保留参数2
			_xdb_verify_unsafe_();
			return param2;
		}

		@Override
		public long getParam3() { // 保留参数3
			_xdb_verify_unsafe_();
			return param3;
		}

		@Override
		public int getLegend() { // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
			_xdb_verify_unsafe_();
			return legend;
		}

		@Override
		public int getLegendtime() { // 传说持续时间
			_xdb_verify_unsafe_();
			return legendtime;
		}

		@Override
		public long getLegendend() { // 传说结束时间
			_xdb_verify_unsafe_();
			return legendend;
		}

		@Override
		public int getLegenditem() { // 传说物品
			_xdb_verify_unsafe_();
			return legenditem;
		}

		@Override
		public void setId(int _v_) { // 任务id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setKind(int _v_) { // 任务类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 任务状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstitemid(int _v_) { // 目的道具的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstitemnum(int _v_) { // 目的道具的数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstnpckey(long _v_) { // 目的npc的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstnpcid(int _v_) { // 目的npc的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setParam1(long _v_) { // 保留参数1
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setParam2(long _v_) { // 保留参数2
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setParam3(long _v_) { // 保留参数3
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLegend(int _v_) { // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLegendtime(int _v_) { // 传说持续时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLegendend(long _v_) { // 传说结束时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLegenditem(int _v_) { // 传说物品
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
			return AnYeTask.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AnYeTask.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AnYeTask.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AnYeTask.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AnYeTask.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AnYeTask.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AnYeTask.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AnYeTask.this.hashCode();
		}

		@Override
		public String toString() {
			return AnYeTask.this.toString();
		}

	}

	public static final class Data implements xbean.AnYeTask {
		private int id; // 任务id
		private int kind; // 任务类型
		private int state; // 任务状态
		private int dstitemid; // 目的道具的id
		private int dstitemnum; // 目的道具的数量
		private long dstnpckey; // 目的npc的key
		private int dstnpcid; // 目的npc的id
		private long param1; // 保留参数1
		private long param2; // 保留参数2
		private long param3; // 保留参数3
		private int legend; // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
		private int legendtime; // 传说持续时间
		private long legendend; // 传说结束时间
		private int legenditem; // 传说物品

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			legend = 0;
			legendtime = 0;
		}

		Data(xbean.AnYeTask _o1_) {
			if (_o1_ instanceof AnYeTask) assign((AnYeTask)_o1_);
			else if (_o1_ instanceof AnYeTask.Data) assign((AnYeTask.Data)_o1_);
			else if (_o1_ instanceof AnYeTask.Const) assign(((AnYeTask.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AnYeTask _o_) {
			id = _o_.id;
			kind = _o_.kind;
			state = _o_.state;
			dstitemid = _o_.dstitemid;
			dstitemnum = _o_.dstitemnum;
			dstnpckey = _o_.dstnpckey;
			dstnpcid = _o_.dstnpcid;
			param1 = _o_.param1;
			param2 = _o_.param2;
			param3 = _o_.param3;
			legend = _o_.legend;
			legendtime = _o_.legendtime;
			legendend = _o_.legendend;
			legenditem = _o_.legenditem;
		}

		private void assign(AnYeTask.Data _o_) {
			id = _o_.id;
			kind = _o_.kind;
			state = _o_.state;
			dstitemid = _o_.dstitemid;
			dstitemnum = _o_.dstitemnum;
			dstnpckey = _o_.dstnpckey;
			dstnpcid = _o_.dstnpcid;
			param1 = _o_.param1;
			param2 = _o_.param2;
			param3 = _o_.param3;
			legend = _o_.legend;
			legendtime = _o_.legendtime;
			legendend = _o_.legendend;
			legenditem = _o_.legenditem;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(kind);
			_os_.marshal(state);
			_os_.marshal(dstitemid);
			_os_.marshal(dstitemnum);
			_os_.marshal(dstnpckey);
			_os_.marshal(dstnpcid);
			_os_.marshal(param1);
			_os_.marshal(param2);
			_os_.marshal(param3);
			_os_.marshal(legend);
			_os_.marshal(legendtime);
			_os_.marshal(legendend);
			_os_.marshal(legenditem);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			kind = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			dstitemid = _os_.unmarshal_int();
			dstitemnum = _os_.unmarshal_int();
			dstnpckey = _os_.unmarshal_long();
			dstnpcid = _os_.unmarshal_int();
			param1 = _os_.unmarshal_long();
			param2 = _os_.unmarshal_long();
			param3 = _os_.unmarshal_long();
			legend = _os_.unmarshal_int();
			legendtime = _os_.unmarshal_int();
			legendend = _os_.unmarshal_long();
			legenditem = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.AnYeTask copy() {
			return new Data(this);
		}

		@Override
		public xbean.AnYeTask toData() {
			return new Data(this);
		}

		public xbean.AnYeTask toBean() {
			return new AnYeTask(this, null, null);
		}

		@Override
		public xbean.AnYeTask toDataIf() {
			return this;
		}

		public xbean.AnYeTask toBeanIf() {
			return new AnYeTask(this, null, null);
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
		public int getId() { // 任务id
			return id;
		}

		@Override
		public int getKind() { // 任务类型
			return kind;
		}

		@Override
		public int getState() { // 任务状态
			return state;
		}

		@Override
		public int getDstitemid() { // 目的道具的id
			return dstitemid;
		}

		@Override
		public int getDstitemnum() { // 目的道具的数量
			return dstitemnum;
		}

		@Override
		public long getDstnpckey() { // 目的npc的key
			return dstnpckey;
		}

		@Override
		public int getDstnpcid() { // 目的npc的id
			return dstnpcid;
		}

		@Override
		public long getParam1() { // 保留参数1
			return param1;
		}

		@Override
		public long getParam2() { // 保留参数2
			return param2;
		}

		@Override
		public long getParam3() { // 保留参数3
			return param3;
		}

		@Override
		public int getLegend() { // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
			return legend;
		}

		@Override
		public int getLegendtime() { // 传说持续时间
			return legendtime;
		}

		@Override
		public long getLegendend() { // 传说结束时间
			return legendend;
		}

		@Override
		public int getLegenditem() { // 传说物品
			return legenditem;
		}

		@Override
		public void setId(int _v_) { // 任务id
			id = _v_;
		}

		@Override
		public void setKind(int _v_) { // 任务类型
			kind = _v_;
		}

		@Override
		public void setState(int _v_) { // 任务状态
			state = _v_;
		}

		@Override
		public void setDstitemid(int _v_) { // 目的道具的id
			dstitemid = _v_;
		}

		@Override
		public void setDstitemnum(int _v_) { // 目的道具的数量
			dstitemnum = _v_;
		}

		@Override
		public void setDstnpckey(long _v_) { // 目的npc的key
			dstnpckey = _v_;
		}

		@Override
		public void setDstnpcid(int _v_) { // 目的npc的id
			dstnpcid = _v_;
		}

		@Override
		public void setParam1(long _v_) { // 保留参数1
			param1 = _v_;
		}

		@Override
		public void setParam2(long _v_) { // 保留参数2
			param2 = _v_;
		}

		@Override
		public void setParam3(long _v_) { // 保留参数3
			param3 = _v_;
		}

		@Override
		public void setLegend(int _v_) { // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
			legend = _v_;
		}

		@Override
		public void setLegendtime(int _v_) { // 传说持续时间
			legendtime = _v_;
		}

		@Override
		public void setLegendend(long _v_) { // 传说结束时间
			legendend = _v_;
		}

		@Override
		public void setLegenditem(int _v_) { // 传说物品
			legenditem = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AnYeTask.Data)) return false;
			AnYeTask.Data _o_ = (AnYeTask.Data) _o1_;
			if (id != _o_.id) return false;
			if (kind != _o_.kind) return false;
			if (state != _o_.state) return false;
			if (dstitemid != _o_.dstitemid) return false;
			if (dstitemnum != _o_.dstitemnum) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (param1 != _o_.param1) return false;
			if (param2 != _o_.param2) return false;
			if (param3 != _o_.param3) return false;
			if (legend != _o_.legend) return false;
			if (legendtime != _o_.legendtime) return false;
			if (legendend != _o_.legendend) return false;
			if (legenditem != _o_.legenditem) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += kind;
			_h_ += state;
			_h_ += dstitemid;
			_h_ += dstitemnum;
			_h_ += dstnpckey;
			_h_ += dstnpcid;
			_h_ += param1;
			_h_ += param2;
			_h_ += param3;
			_h_ += legend;
			_h_ += legendtime;
			_h_ += legendend;
			_h_ += legenditem;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(kind);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(dstitemid);
			_sb_.append(",");
			_sb_.append(dstitemnum);
			_sb_.append(",");
			_sb_.append(dstnpckey);
			_sb_.append(",");
			_sb_.append(dstnpcid);
			_sb_.append(",");
			_sb_.append(param1);
			_sb_.append(",");
			_sb_.append(param2);
			_sb_.append(",");
			_sb_.append(param3);
			_sb_.append(",");
			_sb_.append(legend);
			_sb_.append(",");
			_sb_.append(legendtime);
			_sb_.append(",");
			_sb_.append(legendend);
			_sb_.append(",");
			_sb_.append(legenditem);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
