package com.mammoth.logger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerStats {
	private String encoding = "UTF-8";
	String file;
	public ServerStats(String file){
		this.file = file;
	}
	
	public void countTotal() throws Exception{
		int max = 0;
		int min = 999;
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file),encoding));
		int[] up1 = new int[24];
		int[] up2 = new int[24];
		int[] down1 = new int[24];
		int[] down2 = new int[24];
		int[] pp = new int[24];
		int[] count = new int[24];
		int[] res = new int[24];//流量
		Pattern p = Pattern.compile("接受(\\d+?)条\\[(\\d+?)k]/发送(\\d+?)条\\[(\\d+?)k]共(\\d+?)玩家");
		String line = "";
		Matcher m;
		int t = 0;
		while((line = reader.readLine())!= null){
			String strs[] = line.split("-", 4);
			int time  = Integer.parseInt(strs[0].split(":")[0]);
			m = null;
			m = p.matcher(line);
			if(m.find()){
				up1[time] += Integer.decode(m.group(1));
				up2[time] += Integer.decode(m.group(2));
				down1[time] += Integer.decode(m.group(3));
				down2[time] += Integer.decode(m.group(4));
				t = Integer.decode(m.group(5));
				pp[time] += t;
				count[time]++;
				if(t > max){
					max = t;
				}
				if(t < min){
					min = t;
				}
			}
		}
		reader.close();
		
		StringBuilder sb = new StringBuilder();
		sb.append("小时\t上传条数\t上传流量\t下载条数\t下载流量\t平均每人每小时流量\t平均每分钟在线人数\r\n");
		int tu1 = 0;
		int tu2 = 0;
		int du1 = 0;
		int du2 = 0;
		int dp = 0;//人数
		int dc = 0;
		int tmp = 0;
		int allres = 0;
		for(int i=0;i<24;i++){
			sb.append(i).append("\t");
			sb.append(up1[i]).append("\t");
			sb.append(up2[i]).append('\t');
			sb.append(down1[i]).append('\t');
			sb.append(down2[i]).append('\t');
			if(count[i] == 0){
				tmp = (pp[i]/60);
//				sb.append(pp[i]/60).append("\r\n");
			} else {
				tmp = pp[i]/count[i];
//				sb.append(pp[i]/count[i]).append("\r\n");
			}

			tu1 += up1[i];
			tu2 += up2[i];
			du1 += down1[i];
			du2 += down2[i];
			if(pp[i] != 0){
				dp += tmp;
				dc++;
			}
			if(tmp > 0){
				res[i] = (up2[i] + down2[i]) / tmp;
			}
			sb.append(res[i] + "\t");	
			allres += res[i];
			sb.append(tmp).append("\r\n");
		}
//		System.out.println(sb);
		sb.append("当日统计\r\n");
		sb.append("上传命令:" + tu1 + "条  总上传流量:" + (tu2/1024) + "M\r\n");
		sb.append("下载命令:" + tu1 + "条  总下载流量:" + (du2/1024) + "M\r\n");
		sb.append("平均每小时在线人数：" + (dp/dc) + "人  峰值:" + max + "  低谷:" + min + "\r\n");
		sb.append("平均每人每小时流量：" + (allres/dc) + "k");
		String outputfile = file.replace("res", "output");
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputfile),"GBK"));
		writer.print(sb);
		writer.close();
		System.out.println(file + "当日统计");
		System.out.println("上传命令:" + tu1 + "条  总上传流量:" + (tu2/ 1024) + "M");
		System.out.println("下载命令:" + tu1 + "条  总下载流量:" + (du2 / 1024) + "M");
		System.out.println("平均每小时在线人数：" + (dp/dc) + "人  峰值:" + max + "  低谷:" + min);
		System.out.println("平均每人每小时流量：" + (allres/dc) + "k");
	}
//	
//	public int[] parse(String line){
//		int[] result = new int[4];
//		String strs[] = line.split("-", 4);
//		String[] times = strs[0].split(":");
//		result[0] = Integer.parseInt(times[0]);
//		result[1] = Integer.parseInt(times[1]);
////		log.setMessage(strs[3]);//message
////		系统负担: 每分钟接受0条[0k]/发送0条[0k]共0玩家在线
//
//		Matcher matcher = p.matcher(line);
//		return result;
//	}
	public static void main(String[] args) throws Exception{
		String[] ins = {
				"0302.log",
				"0303.log",
				"0304.log",
				"0305.log",
				"0306.log",
				"0307.log",
				"0308.log",
				"0309.log",
				"0310.log",
		};
		for(int i=0;i<ins.length;i++){
			ServerStats ss = new ServerStats("res/" + ins[i]);
			ss.countTotal();
		}
	}

}
