
package fire.pb.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fire.log.Logger;
import fire.pb.activity.award.RewardMgr;
import fire.pb.game.SFortuneWheel;
import fire.pb.main.ConfigManager;
import fire.pb.map.SActivityAwardItems;
import fire.pb.util.Misc;
import mkdb.Procedure;
import xbean.WheelInfo;
import xbean.WheelInfos;
import xbean.WheelItem;

public class PReqFortuneWheel extends Procedure {

	public static final int JIANGQUAN_SERVICEID = 1057;
	
	private final long roleid;

	private final long npckey;

	private final int npcid;

	private int baseMoney;

	private int baseSMoney;

	private int baseExp;

	private boolean sendP = true;// 是否发送大转盘协议给客户端开始大转盘.有的时候只需要生成大转盘数据,而不用显示界面

	private final int serviceid;// serviceid

	private Integer awardIndex ;

	private int msgid;

	private List<String> paras;
	
	private static Logger logger = Logger.getLogger("MAPMAIN");

	private static java.util.NavigableMap<Integer, SFortuneWheel> FortuneMap = fire.pb.main.ConfigManager.getInstance().getConf(
			fire.pb.game.SFortuneWheel.class);

	static Properties prop = ConfigManager.getInstance().getPropConf("game");
	
	private int flag = 0;

	// 这个构造方法用来再次打开大转盘,此时奖励应该是已经算好的,放在数据库了,直接拿出来
	public PReqFortuneWheel(long roleid, long npckey, int npcid, boolean sendP, int serviceid) {

		super();
		this.roleid = roleid;
		this.npcid = npcid;
		this.serviceid = serviceid;
		this.sendP = sendP;
		this.npckey = npckey;
	}

	
	public PReqFortuneWheel(long roleid, long npckey, int npcid, int baseMoney, int baseExp, int baseSMoney, int awardIndex, int msgid, List<String> paras,
			boolean sendProtocol, int serviceid, int flag) {

		this(roleid, npckey, npcid, sendProtocol, serviceid);
		this.baseMoney = baseMoney;
		this.baseSMoney = baseSMoney;
		this.baseExp = baseExp;
		this.awardIndex = awardIndex;
		this.msgid = msgid;
		this.paras = paras;
		this.flag = flag;
	}
	
	
	public PReqFortuneWheel(long roleid, long npckey, int npcid, int baseMoney, int baseExp, int baseSMoney, int awardIndex, int msgid, List<String> paras,
			boolean sendProtocol, int serviceid) {

		this(roleid, npckey, npcid, sendProtocol, serviceid);
		this.baseMoney = baseMoney;
		this.baseSMoney = baseSMoney;
		this.baseExp = baseExp;
		this.awardIndex = awardIndex;
		this.msgid = msgid;
		this.paras = paras;
	}

	private int getItemId(final int nIndex) {
		SActivityAwardItems items = RewardMgr.getInstance().getAward2Map().get(nIndex);
		if (items == null) {
			return -1;
		}
		
		List<Integer> itemList = items.getItems();
		int index = Misc.getRandomBetween(0, itemList.size() - 1);
		
		return itemList.get(index);
	}

