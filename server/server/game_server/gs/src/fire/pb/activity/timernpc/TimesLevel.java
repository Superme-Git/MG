package fire.pb.activity.timernpc;

public class TimesLevel {
	public final int id;
	public final int minlevel;
	public final int maxlevel;
	public final int times;
	
	public TimesLevel(int id, int minlevel, int maxlevel, int times) {
		this.id = id;
		this.minlevel = minlevel;
		this.maxlevel = maxlevel;
		this.times = times;
	}
}
