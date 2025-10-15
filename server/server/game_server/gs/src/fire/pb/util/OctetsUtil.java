package fire.pb.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;


public class OctetsUtil
{
	public static Octets toOctets(int value)
	{
		OctetsStream oct = new OctetsStream();
		oct.marshal(value);
		return oct;
	}
	
	public static Octets toOctets(byte value)
	{
		OctetsStream oct = new OctetsStream();
		oct.marshal(value);
		return oct;
	}
	
	public static Octets toOctets(Marshal value)
	{
		return value.marshal(new OctetsStream());
	}
	
	public static Octets toOctets(String value)
	{
		OctetsStream oct = new OctetsStream();
		oct.marshal(value,"UTF-16LE");
		return oct;
	}
	
	public static String compactOctetString(String str)
	{
		char c0 = '0';
		StringBuilder sb = new StringBuilder();
		byte c0count = -1;
		for(char c : str.toCharArray())
		{
			if(c != c0)
			{
				if(c0count >= 0)
				{
					sb.append(String.format("%02X", c0count));
					c0count = -1;
				}
				sb.append(c);
			}
			else
			{
				c0count++;
				if(c0count >= 15)
				{
					sb.append(String.format("%02X", c0count));
					c0count = -1;
				}
			}
		}
		if(c0count >= 0)
			sb.append(String.format("%02X", c0count));
		return sb.toString();
	}
	public static String uncompactOctetString(String str)
	{
		char c0 = '0';
		StringBuilder sb = new StringBuilder();
		boolean isc0 = false;
		for(char c : str.toCharArray())
		{
			if(isc0)
			{
				byte c0count = HexChar2Byte(c);
				for(byte i = 0 ; i < c0count;i ++ )
					sb.append(c0);
				isc0 = false;
			}	
			else
			{
				sb.append(c);
				isc0 = (c == c0);
			}
		}
		return sb.toString();
	}
	
	public static byte HexChar2Byte(char c)
	{
		switch(c)
		{
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'A':
		case 'a':
			return 10;
		case 'B':
		case 'b':
			return 11;
		case 'C':
		case 'c':
			return 12;
		case 'D':
		case 'd':
			return 13;
		case 'E':
		case 'e':
			return 14;
		case 'F':
		case 'f':
			return 15;
		default:
			throw new RuntimeException("Unsupported HexChar : " + c);
		}
	}
	
	public static Octets compactedString2Octets(String str)
	{
		return string2Octets(uncompactOctetString(str));
	}
	
	public static Octets string2Octets(String str)
	{
		OctetsStream os = new OctetsStream();
		byte[] bytes = string2Bytes(str);
		return os.insert(0,bytes);
	}
	
	public static byte[] string2Bytes(String str)
	{
		if(str.length() % 2 != 0)
			throw new IllegalArgumentException("illegal octets string");
		byte[] bytes = new byte[str.length()/2];
		for(int i = 0 ; i < bytes.length; i++)
		{
			bytes[i] = (byte)Integer.parseInt(str.substring(2*i,2*i+2), 16);
		}
		return bytes;
	}
	
	public static String octets2CompactedString(Octets oct)
	{
		return compactOctetString(octets2String(oct));
	}
	public static String octets2String(Octets oct)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : oct.getBytes())
		{
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	
	public static Octets toOctets(long value)
	{
		OctetsStream oct = new OctetsStream();
		oct.marshal(value);
		return oct;
	}
	
	
	public static byte[] zip(byte[] data)
	{
		byte[] bytes = null;
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			zip.close();
			bytes = bos.toByteArray();
			bos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static void main(String[] args)
	{
		String str0 = "00000001000000000000006400000064000000000000000000000000000000000500006236000000050000000200000335000004100000008D0000145000000003000004CF0000003200000000000004D90000004400000000000004E3000000560000000000000009000088CA000088D8000088E5000088F1000088FF0000890B0000891800008925000089320000000A";
		String str1 = compactOctetString(str0);
		String str2 = uncompactOctetString(str1);
		System.out.println(str1);
		System.out.println(str2);
		System.out.println(str0.equals(str2));
	}
}
