
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RollCardInfo extends mkdb.XBean implements xbean.RollCardInfo {
	private int serviceid; // 
	private int takeflag; // 0没有领取 1=提取过
	private int basemoney; // 
	private int basesmoney; // 
	private int baseexp; // 
	private int index; // the real card
	private java.util.ArrayList<xbean.WheelItem> wheelitems; // 4张卡

	@Override
	public void _reset_unsafe_() {
		serviceid = 0;
		takeflag = 0;
		basemoney = 0;
		basesmoney = 0;
		baseexp = 0;
		index = 0;
		wheelitems.clear();
	}

	RollCardInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
	}

	public RollCardInfo() {
		this(0, null, null);
	}

	public RollCardInfo(RollCardInfo _o_) {
		this(_o_, null, null);
	}

	RollCardInfo(xbean.RollCardInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RollCardInfo) assign((RollCardInfo)_o1_);
		else if (_o1_ instanceof RollCardInfo.Data) assign((RollCardInfo.Data)_o1_);
		else if (_o1_ instanceof RollCardInfo.Const) assign(((RollCardInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RollCardInfo _o_) {
		_o_._xdb_verify_unsafe_();
		serviceid = _o_.serviceid;
		takeflag = _o_.takeflag;
		basemoney = _o_.basemoney;
		basesmoney = _o_.basesmoney;
		baseexp = _o_.baseexp;
		index = _o_.index;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem(_v_, this, "wheelitems"));
	}

	private void assign(RollCardInfo.Data _o_) {
		serviceid = _o_.serviceid;
		takeflag = _o_.takeflag;
		basemoney = _o_.basemoney;
		basesmoney = _o_.basesmoney;
		baseexp = _o_.baseexp;
		index = _o_.index;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem(_v_, this, "wheelitems"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(serviceid);
		_os_.marshal(takeflag);
		_os_.marshal(basemoney);
		_os_.marshal(basesmoney);
		_os_.marshal(baseexp);
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
		serviceid = _os_.unmarshal_int();
		takeflag = _os_.unmarshal_int();
		basemoney = _os_.unmarshal_int();
		basesmoney = _os_.unmarshal_int();
		baseexp = _os_.unmarshal_int();
		index = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.WheelItem _v_ = new WheelItem(0, this, "wheelitems");
			_v_.unmarshal(_os_);
			wheelitems.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RollCardInfo copy() {
		_xdb_verify_unsafe_();
		return new RollCardInfo(this);
	}

	@Override
	public xbean.RollCardInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RollCardInfo toBean() {
		_xdb_verify_unsafe_();
		return new RollCardInfo(this); // same as copy()
	}

	@Override
	public xbean.RollCardInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RollCardInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getServiceid() { // 
		_xdb_verify_unsafe_();
		return serviceid;
	}

	@Override
	public int getTakeflag() { // 0没有领取 1=提取过
		_xdb_verify_unsafe_();
		return takeflag;
	}

	@Override
	public int getBasemoney() { // 
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
	public int getIndex() { // the real card
		_xdb_verify_unsafe_();
		return index;
	}

	@Override
	public java.util.List<xbean.WheelItem> getWheelitems() { // 4张卡
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "wheelitems"), wheelitems);
	}

	public java.util.List<xbean.WheelItem> getWheelitemsAsData() { // 4张卡
		_xdb_verify_unsafe_();
		java.util.List<xbean.WheelItem> wheelitems;
		RollCardInfo _o_ = this;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem.Data(_v_));
		return wheelitems;
	}

	@Override
	public void setServiceid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "serviceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, serviceid) {
					public void rollback() { serviceid = _xdb_saved; }
				};}});
		serviceid = _v_;
	}

	@Override
	public void setTakeflag(int _v_) { // 0没有领取 1=提取过
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "takeflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, takeflag) {
					public void rollback() { takeflag = _xdb_saved; }
				};}});
		takeflag = _v_;
	}

	@Override
	public void setBasemoney(int _v_) { // 
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
	public void setIndex(int _v_) { // the real card
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
		RollCardInfo _o_ = null;
		if ( _o1_ instanceof RollCardInfo ) _o_ = (RollCardInfo)_o1_;
		else if ( _o1_ instanceof RollCardInfo.Const ) _o_ = ((RollCardInfo.Const)_o1_).nThis();
		else return false;
		if (serviceid != _o_.serviceid) return false;
		if (takeflag != _o_.takeflag) return false;
		if (basemoney != _o_.basemoney) return false;
		if (basesmoney != _o_.basesmoney) return false;
		if (baseexp != _o_.baseexp) return false;
		if (index != _o_.index) return false;
		if (!wheelitems.equals(_o_.wheelitems)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += serviceid;
		_h_ += takeflag;
		_h_ += basemoney;
		_h_ += basesmoney;
		_h_ += baseexp;
		_h_ += index;
		_h_ += wheelitems.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(serviceid);
		_sb_.append(",");
		_sb_.append(takeflag);
		_sb_.append(",");
		_sb_.append(basemoney);
		_sb_.append(",");
		_sb_.append(basesmoney);
		_sb_.append(",");
		_sb_.append(baseexp);
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
		lb.add(new mkdb.logs.ListenableChanged().setVarName("serviceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("takeflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basemoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basesmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("baseexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("index"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("wheelitems"));
		return lb;
	}

	private class Const implements xbean.RollCardInfo {
		RollCardInfo nThis() {
			return RollCardInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RollCardInfo copy() {
			return RollCardInfo.this.copy();
		}

		@Override
		public xbean.RollCardInfo toData() {
			return RollCardInfo.this.toData();
		}

		public xbean.RollCardInfo toBean() {
			return RollCardInfo.this.toBean();
		}

		@Override
		public xbean.RollCardInfo toDataIf() {
			return RollCardInfo.this.toDataIf();
		}

		public xbean.RollCardInfo toBeanIf() {
			return RollCardInfo.this.toBeanIf();
		}

		@Override
		public int getServiceid() { // 
			_xdb_verify_unsafe_();
			return serviceid;
		}

		@Override
		public int getTakeflag() { // 0没有领取 1=提取过
			_xdb_verify_unsafe_();
			return takeflag;
		}

		@Override
		public int getBasemoney() { // 
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
		public int getIndex() { // the real card
			_xdb_verify_unsafe_();
			return index;
		}

		@Override
		public java.util.List<xbean.WheelItem> getWheelitems() { // 4张卡
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(wheelitems);
		}

		public java.util.List<xbean.WheelItem> getWheelitemsAsData() { // 4张卡
			_xdb_verify_unsafe_();
			java.util.List<xbean.WheelItem> wheelitems;
			RollCardInfo _o_ = RollCardInfo.this;
		wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		for (xbean.WheelItem _v_ : _o_.wheelitems)
			wheelitems.add(new WheelItem.Data(_v_));
			return wheelitems;
		}

		@Override
		public void setServiceid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTakeflag(int _v_) { // 0没有领取 1=提取过
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBasemoney(int _v_) { // 
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
		public void setIndex(int _v_) { // the real card
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
			return RollCardInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RollCardInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RollCardInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RollCardInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RollCardInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RollCardInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RollCardInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RollCardInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RollCardInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RollCardInfo {
		private int serviceid; // 
		private int takeflag; // 0没有领取 1=提取过
		private int basemoney; // 
		private int basesmoney; // 
		private int baseexp; // 
		private int index; // the real card
		private java.util.ArrayList<xbean.WheelItem> wheelitems; // 4张卡

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			wheelitems = new java.util.ArrayList<xbean.WheelItem>();
		}

		Data(xbean.RollCardInfo _o1_) {
			if (_o1_ instanceof RollCardInfo) assign((RollCardInfo)_o1_);
			else if (_o1_ instanceof RollCardInfo.Data) assign((RollCardInfo.Data)_o1_);
			else if (_o1_ instanceof RollCardInfo.Const) assign(((RollCardInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RollCardInfo _o_) {
			serviceid = _o_.serviceid;
			takeflag = _o_.takeflag;
			basemoney = _o_.basemoney;
			basesmoney = _o_.basesmoney;
			baseexp = _o_.baseexp;
			index = _o_.index;
			wheelitems = new java.util.ArrayList<xbean.WheelItem>();
			for (xbean.WheelItem _v_ : _o_.wheelitems)
				wheelitems.add(new WheelItem.Data(_v_));
		}

		private void assign(RollCardInfo.Data _o_) {
			serviceid = _o_.serviceid;
			takeflag = _o_.takeflag;
			basemoney = _o_.basemoney;
			basesmoney = _o_.basesmoney;
			baseexp = _o_.baseexp;
			index = _o_.index;
			wheelitems = new java.util.ArrayList<xbean.WheelItem>();
			for (xbean.WheelItem _v_ : _o_.wheelitems)
				wheelitems.add(new WheelItem.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(serviceid);
			_os_.marshal(takeflag);
			_os_.marshal(basemoney);
			_os_.marshal(basesmoney);
			_os_.marshal(baseexp);
			_os_.marshal(index);
			_os_.compact_uint32(wheelitems.size());
			for (xbean.WheelItem _v_ : wheelitems) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			serviceid = _os_.unmarshal_int();
			takeflag = _os_.unmarshal_int();
			basemoney = _os_.unmarshal_int();
			basesmoney = _os_.unmarshal_int();
			baseexp = _os_.unmarshal_int();
			index = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.WheelItem _v_ = xbean.Pod.newWheelItemData();
				_v_.unmarshal(_os_);
				wheelitems.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RollCardInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RollCardInfo toData() {
			return new Data(this);
		}

		public xbean.RollCardInfo toBean() {
			return new RollCardInfo(this, null, null);
		}

		@Override
		public xbean.RollCardInfo toDataIf() {
			return this;
		}

		public xbean.RollCardInfo toBeanIf() {
			return new RollCardInfo(this, null, null);
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
		public int getServiceid() { // 
			return serviceid;
		}

		@Override
		public int getTakeflag() { // 0没有领取 1=提取过
			return takeflag;
		}

		@Override
		public int getBasemoney() { // 
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
		public int getIndex() { // the real card
			return index;
		}

		@Override
		public java.util.List<xbean.WheelItem> getWheelitems() { // 4张卡
			return wheelitems;
		}

		@Override
		public java.util.List<xbean.WheelItem> getWheelitemsAsData() { // 4张卡
			return wheelitems;
		}

		@Override
		public void setServiceid(int _v_) { // 
			serviceid = _v_;
		}

		@Override
		public void setTakeflag(int _v_) { // 0没有领取 1=提取过
			takeflag = _v_;
		}

		@Override
		public void setBasemoney(int _v_) { // 
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
		public void setIndex(int _v_) { // the real card
			index = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RollCardInfo.Data)) return false;
			RollCardInfo.Data _o_ = (RollCardInfo.Data) _o1_;
			if (serviceid != _o_.serviceid) return false;
			if (takeflag != _o_.takeflag) return false;
			if (basemoney != _o_.basemoney) return false;
			if (basesmoney != _o_.basesmoney) return false;
			if (baseexp != _o_.baseexp) return false;
			if (index != _o_.index) return false;
			if (!wheelitems.equals(_o_.wheelitems)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += serviceid;
			_h_ += takeflag;
			_h_ += basemoney;
			_h_ += basesmoney;
			_h_ += baseexp;
			_h_ += index;
			_h_ += wheelitems.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(serviceid);
			_sb_.append(",");
			_sb_.append(takeflag);
			_sb_.append(",");
			_sb_.append(basemoney);
			_sb_.append(",");
			_sb_.append(basesmoney);
			_sb_.append(",");
			_sb_.append(baseexp);
			_sb_.append(",");
			_sb_.append(index);
			_sb_.append(",");
			_sb_.append(wheelitems);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
