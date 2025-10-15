package cn.com.zc.db.util;




public class DataConvert {
    public DataConvert() {
    }

    /**
   * print Hex byteArray
   * @param b byte[]
   */
    public static void PrintByteArray(byte[] b) {
        char[] hex =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                       'e', 'f' };

        for (int i = 0; i < b.length; i++) {

            if (i % 8 == 0)
                System.out.println("");
            System.out
            .print("0x" + hex[(b[i] >> 4 & 0x0f)] + hex[b[i] & 0x0f] + "; ");


        }

    }
    public static String getBytePrintString(byte[] b) {
        char[] hex =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                       'e', 'f' };
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < b.length; i++) {

            if (i % 8 == 0)
                sb.append("\n");
            sb.append("0x" + hex[(b[i] >> 4 & 0x0f)] + hex[b[i] & 0x0f] + " ");


        }
        return sb.toString();
    }


    public static String ByteArraytoHexString(byte[] b, int bLen) {
        int iLen = bLen;
        //ÿ��byte�������ַ���ܱ�ʾ�������ַ�ĳ��������鳤�ȵ�����
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = b[i];
            //�Ѹ���ת��Ϊ����
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            //С��0F������Ҫ��ǰ�油0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static String ByteArraytoHexString(byte[] b, int start, int length) {
        byte[] temp = new byte[length];
        int iLen = temp.length;
        //ÿ��byte�������ַ���ܱ�ʾ�������ַ�ĳ��������鳤�ȵ�����
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = start; i < (iLen + start); i++) {
            int intTmp = b[i];
            //�Ѹ���ת��Ϊ����
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            //С��0F������Ҫ��ǰ�油0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
    
    public static byte[] getSubByteArray(byte[] b,int start, int length){
    	byte[] temp = new byte[length];
    	//byte[]{1,2,f},2,1
        int iLen = temp.length,count = 0;//1
        for (int i = start; i < (iLen + start); i++) {
            temp[count] = b[i];
            count++;
        }
        return temp;
    }

    /**
   * Int (or) long to ByteArray
   * @param number long
   * @return byte[]
   */
    public static

    byte[] LongtoByteArray(long number) {
        long temp = number;
        StringBuffer s = new StringBuffer(Long.toString(temp, 16));
        if (s.length() % 2 == 1) {
            return StringToBytes("0" + s.toString());
        } else {
            return StringToBytes(s.toString());
        }

    }


    /**
   * ByteArray to int or long
   * @param b byte[]
   * @return int
   */
    public static int ByteArraytoInteger(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[i] > 0) {
                s = s + b[i];
            } else {
                s = s + 256 + b[i];
            }
            s = s * 256;
        }
        if (b[3] > 0) {
            s = s + b[3];
        } else {
            s = s + 256 + b[3];
        }
        return s;
    }



    public static long ByteArraytoLong(byte[] b) {
        long l = 0;
        for (int i = 0; i < b.length; i++) {
            l += (long)(b[b.length - i - 1]&0xff) << (8 * i);
        }

        return l;
    }
    public static long ByteArraytoLong(byte[] b,int start ,int len) {
        long l = 0;
        for (int i = 0; i < len; i++) {
            l += (long)(b[start+len - i - 1]&0xFF) << (8 * i);

        }

        return l;
    }
    /**
   * ByteArray to Double
   * @param b byte[]
   * @return double
   */
    public static double ByteArraytoDouble(byte[] b) {
        long l = 0;

        l = b[0];
        l = ((long)b[1] << 8);
        l = ((long)b[2] << 16);
        l = ((long)b[3] << 24);
        l = ((long)b[4] << 32);
        l = ((long)b[5] << 40);
        l = ((long)b[6] << 48);
        l = ((long)b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    public static final byte[] StringToBytes(String s) {
        int temp[] = new int[s.length()];
        byte b[] = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i++) {
            temp[i] = Integer.parseInt(String.valueOf(s.charAt(i)), 16);
        }
        int k = 0;
        for (int j = 0; j < s.length(); j += 2) {
            b[k] = (byte)(temp[j] * 16 + temp[j + 1]);
            k++;
        }

        return b;
    }
    /*
   Integer��Long�ṩ��toBinaryString, toHexString��toOctalString�� �������Է���Ľ����ת���ɶ����ơ�ʮ����ƺͰ˽����ַ����ܸ��ǿ�������toString(int/long i, int radix)���������Խ�һ��ʮ������ת����������Ƶ��ַ���ʽ��
   */

    public static String getRandom(int len, int radix) {
        StringBuffer buf = new StringBuffer("1");
        for (int i = 0; i < len; i++) {
            buf.append("0");
        }
        int div = Integer.parseInt(buf.toString());
        int value = (int)(Math.random() * div);
        if (radix == 10)
            return strPadding(value, len);
        else
            return hexStrPadding(Integer.toHexString(value), len);
    }

    /**
     *
     * @param in
     * @param outlen
     * @return
     */
    public static final String strPadding(long in, int outlen) {
        String str = String.valueOf(in);
        int padlen = outlen - str.length();

        StringBuffer zeroBuf = new StringBuffer("");
        for (int i = 0; i < padlen; i++) {
            zeroBuf.append("0");
        }

        return zeroBuf.append(str).toString();
    }

    public static final String hexStrPadding(String in, int outlen) {
        int padlen = outlen - in.length();

        StringBuffer zeroBuf = new StringBuffer("");
        for (int i = 0; i < padlen; i++) {
            zeroBuf.append("0");
        }

        return zeroBuf.append(in).toString();
    }
    
    public static final String hexStrAsciiPadding(String in, int outlen) {
        int padlen = outlen - in.length();

        StringBuffer zeroBuf = new StringBuffer("");
        for (int i = 0; i < padlen / 2; i++) {
            zeroBuf.append("30");
        }

        return zeroBuf.append(in).toString();
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for(int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF);
            if (stmp.length() == 1) {
                hs += "0" + stmp;
            } else {
                hs += stmp;
            }
        }
        return hs;
    }

    public static byte[] hex2byte(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    public static String decimal2Binary(String str){
        //parse��ת����valueof���෴
        return Integer.toBinaryString(Integer.parseInt(str));
    }

    public static String binary2Decimal(String str){
        //parse��ת����valueof���෴
        return String.valueOf(Integer.parseInt(str,2));
    }

    public static String decimal2Hex(String str){
        return Integer.toHexString(Integer.parseInt(str));
    }

    public static String hex2Decimal(String str){
        //parse��ת����valueof���෴
        return String.valueOf(Integer.parseInt(str,16));
    }

    public static String hex2Binary(String str){
        //parse��ת����valueof���෴
        String decimalStr = hex2Decimal(str);
        return decimal2Binary(decimalStr);
    }

    public static String binary2Hex(String str){
        //parse��ת����valueof���෴
        String binaryStr = binary2Decimal(str);
        return decimal2Hex(binaryStr);
    }
    
    /**
     * L : length  V: value
     * @param asciiStr : str which will be trans lv to normal
     * @param type : 1 -- ascii
     * @return : normal str
     */
    public static String parseLv(String asciiStr,int type){
        byte[] hexa = DataConvert.StringToBytes(asciiStr);
        StringBuffer sb = new StringBuffer("");
        if(type == 1){
            for(int i = 1;i< hexa.length;i++){
                sb.append((char)hexa[i]);
            }
        }
        return sb.toString();
    }
    
    public static byte[] paddedHex2byte(int i){
    	return DataConvert.hex2byte(DataConvert.
    			hexStrPadding(String.valueOf(i), 16));
    }
    
    /**
     * L : length  V: value
     * @param asciiStr : str which will be trans lv to normal
     * @param type : 1 -- ascii
     * @return : normal str
     */
    public static String transHex2Ascii(String asciiStr){
        byte[] hexa = DataConvert.StringToBytes(asciiStr);
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < hexa.length; i++) {
			sb.append((char) hexa[i]);
		}
		return sb.toString();
	}
    
    public static byte[] convertToBCD(String Address) throws Exception {
		int i = 0;
		int len = (Address.length() % 2 == 0) ? Address.length() / 2 : Address
				.length() / 2 + 1;
		byte tmp[] = new byte[len+1];
		// tmp[0] = (byte) Address.length();
		for (int j = 0; j < Address.length(); j++) {
			if (Address.charAt(j) < '0' || Address.charAt(0) > '9') {
				throw new Exception("�������ַ�!");
			}
		}
		tmp[0] = (byte) 0x81;
		for (; i < Address.length() / 2; i++) {
			tmp[i + 1] = (byte) (((Address.charAt(i * 2 + 1) - '0') << 4) | (Address
					.charAt(i * 2) - '0'));
		}
		if (Address.length() % 2 == 1) {
			tmp[i + 1] = (byte) (0xF0 | (Address.charAt(i * 2) - '0'));
			i++;
		}
		i++;
		return tmp;
	}


}
