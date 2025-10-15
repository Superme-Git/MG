package fire.pb;

import fire.pb.title.Title;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

public class AU2GameFunDispatch {
	
	public int userid;
	public int qtype;
	public com.locojoy.base.Octets info;
	public int retcode;
	public int reserved;
	
	public AU2GameFunDispatch(int userid, int qtype, Octets info, int retcode,
			int reserved) {
		this.userid = userid;
		this.qtype = qtype;
		this.info = info;
		this.retcode = retcode;
		this.reserved = reserved;
	}
	
	public void doDispatch(){
		
		if (retcode == 8){//等于8的情况就不要继续做处理了,因为发过来的info是错的
			fire.log.Module.logger.error("AU2Game retcode 8 : " + userid);
			return;
		}
		switch (qtype) {
		case AU2GameType.PRESEND_CARD:
			doFresendCard();
			break;
			
		default:
			break;
		}
		
	}

	protected void removeAllRoleTitle() {
		xbean.User user = xtable.User.select(userid);
		if(null == user)
			return;
		
		for(Long roleid : user.getIdlist()){
			if(Title.hasTitle(roleid, 36468)){
				Title.removeTitle(roleid, 36468);
			}
		}
	}

	@SuppressWarnings("unused")
	private void doFresendCard() {
		long roleid = 0;
		String cardNumber = "";
		int type = 0;
		int parentType = 0;
		try {
			OctetsStream os = new OctetsStream(info);
			roleid = os.unmarshal_long();
			cardNumber = os.unmarshal_String();
			type =	os.unmarshal_int();
			parentType = os.unmarshal_int();
			fire.log.Module.logger.info("freshcard:"+cardNumber+"roleid:"+roleid+"retcode:"+retcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (retcode==0 || retcode == 3){//给予奖励
//			new fire.pb.PFreshCardAward(userid,roleid,cardNumber,type,parentType,retcode).submit();
		}else if (retcode == 2){
			if (cardNumber.length()<5)
				return;
//			SFreshCardAward sFreshCardAward = PFreshCardAward.freshAwards.get(Integer.parseInt(cardNumber.substring(0,5)));
//			List<String> paras = MessageUtil.getMsgParaList(sFreshCardAward.getCardName());
//			fire.pb.talk.Message.sendMsgNotify(roleid, 142467, 10963, paras);
		}else {
			if (cardNumber.length()<5)
				return;
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142465, 10963, null);
		}
		
	}
	

}
