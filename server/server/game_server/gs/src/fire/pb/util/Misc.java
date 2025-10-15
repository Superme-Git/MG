
package fire.pb.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Random;


public class Misc {

	private static SecureRandom random = new SecureRandom();
	
	public static int getRandomBetween(final int start, final int end, Random random) {

		return end > start ? random.nextInt(end - start + 1) + start : random.nextInt(start - end + 1) + end;
	}
	
	/**
	 * 这个是[]
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomBetween(final int start, final int end) {

		return end > start ? mkdb.Mkdb.random().nextInt(end - start + 1) + start : mkdb.Mkdb.random().nextInt(start - end + 1) + end;
	}
	
	/**
	 * 这个是[)注意传递的区间
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomNotBetween(int start, int end) {
		end=end-1;
		if(end-start<0){
			return 0;
		}
		return end > start ? mkdb.Mkdb.random().nextInt(end - start + 1) + start : mkdb.Mkdb.random().nextInt(start - end + 1) + end;
	}

	
	@SuppressWarnings("unchecked")
	public static <T> T getRandom(Collection<T> collections) {
		if(collections == null || collections.isEmpty())
			return null;
		
		Object[] os = collections.toArray();
		int index = getRandomBetween(0, os.length -1);
		return (T)os[index];
	}
	
	
	public static int getRatePercent() {

		return getRandomBetween(1, 100);
	}

	
	public static boolean checkRatePercent(int value) {

		return value >= getRatePercent();
	}

	
	public static int getRateValue(int base) {

		return getRandomBetween(0, base);
	}
	public static int getRateValue(int base,Random random) {
		
		return getRandomBetween(0, base,random);
	}

	
	public static boolean checkRate(int base, int value) {

		if (base < 0)
			return false;
        int tmp = getRateValue(base);
		return value > tmp;
	}
	public static boolean checkRate(int base, int value,Random random) {

		if (base < 0)
			return false;
        int tmp = getRateValue(base,random);
		return value > tmp;
	}

	
	public static java.util.List<Integer> getRandomValues(int min, int max, int num) {

		final java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		if (max < 0)
			return list;
		if (num <= 0)
			return list;
		if (max < min)
			return list;

		if (num > (max - min + 1)) {
			num = max - min + 1;
		}

		for (int i = 0; i < num; ++i) {
			int val = getRandomBetween(min, max);
			while (list.contains(val)) {
				val = min + ((val + 1 - min) % (max - min + 1));
			}

			list.add(val);
		}

		return list;
	}

	
	public static void RandomDistribute(int value, int num, int[] result) {

		if (num <= 0)
			return;
		if (null == result || result.length < num)
			return;
		if (value <= 0) {
			result[0] = value;
			return;
		}
		final int[] r = new int[num + 1];
		r[0] = 0;
		r[num] = value;
		for (int i = 1; i <= num - 1; i++) {
			r[i] = mkdb.Mkdb.random().nextInt(value + 1);
		}
		Arrays.sort(r);
		for (int i = 0; i < num; i++) {
			result[i] = r[i + 1] - r[i];
		}
	}

	
	public static int getProbability(int[] pros, int base) {

		if (pros == null || pros.length == 0)
			return -1;
		int sum = 0;
		for (int i = 0; i < pros.length; i++) {
			sum += pros[i];
		}
		// 相加之和应该等于base，否则返回getProbability(pros);
		if (sum != base)
			return getProbability(pros);
		int value = getRandomBetween(0, base - 1);

		for (int i = 0; i < pros.length; i++) {
			if (value < pros[i])
				return i;
			else
				value = value - pros[i];
		}

		return -1;
	}
	
	
	public static int getProbability(int[] pros, int base, Random random) {

		if (pros == null || pros.length == 0)
			return -1;
		int sum = 0;
		for (int i = 0; i < pros.length; i++) {
			sum += pros[i];
		}
		// 相加之和应该等于base，否则返回getProbability(pros);
		if (sum != base)
			return getProbability(pros,random);
		int value = getRandomBetween(0, base - 1,random);

		for (int i = 0; i < pros.length; i++) {
			if (value < pros[i])
				return i;
			else
				value = value - pros[i];
		}

		return -1;
	}

	
	public static int getProbability(int[] pros) {

		if (pros == null || pros.length == 0)
			return -1;
		int sum = 0;
		for (int i = 0; i < pros.length; i++) {
			sum += pros[i];
		}
        if (sum<=0) {
			return -1;
		}
		int value = getRandomBetween(0, sum - 1);

		for (int i = 0; i < pros.length; i++) {
			if (value < pros[i])
				return i;
			else
				value = value - pros[i];
		}

		return -1;
	}
	
	
	public static int getProbability(int[] pros,Random random) {

		if (pros == null || pros.length == 0)
			return -1;
		int sum = 0;
		for (int i = 0; i < pros.length; i++) {
			sum += pros[i];
		}

		int value = getRandomBetween(0, sum - 1,random);

		for (int i = 0; i < pros.length; i++) {
			if (value < pros[i])
				return i;
			else
				value = value - pros[i];
		}

		return -1;
	}

	public static int getProbability(java.util.List<Integer> prolist, int base) {

		final int[] probs = new int[prolist.size()];
		for (int i = 0; i < probs.length; i++)
			probs[i] = prolist.get(i);

		return getProbability(probs, base);
	}
	
	public static int getProbabilityByBase(java.util.List<Integer> prolist, int base) {
		if (prolist.size() == 0)
			return -1;
		
		final int[] probs = new int[prolist.size()];
		for (int i = 0; i < probs.length; i++)
			probs[i] = prolist.get(i);
		
		int sum = 0;
		for (int i = 0; i < probs.length; i++) {
			sum += probs[i];
		}
        if (sum <= 0) {
			return -1;
		}
		int value = getRandomBetween(0, base - 1);

		for (int i = 0; i < probs.length; i++) {
			if (value < probs[i])
				return i;
			else
				value = value - probs[i];
		}

		return -1;
	}
	
	public static int getProbability(java.util.List<Integer> prolist) {

		final int[] probs = new int[prolist.size()];
		for (int i = 0; i < probs.length; i++)
			probs[i] = prolist.get(i);

		return getProbability(probs);
	}
	public static int getProbability(java.util.List<Integer> prolist,Random random) {
		
		final int[] probs = new int[prolist.size()];
		for (int i = 0; i < probs.length; i++)
			probs[i] = prolist.get(i);
		
		return getProbability(probs,random);
	}

	
	public static int[] getValuesByRate(int[] pros, int num, int base) {

		final int[] ret = new int[num];
		for (int i = 0; i < num; i++)
			ret[i] = getProbability(pros, base);

		return ret;
	}

	public static java.util.List<Integer> getValuesByRate(java.util.List<Integer> pros, int num, int base) {

		final java.util.List<Integer> list = new java.util.ArrayList<Integer>();
		final int[] probs = new int[pros.size()];
		for (int i = 0; i < probs.length; i++)
			probs[i] = pros.get(i);

		for (int i = 0; i < num; i++)
			list.add(getProbability(probs, base));

		return list;
	}

	
	public static int[] getRandomArray(int[] totals, int dest) {

		if (dest <= 0)
			throw new IllegalArgumentException();
		if (dest > totals.length) //如果要选的数比数组的长度还长,那就直接返回整个数组
			return totals;
		int[] ranArr = new int[dest];
		for (int i = 0; i < dest; i++) {
			// 得到一个位置
			int j = mkdb.Mkdb.random().nextInt(totals.length - i);
			ranArr[i] = totals[j];
			// 将未用的数字放到已经被取走的位置中,这样保证不会重复
			totals[j] = totals[totals.length - 1 - i];
		}
		return ranArr;
	}
	
	//这个方法和上边的一样，只是为了满足一些数组里装的是roleid  factionke什么的
	//比较麻烦 ，所以就抄了一遍
	public static Long[] getRandomArray2(Long[] srcArray, int dest) {

		if (dest <= 0)
			throw new IllegalArgumentException();
		if (dest > srcArray.length) //如果要选的数比数组的长度还长,那就直接返回整个数组
			return srcArray;
		Long[] ranArr = new Long[dest];
		for (int i = 0; i < dest; i++) {
			// 得到一个位置
			int j = mkdb.Mkdb.random().nextInt(srcArray.length - i);
			ranArr[i] = srcArray[j];
			// 将未用的数字放到已经被取走的位置中,这样保证不会重复
			srcArray[j] = srcArray[srcArray.length - 1 - i];
		}
		return ranArr;
	}

	/**
	 * 把原有的容器打乱
	 * @param collection
	 * @return
	 */
	public static <T> List<T> getRandomList(Collection<T> collection) {
		int[] tmpArray = new int[collection.size()];
		for (int i = 0; i < tmpArray.length; i++) 
			tmpArray[i] = i;
		int[] chosen = Misc.getRandomArray(tmpArray, tmpArray.length);
		List<T> result = new LinkedList<T>();
		result.addAll(collection);
		
		for (int i = 0; i < chosen.length; i ++) {
			if (i != chosen[i]) {
				T t = result.get(chosen[i]);
				result.set(chosen[i], result.get(i));
				result.set(i, t);
			}
		}
		
		return result;
	}
	
	
	public static <T> List<T> getRandomList(Collection<T> collection, int num) {
		
		//如果集合小于需要的个数，则直接返回一个乱序的list
		if(collection.size() <= num)
		{
			List<T> result = new LinkedList<T>();
			result.addAll(collection);
//			randomlizeList(result);
			Collections.shuffle(result);
			return result;
		}
		
		int[] tmpArray = new int[collection.size()];
		for (int i = 0; i < tmpArray.length; i++) 
			tmpArray[i] = i;
		int[] chosen = Misc.getRandomArray(tmpArray, num);
		List<T> result = new LinkedList<T>();
		Arrays.sort(chosen);
		int i = 0;
		int j = 0;
		for(T t : collection)
		{
			if(chosen[j] == i)
			{
				result.add(t);
				j++;
				if(j >= chosen.length)
					break;
			}
			i++;
		}
		
		return result;
	}
	
	
	public static <T> List<T> getRandomList(Collection<T> collection, int num, List<Integer> indexcollection) {
		
		//如果集合小于需要的个数，则直接返回一个乱序的list
		if(collection.size() <= num)
		{
			List<T> result = new LinkedList<T>();
			result.addAll(collection);
			randomlizeList(result);
			return result;
		}
		
		int[] tmpArray = new int[collection.size()];
		for (int i = 0; i < tmpArray.length; i++) 
			tmpArray[i] = i;
		int[] chosen = Misc.getRandomArray(tmpArray, num);
		List<T> result = new LinkedList<T>();
		Arrays.sort(chosen);
		int i = 0;
		int j = 0;
		for(T t : collection)
		{
			if(chosen[j] == i)
			{
				result.add(t);
				indexcollection.add(j);
				j++;
				if(j >= chosen.length)
					break;
			}
			i++;
		}
		
		return result;
	}
	
	
	
