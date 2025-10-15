package com.jmxservice.mt3interfaces;

import java.io.PrintStream;
import java.io.Serializable;

public class LogInfo
  implements Serializable
{
  private static final long serialVersionUID = 3364670799257474123L;
  public Integer userId = Integer.valueOf(0);
  public String userName = "";
  public String reason = "";

  public LogInfo()
  {
    this.userId = new Integer("0");
    this.userName = new String();
    this.reason = new String();
  }

  public LogInfo(Integer paramInteger, String paramString1, String paramString2)
  {
    this.userId = paramInteger;
    this.userName = paramString1;
    this.reason = paramString2;
  }

  public void dump()
  {
    dump(System.out);
  }

  public void dump(PrintStream paramPrintStream)
  {
    paramPrintStream.println("userId = " + this.userId);
    paramPrintStream.println("userName = " + this.userName);
    paramPrintStream.println("reason = " + this.reason);
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("LogInfo:");
    localStringBuilder.append(" userId=").append(this.userId);
    localStringBuilder.append(" userName=").append(this.userName);
    localStringBuilder.append(" reason=").append(this.reason);
    return localStringBuilder.toString();
  }
}