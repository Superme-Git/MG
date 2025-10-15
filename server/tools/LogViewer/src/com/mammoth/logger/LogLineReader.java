package com.mammoth.logger;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.FileChannel.MapMode;
import java.util.*;

public class LogLineReader {
	private int lineperpage = 10000;
	
	public ArrayList<Position> lines = new ArrayList<Position>();
//	private ArrayList<Long> linePositions = new ArrayList<Long>();
	private String filename;
	private String encoding;
	private int lineCount;
	private FileChannel fc;
	private BufferedReader reader;
	
	public LogLineReader(){
	}
	
	public LogLineReader(String filename){
		this(filename,"UTF-8");
	}

	public LogLineReader(String filename,String encoding) {
		this.filename = filename;
		this.encoding = encoding;
	}
	
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	
	public void setFile(String filename){
		this.filename = filename;
	}

	public int getLines() {
		return lineCount;
	}

	public static final int MEM = 1024*1024*64;//一次读64M
	@SuppressWarnings("resource")
	public void parse() {
		try {
			fc = new FileInputStream(filename).getChannel();
			long size = fc.size();
			int row = 0;
			int preB = 0;
			int curB = 0;
			long lineStartIndex = 0;
			ByteBuffer byteBuffer;
			lines.clear();
			for(long l=0;l<size;l+=MEM){
				long len = MEM;
				if(l + len > size){
					len = size - l;
				}
				byteBuffer = fc.map(MapMode.READ_ONLY, l, len);
				while (byteBuffer.hasRemaining()) {
					curB = byteBuffer.get();
					if(curB == 0x0A){
						if(preB == 0x0D){
						}
						if(row % lineperpage == 0){
							lines.add(new Position(row,lineStartIndex));
						}
//						linePositions.add(Long.valueOf(lineStartIndex));
						lineStartIndex = l + byteBuffer.position();
						row++;
					}
					preB = curB;
				}
			}
			lineCount = row;
			lines.add(new Position(row,lineStartIndex));
//			fc.close();
//			lines = row;
//			linePositions.add(Long.valueOf(lineStartIndex));//加上最后一行
//			
//			byteBuffer = fc.map(MapMode.READ_ONLY, 0L, fc.size());
//			while (byteBuffer.hasRemaining()) {
//				curB = byteBuffer.get();
//				if(curB == 0x0A){
//					if(preB == 0x0D){
//					}
//					linePositions.add(Integer.valueOf(lineStartIndex));
//					lineStartIndex = byteBuffer.position();
//					row++;
//					System.out.println(row);
//				}
//				preB = curB;
//			}
//			lines = row;
//			linePositions.add(Integer.valueOf(lineStartIndex));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void mark(int start, int end) {
//		try {
//			long correntPosition = linePositions.get(start);
//			long nextPosion =  linePositions.get(end);
//			ByteBuffer bf = fc.map(
//					java.nio.channels.FileChannel.MapMode.READ_ONLY,
//					correntPosition, nextPosion - correntPosition);
//			int l = bf.limit();
//			byte msg[] = new byte[l];
//			for (int i = 0; i < msg.length; i++)
//				msg[i] = bf.get();
//
//			reader = new BufferedReader(new InputStreamReader(
//					new ByteArrayInputStream(msg),encoding));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public void mark2(long start,long end){
		try {
			ByteBuffer bf = fc.map(
					java.nio.channels.FileChannel.MapMode.READ_ONLY,
					start, end - start);
			int l = bf.limit();
			byte msg[] = new byte[l];
			for (int i = 0; i < msg.length; i++){
				msg[i] = bf.get();
			}
			reader = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(msg),encoding));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String readLine() {
		try{
			return reader.readLine();
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		LogLineReader reader = new LogLineReader("D:\\big.log");
		reader.parse();
	}

}
