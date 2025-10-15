package fire.pb.gm;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.util.Misc;

public class GM_random extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 2)
			return false;
		int max = Integer.parseInt(args[0]);
		int min = 1;
		int gap = Integer.parseInt(args[1]);
		int[] ranValues = new int[max];
		for (int i = 0; i < max; i++) {
			ranValues[i] = Misc.getRandomBetween(min, max);
		}
		int range = max / gap;
		Arrays.sort(ranValues);
		Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
		int count = 0;
		int limit = range;
		for (int i = 0; i < ranValues.length; i++) {
			if (ranValues[i] <= limit) {
				count++;
			} else {
				result.put(limit, count);
				count = 1;
				limit += range;
			}
			if (i == ranValues.length - 1)
				result.put(limit, count);
		}
		for (Entry<Integer, Integer> entry : result.entrySet()) {
			StringBuffer sb = new StringBuffer();
			sb.append("[").append(entry.getKey() - range + 1).append(",")
					.append(entry.getKey()).append("]:")
					.append(entry.getValue());
			sendToGM(sb.toString());
		}
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
