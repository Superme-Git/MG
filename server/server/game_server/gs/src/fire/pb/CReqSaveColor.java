
package fire.pb;


import org.apache.log4j.Logger;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqSaveColor__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqSaveColor extends __CReqSaveColor__ {
	public static final Logger logger = Logger.getLogger("SYSTEM");
	@Override
	protected void process() {
		// protocol handle
//		//保存人物染色方案
//		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
//		if(roleid < 0) {
//			return;
//		}
//		
//		new mkdb.Procedure() {
//			@Override
//			protected boolean process() throws Exception {
//				xbean.Properties prop=xtable.Properties.get(roleid);
//				if(prop==null){
//					return false;
//				}
//				//判断这个方案是否需要保存
//				boolean havetype=false;
//				java.util.List<xbean.RoleColorType> list=prop.getColorroomlist();
//				for(xbean.RoleColorType type:list){
//					if(type!=null&&type.getColorpos1()==prop.getRolecolor1()&&type.getColorpos2()==prop.getRolecolor2()){
//						havetype=true;
//						break;
//					}
//				}
//				if(havetype==true){
//					logger.info("角色id "+roleid+"染色"+"\t衣橱中已经存在，不需要保存");
//					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160378, null);
//					return false;
//				}
//				int costmoney=0;
//				int max=0;
//				Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
//				if(commonMap!=null){
//					SCommon sCommon=commonMap.get(227);
//					if(sCommon!=null){
//						costmoney=Integer.parseInt(sCommon.getValue());
//					}
//					sCommon=commonMap.get(228);
//					if(sCommon!=null){
//						max=Integer.parseInt(sCommon.getValue());
//					}
//				}
//				//判断衣橱是否满
//				if(prop.getColorroomlist().size()>=max){
//					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160379, null);
//					return false;
//				}
//				//判断是否有足够的钱
//				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
//				if (bag.getMoney() < costmoney) {
//					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160246, 0, null);
//					return false;
//				}
//				if (costmoney <=0){
//					return false;
//				}
//				//扣除钱
//				long rmv = bag.subMoney(-costmoney, "保存染色方案", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_ranse, -costmoney);
//				if (rmv != -costmoney){
//					return false;
//				}
//				//提示消耗钱
//				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160375,Arrays.asList(costmoney+""));
//				//保存
//				xbean.RoleColorType roleColorType=xbean.Pod.newRoleColorType();
//				roleColorType.setColorpos1(prop.getRolecolor1());
//				roleColorType.setColorpos2(prop.getRolecolor2());
//				prop.getColorroomlist().add(roleColorType);
//				
//				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160429, 0, null);
//				
////				SReqSaveColor sReqSaveColor=new SReqSaveColor();
////				gnet.link.Onlines.getInstance().send(roleid, sReqSaveColor);
//				logger.info("角色id "+roleid+"染色"+"\t保存染色方案到衣橱\t"+"方案1="+prop.getRolecolor1()+"\t方案2="+prop.getRolecolor2()+"消耗金币"+costmoney);
//				return true;
//			}
//		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786540;

	public int getType() {
		return 786540;
	}


	public CReqSaveColor() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqSaveColor) {
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

	public int compareTo(CReqSaveColor _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

