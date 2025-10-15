
package fire.pb.clan;

import java.util.Arrays;





import fire.pb.PropRole;
import fire.pb.clan.srv.ClanManage;
import fire.pb.talk.MessageMgr;
import fire.pb.util.StringConstant;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestSelectType__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestSelectType extends __CRequestSelectType__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid < 0) {
			return;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
				if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
					return false;
				}
				//判断权限，只有帮助和副帮主可以
				if (clanInfo.getClanmaster() != roleid && clanInfo.getClanvicemaster() != roleid) {
					// 不是会长也不是副会长
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160243, null);//只有会长或副会长拥有选择权限
					return false;
				}
				if(selecttype!=fire.pb.clan.PClanMedicItem.SELECT_BASE
						&&selecttype!=fire.pb.clan.PClanMedicItem.SELECT_DOUBLE&&selecttype!=fire.pb.clan.PClanMedicItem.SELECT_THREE){
					return false;
				}
				
				//公会资金不足，无法使用此功能
				int level = clanInfo.getHouse().get(fire.pb.clan.srv.ClanManage.DrugStone);
				fire.pb.clan.SClanDrugstore drugstone =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanDrugstore.class).get(level);
				if (drugstone == null){
					return false;
				}
				//按规则随机药品
				int cost=0;
				if(selecttype==fire.pb.clan.PClanMedicItem.SELECT_THREE){//三倍产药
					//判断资金帮会资金是否够
					cost=drugstone.getTrimoney();
				}else if(selecttype==fire.pb.clan.PClanMedicItem.SELECT_DOUBLE){//2倍产药
					cost=drugstone.getDoublemoney();
				}
				//判断资金帮会资金是否够
				if(clanInfo.getMoney()<cost){
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160244, null);
					return false;
				}
				if(selecttype==fire.pb.clan.PClanMedicItem.SELECT_THREE&&clanInfo.getMoney()<ClanManage.getThreeMedicBaseMoney()){
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160244, null);
					return false;
				}
				if(selecttype==fire.pb.clan.PClanMedicItem.SELECT_DOUBLE&&clanInfo.getMoney()<ClanManage.getTwoMedicBaseMoney()){
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160244, null);
					return false;
				}
				
				
				
				
				xbean.MedicItemList itemlist=xtable.Medicitemlisttab.get(clanInfo.getKey());
				if(itemlist==null){
					itemlist=xbean.Pod.newMedicItemList();
					xtable.Medicitemlisttab.insert(clanInfo.getKey(), itemlist);
				}
				itemlist.setSelecttype(selecttype);
				SRequestSelectType sRequestSelectType=new SRequestSelectType();
				sRequestSelectType.selecttype=selecttype;
				gnet.link.Onlines.getInstance().send(roleid, sRequestSelectType);
				
				//提示当公会玩家切换时，调用客户端提示表ID：160442，参数1为玩家名称，参数2为当前产药模式。
				// 通知公会中的兄弟
				PropRole memberProp = new PropRole(roleid, true);
				MessageMgr.psendMsgNotifyWhileCommit(clanInfo.getMembers().keySet(), 160442, 0,Arrays.asList(memberProp.getName(),getTypeName(selecttype)));
				
				return true;
			}
		}.submit();
		
		
	}
	
	/**
	 * 获得产药类型  287两倍    288三倍  289正常
	 * @param type
	 * @return
	 */
	public static String getTypeName(int type){
		String name="";
		if(type==fire.pb.clan.PClanMedicItem.SELECT_THREE){
			name=StringConstant.ID2String("三倍",288);
		}else if(type==fire.pb.clan.PClanMedicItem.SELECT_DOUBLE){
			name=StringConstant.ID2String("两倍",287);
		}else{
			name=StringConstant.ID2String("正常",289);
		}
		return name;
	}
	
	

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808505;

	public int getType() {
		return 808505;
	}

	public int selecttype; // 选择几倍产药

	public CRequestSelectType() {
	}

	public CRequestSelectType(int _selecttype_) {
		this.selecttype = _selecttype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(selecttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		selecttype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestSelectType) {
			CRequestSelectType _o_ = (CRequestSelectType)_o1_;
			if (selecttype != _o_.selecttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += selecttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(selecttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestSelectType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = selecttype - _o_.selecttype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

