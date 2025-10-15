package fire.log.beans;

import java.util.List;

public class TotalValueState {
	
	private String tick;
	
	private int historyUserNum;
	
	private int increaseUserNum;
	
	private int historyRoleNum;

	private int increaseRoleNum;
	
	private int chargeUserNum;
	
	private int chargeTotal;
	
	private int consumeRoleNum;
	
	private int consumeTotal;
	
	private List<ActiveRole> activeRoleList;
	
	private List<ActiveUser> activeUsersList;
	
	private List<ChargeUser> chargeUsersList;
	
	private List<ConsumeRole> consumeRolesList;
	
	public int getHistoryRoleNum() {
		return historyRoleNum;
	}

	public void setHistoryRoleNum(int historyRoleNum) {
		this.historyRoleNum = historyRoleNum;
	}

	public int getIncreaseRoleNum() {
		return increaseRoleNum;
	}

	public void setIncreaseRoleNum(int increaseRoleNum) {
		this.increaseRoleNum = increaseRoleNum;
	}
	
	public int getHistoryUserNum() {
		return historyUserNum;
	}

	public void setHistoryUserNum(int historyUserNum) {
		this.historyUserNum = historyUserNum;
	}

	public int getIncreaseUserNum() {
		return increaseUserNum;
	}

	public void setIncreaseUserNum(int increaseUserNum) {
		this.increaseUserNum = increaseUserNum;
	}

	public int getChargeUserNum() {
		return chargeUserNum;
	}

	public void setChargeUserNum(int chargeUserNum) {
		this.chargeUserNum = chargeUserNum;
	}

	public int getChargeTotal() {
		return chargeTotal;
	}

	public void setChargeTotal(int chargeTotal) {
		this.chargeTotal = chargeTotal;
	}

	public int getConsumeRoleNum() {
		return consumeRoleNum;
	}

	public void setConsumeRoleNum(int consumeRoleNum) {
		this.consumeRoleNum = consumeRoleNum;
	}

	public int getConsumeTotal() {
		return consumeTotal;
	}

	public void setConsumeTotal(int consumeTotal) {
		this.consumeTotal = consumeTotal;
	}

	public List<ActiveRole> getActiveRoleList() {
		return activeRoleList;
	}

	public void setActiveRoleList(List<ActiveRole> activeRoleList) {
		this.activeRoleList = activeRoleList;
	}

	public List<ActiveUser> getActiveUsersList() {
		return activeUsersList;
	}

	public void setActiveUsersList(List<ActiveUser> activeUsersList) {
		this.activeUsersList = activeUsersList;
	}

	public List<ChargeUser> getChargeUsersList() {
		return chargeUsersList;
	}

	public void setChargeUsersList(List<ChargeUser> chargeUsersList) {
		this.chargeUsersList = chargeUsersList;
	}

	public List<ConsumeRole> getConsumeRolesList() {
		return consumeRolesList;
	}

	public void setConsumeRolesList(List<ConsumeRole> consumeRolesList) {
		this.consumeRolesList = consumeRolesList;
	}
	
	public String getTick() {
		return tick;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}
	
}
