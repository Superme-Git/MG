package robot.ride;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 宠物
 * 
 * @author yangtao
 * @dateTime 2016年6月2日 下午6:51:19
 * @version 1.0
 */
public class PetUtils {

	public static List<Integer> petList = new ArrayList<>();

	public static List<Integer> netPetList = new ArrayList<>();

	static int size = 0;

	static int netSize = 0;
	static {
		for (int i = 500100; i <= 500301; i++) {
			petList.add(i);
		}
		for (int i = 506001; i <= 506013; i++) {
			petList.add(i);
		}
		petList.add(510001);
		petList.add(510002);
		petList.add(510003);
		petList.add(510004);
		netPetList.addAll(petList);
		netSize = netPetList.size();
		petList.add(900001);
		petList.add(900002);
		size = petList.size();
	}

	/**
	 * 随机宠物id
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月2日 下午7:00:41
	 * @version 1.0
	 * @return
	 */
	public static int getRandomPetId() {
		return petList.get((int) (Math.random() * size));
	}

	/**
	 * 外网上随机宠物id
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月2日 下午7:00:41
	 * @version 1.0
	 * @return
	 */
	public static int getNetRandomPetId() {
		return netPetList.get((int) (Math.random() * netSize));
	}
}
