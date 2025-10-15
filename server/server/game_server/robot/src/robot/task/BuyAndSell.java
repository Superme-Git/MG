package robot.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import mkio.Protocol;
import robot.LoginRole;
import robot.RoleBag;
import robot.chat.ChatIteamMgr;
import robot.manager.MissionMgr;
import robot.manager.MissionMgr.RobotMarketBuyInfo;
import fire.pb.shop.CGetMarketUpPrice;
import fire.pb.shop.CMarketBrowse;
import fire.pb.shop.CMarketContainerBrowse;
import fire.pb.shop.CMarketDown;
import fire.pb.shop.CMarketItemChatShow;
import fire.pb.shop.CMarketUp;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.MarketThreeTable;
import fire.pb.shop.SGetMarketUpPrice;
import fire.pb.shop.SMarketBrowse;
import fire.pb.shop.SMarketBuy;
import fire.pb.shop.SMarketContainerBrowse;
import fire.pb.shop.SMarketUpSucc;
import fire.pb.talk.CChatItemTips;
import fire.pb.talk.CTransChatMessage2Serv;
import fire.pb.talk.STransChatMessage2Client;

/**
 * author yebin @ 2015年12月25日 摆摊机器人
 */
public class BuyAndSell extends MoveBattle {

	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();

	private LinkedList<CMarketUp> marketUps = new LinkedList<CMarketUp>();

	public static final int BROWSE_MY_BUY = 1; // 我要购买

	public static final int BROWSE_SHOW = 2; // 公示物品

