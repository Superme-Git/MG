
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SkillRole extends mkdb.XBean implements xbean.SkillRole {
	private java.util.HashMap<Integer, Integer> skillbases; // key = skillbase id; value = skillbase level
	private java.util.HashMap<Integer, Integer> inborns; // key = inborn id; value = inborn level
	private long lastdegradetime; // 上次穴位降级时间，一天只能降级一次
	private java.util.HashMap<Integer, xbean.AssistSkill> assistskills; // 辅助技能
	private java.util.HashMap<Integer, xbean.PracticeSkill> practiceskill; // 修炼技能 by changhao
	private java.util.HashMap<Integer, xbean.LiveSkill> liveskill; // 生活技能 by changhao
	private java.util.LinkedList<xbean.SpecialSkill> specialskills; // 装备提供的特技特效
	private java.util.HashMap<Integer, Integer> extskills; // key = extskill id; value = extskill level
	private java.util.HashMap<Integer, Integer> skillsequence; // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
	private java.util.HashMap<Integer, Long> lastusedtime; // 上一次使用技能的时间，目前只用在结拜拉人技能里

	@Override
	public void _reset_unsafe_() {
		skillbases.clear();
		inborns.clear();
		lastdegradetime = 0L;
		assistskills.clear();
		practiceskill.clear();
		liveskill.clear();
		specialskills.clear();
		extskills.clear();
		skillsequence.clear();
		lastusedtime.clear();
	}

	SkillRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		skillbases = new java.util.HashMap<Integer, Integer>();
		inborns = new java.util.HashMap<Integer, Integer>();
		assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
		practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
		liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
		specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
		extskills = new java.util.HashMap<Integer, Integer>();
		skillsequence = new java.util.HashMap<Integer, Integer>();
		lastusedtime = new java.util.HashMap<Integer, Long>();
	}

	public SkillRole() {
		this(0, null, null);
	}

	public SkillRole(SkillRole _o_) {
		this(_o_, null, null);
	}

	SkillRole(xbean.SkillRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SkillRole) assign((SkillRole)_o1_);
		else if (_o1_ instanceof SkillRole.Data) assign((SkillRole.Data)_o1_);
		else if (_o1_ instanceof SkillRole.Const) assign(((SkillRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SkillRole _o_) {
		_o_._xdb_verify_unsafe_();
		skillbases = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillbases.entrySet())
			skillbases.put(_e_.getKey(), _e_.getValue());
		inborns = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inborns.entrySet())
			inborns.put(_e_.getKey(), _e_.getValue());
		lastdegradetime = _o_.lastdegradetime;
		assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
		for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : _o_.assistskills.entrySet())
			assistskills.put(_e_.getKey(), new AssistSkill(_e_.getValue(), this, "assistskills"));
		practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
		for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : _o_.practiceskill.entrySet())
			practiceskill.put(_e_.getKey(), new PracticeSkill(_e_.getValue(), this, "practiceskill"));
		liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskill.entrySet())
			liveskill.put(_e_.getKey(), new LiveSkill(_e_.getValue(), this, "liveskill"));
		specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
		for (xbean.SpecialSkill _v_ : _o_.specialskills)
			specialskills.add(new SpecialSkill(_v_, this, "specialskills"));
		extskills = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extskills.entrySet())
			extskills.put(_e_.getKey(), _e_.getValue());
		skillsequence = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillsequence.entrySet())
			skillsequence.put(_e_.getKey(), _e_.getValue());
		lastusedtime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lastusedtime.entrySet())
			lastusedtime.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(SkillRole.Data _o_) {
		skillbases = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillbases.entrySet())
			skillbases.put(_e_.getKey(), _e_.getValue());
		inborns = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inborns.entrySet())
			inborns.put(_e_.getKey(), _e_.getValue());
		lastdegradetime = _o_.lastdegradetime;
		assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
		for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : _o_.assistskills.entrySet())
			assistskills.put(_e_.getKey(), new AssistSkill(_e_.getValue(), this, "assistskills"));
		practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
		for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : _o_.practiceskill.entrySet())
			practiceskill.put(_e_.getKey(), new PracticeSkill(_e_.getValue(), this, "practiceskill"));
		liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskill.entrySet())
			liveskill.put(_e_.getKey(), new LiveSkill(_e_.getValue(), this, "liveskill"));
		specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
		for (xbean.SpecialSkill _v_ : _o_.specialskills)
			specialskills.add(new SpecialSkill(_v_, this, "specialskills"));
		extskills = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extskills.entrySet())
			extskills.put(_e_.getKey(), _e_.getValue());
		skillsequence = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillsequence.entrySet())
			skillsequence.put(_e_.getKey(), _e_.getValue());
		lastusedtime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lastusedtime.entrySet())
			lastusedtime.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(skillbases.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : skillbases.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(inborns.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : inborns.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(lastdegradetime);
		_os_.compact_uint32(assistskills.size());
		for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : assistskills.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(practiceskill.size());
		for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : practiceskill.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(liveskill.size());
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : liveskill.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(specialskills.size());
		for (xbean.SpecialSkill _v_ : specialskills) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(extskills.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : extskills.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(skillsequence.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : skillsequence.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(lastusedtime.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : lastusedtime.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				skillbases = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				skillbases.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				inborns = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				inborns.put(_k_, _v_);
			}
		}
		lastdegradetime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.AssistSkill _v_ = new AssistSkill(0, this, "assistskills");
				_v_.unmarshal(_os_);
				assistskills.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.PracticeSkill _v_ = new PracticeSkill(0, this, "practiceskill");
				_v_.unmarshal(_os_);
				practiceskill.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.LiveSkill _v_ = new LiveSkill(0, this, "liveskill");
				_v_.unmarshal(_os_);
				liveskill.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.SpecialSkill _v_ = new SpecialSkill(0, this, "specialskills");
			_v_.unmarshal(_os_);
			specialskills.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				extskills = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				extskills.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				skillsequence = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				skillsequence.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				lastusedtime = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				lastusedtime.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.SkillRole copy() {
		_xdb_verify_unsafe_();
		return new SkillRole(this);
	}

	@Override
	public xbean.SkillRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SkillRole toBean() {
		_xdb_verify_unsafe_();
		return new SkillRole(this); // same as copy()
	}

	@Override
	public xbean.SkillRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SkillRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getSkillbases() { // key = skillbase id; value = skillbase level
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "skillbases"), skillbases);
	}

	@Override
	public java.util.Map<Integer, Integer> getSkillbasesAsData() { // key = skillbase id; value = skillbase level
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> skillbases;
		SkillRole _o_ = this;
		skillbases = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillbases.entrySet())
			skillbases.put(_e_.getKey(), _e_.getValue());
		return skillbases;
	}

	@Override
	public java.util.Map<Integer, Integer> getInborns() { // key = inborn id; value = inborn level
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "inborns"), inborns);
	}

	@Override
	public java.util.Map<Integer, Integer> getInbornsAsData() { // key = inborn id; value = inborn level
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> inborns;
		SkillRole _o_ = this;
		inborns = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inborns.entrySet())
			inborns.put(_e_.getKey(), _e_.getValue());
		return inborns;
	}

	@Override
	public long getLastdegradetime() { // 上次穴位降级时间，一天只能降级一次
		_xdb_verify_unsafe_();
		return lastdegradetime;
	}

	@Override
	public java.util.Map<Integer, xbean.AssistSkill> getAssistskills() { // 辅助技能
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "assistskills"), assistskills);
	}

	@Override
	public java.util.Map<Integer, xbean.AssistSkill> getAssistskillsAsData() { // 辅助技能
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.AssistSkill> assistskills;
		SkillRole _o_ = this;
		assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
		for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : _o_.assistskills.entrySet())
			assistskills.put(_e_.getKey(), new AssistSkill.Data(_e_.getValue()));
		return assistskills;
	}

	@Override
	public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskill() { // 修炼技能 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "practiceskill"), practiceskill);
	}

	@Override
	public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskillAsData() { // 修炼技能 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.PracticeSkill> practiceskill;
		SkillRole _o_ = this;
		practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
		for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : _o_.practiceskill.entrySet())
			practiceskill.put(_e_.getKey(), new PracticeSkill.Data(_e_.getValue()));
		return practiceskill;
	}

	@Override
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskill() { // 生活技能 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "liveskill"), liveskill);
	}

	@Override
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskillAsData() { // 生活技能 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.LiveSkill> liveskill;
		SkillRole _o_ = this;
		liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
		for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskill.entrySet())
			liveskill.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
		return liveskill;
	}

	@Override
	public java.util.List<xbean.SpecialSkill> getSpecialskills() { // 装备提供的特技特效
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "specialskills"), specialskills);
	}

	public java.util.List<xbean.SpecialSkill> getSpecialskillsAsData() { // 装备提供的特技特效
		_xdb_verify_unsafe_();
		java.util.List<xbean.SpecialSkill> specialskills;
		SkillRole _o_ = this;
		specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
		for (xbean.SpecialSkill _v_ : _o_.specialskills)
			specialskills.add(new SpecialSkill.Data(_v_));
		return specialskills;
	}

	@Override
	public java.util.Map<Integer, Integer> getExtskills() { // key = extskill id; value = extskill level
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "extskills"), extskills);
	}

	@Override
	public java.util.Map<Integer, Integer> getExtskillsAsData() { // key = extskill id; value = extskill level
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> extskills;
		SkillRole _o_ = this;
		extskills = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extskills.entrySet())
			extskills.put(_e_.getKey(), _e_.getValue());
		return extskills;
	}

	@Override
	public java.util.Map<Integer, Integer> getSkillsequence() { // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "skillsequence"), skillsequence);
	}

	@Override
	public java.util.Map<Integer, Integer> getSkillsequenceAsData() { // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> skillsequence;
		SkillRole _o_ = this;
		skillsequence = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillsequence.entrySet())
			skillsequence.put(_e_.getKey(), _e_.getValue());
		return skillsequence;
	}

	@Override
	public java.util.Map<Integer, Long> getLastusedtime() { // 上一次使用技能的时间，目前只用在结拜拉人技能里
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "lastusedtime"), lastusedtime);
	}

	@Override
	public java.util.Map<Integer, Long> getLastusedtimeAsData() { // 上一次使用技能的时间，目前只用在结拜拉人技能里
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> lastusedtime;
		SkillRole _o_ = this;
		lastusedtime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lastusedtime.entrySet())
			lastusedtime.put(_e_.getKey(), _e_.getValue());
		return lastusedtime;
	}

	@Override
	public void setLastdegradetime(long _v_) { // 上次穴位降级时间，一天只能降级一次
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastdegradetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastdegradetime) {
					public void rollback() { lastdegradetime = _xdb_saved; }
				};}});
		lastdegradetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SkillRole _o_ = null;
		if ( _o1_ instanceof SkillRole ) _o_ = (SkillRole)_o1_;
		else if ( _o1_ instanceof SkillRole.Const ) _o_ = ((SkillRole.Const)_o1_).nThis();
		else return false;
		if (!skillbases.equals(_o_.skillbases)) return false;
		if (!inborns.equals(_o_.inborns)) return false;
		if (lastdegradetime != _o_.lastdegradetime) return false;
		if (!assistskills.equals(_o_.assistskills)) return false;
		if (!practiceskill.equals(_o_.practiceskill)) return false;
		if (!liveskill.equals(_o_.liveskill)) return false;
		if (!specialskills.equals(_o_.specialskills)) return false;
		if (!extskills.equals(_o_.extskills)) return false;
		if (!skillsequence.equals(_o_.skillsequence)) return false;
		if (!lastusedtime.equals(_o_.lastusedtime)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += skillbases.hashCode();
		_h_ += inborns.hashCode();
		_h_ += lastdegradetime;
		_h_ += assistskills.hashCode();
		_h_ += practiceskill.hashCode();
		_h_ += liveskill.hashCode();
		_h_ += specialskills.hashCode();
		_h_ += extskills.hashCode();
		_h_ += skillsequence.hashCode();
		_h_ += lastusedtime.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skillbases);
		_sb_.append(",");
		_sb_.append(inborns);
		_sb_.append(",");
		_sb_.append(lastdegradetime);
		_sb_.append(",");
		_sb_.append(assistskills);
		_sb_.append(",");
		_sb_.append(practiceskill);
		_sb_.append(",");
		_sb_.append(liveskill);
		_sb_.append(",");
		_sb_.append(specialskills);
		_sb_.append(",");
		_sb_.append(extskills);
		_sb_.append(",");
		_sb_.append(skillsequence);
		_sb_.append(",");
		_sb_.append(lastusedtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("skillbases"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("inborns"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastdegradetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("assistskills"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("practiceskill"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("liveskill"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("specialskills"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("extskills"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("skillsequence"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("lastusedtime"));
		return lb;
	}

	private class Const implements xbean.SkillRole {
		SkillRole nThis() {
			return SkillRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SkillRole copy() {
			return SkillRole.this.copy();
		}

		@Override
		public xbean.SkillRole toData() {
			return SkillRole.this.toData();
		}

		public xbean.SkillRole toBean() {
			return SkillRole.this.toBean();
		}

		@Override
		public xbean.SkillRole toDataIf() {
			return SkillRole.this.toDataIf();
		}

		public xbean.SkillRole toBeanIf() {
			return SkillRole.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillbases() { // key = skillbase id; value = skillbase level
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(skillbases);
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillbasesAsData() { // key = skillbase id; value = skillbase level
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> skillbases;
			SkillRole _o_ = SkillRole.this;
			skillbases = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillbases.entrySet())
				skillbases.put(_e_.getKey(), _e_.getValue());
			return skillbases;
		}

		@Override
		public java.util.Map<Integer, Integer> getInborns() { // key = inborn id; value = inborn level
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(inborns);
		}

		@Override
		public java.util.Map<Integer, Integer> getInbornsAsData() { // key = inborn id; value = inborn level
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> inborns;
			SkillRole _o_ = SkillRole.this;
			inborns = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inborns.entrySet())
				inborns.put(_e_.getKey(), _e_.getValue());
			return inborns;
		}

		@Override
		public long getLastdegradetime() { // 上次穴位降级时间，一天只能降级一次
			_xdb_verify_unsafe_();
			return lastdegradetime;
		}

		@Override
		public java.util.Map<Integer, xbean.AssistSkill> getAssistskills() { // 辅助技能
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(assistskills);
		}

		@Override
		public java.util.Map<Integer, xbean.AssistSkill> getAssistskillsAsData() { // 辅助技能
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.AssistSkill> assistskills;
			SkillRole _o_ = SkillRole.this;
			assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
			for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : _o_.assistskills.entrySet())
				assistskills.put(_e_.getKey(), new AssistSkill.Data(_e_.getValue()));
			return assistskills;
		}

		@Override
		public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskill() { // 修炼技能 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(practiceskill);
		}

		@Override
		public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskillAsData() { // 修炼技能 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.PracticeSkill> practiceskill;
			SkillRole _o_ = SkillRole.this;
			practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
			for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : _o_.practiceskill.entrySet())
				practiceskill.put(_e_.getKey(), new PracticeSkill.Data(_e_.getValue()));
			return practiceskill;
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskill() { // 生活技能 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(liveskill);
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskillAsData() { // 生活技能 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.LiveSkill> liveskill;
			SkillRole _o_ = SkillRole.this;
			liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskill.entrySet())
				liveskill.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
			return liveskill;
		}

		@Override
		public java.util.List<xbean.SpecialSkill> getSpecialskills() { // 装备提供的特技特效
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(specialskills);
		}

		public java.util.List<xbean.SpecialSkill> getSpecialskillsAsData() { // 装备提供的特技特效
			_xdb_verify_unsafe_();
			java.util.List<xbean.SpecialSkill> specialskills;
			SkillRole _o_ = SkillRole.this;
		specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
		for (xbean.SpecialSkill _v_ : _o_.specialskills)
			specialskills.add(new SpecialSkill.Data(_v_));
			return specialskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getExtskills() { // key = extskill id; value = extskill level
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(extskills);
		}

		@Override
		public java.util.Map<Integer, Integer> getExtskillsAsData() { // key = extskill id; value = extskill level
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> extskills;
			SkillRole _o_ = SkillRole.this;
			extskills = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extskills.entrySet())
				extskills.put(_e_.getKey(), _e_.getValue());
			return extskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillsequence() { // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(skillsequence);
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillsequenceAsData() { // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> skillsequence;
			SkillRole _o_ = SkillRole.this;
			skillsequence = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillsequence.entrySet())
				skillsequence.put(_e_.getKey(), _e_.getValue());
			return skillsequence;
		}

		@Override
		public java.util.Map<Integer, Long> getLastusedtime() { // 上一次使用技能的时间，目前只用在结拜拉人技能里
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(lastusedtime);
		}

		@Override
		public java.util.Map<Integer, Long> getLastusedtimeAsData() { // 上一次使用技能的时间，目前只用在结拜拉人技能里
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> lastusedtime;
			SkillRole _o_ = SkillRole.this;
			lastusedtime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lastusedtime.entrySet())
				lastusedtime.put(_e_.getKey(), _e_.getValue());
			return lastusedtime;
		}

		@Override
		public void setLastdegradetime(long _v_) { // 上次穴位降级时间，一天只能降级一次
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
			return SkillRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SkillRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SkillRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SkillRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SkillRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SkillRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SkillRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SkillRole.this.hashCode();
		}

		@Override
		public String toString() {
			return SkillRole.this.toString();
		}

	}

	public static final class Data implements xbean.SkillRole {
		private java.util.HashMap<Integer, Integer> skillbases; // key = skillbase id; value = skillbase level
		private java.util.HashMap<Integer, Integer> inborns; // key = inborn id; value = inborn level
		private long lastdegradetime; // 上次穴位降级时间，一天只能降级一次
		private java.util.HashMap<Integer, xbean.AssistSkill> assistskills; // 辅助技能
		private java.util.HashMap<Integer, xbean.PracticeSkill> practiceskill; // 修炼技能 by changhao
		private java.util.HashMap<Integer, xbean.LiveSkill> liveskill; // 生活技能 by changhao
		private java.util.LinkedList<xbean.SpecialSkill> specialskills; // 装备提供的特技特效
		private java.util.HashMap<Integer, Integer> extskills; // key = extskill id; value = extskill level
		private java.util.HashMap<Integer, Integer> skillsequence; // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
		private java.util.HashMap<Integer, Long> lastusedtime; // 上一次使用技能的时间，目前只用在结拜拉人技能里

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			skillbases = new java.util.HashMap<Integer, Integer>();
			inborns = new java.util.HashMap<Integer, Integer>();
			assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
			practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
			liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
			specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
			extskills = new java.util.HashMap<Integer, Integer>();
			skillsequence = new java.util.HashMap<Integer, Integer>();
			lastusedtime = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.SkillRole _o1_) {
			if (_o1_ instanceof SkillRole) assign((SkillRole)_o1_);
			else if (_o1_ instanceof SkillRole.Data) assign((SkillRole.Data)_o1_);
			else if (_o1_ instanceof SkillRole.Const) assign(((SkillRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SkillRole _o_) {
			skillbases = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillbases.entrySet())
				skillbases.put(_e_.getKey(), _e_.getValue());
			inborns = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inborns.entrySet())
				inborns.put(_e_.getKey(), _e_.getValue());
			lastdegradetime = _o_.lastdegradetime;
			assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
			for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : _o_.assistskills.entrySet())
				assistskills.put(_e_.getKey(), new AssistSkill.Data(_e_.getValue()));
			practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
			for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : _o_.practiceskill.entrySet())
				practiceskill.put(_e_.getKey(), new PracticeSkill.Data(_e_.getValue()));
			liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskill.entrySet())
				liveskill.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
			specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
			for (xbean.SpecialSkill _v_ : _o_.specialskills)
				specialskills.add(new SpecialSkill.Data(_v_));
			extskills = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extskills.entrySet())
				extskills.put(_e_.getKey(), _e_.getValue());
			skillsequence = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillsequence.entrySet())
				skillsequence.put(_e_.getKey(), _e_.getValue());
			lastusedtime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lastusedtime.entrySet())
				lastusedtime.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(SkillRole.Data _o_) {
			skillbases = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillbases.entrySet())
				skillbases.put(_e_.getKey(), _e_.getValue());
			inborns = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.inborns.entrySet())
				inborns.put(_e_.getKey(), _e_.getValue());
			lastdegradetime = _o_.lastdegradetime;
			assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>();
			for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : _o_.assistskills.entrySet())
				assistskills.put(_e_.getKey(), new AssistSkill.Data(_e_.getValue()));
			practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>();
			for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : _o_.practiceskill.entrySet())
				practiceskill.put(_e_.getKey(), new PracticeSkill.Data(_e_.getValue()));
			liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>();
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : _o_.liveskill.entrySet())
				liveskill.put(_e_.getKey(), new LiveSkill.Data(_e_.getValue()));
			specialskills = new java.util.LinkedList<xbean.SpecialSkill>();
			for (xbean.SpecialSkill _v_ : _o_.specialskills)
				specialskills.add(new SpecialSkill.Data(_v_));
			extskills = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extskills.entrySet())
				extskills.put(_e_.getKey(), _e_.getValue());
			skillsequence = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.skillsequence.entrySet())
				skillsequence.put(_e_.getKey(), _e_.getValue());
			lastusedtime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lastusedtime.entrySet())
				lastusedtime.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(skillbases.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : skillbases.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(inborns.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : inborns.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(lastdegradetime);
			_os_.compact_uint32(assistskills.size());
			for (java.util.Map.Entry<Integer, xbean.AssistSkill> _e_ : assistskills.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(practiceskill.size());
			for (java.util.Map.Entry<Integer, xbean.PracticeSkill> _e_ : practiceskill.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(liveskill.size());
			for (java.util.Map.Entry<Integer, xbean.LiveSkill> _e_ : liveskill.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(specialskills.size());
			for (xbean.SpecialSkill _v_ : specialskills) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(extskills.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : extskills.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(skillsequence.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : skillsequence.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(lastusedtime.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : lastusedtime.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					skillbases = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					skillbases.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					inborns = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					inborns.put(_k_, _v_);
				}
			}
			lastdegradetime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					assistskills = new java.util.HashMap<Integer, xbean.AssistSkill>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.AssistSkill _v_ = xbean.Pod.newAssistSkillData();
					_v_.unmarshal(_os_);
					assistskills.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					practiceskill = new java.util.HashMap<Integer, xbean.PracticeSkill>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.PracticeSkill _v_ = xbean.Pod.newPracticeSkillData();
					_v_.unmarshal(_os_);
					practiceskill.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					liveskill = new java.util.HashMap<Integer, xbean.LiveSkill>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.LiveSkill _v_ = xbean.Pod.newLiveSkillData();
					_v_.unmarshal(_os_);
					liveskill.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.SpecialSkill _v_ = xbean.Pod.newSpecialSkillData();
				_v_.unmarshal(_os_);
				specialskills.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					extskills = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					extskills.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					skillsequence = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					skillsequence.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					lastusedtime = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					lastusedtime.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.SkillRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.SkillRole toData() {
			return new Data(this);
		}

		public xbean.SkillRole toBean() {
			return new SkillRole(this, null, null);
		}

		@Override
		public xbean.SkillRole toDataIf() {
			return this;
		}

		public xbean.SkillRole toBeanIf() {
			return new SkillRole(this, null, null);
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
		public java.util.Map<Integer, Integer> getSkillbases() { // key = skillbase id; value = skillbase level
			return skillbases;
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillbasesAsData() { // key = skillbase id; value = skillbase level
			return skillbases;
		}

		@Override
		public java.util.Map<Integer, Integer> getInborns() { // key = inborn id; value = inborn level
			return inborns;
		}

		@Override
		public java.util.Map<Integer, Integer> getInbornsAsData() { // key = inborn id; value = inborn level
			return inborns;
		}

		@Override
		public long getLastdegradetime() { // 上次穴位降级时间，一天只能降级一次
			return lastdegradetime;
		}

		@Override
		public java.util.Map<Integer, xbean.AssistSkill> getAssistskills() { // 辅助技能
			return assistskills;
		}

		@Override
		public java.util.Map<Integer, xbean.AssistSkill> getAssistskillsAsData() { // 辅助技能
			return assistskills;
		}

		@Override
		public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskill() { // 修炼技能 by changhao
			return practiceskill;
		}

		@Override
		public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskillAsData() { // 修炼技能 by changhao
			return practiceskill;
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskill() { // 生活技能 by changhao
			return liveskill;
		}

		@Override
		public java.util.Map<Integer, xbean.LiveSkill> getLiveskillAsData() { // 生活技能 by changhao
			return liveskill;
		}

		@Override
		public java.util.List<xbean.SpecialSkill> getSpecialskills() { // 装备提供的特技特效
			return specialskills;
		}

		@Override
		public java.util.List<xbean.SpecialSkill> getSpecialskillsAsData() { // 装备提供的特技特效
			return specialskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getExtskills() { // key = extskill id; value = extskill level
			return extskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getExtskillsAsData() { // key = extskill id; value = extskill level
			return extskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillsequence() { // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
			return skillsequence;
		}

		@Override
		public java.util.Map<Integer, Integer> getSkillsequenceAsData() { // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
			return skillsequence;
		}

		@Override
		public java.util.Map<Integer, Long> getLastusedtime() { // 上一次使用技能的时间，目前只用在结拜拉人技能里
			return lastusedtime;
		}

		@Override
		public java.util.Map<Integer, Long> getLastusedtimeAsData() { // 上一次使用技能的时间，目前只用在结拜拉人技能里
			return lastusedtime;
		}

		@Override
		public void setLastdegradetime(long _v_) { // 上次穴位降级时间，一天只能降级一次
			lastdegradetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SkillRole.Data)) return false;
			SkillRole.Data _o_ = (SkillRole.Data) _o1_;
			if (!skillbases.equals(_o_.skillbases)) return false;
			if (!inborns.equals(_o_.inborns)) return false;
			if (lastdegradetime != _o_.lastdegradetime) return false;
			if (!assistskills.equals(_o_.assistskills)) return false;
			if (!practiceskill.equals(_o_.practiceskill)) return false;
			if (!liveskill.equals(_o_.liveskill)) return false;
			if (!specialskills.equals(_o_.specialskills)) return false;
			if (!extskills.equals(_o_.extskills)) return false;
			if (!skillsequence.equals(_o_.skillsequence)) return false;
			if (!lastusedtime.equals(_o_.lastusedtime)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += skillbases.hashCode();
			_h_ += inborns.hashCode();
			_h_ += lastdegradetime;
			_h_ += assistskills.hashCode();
			_h_ += practiceskill.hashCode();
			_h_ += liveskill.hashCode();
			_h_ += specialskills.hashCode();
			_h_ += extskills.hashCode();
			_h_ += skillsequence.hashCode();
			_h_ += lastusedtime.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(skillbases);
			_sb_.append(",");
			_sb_.append(inborns);
			_sb_.append(",");
			_sb_.append(lastdegradetime);
			_sb_.append(",");
			_sb_.append(assistskills);
			_sb_.append(",");
			_sb_.append(practiceskill);
			_sb_.append(",");
			_sb_.append(liveskill);
			_sb_.append(",");
			_sb_.append(specialskills);
			_sb_.append(",");
			_sb_.append(extskills);
			_sb_.append(",");
			_sb_.append(skillsequence);
			_sb_.append(",");
			_sb_.append(lastusedtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
