
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 师徒系统 定义协议
*/
public class DataConfig implements Marshal , Comparable<DataConfig>{
	public final static int REG_MONEY = 20000; // 登记师父扣除的游戏币
	public final static int MONEY1 = 10000; // 徒弟3——7天不在线，解除关系后需扣除师父10000游戏币
	public final static int MONEY2 = 100000; // 徒弟3天以内不在线，解除关系后需扣除师父100000游戏币
	public final static int REN_QI = 100; // 徒弟3天以内不在线，解除关系后需扣除师父100人气值
	public final static int TAIXUEFUZI_ID = 10215; // 太学夫子id
	public final static int EVALUATER_LEVEL1 = 120; // 师父收益一
	public final static int EVALUATER_LEVEL2 = 100; // 师父收益二
	public final static int EVALUATER_LEVEL3 = 80; // 师父收益三
	public final static int EVALUATER_LEVEL4 = 60; // 师父收益四
	public final static int EVALUATER_LEVEL5 = 40; // 师父收益五


	public DataConfig() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DataConfig) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(DataConfig _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