	public static<T> void randomlizeList( java.util.List<T> list ) {
		for ( int i = 1; i < list.size(); i++ ) {
			final int idx = getRandomBetween( 0, i );
			if ( i != idx ) {
				T t = list.get( idx );
				list.set( idx, list.get( i ) );
				list.set( i, t );
			}
		}
	}
	
	
	public static int randomValue(final int value, final double minPct, final double maxPct)
	{
		return getRandomBetween((int)(value * minPct), (int)(value * maxPct));
	}
	
	public static java.util.List<Integer> getRandomNotRepeatIntList(int min, int max, int step, int retsize) {
		if (max < min || step <= 0 || (max-min+1)/step<retsize) {
			throw new IllegalArgumentException("错误的参数");
		}
		List<Integer> ret = new ArrayList<Integer>();
		int size = (max - min + 1)/step;
		Map<Integer, Integer> swaps = new HashMap<Integer, Integer>();
		for (int i = 0; i < retsize; i++) {
			int idx = getRandomBetween(i, size-1);
			Integer curval = swaps.get(i);
			if (curval == null) {
				curval = i*step+min;
			}
			if (idx == i) {
				ret.add(curval);
				continue;
			}
			Integer swapval = swaps.get(idx);
			if (swapval == null) {
				swapval = idx*step+min;
			}
			if (idx > i) {
				swaps.put(idx, curval);
			}
			ret.add(swapval);
		}
		return ret;
	}
	
	public static boolean isProbability(Double odds) {
		if (odds.intValue() >= 1) {
			return true;
		}
		if (odds.intValue() < 0) {
			return false;
		}
		return random.nextDouble() <= odds;
	}

	public static void main(String[] args) {
		List<Integer> array = getRandomValues(1, 2, 0);
		System.out.println(array);
	}
	
}
