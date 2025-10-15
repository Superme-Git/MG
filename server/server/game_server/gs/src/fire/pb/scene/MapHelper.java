package fire.pb.scene;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import fire.pb.scene.sPos.BasicPos;
import fire.pb.scene.sPos.ScreenPos;

/**
 * 场景屏的数据处理
 * @author nobody
 *
 */
public final class MapHelper {
	
	private final static Random random = new Random();
	/**
	 * @return
	 * 获得随机数
	 */
	public static Random getRandom() {
		return random;
	}

	
	/**
	 * 根据屏幕区块索引判断两区块的方位关系
	 * @param hereIndex		当前
	 * @param whereIndex	目标
	 * @return
	 */
	public static int where(int hereIndex, int whereIndex){
		return new ScreenPos(hereIndex).where(new ScreenPos(whereIndex));
	}	
	
	/**
	 * 当角色移动到新的屏幕区块时，向角色移动方向前面的屏幕区块（3/5块或者更少）
	 * 发送一些通消息，这个方法用来找到这些屏幕区块的索引		 
	 *   _ _ _
	 *  |_|_|_|
	 *  |_|_|_|
	 *  |_|_|_|
	 * @param screenIndex 即将进入的屏幕区块的索引
	 * @param dir 移动方向
	 * @return 获得的人物前面的若干区块索引
	 * @param
	 * x, y  屏块坐标
	 * w, h  最大屏块的坐标
	 */
	public static Set<Integer> getForwardScreenIndex(int x, int y, int w, int h, int dir)
	{
		final Set<Integer> ret = new HashSet<Integer>();
		
		switch(dir){
		case BasicPos.DIR_UP:{
			if(y-1 >= 0)
			{
				// 向上的三屏
				for(int i=x-1; i<=x+1; i++){
					if(i>=0 && i<w)
						ret.add((i<<16) + (y-1));
				}
			}
		}break;
		case BasicPos.DIR_RIGHTUP:{
			if((y-1) >= 0)
			{
				// 向上的三屏
				for(int i=x-1; i<=x+1; i++){
					if(i>=0 && i<w)
						ret.add((i<<16) + (y-1));
				}
			}
			
			if((x+1) < w)
			{
				// 向右的二屏
				ret.add(((x+1)<< 16) + y);			// 右侧相邻
				if((y+1) < h)						// 向下相邻
					ret.add(((x+1)<< 16) + (y+1));	
			}						
		}break;
		case BasicPos.DIR_RIGHT:{
			if((x+1) < w)
			{
				// 向右的三屏
				for(int j=y-1; j<=y+1; j++)
				{
					if(j>=0 && j< h )
						ret.add(((x+1) << 16) + j);
				}
			}
		}break;
		
		case BasicPos.DIR_RIGHTDOWN:{
			if((y+1) < h)
			{	//向下的三屏
				for(int i=x-1; i<=x+1; i++)
				{
					if(i>=0 && i<w){
						ret.add((i<<16) + (y+1));
					}
				}
			}
			if((x+1) < w)
			{	//向右的二屏幕
				ret.add(((x+1)<<16) + y);			// 右侧相邻
				if((y-1) >= 0)						// 右上相邻
					ret.add(((x+1)<<16) + (y-1));
			}
		}break;

		case BasicPos.DIR_DOWN:
		{
			if((y+1) < h)
			{
				//向下的三屏
				for(int i=x-1; i<=x+1; i++)
				{
					if(i>=0 && i<w){
						ret.add((i<<16) + (y+1));
					}
				}
			}
		}break;
		case BasicPos.DIR_LEFTDOWN:{
			if((y+1) < h)
			{	
				//向下的三屏
				for(int i=x-1; i<=x+1; i++)
				{
					if(i>=0 && i<w)
					{
						ret.add((i<<16) + (y+1));
					}
				}
			}
			
			if(x-1 >= 0)
			{	
				//向左的二屏
				ret.add(((x-1)<<16) + y); 			// 左侧相邻
				if((y-1) >= 0 )						// 左上相邻
					ret.add(((x-1)<<16) + (y-1));
			}
		}break;
		
		case BasicPos.DIR_LEFT:{
			if((x-1) >= 0)
			{
				//向左的三屏幕
				for(int j=y-1; j<=y+1; j++){
					if(j>=0 && j<h)
						ret.add(((x-1)<<16) + j);
				}
			}
		}break;
		case BasicPos.DIR_LEFTUP:{
			if((x-1) >= 0)
			{
				//向左的三屏幕
				for(int j=y-1; j<=y+1; j++){
					if(j>=0 && j<h)
						ret.add(((x-1)<<16) + j);
				}
			}
			if((y-1) >= 0)
			{
				ret.add((x<<16) + (y-1));			// 向上相邻
				if((x+1) < w)						// 右上相邻
					ret.add(((x+1)<< 16) + (y-1));
			}
		}break;
		
		default: break;
		}
		
		return ret;
	}

}
