
package com.locojoy.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class Idcard
{
	///////////////////////////////////////////////////////////////////////////////
	// address(6) + birthday(8) + serial(3) + code(1)
	// address  : province(2) + city(2) + area(2)
	// birthday : year(4) + month(2) + day(2)

	///////////////////////////////////////////////////////////////////////////////
	// 转换成18位身份证，并作一些验证。

	private static java.text.SimpleDateFormat sdf;

	static
	{
		sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		sdf.setLenient(false);
	}

	public static String to18(String idcard) throws java.text.ParseException
	{
		switch (idcard.length())
		{
		case 15:
			{
				// XXX always insert "19"
				String id17 = idcard.substring(0, 6) + "19" + idcard.substring(6);
				idcard = id17 + makeISOMod(id17);
			}
			break;

		case 18:
			verifyISOMod(idcard);
			break;

		default:
			throw new RuntimeException("invalid length : " + idcard);
		}

		verifyAddress(idcard);
		verifyBirthday(idcard);

		return idcard;
	}

	///////////////////////////////////////////////////////////////////////////////
	// 以下方法的参数除非特别说明，都要求是18位身份证
	// odd is male
	public static boolean isFemale(String idcard) { return (getSerial(idcard) & 1) == 0; }
	public static int getSerial(String idcard)    { return Integer.valueOf(idcard.substring(14, 17)); }

	public static int getYear(String idcard)  { return Integer.valueOf(idcard.substring(6, 10)); }
	public static int getMonth(String idcard) { return Integer.valueOf(idcard.substring(10, 12)); }
	public static int getDay(String idcard)   { return Integer.valueOf(idcard.substring(12, 14)); }

	public static String getBirthday(String idcard)   { return idcard.substring(6, 14); }

	public static int getProvince(String idcard) { return Integer.valueOf(idcard.substring(0, 2)); }

	public static int getAge(String idcard)
	{
		return getAge(getYear(idcard), getMonth(idcard), getDay(idcard));
	}

	public static int getAge(int year, int month, int day)
	{
		Calendar cal = Calendar.getInstance();

		int age = cal.get(Calendar.YEAR) - year;

		int dmonth = 1 + cal.get(Calendar.MONTH) - month;
		if (dmonth < 0) return age - 1;
		if (dmonth > 0) return age;

		int dday = cal.get(Calendar.DATE) - day;
		if (dday < 0) return age - 1;
		// if (dday > 0) return age;
		return age;
	}

	///////////////////////////////////////////////////////////////////////////////
	// helper
	public static boolean isLeapYear(int year)
	{
		return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
	}

	public static int dayOfMonth(int year, int month)
	{
		switch (month)
		{
		case 4: case 6: case 9: case 11: return 30;
		case 2: return isLeapYear(year) ? 29 : 28;
		default: return 31;
		}
	}

	// ISO 7064: 1983, MOD 11-2.
	public static final int [] w = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	//public static final int [] m = new int[] { 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };

	// idcard.length 17, 18
	public static int sumISOMod(String idcard)
	{
		int sum = 0;

		int end = idcard.length() - 1;
		char last = idcard.charAt(end);

		if (last == 'x' || last == 'X')
			sum += 10; // w[end] is 1
		else
			++ end;

		for (int i = 0; i < end; ++i)
			sum += w[i] * (idcard.charAt(i) - '0');

		return sum;
	}

	public static char makeISOMod(String idcard)
	{
		int idx = sumISOMod(idcard) % 11;

		switch (idx)
		{
		case 0: return '1';
		case 1: return '0';
		case 2: return 'x';
		default: return Character.forDigit(12 - idx, 10);
		}
	}

	public static void verifyISOMod(String idcard)
	{
		int sum = sumISOMod(idcard);
		if (sum % 11 != 1)
			throw new RuntimeException("invalid isomod11-2 : " + idcard);
	}

	// GB/T7408
	public static void verifyBirthday(String idcard) throws java.text.ParseException
	{
		// verify birthday format
		sdf.parse(getBirthday(idcard));

		/*
		int year = getYear(idcard);

		int month = getMonth(idcard);
		if (month < 1 || month > 12)
			throw new RuntimeException("month out of range : " + idcard);

		int day = getDay(idcard);
		if (day < 1 || day > dayOfMonth(year, month))
			throw new RuntimeException("day out of range : " + idcard);
		*/

		// verify age
		int age = getAge(idcard);
		if (age < 0 || age > 150)
			throw new RuntimeException("age out of range [0, 100] : " + idcard);
		// */
	}

	// GB/T2260 address.
	public static final Map<Integer, String> provinces = new HashMap<Integer, String>();
	static
	{
		provinces.put(Integer.valueOf(11), "北京市");
		provinces.put(Integer.valueOf(12), "天津市");
		provinces.put(Integer.valueOf(13), "河北省");
		provinces.put(Integer.valueOf(14), "山西省");
		provinces.put(Integer.valueOf(15), "内蒙古自治区");

		provinces.put(Integer.valueOf(21), "辽宁省");
		provinces.put(Integer.valueOf(22), "吉林省");
		provinces.put(Integer.valueOf(23), "黑龙江省");

		provinces.put(Integer.valueOf(31), "上海市");
		provinces.put(Integer.valueOf(32), "江苏省");
		provinces.put(Integer.valueOf(33), "浙江省");
		provinces.put(Integer.valueOf(34), "安徽省");
		provinces.put(Integer.valueOf(35), "福建省");
		provinces.put(Integer.valueOf(36), "江西省");
		provinces.put(Integer.valueOf(37), "山东省");

		provinces.put(Integer.valueOf(41), "河南省");
		provinces.put(Integer.valueOf(42), "湖北省");
		provinces.put(Integer.valueOf(43), "湖南省");
		provinces.put(Integer.valueOf(44), "广东省");
		provinces.put(Integer.valueOf(45), "广西壮族自治区");
		provinces.put(Integer.valueOf(46), "海南省");

		provinces.put(Integer.valueOf(50), "重庆市");
		provinces.put(Integer.valueOf(51), "四川省");
		provinces.put(Integer.valueOf(52), "贵州省");
		provinces.put(Integer.valueOf(53), "云南省");
		provinces.put(Integer.valueOf(54), "西藏自治区");

		provinces.put(Integer.valueOf(61), "陕西省");
		provinces.put(Integer.valueOf(62), "甘肃省");
		provinces.put(Integer.valueOf(63), "青海省");
		provinces.put(Integer.valueOf(64), "宁夏回族自治区");
		provinces.put(Integer.valueOf(65), "新疆维吾尔自治区");
	};

	public static void verifyAddress(String idcard)
	{
		int province = getProvince(idcard);
		if (provinces.get(Integer.valueOf(province)) == null)
			throw new RuntimeException("invalid province : " + idcard);
	}

	///////////////////////////////////////////////////////////////////////////////
	// TEST
	public static void test(int i, String idcard, String want)
	{
		try
		{
			String result = Idcard.to18(idcard);
			if (want != null && result.equals(want))
				return;

			System.out.println("test failed! i=" + i
				+ " idcard=" + idcard + " want=" + want + " result=" + result);
		}
		catch (Exception e)
		{
			if (want == null) return;

			System.out.println("test failed! i=" + i
				+ " idcard=" + idcard + " want=" + want + " exp=" + e);
		}
	}

	public static void main(String [] args) throws Exception
	{
		for (int i = 0; i < args.length; ++i)
		{
			Random r = new Random(System.currentTimeMillis());
			String sp = null;
			int idx = r.nextInt() % provinces.size();
			for (Integer p : provinces.keySet())
			{
				sp = p.toString();
				if (--idx < 0) break;
			}
			String idcard18 = Idcard.to18(sp + "0101" + args[i] + "010");
			System.out.println(idcard18 + " age=" + Idcard.getAge(idcard18));
		}


		// test
		String [] in = new String[] {
				"352202760110425", "35220219760110425x",
				"440524800101001", "440524198001010013",

				"440524188001010014", "440524188001010014",

				"11010519491231002x", "11010519491231002x",
				"11010519491231002X", "11010519491231002X",

				"35220219760110425X", "35220219760110425X",
				"35220219760110425x", "35220219760110425x",

				"352202760229425", "352202197602294251",

				"352202761310425", null,
				"352202761100425", null,
				"352202770229425", null, // not leap year
				"4405248001011", null,
				"993202760110425", null
			};

		for (int i = 0; i < in.length; i += 2)
			test(i, in[i], in[i+1]);
	}
}

