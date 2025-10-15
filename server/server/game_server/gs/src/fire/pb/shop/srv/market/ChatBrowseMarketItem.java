package fire.pb.shop.srv.market;

import fire.pb.shop.SMarketItemChatShow;

public class ChatBrowseMarketItem {

	private int state = 0;
	private int query = 0;
	private SMarketItemChatShow chatBrowse = null;
	private long showTime = 0L;
	private long expireTime = 0L;
	private long cacheCount =  0L;
	private int showType = 0;
	
	public ChatBrowseMarketItem(SMarketItemChatShow chatBrowse, long showTime, long expireTime, int showType) {
		super();
		this.chatBrowse = chatBrowse;
		this.showTime = showTime;
		this.expireTime = expireTime;
		this.showType = showType;
	}

	/**
	 * 道具状态
	 * 0:有效 1:无效
	 */
	public int getState() {
		return state;
	}

	/**
	 * 道具状态
	 * 0:有效 1:无效
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 是否重新查询
	 * 0:数据无变化 1:数据有变化
	 */
	public int getQuery() {
		return query;
	}

	/**
	 * 是否重新查询
	 * 0:数据无变化 1:数据有变化
	 */
	public void setQuery(int query) {
		this.query = query;
	}

	public SMarketItemChatShow getChatBrowse() {
		return chatBrowse;
	}

	public void setChatBrowse(SMarketItemChatShow chatBrowse) {
		this.chatBrowse = chatBrowse;
	}

	public long getShowTime() {
		return showTime;
	}

	public void setShowTime(long showTime) {
		this.showTime = showTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public long getCacheCount() {
		this.cacheCount += 1;
		return cacheCount;
	}

	/**
	 * 浏览类型  1 我要购买 2公示物品
	 */
	public int getShowType() {
		return showType;
	}
	
	/**
	 * 浏览类型  1 我要购买 2公示物品
	 */
	public void setShowType(int showType) {
		this.showType = showType;
	}
	
}
