
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Item extends mkdb.XBean implements xbean.Item {
	private int id; // 物品编号
	private int flags; // 标志，叠加的时候，flags 也 OR 叠加
	private int position; // 背包属性，位置。从0开始编号
	private int number; // 数量
	private java.util.HashMap<Integer, Integer> numbermap; // 数量
	private long timeout; // 到期时间。如果为0，代表没有时间限制
	private long extid; // 扩展动态属性
	private long uniqueid; // 物品的唯一id
	private long loseeffecttime; // 物品失效时间
	private long markettime; // 摆摊道具冻结时间
	private int typeid; // 物品类型

	@Override
	public void _reset_unsafe_() {
		id = 0;
		flags = 0;
		position = -1;
		number = 0;
		numbermap.clear();
		timeout = 0;
		extid = 0L;
		uniqueid = 0L;
		loseeffecttime = 0L;
		markettime = 0L;
		typeid = -1;
	}

	Item(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		position = -1;
		numbermap = new java.util.HashMap<Integer, Integer>();
		timeout = 0;
		typeid = -1;
	}

	public Item() {
		this(0, null, null);
	}

	public Item(Item _o_) {
		this(_o_, null, null);
	}

	Item(xbean.Item _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Item) assign((Item)_o1_);
		else if (_o1_ instanceof Item.Data) assign((Item.Data)_o1_);
		else if (_o1_ instanceof Item.Const) assign(((Item.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Item _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		flags = _o_.flags;
		position = _o_.position;
		number = _o_.number;
		numbermap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.numbermap.entrySet())
			numbermap.put(_e_.getKey(), _e_.getValue());
		timeout = _o_.timeout;
		extid = _o_.extid;
		uniqueid = _o_.uniqueid;
		loseeffecttime = _o_.loseeffecttime;
		markettime = _o_.markettime;
		typeid = _o_.typeid;
	}

	private void assign(Item.Data _o_) {
		id = _o_.id;
		flags = _o_.flags;
		position = _o_.position;
		number = _o_.number;
		numbermap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.numbermap.entrySet())
			numbermap.put(_e_.getKey(), _e_.getValue());
		timeout = _o_.timeout;
		extid = _o_.extid;
		uniqueid = _o_.uniqueid;
		loseeffecttime = _o_.loseeffecttime;
		markettime = _o_.markettime;
		typeid = _o_.typeid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(flags);
		_os_.marshal(position);
		_os_.marshal(number);
		_os_.compact_uint32(numbermap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : numbermap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(timeout);
		_os_.marshal(extid);
		_os_.marshal(uniqueid);
		_os_.marshal(loseeffecttime);
		_os_.marshal(markettime);
		_os_.marshal(typeid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		flags = _os_.unmarshal_int();
		position = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				numbermap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				numbermap.put(_k_, _v_);
			}
		}
		timeout = _os_.unmarshal_long();
		extid = _os_.unmarshal_long();
		uniqueid = _os_.unmarshal_long();
		loseeffecttime = _os_.unmarshal_long();
		markettime = _os_.unmarshal_long();
		typeid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.Item copy() {
		_xdb_verify_unsafe_();
		return new Item(this);
	}

	@Override
	public xbean.Item toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Item toBean() {
		_xdb_verify_unsafe_();
		return new Item(this); // same as copy()
	}

	@Override
	public xbean.Item toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Item toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 物品编号
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getFlags() { // 标志，叠加的时候，flags 也 OR 叠加
		_xdb_verify_unsafe_();
		return flags;
	}

	@Override
	public int getPosition() { // 背包属性，位置。从0开始编号
		_xdb_verify_unsafe_();
		return position;
	}

	@Override
	public int getNumber() { // 数量
		_xdb_verify_unsafe_();
		return number;
	}

	@Override
	public java.util.Map<Integer, Integer> getNumbermap() { // 数量
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "numbermap"), numbermap);
	}

	@Override
	public java.util.Map<Integer, Integer> getNumbermapAsData() { // 数量
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> numbermap;
		Item _o_ = this;
		numbermap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.numbermap.entrySet())
			numbermap.put(_e_.getKey(), _e_.getValue());
		return numbermap;
	}

	@Override
	public long getTimeout() { // 到期时间。如果为0，代表没有时间限制
		_xdb_verify_unsafe_();
		return timeout;
	}

	@Override
	public long getExtid() { // 扩展动态属性
		_xdb_verify_unsafe_();
		return extid;
	}

	@Override
	public long getUniqueid() { // 物品的唯一id
		_xdb_verify_unsafe_();
		return uniqueid;
	}

	@Override
	public long getLoseeffecttime() { // 物品失效时间
		_xdb_verify_unsafe_();
		return loseeffecttime;
	}

	@Override
	public long getMarkettime() { // 摆摊道具冻结时间
		_xdb_verify_unsafe_();
		return markettime;
	}

	@Override
	public int getTypeid() { // 物品类型
		_xdb_verify_unsafe_();
		return typeid;
	}

	@Override
	public void setId(int _v_) { // 物品编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setFlags(int _v_) { // 标志，叠加的时候，flags 也 OR 叠加
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flags") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flags) {
					public void rollback() { flags = _xdb_saved; }
				};}});
		flags = _v_;
	}

	@Override
	public void setPosition(int _v_) { // 背包属性，位置。从0开始编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "position") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, position) {
					public void rollback() { position = _xdb_saved; }
				};}});
		position = _v_;
	}

	@Override
	public void setNumber(int _v_) { // 数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "number") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, number) {
					public void rollback() { number = _xdb_saved; }
				};}});
		number = _v_;
	}

	@Override
	public void setTimeout(long _v_) { // 到期时间。如果为0，代表没有时间限制
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timeout") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, timeout) {
					public void rollback() { timeout = _xdb_saved; }
				};}});
		timeout = _v_;
	}

	@Override
	public void setExtid(long _v_) { // 扩展动态属性
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "extid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, extid) {
					public void rollback() { extid = _xdb_saved; }
				};}});
		extid = _v_;
	}

	@Override
	public void setUniqueid(long _v_) { // 物品的唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uniqueid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, uniqueid) {
					public void rollback() { uniqueid = _xdb_saved; }
				};}});
		uniqueid = _v_;
	}

	@Override
	public void setLoseeffecttime(long _v_) { // 物品失效时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "loseeffecttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, loseeffecttime) {
					public void rollback() { loseeffecttime = _xdb_saved; }
				};}});
		loseeffecttime = _v_;
	}

	@Override
	public void setMarkettime(long _v_) { // 摆摊道具冻结时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "markettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, markettime) {
					public void rollback() { markettime = _xdb_saved; }
				};}});
		markettime = _v_;
	}

	@Override
	public void setTypeid(int _v_) { // 物品类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "typeid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, typeid) {
					public void rollback() { typeid = _xdb_saved; }
				};}});
		typeid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Item _o_ = null;
		if ( _o1_ instanceof Item ) _o_ = (Item)_o1_;
		else if ( _o1_ instanceof Item.Const ) _o_ = ((Item.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (flags != _o_.flags) return false;
		if (position != _o_.position) return false;
		if (number != _o_.number) return false;
		if (!numbermap.equals(_o_.numbermap)) return false;
		if (timeout != _o_.timeout) return false;
		if (extid != _o_.extid) return false;
		if (uniqueid != _o_.uniqueid) return false;
		if (loseeffecttime != _o_.loseeffecttime) return false;
		if (markettime != _o_.markettime) return false;
		if (typeid != _o_.typeid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += flags;
		_h_ += position;
		_h_ += number;
		_h_ += numbermap.hashCode();
		_h_ += timeout;
		_h_ += extid;
		_h_ += uniqueid;
		_h_ += loseeffecttime;
		_h_ += markettime;
		_h_ += typeid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(flags);
		_sb_.append(",");
		_sb_.append(position);
		_sb_.append(",");
		_sb_.append(number);
		_sb_.append(",");
		_sb_.append(numbermap);
		_sb_.append(",");
		_sb_.append(timeout);
		_sb_.append(",");
		_sb_.append(extid);
		_sb_.append(",");
		_sb_.append(uniqueid);
		_sb_.append(",");
		_sb_.append(loseeffecttime);
		_sb_.append(",");
		_sb_.append(markettime);
		_sb_.append(",");
		_sb_.append(typeid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flags"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("position"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("number"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("numbermap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timeout"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("extid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniqueid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("loseeffecttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("markettime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("typeid"));
		return lb;
	}

	private class Const implements xbean.Item {
		Item nThis() {
			return Item.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Item copy() {
			return Item.this.copy();
		}

		@Override
		public xbean.Item toData() {
			return Item.this.toData();
		}

		public xbean.Item toBean() {
			return Item.this.toBean();
		}

		@Override
		public xbean.Item toDataIf() {
			return Item.this.toDataIf();
		}

		public xbean.Item toBeanIf() {
			return Item.this.toBeanIf();
		}

		@Override
		public int getId() { // 物品编号
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getFlags() { // 标志，叠加的时候，flags 也 OR 叠加
			_xdb_verify_unsafe_();
			return flags;
		}

		@Override
		public int getPosition() { // 背包属性，位置。从0开始编号
			_xdb_verify_unsafe_();
			return position;
		}

		@Override
		public int getNumber() { // 数量
			_xdb_verify_unsafe_();
			return number;
		}

		@Override
		public java.util.Map<Integer, Integer> getNumbermap() { // 数量
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(numbermap);
		}

		@Override
		public java.util.Map<Integer, Integer> getNumbermapAsData() { // 数量
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> numbermap;
			Item _o_ = Item.this;
			numbermap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.numbermap.entrySet())
				numbermap.put(_e_.getKey(), _e_.getValue());
			return numbermap;
		}

		@Override
		public long getTimeout() { // 到期时间。如果为0，代表没有时间限制
			_xdb_verify_unsafe_();
			return timeout;
		}

		@Override
		public long getExtid() { // 扩展动态属性
			_xdb_verify_unsafe_();
			return extid;
		}

		@Override
		public long getUniqueid() { // 物品的唯一id
			_xdb_verify_unsafe_();
			return uniqueid;
		}

		@Override
		public long getLoseeffecttime() { // 物品失效时间
			_xdb_verify_unsafe_();
			return loseeffecttime;
		}

		@Override
		public long getMarkettime() { // 摆摊道具冻结时间
			_xdb_verify_unsafe_();
			return markettime;
		}

		@Override
		public int getTypeid() { // 物品类型
			_xdb_verify_unsafe_();
			return typeid;
		}

		@Override
		public void setId(int _v_) { // 物品编号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlags(int _v_) { // 标志，叠加的时候，flags 也 OR 叠加
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosition(int _v_) { // 背包属性，位置。从0开始编号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNumber(int _v_) { // 数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimeout(long _v_) { // 到期时间。如果为0，代表没有时间限制
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExtid(long _v_) { // 扩展动态属性
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUniqueid(long _v_) { // 物品的唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLoseeffecttime(long _v_) { // 物品失效时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMarkettime(long _v_) { // 摆摊道具冻结时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTypeid(int _v_) { // 物品类型
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
			return Item.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Item.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Item.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Item.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Item.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Item.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Item.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Item.this.hashCode();
		}

		@Override
		public String toString() {
			return Item.this.toString();
		}

	}

	public static final class Data implements xbean.Item {
		private int id; // 物品编号
		private int flags; // 标志，叠加的时候，flags 也 OR 叠加
		private int position; // 背包属性，位置。从0开始编号
		private int number; // 数量
		private java.util.HashMap<Integer, Integer> numbermap; // 数量
		private long timeout; // 到期时间。如果为0，代表没有时间限制
		private long extid; // 扩展动态属性
		private long uniqueid; // 物品的唯一id
		private long loseeffecttime; // 物品失效时间
		private long markettime; // 摆摊道具冻结时间
		private int typeid; // 物品类型

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			position = -1;
			numbermap = new java.util.HashMap<Integer, Integer>();
			timeout = 0;
			typeid = -1;
		}

		Data(xbean.Item _o1_) {
			if (_o1_ instanceof Item) assign((Item)_o1_);
			else if (_o1_ instanceof Item.Data) assign((Item.Data)_o1_);
			else if (_o1_ instanceof Item.Const) assign(((Item.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Item _o_) {
			id = _o_.id;
			flags = _o_.flags;
			position = _o_.position;
			number = _o_.number;
			numbermap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.numbermap.entrySet())
				numbermap.put(_e_.getKey(), _e_.getValue());
			timeout = _o_.timeout;
			extid = _o_.extid;
			uniqueid = _o_.uniqueid;
			loseeffecttime = _o_.loseeffecttime;
			markettime = _o_.markettime;
			typeid = _o_.typeid;
		}

		private void assign(Item.Data _o_) {
			id = _o_.id;
			flags = _o_.flags;
			position = _o_.position;
			number = _o_.number;
			numbermap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.numbermap.entrySet())
				numbermap.put(_e_.getKey(), _e_.getValue());
			timeout = _o_.timeout;
			extid = _o_.extid;
			uniqueid = _o_.uniqueid;
			loseeffecttime = _o_.loseeffecttime;
			markettime = _o_.markettime;
			typeid = _o_.typeid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(flags);
			_os_.marshal(position);
			_os_.marshal(number);
			_os_.compact_uint32(numbermap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : numbermap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(timeout);
			_os_.marshal(extid);
			_os_.marshal(uniqueid);
			_os_.marshal(loseeffecttime);
			_os_.marshal(markettime);
			_os_.marshal(typeid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			flags = _os_.unmarshal_int();
			position = _os_.unmarshal_int();
			number = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					numbermap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					numbermap.put(_k_, _v_);
				}
			}
			timeout = _os_.unmarshal_long();
			extid = _os_.unmarshal_long();
			uniqueid = _os_.unmarshal_long();
			loseeffecttime = _os_.unmarshal_long();
			markettime = _os_.unmarshal_long();
			typeid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.Item copy() {
			return new Data(this);
		}

		@Override
		public xbean.Item toData() {
			return new Data(this);
		}

		public xbean.Item toBean() {
			return new Item(this, null, null);
		}

		@Override
		public xbean.Item toDataIf() {
			return this;
		}

		public xbean.Item toBeanIf() {
			return new Item(this, null, null);
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
		public int getId() { // 物品编号
			return id;
		}

		@Override
		public int getFlags() { // 标志，叠加的时候，flags 也 OR 叠加
			return flags;
		}

		@Override
		public int getPosition() { // 背包属性，位置。从0开始编号
			return position;
		}

		@Override
		public int getNumber() { // 数量
			return number;
		}

		@Override
		public java.util.Map<Integer, Integer> getNumbermap() { // 数量
			return numbermap;
		}

		@Override
		public java.util.Map<Integer, Integer> getNumbermapAsData() { // 数量
			return numbermap;
		}

		@Override
		public long getTimeout() { // 到期时间。如果为0，代表没有时间限制
			return timeout;
		}

		@Override
		public long getExtid() { // 扩展动态属性
			return extid;
		}

		@Override
		public long getUniqueid() { // 物品的唯一id
			return uniqueid;
		}

		@Override
		public long getLoseeffecttime() { // 物品失效时间
			return loseeffecttime;
		}

		@Override
		public long getMarkettime() { // 摆摊道具冻结时间
			return markettime;
		}

		@Override
		public int getTypeid() { // 物品类型
			return typeid;
		}

		@Override
		public void setId(int _v_) { // 物品编号
			id = _v_;
		}

		@Override
		public void setFlags(int _v_) { // 标志，叠加的时候，flags 也 OR 叠加
			flags = _v_;
		}

		@Override
		public void setPosition(int _v_) { // 背包属性，位置。从0开始编号
			position = _v_;
		}

		@Override
		public void setNumber(int _v_) { // 数量
			number = _v_;
		}

		@Override
		public void setTimeout(long _v_) { // 到期时间。如果为0，代表没有时间限制
			timeout = _v_;
		}

		@Override
		public void setExtid(long _v_) { // 扩展动态属性
			extid = _v_;
		}

		@Override
		public void setUniqueid(long _v_) { // 物品的唯一id
			uniqueid = _v_;
		}

		@Override
		public void setLoseeffecttime(long _v_) { // 物品失效时间
			loseeffecttime = _v_;
		}

		@Override
		public void setMarkettime(long _v_) { // 摆摊道具冻结时间
			markettime = _v_;
		}

		@Override
		public void setTypeid(int _v_) { // 物品类型
			typeid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Item.Data)) return false;
			Item.Data _o_ = (Item.Data) _o1_;
			if (id != _o_.id) return false;
			if (flags != _o_.flags) return false;
			if (position != _o_.position) return false;
			if (number != _o_.number) return false;
			if (!numbermap.equals(_o_.numbermap)) return false;
			if (timeout != _o_.timeout) return false;
			if (extid != _o_.extid) return false;
			if (uniqueid != _o_.uniqueid) return false;
			if (loseeffecttime != _o_.loseeffecttime) return false;
			if (markettime != _o_.markettime) return false;
			if (typeid != _o_.typeid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += flags;
			_h_ += position;
			_h_ += number;
			_h_ += numbermap.hashCode();
			_h_ += timeout;
			_h_ += extid;
			_h_ += uniqueid;
			_h_ += loseeffecttime;
			_h_ += markettime;
			_h_ += typeid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(flags);
			_sb_.append(",");
			_sb_.append(position);
			_sb_.append(",");
			_sb_.append(number);
			_sb_.append(",");
			_sb_.append(numbermap);
			_sb_.append(",");
			_sb_.append(timeout);
			_sb_.append(",");
			_sb_.append(extid);
			_sb_.append(",");
			_sb_.append(uniqueid);
			_sb_.append(",");
			_sb_.append(loseeffecttime);
			_sb_.append(",");
			_sb_.append(markettime);
			_sb_.append(",");
			_sb_.append(typeid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
