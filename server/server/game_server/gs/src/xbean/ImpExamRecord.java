
package xbean;

public interface ImpExamRecord extends mkdb.Bean {
	public ImpExamRecord copy(); // deep clone
	public ImpExamRecord toData(); // a Data instance
	public ImpExamRecord toBean(); // a Bean instance
	public ImpExamRecord toDataIf(); // a Data instance If need. else return this
	public ImpExamRecord toBeanIf(); // a Bean instance If need. else return this

	public int getMaxvillexamright(); // villexam历史最多答对题目数
	public int getCurrexamaccexp(); // 本次测试累积获得多少经验
	public int getCurrexamaccmoney(); // 本次测试累积获得多少钱
	public int getAccrightnum(); // 累积答对次数(vill---prov--state)
	public int getCurrexamaccright(); // 当前测试答对次数(vill或prov或state)
	public int getDelwrongval(); // 地王令
	public int getChorightval(); // 天王令
	public int getMaxcontinueright(); // 最多连续答对次数(vill---prov--state)
	public java.util.Map<Integer, Integer> getRightquesmap(); // 每一卷考卷答对的题目map
	public java.util.Map<Integer, Integer> getRightquesmapAsData(); // 每一卷考卷答对的题目map
	public long getExamsendquestime(); // 上一次发题时间(prov专用)
	public long getExamconsumetime(); // 本次测试消耗时间(prov专用)
	public long getMinstateexamtime(); // stateexam历史最短时间
	public int getMaxstateexamright(); // stateexam历史最多答对题目数
	public long getStateexamtime(); // 通过stateexam所用的时间
	public long getLastanswertime(); // 最后一次答题时间
	public int getHelpcnt(); // 求助次数
	public int getStep(); // 当前进度 1=vill 2=prov 3=state
	public int getFlag(); // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
	public java.util.List<Integer> getQuesidlist(); // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
	public java.util.List<Integer> getQuesidlistAsData(); // 题目的List,一开始的时候就生成,玩家每答完一题就发送下一题
	public int getLastquesid(); // 最近一道题序号
	public int getLastanswer(); // 最近一道题的答案
	public int getLastisright(); // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
	public java.util.List<Integer> getWorngqueslist(); // 最近一道题的错误答案List(prov专用)
	public java.util.List<Integer> getWorngqueslistAsData(); // 最近一道题的错误答案List(prov专用)
	public long getBegintime(); // 开始答题时间

	public void setMaxvillexamright(int _v_); // villexam历史最多答对题目数
	public void setCurrexamaccexp(int _v_); // 本次测试累积获得多少经验
	public void setCurrexamaccmoney(int _v_); // 本次测试累积获得多少钱
	public void setAccrightnum(int _v_); // 累积答对次数(vill---prov--state)
	public void setCurrexamaccright(int _v_); // 当前测试答对次数(vill或prov或state)
	public void setDelwrongval(int _v_); // 地王令
	public void setChorightval(int _v_); // 天王令
	public void setMaxcontinueright(int _v_); // 最多连续答对次数(vill---prov--state)
	public void setExamsendquestime(long _v_); // 上一次发题时间(prov专用)
	public void setExamconsumetime(long _v_); // 本次测试消耗时间(prov专用)
	public void setMinstateexamtime(long _v_); // stateexam历史最短时间
	public void setMaxstateexamright(int _v_); // stateexam历史最多答对题目数
	public void setStateexamtime(long _v_); // 通过stateexam所用的时间
	public void setLastanswertime(long _v_); // 最后一次答题时间
	public void setHelpcnt(int _v_); // 求助次数
	public void setStep(int _v_); // 当前进度 1=vill 2=prov 3=state
	public void setFlag(int _v_); // 1=答题中 2=放弃 3=过关失败 4=答题顺利通过
	public void setLastquesid(int _v_); // 最近一道题序号
	public void setLastanswer(int _v_); // 最近一道题的答案
	public void setLastisright(int _v_); // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
	public void setBegintime(long _v_); // 开始答题时间
}
