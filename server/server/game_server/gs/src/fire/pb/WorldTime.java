package fire.pb;

import java.util.Calendar;
import java.util.Date;

public class WorldTime {
	private static volatile long deltatime;
	private Calendar date;
	WorldTime(Calendar date) {
		this.date = date;
		date.setTimeInMillis(date.getTimeInMillis() + deltatime);
	}
	public static WorldTime getInstance() {
		return new WorldTime(Calendar.getInstance());
	}
	
	public boolean before(Object when) {
		return date.before(when);
	}

	public void add(int field, int amount) {
		date.add(field, amount);
	}
	
	public void set(int field, int amount) {
		date.set(field, amount);
	}
	
	public boolean after(WorldTime when) {
		return date.after(when.date);
	}

	public Date getTime() {
		return date.getTime();
	}
	
	public void setTime(Calendar newtime) {
		deltatime += newtime.getTimeInMillis() - date.getTimeInMillis();
		date.setTimeInMillis(newtime.getTimeInMillis());
	}
	
	public void setTime(WorldTime newtime) {
		deltatime += newtime.getTimeInMillis() - date.getTimeInMillis();
		date.setTimeInMillis(newtime.getTimeInMillis());
	}
	
	public int get(int field) {
		return date.get(field);
	}
	
	public long getTimeInMillis() {
		return date.getTimeInMillis();
	}
	
	
}
