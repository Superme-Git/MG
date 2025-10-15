
package xbean;

public interface ActivityQuestion extends mkdb.Bean {
	public ActivityQuestion copy(); // deep clone
	public ActivityQuestion toData(); // a Data instance
	public ActivityQuestion toBean(); // a Bean instance
	public ActivityQuestion toDataIf(); // a Data instance If need. else return this
	public ActivityQuestion toBeanIf(); // a Bean instance If need. else return this

	public long getActivityquestionstarttime(); // 活动答题开启时间 by changhao
	public java.util.List<Integer> getQuestionids(); // 题目 by changhao
	public java.util.List<Integer> getQuestionidsAsData(); // 题目 by changhao
	public int getCurquestionindex(); // 当前回答的问题 by changhao
	public int getCurquestionstep(); // 当前回答的步骤 by changhao
	public int getAnswerrighttimes(); // 回答正确的次数 by changhao
	public int getTotalanswernum(); // 总共回答的数量 by changhao
	public int getGrabreward(); // 1是可以领取2是已经领取3是不能领取 by changhao
	public int getTotalexp(); // 总经验 by changhao
	public int getTotalmoney(); // 总金币 by changhao
	public int getHelptimes(); // 帮助次数 by changhao

	public void setActivityquestionstarttime(long _v_); // 活动答题开启时间 by changhao
	public void setCurquestionindex(int _v_); // 当前回答的问题 by changhao
	public void setCurquestionstep(int _v_); // 当前回答的步骤 by changhao
	public void setAnswerrighttimes(int _v_); // 回答正确的次数 by changhao
	public void setTotalanswernum(int _v_); // 总共回答的数量 by changhao
	public void setGrabreward(int _v_); // 1是可以领取2是已经领取3是不能领取 by changhao
	public void setTotalexp(int _v_); // 总经验 by changhao
	public void setTotalmoney(int _v_); // 总金币 by changhao
	public void setHelptimes(int _v_); // 帮助次数 by changhao
}
