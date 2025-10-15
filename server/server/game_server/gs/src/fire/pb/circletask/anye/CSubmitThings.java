
package fire.pb.circletask.anye;
import fire.pb.HelpCountManage;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.npc.SubmitType;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSubmitThings__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSubmitThings extends __CSubmitThings__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		fire.pb.item.Pack submiterBag = new fire.pb.item.Pack(roleid, true);
		for (SubmitThing unit : things) {
			if (submittype == SubmitType.PET)
				continue;
			if (submittype == SubmitType.MONEY) {
				long money = unit.key;
				if (submiterBag.getMoney() < money)
					continue;
			} else {
				ItemBase bi = submiterBag.getItem(unit.key);
				if (bi == null) {
					continue;
				}
				if (bi.getNumber() < unit.num) {
					continue;
				}
			}
		}
		
		java.util.List<xbean.PetInfo> pis = null;
		if (submittype == SubmitType.PET) {
			pis = new java.util.ArrayList<xbean.PetInfo>();
			final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(roleid, PetColumnTypes.PET, true);
			for (SubmitThing unit : things) {
				fire.pb.pet.Pet pet = petcol.getPet(unit.key);
				if (pet == null || pet.isLocked() != -1)
					return;
				if (petcol.petIsFightPet(unit.key) || petcol.petIsShowPet(unit.key)) {
					MessageMgr.sendMsgNotify(roleid, 144418, null);
					return;
				}
				pis.add(pet.getPetInfo().copy());
			}
		}
		
		if (submittype == SubmitType.ITEM) {
			Pack bag = new Pack(roleid, true);
			for (SubmitThing unit : things) {
				ItemBase item = bag.getItem(unit.key);
				if (item == null)
					return;
			}
		}
		
		if (taskid == 1080000 && roleid != taskrole) {
			//判断我的援助次数是否已满
			//添加援助等级限制
			int submiterLv = xtable.Properties.selectLevel(roleid);
			if(submiterLv<fire.pb.circletask.PSubmitThings.getGiveHelpLevel()){
				MessageMgr.sendMsgNotify(roleid, 145390, null);
	    		return;
			}
			
			xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
	    	int max=HelpCountManage.getInstance().getHelpGiveItemNumMax(roleid);
	    	if(helpcount!=null&&helpcount.getHelpgiveitemnum()>=max){
	    		MessageMgr.sendMsgNotify(roleid, 166088, null);
	    		return;
	    	}
	    	//判断目标请求次数是否已满
	    	xbean.helpcount taskrolehelpcount = xtable.Helpcount.select((long) taskrole);
	    	int maxtaskrolehelpcount=HelpCountManage.getInstance().getHelpItemNumMax(taskrole);
	    	if(taskrolehelpcount!=null&&taskrolehelpcount.getHelpitemnum()>=maxtaskrolehelpcount){
	    		MessageMgr.sendMsgNotify(roleid, 166093, null);
	    		return;
	    	}
		}
		
		fire.pb.circletask.PSubmitThings proc= new fire.pb.circletask.PSubmitThings(roleid,taskrole,things,taskpos);
		mkdb.Procedure.execute(proc);
		return;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807455;

	public int getType() {
		return 807455;
	}

	public int taskpos; // 任务栏位
	public int taskid; // 任务id
	public long taskrole; // 任务所属角色id(帮别人提交物品时用到)
	public int submittype; // 提交的类型 1道具 2宠物 3金钱
	public java.util.ArrayList<fire.pb.circletask.anye.SubmitThing> things; // 提交的道具key 或者宠物的key

	public CSubmitThings() {
		things = new java.util.ArrayList<fire.pb.circletask.anye.SubmitThing>();
	}

	public CSubmitThings(int _taskpos_, int _taskid_, long _taskrole_, int _submittype_, java.util.ArrayList<fire.pb.circletask.anye.SubmitThing> _things_) {
		this.taskpos = _taskpos_;
		this.taskid = _taskid_;
		this.taskrole = _taskrole_;
		this.submittype = _submittype_;
		this.things = _things_;
	}

	public final boolean _validator_() {
		if (taskpos < 0) return false;
		if (taskid < 0) return false;
		if (taskrole < 0) return false;
		if (submittype < 0) return false;
		for (fire.pb.circletask.anye.SubmitThing _v_ : things)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(taskpos);
		_os_.marshal(taskid);
		_os_.marshal(taskrole);
		_os_.marshal(submittype);
		_os_.compact_uint32(things.size());
		for (fire.pb.circletask.anye.SubmitThing _v_ : things) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		taskpos = _os_.unmarshal_int();
		taskid = _os_.unmarshal_int();
		taskrole = _os_.unmarshal_long();
		submittype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.circletask.anye.SubmitThing _v_ = new fire.pb.circletask.anye.SubmitThing();
			_v_.unmarshal(_os_);
			things.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSubmitThings) {
			CSubmitThings _o_ = (CSubmitThings)_o1_;
			if (taskpos != _o_.taskpos) return false;
			if (taskid != _o_.taskid) return false;
			if (taskrole != _o_.taskrole) return false;
			if (submittype != _o_.submittype) return false;
			if (!things.equals(_o_.things)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += taskpos;
		_h_ += taskid;
		_h_ += (int)taskrole;
		_h_ += submittype;
		_h_ += things.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(taskpos).append(",");
		_sb_.append(taskid).append(",");
		_sb_.append(taskrole).append(",");
		_sb_.append(submittype).append(",");
		_sb_.append(things).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