	public static final int[] ITEM_LEVEL = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };

	int sellItemId = 0;

	long sellId = 0;

	int type = 0;

	private long lastTime = 0l;

	private int delay = 2000;

	private long sendTime = 0l;

	List<String> sendGoodsList = new ArrayList<String>();

	public BuyAndSell(LoginRole role) {
		super(role, true, false);
		init();
	}

	@Override
	public void start() {
	}

	private void checkOwnSellItems() {
		CMarketContainerBrowse ccb = new CMarketContainerBrowse();
		role.sendProtocol(ccb);
	}

	private void checkBuyItems() {
		RobotMarketBuyInfo jcmbi = MissionMgr.getInstance().getRandRobotMarketBuyInfo();
		if (jcmbi == null) {
			mkdb.Trace.error("checkBuyItems()摆摊购买配置文件读取错误");
			return;
		}
		CMarketBrowse cmb = new CMarketBrowse();
		cmb.browsetype = BROWSE_MY_BUY; // 浏览类型 1 我要购买 2公示物品
		cmb.firstno = jcmbi.firstno; // 1级页签类型
		cmb.twono = jcmbi.twono; // 2级页签类型
		int randLimitIndex = (int) (Math.random() * ITEM_LEVEL.length);
		cmb.limitmin = ITEM_LEVEL[randLimitIndex] - 9; // 条件下限 只有itemtype为4时候才有用
		cmb.limitmax = ITEM_LEVEL[randLimitIndex]; // 条件上限
		cmb.currpage = 1; // 当前页
		cmb.threeno = new ArrayList<Integer>();
		for (MarketThreeTable threenos : jcmbi.threenos) {
			cmb.threeno.add(threenos.threeno); // 3级页签类型
			cmb.itemtype = threenos.itemtype; // 物品类型
		}
		cmb.issearch = 0;
		cmb.pricesort = 1;
		role.sendProtocol(cmb);
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		dealProtocols();
		super.run();
		final Long now = System.currentTimeMillis();
		// 间隔一定的时间处理
		if ((now - this.lastTime) < delay) {
			return;
		}
		this.lastTime = System.currentTimeMillis();
		double rand = Math.random();
		if (rand < 0.5) {
			checkOwnSellItems();
		}
		if (rand < 0.25) {
			checkBuyItems();
		}
		if (rand < 0.1) {
			role.getRoleBag().checkMarketUpGoodsVaildTime();
		}
	}

	@Override
	public void processProtocol(Protocol p) {
		super.processProtocol(p);
		synchronized (protocols) {
			protocols.offer(p);
		}
	}

	private void init() {
		role.getRoleBag().checkMarketUpGoodsVaildTime();
		initRoleState(true);
	}

	/**
	 * 初始化角色等级,物品信息
	 */
	private void initRoleState(boolean bInit) {
		// 调整等级
		StringBuilder sb = new StringBuilder();
		if (role.getLevel() < 80 && bInit) {
			role.sendCommand("//addlevel " + (80 - role.getLevel()) + " equip");
			// sb.append("ROLEID=").append(role.roleId).append("\tUSER=").append(role.loginui.getUserName()).append("\t通过GM命令升级至:")
			// .append(80);
			// mkdb.Trace.info(sb);
		}
		// 增加活跃度
		role.sendCommand("//addhyd 1000");
		RoleBag bag = role.getRoleBag();
		if (bag == null)
			return;
		// 添加金币
		if (bag.getMoney() < 10000000) {
			sb.setLength(0);
			role.sendCommand("//addgold 500000000");
			// sb.append("ROLEID=").append(role.roleId).append("\tUSER=").append(role.loginui.getUserName()).append("\t通过GM命令获得金币:")
			// .append(500000000);
			// mkdb.Trace.info(sb);
		}
		// 添加银币
		if (bag.getSliver() < 10000000) {
			sb.setLength(0);
			role.sendCommand("//addmoney 500000000");
			// sb.append("ROLEID=").append(role.roleId).append("\tUSER=").append(role.loginui.getUserName()).append("\t通过GM命令获得银币:")
			// .append(500000000);
			// mkdb.Trace.info(sb);
		}
		int size = role.getRoleBag().getCanMarketUpItemsNum();
		// 添加一些可以贩卖的物品
		if (size < 20) {
			List<MarketThreeTable> items = MissionMgr.getInstance().getSomeMarketItemIds(20 - size);
			if (items == null) {
				mkdb.Trace.error("buy and sell items map is null");
				return;
			}
			int addPets = 0;
			int addItems = 0;
			for (MarketThreeTable item : items) {
				if (item.itemtype == 2) {
					if (addPets > 3 || role.getRoleBag().emptyNumbers() < 5) {
						continue;
					}
				} else {
					if (addItems > 3) {
						continue;
					}
				}
				if (addItems > 3 && addPets > 17) {
					break;
				}
				sb.setLength(0);
				// String itemName = MissionMgr.getInstance().getMarketItemName(item.id);
				// sb.append("ROLEID=").append(role.roleId).append("\tUSER=").append(role.loginui.getUserName()).append("\t通过GM命令获得")
				// .append(itemName);
				switch (item.itemtype) {
				case 1: // 道具
					addItems++;
					role.sendCommand("//additem " + item.id + " " + ((int) (Math.random() * 10 + 1)));
					// sb.append("道具:").append(itemName).append("\tITEM_ID=").append(item.id);
					break;
				case 2: // 宠物
					addPets++;
					if (role.getRoleBag().getCanMarketUpPetsNum() > 3) {
						break;
					}
					role.sendCommand("//addpet " + item.id + " " + role.getLevel());
					// sb.append("宠物:").append(itemName).append("\tPET_ID=").append(item.id);
					break;
				case 3: // 装备
					addItems++;
					role.sendCommand("//additem " + item.id);
					// sb.append("装备:").append(itemName).append("\tEQUIP_ITEM_ID=").append(item.id);
					break;
				default: // 特殊道具
					addItems++;
					role.sendCommand("//additem " + item.id + " " + ((int) (Math.random() * 10 + 1)));
					// sb.append("特殊道具:").append(itemName).append("\tITEM_ID=").append(item.id);
					break;
				}
				mkdb.Trace.info(sb);
			}
		}
	}

	/**
	 * 准备出售物品
	 */
	void prepareToSellSomeItems(int canMarketUpItemNum) {
		if (canMarketUpItemNum <= 0) {
			return;
		}
		// 检查下背包里是否有足够的物品可以卖
		initRoleState(false);
		for (int i = 0; i < canMarketUpItemNum; i++) {
			int[] arg = role.getRoleBag().randMarketUpOneItemOrPet();
			if (arg == null || Arrays.equals(arg, new int[] { 0, 0, 0, 0 })) {
				continue;
			}
			CMarketUp cu = new CMarketUp();
			cu.containertype = arg[0];
			cu.key = arg[1];
			cu.num = arg[2];
			int itemId = arg[3];
			sellItemId = itemId;
			MarketThreeTable mtt = MissionMgr.marketThreeConfMap.get(itemId);
			if (mtt == null) {
				continue;
			}
			type = mtt.itemtype;
			if (mtt.israrity != 0) {// 珍品 直接发送
				// mkdb.Trace.error("**********随机上架物品" + role.roleId + "---arg:" + Arrays.toString(arg));
				cu.price = (int) (Math.random() * 3000 + 1000);
				role.sendProtocol(cu);
			} else {
				// mkdb.Trace.error("**********非珍品 缓存 等价格获取到了再发" + role.roleId + "---arg:" + Arrays.toString(arg));
				// 非珍品 缓存 等价格获取到了再发
				cu.price = itemId; // 借用price把itemId先传过去
				marketUps.add(cu);
				// 获取价格
				CGetMarketUpPrice cgup = new CGetMarketUpPrice(arg[0], arg[1]);
				role.sendProtocol(cgup);
			}
		}
	}

	/**
	 * 消息处理
	 */
	private void dealProtocols() {
		if (System.currentTimeMillis() - sendTime > 60000) {
			if (sendGoodsList.size() > 0) {
				String goodsAndType = sendGoodsList.remove(0);
				String[] goodArr = goodsAndType.split("_");
				String message = new StringBuffer("<T t=" + '\"' + '\"' + " c=" + '\"' + "FF50321A" + '\"' + " ></T><C t=" + '\"'
						+ goodArr[2] + '\"')
						.append(
								" arg=" + '\"' + "t=1,id=" + goodArr[0] + ",type=" + goodArr[1] + '\"' + " c=" + '\"' + "FF00b1ff"
										+ '\"' + "/>").append("<T t=" + '\"' + '\"' + " c=" + '\"' + "FF50321A" + '\"' + " ></T>")
						.toString();
				// mkdb.Trace.info("发送链接ROLEID=" + role.roleId + "message:" + message);
				java.util.ArrayList<fire.pb.talk.DisplayInfo> info = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
				// 发送道具到世界频道
				CTransChatMessage2Serv cTransChatMessage2Serv = new CTransChatMessage2Serv(5, message, "", info, 0, 0);
				role.sendProtocol(cTransChatMessage2Serv);
				sendTime = System.currentTimeMillis();
				// 增加活跃度
				role.sendCommand("//addhyd 20");
			}
		}
		synchronized (protocols) {
			Protocol p = protocols.poll();
			if (p == null) {
				return;
			}
			// boolean isPrint = true;
			if (p instanceof SMarketBrowse) {
				// SMarketBrowse marketBrowseMsg = ((SMarketBrowse) p);
				// if (marketBrowseMsg.goodslist.size() != 0) {
				// int buyItemIndex = (int) (Math.random() * marketBrowseMsg.goodslist
				// .size());
				// MarketGoods good = marketBrowseMsg.goodslist
				// .get(buyItemIndex);
				// if (good != null) {
				// CMarketBuy cmb = new CMarketBuy();
				// cmb.id = good.id;
				// cmb.saleroleid = good.saleroleid;
				// cmb.itemid = good.itemid;
				// cmb.num = (int) (1 + Math.random() * good.num);
				// role.sendProtocol(cmb);
				//
				// MarketThreeTable mtt = MissionMgr.marketThreeConfMap
				// .get(good.itemid);
				// if (mtt == null) {
				// System.out.println("搞笑了!!配置表里居然没有!");
				// return;
				// }
				//
				// mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
				// + role.loginui.getUserName() + "\tINFO=购买"
				// + (mtt.itemtype == 2 ? "宠物" : "物品")
				// + "\tITEMID=" + mtt.id + "\t物品名称="
				// + mtt.itemname);
				// }
				// }
			} else if (p instanceof SGetMarketUpPrice) {
				if (marketUps.size() == 0) {
					mkdb.Trace.error("ROLEID=" + role.roleId + "\tUSERID=" + role.loginui.getUserName() + "\tERROR=" + "上架物品列表错误");
					return;
				}
				CMarketUp cmu = marketUps.pop();
				MarketThreeTable mtt = MissionMgr.marketThreeConfMap.get(cmu.price);
				if (mtt == null) {
					System.out.println("搞笑了");
					return;
				}
				int itemId = cmu.price;
				int itemMartetUpFee = ((SGetMarketUpPrice) p).stallprice; // 摊位费
				if (mtt.israrity == 0) {
					cmu.price = (int) ((0.5 + Math.random()) * ((SGetMarketUpPrice) p).price); // 推荐价格;
				} else {
					cmu.price = (int) (Math.random() * 5000 + 1000);
				}
				role.sendProtocol(cmu);
				// mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID=" + role.loginui.getUserName() + "\tINFO=" + "上架"
				// + (cmu.containertype == 2 ? "宠物" : "物品") + "\tITEMID=" + itemId + "\t价格=" + cmu.price + " 金币" + " 摆摊费:"
				// + itemMartetUpFee + "银币");
			} else if (p instanceof SMarketUpSucc) {
				// mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID=" + role.loginui.getUserName() + "\tINFO=上架"
				// + (((SMarketUpSucc) p).israrity == 0 ? "非珍品" : "珍品") + "成功!!!");
				// if (sellId != 0) {
				// if (System.currentTimeMillis() - sendTime < 60000) {
				// String data = new StringBuffer().append(sellId).append("_").append(type).toString();
				// if (!sendGoodsList.contains(data))
				// sendGoodsList.add(data);
				// return;
				// }
				// String message = "< arg=" + '\"' + "t=1,id=" + sellId + ",type=" + type + '\"' + " />";
				// mkdb.Trace.info("发送链接ROLEID=" + role.roleId + "message:" + message);
				// java.util.ArrayList<fire.pb.talk.DisplayInfo> info = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
				// // 发送道具到世界频道
				// CTransChatMessage2Serv cTransChatMessage2Serv = new CTransChatMessage2Serv(5, message, "", info, 0, 0);
				// role.sendProtocol(cTransChatMessage2Serv);
				// sendTime = System.currentTimeMillis();
				// // 增加活跃度
				// role.sendCommand("//addhyd 20");
				// }
			} else if (p instanceof SMarketBuy) {
				mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID=" + role.loginui.getUserName() + "\tINFO=购买物品成功^_^");
			} else if (p instanceof SMarketContainerBrowse) {
				// 获取到拍卖的id和类型
				List<MarketGoods> goodsList = ((SMarketContainerBrowse) p).goodslist;
				if (sellItemId != 0) {
					for (MarketGoods marketGoods : goodsList) {
						if (marketGoods.itemid == sellItemId) {
							sellId = marketGoods.id;
							type = marketGoods.itemtype;
							MarketThreeTable mtt = MissionMgr.marketThreeConfMap.get(sellItemId);
							if (mtt == null) {
								continue;
							}
							String data = new StringBuffer().append(sellId).append("_").append(type).append("_").append(mtt.itemname)
									.toString();
							if (!sendGoodsList.contains(data))
								sendGoodsList.add(data);
							sellItemId = 0;
							break;
						}
					}
				}
				if (goodsList.size() < 8) {
					prepareToSellSomeItems(1);
				} else if (Math.random() < 0.15) {
					// 一定几率自动下架些物品
					int downGoodsNum = 1;// (int)(Math.random() *
					// (goodsList.size() / 2 + 1));
					Collections.shuffle(goodsList);
					int count = 0;
					for (MarketGoods wantDownGood : goodsList) {
						if (count >= downGoodsNum) {
							break;
						}
						MarketThreeTable mttDownGood = MissionMgr.marketThreeConfMap.get(wantDownGood.itemid);
						if (mttDownGood == null || mttDownGood.israrity == 0) {
							continue;
						}
						if (wantDownGood != null) {
							CMarketDown cmd = new CMarketDown(wantDownGood.itemtype, wantDownGood.key);
							role.sendProtocol(cmd);
							// mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID=" + role.loginui.getUserName() + "\tINFO=下架"
							// + (wantDownGood.itemtype == 2 ? "宠物" : "物品") + "\tITEMID=" + wantDownGood.itemid + "\t价格="
							// + wantDownGood.price + "金币");
						}
						count++;
					}
				}
			} else if (p instanceof STransChatMessage2Client) {
				STransChatMessage2Client sTransChatMessage2Client = (STransChatMessage2Client) p;
				// mkdb.Trace.error("**********进入协议message:role.roleId" + role.roleId + "----" + sTransChatMessage2Client.message);
				// 解析信息串
				Map<String, String> dataMap = ChatIteamMgr.getInstance().processMessage(sTransChatMessage2Client.message);
				if (dataMap.size() <= 0) {
					return;
				}
				int type = 0;
				if (dataMap.containsKey("type")) {
					type = Integer.parseInt(dataMap.get("type"));
				}
				// 非道具直接过滤掉
				if (type == 0)
					return;
				// mkdb.Trace.error("**********进入协议解析:role.roleId" + role.roleId + "---type:" + type + "id:" + dataMap.get("id"));
				if (!dataMap.containsKey("id")) {
					// 针对背包里面的道具和宠物
					CChatItemTips cChatItemTips = new CChatItemTips();
					fire.pb.talk.DisplayInfo displayinfo = new fire.pb.talk.DisplayInfo();
					displayinfo.counterid = Integer.parseInt(dataMap.get("counter"));
					displayinfo.displaytype = type;
					displayinfo.shopid = Long.parseLong(dataMap.get("shopid"));
					displayinfo.uniqid = Integer.parseInt(dataMap.get("key"));
					displayinfo.roleid = sTransChatMessage2Client.roleid;
					displayinfo.teamid = 0l;
					cChatItemTips.displayinfo = displayinfo;
					role.sendProtocol(cChatItemTips);
				} else {
					// 针对拍卖里面的道具和宠物
					CMarketItemChatShow cMarketItemChatShow = new CMarketItemChatShow();
					cMarketItemChatShow.itemtype = type;
					cMarketItemChatShow.id = Long.parseLong(dataMap.get("id"));
					for (int i = 0; i < 1; i++)
						role.sendProtocol(cMarketItemChatShow);
					// mkdb.Trace.error("**********CMarketItemChatShow");
				}
			} else {
				// isPrint = false;
			}
			// if (isPrint) {
			// System.out.println(p.getClass().getName());
			// }
		}
	}
}
