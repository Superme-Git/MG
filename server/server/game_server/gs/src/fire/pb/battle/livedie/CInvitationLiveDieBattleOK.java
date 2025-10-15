
package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import fire.pb.PropRole;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.talk.MessageMgr;
import fire.pb.title.COffTitle;
import gnet.link.Onlines;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CInvitationLiveDieBattleOK__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CInvitationLiveDieBattleOK extends __CInvitationLiveDieBattleOK__ {
	public static final Logger logger = Logger.getLogger("BATTLE");
	@Override
	protected void process() {
		//确定是否下战书
		final long hostid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (hostid <= 0)
			return;
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				//判断目标玩家是否在线
				Role gRole = RoleManager.getInstance().getRoleByID(objectid);
				if (gRole == null){
					fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 145001, null);
					return false;
				}
				xbean.Properties guestprop=xtable.Properties.select(objectid);
				if(guestprop==null){
					fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 145001, null);
					return false;
				}
				//如果自己身上有生死战称号，不能下战书
				fire.pb.PropRole hostprole = new fire.pb.PropRole(hostid, true);
				Role hostrole = RoleManager.getInstance().getRoleByID(hostid);
				if(hostrole != null){
					if(COffTitle.isLiveDieTitle(hostprole)){
						fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162088, null);
						return false;
					}
				}
				//这里需要判断我是否已经下过战书
				xbean.LiveDieRoleInfo hostliveDieRoleInfo=xtable.Livedieroleinfotab.select(hostid);
				if(hostliveDieRoleInfo!=null){
					if(System.currentTimeMillis()-hostliveDieRoleInfo.getInvitationtime()<LiveDieMange.getLiveDieTime()){
						String name=new PropRole(hostliveDieRoleInfo.getObjectid(),true).getName();
						if(objectid==hostliveDieRoleInfo.getObjectid()){
							//提示已经发送过162073
							fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162073, Arrays.asList(name));
						}else{
							//提示已经发送过162074
							fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162074, Arrays.asList(name));
						}
						return false;
					}
				}
				//判断我是否已经接受过别人的战书
				Long selfid=xtable.Livedie2key.select(hostid);
				if(selfid!=null){
					xbean.LiveDieRoleInfo otherLiveDieRoleInfo=xtable.Livedieroleinfotab.select(selfid);
					if(otherLiveDieRoleInfo!=null&&System.currentTimeMillis()-otherLiveDieRoleInfo.getInvitationtime()<LiveDieMange.getLiveDieTime()){
						String name=new PropRole(selfid,true).getName();
						fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162076, Arrays.asList(name));
						return false;
					}
				}
				//目标身上有生死战称号，不能下战书
				fire.pb.PropRole guestprole = new fire.pb.PropRole(objectid, true);
				if(COffTitle.isLiveDieTitle(guestprole)){
					fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162089, null);
					return false;
				}
				//判断目标是否下过战书
				xbean.LiveDieRoleInfo geustliveDieRoleInfo=xtable.Livedieroleinfotab.select(objectid);
				if(geustliveDieRoleInfo!=null){
					if(System.currentTimeMillis()-geustliveDieRoleInfo.getInvitationtime()<LiveDieMange.getLiveDieTime()){
						String name=new PropRole(objectid,true).getName();
						fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162077, Arrays.asList(name));
						return false;
					}
				}
				//目标是否接受了别人的战书
				Long otherid=xtable.Livedie2key.select(objectid);
				if(otherid!=null){
					xbean.LiveDieRoleInfo otherLiveDieRoleInfo=xtable.Livedieroleinfotab.select(otherid);
					if(otherLiveDieRoleInfo!=null&&System.currentTimeMillis()-otherLiveDieRoleInfo.getInvitationtime()<LiveDieMange.getLiveDieTime()){
						if(otherLiveDieRoleInfo.getObjectid()==hostid){
							String name=new PropRole(objectid,true).getName();
							fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162075, Arrays.asList(name));
							return false;
						}else{
							String name=new PropRole(objectid,true).getName();
							fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162077, Arrays.asList(name));
							return false;
						}
					}
				}
				//需要锁
				List<Long> list = new ArrayList<Long>();
				list.add(hostid);
				list.add(objectid);
				mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));
				//扣除银币
				fire.pb.item.Pack bag = new fire.pb.item.Pack(hostid, false);
				int cost=LiveDieMange.getLiveDieCostMoney();
				if(bag.getMoney()<cost){
//					MessageMgr.sendMsgNotify(hostid, 145005, null);
					return false;
				}
				if (bag.subMoney(-cost, "下战书扣除银币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiazhanshu, 0) != -cost) {
//					MessageMgr.sendMsgNotify(hostid, 145005, null);
					return false;
				}
				hostliveDieRoleInfo=xtable.Livedieroleinfotab.get(hostid);
				//保存数据库
				if(hostliveDieRoleInfo==null){
					hostliveDieRoleInfo=xbean.Pod.newLiveDieRoleInfo();
					xtable.Livedieroleinfotab.insert(hostid, hostliveDieRoleInfo);
				}
				hostliveDieRoleInfo.setObjectid(objectid);
				hostliveDieRoleInfo.setSelecttype(selecttype);
				hostliveDieRoleInfo.setInvitationtime(System.currentTimeMillis());
				hostliveDieRoleInfo.setAcceptflag(0);
				
				//插入关联表
				Long object=xtable.Livedie2key.get(objectid);
				if(object==null){
					xtable.Livedie2key.insert(objectid, hostid);
				}else{
					//时间过期了，重新设置值
					xtable.Livedie2key.remove(objectid);
					xtable.Livedie2key.insert(objectid, hostid);
				}
				SInvitationLiveDieBattleOK sInvitationLiveDieBattleOK =new SInvitationLiveDieBattleOK();
				sInvitationLiveDieBattleOK.sourceid=hostid;
				sInvitationLiveDieBattleOK.sourcename=hostprole.getName();
				sInvitationLiveDieBattleOK.selecttype=selecttype;
				Onlines.getInstance().send(objectid, sInvitationLiveDieBattleOK);
				
				//发送系统公告
				String hostname=new PropRole(hostid,true).getName();
				String guestname=new PropRole(objectid,true).getName();
				MessageMgr.sendSystemMsg(162066,Arrays.asList(hostname,guestname));
				
				logger.info("生死战，下战书,角色id"+hostid+"\t对手id"+objectid+"\t金钱数量"+cost);
				return true;
			};
		}.submit();
		
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793835;

	public int getType() {
		return 793835;
	}

	public long objectid; // 目标玩家id
	public int selecttype; // 0单人   1组队

	public CInvitationLiveDieBattleOK() {
	}

	public CInvitationLiveDieBattleOK(long _objectid_, int _selecttype_) {
		this.objectid = _objectid_;
		this.selecttype = _selecttype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(objectid);
		_os_.marshal(selecttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		objectid = _os_.unmarshal_long();
		selecttype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CInvitationLiveDieBattleOK) {
			CInvitationLiveDieBattleOK _o_ = (CInvitationLiveDieBattleOK)_o1_;
			if (objectid != _o_.objectid) return false;
			if (selecttype != _o_.selecttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)objectid;
		_h_ += selecttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(objectid).append(",");
		_sb_.append(selecttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CInvitationLiveDieBattleOK _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(objectid - _o_.objectid);
		if (0 != _c_) return _c_;
		_c_ = selecttype - _o_.selecttype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

