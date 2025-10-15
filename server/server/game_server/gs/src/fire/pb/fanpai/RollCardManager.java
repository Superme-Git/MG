package fire.pb.fanpai;

import java.util.*;

import fire.log.Logger;
import fire.pb.game.SRollCardConfig;
import fire.pb.npc.ForturneWheelType;
import fire.pb.util.HighQualityRandom;
import fire.pb.util.Misc;


public class RollCardManager {

	static class SingletonHolder {
		static RollCardManager singleton = new RollCardManager();
	}

	public static RollCardManager getInstance() {
		return SingletonHolder.singleton;
	}

	public static final Logger logger = Logger.getLogger("AWARD");

	private static Random hRandom = new HighQualityRandom();

	private Map<Integer, List<CardItem>> rollCardMap = new HashMap<Integer, List<CardItem>>();

	private RollCardManager() {
		reload();
	}

	public void reload() {
		Map<Integer, SRollCardConfig> map = fire.pb.main.ConfigManager
				.getInstance().getConf(SRollCardConfig.class);
		if (map != null) {
			for (SRollCardConfig config : map.values()) {

				List<CardItem> cardItems = new ArrayList<CardItem>();
				rollCardMap.put(config.getId(), cardItems);

				for (String item : config.getObjs()) {
					CardItem cardItem = new CardItem();
					item = item.replace("；", ";");
					String[] temp = item.split(";");
					if (temp.length < 6) {
						logger.info("Exception:RollCard  配置文件有问题");
						continue;
					}

					int type = Integer.parseInt(temp[0]);
					int number = Integer.parseInt(temp[1]);
					int times = Integer.parseInt(temp[3]);
					int probability = Integer.parseInt(temp[4]);
					int mustShoot = 0;
					int itemId = 0;
					int bind = 0;
					int msgId = 0;

					// 物品的特有属性
					if ((type == 1 || type == 5) && temp.length == 8) {
						itemId = Integer.parseInt(temp[2]);
						bind = Integer.parseInt(temp[5]);
						msgId = Integer.parseInt(temp[6]);
						mustShoot = Integer.parseInt(temp[7]);
					}
					else {
						mustShoot = Integer.parseInt(temp[5]);
					}

					cardItem.setType(type);
					cardItem.setBind(bind);
					cardItem.setTimes(times);
					cardItem.setNumber(number);
					cardItem.setMsgId(msgId);
					cardItem.setProbability(probability);
					cardItem.setItemId(itemId);
					cardItem.setRealIndex(mustShoot);
					cardItem.setObs(item);
					cardItems.add(cardItem);
				}
			}
		}
	}

	
	public List<CardItem> choseCard(int keyId, int choseNum) {
		List<CardItem> items = rollCardMap.get(keyId);

		List<CardItem> choseItem = new ArrayList<CardItem>();
		if (items == null) {
			return choseItem;
		}
		List<CardItem> allItem = new ArrayList<CardItem>();
		allItem.addAll(items);
		if (items.size() <= choseNum) {
			choseItem.addAll(items);
		} else {

			// 拿出来必中的东西
			for (CardItem item : allItem) {
				if (item.getRealIndex() == 1) {
					choseItem.add(item);
				}
			}

			// 移除多余的
			if (choseItem.size() > choseNum) {
				for (int i = choseItem.size() - 1; i >= choseNum; i--) {
					choseItem.remove(i);
				}
			}

			// 已经选够数目了
			int needsize = choseNum - choseItem.size();
			if (needsize <= 0) {
				return choseItem;
			}

			// 不够继续选
			allItem.removeAll(choseItem);

			for (int i = 0; i < needsize; i++) {
				int[] proArray = new int[allItem.size()];
				for (int j = 0; j < allItem.size(); j++) {
					proArray[j] = allItem.get(j).getProbability();
				}
				int index = Misc.getProbability(proArray, hRandom);

				if (index != -1) {
					CardItem aa = allItem.remove(index);
					choseItem.add(aa);
				}
			}
		}

		return choseItem;
	}

	
	public int getSelectIndex(List<CardItem> items) {
		int[] proArray = new int[items.size()];
		for (int j = 0; j < items.size(); j++) {
			proArray[j] = items.get(j).getProbability();
		}
		int index = Misc.getProbability(proArray, hRandom);
		return index == -1 ? 0 : index;
	}

	public void clearLastCard(xbean.RollCardInfo info) {
		info.setBaseexp(0);
		info.setBasemoney(0);
		info.setBasesmoney(0);
		info.setIndex(0);
		info.setServiceid(0);
		info.setTakeflag(0);
		info.getWheelitems().clear();
	}

	public List<xbean.WheelItem> toWheelItemList(List<CardItem> items) {
		List<xbean.WheelItem> list = new ArrayList<xbean.WheelItem>();
		for (CardItem item : items) {
			xbean.WheelItem wheelItem = xbean.Pod.newWheelItem();
			wheelItem.setItemid(item.getItemId());
			wheelItem.setBind(item.getBind());
			wheelItem.setItemtype(item.getType());
			wheelItem.setMsgid(item.getMsgId());
			wheelItem.setNum(item.getNumber());
			wheelItem.setTimes(item.getTimes());
			list.add(wheelItem);
		}
		return list;
	}

	public ArrayList<ForturneWheelType> tiForturneWheelType(List<CardItem> items) {
		ArrayList<ForturneWheelType> wheelItems = new ArrayList<ForturneWheelType>();
		for (CardItem item : items) {
			ForturneWheelType wheel = new ForturneWheelType();
			wheel.id = item.getItemId();
			wheel.itemtype = item.getType();
			wheel.num = item.getNumber();
			wheel.times = item.getTimes();
			wheelItems.add(wheel);
		}
		return wheelItems;
	}

}
