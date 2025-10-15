
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EquipItem extends mkdb.XBean implements xbean.EquipItem {
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
	private int effect; // 特效
	private int skill; // 特技
	private int color; // 品质
	private java.util.HashMap<Integer, Integer> attr; // 基础属性
	private java.util.HashMap<Integer, Integer> addattr; // 附加属性
	private int totalattr; // 属性总和
	private int equipscore; // 装备评分

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
		effect = 0;
		skill = 0;
		color = 0;
		attr.clear();
		addattr.clear();
		totalattr = 0;
		equipscore = 0;
	}

	EquipItem(int __, mkdb.XBean _xp_, String _vn_) {
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
		effect = 0;
		skill = 0;
		color = 0;
		attr = new java.util.HashMap<Integer, Integer>();
		addattr = new java.util.HashMap<Integer, Integer>();
		totalattr = 0;
		equipscore = 0;
	}

	public EquipItem() {
		this(0, null, null);
	}

	public EquipItem(EquipItem _o_) {
		this(_o_, null, null);
	}

	EquipItem(xbean.EquipItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EquipItem) assign((EquipItem)_o1_);
		else if (_o1_ instanceof EquipItem.Data) assign((EquipItem.Data)_o1_);
		else if (_o1_ instanceof EquipItem.Const) assign(((EquipItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EquipItem _o_) {
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
		effect = _o_.effect;
		skill = _o_.skill;
		color = _o_.color;
		attr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
			attr.put(_e_.getKey(), _e_.getValue());
		addattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
			addattr.put(_e_.getKey(), _e_.getValue());
		totalattr = _o_.totalattr;
		equipscore = _o_.equipscore;
	}

	private void assign(EquipItem.Data _o_) {
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
		effect = _o_.effect;
		skill = _o_.skill;
		color = _o_.color;
		attr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
			attr.put(_e_.getKey(), _e_.getValue());
		addattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
			addattr.put(_e_.getKey(), _e_.getValue());
		totalattr = _o_.totalattr;
		equipscore = _o_.equipscore;
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
		_os_.marshal(effect);
		_os_.marshal(skill);
		_os_.marshal(color);
		_os_.compact_uint32(attr.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : attr.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(addattr.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : addattr.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(totalattr);
		_os_.marshal(equipscore);
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
		effect = _os_.unmarshal_int();
		skill = _os_.unmarshal_int();
		color = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				attr = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				attr.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				addattr = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				addattr.put(_k_, _v_);
			}
		}
		totalattr = _os_.unmarshal_int();
		equipscore = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.EquipItem copy() {
		_xdb_verify_unsafe_();
		return new EquipItem(this);
	}

	@Override
	public xbean.EquipItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EquipItem toBean() {
		_xdb_verify_unsafe_();
		return new EquipItem(this); // same as copy()
	}

	@Override
	public xbean.EquipItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EquipItem toBeanIf() {
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
	public int getEffect() { // 特效
		_xdb_verify_unsafe_();
		return effect;
	}

	@Override
	public int getSkill() { // 特技
		_xdb_verify_unsafe_();
		return skill;
	}

	@Override
	public int getColor() { // 品质
		_xdb_verify_unsafe_();
		return color;
	}

	@Override
	public java.util.Map<Integer, Integer> getAttr() { // 基础属性
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "attr"), attr);
	}

	@Override
	public java.util.Map<Integer, Integer> getAttrAsData() { // 基础属性
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> attr;
		EquipItem _o_ = this;
		attr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
			attr.put(_e_.getKey(), _e_.getValue());
		return attr;
	}

	@Override
	public java.util.Map<Integer, Integer> getAddattr() { // 附加属性
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "addattr"), addattr);
	}

	@Override
	public java.util.Map<Integer, Integer> getAddattrAsData() { // 附加属性
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> addattr;
		EquipItem _o_ = this;
		addattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
			addattr.put(_e_.getKey(), _e_.getValue());
		return addattr;
	}

	@Override
	public int getTotalattr() { // 属性总和
		_xdb_verify_unsafe_();
		return totalattr;
	}

	@Override
	public int getEquipscore() { // 装备评分
		_xdb_verify_unsafe_();
		return equipscore;
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
	public void setEffect(int _v_) { // 特效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "effect") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, effect) {
					public void rollback() { effect = _xdb_saved; }
				};}});
		effect = _v_;
	}

	@Override
	public void setSkill(int _v_) { // 特技
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skill") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skill) {
					public void rollback() { skill = _xdb_saved; }
				};}});
		skill = _v_;
	}

	@Override
	public void setColor(int _v_) { // 品质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "color") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, color) {
					public void rollback() { color = _xdb_saved; }
				};}});
		color = _v_;
	}

	@Override
	public void setTotalattr(int _v_) { // 属性总和
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalattr") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalattr) {
					public void rollback() { totalattr = _xdb_saved; }
				};}});
		totalattr = _v_;
	}

	@Override
	public void setEquipscore(int _v_) { // 装备评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "equipscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, equipscore) {
					public void rollback() { equipscore = _xdb_saved; }
				};}});
		equipscore = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EquipItem _o_ = null;
		if ( _o1_ instanceof EquipItem ) _o_ = (EquipItem)_o1_;
		else if ( _o1_ instanceof EquipItem.Const ) _o_ = ((EquipItem.Const)_o1_).nThis();
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
		if (effect != _o_.effect) return false;
		if (skill != _o_.skill) return false;
		if (color != _o_.color) return false;
		if (!attr.equals(_o_.attr)) return false;
		if (!addattr.equals(_o_.addattr)) return false;
		if (totalattr != _o_.totalattr) return false;
		if (equipscore != _o_.equipscore) return false;
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
		_h_ += effect;
		_h_ += skill;
		_h_ += color;
		_h_ += attr.hashCode();
		_h_ += addattr.hashCode();
		_h_ += totalattr;
		_h_ += equipscore;
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
		_sb_.append(",");
		_sb_.append(effect);
		_sb_.append(",");
		_sb_.append(skill);
		_sb_.append(",");
		_sb_.append(color);
		_sb_.append(",");
		_sb_.append(attr);
		_sb_.append(",");
		_sb_.append(addattr);
		_sb_.append(",");
		_sb_.append(totalattr);
		_sb_.append(",");
		_sb_.append(equipscore);
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
		lb.add(new mkdb.logs.ListenableChanged().setVarName("effect"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skill"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("color"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("attr"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("addattr"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalattr"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("equipscore"));
		return lb;
	}

	private class Const implements xbean.EquipItem {
		EquipItem nThis() {
			return EquipItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EquipItem copy() {
			return EquipItem.this.copy();
		}

		@Override
		public xbean.EquipItem toData() {
			return EquipItem.this.toData();
		}

		public xbean.EquipItem toBean() {
			return EquipItem.this.toBean();
		}

		@Override
		public xbean.EquipItem toDataIf() {
			return EquipItem.this.toDataIf();
		}

		public xbean.EquipItem toBeanIf() {
			return EquipItem.this.toBeanIf();
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
			return EquipItem.this.getNameOctets();
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
		public int getEffect() { // 特效
			_xdb_verify_unsafe_();
			return effect;
		}

		@Override
		public int getSkill() { // 特技
			_xdb_verify_unsafe_();
			return skill;
		}

		@Override
		public int getColor() { // 品质
			_xdb_verify_unsafe_();
			return color;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttr() { // 基础属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(attr);
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrAsData() { // 基础属性
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> attr;
			EquipItem _o_ = EquipItem.this;
			attr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
				attr.put(_e_.getKey(), _e_.getValue());
			return attr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattr() { // 附加属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(addattr);
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattrAsData() { // 附加属性
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> addattr;
			EquipItem _o_ = EquipItem.this;
			addattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
				addattr.put(_e_.getKey(), _e_.getValue());
			return addattr;
		}

		@Override
		public int getTotalattr() { // 属性总和
			_xdb_verify_unsafe_();
			return totalattr;
		}

		@Override
		public int getEquipscore() { // 装备评分
			_xdb_verify_unsafe_();
			return equipscore;
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
		public void setEffect(int _v_) { // 特效
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkill(int _v_) { // 特技
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setColor(int _v_) { // 品质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalattr(int _v_) { // 属性总和
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEquipscore(int _v_) { // 装备评分
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
			return EquipItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EquipItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EquipItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EquipItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EquipItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EquipItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EquipItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EquipItem.this.hashCode();
		}

		@Override
		public String toString() {
			return EquipItem.this.toString();
		}

	}

	public static final class Data implements xbean.EquipItem {
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
		private int effect; // 特效
		private int skill; // 特技
		private int color; // 品质
		private java.util.HashMap<Integer, Integer> attr; // 基础属性
		private java.util.HashMap<Integer, Integer> addattr; // 附加属性
		private int totalattr; // 属性总和
		private int equipscore; // 装备评分

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
			effect = 0;
			skill = 0;
			color = 0;
			attr = new java.util.HashMap<Integer, Integer>();
			addattr = new java.util.HashMap<Integer, Integer>();
			totalattr = 0;
			equipscore = 0;
		}

		Data(xbean.EquipItem _o1_) {
			if (_o1_ instanceof EquipItem) assign((EquipItem)_o1_);
			else if (_o1_ instanceof EquipItem.Data) assign((EquipItem.Data)_o1_);
			else if (_o1_ instanceof EquipItem.Const) assign(((EquipItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EquipItem _o_) {
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
			effect = _o_.effect;
			skill = _o_.skill;
			color = _o_.color;
			attr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
				attr.put(_e_.getKey(), _e_.getValue());
			addattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
				addattr.put(_e_.getKey(), _e_.getValue());
			totalattr = _o_.totalattr;
			equipscore = _o_.equipscore;
		}

		private void assign(EquipItem.Data _o_) {
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
			effect = _o_.effect;
			skill = _o_.skill;
			color = _o_.color;
			attr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
				attr.put(_e_.getKey(), _e_.getValue());
			addattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
				addattr.put(_e_.getKey(), _e_.getValue());
			totalattr = _o_.totalattr;
			equipscore = _o_.equipscore;
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
			_os_.marshal(effect);
			_os_.marshal(skill);
			_os_.marshal(color);
			_os_.compact_uint32(attr.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : attr.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(addattr.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : addattr.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(totalattr);
			_os_.marshal(equipscore);
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
			effect = _os_.unmarshal_int();
			skill = _os_.unmarshal_int();
			color = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					attr = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					attr.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					addattr = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					addattr.put(_k_, _v_);
				}
			}
			totalattr = _os_.unmarshal_int();
			equipscore = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.EquipItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.EquipItem toData() {
			return new Data(this);
		}

		public xbean.EquipItem toBean() {
			return new EquipItem(this, null, null);
		}

		@Override
		public xbean.EquipItem toDataIf() {
			return this;
		}

		public xbean.EquipItem toBeanIf() {
			return new EquipItem(this, null, null);
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
		public int getEffect() { // 特效
			return effect;
		}

		@Override
		public int getSkill() { // 特技
			return skill;
		}

		@Override
		public int getColor() { // 品质
			return color;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttr() { // 基础属性
			return attr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrAsData() { // 基础属性
			return attr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattr() { // 附加属性
			return addattr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattrAsData() { // 附加属性
			return addattr;
		}

		@Override
		public int getTotalattr() { // 属性总和
			return totalattr;
		}

		@Override
		public int getEquipscore() { // 装备评分
			return equipscore;
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
		public void setEffect(int _v_) { // 特效
			effect = _v_;
		}

		@Override
		public void setSkill(int _v_) { // 特技
			skill = _v_;
		}

		@Override
		public void setColor(int _v_) { // 品质
			color = _v_;
		}

		@Override
		public void setTotalattr(int _v_) { // 属性总和
			totalattr = _v_;
		}

		@Override
		public void setEquipscore(int _v_) { // 装备评分
			equipscore = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EquipItem.Data)) return false;
			EquipItem.Data _o_ = (EquipItem.Data) _o1_;
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
			if (effect != _o_.effect) return false;
			if (skill != _o_.skill) return false;
			if (color != _o_.color) return false;
			if (!attr.equals(_o_.attr)) return false;
			if (!addattr.equals(_o_.addattr)) return false;
			if (totalattr != _o_.totalattr) return false;
			if (equipscore != _o_.equipscore) return false;
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
			_h_ += effect;
			_h_ += skill;
			_h_ += color;
			_h_ += attr.hashCode();
			_h_ += addattr.hashCode();
			_h_ += totalattr;
			_h_ += equipscore;
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
			_sb_.append(",");
			_sb_.append(effect);
			_sb_.append(",");
			_sb_.append(skill);
			_sb_.append(",");
			_sb_.append(color);
			_sb_.append(",");
			_sb_.append(attr);
			_sb_.append(",");
			_sb_.append(addattr);
			_sb_.append(",");
			_sb_.append(totalattr);
			_sb_.append(",");
			_sb_.append(equipscore);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
