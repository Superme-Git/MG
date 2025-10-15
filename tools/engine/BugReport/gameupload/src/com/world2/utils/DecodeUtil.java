package com.world2.utils;

import java.lang.Character.UnicodeBlock;

public class DecodeUtil {
	private static final int MASKBITS = 0x3F;
	 private static final int MASKBYTE = 0x80;
	 private static final int MASK2BYTES = 0xC0;
	 private static final int MASK3BYTES = 0xE0;
	 //private static final int MASK4BYTES = 0xF0;
	 //private static final int MASK5BYTES = 0xF8;
	 //private static final int MASK6BYTES = 0xFC;
	/** *//**
	 * @����: ��UNICODE��UTF-16LE������ת��UTF-8����
	 * @����: byte[] b Դ�ֽ�����
	 * @����ֵ: byte[] b תΪUTF-8����������
	 * @����: imuse
	 * @MAIL: postzhu@hotmail.com
	 */
	public static byte[] UNICODE_TO_UTF8(byte[] b)
	{
		int i = 0;
	    int j = 0;
	    byte[] utf8Byte = new byte[b.length * 2];
	    while (i < b.length)
		{
	        byte[] bUTF = new byte[1];
	        int nCode = (b[i] & 0xFF) | ((b[i + 1] & 0xFF) << 8);
	        if (nCode < 0x80)
			{
	            bUTF = new byte[1];
	            bUTF[0] = (byte) nCode;
	        }
	        // 110xxxxx 10xxxxxx
	        else if (nCode < 0x800)
			{
	            bUTF = new byte[2];
	            bUTF[0] = (byte) (MASK2BYTES | nCode >> 6);
	            bUTF[1] = (byte) (MASKBYTE | nCode & MASKBITS);
	        }
	        // 1110xxxx 10xxxxxx 10xxxxxx
	        else if (nCode < 0x10000)
			{
	            bUTF = new byte[3];
	            bUTF[0] = (byte) (MASK3BYTES | nCode >> 12);
	            bUTF[1] = (byte) (MASKBYTE | nCode >> 6 & MASKBITS);
	            bUTF[2] = (byte) (MASKBYTE | nCode & MASKBITS);
	        }
	        for (int k = 0; k < bUTF.length; k++)
			{
	            utf8Byte[j++] = bUTF[k];
	        }
	        i += 2;
	    }
	    b = new byte[j];
	    System.arraycopy(utf8Byte, 0, b, 0, j);
	    return b;
	}

	/** *//**
	 * @����: ��һ������Ϊ2 byte����תΪshort
	 * @����: byte[] bytesShortҪת���ֽ�����
	 * @����ֵ: short sRet ת���shortֵ
	 */
	public static short bytesToShort(byte[] bytesShort)
	{
	    short sRet = 0;
	    sRet += (bytesShort[0] & 0xFF) << 8;
	    sRet += bytesShort[1] & 0xFF;
	    return sRet;
	}

	/** *//**
	 * @����: ��һ��shortֵתΪbyte����
	 * @����: short sNum    Ҫת��shortֵ
	 * @����ֵ: byte[] bytesRet ת���byte����
	 */
	public static byte[] shortToBytes(short sNum)
	{
	    byte[] bytesRet = new byte[2];
	    bytesRet[0] = (byte) ((sNum >> 8) & 0xFF);
	    bytesRet[1] = (byte) (sNum & 0xFF);
	    return bytesRet;
	}

	/** *//** 
	 * @����: ��һ������Ϊ4 byte����תΪint
	 * @����: byte[] bNumҪת���ֽ�����
	 * @����ֵ: int retInt ת���intֵ
	 */
	public static int bytesToInt(byte[] bNum)
	{
	    int retInt = 0;
	    retInt = ((bNum[0] & 0xFF) << 24);
	    retInt += (bNum[1] & 0xFF) << 16;
	    retInt += (bNum[2] & 0xFF) << 8;
	    retInt += bNum[3] & 0xFF;
	    return retInt;
	}

	/** *//** 
	* @����: ��һ��intֵתΪbyte���� 
	* @����: int nNum        Ҫת��intֵ 
	* @����ֵ: byte[] bytesRet ת���byte���� 
	*/
	public static byte[] intToBytes(int nNum)
	{ 
		byte[] bytesRet = new byte[4];  
	    bytesRet[0] = (byte) ((nNum >> 24) & 0xFF); 
	    bytesRet[1] = (byte) ((nNum >> 16) & 0xFF);
	    bytesRet[2] = (byte) ((nNum >> 8) & 0xFF); 
	    bytesRet[3] = (byte) (nNum & 0xFF); 
	    return bytesRet;
	}
	
}
