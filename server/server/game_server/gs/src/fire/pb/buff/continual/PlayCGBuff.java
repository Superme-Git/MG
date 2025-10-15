
package fire.pb.buff.continual;

import xbean.Buff;

/**CG播放*/
public class PlayCGBuff extends ConstantlyBuff{
	public static final int GOTO_EVENT = 1; //动画播完后跳转	
	public static final int MAPID=11;//这三个变量都是给goto event用的
	public static final int POSX=12;
	public static final int POSY=13;
	public PlayCGBuff(Buff buffBean) {super(buffBean);}	
    public PlayCGBuff(ConstantlyBuffConfig buffConfig){super(buffConfig);}
}

