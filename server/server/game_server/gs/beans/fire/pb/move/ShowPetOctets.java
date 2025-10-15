
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ShowPetOctets implements Marshal {
	public int showpetid; // 角色展示宠物相关
	public java.lang.String showpetname; // 角色展示的宠物相关
	public short petcoloursndsize; // 高四位：宠物的颜色 [1,8]，低四位：宠物大小[1,5]
	public byte showskilleffect; // 气质类技能的特效ID[1,128]
	public byte evolvelevel; // 宠物进化等级

	public ShowPetOctets() {
		showpetname = "";
	}

	public ShowPetOctets(int _showpetid_, java.lang.String _showpetname_, short _petcoloursndsize_, byte _showskilleffect_, byte _evolvelevel_) {
		this.showpetid = _showpetid_;
		this.showpetname = _showpetname_;
		this.petcoloursndsize = _petcoloursndsize_;
		this.showskilleffect = _showskilleffect_;
		this.evolvelevel = _evolvelevel_;
	}

	public final boolean _validator_() {
		if (showpetid < -1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(showpetid);
		_os_.marshal(showpetname, "UTF-16LE");
		_os_.marshal(petcoloursndsize);
		_os_.marshal(showskilleffect);
		_os_.marshal(evolvelevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		showpetid = _os_.unmarshal_int();
		showpetname = _os_.unmarshal_String("UTF-16LE");
		petcoloursndsize = _os_.unmarshal_short();
		showskilleffect = _os_.unmarshal_byte();
		evolvelevel = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ShowPetOctets) {
			ShowPetOctets _o_ = (ShowPetOctets)_o1_;
			if (showpetid != _o_.showpetid) return false;
			if (!showpetname.equals(_o_.showpetname)) return false;
			if (petcoloursndsize != _o_.petcoloursndsize) return false;
			if (showskilleffect != _o_.showskilleffect) return false;
			if (evolvelevel != _o_.evolvelevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += showpetid;
		_h_ += showpetname.hashCode();
		_h_ += petcoloursndsize;
		_h_ += (int)showskilleffect;
		_h_ += (int)evolvelevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(showpetid).append(",");
		_sb_.append("T").append(showpetname.length()).append(",");
		_sb_.append(petcoloursndsize).append(",");
		_sb_.append(showskilleffect).append(",");
		_sb_.append(evolvelevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

