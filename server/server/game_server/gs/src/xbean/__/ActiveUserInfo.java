
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ActiveUserInfo extends mkdb.XBean implements xbean.ActiveUserInfo {
	private int firsttimeenter; // 第一次进入服务器 当前天
	private int lasttimeenter; // 最近一次进入服务器 当前天
	private int lasttimeactiveday1; // 最近两次活跃的 当前天 day1比day2新,下同
	private int lasttimeactiveday2; // 
	private int lasttimeactiveweek1; // 最近两次活跃的 当前周 week1比week2新,下同
	private int lasttimeactiveweek2; // 
	private int firsttimeactiveday; // 第一次活跃的 当前天
	private int firsttimeactiveweek; // 第一次活跃的 当前周
	private int lasttimechargeday1; // 最近2次充值的当前天
	private int lasttimechargeday2; // 最近2次充值的当前天
	private int lasttimechargeweek1; // 最近2次充值的当前周
	private int lasttimechargeweek2; // 最近2次充值的当前周
	private int firsttimechargeday; // 第一次充值 当前天
	private int firsttimechargeweek; // 第一次充值 当前周
	private int lasttimechargeday; // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
	private int lasttimechargedaytotalamount; // 最近一天充值的总额 当前天
	private int lasttimechargeweek; // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
	private int lasttimechargeweektotalamount; // 最近一周充值的总额 当前周
	private int chargetotalamount; // 充值总额
	private int chargetotalrmb; // 充值rmb总额
	private long chargetotalamountlong; // 充值总额
	private long chargetotalrmblong; // 充值rmb总额
	private int chargetotalrmbuntillastday; // 截止昨天的充值总额，方便计算当日充值多少
	private long chargetotalrmbuntillastdaylong; // 截止昨天的充值总额，方便计算当日充值多少
	private int convertchargetotalamouttolong; // 是否已经把充值总额转变成了long型
	private long fushiconsumetotalamount; // 符石消耗的总量
	private long cashfushiconsumetotalamount; // 现金充值符石消耗的总量
	private int lasttimefushiconsumeday; // 最近天 消耗符石,记录的是天数
	private int lasttimefushiconsumedaytotalamount; // 最近天 消耗的符石数量
	private int lasttimefushiconsumeweek; // 最近周 消耗符石,记录的是周数
	private int lasttimefushiconsumeweektotalamount; // 最近周 消耗的符石数量
	private long fushibuytotalamount; // 符石购买的总量,给log用,不影响活跃
	private long fushiselltotalamount; // 符石出售的总量,给log用
	private int cashfushi; // 剩余流通符石
	private int bindfushi; // 剩余绑定rmb符石
	private int sysfushi; // 剩余系统赠送符石
	private int fushiinplatform; // 在交易平台里面的挂单符石

	@Override
	public void _reset_unsafe_() {
		firsttimeenter = 0;
		lasttimeenter = 0;
		lasttimeactiveday1 = 0;
		lasttimeactiveday2 = 0;
		lasttimeactiveweek1 = 0;
		lasttimeactiveweek2 = 0;
		firsttimeactiveday = 0;
		firsttimeactiveweek = 0;
		lasttimechargeday1 = 0;
		lasttimechargeday2 = 0;
		lasttimechargeweek1 = 0;
		lasttimechargeweek2 = 0;
		firsttimechargeday = 0;
		firsttimechargeweek = 0;
		lasttimechargeday = 0;
		lasttimechargedaytotalamount = 0;
		lasttimechargeweek = 0;
		lasttimechargeweektotalamount = 0;
		chargetotalamount = 0;
		chargetotalrmb = 0;
		chargetotalamountlong = 0L;
		chargetotalrmblong = 0L;
		chargetotalrmbuntillastday = 0;
		chargetotalrmbuntillastdaylong = 0L;
		convertchargetotalamouttolong = 0;
		fushiconsumetotalamount = 0L;
		cashfushiconsumetotalamount = 0L;
		lasttimefushiconsumeday = 0;
		lasttimefushiconsumedaytotalamount = 0;
		lasttimefushiconsumeweek = 0;
		lasttimefushiconsumeweektotalamount = 0;
		fushibuytotalamount = 0L;
		fushiselltotalamount = 0L;
		cashfushi = 0;
		bindfushi = 0;
		sysfushi = 0;
		fushiinplatform = 0;
	}

	ActiveUserInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ActiveUserInfo() {
		this(0, null, null);
	}

	public ActiveUserInfo(ActiveUserInfo _o_) {
		this(_o_, null, null);
	}

	ActiveUserInfo(xbean.ActiveUserInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ActiveUserInfo) assign((ActiveUserInfo)_o1_);
		else if (_o1_ instanceof ActiveUserInfo.Data) assign((ActiveUserInfo.Data)_o1_);
		else if (_o1_ instanceof ActiveUserInfo.Const) assign(((ActiveUserInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ActiveUserInfo _o_) {
		_o_._xdb_verify_unsafe_();
		firsttimeenter = _o_.firsttimeenter;
		lasttimeenter = _o_.lasttimeenter;
		lasttimeactiveday1 = _o_.lasttimeactiveday1;
		lasttimeactiveday2 = _o_.lasttimeactiveday2;
		lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
		lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
		firsttimeactiveday = _o_.firsttimeactiveday;
		firsttimeactiveweek = _o_.firsttimeactiveweek;
		lasttimechargeday1 = _o_.lasttimechargeday1;
		lasttimechargeday2 = _o_.lasttimechargeday2;
		lasttimechargeweek1 = _o_.lasttimechargeweek1;
		lasttimechargeweek2 = _o_.lasttimechargeweek2;
		firsttimechargeday = _o_.firsttimechargeday;
		firsttimechargeweek = _o_.firsttimechargeweek;
		lasttimechargeday = _o_.lasttimechargeday;
		lasttimechargedaytotalamount = _o_.lasttimechargedaytotalamount;
		lasttimechargeweek = _o_.lasttimechargeweek;
		lasttimechargeweektotalamount = _o_.lasttimechargeweektotalamount;
		chargetotalamount = _o_.chargetotalamount;
		chargetotalrmb = _o_.chargetotalrmb;
		chargetotalamountlong = _o_.chargetotalamountlong;
		chargetotalrmblong = _o_.chargetotalrmblong;
		chargetotalrmbuntillastday = _o_.chargetotalrmbuntillastday;
		chargetotalrmbuntillastdaylong = _o_.chargetotalrmbuntillastdaylong;
		convertchargetotalamouttolong = _o_.convertchargetotalamouttolong;
		fushiconsumetotalamount = _o_.fushiconsumetotalamount;
		cashfushiconsumetotalamount = _o_.cashfushiconsumetotalamount;
		lasttimefushiconsumeday = _o_.lasttimefushiconsumeday;
		lasttimefushiconsumedaytotalamount = _o_.lasttimefushiconsumedaytotalamount;
		lasttimefushiconsumeweek = _o_.lasttimefushiconsumeweek;
		lasttimefushiconsumeweektotalamount = _o_.lasttimefushiconsumeweektotalamount;
		fushibuytotalamount = _o_.fushibuytotalamount;
		fushiselltotalamount = _o_.fushiselltotalamount;
		cashfushi = _o_.cashfushi;
		bindfushi = _o_.bindfushi;
		sysfushi = _o_.sysfushi;
		fushiinplatform = _o_.fushiinplatform;
	}

	private void assign(ActiveUserInfo.Data _o_) {
		firsttimeenter = _o_.firsttimeenter;
		lasttimeenter = _o_.lasttimeenter;
		lasttimeactiveday1 = _o_.lasttimeactiveday1;
		lasttimeactiveday2 = _o_.lasttimeactiveday2;
		lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
		lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
		firsttimeactiveday = _o_.firsttimeactiveday;
		firsttimeactiveweek = _o_.firsttimeactiveweek;
		lasttimechargeday1 = _o_.lasttimechargeday1;
		lasttimechargeday2 = _o_.lasttimechargeday2;
		lasttimechargeweek1 = _o_.lasttimechargeweek1;
		lasttimechargeweek2 = _o_.lasttimechargeweek2;
		firsttimechargeday = _o_.firsttimechargeday;
		firsttimechargeweek = _o_.firsttimechargeweek;
		lasttimechargeday = _o_.lasttimechargeday;
		lasttimechargedaytotalamount = _o_.lasttimechargedaytotalamount;
		lasttimechargeweek = _o_.lasttimechargeweek;
		lasttimechargeweektotalamount = _o_.lasttimechargeweektotalamount;
		chargetotalamount = _o_.chargetotalamount;
		chargetotalrmb = _o_.chargetotalrmb;
		chargetotalamountlong = _o_.chargetotalamountlong;
		chargetotalrmblong = _o_.chargetotalrmblong;
		chargetotalrmbuntillastday = _o_.chargetotalrmbuntillastday;
		chargetotalrmbuntillastdaylong = _o_.chargetotalrmbuntillastdaylong;
		convertchargetotalamouttolong = _o_.convertchargetotalamouttolong;
		fushiconsumetotalamount = _o_.fushiconsumetotalamount;
		cashfushiconsumetotalamount = _o_.cashfushiconsumetotalamount;
		lasttimefushiconsumeday = _o_.lasttimefushiconsumeday;
		lasttimefushiconsumedaytotalamount = _o_.lasttimefushiconsumedaytotalamount;
		lasttimefushiconsumeweek = _o_.lasttimefushiconsumeweek;
		lasttimefushiconsumeweektotalamount = _o_.lasttimefushiconsumeweektotalamount;
		fushibuytotalamount = _o_.fushibuytotalamount;
		fushiselltotalamount = _o_.fushiselltotalamount;
		cashfushi = _o_.cashfushi;
		bindfushi = _o_.bindfushi;
		sysfushi = _o_.sysfushi;
		fushiinplatform = _o_.fushiinplatform;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(firsttimeenter);
		_os_.marshal(lasttimeenter);
		_os_.marshal(lasttimeactiveday1);
		_os_.marshal(lasttimeactiveday2);
		_os_.marshal(lasttimeactiveweek1);
		_os_.marshal(lasttimeactiveweek2);
		_os_.marshal(firsttimeactiveday);
		_os_.marshal(firsttimeactiveweek);
		_os_.marshal(lasttimechargeday1);
		_os_.marshal(lasttimechargeday2);
		_os_.marshal(lasttimechargeweek1);
		_os_.marshal(lasttimechargeweek2);
		_os_.marshal(firsttimechargeday);
		_os_.marshal(firsttimechargeweek);
		_os_.marshal(lasttimechargeday);
		_os_.marshal(lasttimechargedaytotalamount);
		_os_.marshal(lasttimechargeweek);
		_os_.marshal(lasttimechargeweektotalamount);
		_os_.marshal(chargetotalamount);
		_os_.marshal(chargetotalrmb);
		_os_.marshal(chargetotalamountlong);
		_os_.marshal(chargetotalrmblong);
		_os_.marshal(chargetotalrmbuntillastday);
		_os_.marshal(chargetotalrmbuntillastdaylong);
		_os_.marshal(convertchargetotalamouttolong);
		_os_.marshal(fushiconsumetotalamount);
		_os_.marshal(cashfushiconsumetotalamount);
		_os_.marshal(lasttimefushiconsumeday);
		_os_.marshal(lasttimefushiconsumedaytotalamount);
		_os_.marshal(lasttimefushiconsumeweek);
		_os_.marshal(lasttimefushiconsumeweektotalamount);
		_os_.marshal(fushibuytotalamount);
		_os_.marshal(fushiselltotalamount);
		_os_.marshal(cashfushi);
		_os_.marshal(bindfushi);
		_os_.marshal(sysfushi);
		_os_.marshal(fushiinplatform);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		firsttimeenter = _os_.unmarshal_int();
		lasttimeenter = _os_.unmarshal_int();
		lasttimeactiveday1 = _os_.unmarshal_int();
		lasttimeactiveday2 = _os_.unmarshal_int();
		lasttimeactiveweek1 = _os_.unmarshal_int();
		lasttimeactiveweek2 = _os_.unmarshal_int();
		firsttimeactiveday = _os_.unmarshal_int();
		firsttimeactiveweek = _os_.unmarshal_int();
		lasttimechargeday1 = _os_.unmarshal_int();
		lasttimechargeday2 = _os_.unmarshal_int();
		lasttimechargeweek1 = _os_.unmarshal_int();
		lasttimechargeweek2 = _os_.unmarshal_int();
		firsttimechargeday = _os_.unmarshal_int();
		firsttimechargeweek = _os_.unmarshal_int();
		lasttimechargeday = _os_.unmarshal_int();
		lasttimechargedaytotalamount = _os_.unmarshal_int();
		lasttimechargeweek = _os_.unmarshal_int();
		lasttimechargeweektotalamount = _os_.unmarshal_int();
		chargetotalamount = _os_.unmarshal_int();
		chargetotalrmb = _os_.unmarshal_int();
		chargetotalamountlong = _os_.unmarshal_long();
		chargetotalrmblong = _os_.unmarshal_long();
		chargetotalrmbuntillastday = _os_.unmarshal_int();
		chargetotalrmbuntillastdaylong = _os_.unmarshal_long();
		convertchargetotalamouttolong = _os_.unmarshal_int();
		fushiconsumetotalamount = _os_.unmarshal_long();
		cashfushiconsumetotalamount = _os_.unmarshal_long();
		lasttimefushiconsumeday = _os_.unmarshal_int();
		lasttimefushiconsumedaytotalamount = _os_.unmarshal_int();
		lasttimefushiconsumeweek = _os_.unmarshal_int();
		lasttimefushiconsumeweektotalamount = _os_.unmarshal_int();
		fushibuytotalamount = _os_.unmarshal_long();
		fushiselltotalamount = _os_.unmarshal_long();
		cashfushi = _os_.unmarshal_int();
		bindfushi = _os_.unmarshal_int();
		sysfushi = _os_.unmarshal_int();
		fushiinplatform = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ActiveUserInfo copy() {
		_xdb_verify_unsafe_();
		return new ActiveUserInfo(this);
	}

	@Override
	public xbean.ActiveUserInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActiveUserInfo toBean() {
		_xdb_verify_unsafe_();
		return new ActiveUserInfo(this); // same as copy()
	}

	@Override
	public xbean.ActiveUserInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActiveUserInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getFirsttimeenter() { // 第一次进入服务器 当前天
		_xdb_verify_unsafe_();
		return firsttimeenter;
	}

	@Override
	public int getLasttimeenter() { // 最近一次进入服务器 当前天
		_xdb_verify_unsafe_();
		return lasttimeenter;
	}

	@Override
	public int getLasttimeactiveday1() { // 最近两次活跃的 当前天 day1比day2新,下同
		_xdb_verify_unsafe_();
		return lasttimeactiveday1;
	}

	@Override
	public int getLasttimeactiveday2() { // 
		_xdb_verify_unsafe_();
		return lasttimeactiveday2;
	}

	@Override
	public int getLasttimeactiveweek1() { // 最近两次活跃的 当前周 week1比week2新,下同
		_xdb_verify_unsafe_();
		return lasttimeactiveweek1;
	}

	@Override
	public int getLasttimeactiveweek2() { // 
		_xdb_verify_unsafe_();
		return lasttimeactiveweek2;
	}

	@Override
	public int getFirsttimeactiveday() { // 第一次活跃的 当前天
		_xdb_verify_unsafe_();
		return firsttimeactiveday;
	}

	@Override
	public int getFirsttimeactiveweek() { // 第一次活跃的 当前周
		_xdb_verify_unsafe_();
		return firsttimeactiveweek;
	}

	@Override
	public int getLasttimechargeday1() { // 最近2次充值的当前天
		_xdb_verify_unsafe_();
		return lasttimechargeday1;
	}

	@Override
	public int getLasttimechargeday2() { // 最近2次充值的当前天
		_xdb_verify_unsafe_();
		return lasttimechargeday2;
	}

	@Override
	public int getLasttimechargeweek1() { // 最近2次充值的当前周
		_xdb_verify_unsafe_();
		return lasttimechargeweek1;
	}

	@Override
	public int getLasttimechargeweek2() { // 最近2次充值的当前周
		_xdb_verify_unsafe_();
		return lasttimechargeweek2;
	}

	@Override
	public int getFirsttimechargeday() { // 第一次充值 当前天
		_xdb_verify_unsafe_();
		return firsttimechargeday;
	}

	@Override
	public int getFirsttimechargeweek() { // 第一次充值 当前周
		_xdb_verify_unsafe_();
		return firsttimechargeweek;
	}

	@Override
	public int getLasttimechargeday() { // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
		_xdb_verify_unsafe_();
		return lasttimechargeday;
	}

	@Override
	public int getLasttimechargedaytotalamount() { // 最近一天充值的总额 当前天
		_xdb_verify_unsafe_();
		return lasttimechargedaytotalamount;
	}

	@Override
	public int getLasttimechargeweek() { // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
		_xdb_verify_unsafe_();
		return lasttimechargeweek;
	}

	@Override
	public int getLasttimechargeweektotalamount() { // 最近一周充值的总额 当前周
		_xdb_verify_unsafe_();
		return lasttimechargeweektotalamount;
	}

	@Override
	public int getChargetotalamount() { // 充值总额
		_xdb_verify_unsafe_();
		return chargetotalamount;
	}

	@Override
	public int getChargetotalrmb() { // 充值rmb总额
		_xdb_verify_unsafe_();
		return chargetotalrmb;
	}

	@Override
	public long getChargetotalamountlong() { // 充值总额
		_xdb_verify_unsafe_();
		return chargetotalamountlong;
	}

	@Override
	public long getChargetotalrmblong() { // 充值rmb总额
		_xdb_verify_unsafe_();
		return chargetotalrmblong;
	}

	@Override
	public int getChargetotalrmbuntillastday() { // 截止昨天的充值总额，方便计算当日充值多少
		_xdb_verify_unsafe_();
		return chargetotalrmbuntillastday;
	}

	@Override
	public long getChargetotalrmbuntillastdaylong() { // 截止昨天的充值总额，方便计算当日充值多少
		_xdb_verify_unsafe_();
		return chargetotalrmbuntillastdaylong;
	}

	@Override
	public int getConvertchargetotalamouttolong() { // 是否已经把充值总额转变成了long型
		_xdb_verify_unsafe_();
		return convertchargetotalamouttolong;
	}

	@Override
	public long getFushiconsumetotalamount() { // 符石消耗的总量
		_xdb_verify_unsafe_();
		return fushiconsumetotalamount;
	}

	@Override
	public long getCashfushiconsumetotalamount() { // 现金充值符石消耗的总量
		_xdb_verify_unsafe_();
		return cashfushiconsumetotalamount;
	}

	@Override
	public int getLasttimefushiconsumeday() { // 最近天 消耗符石,记录的是天数
		_xdb_verify_unsafe_();
		return lasttimefushiconsumeday;
	}

	@Override
	public int getLasttimefushiconsumedaytotalamount() { // 最近天 消耗的符石数量
		_xdb_verify_unsafe_();
		return lasttimefushiconsumedaytotalamount;
	}

	@Override
	public int getLasttimefushiconsumeweek() { // 最近周 消耗符石,记录的是周数
		_xdb_verify_unsafe_();
		return lasttimefushiconsumeweek;
	}

	@Override
	public int getLasttimefushiconsumeweektotalamount() { // 最近周 消耗的符石数量
		_xdb_verify_unsafe_();
		return lasttimefushiconsumeweektotalamount;
	}

	@Override
	public long getFushibuytotalamount() { // 符石购买的总量,给log用,不影响活跃
		_xdb_verify_unsafe_();
		return fushibuytotalamount;
	}

	@Override
	public long getFushiselltotalamount() { // 符石出售的总量,给log用
		_xdb_verify_unsafe_();
		return fushiselltotalamount;
	}

	@Override
	public int getCashfushi() { // 剩余流通符石
		_xdb_verify_unsafe_();
		return cashfushi;
	}

	@Override
	public int getBindfushi() { // 剩余绑定rmb符石
		_xdb_verify_unsafe_();
		return bindfushi;
	}

	@Override
	public int getSysfushi() { // 剩余系统赠送符石
		_xdb_verify_unsafe_();
		return sysfushi;
	}

	@Override
	public int getFushiinplatform() { // 在交易平台里面的挂单符石
		_xdb_verify_unsafe_();
		return fushiinplatform;
	}

	@Override
	public void setFirsttimeenter(int _v_) { // 第一次进入服务器 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimeenter") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimeenter) {
					public void rollback() { firsttimeenter = _xdb_saved; }
				};}});
		firsttimeenter = _v_;
	}

	@Override
	public void setLasttimeenter(int _v_) { // 最近一次进入服务器 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeenter") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeenter) {
					public void rollback() { lasttimeenter = _xdb_saved; }
				};}});
		lasttimeenter = _v_;
	}

	@Override
	public void setLasttimeactiveday1(int _v_) { // 最近两次活跃的 当前天 day1比day2新,下同
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveday1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveday1) {
					public void rollback() { lasttimeactiveday1 = _xdb_saved; }
				};}});
		lasttimeactiveday1 = _v_;
	}

	@Override
	public void setLasttimeactiveday2(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveday2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveday2) {
					public void rollback() { lasttimeactiveday2 = _xdb_saved; }
				};}});
		lasttimeactiveday2 = _v_;
	}

	@Override
	public void setLasttimeactiveweek1(int _v_) { // 最近两次活跃的 当前周 week1比week2新,下同
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveweek1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveweek1) {
					public void rollback() { lasttimeactiveweek1 = _xdb_saved; }
				};}});
		lasttimeactiveweek1 = _v_;
	}

	@Override
	public void setLasttimeactiveweek2(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveweek2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveweek2) {
					public void rollback() { lasttimeactiveweek2 = _xdb_saved; }
				};}});
		lasttimeactiveweek2 = _v_;
	}

	@Override
	public void setFirsttimeactiveday(int _v_) { // 第一次活跃的 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimeactiveday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimeactiveday) {
					public void rollback() { firsttimeactiveday = _xdb_saved; }
				};}});
		firsttimeactiveday = _v_;
	}

	@Override
	public void setFirsttimeactiveweek(int _v_) { // 第一次活跃的 当前周
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimeactiveweek") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimeactiveweek) {
					public void rollback() { firsttimeactiveweek = _xdb_saved; }
				};}});
		firsttimeactiveweek = _v_;
	}

	@Override
	public void setLasttimechargeday1(int _v_) { // 最近2次充值的当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeday1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeday1) {
					public void rollback() { lasttimechargeday1 = _xdb_saved; }
				};}});
		lasttimechargeday1 = _v_;
	}

	@Override
	public void setLasttimechargeday2(int _v_) { // 最近2次充值的当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeday2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeday2) {
					public void rollback() { lasttimechargeday2 = _xdb_saved; }
				};}});
		lasttimechargeday2 = _v_;
	}

	@Override
	public void setLasttimechargeweek1(int _v_) { // 最近2次充值的当前周
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeweek1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeweek1) {
					public void rollback() { lasttimechargeweek1 = _xdb_saved; }
				};}});
		lasttimechargeweek1 = _v_;
	}

	@Override
	public void setLasttimechargeweek2(int _v_) { // 最近2次充值的当前周
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeweek2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeweek2) {
					public void rollback() { lasttimechargeweek2 = _xdb_saved; }
				};}});
		lasttimechargeweek2 = _v_;
	}

	@Override
	public void setFirsttimechargeday(int _v_) { // 第一次充值 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimechargeday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimechargeday) {
					public void rollback() { firsttimechargeday = _xdb_saved; }
				};}});
		firsttimechargeday = _v_;
	}

	@Override
	public void setFirsttimechargeweek(int _v_) { // 第一次充值 当前周
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimechargeweek") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimechargeweek) {
					public void rollback() { firsttimechargeweek = _xdb_saved; }
				};}});
		firsttimechargeweek = _v_;
	}

	@Override
	public void setLasttimechargeday(int _v_) { // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeday) {
					public void rollback() { lasttimechargeday = _xdb_saved; }
				};}});
		lasttimechargeday = _v_;
	}

	@Override
	public void setLasttimechargedaytotalamount(int _v_) { // 最近一天充值的总额 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargedaytotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargedaytotalamount) {
					public void rollback() { lasttimechargedaytotalamount = _xdb_saved; }
				};}});
		lasttimechargedaytotalamount = _v_;
	}

	@Override
	public void setLasttimechargeweek(int _v_) { // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeweek") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeweek) {
					public void rollback() { lasttimechargeweek = _xdb_saved; }
				};}});
		lasttimechargeweek = _v_;
	}

	@Override
	public void setLasttimechargeweektotalamount(int _v_) { // 最近一周充值的总额 当前周
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimechargeweektotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimechargeweektotalamount) {
					public void rollback() { lasttimechargeweektotalamount = _xdb_saved; }
				};}});
		lasttimechargeweektotalamount = _v_;
	}

	@Override
	public void setChargetotalamount(int _v_) { // 充值总额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, chargetotalamount) {
					public void rollback() { chargetotalamount = _xdb_saved; }
				};}});
		chargetotalamount = _v_;
	}

	@Override
	public void setChargetotalrmb(int _v_) { // 充值rmb总额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetotalrmb") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, chargetotalrmb) {
					public void rollback() { chargetotalrmb = _xdb_saved; }
				};}});
		chargetotalrmb = _v_;
	}

	@Override
	public void setChargetotalamountlong(long _v_) { // 充值总额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetotalamountlong") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, chargetotalamountlong) {
					public void rollback() { chargetotalamountlong = _xdb_saved; }
				};}});
		chargetotalamountlong = _v_;
	}

	@Override
	public void setChargetotalrmblong(long _v_) { // 充值rmb总额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetotalrmblong") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, chargetotalrmblong) {
					public void rollback() { chargetotalrmblong = _xdb_saved; }
				};}});
		chargetotalrmblong = _v_;
	}

	@Override
	public void setChargetotalrmbuntillastday(int _v_) { // 截止昨天的充值总额，方便计算当日充值多少
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetotalrmbuntillastday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, chargetotalrmbuntillastday) {
					public void rollback() { chargetotalrmbuntillastday = _xdb_saved; }
				};}});
		chargetotalrmbuntillastday = _v_;
	}

	@Override
	public void setChargetotalrmbuntillastdaylong(long _v_) { // 截止昨天的充值总额，方便计算当日充值多少
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetotalrmbuntillastdaylong") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, chargetotalrmbuntillastdaylong) {
					public void rollback() { chargetotalrmbuntillastdaylong = _xdb_saved; }
				};}});
		chargetotalrmbuntillastdaylong = _v_;
	}

	@Override
	public void setConvertchargetotalamouttolong(int _v_) { // 是否已经把充值总额转变成了long型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "convertchargetotalamouttolong") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, convertchargetotalamouttolong) {
					public void rollback() { convertchargetotalamouttolong = _xdb_saved; }
				};}});
		convertchargetotalamouttolong = _v_;
	}

	@Override
	public void setFushiconsumetotalamount(long _v_) { // 符石消耗的总量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushiconsumetotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fushiconsumetotalamount) {
					public void rollback() { fushiconsumetotalamount = _xdb_saved; }
				};}});
		fushiconsumetotalamount = _v_;
	}

	@Override
	public void setCashfushiconsumetotalamount(long _v_) { // 现金充值符石消耗的总量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cashfushiconsumetotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, cashfushiconsumetotalamount) {
					public void rollback() { cashfushiconsumetotalamount = _xdb_saved; }
				};}});
		cashfushiconsumetotalamount = _v_;
	}

	@Override
	public void setLasttimefushiconsumeday(int _v_) { // 最近天 消耗符石,记录的是天数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimefushiconsumeday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimefushiconsumeday) {
					public void rollback() { lasttimefushiconsumeday = _xdb_saved; }
				};}});
		lasttimefushiconsumeday = _v_;
	}

	@Override
	public void setLasttimefushiconsumedaytotalamount(int _v_) { // 最近天 消耗的符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimefushiconsumedaytotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimefushiconsumedaytotalamount) {
					public void rollback() { lasttimefushiconsumedaytotalamount = _xdb_saved; }
				};}});
		lasttimefushiconsumedaytotalamount = _v_;
	}

	@Override
	public void setLasttimefushiconsumeweek(int _v_) { // 最近周 消耗符石,记录的是周数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimefushiconsumeweek") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimefushiconsumeweek) {
					public void rollback() { lasttimefushiconsumeweek = _xdb_saved; }
				};}});
		lasttimefushiconsumeweek = _v_;
	}

	@Override
	public void setLasttimefushiconsumeweektotalamount(int _v_) { // 最近周 消耗的符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimefushiconsumeweektotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimefushiconsumeweektotalamount) {
					public void rollback() { lasttimefushiconsumeweektotalamount = _xdb_saved; }
				};}});
		lasttimefushiconsumeweektotalamount = _v_;
	}

	@Override
	public void setFushibuytotalamount(long _v_) { // 符石购买的总量,给log用,不影响活跃
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushibuytotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fushibuytotalamount) {
					public void rollback() { fushibuytotalamount = _xdb_saved; }
				};}});
		fushibuytotalamount = _v_;
	}

	@Override
	public void setFushiselltotalamount(long _v_) { // 符石出售的总量,给log用
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushiselltotalamount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fushiselltotalamount) {
					public void rollback() { fushiselltotalamount = _xdb_saved; }
				};}});
		fushiselltotalamount = _v_;
	}

	@Override
	public void setCashfushi(int _v_) { // 剩余流通符石
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cashfushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, cashfushi) {
					public void rollback() { cashfushi = _xdb_saved; }
				};}});
		cashfushi = _v_;
	}

	@Override
	public void setBindfushi(int _v_) { // 剩余绑定rmb符石
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindfushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bindfushi) {
					public void rollback() { bindfushi = _xdb_saved; }
				};}});
		bindfushi = _v_;
	}

	@Override
	public void setSysfushi(int _v_) { // 剩余系统赠送符石
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sysfushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sysfushi) {
					public void rollback() { sysfushi = _xdb_saved; }
				};}});
		sysfushi = _v_;
	}

	@Override
	public void setFushiinplatform(int _v_) { // 在交易平台里面的挂单符石
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushiinplatform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fushiinplatform) {
					public void rollback() { fushiinplatform = _xdb_saved; }
				};}});
		fushiinplatform = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ActiveUserInfo _o_ = null;
		if ( _o1_ instanceof ActiveUserInfo ) _o_ = (ActiveUserInfo)_o1_;
		else if ( _o1_ instanceof ActiveUserInfo.Const ) _o_ = ((ActiveUserInfo.Const)_o1_).nThis();
		else return false;
		if (firsttimeenter != _o_.firsttimeenter) return false;
		if (lasttimeenter != _o_.lasttimeenter) return false;
		if (lasttimeactiveday1 != _o_.lasttimeactiveday1) return false;
		if (lasttimeactiveday2 != _o_.lasttimeactiveday2) return false;
		if (lasttimeactiveweek1 != _o_.lasttimeactiveweek1) return false;
		if (lasttimeactiveweek2 != _o_.lasttimeactiveweek2) return false;
		if (firsttimeactiveday != _o_.firsttimeactiveday) return false;
		if (firsttimeactiveweek != _o_.firsttimeactiveweek) return false;
		if (lasttimechargeday1 != _o_.lasttimechargeday1) return false;
		if (lasttimechargeday2 != _o_.lasttimechargeday2) return false;
		if (lasttimechargeweek1 != _o_.lasttimechargeweek1) return false;
		if (lasttimechargeweek2 != _o_.lasttimechargeweek2) return false;
		if (firsttimechargeday != _o_.firsttimechargeday) return false;
		if (firsttimechargeweek != _o_.firsttimechargeweek) return false;
		if (lasttimechargeday != _o_.lasttimechargeday) return false;
		if (lasttimechargedaytotalamount != _o_.lasttimechargedaytotalamount) return false;
		if (lasttimechargeweek != _o_.lasttimechargeweek) return false;
		if (lasttimechargeweektotalamount != _o_.lasttimechargeweektotalamount) return false;
		if (chargetotalamount != _o_.chargetotalamount) return false;
		if (chargetotalrmb != _o_.chargetotalrmb) return false;
		if (chargetotalamountlong != _o_.chargetotalamountlong) return false;
		if (chargetotalrmblong != _o_.chargetotalrmblong) return false;
		if (chargetotalrmbuntillastday != _o_.chargetotalrmbuntillastday) return false;
		if (chargetotalrmbuntillastdaylong != _o_.chargetotalrmbuntillastdaylong) return false;
		if (convertchargetotalamouttolong != _o_.convertchargetotalamouttolong) return false;
		if (fushiconsumetotalamount != _o_.fushiconsumetotalamount) return false;
		if (cashfushiconsumetotalamount != _o_.cashfushiconsumetotalamount) return false;
		if (lasttimefushiconsumeday != _o_.lasttimefushiconsumeday) return false;
		if (lasttimefushiconsumedaytotalamount != _o_.lasttimefushiconsumedaytotalamount) return false;
		if (lasttimefushiconsumeweek != _o_.lasttimefushiconsumeweek) return false;
		if (lasttimefushiconsumeweektotalamount != _o_.lasttimefushiconsumeweektotalamount) return false;
		if (fushibuytotalamount != _o_.fushibuytotalamount) return false;
		if (fushiselltotalamount != _o_.fushiselltotalamount) return false;
		if (cashfushi != _o_.cashfushi) return false;
		if (bindfushi != _o_.bindfushi) return false;
		if (sysfushi != _o_.sysfushi) return false;
		if (fushiinplatform != _o_.fushiinplatform) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += firsttimeenter;
		_h_ += lasttimeenter;
		_h_ += lasttimeactiveday1;
		_h_ += lasttimeactiveday2;
		_h_ += lasttimeactiveweek1;
		_h_ += lasttimeactiveweek2;
		_h_ += firsttimeactiveday;
		_h_ += firsttimeactiveweek;
		_h_ += lasttimechargeday1;
		_h_ += lasttimechargeday2;
		_h_ += lasttimechargeweek1;
		_h_ += lasttimechargeweek2;
		_h_ += firsttimechargeday;
		_h_ += firsttimechargeweek;
		_h_ += lasttimechargeday;
		_h_ += lasttimechargedaytotalamount;
		_h_ += lasttimechargeweek;
		_h_ += lasttimechargeweektotalamount;
		_h_ += chargetotalamount;
		_h_ += chargetotalrmb;
		_h_ += chargetotalamountlong;
		_h_ += chargetotalrmblong;
		_h_ += chargetotalrmbuntillastday;
		_h_ += chargetotalrmbuntillastdaylong;
		_h_ += convertchargetotalamouttolong;
		_h_ += fushiconsumetotalamount;
		_h_ += cashfushiconsumetotalamount;
		_h_ += lasttimefushiconsumeday;
		_h_ += lasttimefushiconsumedaytotalamount;
		_h_ += lasttimefushiconsumeweek;
		_h_ += lasttimefushiconsumeweektotalamount;
		_h_ += fushibuytotalamount;
		_h_ += fushiselltotalamount;
		_h_ += cashfushi;
		_h_ += bindfushi;
		_h_ += sysfushi;
		_h_ += fushiinplatform;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(firsttimeenter);
		_sb_.append(",");
		_sb_.append(lasttimeenter);
		_sb_.append(",");
		_sb_.append(lasttimeactiveday1);
		_sb_.append(",");
		_sb_.append(lasttimeactiveday2);
		_sb_.append(",");
		_sb_.append(lasttimeactiveweek1);
		_sb_.append(",");
		_sb_.append(lasttimeactiveweek2);
		_sb_.append(",");
		_sb_.append(firsttimeactiveday);
		_sb_.append(",");
		_sb_.append(firsttimeactiveweek);
		_sb_.append(",");
		_sb_.append(lasttimechargeday1);
		_sb_.append(",");
		_sb_.append(lasttimechargeday2);
		_sb_.append(",");
		_sb_.append(lasttimechargeweek1);
		_sb_.append(",");
		_sb_.append(lasttimechargeweek2);
		_sb_.append(",");
		_sb_.append(firsttimechargeday);
		_sb_.append(",");
		_sb_.append(firsttimechargeweek);
		_sb_.append(",");
		_sb_.append(lasttimechargeday);
		_sb_.append(",");
		_sb_.append(lasttimechargedaytotalamount);
		_sb_.append(",");
		_sb_.append(lasttimechargeweek);
		_sb_.append(",");
		_sb_.append(lasttimechargeweektotalamount);
		_sb_.append(",");
		_sb_.append(chargetotalamount);
		_sb_.append(",");
		_sb_.append(chargetotalrmb);
		_sb_.append(",");
		_sb_.append(chargetotalamountlong);
		_sb_.append(",");
		_sb_.append(chargetotalrmblong);
		_sb_.append(",");
		_sb_.append(chargetotalrmbuntillastday);
		_sb_.append(",");
		_sb_.append(chargetotalrmbuntillastdaylong);
		_sb_.append(",");
		_sb_.append(convertchargetotalamouttolong);
		_sb_.append(",");
		_sb_.append(fushiconsumetotalamount);
		_sb_.append(",");
		_sb_.append(cashfushiconsumetotalamount);
		_sb_.append(",");
		_sb_.append(lasttimefushiconsumeday);
		_sb_.append(",");
		_sb_.append(lasttimefushiconsumedaytotalamount);
		_sb_.append(",");
		_sb_.append(lasttimefushiconsumeweek);
		_sb_.append(",");
		_sb_.append(lasttimefushiconsumeweektotalamount);
		_sb_.append(",");
		_sb_.append(fushibuytotalamount);
		_sb_.append(",");
		_sb_.append(fushiselltotalamount);
		_sb_.append(",");
		_sb_.append(cashfushi);
		_sb_.append(",");
		_sb_.append(bindfushi);
		_sb_.append(",");
		_sb_.append(sysfushi);
		_sb_.append(",");
		_sb_.append(fushiinplatform);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimeenter"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeenter"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveday1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveday2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveweek1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveweek2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimeactiveday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimeactiveweek"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeday1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeday2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeweek1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeweek2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimechargeday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimechargeweek"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargedaytotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeweek"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimechargeweektotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetotalrmb"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetotalamountlong"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetotalrmblong"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetotalrmbuntillastday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetotalrmbuntillastdaylong"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("convertchargetotalamouttolong"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushiconsumetotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cashfushiconsumetotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimefushiconsumeday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimefushiconsumedaytotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimefushiconsumeweek"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimefushiconsumeweektotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushibuytotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushiselltotalamount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cashfushi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindfushi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sysfushi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushiinplatform"));
		return lb;
	}

	private class Const implements xbean.ActiveUserInfo {
		ActiveUserInfo nThis() {
			return ActiveUserInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ActiveUserInfo copy() {
			return ActiveUserInfo.this.copy();
		}

		@Override
		public xbean.ActiveUserInfo toData() {
			return ActiveUserInfo.this.toData();
		}

		public xbean.ActiveUserInfo toBean() {
			return ActiveUserInfo.this.toBean();
		}

		@Override
		public xbean.ActiveUserInfo toDataIf() {
			return ActiveUserInfo.this.toDataIf();
		}

		public xbean.ActiveUserInfo toBeanIf() {
			return ActiveUserInfo.this.toBeanIf();
		}

		@Override
		public int getFirsttimeenter() { // 第一次进入服务器 当前天
			_xdb_verify_unsafe_();
			return firsttimeenter;
		}

		@Override
		public int getLasttimeenter() { // 最近一次进入服务器 当前天
			_xdb_verify_unsafe_();
			return lasttimeenter;
		}

		@Override
		public int getLasttimeactiveday1() { // 最近两次活跃的 当前天 day1比day2新,下同
			_xdb_verify_unsafe_();
			return lasttimeactiveday1;
		}

		@Override
		public int getLasttimeactiveday2() { // 
			_xdb_verify_unsafe_();
			return lasttimeactiveday2;
		}

		@Override
		public int getLasttimeactiveweek1() { // 最近两次活跃的 当前周 week1比week2新,下同
			_xdb_verify_unsafe_();
			return lasttimeactiveweek1;
		}

		@Override
		public int getLasttimeactiveweek2() { // 
			_xdb_verify_unsafe_();
			return lasttimeactiveweek2;
		}

		@Override
		public int getFirsttimeactiveday() { // 第一次活跃的 当前天
			_xdb_verify_unsafe_();
			return firsttimeactiveday;
		}

		@Override
		public int getFirsttimeactiveweek() { // 第一次活跃的 当前周
			_xdb_verify_unsafe_();
			return firsttimeactiveweek;
		}

		@Override
		public int getLasttimechargeday1() { // 最近2次充值的当前天
			_xdb_verify_unsafe_();
			return lasttimechargeday1;
		}

		@Override
		public int getLasttimechargeday2() { // 最近2次充值的当前天
			_xdb_verify_unsafe_();
			return lasttimechargeday2;
		}

		@Override
		public int getLasttimechargeweek1() { // 最近2次充值的当前周
			_xdb_verify_unsafe_();
			return lasttimechargeweek1;
		}

		@Override
		public int getLasttimechargeweek2() { // 最近2次充值的当前周
			_xdb_verify_unsafe_();
			return lasttimechargeweek2;
		}

		@Override
		public int getFirsttimechargeday() { // 第一次充值 当前天
			_xdb_verify_unsafe_();
			return firsttimechargeday;
		}

		@Override
		public int getFirsttimechargeweek() { // 第一次充值 当前周
			_xdb_verify_unsafe_();
			return firsttimechargeweek;
		}

		@Override
		public int getLasttimechargeday() { // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
			_xdb_verify_unsafe_();
			return lasttimechargeday;
		}

		@Override
		public int getLasttimechargedaytotalamount() { // 最近一天充值的总额 当前天
			_xdb_verify_unsafe_();
			return lasttimechargedaytotalamount;
		}

		@Override
		public int getLasttimechargeweek() { // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
			_xdb_verify_unsafe_();
			return lasttimechargeweek;
		}

		@Override
		public int getLasttimechargeweektotalamount() { // 最近一周充值的总额 当前周
			_xdb_verify_unsafe_();
			return lasttimechargeweektotalamount;
		}

		@Override
		public int getChargetotalamount() { // 充值总额
			_xdb_verify_unsafe_();
			return chargetotalamount;
		}

		@Override
		public int getChargetotalrmb() { // 充值rmb总额
			_xdb_verify_unsafe_();
			return chargetotalrmb;
		}

		@Override
		public long getChargetotalamountlong() { // 充值总额
			_xdb_verify_unsafe_();
			return chargetotalamountlong;
		}

		@Override
		public long getChargetotalrmblong() { // 充值rmb总额
			_xdb_verify_unsafe_();
			return chargetotalrmblong;
		}

		@Override
		public int getChargetotalrmbuntillastday() { // 截止昨天的充值总额，方便计算当日充值多少
			_xdb_verify_unsafe_();
			return chargetotalrmbuntillastday;
		}

		@Override
		public long getChargetotalrmbuntillastdaylong() { // 截止昨天的充值总额，方便计算当日充值多少
			_xdb_verify_unsafe_();
			return chargetotalrmbuntillastdaylong;
		}

		@Override
		public int getConvertchargetotalamouttolong() { // 是否已经把充值总额转变成了long型
			_xdb_verify_unsafe_();
			return convertchargetotalamouttolong;
		}

		@Override
		public long getFushiconsumetotalamount() { // 符石消耗的总量
			_xdb_verify_unsafe_();
			return fushiconsumetotalamount;
		}

		@Override
		public long getCashfushiconsumetotalamount() { // 现金充值符石消耗的总量
			_xdb_verify_unsafe_();
			return cashfushiconsumetotalamount;
		}

		@Override
		public int getLasttimefushiconsumeday() { // 最近天 消耗符石,记录的是天数
			_xdb_verify_unsafe_();
			return lasttimefushiconsumeday;
		}

		@Override
		public int getLasttimefushiconsumedaytotalamount() { // 最近天 消耗的符石数量
			_xdb_verify_unsafe_();
			return lasttimefushiconsumedaytotalamount;
		}

		@Override
		public int getLasttimefushiconsumeweek() { // 最近周 消耗符石,记录的是周数
			_xdb_verify_unsafe_();
			return lasttimefushiconsumeweek;
		}

		@Override
		public int getLasttimefushiconsumeweektotalamount() { // 最近周 消耗的符石数量
			_xdb_verify_unsafe_();
			return lasttimefushiconsumeweektotalamount;
		}

		@Override
		public long getFushibuytotalamount() { // 符石购买的总量,给log用,不影响活跃
			_xdb_verify_unsafe_();
			return fushibuytotalamount;
		}

		@Override
		public long getFushiselltotalamount() { // 符石出售的总量,给log用
			_xdb_verify_unsafe_();
			return fushiselltotalamount;
		}

		@Override
		public int getCashfushi() { // 剩余流通符石
			_xdb_verify_unsafe_();
			return cashfushi;
		}

		@Override
		public int getBindfushi() { // 剩余绑定rmb符石
			_xdb_verify_unsafe_();
			return bindfushi;
		}

		@Override
		public int getSysfushi() { // 剩余系统赠送符石
			_xdb_verify_unsafe_();
			return sysfushi;
		}

		@Override
		public int getFushiinplatform() { // 在交易平台里面的挂单符石
			_xdb_verify_unsafe_();
			return fushiinplatform;
		}

		@Override
		public void setFirsttimeenter(int _v_) { // 第一次进入服务器 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeenter(int _v_) { // 最近一次进入服务器 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveday1(int _v_) { // 最近两次活跃的 当前天 day1比day2新,下同
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveday2(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveweek1(int _v_) { // 最近两次活跃的 当前周 week1比week2新,下同
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveweek2(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttimeactiveday(int _v_) { // 第一次活跃的 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttimeactiveweek(int _v_) { // 第一次活跃的 当前周
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeday1(int _v_) { // 最近2次充值的当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeday2(int _v_) { // 最近2次充值的当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeweek1(int _v_) { // 最近2次充值的当前周
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeweek2(int _v_) { // 最近2次充值的当前周
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttimechargeday(int _v_) { // 第一次充值 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttimechargeweek(int _v_) { // 第一次充值 当前周
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeday(int _v_) { // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargedaytotalamount(int _v_) { // 最近一天充值的总额 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeweek(int _v_) { // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimechargeweektotalamount(int _v_) { // 最近一周充值的总额 当前周
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetotalamount(int _v_) { // 充值总额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetotalrmb(int _v_) { // 充值rmb总额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetotalamountlong(long _v_) { // 充值总额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetotalrmblong(long _v_) { // 充值rmb总额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetotalrmbuntillastday(int _v_) { // 截止昨天的充值总额，方便计算当日充值多少
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetotalrmbuntillastdaylong(long _v_) { // 截止昨天的充值总额，方便计算当日充值多少
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setConvertchargetotalamouttolong(int _v_) { // 是否已经把充值总额转变成了long型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushiconsumetotalamount(long _v_) { // 符石消耗的总量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCashfushiconsumetotalamount(long _v_) { // 现金充值符石消耗的总量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimefushiconsumeday(int _v_) { // 最近天 消耗符石,记录的是天数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimefushiconsumedaytotalamount(int _v_) { // 最近天 消耗的符石数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimefushiconsumeweek(int _v_) { // 最近周 消耗符石,记录的是周数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimefushiconsumeweektotalamount(int _v_) { // 最近周 消耗的符石数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushibuytotalamount(long _v_) { // 符石购买的总量,给log用,不影响活跃
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushiselltotalamount(long _v_) { // 符石出售的总量,给log用
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCashfushi(int _v_) { // 剩余流通符石
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindfushi(int _v_) { // 剩余绑定rmb符石
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSysfushi(int _v_) { // 剩余系统赠送符石
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushiinplatform(int _v_) { // 在交易平台里面的挂单符石
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
			return ActiveUserInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ActiveUserInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ActiveUserInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ActiveUserInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ActiveUserInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ActiveUserInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ActiveUserInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ActiveUserInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ActiveUserInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ActiveUserInfo {
		private int firsttimeenter; // 第一次进入服务器 当前天
		private int lasttimeenter; // 最近一次进入服务器 当前天
		private int lasttimeactiveday1; // 最近两次活跃的 当前天 day1比day2新,下同
		private int lasttimeactiveday2; // 
		private int lasttimeactiveweek1; // 最近两次活跃的 当前周 week1比week2新,下同
		private int lasttimeactiveweek2; // 
		private int firsttimeactiveday; // 第一次活跃的 当前天
		private int firsttimeactiveweek; // 第一次活跃的 当前周
		private int lasttimechargeday1; // 最近2次充值的当前天
		private int lasttimechargeday2; // 最近2次充值的当前天
		private int lasttimechargeweek1; // 最近2次充值的当前周
		private int lasttimechargeweek2; // 最近2次充值的当前周
		private int firsttimechargeday; // 第一次充值 当前天
		private int firsttimechargeweek; // 第一次充值 当前周
		private int lasttimechargeday; // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
		private int lasttimechargedaytotalamount; // 最近一天充值的总额 当前天
		private int lasttimechargeweek; // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
		private int lasttimechargeweektotalamount; // 最近一周充值的总额 当前周
		private int chargetotalamount; // 充值总额
		private int chargetotalrmb; // 充值rmb总额
		private long chargetotalamountlong; // 充值总额
		private long chargetotalrmblong; // 充值rmb总额
		private int chargetotalrmbuntillastday; // 截止昨天的充值总额，方便计算当日充值多少
		private long chargetotalrmbuntillastdaylong; // 截止昨天的充值总额，方便计算当日充值多少
		private int convertchargetotalamouttolong; // 是否已经把充值总额转变成了long型
		private long fushiconsumetotalamount; // 符石消耗的总量
		private long cashfushiconsumetotalamount; // 现金充值符石消耗的总量
		private int lasttimefushiconsumeday; // 最近天 消耗符石,记录的是天数
		private int lasttimefushiconsumedaytotalamount; // 最近天 消耗的符石数量
		private int lasttimefushiconsumeweek; // 最近周 消耗符石,记录的是周数
		private int lasttimefushiconsumeweektotalamount; // 最近周 消耗的符石数量
		private long fushibuytotalamount; // 符石购买的总量,给log用,不影响活跃
		private long fushiselltotalamount; // 符石出售的总量,给log用
		private int cashfushi; // 剩余流通符石
		private int bindfushi; // 剩余绑定rmb符石
		private int sysfushi; // 剩余系统赠送符石
		private int fushiinplatform; // 在交易平台里面的挂单符石

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ActiveUserInfo _o1_) {
			if (_o1_ instanceof ActiveUserInfo) assign((ActiveUserInfo)_o1_);
			else if (_o1_ instanceof ActiveUserInfo.Data) assign((ActiveUserInfo.Data)_o1_);
			else if (_o1_ instanceof ActiveUserInfo.Const) assign(((ActiveUserInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ActiveUserInfo _o_) {
			firsttimeenter = _o_.firsttimeenter;
			lasttimeenter = _o_.lasttimeenter;
			lasttimeactiveday1 = _o_.lasttimeactiveday1;
			lasttimeactiveday2 = _o_.lasttimeactiveday2;
			lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
			lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
			firsttimeactiveday = _o_.firsttimeactiveday;
			firsttimeactiveweek = _o_.firsttimeactiveweek;
			lasttimechargeday1 = _o_.lasttimechargeday1;
			lasttimechargeday2 = _o_.lasttimechargeday2;
			lasttimechargeweek1 = _o_.lasttimechargeweek1;
			lasttimechargeweek2 = _o_.lasttimechargeweek2;
			firsttimechargeday = _o_.firsttimechargeday;
			firsttimechargeweek = _o_.firsttimechargeweek;
			lasttimechargeday = _o_.lasttimechargeday;
			lasttimechargedaytotalamount = _o_.lasttimechargedaytotalamount;
			lasttimechargeweek = _o_.lasttimechargeweek;
			lasttimechargeweektotalamount = _o_.lasttimechargeweektotalamount;
			chargetotalamount = _o_.chargetotalamount;
			chargetotalrmb = _o_.chargetotalrmb;
			chargetotalamountlong = _o_.chargetotalamountlong;
			chargetotalrmblong = _o_.chargetotalrmblong;
			chargetotalrmbuntillastday = _o_.chargetotalrmbuntillastday;
			chargetotalrmbuntillastdaylong = _o_.chargetotalrmbuntillastdaylong;
			convertchargetotalamouttolong = _o_.convertchargetotalamouttolong;
			fushiconsumetotalamount = _o_.fushiconsumetotalamount;
			cashfushiconsumetotalamount = _o_.cashfushiconsumetotalamount;
			lasttimefushiconsumeday = _o_.lasttimefushiconsumeday;
			lasttimefushiconsumedaytotalamount = _o_.lasttimefushiconsumedaytotalamount;
			lasttimefushiconsumeweek = _o_.lasttimefushiconsumeweek;
			lasttimefushiconsumeweektotalamount = _o_.lasttimefushiconsumeweektotalamount;
			fushibuytotalamount = _o_.fushibuytotalamount;
			fushiselltotalamount = _o_.fushiselltotalamount;
			cashfushi = _o_.cashfushi;
			bindfushi = _o_.bindfushi;
			sysfushi = _o_.sysfushi;
			fushiinplatform = _o_.fushiinplatform;
		}

		private void assign(ActiveUserInfo.Data _o_) {
			firsttimeenter = _o_.firsttimeenter;
			lasttimeenter = _o_.lasttimeenter;
			lasttimeactiveday1 = _o_.lasttimeactiveday1;
			lasttimeactiveday2 = _o_.lasttimeactiveday2;
			lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
			lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
			firsttimeactiveday = _o_.firsttimeactiveday;
			firsttimeactiveweek = _o_.firsttimeactiveweek;
			lasttimechargeday1 = _o_.lasttimechargeday1;
			lasttimechargeday2 = _o_.lasttimechargeday2;
			lasttimechargeweek1 = _o_.lasttimechargeweek1;
			lasttimechargeweek2 = _o_.lasttimechargeweek2;
			firsttimechargeday = _o_.firsttimechargeday;
			firsttimechargeweek = _o_.firsttimechargeweek;
			lasttimechargeday = _o_.lasttimechargeday;
			lasttimechargedaytotalamount = _o_.lasttimechargedaytotalamount;
			lasttimechargeweek = _o_.lasttimechargeweek;
			lasttimechargeweektotalamount = _o_.lasttimechargeweektotalamount;
			chargetotalamount = _o_.chargetotalamount;
			chargetotalrmb = _o_.chargetotalrmb;
			chargetotalamountlong = _o_.chargetotalamountlong;
			chargetotalrmblong = _o_.chargetotalrmblong;
			chargetotalrmbuntillastday = _o_.chargetotalrmbuntillastday;
			chargetotalrmbuntillastdaylong = _o_.chargetotalrmbuntillastdaylong;
			convertchargetotalamouttolong = _o_.convertchargetotalamouttolong;
			fushiconsumetotalamount = _o_.fushiconsumetotalamount;
			cashfushiconsumetotalamount = _o_.cashfushiconsumetotalamount;
			lasttimefushiconsumeday = _o_.lasttimefushiconsumeday;
			lasttimefushiconsumedaytotalamount = _o_.lasttimefushiconsumedaytotalamount;
			lasttimefushiconsumeweek = _o_.lasttimefushiconsumeweek;
			lasttimefushiconsumeweektotalamount = _o_.lasttimefushiconsumeweektotalamount;
			fushibuytotalamount = _o_.fushibuytotalamount;
			fushiselltotalamount = _o_.fushiselltotalamount;
			cashfushi = _o_.cashfushi;
			bindfushi = _o_.bindfushi;
			sysfushi = _o_.sysfushi;
			fushiinplatform = _o_.fushiinplatform;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(firsttimeenter);
			_os_.marshal(lasttimeenter);
			_os_.marshal(lasttimeactiveday1);
			_os_.marshal(lasttimeactiveday2);
			_os_.marshal(lasttimeactiveweek1);
			_os_.marshal(lasttimeactiveweek2);
			_os_.marshal(firsttimeactiveday);
			_os_.marshal(firsttimeactiveweek);
			_os_.marshal(lasttimechargeday1);
			_os_.marshal(lasttimechargeday2);
			_os_.marshal(lasttimechargeweek1);
			_os_.marshal(lasttimechargeweek2);
			_os_.marshal(firsttimechargeday);
			_os_.marshal(firsttimechargeweek);
			_os_.marshal(lasttimechargeday);
			_os_.marshal(lasttimechargedaytotalamount);
			_os_.marshal(lasttimechargeweek);
			_os_.marshal(lasttimechargeweektotalamount);
			_os_.marshal(chargetotalamount);
			_os_.marshal(chargetotalrmb);
			_os_.marshal(chargetotalamountlong);
			_os_.marshal(chargetotalrmblong);
			_os_.marshal(chargetotalrmbuntillastday);
			_os_.marshal(chargetotalrmbuntillastdaylong);
			_os_.marshal(convertchargetotalamouttolong);
			_os_.marshal(fushiconsumetotalamount);
			_os_.marshal(cashfushiconsumetotalamount);
			_os_.marshal(lasttimefushiconsumeday);
			_os_.marshal(lasttimefushiconsumedaytotalamount);
			_os_.marshal(lasttimefushiconsumeweek);
			_os_.marshal(lasttimefushiconsumeweektotalamount);
			_os_.marshal(fushibuytotalamount);
			_os_.marshal(fushiselltotalamount);
			_os_.marshal(cashfushi);
			_os_.marshal(bindfushi);
			_os_.marshal(sysfushi);
			_os_.marshal(fushiinplatform);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			firsttimeenter = _os_.unmarshal_int();
			lasttimeenter = _os_.unmarshal_int();
			lasttimeactiveday1 = _os_.unmarshal_int();
			lasttimeactiveday2 = _os_.unmarshal_int();
			lasttimeactiveweek1 = _os_.unmarshal_int();
			lasttimeactiveweek2 = _os_.unmarshal_int();
			firsttimeactiveday = _os_.unmarshal_int();
			firsttimeactiveweek = _os_.unmarshal_int();
			lasttimechargeday1 = _os_.unmarshal_int();
			lasttimechargeday2 = _os_.unmarshal_int();
			lasttimechargeweek1 = _os_.unmarshal_int();
			lasttimechargeweek2 = _os_.unmarshal_int();
			firsttimechargeday = _os_.unmarshal_int();
			firsttimechargeweek = _os_.unmarshal_int();
			lasttimechargeday = _os_.unmarshal_int();
			lasttimechargedaytotalamount = _os_.unmarshal_int();
			lasttimechargeweek = _os_.unmarshal_int();
			lasttimechargeweektotalamount = _os_.unmarshal_int();
			chargetotalamount = _os_.unmarshal_int();
			chargetotalrmb = _os_.unmarshal_int();
			chargetotalamountlong = _os_.unmarshal_long();
			chargetotalrmblong = _os_.unmarshal_long();
			chargetotalrmbuntillastday = _os_.unmarshal_int();
			chargetotalrmbuntillastdaylong = _os_.unmarshal_long();
			convertchargetotalamouttolong = _os_.unmarshal_int();
			fushiconsumetotalamount = _os_.unmarshal_long();
			cashfushiconsumetotalamount = _os_.unmarshal_long();
			lasttimefushiconsumeday = _os_.unmarshal_int();
			lasttimefushiconsumedaytotalamount = _os_.unmarshal_int();
			lasttimefushiconsumeweek = _os_.unmarshal_int();
			lasttimefushiconsumeweektotalamount = _os_.unmarshal_int();
			fushibuytotalamount = _os_.unmarshal_long();
			fushiselltotalamount = _os_.unmarshal_long();
			cashfushi = _os_.unmarshal_int();
			bindfushi = _os_.unmarshal_int();
			sysfushi = _os_.unmarshal_int();
			fushiinplatform = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ActiveUserInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ActiveUserInfo toData() {
			return new Data(this);
		}

		public xbean.ActiveUserInfo toBean() {
			return new ActiveUserInfo(this, null, null);
		}

		@Override
		public xbean.ActiveUserInfo toDataIf() {
			return this;
		}

		public xbean.ActiveUserInfo toBeanIf() {
			return new ActiveUserInfo(this, null, null);
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
		public int getFirsttimeenter() { // 第一次进入服务器 当前天
			return firsttimeenter;
		}

		@Override
		public int getLasttimeenter() { // 最近一次进入服务器 当前天
			return lasttimeenter;
		}

		@Override
		public int getLasttimeactiveday1() { // 最近两次活跃的 当前天 day1比day2新,下同
			return lasttimeactiveday1;
		}

		@Override
		public int getLasttimeactiveday2() { // 
			return lasttimeactiveday2;
		}

		@Override
		public int getLasttimeactiveweek1() { // 最近两次活跃的 当前周 week1比week2新,下同
			return lasttimeactiveweek1;
		}

		@Override
		public int getLasttimeactiveweek2() { // 
			return lasttimeactiveweek2;
		}

		@Override
		public int getFirsttimeactiveday() { // 第一次活跃的 当前天
			return firsttimeactiveday;
		}

		@Override
		public int getFirsttimeactiveweek() { // 第一次活跃的 当前周
			return firsttimeactiveweek;
		}

		@Override
		public int getLasttimechargeday1() { // 最近2次充值的当前天
			return lasttimechargeday1;
		}

		@Override
		public int getLasttimechargeday2() { // 最近2次充值的当前天
			return lasttimechargeday2;
		}

		@Override
		public int getLasttimechargeweek1() { // 最近2次充值的当前周
			return lasttimechargeweek1;
		}

		@Override
		public int getLasttimechargeweek2() { // 最近2次充值的当前周
			return lasttimechargeweek2;
		}

		@Override
		public int getFirsttimechargeday() { // 第一次充值 当前天
			return firsttimechargeday;
		}

		@Override
		public int getFirsttimechargeweek() { // 第一次充值 当前周
			return firsttimechargeweek;
		}

		@Override
		public int getLasttimechargeday() { // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
			return lasttimechargeday;
		}

		@Override
		public int getLasttimechargedaytotalamount() { // 最近一天充值的总额 当前天
			return lasttimechargedaytotalamount;
		}

		@Override
		public int getLasttimechargeweek() { // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
			return lasttimechargeweek;
		}

		@Override
		public int getLasttimechargeweektotalamount() { // 最近一周充值的总额 当前周
			return lasttimechargeweektotalamount;
		}

		@Override
		public int getChargetotalamount() { // 充值总额
			return chargetotalamount;
		}

		@Override
		public int getChargetotalrmb() { // 充值rmb总额
			return chargetotalrmb;
		}

		@Override
		public long getChargetotalamountlong() { // 充值总额
			return chargetotalamountlong;
		}

		@Override
		public long getChargetotalrmblong() { // 充值rmb总额
			return chargetotalrmblong;
		}

		@Override
		public int getChargetotalrmbuntillastday() { // 截止昨天的充值总额，方便计算当日充值多少
			return chargetotalrmbuntillastday;
		}

		@Override
		public long getChargetotalrmbuntillastdaylong() { // 截止昨天的充值总额，方便计算当日充值多少
			return chargetotalrmbuntillastdaylong;
		}

		@Override
		public int getConvertchargetotalamouttolong() { // 是否已经把充值总额转变成了long型
			return convertchargetotalamouttolong;
		}

		@Override
		public long getFushiconsumetotalamount() { // 符石消耗的总量
			return fushiconsumetotalamount;
		}

		@Override
		public long getCashfushiconsumetotalamount() { // 现金充值符石消耗的总量
			return cashfushiconsumetotalamount;
		}

		@Override
		public int getLasttimefushiconsumeday() { // 最近天 消耗符石,记录的是天数
			return lasttimefushiconsumeday;
		}

		@Override
		public int getLasttimefushiconsumedaytotalamount() { // 最近天 消耗的符石数量
			return lasttimefushiconsumedaytotalamount;
		}

		@Override
		public int getLasttimefushiconsumeweek() { // 最近周 消耗符石,记录的是周数
			return lasttimefushiconsumeweek;
		}

		@Override
		public int getLasttimefushiconsumeweektotalamount() { // 最近周 消耗的符石数量
			return lasttimefushiconsumeweektotalamount;
		}

		@Override
		public long getFushibuytotalamount() { // 符石购买的总量,给log用,不影响活跃
			return fushibuytotalamount;
		}

		@Override
		public long getFushiselltotalamount() { // 符石出售的总量,给log用
			return fushiselltotalamount;
		}

		@Override
		public int getCashfushi() { // 剩余流通符石
			return cashfushi;
		}

		@Override
		public int getBindfushi() { // 剩余绑定rmb符石
			return bindfushi;
		}

		@Override
		public int getSysfushi() { // 剩余系统赠送符石
			return sysfushi;
		}

		@Override
		public int getFushiinplatform() { // 在交易平台里面的挂单符石
			return fushiinplatform;
		}

		@Override
		public void setFirsttimeenter(int _v_) { // 第一次进入服务器 当前天
			firsttimeenter = _v_;
		}

		@Override
		public void setLasttimeenter(int _v_) { // 最近一次进入服务器 当前天
			lasttimeenter = _v_;
		}

		@Override
		public void setLasttimeactiveday1(int _v_) { // 最近两次活跃的 当前天 day1比day2新,下同
			lasttimeactiveday1 = _v_;
		}

		@Override
		public void setLasttimeactiveday2(int _v_) { // 
			lasttimeactiveday2 = _v_;
		}

		@Override
		public void setLasttimeactiveweek1(int _v_) { // 最近两次活跃的 当前周 week1比week2新,下同
			lasttimeactiveweek1 = _v_;
		}

		@Override
		public void setLasttimeactiveweek2(int _v_) { // 
			lasttimeactiveweek2 = _v_;
		}

		@Override
		public void setFirsttimeactiveday(int _v_) { // 第一次活跃的 当前天
			firsttimeactiveday = _v_;
		}

		@Override
		public void setFirsttimeactiveweek(int _v_) { // 第一次活跃的 当前周
			firsttimeactiveweek = _v_;
		}

		@Override
		public void setLasttimechargeday1(int _v_) { // 最近2次充值的当前天
			lasttimechargeday1 = _v_;
		}

		@Override
		public void setLasttimechargeday2(int _v_) { // 最近2次充值的当前天
			lasttimechargeday2 = _v_;
		}

		@Override
		public void setLasttimechargeweek1(int _v_) { // 最近2次充值的当前周
			lasttimechargeweek1 = _v_;
		}

		@Override
		public void setLasttimechargeweek2(int _v_) { // 最近2次充值的当前周
			lasttimechargeweek2 = _v_;
		}

		@Override
		public void setFirsttimechargeday(int _v_) { // 第一次充值 当前天
			firsttimechargeday = _v_;
		}

		@Override
		public void setFirsttimechargeweek(int _v_) { // 第一次充值 当前周
			firsttimechargeweek = _v_;
		}

		@Override
		public void setLasttimechargeday(int _v_) { // 最近一天充值 当前天,理论上跟lasttimeChargeDay1是相等的
			lasttimechargeday = _v_;
		}

		@Override
		public void setLasttimechargedaytotalamount(int _v_) { // 最近一天充值的总额 当前天
			lasttimechargedaytotalamount = _v_;
		}

		@Override
		public void setLasttimechargeweek(int _v_) { // 最近一周充值 当前周,理论上跟lasttimeChargeWeek1相等
			lasttimechargeweek = _v_;
		}

		@Override
		public void setLasttimechargeweektotalamount(int _v_) { // 最近一周充值的总额 当前周
			lasttimechargeweektotalamount = _v_;
		}

		@Override
		public void setChargetotalamount(int _v_) { // 充值总额
			chargetotalamount = _v_;
		}

		@Override
		public void setChargetotalrmb(int _v_) { // 充值rmb总额
			chargetotalrmb = _v_;
		}

		@Override
		public void setChargetotalamountlong(long _v_) { // 充值总额
			chargetotalamountlong = _v_;
		}

		@Override
		public void setChargetotalrmblong(long _v_) { // 充值rmb总额
			chargetotalrmblong = _v_;
		}

		@Override
		public void setChargetotalrmbuntillastday(int _v_) { // 截止昨天的充值总额，方便计算当日充值多少
			chargetotalrmbuntillastday = _v_;
		}

		@Override
		public void setChargetotalrmbuntillastdaylong(long _v_) { // 截止昨天的充值总额，方便计算当日充值多少
			chargetotalrmbuntillastdaylong = _v_;
		}

		@Override
		public void setConvertchargetotalamouttolong(int _v_) { // 是否已经把充值总额转变成了long型
			convertchargetotalamouttolong = _v_;
		}

		@Override
		public void setFushiconsumetotalamount(long _v_) { // 符石消耗的总量
			fushiconsumetotalamount = _v_;
		}

		@Override
		public void setCashfushiconsumetotalamount(long _v_) { // 现金充值符石消耗的总量
			cashfushiconsumetotalamount = _v_;
		}

		@Override
		public void setLasttimefushiconsumeday(int _v_) { // 最近天 消耗符石,记录的是天数
			lasttimefushiconsumeday = _v_;
		}

		@Override
		public void setLasttimefushiconsumedaytotalamount(int _v_) { // 最近天 消耗的符石数量
			lasttimefushiconsumedaytotalamount = _v_;
		}

		@Override
		public void setLasttimefushiconsumeweek(int _v_) { // 最近周 消耗符石,记录的是周数
			lasttimefushiconsumeweek = _v_;
		}

		@Override
		public void setLasttimefushiconsumeweektotalamount(int _v_) { // 最近周 消耗的符石数量
			lasttimefushiconsumeweektotalamount = _v_;
		}

		@Override
		public void setFushibuytotalamount(long _v_) { // 符石购买的总量,给log用,不影响活跃
			fushibuytotalamount = _v_;
		}

		@Override
		public void setFushiselltotalamount(long _v_) { // 符石出售的总量,给log用
			fushiselltotalamount = _v_;
		}

		@Override
		public void setCashfushi(int _v_) { // 剩余流通符石
			cashfushi = _v_;
		}

		@Override
		public void setBindfushi(int _v_) { // 剩余绑定rmb符石
			bindfushi = _v_;
		}

		@Override
		public void setSysfushi(int _v_) { // 剩余系统赠送符石
			sysfushi = _v_;
		}

		@Override
		public void setFushiinplatform(int _v_) { // 在交易平台里面的挂单符石
			fushiinplatform = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ActiveUserInfo.Data)) return false;
			ActiveUserInfo.Data _o_ = (ActiveUserInfo.Data) _o1_;
			if (firsttimeenter != _o_.firsttimeenter) return false;
			if (lasttimeenter != _o_.lasttimeenter) return false;
			if (lasttimeactiveday1 != _o_.lasttimeactiveday1) return false;
			if (lasttimeactiveday2 != _o_.lasttimeactiveday2) return false;
			if (lasttimeactiveweek1 != _o_.lasttimeactiveweek1) return false;
			if (lasttimeactiveweek2 != _o_.lasttimeactiveweek2) return false;
			if (firsttimeactiveday != _o_.firsttimeactiveday) return false;
			if (firsttimeactiveweek != _o_.firsttimeactiveweek) return false;
			if (lasttimechargeday1 != _o_.lasttimechargeday1) return false;
			if (lasttimechargeday2 != _o_.lasttimechargeday2) return false;
			if (lasttimechargeweek1 != _o_.lasttimechargeweek1) return false;
			if (lasttimechargeweek2 != _o_.lasttimechargeweek2) return false;
			if (firsttimechargeday != _o_.firsttimechargeday) return false;
			if (firsttimechargeweek != _o_.firsttimechargeweek) return false;
			if (lasttimechargeday != _o_.lasttimechargeday) return false;
			if (lasttimechargedaytotalamount != _o_.lasttimechargedaytotalamount) return false;
			if (lasttimechargeweek != _o_.lasttimechargeweek) return false;
			if (lasttimechargeweektotalamount != _o_.lasttimechargeweektotalamount) return false;
			if (chargetotalamount != _o_.chargetotalamount) return false;
			if (chargetotalrmb != _o_.chargetotalrmb) return false;
			if (chargetotalamountlong != _o_.chargetotalamountlong) return false;
			if (chargetotalrmblong != _o_.chargetotalrmblong) return false;
			if (chargetotalrmbuntillastday != _o_.chargetotalrmbuntillastday) return false;
			if (chargetotalrmbuntillastdaylong != _o_.chargetotalrmbuntillastdaylong) return false;
			if (convertchargetotalamouttolong != _o_.convertchargetotalamouttolong) return false;
			if (fushiconsumetotalamount != _o_.fushiconsumetotalamount) return false;
			if (cashfushiconsumetotalamount != _o_.cashfushiconsumetotalamount) return false;
			if (lasttimefushiconsumeday != _o_.lasttimefushiconsumeday) return false;
			if (lasttimefushiconsumedaytotalamount != _o_.lasttimefushiconsumedaytotalamount) return false;
			if (lasttimefushiconsumeweek != _o_.lasttimefushiconsumeweek) return false;
			if (lasttimefushiconsumeweektotalamount != _o_.lasttimefushiconsumeweektotalamount) return false;
			if (fushibuytotalamount != _o_.fushibuytotalamount) return false;
			if (fushiselltotalamount != _o_.fushiselltotalamount) return false;
			if (cashfushi != _o_.cashfushi) return false;
			if (bindfushi != _o_.bindfushi) return false;
			if (sysfushi != _o_.sysfushi) return false;
			if (fushiinplatform != _o_.fushiinplatform) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += firsttimeenter;
			_h_ += lasttimeenter;
			_h_ += lasttimeactiveday1;
			_h_ += lasttimeactiveday2;
			_h_ += lasttimeactiveweek1;
			_h_ += lasttimeactiveweek2;
			_h_ += firsttimeactiveday;
			_h_ += firsttimeactiveweek;
			_h_ += lasttimechargeday1;
			_h_ += lasttimechargeday2;
			_h_ += lasttimechargeweek1;
			_h_ += lasttimechargeweek2;
			_h_ += firsttimechargeday;
			_h_ += firsttimechargeweek;
			_h_ += lasttimechargeday;
			_h_ += lasttimechargedaytotalamount;
			_h_ += lasttimechargeweek;
			_h_ += lasttimechargeweektotalamount;
			_h_ += chargetotalamount;
			_h_ += chargetotalrmb;
			_h_ += chargetotalamountlong;
			_h_ += chargetotalrmblong;
			_h_ += chargetotalrmbuntillastday;
			_h_ += chargetotalrmbuntillastdaylong;
			_h_ += convertchargetotalamouttolong;
			_h_ += fushiconsumetotalamount;
			_h_ += cashfushiconsumetotalamount;
			_h_ += lasttimefushiconsumeday;
			_h_ += lasttimefushiconsumedaytotalamount;
			_h_ += lasttimefushiconsumeweek;
			_h_ += lasttimefushiconsumeweektotalamount;
			_h_ += fushibuytotalamount;
			_h_ += fushiselltotalamount;
			_h_ += cashfushi;
			_h_ += bindfushi;
			_h_ += sysfushi;
			_h_ += fushiinplatform;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(firsttimeenter);
			_sb_.append(",");
			_sb_.append(lasttimeenter);
			_sb_.append(",");
			_sb_.append(lasttimeactiveday1);
			_sb_.append(",");
			_sb_.append(lasttimeactiveday2);
			_sb_.append(",");
			_sb_.append(lasttimeactiveweek1);
			_sb_.append(",");
			_sb_.append(lasttimeactiveweek2);
			_sb_.append(",");
			_sb_.append(firsttimeactiveday);
			_sb_.append(",");
			_sb_.append(firsttimeactiveweek);
			_sb_.append(",");
			_sb_.append(lasttimechargeday1);
			_sb_.append(",");
			_sb_.append(lasttimechargeday2);
			_sb_.append(",");
			_sb_.append(lasttimechargeweek1);
			_sb_.append(",");
			_sb_.append(lasttimechargeweek2);
			_sb_.append(",");
			_sb_.append(firsttimechargeday);
			_sb_.append(",");
			_sb_.append(firsttimechargeweek);
			_sb_.append(",");
			_sb_.append(lasttimechargeday);
			_sb_.append(",");
			_sb_.append(lasttimechargedaytotalamount);
			_sb_.append(",");
			_sb_.append(lasttimechargeweek);
			_sb_.append(",");
			_sb_.append(lasttimechargeweektotalamount);
			_sb_.append(",");
			_sb_.append(chargetotalamount);
			_sb_.append(",");
			_sb_.append(chargetotalrmb);
			_sb_.append(",");
			_sb_.append(chargetotalamountlong);
			_sb_.append(",");
			_sb_.append(chargetotalrmblong);
			_sb_.append(",");
			_sb_.append(chargetotalrmbuntillastday);
			_sb_.append(",");
			_sb_.append(chargetotalrmbuntillastdaylong);
			_sb_.append(",");
			_sb_.append(convertchargetotalamouttolong);
			_sb_.append(",");
			_sb_.append(fushiconsumetotalamount);
			_sb_.append(",");
			_sb_.append(cashfushiconsumetotalamount);
			_sb_.append(",");
			_sb_.append(lasttimefushiconsumeday);
			_sb_.append(",");
			_sb_.append(lasttimefushiconsumedaytotalamount);
			_sb_.append(",");
			_sb_.append(lasttimefushiconsumeweek);
			_sb_.append(",");
			_sb_.append(lasttimefushiconsumeweektotalamount);
			_sb_.append(",");
			_sb_.append(fushibuytotalamount);
			_sb_.append(",");
			_sb_.append(fushiselltotalamount);
			_sb_.append(",");
			_sb_.append(cashfushi);
			_sb_.append(",");
			_sb_.append(bindfushi);
			_sb_.append(",");
			_sb_.append(sysfushi);
			_sb_.append(",");
			_sb_.append(fushiinplatform);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