	protected boolean process() throws Exception {

		xbean.WheelItemLimit itemLimit = null;
		if (awardIndex!=null){
			itemLimit =xtable.Wheelitemlimit.get(awardIndex);//先获得锁,以免有上限的奖励给了多份
			if (itemLimit == null) {
				itemLimit = xbean.Pod.newWheelItemLimit();
				xtable.Wheelitemlimit.insert(awardIndex, itemLimit);
			}
		}
		// 先找数据库中有没有现成的大转盘,如果有的话直接拿出来,防止玩家不停的开关大转盘界面来刷最高奖励
		WheelInfos wts = xtable.Wheelprogress.get(roleid);
		if (wts == null) {
			wts = xbean.Pod.newWheelInfos();
			xtable.Wheelprogress.insert(roleid, wts);
		}
		WheelInfo wt = findWheelType(wts, npcid, serviceid);
        if (serviceid == JIANGQUAN_SERVICEID && wt != null){//奖券的抽奖不保留
        	
        	wts.getWheellist().remove(wt);
        	wt = null;
        }
		if (wt == null&&awardIndex!=null) {// 如果没找到,则新建一个
			wt = xbean.Pod.newWheelInfo();
			wts.getWheellist().add(wt);
			wt.setNpcbaseid(npcid);
			wt.setServiceid(serviceid);
			wt.setTime(System.currentTimeMillis());
			wt.setBaseexp(baseExp);
			wt.setBasemoney(baseMoney);
			wt.setBasesmoney(baseSMoney);
			wt.setMsgid(msgid);
			if (paras != null)
				wt.getMsgparas().addAll(paras);
			wt.setFinish(false);
			SFortuneWheel sfw = FortuneMap.get(awardIndex);
			Integer[] objArr = new Integer[sfw.cards.size()];
			List<Integer> fixeditemIndexs = new ArrayList<Integer>();
			List<Integer> allIndexs = new ArrayList<Integer>();
			for (int i = 0; i < objArr.length; i++) {
				objArr[i] = i;
				String[] strs = sfw.cards.get(i).split(";");
				int type = Integer.parseInt(strs[0]);
				if ((type==1 || type == 5)&&strs.length==9){
					if (Integer.parseInt(strs[8])==1)
						fixeditemIndexs.add(i);
				}else {
					if (Integer.parseInt(strs[5])==1) 
						fixeditemIndexs.add(i);
				}
				allIndexs.add(i);
			}
			if (!fixeditemIndexs.isEmpty()) 
				allIndexs.removeAll(fixeditemIndexs);
			
			int count = 0;
			Integer[] objs = null;
			List<Integer> probs = new ArrayList<Integer>();
			if (serviceid == JIANGQUAN_SERVICEID) {
				count = npcid;
				objs = objArr;
			}else {
				count = 8;
				//count = FireProp.getIntValue(prop, "mtgserv.fortuneWheel.itemsNum");
				count = count - fixeditemIndexs.size();
				int[] allIndexArray = new int[allIndexs.size()];
				for (int i=0;i<allIndexs.size();i++) {
					allIndexArray[i] = allIndexs.get(i);
				}
				
				int[] tmparray = Misc.getRandomArray(allIndexArray, count);;
				List<Integer> randomIndexes = new ArrayList<Integer>();
				for (int i = 0; i < tmparray.length; i++) {
					randomIndexes.add(tmparray[i]);
				}
				randomIndexes.addAll(fixeditemIndexs);
				objs = new Integer[randomIndexes.size()];
				objs = randomIndexes.toArray(objs);
			}
			int sum = 0;
			for (int i = 0; i < objs.length; i++) {
				String[] strs = sfw.cards.get(objs[i]).split(";");
				int type = Integer.parseInt(strs[0]);
				int num = Integer.parseInt(strs[1]);
				int itemid = Integer.parseInt(strs[2]);
				int times = Integer.parseInt(strs[3]);
				xbean.WheelItem item = xbean.Pod.newWheelItem();
				item.setItemtype(type);
				item.setItemid(itemid);
				item.setNum(num);
				item.setTimes(times);
				wt.getWheelitems().add(item);
				int prob = Integer.parseInt(strs[4]);
				if (type==1&&strs.length==9){
					item.setBind(Integer.parseInt(strs[5]));
					item.setMsgid(Integer.parseInt(strs[7]));
					int maxNum = Integer.parseInt(strs[6]);
					if (maxNum>0){
						item.setLimit(maxNum);
						@SuppressWarnings("null")
						Integer curNum = itemLimit.getLimitmap().get(itemid);
						if (curNum!=null&&curNum+num > maxNum)//达到上限概率变0
							probs.add(0);
						else
							probs.add(prob);
					}else
						probs.add(prob);
				} else {
					if (type == 5){
						//道具类型库
						int true_itemid = this.getItemId(itemid); //itemid是物品类型库里的id
						if (true_itemid > 0) {
							item.setItemtype(1);//虽然服务器走的物品类型库逻辑，但给客户端显示的数据应该是普通物品类型！故强制改为类型1。
							item.setItemid(true_itemid);
							probs.add(prob);
						} else {
							item.setItemtype(1);
							probs.add(0);
						}
					} else {
						probs.add(prob);
					}
				}
				sum += prob;
			}
			int index = Misc.getProbability(probs, sum); 
			wt.setIndex(index);
			//如果给的是有上限的物品,增加数量
			xbean.WheelItem item = wt.getWheelitems().get(index);
			if (item.getLimit()>0){
				@SuppressWarnings("null")
				Integer curNum = itemLimit.getLimitmap().get(item.getItemid());
				if (curNum == null)
					itemLimit.getLimitmap().put(item.getItemid(), item.getNum());
				else
					itemLimit.getLimitmap().put(item.getItemid(), curNum+item.getNum());
			}
		}
		if (wt == null)
			logger.error("no fortune wheel data:npcid:"+npcid+"taskid:"+serviceid+"roleid:"+roleid);
		if (sendP&&wt!=null) {
			SReqFortuneWheel srfw = genFortuneWheelProtocal(wt);
			srfw.flag = (byte)flag;
			psendWhileCommit(roleid, srfw);
		}
		return true;
	}

	

	private SReqFortuneWheel genFortuneWheelProtocal(WheelInfo wt) {

		SReqFortuneWheel srfw = new SReqFortuneWheel();
		srfw.npckey = npckey;
		srfw.serviceid = wt.getServiceid();
		srfw.index = wt.getIndex();
		for (WheelItem item : wt.getWheelitems()) {
			int type = item.getItemtype();
			int itemid = item.getItemid();
			int num = item.getNum();
			int times = item.getTimes();
			srfw.itemids.add(new ForturneWheelType(type, itemid, num, times));
		}

		return srfw;
	}

	public static WheelInfo findWheelType(WheelInfos wts, int npcbaseid, int serviceid) {

		if (wts == null || wts.getWheellist().size() == 0)
			return null;
		for (WheelInfo wtype : wts.getWheellist()) {
			if (wtype.getServiceid() == serviceid)
				if (wtype.getNpcbaseid() == npcbaseid)
					return wtype;
				else if (serviceid == PReqFortuneWheel.JIANGQUAN_SERVICEID) {
					return wtype;
				}
		}
		return null;
	}

    public static boolean reload(){
    	FortuneMap = fire.pb.main.ConfigManager.getInstance().getConf(
    			fire.pb.game.SFortuneWheel.class);
    	return true;
    }
}
