package fire.pb.callback;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
/**
 * @author dc
 * 字节消息
 */
public class Message {

	public Message(short iMessageNum) {
		this.iMessageNum = iMessageNum;
		body = new byte[1024];
	}
	
	public Message(ByteBuffer buff) {
		this.body = buff.array();
	}
	
	public void readHeader() throws ArrayIndexOutOfBoundsException {
		if(body.length >= 2) {
			iMessageNum = BitConverter.hostBytesToShort(body);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
		if(body.length >= 4) {
			iMessageLen = BitConverter.hostBytesToShort(body, 2);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public short readShort() throws ArrayIndexOutOfBoundsException {
		if(body.length >= iPosition + 2) {
			short iValue = BitConverter.hostBytesToShort(body, iPosition);
			iPosition += 2;
			return iValue;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public int readInt() throws ArrayIndexOutOfBoundsException {
		if(body.length >= iPosition + 4) {
			int iValue = BitConverter.hostBytesToInt(body, iPosition);
			iPosition += 4;
			return iValue;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public void writeShort(short iValue)  throws ArrayIndexOutOfBoundsException {
		if(body.length >= iPosition + 2) {
			byte[] temp = BitConverter.shortToHostBytes(iValue);
			System.arraycopy(temp, 0, body, iPosition, 2);
			iPosition += 2;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public void writeInt(int iValue)  throws ArrayIndexOutOfBoundsException {
		if(body.length >= iPosition + 4) {
			byte[] temp = BitConverter.intToHostBytes(iValue);
			System.arraycopy(temp, 0, body, iPosition, 4);
			iPosition += 4;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public String readString() throws ArrayIndexOutOfBoundsException {
		if(body.length >= iPosition + 2) {
			short iLength = BitConverter.hostBytesToShort(body, iPosition);
			if(body.length >= iPosition + 2 + iLength) {
				byte[] temp = new byte[iLength];
				System.arraycopy(body, iPosition + 2, temp, 0, iLength);
				String strValue = "";
				try {
					strValue = new String(temp, "UTF-8");
				} catch (UnsupportedEncodingException e) {
				}
				iPosition += (2 + iLength);
				return strValue;
			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public void writeString(String strValue)  throws ArrayIndexOutOfBoundsException {
		try {
			byte[] buf = strValue.getBytes("UTF-8");
			short iLength = (short)buf.length;
			if(body.length >= iPosition + 2 + iLength) {
				byte[] temp = BitConverter.shortToHostBytes(iLength);
				System.arraycopy(temp, 0, body, iPosition, 2);
				System.arraycopy(buf, 0, body, iPosition + 2, iLength);
				iPosition += (2 + iLength);
			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
			
		} catch (UnsupportedEncodingException e) {
			
		}
	}
	
	public ByteBuffer toSend() {
		byte[] temp = BitConverter.shortToHostBytes(iMessageNum);
		System.arraycopy(temp, 0, body, 0, 2);
		iMessageLen = (short)(iPosition - 4);
		temp = BitConverter.shortToHostBytes(iMessageLen);
		System.arraycopy(temp, 0, body, 2, 2);
		
		temp = new byte[4 + iMessageLen];
		System.arraycopy(body, 0, temp, 0, 4 + iMessageLen);
		return ByteBuffer.wrap(temp);
	}
	
	public short getMessageNum() {
		return iMessageNum;
	}
	
	public void setMessageNum(short iMessageNum) {
		this.iMessageNum = iMessageNum;
	}
	
	public short getMessageLen() {
		return iMessageLen;
	}
	
	public void setMessageLen(short iMessageLen) {
		this.iMessageLen = iMessageLen;
	}
	
	private byte[] body;
	private short iMessageNum = 0;
	private short iMessageLen = 0;
	private int iPosition = 4;
}
