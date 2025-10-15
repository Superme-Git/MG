
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WheelInfo extends mkdb.XBean implements xbean.WheelInfo {
	private long time; // 答题的时间
	private int npcbaseid; // 大转盘可能有多个npc有服务,所以要有npc的id
	private int serviceid; // 一个npc可以有多个大转盘,所以要有服务的id
	private boolean finish; // 是否完成了这个大转盘
	private int basemoney; // 金钱的基数(在翻倍的时候用得上)
	private int basesmoney; // 
	private int baseexp; // 
	private int msgid; // 策划要求每个大转盘给的闲话id都不一样
	private java.util.ArrayList<String> msgparas; // 
	private int index; // 真正中奖的那个item的下标
	private java.util.ArrayList<xbean.WheelItem> wheelitems; // 一个大转盘所包含的格子

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		npcbaseid = 0;
		serviceid = 0;
		finish = false;
		basemoney = 0;
		basesmoney = 0;
		baseexp = 0;
		msgid = 0;
		msgparas.clear();
		index = 0;
		wheelitems.clear();
	}

	WheelInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		msgparas = new java.util.ArrayList<String>();
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
	}

	public WheelInfo() {
		this(0, null, null);
	}

	public WheelInfo(WheelInfo _o_) {
		this(_o_, null, null);
	}

	WheelInfo(xbean.WheelInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WheelInfo) assign((WheelInfo)_o1_);
		else if (_o1_ instanceof WheelInfo.Data) assign((WheelInfo.Data)_o1_);
		else if (_o1_ instanceof WheelInfo.Const) assign(((WheelInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WheelInfo _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		npcbaseid = _o_.npcbaseid;
		serviceid = _o_.serviceid;
		finish = _o_.finish;
		basemoney = _o_.basemoney;
		basesmoney = _o_.basesmoney;
		baseexp = _o_.baseexp;
		msgid = _o_.msgid;
		msgparas = new java.util.ArrayList<String>();
		msgparas.addAll(_o_.msgparas);
		index = _o_.index;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem(_v_, this, "wheelitems"));
	}

	private void assign(WheelInfo.Data _o_) {
		time = _o_.time;
		npcbaseid = _o_.npcbaseid;
		serviceid = _o_.serviceid;
		finish = _o_.finish;
		basemoney = _o_.basemoney;
		basesmoney = _o_.basesmoney;
		baseexp = _o_.baseexp;
		msgid = _o_.msgid;
		msgparas = new java.util.ArrayList<String>();
		msgparas.addAll(_o_.msgparas);
		index = _o_.index;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem(_v_, this, "wheelitems"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		_os_.marshal(npcbaseid);
		_os_.marshal(serviceid);
		_os_.marshal(finish);
		_os_.marshal(basemoney);
		_os_.marshal(basesmoney);
		_os_.marshal(baseexp);
		_os_.marshal(msgid);
		_os_.compact_uint32(msgparas.size());
		for (String _v_ : msgparas) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		_os_.marshal(index);
		_os_.compact_uint32(wheelitems.size());
		for (xbean.WheelItem _v_ : wheelitems) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		npcbaseid = _os_.unmarshal_int();
		serviceid = _os_.unmarshal_int();
		finish = _os_.unmarshal_boolean();
		basemoney = _os_.unmarshal_int();
		basesmoney = _os_.unmarshal_int();
		baseexp = _os_.unmarshal_int();
		msgid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			msgparas.add(_v_);
		}
		index = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.WheelItem _v_ = new WheelItem(0, this, "wheelitems");
			_v_.unmarshal(_os_);
			wheelitems.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.WheelInfo copy() {
		_xdb_verify_unsafe_();
		return new WheelInfo(this);
	}

	@Override
	public xbean.WheelInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelInfo toBean() {
		_xdb_verify_unsafe_();
		return new WheelInfo(this); // same as copy()
	}

	@Override
	public xbean.WheelInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 答题的时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public int getNpcbaseid() { // 大转盘可能有多个npc有服务,所以要有npc的id
		_xdb_verify_unsafe_();
		return npcbaseid;
	}

	@Override
	public int getServiceid() { // 一个npc可以有多个大转盘,所以要有服务的id
		_xdb_verify_unsafe_();
		return serviceid;
	}

	@Override
	public boolean getFinish() { // 是否完成了这个大转盘
		_xdb_verify_unsafe_();
		return finish;
	}

	@Override
	public int getBasemoney() { // 金钱的基数(在翻倍的时候用得上)
		_xdb_verify_unsafe_();
		return basemoney;
	}

	@Override
	public int getBasesmoney() { // 
		_xdb_verify_unsafe_();
		return basesmoney;
	}

	@Override
	public int getBaseexp() { // 
		_xdb_verify_unsafe_();
		return baseexp;
	}

	@Override
	public int getMsgid() { // 策划要求每个大转盘给的闲话id都不一样
		_xdb_verify_unsafe_();
		return msgid;
	}

	@Override
	public java.util.List<String> getMsgparas() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "msgparas"), msgparas);
	}

	public java.util.List<String> getMsgparasAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<String> msgparas;
		WheelInfo _o_ = this;
		msgparas = new java.util.ArrayList<String>();
		msgparas.addAll(_o_.msgparas);
		return msgparas;
	}

	@Override
	public int getIndex() { // 真正中奖的那个item的下标
		_xdb_verify_unsafe_();
		return index;
	}

	@Override
	public java.util.List<xbean.WheelItem> getWheelitems() { // 一个大转盘所包含的格子
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "wheelitems"), wheelitems);
	}

	public java.util.List<xbean.WheelItem> getWheelitemsAsData() { // 一个大转盘所包含的格子
		_xdb_verify_unsafe_();
		java.util.List<xbean.WheelItem> wheelitems;
		WheelInfo _o_ = this;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem.Data(_v_));
		return wheelitems;
	}

	@Override
	public void setTime(long _v_) { // 答题的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setNpcbaseid(int _v_) { // 大转盘可能有多个npc有服务,所以要有npc的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcbaseid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, npcbaseid) {
					public void rollback() { npcbaseid = _xdb_saved; }
				};}});
		npcbaseid = _v_;
	}

	@Override
	public void setServiceid(int _v_) { // 一个npc可以有多个大转盘,所以要有服务的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "serviceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, serviceid) {
					public void rollback() { serviceid = _xdb_saved; }
				};}});
		serviceid = _v_;
	}

	@Override
	public void setFinish(boolean _v_) { // 是否完成了这个大转盘
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "finish") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, finish) {
					public void rollback() { finish = _xdb_saved; }
				};}});
		finish = _v_;
	}

	@Override
	public void setBasemoney(int _v_) { // 金钱的基数(在翻倍的时候用得上)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "basemoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, basemoney) {
					public void rollback() { basemoney = _xdb_saved; }
				};}});
		basemoney = _v_;
	}

	@Override
	public void setBasesmoney(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "basesmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, basesmoney) {
					public void rollback() { basesmoney = _xdb_saved; }
				};}});
		basesmoney = _v_;
	}

	@Override
	public void setBaseexp(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "baseexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, baseexp) {
					public void rollback() { baseexp = _xdb_saved; }
				};}});
		baseexp = _v_;
	}

	@Override
	public void setMsgid(int _v_) { // 策划要求每个大转盘给的闲话id都不一样
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "msgid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, msgid) {
					public void rollback() { msgid = _xdb_saved; }
				};}});
		msgid = _v_;
	}

	@Override
	public void setIndex(int _v_) { // 真正中奖的那个item的下标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "index") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, index) {
					public void rollback() { index = _xdb_saved; }
				};}});
		index = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WheelInfo _o_ = null;
		if ( _o1_ instanceof WheelInfo ) _o_ = (WheelInfo)_o1_;
		else if ( _o1_ instanceof WheelInfo.Const ) _o_ = ((WheelInfo.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (npcbaseid != _o_.npcbaseid) return false;
		if (serviceid != _o_.serviceid) return false;
		if (finish != _o_.finish) return false;
		if (basemoney != _o_.basemoney) return false;
		if (basesmoney != _o_.basesmoney) return false;
		if (baseexp != _o_.baseexp) return false;
		if (msgid != _o_.msgid) return false;
		if (!msgparas.equals(_o_.msgparas)) return false;
		if (index != _o_.index) return false;
		if (!wheelitems.equals(_o_.wheelitems)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += npcbaseid;
		_h_ += serviceid;
		_h_ += finish ? 1231 : 1237;
		_h_ += basemoney;
		_h_ += basesmoney;
		_h_ += baseexp;
		_h_ += msgid;
		_h_ += msgparas.hashCode();
		_h_ += index;
		_h_ += wheelitems.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(npcbaseid);
		_sb_.append(",");
		_sb_.append(serviceid);
		_sb_.append(",");
		_sb_.append(finish);
		_sb_.append(",");
		_sb_.append(basemoney);
		_sb_.append(",");
		_sb_.append(basesmoney);
		_sb_.append(",");
		_sb_.append(baseexp);
		_sb_.append(",");
		_sb_.append(msgid);
		_sb_.append(",");
		_sb_.append(msgparas);
		_sb_.append(",");
		_sb_.append(index);
		_sb_.append(",");
		_sb_.append(wheelitems);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcbaseid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("serviceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("finish"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basemoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basesmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("baseexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("msgid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("msgparas"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("index"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("wheelitems"));
		return lb;
	}

	private class Const implements xbean.WheelInfo {
		WheelInfo nThis() {
			return WheelInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WheelInfo copy() {
			return WheelInfo.this.copy();
		}

		@Override
		public xbean.WheelInfo toData() {
			return WheelInfo.this.toData();
		}

		public xbean.WheelInfo toBean() {
			return WheelInfo.this.toBean();
		}

		@Override
		public xbean.WheelInfo toDataIf() {
			return WheelInfo.this.toDataIf();
		}

		public xbean.WheelInfo toBeanIf() {
			return WheelInfo.this.toBeanIf();
		}

		@Override
		public long getTime() { // 答题的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public int getNpcbaseid() { // 大转盘可能有多个npc有服务,所以要有npc的id
			_xdb_verify_unsafe_();
			return npcbaseid;
		}

		@Override
		public int getServiceid() { // 一个npc可以有多个大转盘,所以要有服务的id
			_xdb_verify_unsafe_();
			return serviceid;
		}

		@Override
		public boolean getFinish() { // 是否完成了这个大转盘
			_xdb_verify_unsafe_();
			return finish;
		}

		@Override
		public int getBasemoney() { // 金钱的基数(在翻倍的时候用得上)
			_xdb_verify_unsafe_();
			return basemoney;
		}

		@Override
		public int getBasesmoney() { // 
			_xdb_verify_unsafe_();
			return basesmoney;
		}

		@Override
		public int getBaseexp() { // 
			_xdb_verify_unsafe_();
			return baseexp;
		}

		@Override
		public int getMsgid() { // 策划要求每个大转盘给的闲话id都不一样
			_xdb_verify_unsafe_();
			return msgid;
		}

		@Override
		public java.util.List<String> getMsgparas() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(msgparas);
		}

		public java.util.List<String> getMsgparasAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<String> msgparas;
			WheelInfo _o_ = WheelInfo.this;
		msgparas = new java.util.ArrayList<String>();
		msgparas.addAll(_o_.msgparas);
			return msgparas;
		}

		@Override
		public int getIndex() { // 真正中奖的那个item的下标
			_xdb_verify_unsafe_();
			return index;
		}

		@Override
		public java.util.List<xbean.WheelItem> getWheelitems() { // 一个大转盘所包含的格子
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(wheelitems);
		}

		public java.util.List<xbean.WheelItem> getWheelitemsAsData() { // 一个大转盘所包含的格子
			_xdb_verify_unsafe_();
			java.util.List<xbean.WheelItem> wheelitems;
			WheelInfo _o_ = WheelInfo.this;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem.Data(_v_));
			return wheelitems;
		}

		@Override
		public void setTime(long _v_) { // 答题的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNpcbaseid(int _v_) { // 大转盘可能有多个npc有服务,所以要有npc的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setServiceid(int _v_) { // 一个npc可以有多个大转盘,所以要有服务的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFinish(boolean _v_) { // 是否完成了这个大转盘
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBasemoney(int _v_) { // 金钱的基数(在翻倍的时候用得上)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBasesmoney(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBaseexp(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMsgid(int _v_) { // 策划要求每个大转盘给的闲话id都不一样
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIndex(int _v_) { // 真正中奖的那个item的下标
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
			return WheelInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WheelInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WheelInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WheelInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WheelInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WheelInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WheelInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WheelInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return WheelInfo.this.toString();
		}

	}

	public static final class Data implements xbean.WheelInfo {
		private long time; // 答题的时间
		private int npcbaseid; // 大转盘可能有多个npc有服务,所以要有npc的id
		private int serviceid; // 一个npc可以有多个大转盘,所以要有服务的id
		private boolean finish; // 是否完成了这个大转盘
		private int basemoney; // 金钱的基数(在翻倍的时候用得上)
		private int basesmoney; // 
		private int baseexp; // 
		private int msgid; // 策划要求每个大转盘给的闲话id都不一样
		private java.util.ArrayList<String> msgparas; // 
		private int index; // 真正中奖的那个item的下标
		private java.util.ArrayList<xbean.WheelItem> wheelitems; // 一个大转盘所包含的格子

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			msgparas = new java.util.ArrayList<String>();
			wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		}

		Data(xbean.WheelInfo _o1_) {
			if (_o1_ instanceof WheelInfo) assign((WheelInfo)_o1_);
			else if (_o1_ instanceof WheelInfo.Data) assign((WheelInfo.Data)_o1_);
			else if (_o1_ instanceof WheelInfo.Const) assign(((WheelInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WheelInfo _o_) {
			time = _o_.time;
			npcbaseid = _o_.npcbaseid;
			serviceid = _o_.serviceid;
			finish = _o_.finish;
			basemoney = _o_.basemoney;
			basesmoney = _o_.basesmoney;
			baseexp = _o_.baseexp;
			msgid = _o_.msgid;
			msgparas = new java.util.ArrayList<String>();
			msgparas.addAll(_o_.msgparas);
			index = _o_.index;
			wheelitems = new java.util.ArrayList<xbean.WheelItem>();
			for (xbean.WheelItem _v_ : _o_.wheelitems)
				wheelitems.add(new WheelItem.Data(_v_));
		}

		private void assign(WheelInfo.Data _o_) {
			time = _o_.time;
			npcbaseid = _o_.npcbaseid;
			serviceid = _o_.serviceid;
			finish = _o_.finish;
			basemoney = _o_.basemoney;
			basesmoney = _o_.basesmoney;
			baseexp = _o_.baseexp;
			msgid = _o_.msgid;
			msgparas = new java.util.ArrayList<String>();
			msgparas.addAll(_o_.msgparas);
			index = _o_.index;
			wheelitems = new java.util.ArrayList<xbean.WheelItem>();
			for (xbean.WheelItem _v_ : _o_.wheelitems)
				wheelitems.add(new WheelItem.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			_os_.marshal(npcbaseid);
			_os_.marshal(serviceid);
			_os_.marshal(finish);
			_os_.marshal(basemoney);
			_os_.marshal(basesmoney);
			_os_.marshal(baseexp);
			_os_.marshal(msgid);
			_os_.compact_uint32(msgparas.size());
			for (String _v_ : msgparas) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			_os_.marshal(index);
			_os_.compact_uint32(wheelitems.size());
			for (xbean.WheelItem _v_ : wheelitems) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			npcbaseid = _os_.unmarshal_int();
			serviceid = _os_.unmarshal_int();
			finish = _os_.unmarshal_boolean();
			basemoney = _os_.unmarshal_int();
			basesmoney = _os_.unmarshal_int();
			baseexp = _os_.unmarshal_int();
			msgid = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				msgparas.add(_v_);
			}
			index = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.WheelItem _v_ = xbean.Pod.newWheelItemData();
				_v_.unmarshal(_os_);
				wheelitems.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.WheelInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.WheelInfo toData() {
			return new Data(this);
		}

		public xbean.WheelInfo toBean() {
			return new WheelInfo(this, null, null);
		}

		@Override
		public xbean.WheelInfo toDataIf() {
			return this;
		}

		public xbean.WheelInfo toBeanIf() {
			return new WheelInfo(this, null, null);
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
		public long getTime() { // 答题的时间
			return time;
		}

		@Override
		public int getNpcbaseid() { // 大转盘可能有多个npc有服务,所以要有npc的id
			return npcbaseid;
		}

		@Override
		public int getServiceid() { // 一个npc可以有多个大转盘,所以要有服务的id
			return serviceid;
		}

		@Override
		public boolean getFinish() { // 是否完成了这个大转盘
			return finish;
		}

		@Override
		public int getBasemoney() { // 金钱的基数(在翻倍的时候用得上)
			return basemoney;
		}

		@Override
		public int getBasesmoney() { // 
			return basesmoney;
		}

		@Override
		public int getBaseexp() { // 
			return baseexp;
		}

		@Override
		public int getMsgid() { // 策划要求每个大转盘给的闲话id都不一样
			return msgid;
		}

		@Override
		public java.util.List<String> getMsgparas() { // 
			return msgparas;
		}

		@Override
		public java.util.List<String> getMsgparasAsData() { // 
			return msgparas;
		}

		@Override
		public int getIndex() { // 真正中奖的那个item的下标
			return index;
		}

		@Override
		public java.util.List<xbean.WheelItem> getWheelitems() { // 一个大转盘所包含的格子
			return wheelitems;
		}

		@Override
		public java.util.List<xbean.WheelItem> getWheelitemsAsData() { // 一个大转盘所包含的格子
			return wheelitems;
		}

		@Override
		public void setTime(long _v_) { // 答题的时间
			time = _v_;
		}

		@Override
		public void setNpcbaseid(int _v_) { // 大转盘可能有多个npc有服务,所以要有npc的id
			npcbaseid = _v_;
		}

		@Override
		public void setServiceid(int _v_) { // 一个npc可以有多个大转盘,所以要有服务的id
			serviceid = _v_;
		}

		@Override
		public void setFinish(boolean _v_) { // 是否完成了这个大转盘
			finish = _v_;
		}

		@Override
		public void setBasemoney(int _v_) { // 金钱的基数(在翻倍的时候用得上)
			basemoney = _v_;
		}

		@Override
		public void setBasesmoney(int _v_) { // 
			basesmoney = _v_;
		}

		@Override
		public void setBaseexp(int _v_) { // 
			baseexp = _v_;
		}

		@Override
		public void setMsgid(int _v_) { // 策划要求每个大转盘给的闲话id都不一样
			msgid = _v_;
		}

		@Override
		public void setIndex(int _v_) { // 真正中奖的那个item的下标
			index = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WheelInfo.Data)) return false;
			WheelInfo.Data _o_ = (WheelInfo.Data) _o1_;
			if (time != _o_.time) return false;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (serviceid != _o_.serviceid) return false;
			if (finish != _o_.finish) return false;
			if (basemoney != _o_.basemoney) return false;
			if (basesmoney != _o_.basesmoney) return false;
			if (baseexp != _o_.baseexp) return false;
			if (msgid != _o_.msgid) return false;
			if (!msgparas.equals(_o_.msgparas)) return false;
			if (index != _o_.index) return false;
			if (!wheelitems.equals(_o_.wheelitems)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += npcbaseid;
			_h_ += serviceid;
			_h_ += finish ? 1231 : 1237;
			_h_ += basemoney;
			_h_ += basesmoney;
			_h_ += baseexp;
			_h_ += msgid;
			_h_ += msgparas.hashCode();
			_h_ += index;
			_h_ += wheelitems.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(npcbaseid);
			_sb_.append(",");
			_sb_.append(serviceid);
			_sb_.append(",");
			_sb_.append(finish);
			_sb_.append(",");
			_sb_.append(basemoney);
			_sb_.append(",");
			_sb_.append(basesmoney);
			_sb_.append(",");
			_sb_.append(baseexp);
			_sb_.append(",");
			_sb_.append(msgid);
			_sb_.append(",");
			_sb_.append(msgparas);
			_sb_.append(",");
			_sb_.append(index);
			_sb_.append(",");
			_sb_.append(wheelitems);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
