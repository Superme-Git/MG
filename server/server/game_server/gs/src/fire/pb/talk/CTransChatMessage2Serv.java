
package fire.pb.talk;

import fire.pb.HelpCountManage;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CTransChatMessage2Serv__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CTransChatMessage2Serv extends __CTransChatMessage2Serv__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		if(fire.pb.talk.ChatChannel.isShutUp(roleId))
			return;
		//判断是否是求助类型，如果是1表示求助，需要判断求助的最大次数
		if(funtype==FunModelType.FUN_TASKITEM){
			xbean.helpcount helpcount = xtable.Helpcount.select(roleId);
	    	int max=HelpCountManage.getInstance().getHelpItemNumMax(roleId);
	    	if(helpcount!=null&&helpcount.getHelpitemnum()>=max){
	    		MessageMgr.sendMsgNotify(roleId, 166087, null);
	    		return;
	    	}
		}
		if (funtype == FunModelType.QIU_ZHU) {
			xbean.helpcount helpcount = xtable.Helpcount.select(roleId);
	    	int max=HelpCountManage.getInstance().getHelpItemNumMax(roleId);
	    	if(helpcount!=null&&helpcount.getHelpitemnum()>=max){
	    		MessageMgr.sendMsgNotify(roleId, 166087, null);
	    		return;
	    	}
			//记录玩家在频道中发布的任务求助信息
			new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					xbean.RoleRecourse roleRec = xtable.Rolerecourse.get(roleId);
					if (roleRec == null) {
						roleRec = xbean.Pod.newRoleRecourse();
						xtable.Rolerecourse.insert(roleId, roleRec);
					}
					
					//解析出taskid
					xbean.TaskRecourse taskRec = roleRec.getRecoursetask().get(taskid);
					if (taskRec == null) {
						taskRec = xbean.Pod.newTaskRecourse();
						roleRec.getRecoursetask().put(taskid, taskRec);
					}
					
					//存储信息
					if (!taskRec.getChannellist().contains(Integer.valueOf(messagetype))) {
						taskRec.getChannellist().add(messagetype);
					}
					
					return true;
				}
			}.submit();
		}
		
		boolean flag=ChatChannel.getInstance().process(roleId, messagetype, message, checkshiedmessage, displayinfos, 0);
		if(flag==true&&(funtype==FunModelType.FUN_DIELIVE_CLAN||funtype==FunModelType.FUN_DIELIVE_WORLD)){
			MessageMgr.sendMsgNotify(roleId, 162127, null);
		}
		if(flag == true && funtype == FunModelType.QIU_ZHU) {
			SChatHelpResult snd = new SChatHelpResult();
			snd.result = 0;
			gnet.link.Onlines.getInstance().send(roleId, snd);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 792433;

	public int getType() {
		return 792433;
	}

	public int messagetype; // 消息频道 MsgTye中所定义的部分
	public java.lang.String message; // 内容
	public java.lang.String checkshiedmessage; // 检测屏蔽字的纯文本
	public java.util.ArrayList<fire.pb.talk.DisplayInfo> displayinfos; // 显示道具、宠物、技能
	public int funtype; // 功能类型  类型编号对应FunModelType
	public int taskid; // 任务id

	public CTransChatMessage2Serv() {
		message = "";
		checkshiedmessage = "";
		displayinfos = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
	}

	public CTransChatMessage2Serv(int _messagetype_, java.lang.String _message_, java.lang.String _checkshiedmessage_, java.util.ArrayList<fire.pb.talk.DisplayInfo> _displayinfos_, int _funtype_, int _taskid_) {
		this.messagetype = _messagetype_;
		this.message = _message_;
		this.checkshiedmessage = _checkshiedmessage_;
		this.displayinfos = _displayinfos_;
		this.funtype = _funtype_;
		this.taskid = _taskid_;
	}

	public final boolean _validator_() {
		for (fire.pb.talk.DisplayInfo _v_ : displayinfos)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(messagetype);
		_os_.marshal(message, "UTF-16LE");
		_os_.marshal(checkshiedmessage, "UTF-16LE");
		_os_.compact_uint32(displayinfos.size());
		for (fire.pb.talk.DisplayInfo _v_ : displayinfos) {
			_os_.marshal(_v_);
		}
		_os_.marshal(funtype);
		_os_.marshal(taskid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		messagetype = _os_.unmarshal_int();
		message = _os_.unmarshal_String("UTF-16LE");
		checkshiedmessage = _os_.unmarshal_String("UTF-16LE");
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.talk.DisplayInfo _v_ = new fire.pb.talk.DisplayInfo();
			_v_.unmarshal(_os_);
			displayinfos.add(_v_);
		}
		funtype = _os_.unmarshal_int();
		taskid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CTransChatMessage2Serv) {
			CTransChatMessage2Serv _o_ = (CTransChatMessage2Serv)_o1_;
			if (messagetype != _o_.messagetype) return false;
			if (!message.equals(_o_.message)) return false;
			if (!checkshiedmessage.equals(_o_.checkshiedmessage)) return false;
			if (!displayinfos.equals(_o_.displayinfos)) return false;
			if (funtype != _o_.funtype) return false;
			if (taskid != _o_.taskid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += messagetype;
		_h_ += message.hashCode();
		_h_ += checkshiedmessage.hashCode();
		_h_ += displayinfos.hashCode();
		_h_ += funtype;
		_h_ += taskid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(messagetype).append(",");
		_sb_.append("T").append(message.length()).append(",");
		_sb_.append("T").append(checkshiedmessage.length()).append(",");
		_sb_.append(displayinfos).append(",");
		_sb_.append(funtype).append(",");
		_sb_.append(taskid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

