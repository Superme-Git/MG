
package fire.pb.npc;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.item.ItemShuXing;
import fire.pb.map.Npc;
import fire.pb.map.SceneManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;
import xbean.WheelInfo;
import xbean.WheelInfos;


public class PFinishFortuneWheel extends Procedure {

	private final long roleid;

	private final long npckey;

	private final int serviceid;

	private final int suc;

	//private final Map<Integer, Integer> msgs;

	
	public PFinishFortuneWheel(long roleid, long npckey, int serviceid, int succ) {

		super();
		this.roleid = roleid;
		this.npckey = npckey;
		this.serviceid = serviceid;
		this.suc = succ;
		//this.msgs = msgs;
	}

	@Override
	protected boolean process() throws Exception {

		WheelInfos wts = xtable.Wheelprogress.get(roleid);
		if (null == wts)
			return false;
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		//if (npc == null && (serviceid != NpcServices.PET_BAG && serviceid != PReqFortuneWheel.JIANGQUAN_SERVICEID))//在高级藏宝图的宠物袋中,npc是已经被删除的,此时就算npc为null,也不能return false
			//return false;
		WheelInfo wt = PReqFortuneWheel.findWheelType(wts, npc!=null?npc.getNpcID():0, serviceid);
		if (null == wt)
			return false;
        xbean.WheelItem item = wt.getWheelitems().get(wt.getIndex());
        if (item == null)
        	return false;
		int itemType = item.getItemtype();
	
		boolean succ = false;
		switch (itemType) {
		// if award item
		case 1: {
			int rt = 0;
			// rt= itemmodule.addItemToBag(roleid, wt.getItemid(), (int)
			// wt.getNum(), "award of Fortune Wheel");
			if (item.getBind()==0) 
			rt = BagUtil.addItem(roleid, item.getItemid(), item.getNum(), "award of Fortune Wheel",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_rollschoolwheel, 4
					);
			else
				rt = BagUtil.addBindItem(roleid, item.getItemid(), item.getNum(), "award of Fortune Wheel",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_rollschoolwheel, 4,false);
			if (rt != item.getNum()) {
				return false;
			} else {
				succ = true;
			    sendItemMsg(item.getItemid(),rt);
			    if (item.getMsgid()>0){//如果需要发公告
			    	List<String> paras = new ArrayList<String>();
			    	paras.add(new PropRole(roleid, true).getName());
			    	paras.addAll(MessageUtil.getItemMsgParas(item.getItemid(), item.getNum()));
			    	SceneManager.sendAll(MessageMgr.getMsgNotify(item.getMsgid(), 0, paras));
			    }
//			    if (npc.getNpcID() == PSuperTreasuremapEnd.PET_BAG_ID){//如果是高级藏宝图的宠物袋大转盘,则给宠物袋成功后把箱子删了
//			    	SceneNpcManager.premoveNpcWhileCommit(npckey);
//			    	if (xtable.Petbagownertable.select(npckey)!=null) 
//			    	xtable.Petbagownertable.remove(npckey);
//			    }
			}
		}
			break;
		// if award exp
		case 2: {
			int baseExp = wt.getBaseexp();
			int exp = baseExp;
			if (suc == 1)
				if (item.getTimes() > 0)
					exp = (int) (baseExp * item.getTimes() / 10);// 倍数是按*10来填的,所以除以10
				else if (item.getNum() > 0)
					exp = (int) item.getNum();
			// 应该只给额外的奖励,比如1.5倍经验,多给0.5倍,剩下的1倍在大转盘之前就应该给.钱和经验一样的处理
			if (exp > baseExp)
				succ = new PAddExpProc(roleid, exp - baseExp,false,PAddExpProc.OTHER,"幸运大转盘").call();
			else
				succ = true;
			if (succ) {
				
			sendExpMsg(exp-baseExp);
			}
			break;
		}
		case 3: {
			int baseMoney = wt.getBasemoney();
			long money = baseMoney;
			if (suc == 1)
				if (item.getTimes() > 0)
					money = baseMoney * item.getTimes() / 10;
				else if (item.getNum() > 0)
					money =  item.getNum();
			if (money > baseMoney) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				long before = bag.getMoney();
				bag.addSysMoney(money - baseMoney, "award of Fortune Wheel",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhuanpan, 0 );
				succ = (bag.getMoney() != before);
				// succ = new PAddMoney(roleid, money - baseMoney, BagTypes.BAG,
				// "award of Fortune Wheel").call();
			} else
				succ = true;
			if (succ) {
						sendMoneyMsg( money);
			}
			break;
		}
		case 4: {
			int baseSMoney = wt.getBasesmoney();
			long money = baseSMoney;
			if (suc == 1)
				if (item.getTimes() > 0)
					money = baseSMoney * item.getTimes() / 10;
				else if (item.getNum() > 0)
					money =  item.getNum();
			
			succ = true;
			break;
		}
		default:
			break;
		}
		// 如果发放奖励成功,则从数据库中删除玩家这次的大转盘数据,流程正常结束
		if (succ) {
			if (wts.getWheellist().contains(wt)){
				wts.getWheellist().remove(wt);
				if (wts.getWheellist().isEmpty())
					xtable.Wheelprogress.remove(roleid);
			}
			else {
				return false; //不包含肯定是有问题的,回滚
			}
		} else {
			return false;
		}
		return true;
	}

	private void sendMoneyMsg( long money) {
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		int npcid = 0;
	    if (npc != null)
	       npcid = npc.getNpcID();
		List<String> paras = new ArrayList<String>();
		paras.add(Long.toString(money));
		psendWhileCommit(roleid, MessageMgr.getMsgNotify(MessageMgr.ADD_MONEY, npcid, paras));
	}
//	private void sendSMoneyMsg( long money) {
//		
//		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
//		List<String> paras = new ArrayList<String>();
//		paras.add(Long.toString(money));
//		psendWhileCommit(roleid, Message.getMsgNotify(Message.ADD_SMONEY, npcid, paras));
//	}

	private void sendExpMsg( int exp) {
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		int npcid = 0;
	    if (npc != null)
	       npcid = npc.getNpcID();
		List<String> paras = new ArrayList<String>();
		paras.add(Integer.toString(exp));
		psendWhileCommit(roleid, MessageMgr.getMsgNotify(MessageMgr.ADD_EXP, npcid, paras));
	}

	private void sendItemMsg( int itemid,int num) {
        Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		int npcid = 0;
		if (npc!=null) 
		   npcid = npc.getNpcID();
		ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemid);
		if (attr==null) {
			return;
		}
		String name=attr.getName();
		String unit=attr.getUnit();
		List<String> paras = MessageUtil.getMsgParaList(Integer.toString(num), unit, name);
		psendWhileCommit(roleid, MessageMgr.getMsgNotify(MessageMgr.ADD_ITEM, npcid, paras));
	}

}
