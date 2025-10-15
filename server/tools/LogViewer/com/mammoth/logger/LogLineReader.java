// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) ansi 
// Source File Name:   LogLineReader.java

package com.mammoth.logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

// Referenced classes of package com.mammoth.logger:
//            Position

public class LogLineReader
{

    public LogLineReader()
    {
        lineperpage = 10000;
        lines = new ArrayList();
    }

    public LogLineReader(String filename)
    {
        this(filename, "UTF-8");
    }

    public LogLineReader(String filename, String encoding)
    {
        lineperpage = 10000;
        lines = new ArrayList();
        this.filename = filename;
        this.encoding = encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public void setFile(String filename)
    {
        this.filename = filename;
    }

    public int getLines()
    {
        return lineCount;
    }

    public void parse()
    {
        try
        {
            fc = (new FileInputStream(filename)).getChannel();
            long size = fc.size();
            int row = 0;
            int preB = 0;
            int curB = 0;
            long lineStartIndex = 0L;
            lines.clear();
            for(long l = 0L; l < size; l += 0x4000000L)
            {
                long len = 0x4000000L;
                if(l + len > size)
                    len = size - l;
                for(ByteBuffer byteBuffer = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, l, len); byteBuffer.hasRemaining();)
                {
                    curB = byteBuffer.get();
                    if(curB == 10)
                    {
                        if(row % lineperpage == 0)
                            lines.add(new Position(row, lineStartIndex));
                        lineStartIndex = l + (long)byteBuffer.position();
                        row++;
                    }
                    preB = curB;
                }

            }

            lineCount = row;
            lines.add(new Position(row, lineStartIndex));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void mark2(long start, long end)
    {
        try
        {
            ByteBuffer bf = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, start, end - start);
            int l = bf.limit();
            byte msg[] = new byte[l];
            for(int i = 0; i < msg.length; i++)
                msg[i] = bf.get();

            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(msg), encoding));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String readLine()
    {
        try
        {
            return reader.readLine();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String args[])
    {
        LogLineReader reader = new LogLineReader("D:\\big.log");
        reader.parse();
    }

    private int lineperpage;
    public ArrayList lines;
    private String filename;
    private String encoding;
    private int lineCount;
    private FileChannel fc;
    private BufferedReader reader;
    public static final int MEM = 0x4000000;
}
