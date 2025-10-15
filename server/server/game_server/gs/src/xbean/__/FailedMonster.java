
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FailedMonster extends mkdb.XBean implements xbean.FailedMonster {
	private int fighterid; // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
	private boolean ishost; // 主方or客方怪物
	private int monsterid; // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
	private int level; // 等级
	private int shape; // 造型Id
	private int fightnpctype; // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
	private int monstertype; // 怪物类型,普通,精英,宝宝,boss,公会boss
	private int result; // 结束时的结果，参考fire.pb.battle.ResultType

	@Override
	public void _reset_unsafe_() {
		fighterid = 0;
		ishost = false;
		monsterid = 0;
		level = 0;
		shape = 0;
		fightnpctype = 0;
		monstertype = 0;
		result = 0;
	}

	FailedMonster(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public FailedMonster() {
		this(0, null, null);
	}

	public FailedMonster(FailedMonster _o_) {
		this(_o_, null, null);
	}

	FailedMonster(xbean.FailedMonster _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FailedMonster) assign((FailedMonster)_o1_);
		else if (_o1_ instanceof FailedMonster.Data) assign((FailedMonster.Data)_o1_);
		else if (_o1_ instanceof FailedMonster.Const) assign(((FailedMonster.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FailedMonster _o_) {
		_o_._xdb_verify_unsafe_();
		fighterid = _o_.fighterid;
		ishost = _o_.ishost;
		monsterid = _o_.monsterid;
		level = _o_.level;
		shape = _o_.shape;
		fightnpctype = _o_.fightnpctype;
		monstertype = _o_.monstertype;
		result = _o_.result;
	}

	private void assign(FailedMonster.Data _o_) {
		fighterid = _o_.fighterid;
		ishost = _o_.ishost;
		monsterid = _o_.monsterid;
		level = _o_.level;
		shape = _o_.shape;
		fightnpctype = _o_.fightnpctype;
		monstertype = _o_.monstertype;
		result = _o_.result;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(fighterid);
		_os_.marshal(ishost);
		_os_.marshal(monsterid);
		_os_.marshal(level);
		_os_.marshal(shape);
		_os_.marshal(fightnpctype);
		_os_.marshal(monstertype);
		_os_.marshal(result);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		fighterid = _os_.unmarshal_int();
		ishost = _os_.unmarshal_boolean();
		monsterid = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		fightnpctype = _os_.unmarshal_int();
		monstertype = _os_.unmarshal_int();
		result = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.FailedMonster copy() {
		_xdb_verify_unsafe_();
		return new FailedMonster(this);
	}

	@Override
	public xbean.FailedMonster toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FailedMonster toBean() {
		_xdb_verify_unsafe_();
		return new FailedMonster(this); // same as copy()
	}

	@Override
	public xbean.FailedMonster toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FailedMonster toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getFighterid() { // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
		_xdb_verify_unsafe_();
		return fighterid;
	}

	@Override
	public boolean getIshost() { // 主方or客方怪物
		_xdb_verify_unsafe_();
		return ishost;
	}

	@Override
	public int getMonsterid() { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
		_xdb_verify_unsafe_();
		return monsterid;
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getShape() { // 造型Id
		_xdb_verify_unsafe_();
		return shape;
	}

	@Override
	public int getFightnpctype() { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
		_xdb_verify_unsafe_();
		return fightnpctype;
	}

	@Override
	public int getMonstertype() { // 怪物类型,普通,精英,宝宝,boss,公会boss
		_xdb_verify_unsafe_();
		return monstertype;
	}

	@Override
	public int getResult() { // 结束时的结果，参考fire.pb.battle.ResultType
		_xdb_verify_unsafe_();
		return result;
	}

	@Override
	public void setFighterid(int _v_) { // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fighterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fighterid) {
					public void rollback() { fighterid = _xdb_saved; }
				};}});
		fighterid = _v_;
	}

	@Override
	public void setIshost(boolean _v_) { // 主方or客方怪物
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ishost") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, ishost) {
					public void rollback() { ishost = _xdb_saved; }
				};}});
		ishost = _v_;
	}

	@Override
	public void setMonsterid(int _v_) { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monsterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, monsterid) {
					public void rollback() { monsterid = _xdb_saved; }
				};}});
		monsterid = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setShape(int _v_) { // 造型Id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shape") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shape) {
					public void rollback() { shape = _xdb_saved; }
				};}});
		shape = _v_;
	}

	@Override
	public void setFightnpctype(int _v_) { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fightnpctype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fightnpctype) {
					public void rollback() { fightnpctype = _xdb_saved; }
				};}});
		fightnpctype = _v_;
	}

	@Override
	public void setMonstertype(int _v_) { // 怪物类型,普通,精英,宝宝,boss,公会boss
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monstertype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, monstertype) {
					public void rollback() { monstertype = _xdb_saved; }
				};}});
		monstertype = _v_;
	}

	@Override
	public void setResult(int _v_) { // 结束时的结果，参考fire.pb.battle.ResultType
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "result") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, result) {
					public void rollback() { result = _xdb_saved; }
				};}});
		result = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FailedMonster _o_ = null;
		if ( _o1_ instanceof FailedMonster ) _o_ = (FailedMonster)_o1_;
		else if ( _o1_ instanceof FailedMonster.Const ) _o_ = ((FailedMonster.Const)_o1_).nThis();
		else return false;
		if (fighterid != _o_.fighterid) return false;
		if (ishost != _o_.ishost) return false;
		if (monsterid != _o_.monsterid) return false;
		if (level != _o_.level) return false;
		if (shape != _o_.shape) return false;
		if (fightnpctype != _o_.fightnpctype) return false;
		if (monstertype != _o_.monstertype) return false;
		if (result != _o_.result) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += fighterid;
		_h_ += ishost ? 1231 : 1237;
		_h_ += monsterid;
		_h_ += level;
		_h_ += shape;
		_h_ += fightnpctype;
		_h_ += monstertype;
		_h_ += result;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(fighterid);
		_sb_.append(",");
		_sb_.append(ishost);
		_sb_.append(",");
		_sb_.append(monsterid);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(shape);
		_sb_.append(",");
		_sb_.append(fightnpctype);
		_sb_.append(",");
		_sb_.append(monstertype);
		_sb_.append(",");
		_sb_.append(result);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ishost"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monsterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shape"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fightnpctype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monstertype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("result"));
		return lb;
	}

	private class Const implements xbean.FailedMonster {
		FailedMonster nThis() {
			return FailedMonster.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FailedMonster copy() {
			return FailedMonster.this.copy();
		}

		@Override
		public xbean.FailedMonster toData() {
			return FailedMonster.this.toData();
		}

		public xbean.FailedMonster toBean() {
			return FailedMonster.this.toBean();
		}

		@Override
		public xbean.FailedMonster toDataIf() {
			return FailedMonster.this.toDataIf();
		}

		public xbean.FailedMonster toBeanIf() {
			return FailedMonster.this.toBeanIf();
		}

		@Override
		public int getFighterid() { // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
			_xdb_verify_unsafe_();
			return fighterid;
		}

		@Override
		public boolean getIshost() { // 主方or客方怪物
			_xdb_verify_unsafe_();
			return ishost;
		}

		@Override
		public int getMonsterid() { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			_xdb_verify_unsafe_();
			return monsterid;
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getShape() { // 造型Id
			_xdb_verify_unsafe_();
			return shape;
		}

		@Override
		public int getFightnpctype() { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			_xdb_verify_unsafe_();
			return fightnpctype;
		}

		@Override
		public int getMonstertype() { // 怪物类型,普通,精英,宝宝,boss,公会boss
			_xdb_verify_unsafe_();
			return monstertype;
		}

		@Override
		public int getResult() { // 结束时的结果，参考fire.pb.battle.ResultType
			_xdb_verify_unsafe_();
			return result;
		}

		@Override
		public void setFighterid(int _v_) { // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIshost(boolean _v_) { // 主方or客方怪物
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonsterid(int _v_) { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShape(int _v_) { // 造型Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFightnpctype(int _v_) { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型,普通,精英,宝宝,boss,公会boss
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setResult(int _v_) { // 结束时的结果，参考fire.pb.battle.ResultType
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
			return FailedMonster.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FailedMonster.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FailedMonster.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FailedMonster.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FailedMonster.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FailedMonster.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FailedMonster.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FailedMonster.this.hashCode();
		}

		@Override
		public String toString() {
			return FailedMonster.this.toString();
		}

	}

	public static final class Data implements xbean.FailedMonster {
		private int fighterid; // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
		private boolean ishost; // 主方or客方怪物
		private int monsterid; // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
		private int level; // 等级
		private int shape; // 造型Id
		private int fightnpctype; // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
		private int monstertype; // 怪物类型,普通,精英,宝宝,boss,公会boss
		private int result; // 结束时的结果，参考fire.pb.battle.ResultType

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.FailedMonster _o1_) {
			if (_o1_ instanceof FailedMonster) assign((FailedMonster)_o1_);
			else if (_o1_ instanceof FailedMonster.Data) assign((FailedMonster.Data)_o1_);
			else if (_o1_ instanceof FailedMonster.Const) assign(((FailedMonster.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FailedMonster _o_) {
			fighterid = _o_.fighterid;
			ishost = _o_.ishost;
			monsterid = _o_.monsterid;
			level = _o_.level;
			shape = _o_.shape;
			fightnpctype = _o_.fightnpctype;
			monstertype = _o_.monstertype;
			result = _o_.result;
		}

		private void assign(FailedMonster.Data _o_) {
			fighterid = _o_.fighterid;
			ishost = _o_.ishost;
			monsterid = _o_.monsterid;
			level = _o_.level;
			shape = _o_.shape;
			fightnpctype = _o_.fightnpctype;
			monstertype = _o_.monstertype;
			result = _o_.result;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(fighterid);
			_os_.marshal(ishost);
			_os_.marshal(monsterid);
			_os_.marshal(level);
			_os_.marshal(shape);
			_os_.marshal(fightnpctype);
			_os_.marshal(monstertype);
			_os_.marshal(result);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			fighterid = _os_.unmarshal_int();
			ishost = _os_.unmarshal_boolean();
			monsterid = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			shape = _os_.unmarshal_int();
			fightnpctype = _os_.unmarshal_int();
			monstertype = _os_.unmarshal_int();
			result = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.FailedMonster copy() {
			return new Data(this);
		}

		@Override
		public xbean.FailedMonster toData() {
			return new Data(this);
		}

		public xbean.FailedMonster toBean() {
			return new FailedMonster(this, null, null);
		}

		@Override
		public xbean.FailedMonster toDataIf() {
			return this;
		}

		public xbean.FailedMonster toBeanIf() {
			return new FailedMonster(this, null, null);
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
		public int getFighterid() { // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
			return fighterid;
		}

		@Override
		public boolean getIshost() { // 主方or客方怪物
			return ishost;
		}

		@Override
		public int getMonsterid() { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			return monsterid;
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getShape() { // 造型Id
			return shape;
		}

		@Override
		public int getFightnpctype() { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			return fightnpctype;
		}

		@Override
		public int getMonstertype() { // 怪物类型,普通,精英,宝宝,boss,公会boss
			return monstertype;
		}

		@Override
		public int getResult() { // 结束时的结果，参考fire.pb.battle.ResultType
			return result;
		}

		@Override
		public void setFighterid(int _v_) { // 怪物在战场上时的战斗ID，注意，这个ID可能跟其他怪重复
			fighterid = _v_;
		}

		@Override
		public void setIshost(boolean _v_) { // 主方or客方怪物
			ishost = _v_;
		}

		@Override
		public void setMonsterid(int _v_) { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			monsterid = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setShape(int _v_) { // 造型Id
			shape = _v_;
		}

		@Override
		public void setFightnpctype(int _v_) { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			fightnpctype = _v_;
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型,普通,精英,宝宝,boss,公会boss
			monstertype = _v_;
		}

		@Override
		public void setResult(int _v_) { // 结束时的结果，参考fire.pb.battle.ResultType
			result = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FailedMonster.Data)) return false;
			FailedMonster.Data _o_ = (FailedMonster.Data) _o1_;
			if (fighterid != _o_.fighterid) return false;
			if (ishost != _o_.ishost) return false;
			if (monsterid != _o_.monsterid) return false;
			if (level != _o_.level) return false;
			if (shape != _o_.shape) return false;
			if (fightnpctype != _o_.fightnpctype) return false;
			if (monstertype != _o_.monstertype) return false;
			if (result != _o_.result) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += fighterid;
			_h_ += ishost ? 1231 : 1237;
			_h_ += monsterid;
			_h_ += level;
			_h_ += shape;
			_h_ += fightnpctype;
			_h_ += monstertype;
			_h_ += result;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(fighterid);
			_sb_.append(",");
			_sb_.append(ishost);
			_sb_.append(",");
			_sb_.append(monsterid);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(shape);
			_sb_.append(",");
			_sb_.append(fightnpctype);
			_sb_.append(",");
			_sb_.append(monstertype);
			_sb_.append(",");
			_sb_.append(result);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
