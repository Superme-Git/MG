
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NormalItem extends mkdb.XBean implements xbean.NormalItem {
	private long id; // 主键id
	private int firstno; // 一级菜单
	private int twono; // 二级菜单
	private int threeno; // 三级菜单
	private long uniquid; // 道具唯一id
	private int key; // 背包中key
	private int itemid; // 道具id
	private long extid; // 道具属性扩展id
	private String name; // 道具名称
	private long roleid; // 拥有者id
	private int level; // 道具等级
	private int number; // 道具数量
	private int price; // 道具价格
	private int attentionnumber; // 关注数量
	private long showtime; // 公示时间
	private long expiretime; // 到期时间

	@Override
	public void _reset_unsafe_() {
		id = 0;
		firstno = 0;
		twono = 0;
		threeno = 0;
		uniquid = 0;
		key = 0;
		itemid = 0;
		extid = 0;
		name = "";
		roleid = 0;
		level = 0;
		number = 0;
		price = 0;
		attentionnumber = 0;
		showtime = 0;
		expiretime = 0;
	}

	NormalItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		id = 0;
		firstno = 0;
		twono = 0;
		threeno = 0;
		uniquid = 0;
		key = 0;
		itemid = 0;
		extid = 0;
		name = "";
		roleid = 0;
		level = 0;
		number = 0;
		price = 0;
		attentionnumber = 0;
		showtime = 0;
		expiretime = 0;
	}

	public NormalItem() {
		this(0, null, null);
	}

	public NormalItem(NormalItem _o_) {
		this(_o_, null, null);
	}

	NormalItem(xbean.NormalItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NormalItem) assign((NormalItem)_o1_);
		else if (_o1_ instanceof NormalItem.Data) assign((NormalItem.Data)_o1_);
		else if (_o1_ instanceof NormalItem.Const) assign(((NormalItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NormalItem _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		firstno = _o_.firstno;
		twono = _o_.twono;
		threeno = _o_.threeno;
		uniquid = _o_.uniquid;
		key = _o_.key;
		itemid = _o_.itemid;
		extid = _o_.extid;
		name = _o_.name;
		roleid = _o_.roleid;
		level = _o_.level;
		number = _o_.number;
		price = _o_.price;
		attentionnumber = _o_.attentionnumber;
		showtime = _o_.showtime;
		expiretime = _o_.expiretime;
	}

	private void assign(NormalItem.Data _o_) {
		id = _o_.id;
		firstno = _o_.firstno;
		twono = _o_.twono;
		threeno = _o_.threeno;
		uniquid = _o_.uniquid;
		key = _o_.key;
		itemid = _o_.itemid;
		extid = _o_.extid;
		name = _o_.name;
		roleid = _o_.roleid;
		level = _o_.level;
		number = _o_.number;
		price = _o_.price;
		attentionnumber = _o_.attentionnumber;
		showtime = _o_.showtime;
		expiretime = _o_.expiretime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(firstno);
		_os_.marshal(twono);
		_os_.marshal(threeno);
		_os_.marshal(uniquid);
		_os_.marshal(key);
		_os_.marshal(itemid);
		_os_.marshal(extid);
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(roleid);
		_os_.marshal(level);
		_os_.marshal(number);
		_os_.marshal(price);
		_os_.marshal(attentionnumber);
		_os_.marshal(showtime);
		_os_.marshal(expiretime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_long();
		firstno = _os_.unmarshal_int();
		twono = _os_.unmarshal_int();
		threeno = _os_.unmarshal_int();
		uniquid = _os_.unmarshal_long();
		key = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		extid = _os_.unmarshal_long();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		roleid = _os_.unmarshal_long();
		level = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		attentionnumber = _os_.unmarshal_int();
		showtime = _os_.unmarshal_long();
		expiretime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.NormalItem copy() {
		_xdb_verify_unsafe_();
		return new NormalItem(this);
	}

	@Override
	public xbean.NormalItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NormalItem toBean() {
		_xdb_verify_unsafe_();
		return new NormalItem(this); // same as copy()
	}

	@Override
	public xbean.NormalItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NormalItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getId() { // 主键id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getFirstno() { // 一级菜单
		_xdb_verify_unsafe_();
		return firstno;
	}

	@Override
	public int getTwono() { // 二级菜单
		_xdb_verify_unsafe_();
		return twono;
	}

	@Override
	public int getThreeno() { // 三级菜单
		_xdb_verify_unsafe_();
		return threeno;
	}

	@Override
	public long getUniquid() { // 道具唯一id
		_xdb_verify_unsafe_();
		return uniquid;
	}

	@Override
	public int getKey() { // 背包中key
		_xdb_verify_unsafe_();
		return key;
	}

	@Override
	public int getItemid() { // 道具id
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public long getExtid() { // 道具属性扩展id
		_xdb_verify_unsafe_();
		return extid;
	}

	@Override
	public String getName() { // 道具名称
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 道具名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getRoleid() { // 拥有者id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getLevel() { // 道具等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getNumber() { // 道具数量
		_xdb_verify_unsafe_();
		return number;
	}

	@Override
	public int getPrice() { // 道具价格
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public int getAttentionnumber() { // 关注数量
		_xdb_verify_unsafe_();
		return attentionnumber;
	}

	@Override
	public long getShowtime() { // 公示时间
		_xdb_verify_unsafe_();
		return showtime;
	}

	@Override
	public long getExpiretime() { // 到期时间
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public void setId(long _v_) { // 主键id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setFirstno(int _v_) { // 一级菜单
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstno") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firstno) {
					public void rollback() { firstno = _xdb_saved; }
				};}});
		firstno = _v_;
	}

	@Override
	public void setTwono(int _v_) { // 二级菜单
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "twono") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, twono) {
					public void rollback() { twono = _xdb_saved; }
				};}});
		twono = _v_;
	}

	@Override
	public void setThreeno(int _v_) { // 三级菜单
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "threeno") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, threeno) {
					public void rollback() { threeno = _xdb_saved; }
				};}});
		threeno = _v_;
	}

	@Override
	public void setUniquid(long _v_) { // 道具唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uniquid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, uniquid) {
					public void rollback() { uniquid = _xdb_saved; }
				};}});
		uniquid = _v_;
	}

	@Override
	public void setKey(int _v_) { // 背包中key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "key") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, key) {
					public void rollback() { key = _xdb_saved; }
				};}});
		key = _v_;
	}

	@Override
	public void setItemid(int _v_) { // 道具id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setExtid(long _v_) { // 道具属性扩展id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "extid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, extid) {
					public void rollback() { extid = _xdb_saved; }
				};}});
		extid = _v_;
	}

	@Override
	public void setName(String _v_) { // 道具名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "name") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, name) {
					public void rollback() { name = _xdb_saved; }
				};}});
		name = _v_;
	}

	@Override
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 道具名称
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setRoleid(long _v_) { // 拥有者id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 道具等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setNumber(int _v_) { // 道具数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "number") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, number) {
					public void rollback() { number = _xdb_saved; }
				};}});
		number = _v_;
	}

	@Override
	public void setPrice(int _v_) { // 道具价格
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "price") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, price) {
					public void rollback() { price = _xdb_saved; }
				};}});
		price = _v_;
	}

	@Override
	public void setAttentionnumber(int _v_) { // 关注数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "attentionnumber") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, attentionnumber) {
					public void rollback() { attentionnumber = _xdb_saved; }
				};}});
		attentionnumber = _v_;
	}

	@Override
	public void setShowtime(long _v_) { // 公示时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "showtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, showtime) {
					public void rollback() { showtime = _xdb_saved; }
				};}});
		showtime = _v_;
	}

	@Override
	public void setExpiretime(long _v_) { // 到期时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NormalItem _o_ = null;
		if ( _o1_ instanceof NormalItem ) _o_ = (NormalItem)_o1_;
		else if ( _o1_ instanceof NormalItem.Const ) _o_ = ((NormalItem.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (firstno != _o_.firstno) return false;
		if (twono != _o_.twono) return false;
		if (threeno != _o_.threeno) return false;
		if (uniquid != _o_.uniquid) return false;
		if (key != _o_.key) return false;
		if (itemid != _o_.itemid) return false;
		if (extid != _o_.extid) return false;
		if (!name.equals(_o_.name)) return false;
		if (roleid != _o_.roleid) return false;
		if (level != _o_.level) return false;
		if (number != _o_.number) return false;
		if (price != _o_.price) return false;
		if (attentionnumber != _o_.attentionnumber) return false;
		if (showtime != _o_.showtime) return false;
		if (expiretime != _o_.expiretime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += firstno;
		_h_ += twono;
		_h_ += threeno;
		_h_ += uniquid;
		_h_ += key;
		_h_ += itemid;
		_h_ += extid;
		_h_ += name.hashCode();
		_h_ += roleid;
		_h_ += level;
		_h_ += number;
		_h_ += price;
		_h_ += attentionnumber;
		_h_ += showtime;
		_h_ += expiretime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(firstno);
		_sb_.append(",");
		_sb_.append(twono);
		_sb_.append(",");
		_sb_.append(threeno);
		_sb_.append(",");
		_sb_.append(uniquid);
		_sb_.append(",");
		_sb_.append(key);
		_sb_.append(",");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(extid);
		_sb_.append(",");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(number);
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(attentionnumber);
		_sb_.append(",");
		_sb_.append(showtime);
		_sb_.append(",");
		_sb_.append(expiretime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstno"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("twono"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("threeno"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniquid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("key"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("extid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("number"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("attentionnumber"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("showtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		return lb;
	}

	private class Const implements xbean.NormalItem {
		NormalItem nThis() {
			return NormalItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NormalItem copy() {
			return NormalItem.this.copy();
		}

		@Override
		public xbean.NormalItem toData() {
			return NormalItem.this.toData();
		}

		public xbean.NormalItem toBean() {
			return NormalItem.this.toBean();
		}

		@Override
		public xbean.NormalItem toDataIf() {
			return NormalItem.this.toDataIf();
		}

		public xbean.NormalItem toBeanIf() {
			return NormalItem.this.toBeanIf();
		}

		@Override
		public long getId() { // 主键id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getFirstno() { // 一级菜单
			_xdb_verify_unsafe_();
			return firstno;
		}

		@Override
		public int getTwono() { // 二级菜单
			_xdb_verify_unsafe_();
			return twono;
		}

		@Override
		public int getThreeno() { // 三级菜单
			_xdb_verify_unsafe_();
			return threeno;
		}

		@Override
		public long getUniquid() { // 道具唯一id
			_xdb_verify_unsafe_();
			return uniquid;
		}

		@Override
		public int getKey() { // 背包中key
			_xdb_verify_unsafe_();
			return key;
		}

		@Override
		public int getItemid() { // 道具id
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public long getExtid() { // 道具属性扩展id
			_xdb_verify_unsafe_();
			return extid;
		}

		@Override
		public String getName() { // 道具名称
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 道具名称
			_xdb_verify_unsafe_();
			return NormalItem.this.getNameOctets();
		}

		@Override
		public long getRoleid() { // 拥有者id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getLevel() { // 道具等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getNumber() { // 道具数量
			_xdb_verify_unsafe_();
			return number;
		}

		@Override
		public int getPrice() { // 道具价格
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public int getAttentionnumber() { // 关注数量
			_xdb_verify_unsafe_();
			return attentionnumber;
		}

		@Override
		public long getShowtime() { // 公示时间
			_xdb_verify_unsafe_();
			return showtime;
		}

		@Override
		public long getExpiretime() { // 到期时间
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public void setId(long _v_) { // 主键id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstno(int _v_) { // 一级菜单
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTwono(int _v_) { // 二级菜单
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setThreeno(int _v_) { // 三级菜单
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUniquid(long _v_) { // 道具唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setKey(int _v_) { // 背包中key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemid(int _v_) { // 道具id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExtid(long _v_) { // 道具属性扩展id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(String _v_) { // 道具名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 道具名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 拥有者id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 道具等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNumber(int _v_) { // 道具数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(int _v_) { // 道具价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttentionnumber(int _v_) { // 关注数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShowtime(long _v_) { // 公示时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpiretime(long _v_) { // 到期时间
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
			return NormalItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NormalItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NormalItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NormalItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NormalItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NormalItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NormalItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NormalItem.this.hashCode();
		}

		@Override
		public String toString() {
			return NormalItem.this.toString();
		}

	}

	public static final class Data implements xbean.NormalItem {
		private long id; // 主键id
		private int firstno; // 一级菜单
		private int twono; // 二级菜单
		private int threeno; // 三级菜单
		private long uniquid; // 道具唯一id
		private int key; // 背包中key
		private int itemid; // 道具id
		private long extid; // 道具属性扩展id
		private String name; // 道具名称
		private long roleid; // 拥有者id
		private int level; // 道具等级
		private int number; // 道具数量
		private int price; // 道具价格
		private int attentionnumber; // 关注数量
		private long showtime; // 公示时间
		private long expiretime; // 到期时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			id = 0;
			firstno = 0;
			twono = 0;
			threeno = 0;
			uniquid = 0;
			key = 0;
			itemid = 0;
			extid = 0;
			name = "";
			roleid = 0;
			level = 0;
			number = 0;
			price = 0;
			attentionnumber = 0;
			showtime = 0;
			expiretime = 0;
		}

		Data(xbean.NormalItem _o1_) {
			if (_o1_ instanceof NormalItem) assign((NormalItem)_o1_);
			else if (_o1_ instanceof NormalItem.Data) assign((NormalItem.Data)_o1_);
			else if (_o1_ instanceof NormalItem.Const) assign(((NormalItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NormalItem _o_) {
			id = _o_.id;
			firstno = _o_.firstno;
			twono = _o_.twono;
			threeno = _o_.threeno;
			uniquid = _o_.uniquid;
			key = _o_.key;
			itemid = _o_.itemid;
			extid = _o_.extid;
			name = _o_.name;
			roleid = _o_.roleid;
			level = _o_.level;
			number = _o_.number;
			price = _o_.price;
			attentionnumber = _o_.attentionnumber;
			showtime = _o_.showtime;
			expiretime = _o_.expiretime;
		}

		private void assign(NormalItem.Data _o_) {
			id = _o_.id;
			firstno = _o_.firstno;
			twono = _o_.twono;
			threeno = _o_.threeno;
			uniquid = _o_.uniquid;
			key = _o_.key;
			itemid = _o_.itemid;
			extid = _o_.extid;
			name = _o_.name;
			roleid = _o_.roleid;
			level = _o_.level;
			number = _o_.number;
			price = _o_.price;
			attentionnumber = _o_.attentionnumber;
			showtime = _o_.showtime;
			expiretime = _o_.expiretime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(firstno);
			_os_.marshal(twono);
			_os_.marshal(threeno);
			_os_.marshal(uniquid);
			_os_.marshal(key);
			_os_.marshal(itemid);
			_os_.marshal(extid);
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(roleid);
			_os_.marshal(level);
			_os_.marshal(number);
			_os_.marshal(price);
			_os_.marshal(attentionnumber);
			_os_.marshal(showtime);
			_os_.marshal(expiretime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_long();
			firstno = _os_.unmarshal_int();
			twono = _os_.unmarshal_int();
			threeno = _os_.unmarshal_int();
			uniquid = _os_.unmarshal_long();
			key = _os_.unmarshal_int();
			itemid = _os_.unmarshal_int();
			extid = _os_.unmarshal_long();
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			roleid = _os_.unmarshal_long();
			level = _os_.unmarshal_int();
			number = _os_.unmarshal_int();
			price = _os_.unmarshal_int();
			attentionnumber = _os_.unmarshal_int();
			showtime = _os_.unmarshal_long();
			expiretime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.NormalItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.NormalItem toData() {
			return new Data(this);
		}

		public xbean.NormalItem toBean() {
			return new NormalItem(this, null, null);
		}

		@Override
		public xbean.NormalItem toDataIf() {
			return this;
		}

		public xbean.NormalItem toBeanIf() {
			return new NormalItem(this, null, null);
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
		public long getId() { // 主键id
			return id;
		}

		@Override
		public int getFirstno() { // 一级菜单
			return firstno;
		}

		@Override
		public int getTwono() { // 二级菜单
			return twono;
		}

		@Override
		public int getThreeno() { // 三级菜单
			return threeno;
		}

		@Override
		public long getUniquid() { // 道具唯一id
			return uniquid;
		}

		@Override
		public int getKey() { // 背包中key
			return key;
		}

		@Override
		public int getItemid() { // 道具id
			return itemid;
		}

		@Override
		public long getExtid() { // 道具属性扩展id
			return extid;
		}

		@Override
		public String getName() { // 道具名称
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 道具名称
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getRoleid() { // 拥有者id
			return roleid;
		}

		@Override
		public int getLevel() { // 道具等级
			return level;
		}

		@Override
		public int getNumber() { // 道具数量
			return number;
		}

		@Override
		public int getPrice() { // 道具价格
			return price;
		}

		@Override
		public int getAttentionnumber() { // 关注数量
			return attentionnumber;
		}

		@Override
		public long getShowtime() { // 公示时间
			return showtime;
		}

		@Override
		public long getExpiretime() { // 到期时间
			return expiretime;
		}

		@Override
		public void setId(long _v_) { // 主键id
			id = _v_;
		}

		@Override
		public void setFirstno(int _v_) { // 一级菜单
			firstno = _v_;
		}

		@Override
		public void setTwono(int _v_) { // 二级菜单
			twono = _v_;
		}

		@Override
		public void setThreeno(int _v_) { // 三级菜单
			threeno = _v_;
		}

		@Override
		public void setUniquid(long _v_) { // 道具唯一id
			uniquid = _v_;
		}

		@Override
		public void setKey(int _v_) { // 背包中key
			key = _v_;
		}

		@Override
		public void setItemid(int _v_) { // 道具id
			itemid = _v_;
		}

		@Override
		public void setExtid(long _v_) { // 道具属性扩展id
			extid = _v_;
		}

		@Override
		public void setName(String _v_) { // 道具名称
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 道具名称
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setRoleid(long _v_) { // 拥有者id
			roleid = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 道具等级
			level = _v_;
		}

		@Override
		public void setNumber(int _v_) { // 道具数量
			number = _v_;
		}

		@Override
		public void setPrice(int _v_) { // 道具价格
			price = _v_;
		}

		@Override
		public void setAttentionnumber(int _v_) { // 关注数量
			attentionnumber = _v_;
		}

		@Override
		public void setShowtime(long _v_) { // 公示时间
			showtime = _v_;
		}

		@Override
		public void setExpiretime(long _v_) { // 到期时间
			expiretime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NormalItem.Data)) return false;
			NormalItem.Data _o_ = (NormalItem.Data) _o1_;
			if (id != _o_.id) return false;
			if (firstno != _o_.firstno) return false;
			if (twono != _o_.twono) return false;
			if (threeno != _o_.threeno) return false;
			if (uniquid != _o_.uniquid) return false;
			if (key != _o_.key) return false;
			if (itemid != _o_.itemid) return false;
			if (extid != _o_.extid) return false;
			if (!name.equals(_o_.name)) return false;
			if (roleid != _o_.roleid) return false;
			if (level != _o_.level) return false;
			if (number != _o_.number) return false;
			if (price != _o_.price) return false;
			if (attentionnumber != _o_.attentionnumber) return false;
			if (showtime != _o_.showtime) return false;
			if (expiretime != _o_.expiretime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += firstno;
			_h_ += twono;
			_h_ += threeno;
			_h_ += uniquid;
			_h_ += key;
			_h_ += itemid;
			_h_ += extid;
			_h_ += name.hashCode();
			_h_ += roleid;
			_h_ += level;
			_h_ += number;
			_h_ += price;
			_h_ += attentionnumber;
			_h_ += showtime;
			_h_ += expiretime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(firstno);
			_sb_.append(",");
			_sb_.append(twono);
			_sb_.append(",");
			_sb_.append(threeno);
			_sb_.append(",");
			_sb_.append(uniquid);
			_sb_.append(",");
			_sb_.append(key);
			_sb_.append(",");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(extid);
			_sb_.append(",");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(number);
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(attentionnumber);
			_sb_.append(",");
			_sb_.append(showtime);
			_sb_.append(",");
			_sb_.append(expiretime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
