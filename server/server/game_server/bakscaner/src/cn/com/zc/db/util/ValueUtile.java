package cn.com.zc.db.util;

import java.util.Calendar;

public class ValueUtile {
	
	/**
	 * 如果是等步长的区间，那么就用二分法来查找区间的index
	 * 
	 * @param valueArray
	 * @param currentV
	 * @param space
	 * @return
	 */
	public static int binSearchValueSpace(int valueArray[], int currentV, int space){
		int mid = valueArray.length/2;
		if(inValueSpace(valueArray[mid], currentV, space)){
			return mid;
		}
		
		int start = 0;
		int end = valueArray.length - 1;
		while(start <= end){
			mid = (end - start)/2 + start;
			
			if(currentV < valueArray[mid]){
				end = mid - 1;
			}else if(currentV > valueArray[mid]+space-1){
				start = mid + 1;
			}else{
				return mid;
			}
		}
		
		return -1;
	}

	private static boolean inValueSpace(int vIndex, int currentV, int space) {
		if(currentV > vIndex && currentV <= (vIndex+space)){
			return true;
		}
		return false;
	}
	
	/**
	 * 如果是非等步长的区间，那么就遍历数组来确定区间的index
	 * 
	 * @param valueArray
	 * @param currentV
	 * @return
	 */
	public static int searchValueSpace(int valueArray[], int currentV){
		if(currentV == 0)
			return -1;
		
		int index = 0;
		for(int i=0; i<valueArray.length; i++){
			if(i == valueArray.length-1){
				return i;
			}
			if(currentV > valueArray[i] && currentV <= valueArray[i+1]){
				return index;
			}
			index ++;
		}
		
		return index;
	}
	
	public static int get360Flag(long offlinetime, long lastOnline) {
		if(lastOnline > offlinetime){
			return 0;
		}else{
			long timeSpace = System.currentTimeMillis() - offlinetime;
			long _360Time = 365L*24*60*60*1000;
			if(timeSpace >= _360Time){
				return 1;
			}else{
				return 0;
			}
		}
	}
	
	public static void main(String[] args) {
//		int index = binSearchValueSpace(Constant.ROLE_LEVEL_INTERAL, 1, Constant.ROLE_LEVEL_SPACE);
//		if(-1 == index){
//			System.out.println("并未找到");
//			return;
//		}
//		int[] tmp = Constant.ROLE_LEVEL_INTERAL;
//		System.out.println("数组下标  ：" + index);
//		System.out.println(Constant.ROLE_LEVEL_INTERAL[index]);
//		
//		System.out.println("===============================");
//		System.out.println(searchValueSpace(Constant.TRADE_LIMIT, 0));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2011, 8, 2);
		System.out.println(calendar.getTimeInMillis()/(1000*60*60*24));
		
	}

}
