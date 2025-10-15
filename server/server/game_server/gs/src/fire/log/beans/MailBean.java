package fire.log.beans;

import java.util.ArrayList;
import java.util.List;

public class MailBean {
	private byte kind;//邮件类型
	private long mailid;//邮件编号
	private String titile;//邮件标题
	private String content;//邮件内容
	private String time;//邮件领取时间
	private List<ItemBaseBean> items=new ArrayList<ItemBaseBean>();//邮件物品

	public MailBean(byte kind, long mailid, String titile, String content,
			String time, List<ItemBaseBean> items) {
		this.kind = kind;
		this.mailid = mailid;
		this.titile = titile;
		this.content = content;
		this.time = time;
		this.items.addAll(items);
	}
	
	public byte getKind() {
		return kind;
	}

	public void setKind(byte kind) {
		this.kind = kind;
	}

	public long getMailid() {
		return mailid;
	}

	public void setMailid(long mailid) {
		this.mailid = mailid;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<ItemBaseBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBaseBean> items) {
		this.items = items;
	}
	
	


}
