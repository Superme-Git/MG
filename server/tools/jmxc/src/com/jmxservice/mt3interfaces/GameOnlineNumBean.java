package com.jmxservice.mt3interfaces;

import java.io.PrintStream;
import java.io.Serializable;

public class GameOnlineNumBean
  implements Serializable
{
  private static final long serialVersionUID = 7625085084251392283L;
  public Integer curronlinenum;
  public Integer zero;
  public Integer ten;
  public Integer twenty;
  public Integer thirty;
  public Integer forty;
  public Integer fifty;
  public Integer sixty;
  public Integer seventy;
  public Integer eighty;
  public Integer ninty;
  public Integer hundred;
  public Integer hundredten;
  public Integer hundredtwenty;
  public Integer hundredthirty;
  public Integer hundredforty;
  public Integer hundredfifty;

  public GameOnlineNumBean()
  {
    this.curronlinenum = new Integer("0");
    this.zero = new Integer("0");
    this.ten = new Integer("0");
    this.twenty = new Integer("0");
    this.thirty = new Integer("0");
    this.forty = new Integer("0");
    this.fifty = new Integer("0");
    this.sixty = new Integer("0");
    this.seventy = new Integer("0");
    this.eighty = new Integer("0");
    this.ninty = new Integer("0");
    this.hundred = new Integer("0");
    this.hundredten = new Integer("0");
    this.hundredtwenty = new Integer("0");
    this.hundredthirty = new Integer("0");
    this.hundredforty = new Integer("0");
    this.hundredfifty = new Integer("0");
  }

  public GameOnlineNumBean(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, Integer paramInteger4, Integer paramInteger5, Integer paramInteger6, Integer paramInteger7, Integer paramInteger8, Integer paramInteger9, Integer paramInteger10, Integer paramInteger11, Integer paramInteger12, Integer paramInteger13, Integer paramInteger14, Integer paramInteger15, Integer paramInteger16, Integer paramInteger17)
  {
    this.curronlinenum = paramInteger1;
    this.zero = paramInteger2;
    this.ten = paramInteger3;
    this.twenty = paramInteger4;
    this.thirty = paramInteger5;
    this.forty = paramInteger6;
    this.fifty = paramInteger7;
    this.sixty = paramInteger8;
    this.seventy = paramInteger9;
    this.eighty = paramInteger10;
    this.ninty = paramInteger11;
    this.hundred = paramInteger12;
    this.hundredten = paramInteger13;
    this.hundredtwenty = paramInteger14;
    this.hundredthirty = paramInteger15;
    this.hundredforty = paramInteger16;
    this.hundredfifty = paramInteger17;
  }

  public void dump()
  {
    dump(System.out);
  }

  public void dump(PrintStream paramPrintStream)
  {
    paramPrintStream.println("curronlinenum = " + this.curronlinenum);
    paramPrintStream.println("zero = " + this.zero);
    paramPrintStream.println("ten = " + this.ten);
    paramPrintStream.println("twenty = " + this.twenty);
    paramPrintStream.println("thirty = " + this.thirty);
    paramPrintStream.println("forty = " + this.forty);
    paramPrintStream.println("fifty = " + this.fifty);
    paramPrintStream.println("sixty = " + this.sixty);
    paramPrintStream.println("seventy = " + this.seventy);
    paramPrintStream.println("eighty = " + this.eighty);
    paramPrintStream.println("ninty = " + this.ninty);
    paramPrintStream.println("hundred = " + this.hundred);
    paramPrintStream.println("hundredten = " + this.hundredten);
    paramPrintStream.println("hundredtwenty = " + this.hundredtwenty);
    paramPrintStream.println("hundredthirty = " + this.hundredthirty);
    paramPrintStream.println("hundredforty = " + this.hundredforty);
    paramPrintStream.println("hundredfifty = " + this.hundredfifty);
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GameOnlineNumBean:");
    localStringBuilder.append(" curronlinenum=").append(this.curronlinenum);
    localStringBuilder.append(" zero=").append(this.zero);
    localStringBuilder.append(" ten=").append(this.ten);
    localStringBuilder.append(" twenty=").append(this.twenty);
    localStringBuilder.append(" thirty=").append(this.thirty);
    localStringBuilder.append(" forty=").append(this.forty);
    localStringBuilder.append(" fifty=").append(this.fifty);
    localStringBuilder.append(" sixty=").append(this.sixty);
    localStringBuilder.append(" seventy=").append(this.seventy);
    localStringBuilder.append(" eighty=").append(this.eighty);
    localStringBuilder.append(" ninty=").append(this.ninty);
    localStringBuilder.append(" hundred=").append(this.hundred);
    localStringBuilder.append(" hundredten=").append(this.hundredten);
    localStringBuilder.append(" hundredtwenty=").append(this.hundredtwenty);
    localStringBuilder.append(" hundredthirty=").append(this.hundredthirty);
    localStringBuilder.append(" hundredforty=").append(this.hundredforty);
    localStringBuilder.append(" hundredfifty=").append(this.hundredfifty);
    return localStringBuilder.toString();
  }
}