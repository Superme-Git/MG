package fire.script; 

import java.util.HashMap;
import java.util.Map;
import fire.pb.battle.Fighter;

public class JsFunManager
{
	static Map<String, Integer> funMap = new HashMap<String, Integer>();
	public JsFunManager(){InitFunMap();}
	static public float randfloat(int t, int t1) { return (float)(t1 > t ? Math.random()*(t1-t) + t : Math.random()*(t-t1) + t1) ; }
	static public int   randint(int t,int t1) {return (int)(t1 > t ? Math.round(Math.random()*(t1-t)) + t : Math.round(Math.random()*(t-t1)) + t1) ;}
	static public int GetFunID(String fun)	{  if(funMap.size() < 1) return -1;if(funMap.get(fun)!= null) return funMap.get(fun); else return -1; }
	static public void InitFunMap()
	{
		funMap.put("with(Math){ return ((random()*(3-2))+2);}",176);
		funMap.put("with(Math){ return (1000*RoleLv*0.4)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",304);
		funMap.put("with(Math){ return -2*skilllevela;}",55);
		funMap.put("with(Math){ return -(phyattacka*0.7-defendb+1*skilllevela);}",133);
		funMap.put("with(Math){ return (1000*RoleLv*0.1*(0.79+0.02*AnswerCnt))*0.5*(random()*(1.02-0.98)+0.98);}",280);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*0.5;}",241);
		funMap.put("with(Math){ return 3*gradea;}",165);
		funMap.put("with(Math){ return _506109_;}",716);
		funMap.put("with(Math){ return floor((min(max(floor((ServerLv-50)*0.2),0),3)*2+3)*min(max(rolenum*0.0005,1),2));}",463);
		funMap.put("with(Math){ return 400*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.35*(0.5+0.1*Saveid)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",371);
		funMap.put("with(Math){ return _96101_==1;}",611);
		funMap.put("with(Math){ return SwXs*1;}",435);
		funMap.put("with(Math){ return _95073_==1;}",570);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,59)/10)*440+(200+floor(min(RoleLv,59)/10)*40*10))*2;}",424);
		funMap.put("with(Math){ return (1000*RoleLv*0.232*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",282);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+7*IsDbPoint+IsSerMul)*(StdExp*10/63*((Ring-1)*0.05+1));}",368);
		funMap.put("with(Math){ return _95020_==1;}",537);
		funMap.put("with(Math){ return _95043_>=3;}",550);
		funMap.put("with(Math){ return 2.5*RoleLv*(random()*(1.02-0.98)+0.98);}",337);
		funMap.put("with(Math){ return (400*MonsterLv*0.017*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*(1-IsDbPoint)+400*MonsterLv*0.13*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*IsDbPoint)*(random()*(1.02-0.98)+0.98);}",387);
		funMap.put("with(Math){ return 0.5+0.001*(skilllevela-gradeb);}",59);
		funMap.put("with(Math){ return _94024_>=1;}",467);
		funMap.put("with(Math){ return 10+2.4*gradea;}",217);
		funMap.put("with(Math){ return -(phyattacka*1.6-min(defendb,magicdefb)+1*skilllevela);}",199);
		funMap.put("with(Math){ return -(phyattacka*1-defendb+1*gradea)*(0.45+0.05*skilllevela);}",39);
		funMap.put("with(Math){ return 400*RoleLv*0.656*(0.82+0.04*((Ring-1)%8+1))*(0.58+0.04*(floor((Ring-1)/8)+1));}",393);
		funMap.put("with(Math){ return -(phyattacka*0.95-defendb+1*skilllevela+max(0,speeda-speedb)*0.5);}",209);
		funMap.put("with(Math){ return skilllevela*1;}",138);
		funMap.put("with(Math){ return survivala<survivalb;}",69);
		funMap.put("with(Math){ return _95001_>=1;}",529);
		funMap.put("with(Math){ return 9.5*RoleLv;}",294);
		funMap.put("with(Math){ return _96103_>=1;}",603);
		funMap.put("with(Math){ return _94044_>=1&&_94057_<1;}",519);
		funMap.put("with(Math){ return -(phyattacka*1.2-defendb+1*skilllevela);}",122);
		funMap.put("with(Math){ return _96401_==1;}",645);
		funMap.put("with(Math){ return maxhpa*0.08;}",224);
		funMap.put("with(Math){ return _96351_>=1&&_96359_<1;}",636);
		funMap.put("with(Math){ return (1+IsSerMul)*(StdExp*5);}",350);
		funMap.put("with(Math){ return (1*RoleLv*8)*(random()*(1.02-0.98)+0.98);}",326);
		funMap.put("with(Math){ return _94030_==2;}",517);
		funMap.put("with(Math){ return 0*TeamNum+5;}",732);
		funMap.put("with(Math){ return -(magicattacka*0.9-magicdefb+3*skilllevela);}",134);
		funMap.put("with(Math){ return _99001_<1&&_99012_>=1;}",681);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),4);}",94);
		funMap.put("with(Math){ return _99001_<1&&_99004_==1;}",673);
		funMap.put("with(Math){ return skilllevela>=60;}",52);
		funMap.put("with(Math){ return (400*RoleLv*1.099*0.6);}",401);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*(0.5+0.05*(3-preaimcount))*1.5;}",203);
		funMap.put("with(Math){ return 3*skilllevela;}",50);
		funMap.put("with(Math){ return 5000*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.058*(0.5+0.1*Saveid)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",453);
		funMap.put("with(Math){ return 1*RoleLv*0.667*2*(3.5+0.5*(floor((Time-1)/5)+1))*floor(1-(Time%5)*0.2)*(random()*(1.02-0.98)+0.98);}",333);
		funMap.put("with(Math){ return -(phyattacka*1.05-min(defendb,magicdefb)+1*skilllevela);}",195);
		funMap.put("with(Math){ return 4*skilllevela;}",221);
		funMap.put("with(Math){ return (400*RoleLv*1.099*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",342);
		funMap.put("with(Math){ return quality*3*(havebuffa(508042)?(1.2):(1));}",10);
		funMap.put("with(Math){ return _96202_>=4;}",654);
		funMap.put("with(Math){ return (1000*MonsterLv*0.1)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",253);
		funMap.put("with(Math){ return _96352_>=1;}",633);
		funMap.put("with(Math){ return _94016_>=1;}",496);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))+(1-curhpa/maxhpa)*0.1;}",216);
		funMap.put("with(Math){ return (400*RoleLv*0.628*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",355);
		funMap.put("with(Math){ return 2200+floor(RoleLv/10)*440+(200+floor(RoleLv/10)*40*(Ring-1));}",423);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),3);}",93);
		funMap.put("with(Math){ return -(phyattacka*1.05-defendb+1*skilllevela+max(0,speeda-speedb)*0.5);}",208);
		funMap.put("with(Math){ return _95037_>=2||_95040_>=1;}",547);
		funMap.put("with(Math){ return _95048_>=1;}",555);
		funMap.put("with(Math){ return 200*RoleLv;}",272);
		funMap.put("with(Math){ return RoleLv*100;}",407);
		funMap.put("with(Math){ return (400*RoleLv*0.5*(0.79+0.02*AnswerCnt))*0.5*(random()*(1.02-0.98)+0.98);}",411);
		funMap.put("with(Math){ return 10+2.4*skilllevela;}",100);
		funMap.put("with(Math){ return 400*RoleLv*0.875*(0.91+0.02*((Ring-1)%8+1))*(0.58+0.04*(floor((Ring-1)/8)+1));}",344);
		funMap.put("with(Math){ return (400*min(max(RoleLv,FuBenLv),FuBenLv+9)*1*(0.7+0.1*Ring))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",402);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*0.6;}",240);
		funMap.put("with(Math){ return _94029_>=1;}",468);
		funMap.put("with(Math){ return skilllevela>=50;}",64);
		funMap.put("with(Math){ return (1000*RoleLv*0.1)*(random()*(1.02-0.98)+0.98)*0.5;}",290);
		funMap.put("with(Math){ return 400*RoleLv*1.215*(0.85+0.03*((Time-1)%9+1))*(0.9+0.1*(floor((Time-1)/9)+1))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",375);
		funMap.put("with(Math){ return (1000*RoleLv*0.215*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",262);
		funMap.put("with(Math){ return -quality*3;}",13);
		funMap.put("with(Math){ return (400*RoleLv*0.678*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",357);
		funMap.put("with(Math){ return (1000*RoleLv*0.2)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",269);
		funMap.put("with(Math){ return maxmpb*0.15+250;}",182);
		funMap.put("with(Math){ return -(phyattacka*1-min(defendb,magicdefb)+1*skilllevela);}",198);
		funMap.put("with(Math){ return _94025_>=1&&_94030_>=3;}",512);
		funMap.put("with(Math){ return _96015_==1;}",598);
		funMap.put("with(Math){ return 1000*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.35*(0.75+0.05*Saveid)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",261);
		funMap.put("with(Math){ return maindamage;}",63);
		funMap.put("with(Math){ return 400*min(RoleLv,99)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",416);
		funMap.put("with(Math){ return _99003_>=1&&_96124_<=0.01;}",674);
		funMap.put("with(Math){ return _94030_==1;}",518);
		funMap.put("with(Math){ return _96207_>=4;}",659);
		funMap.put("with(Math){ return 1*TeamNum+4;}",729);
		funMap.put("with(Math){ return -curhpb*0.8;}",156);
		funMap.put("with(Math){ return -(magicattacka*2.2-magicdefb+2*gradea)*0.5;}",166);
		funMap.put("with(Math){ return 0*TeamNum+4;}",730);
		funMap.put("with(Math){ return _94005_>=1;}",488);
		funMap.put("with(Math){ return quality*12+150;}",18);
		funMap.put("with(Math){ return (1*RoleLv*1.256*2*2)*(random()*(1.02-0.98)+0.98);}",307);
		funMap.put("with(Math){ return (1*RoleLv*1.256*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",313);
		funMap.put("with(Math){ return _94007_>=2&&_94011_>=1;}",490);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))*0.4;}",153);
		funMap.put("with(Math){ return _97005_==1;}",651);
		funMap.put("with(Math){ return _94008_>=1;}",492);
		funMap.put("with(Math){ return 400*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.35*(0.75+0.05*Saveid)*(random()*(1.02-0.98)+0.98);}",377);
		funMap.put("with(Math){ return (1+IsSerMul)*(StdExp*13.333/28.867*((Ring-1)*0.08+1)+RoleLv*50-1000);}",386);
		funMap.put("with(Math){ return _95030_>=1;}",542);
		funMap.put("with(Math){ return min(-2.4*gradea+1.2*skilllevela,0);}",33);
		funMap.put("with(Math){ return -curhpb*0.05;}",143);
		funMap.put("with(Math){ return maxhpb*0.5;}",152);
		funMap.put("with(Math){ return _96216_<=3;}",667);
		funMap.put("with(Math){ return _503002_;}",710);
		funMap.put("with(Math){ return _95017_==1;}",535);
		funMap.put("with(Math){ return _95051_>=2;}",558);
		funMap.put("with(Math){ return -((0.15+0.001*skilllevela)*defendb+1*skilllevela);}",38);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),5);}",95);
		funMap.put("with(Math){ return _96301_>=1&&_96309_<1;}",623);
		funMap.put("with(Math){ return 1.4*skilllevela;}",73);
		funMap.put("with(Math){ return -curhpb;}",74);
		funMap.put("with(Math){ return 2*skilllevela;}",72);
		funMap.put("with(Math){ return _99032_>=1;}",676);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela);}",239);
		funMap.put("with(Math){ return _502003_||_506002_;}",701);
		funMap.put("with(Math){ return 2.22*RoleLv*(random()*(1.02-0.98)+0.98);}",336);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*0.25;}",105);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*(0.55+0.05*(3-preaimcount));}",110);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*(0.5+0.05*(3-preaimcount))*(havebuffa(506109)?((random()*(1.20-1.1))+1.1):((random()*(1.20-0.9))+0.9));}",114);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,49)/10)*440+(200+floor(min(RoleLv,49)/10)*40*10))*2;}",430);
		funMap.put("with(Math){ return !_506003_;}",699);
		funMap.put("with(Math){ return maxhpa*1;}",149);
		funMap.put("with(Math){ return _96304_<1;}",631);
		funMap.put("with(Math){ return -(magicattacka*2.5-magicdefb+2*skilllevela)*0.5*(havebuffa(506109)?((random()*(1.20-1.1))+1.1):((random()*(1.20-0.9))+0.9));}",115);
		funMap.put("with(Math){ return min(maxhpb*0.15,gradeb*12);}",188);
		funMap.put("with(Math){ return _94043_>=1;}",527);
		funMap.put("with(Math){ return _96351_>=1&&_96364_<1;}",641);
		funMap.put("with(Math){ return _94035_>=1;}",473);
		funMap.put("with(Math){ return -0.7*skilllevela;}",228);
		funMap.put("with(Math){ return _96111_>=1;}",610);
		funMap.put("with(Math){ return _95077_>=2;}",579);
		funMap.put("with(Math){ return StdExp*7*2.86/168*8;}",399);
		funMap.put("with(Math){ return (StdMoney*2/15)*(random()*(1.05-0.95)+0.95);}",273);
		funMap.put("with(Math){ return -(max(phyattacka*1-defendb+1*gradea,phyattacka*0.1)+(havebuffa(509300)?(min(max(defendb-phyattackb,((pve)?(2*gradea):(10))),4*gradea)):(0)));}",0);
		funMap.put("with(Math){ return -0.05+0.1*skilllevela;}",131);
		funMap.put("with(Math){ return 400*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.35*(0.75+0.05*Saveid)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",414);
		funMap.put("with(Math){ return _95009_==1;}",531);
		funMap.put("with(Math){ return !_509097_;}",700);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))*0.5;}",83);
		funMap.put("with(Math){ return _99001_<1&&_99010_>=1;}",679);
		funMap.put("with(Math){ return _94013_>=1;}",495);
		funMap.put("with(Math){ return (1000*TeamLv*0.075*(1-IsDbPoint)+1000*TeamLv*0.12*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",250);
		funMap.put("with(Math){ return (1000*RoleLv*0.1*(0.79+0.02*AnswerCnt))*(random()*(1.02-0.98)+0.98);}",297);
		funMap.put("with(Math){ return 0.5+0.001*(skilllevela-gradeb)+(enhanceseala-resistsealb);}",218);
		funMap.put("with(Math){ return (1*MonsterLv*0.3)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",320);
		funMap.put("with(Math){ return _94044_>=1&&_94057_>=1;}",520);
		funMap.put("with(Math){ return (1*RoleLv*0.775*2)*(random()*(1.02-0.98)+0.98);}",315);
		funMap.put("with(Math){ return 10+2*skilllevela;}",82);
		funMap.put("with(Math){ return min(maxhpb*0.25,gradeb*12);}",187);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,79)/10)*440+(200+floor(min(RoleLv,79)/10)*40*10))*2;}",434);
		funMap.put("with(Math){ return (400*RoleLv*2.5)*(random()*(1.02-0.98)+0.98);}",343);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*gradea)*0.5;}",127);
		funMap.put("with(Math){ return _94037_>=1;}",521);
		funMap.put("with(Math){ return _502002_&&!_502003_;}",690);
		funMap.put("with(Math){ return _96218_>=1;}",669);
		funMap.put("with(Math){ return (400*MonsterLv*0.011*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*(1-IsDbPoint)+400*MonsterLv*0.13*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*IsDbPoint)*(random()*(1.02-0.98)+0.98);}",376);
		funMap.put("with(Math){ return _94046_<1&&_94045_>=3;}",471);
		funMap.put("with(Math){ return _96108_>=1;}",604);
		funMap.put("with(Math){ return magicattacka*0.1;}",225);
		funMap.put("with(Math){ return 2.1*skilllevela;}",118);
		funMap.put("with(Math){ return 1*skilllevela;}",46);
		funMap.put("with(Math){ return _96001_/_96002_<0.2;}",594);
		funMap.put("with(Math){ return StdMoney*0.4*((Ring-1)*0.09+1);}",305);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*6.7);}",380);
		funMap.put("with(Math){ return _96351_>=1&&_96363_<1;}",640);
		funMap.put("with(Math){ return 0*TeamNum+2;}",736);
		funMap.put("with(Math){ return -(magicattacka*2-magicdefb+2*skilllevela)*0.5;}",84);
		funMap.put("with(Math){ return quality*5+50;}",19);
		funMap.put("with(Math){ return 100*(havebuffa(508042)?(1.2):(1));}",12);
		funMap.put("with(Math){ return skilllevela>=40;}",25);
		funMap.put("with(Math){ return _120_;}",696);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,69)/10)*440+(200+floor(min(RoleLv,69)/10)*40*10))*2;}",426);
		funMap.put("with(Math){ return 0.15+0.1*skilllevela;}",113);
		funMap.put("with(Math){ return _94021_>=1;}",509);
		funMap.put("with(Math){ return StdExp*5/10*(random()*(1.2-0.8)+0.8);}",404);
		funMap.put("with(Math){ return !_510139_;}",718);
		funMap.put("with(Math){ return 0.6*skilllevela;}",200);
		funMap.put("with(Math){ return (5000*MonsterLv*0.05)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",445);
		funMap.put("with(Math){ return _96301_>=1&&_96366_<1;}",630);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*gradea)*0.5*(1.2+(0.6+0.2*skilllevela)*(1-curmpb/maxmpb));}",109);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,89)/10)*440+(200+floor(min(RoleLv,89)/10)*40*10))*2;}",433);
		funMap.put("with(Math){ return _96004_>=4;}",595);
		funMap.put("with(Math){ return (1*RoleLv*0.717*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",329);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*(0.55+0.05*(4-preaimcount));}",219);
		funMap.put("with(Math){ return 400*min(RoleLv,94)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",398);
		funMap.put("with(Math){ return max((magicattacka*2.5-magicdefb+2*skilllevela)*0.5*(havebuffa(506109)?((random()*(1.20-1.1))+1.1):((random()*(1.20-0.9))+0.9)),3*skilllevela);}",227);
		funMap.put("with(Math){ return round((pow(1.02,skilllevela)-1)*1000);}",245);
		funMap.put("with(Math){ return (400*RoleLv*0.694*(0.78+0.04*Ring))*(random()*(1.02-0.98)+0.98);}",360);
		funMap.put("with(Math){ return _95063_==1;}",571);
		funMap.put("with(Math){ return 6*skilllevela;}",75);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),1);}",91);
		funMap.put("with(Math){ return (1000*RoleLv*0.2*(0.78+0.04*Ring))*(random()*(1.02-0.98)+0.98);}",296);
		funMap.put("with(Math){ return 400*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.0392*(0.4+0.1*Saveid);}",397);
		funMap.put("with(Math){ return _94017_<1;}",499);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+1.5*IsDbPoint+IsSerMul)*(5000*RoleLv*1.25/50);}",441);
		funMap.put("with(Math){ return (400*RoleLv*1.5)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",408);
		funMap.put("with(Math){ return _94014_>=1;}",497);
		funMap.put("with(Math){ return _97001_/_97002_>=0.2;}",647);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1);}",29);
		funMap.put("with(Math){ return (400*RoleLv*0.833)*(random()*(1.02-0.98)+0.98);}",365);
		funMap.put("with(Math){ return Ring+1;}",422);
		funMap.put("with(Math){ return _500033_;}",697);
		funMap.put("with(Math){ return StdExp*0.83/14.5*((Ring-1)*0.1+1);}",395);
		funMap.put("with(Math){ return !_502002_;}",724);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb+1*skilllevela);}",70);
		funMap.put("with(Math){ return (1*RoleLv*0.775*2*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",309);
		funMap.put("with(Math){ return -(magicattacka*1+magicdefa*1-magicdefb+1*gradea);}",99);
		funMap.put("with(Math){ return -0.3*skilllevela;}",85);
		funMap.put("with(Math){ return _96301_>=1&&_96364_<1;}",628);
		funMap.put("with(Math){ return (1*RoleLv*0.717*2*2)*(random()*(1.02-0.98)+0.98);}",327);
		funMap.put("with(Math){ return 5000*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.058*(0.75+0.05*Saveid)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",454);
		funMap.put("with(Math){ return _95052_>=1;}",565);
		funMap.put("with(Math){ return _96106_>=1;}",605);
		funMap.put("with(Math){ return -(phyattacka*0.5+defenda*0.6-defendb+1*skilllevela);}",135);
		funMap.put("with(Math){ return maxhpb;}",150);
		funMap.put("with(Math){ return 3000+5000*random();}",288);
		funMap.put("with(Math){ return min(max(rolenum*0.004,8),16);}",462);
		funMap.put("with(Math){ return maxhpb*0.09+600;}",180);
		funMap.put("with(Math){ return _96204_>=1;}",656);
		funMap.put("with(Math){ return (400*RoleLv*1.099*3)*(random()*(1.02-0.98)+0.98);}",378);
		funMap.put("with(Math){ return -(phyattacka*1-defendb+1*skilllevela);}",35);
		funMap.put("with(Math){ return _99034_>=1;}",678);
		funMap.put("with(Math){ return (5000*RoleLv*0.075)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",439);
		funMap.put("with(Math){ return quality;}",5);
		funMap.put("with(Math){ return ((medicala+3*skilllevela)*0.5+abs(maindamage)*0.5)*(1+healrevisea)*(1+medicaljiashena/1000);}",62);
		funMap.put("with(Math){ return 1.38*RoleLv*(random()*(1.02-0.98)+0.98);}",339);
		funMap.put("with(Math){ return _96122_<1;}",616);
		funMap.put("with(Math){ return -(magicattacka*1-magicdefb+3*skilllevela);}",136);
		funMap.put("with(Math){ return (1*RoleLv*5)*(random()*(1.02-0.98)+0.98);}",330);
		funMap.put("with(Math){ return _95062_==1;}",569);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb*0.9+2*skilllevela);}",158);
		funMap.put("with(Math){ return _94006_<3;}",489);
		funMap.put("with(Math){ return _96206_>=4;}",658);
		funMap.put("with(Math){ return !_501004_;}",704);
		funMap.put("with(Math){ return 2+min(floor(skilllevela/60),1);}",90);
		funMap.put("with(Math){ return maxhpb*0.06+400;}",179);
		funMap.put("with(Math){ return (sealhita>=unsealb)?(0.98-0.38*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)*(0.3+0.05*skilllevela)):(0.6*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)*(0.3+0.05*skilllevela));}",132);
		funMap.put("with(Math){ return _96301_>=1&&_96308_<1;}",622);
		funMap.put("with(Math){ return -max(curhpa-maxhpa*0.1,0);}",185);
		funMap.put("with(Math){ return _96105_>=1;}",607);
		funMap.put("with(Math){ return 400*min(RoleLv,84)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",417);
		funMap.put("with(Math){ return _95049_>=2||_95054_>=1;}",556);
		funMap.put("with(Math){ return (1000*MonsterLv*0.019*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*IsDbPoint)*(random()*(1.02-0.98)+0.98);}",275);
		funMap.put("with(Math){ return (400*RoleLv*0.5)*(random()*(1.02-0.98)+0.98);}",405);
		funMap.put("with(Math){ return (400*RoleLv*0.628*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1))*0.5)*(random()*(1.02-0.98)+0.98);}",348);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*(0.5+0.05*(4-preaimcount));}",101);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*6.6667/8*((Ring-1)*0.3+1));}",351);
		funMap.put("with(Math){ return gradea*0.4;}",160);
		funMap.put("with(Math){ return _99033_>=1;}",677);
		funMap.put("with(Math){ return -1.4*skilllevela;}",87);
		funMap.put("with(Math){ return 5*skilllevela;}",213);
		funMap.put("with(Math){ return _95057_<1;}",572);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)));}",89);
		funMap.put("with(Math){ return (StdMoney*4.2/8*((Ring-1)*0.3+1))*(random()*(1.05-0.95)+0.95);}",274);
		funMap.put("with(Math){ return _96016_>=1||_96018_>=1;}",599);
		funMap.put("with(Math){ return (1*TeamLv*0.196*(0.78+0.04*Ring)*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",314);
		funMap.put("with(Math){ return 4+min(floor(skilllevela/60),1);}",36);
		funMap.put("with(Math){ return _94022_>=1;}",510);
		funMap.put("with(Math){ return -(phyattacka*0.75-defendb+1*skilllevela);}",26);
		funMap.put("with(Math){ return _96301_>=1&&_96311_<1;}",625);
		funMap.put("with(Math){ return (1+14*IsDbPoint+IsSerMul)*(StdMoney*1.5/74*((Ring-1)*0.09+1))*(random()*(1.2-0.8)+0.8);}",302);
		funMap.put("with(Math){ return _96001_/_96002_>=0.2;}",593);
		funMap.put("with(Math){ return _96301_>=1&&_96362_<1;}",626);
		funMap.put("with(Math){ return _94030_>=3;}",515);
		funMap.put("with(Math){ return 10+1.2*gradea;}",192);
		funMap.put("with(Math){ return (1*RoleLv*4)*(random()*(1.02-0.98)+0.98);}",335);
		funMap.put("with(Math){ return ((medicala+3*skilllevela)*0.25+abs(maindamage)*0.25)*(1+healrevisea)*(1+medicaljiashena/1000);}",204);
		funMap.put("with(Math){ return -(phyattacka*0.75-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",232);
		funMap.put("with(Math){ return _94028_>=1;}",523);
		funMap.put("with(Math){ return (1000*TeamLv*0.038*(0.78+0.04*Ring)*(1-IsDbPoint)+1000*TeamLv*0.098*(0.78+0.04*Ring)*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",268);
		funMap.put("with(Math){ return _509082_||_509083_;}",692);
		funMap.put("with(Math){ return _96209_>=1;}",661);
		funMap.put("with(Math){ return _96351_>=1&&_96365_<1;}",642);
		funMap.put("with(Math){ return _94031_>=3;}",472);
		funMap.put("with(Math){ return _95010_>=1;}",533);
		funMap.put("with(Math){ return _96104_>=1;}",606);
		funMap.put("with(Math){ return -(magicattacka*1.1-magicdefb+3*skilllevela);}",141);
		funMap.put("with(Math){ return _95060_>=1;}",566);
		funMap.put("with(Math){ return _96212_>=1;}",664);
		funMap.put("with(Math){ return 7*skilllevela;}",81);
		funMap.put("with(Math){ return -maxhpb*2.5;}",148);
		funMap.put("with(Math){ return _501010_;}",721);
		funMap.put("with(Math){ return (5000*RoleLv*0.111)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",436);
		funMap.put("with(Math){ return _95057_==1;}",568);
		funMap.put("with(Math){ return (quality*12+150)*(havebuffa(508042)?(1.2):(1));}",8);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb*0.9+1*skilllevela);}",71);
		funMap.put("with(Math){ return _94009_<1;}",493);
		funMap.put("with(Math){ return speeda;}",212);
		funMap.put("with(Math){ return 200*(havebuffa(508042)?(1.2):(1));}",14);
		funMap.put("with(Math){ return -((0.15+0.001*skilllevela)*magicdefb+1*skilllevela);}",37);
		funMap.put("with(Math){ return medicala+1*skilllevela;}",108);
		funMap.put("with(Math){ return (1000*RoleLv*0.232*2)*(random()*(1.02-0.98)+0.98);}",285);
		funMap.put("with(Math){ return -(phyattacka*0.9-defendb+1*skilllevela);}",24);
		funMap.put("with(Math){ return _96205_>=1;}",657);
		funMap.put("with(Math){ return -(phyattacka*1.05-defendb+1*skilllevela);}",23);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*6.7/10)*((TeamNum-1)*0.05+1);}",354);
		funMap.put("with(Math){ return !_504002_;}",694);
		funMap.put("with(Math){ return (quality*5+50)*(havebuffa(508042)?(1.2):(1));}",9);
		funMap.put("with(Math){ return _94040_<1;}",476);
		funMap.put("with(Math){ return _96213_>=3;}",665);
		funMap.put("with(Math){ return 250*RoleLv*(random()*(1.02-0.98)+0.98);}",291);
		funMap.put("with(Math){ return 10+2*gradea;}",214);
		funMap.put("with(Math){ return _96402_>=1||_96403_>=1;}",646);
		funMap.put("with(Math){ return -(magicattacka*2.5-magicdefb+2*skilllevela)*0.5*(havebuffa(506109)?((random()*(1.20-1.1))+1.1):((random()*(1.20-0.9))+0.9))*2;}",226);
		funMap.put("with(Math){ return _95059_>=1;}",564);
		funMap.put("with(Math){ return 0.25+0.001*(skilllevela-gradeb);}",60);
		funMap.put("with(Math){ return maindamage*0.33*((random()*(1.05-0.95))+0.95);}",172);
		funMap.put("with(Math){ return 10+1.2*skilllevela;}",22);
		funMap.put("with(Math){ return 10*skilllevela;}",45);
		funMap.put("with(Math){ return (15+random()*5)*RoleLv;}",251);
		funMap.put("with(Math){ return (StdMoney*2)*(random()*(1.05-0.95)+0.95);}",300);
		funMap.put("with(Math){ return _95046_<=2;}",553);
		funMap.put("with(Math){ return (400*RoleLv*0.25*(0.79+0.02*AnswerCnt))*0.5*(random()*(1.02-0.98)+0.98);}",419);
		funMap.put("with(Math){ return -(phyattacka*0.9-defendb+1*gradea);}",79);
		funMap.put("with(Math){ return _94023_>=1;}",466);
		funMap.put("with(Math){ return _94033_>=2;}",469);
		funMap.put("with(Math){ return (1000*RoleLv*0.232*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1))*0.5)*(random()*(1.02-0.98)+0.98);}",252);
		funMap.put("with(Math){ return effectpointa>=4;}",506);
		funMap.put("with(Math){ return 400*min(RoleLv,69)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",372);
		funMap.put("with(Math){ return _95085_>=2&&_95089_<1;}",588);
		funMap.put("with(Math){ return _95002_==1;}",530);
		funMap.put("with(Math){ return (400*RoleLv*1.25)*(random()*(1.02-0.98)+0.98);}",379);
		funMap.put("with(Math){ return skilllevela*10;}",20);
		funMap.put("with(Math){ return _94026_>=1;}",481);
		funMap.put("with(Math){ return _99002_<1;}",618);
		funMap.put("with(Math){ return 500*RoleLv;}",266);
		funMap.put("with(Math){ return abs(maindamage*0.5);}",125);
		funMap.put("with(Math){ return 0.3*skilllevela;}",206);
		funMap.put("with(Math){ return _95065_>=3;}",574);
		funMap.put("with(Math){ return -(phyattacka*1.05-defendb+1*gradea);}",170);
		funMap.put("with(Math){ return floor((min(max(floor((ServerLv-40)*0.2),0),4)*3+6)*min(max(rolenum*0.0005,1),2));}",465);
		funMap.put("with(Math){ return null;}",464);
		funMap.put("with(Math){ return _95058_>=1;}",563);
		funMap.put("with(Math){ return -curhpa*1;}",142);
		funMap.put("with(Math){ return _96215_>=4;}",666);
		funMap.put("with(Math){ return (400*RoleLv*1)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",415);
		funMap.put("with(Math){ return _96102_>=1;}",612);
		funMap.put("with(Math){ return min(maxhpb*0.50,gradeb*30);}",184);
		funMap.put("with(Math){ return _94019_>=1;}",503);
		funMap.put("with(Math){ return medicala+3*skilllevela;}",111);
		funMap.put("with(Math){ return !_13_;}",691);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))+0.05+(1-curhpa/maxhpa)*0.1;}",215);
		funMap.put("with(Math){ return _95039_<1;}",545);
		funMap.put("with(Math){ return medicala+3*skilllevela+((pve)?(1*skilllevela):(0));}",220);
		funMap.put("with(Math){ return _509082_||_509083_||_506201_||_509068_||_509031_;}",723);
		funMap.put("with(Math){ return _95064_>=2;}",573);
		funMap.put("with(Math){ return _97001_/_97002_<0.2;}",648);
		funMap.put("with(Math){ return -(phyattacka*1.25-defendb+1*skilllevela);}",32);
		funMap.put("with(Math){ return -(phyattacka*1-defendb+1*gradea+max((phyattacka-phyattackb)*0.25,0));}",173);
		funMap.put("with(Math){ return (400*RoleLv*1.099*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1))*0.5)*(random()*(1.02-0.98)+0.98);}",382);
		funMap.put("with(Math){ return 1000*RoleLv*0.067*(0.74+0.02*Time)*(random()*(1.02-0.98)+0.98);}",257);
		funMap.put("with(Math){ return 0.5*skilllevela;}",44);
		funMap.put("with(Math){ return (5000*RoleLv*0.049*1.5)*(random()*(1.02-0.98)+0.98);}",451);
		funMap.put("with(Math){ return TeamLv;}",728);
		funMap.put("with(Math){ return _95046_<=1;}",578);
		funMap.put("with(Math){ return _96351_>=1&&_96360_<1;}",637);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),2);}",92);
		funMap.put("with(Math){ return (5000*min(max(RoleLv,FuBenLv),FuBenLv+9)*0.2*(0.7+0.1*Ring))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",448);
		funMap.put("with(Math){ return _96211_>=1;}",663);
		funMap.put("with(Math){ return 10+1.4*skilllevela;}",49);
		funMap.put("with(Math){ return effectpointa>=5;}",507);
		funMap.put("with(Math){ return (1000*RoleLv*0.1*(0.89+0.02*AnswerCnt))*0.5*(random()*(1.02-0.98)+0.98);}",292);
		funMap.put("with(Math){ return _95057_>=1;}",591);
		funMap.put("with(Math){ return (5000*RoleLv*0.15)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",459);
		funMap.put("with(Math){ return randint(1,2);}",107);
		funMap.put("with(Math){ return (1*RoleLv*1.333)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",306);
		funMap.put("with(Math){ return (400*RoleLv*0.678*2)*(random()*(1.02-0.98)+0.98);}",353);
		funMap.put("with(Math){ return -(magicattacka*2.5-magicdefb+2*skilllevela)*0.5*(havebuffa(506109)?((random()*(1.20-1.1))+1.1):((random()*(1.20-0.9))+0.9))*(1+(0.4+0.1*skilllevela)*(1-curhpa/maxhpa));}",116);
		funMap.put("with(Math){ return 100*RoleLv;}",301);
		funMap.put("with(Math){ return 400*RoleLv*1.215*(0.88+0.03);}",384);
		funMap.put("with(Math){ return 14*skilllevela;}",48);
		funMap.put("with(Math){ return -(phyattacka*1.15-defendb+1*skilllevela);}",31);
		funMap.put("with(Math){ return (curhpa/maxhpa)<=0.4;}",42);
		funMap.put("with(Math){ return (1*RoleLv*0.775*2*2)*(random()*(1.02-0.98)+0.98);}",316);
		funMap.put("with(Math){ return _110_||_120_;}",725);
		funMap.put("with(Math){ return (1*RoleLv*1)*(random()*(1.02-0.98)+0.98);}",323);
		funMap.put("with(Math){ return -min(0.1*curhpb,10*skilllevela)-3*skilllevela;}",61);
		funMap.put("with(Math){ return (1000*RoleLv*0.377*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1))*0.5)*(random()*(1.02-0.98)+0.98);}",259);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)/10)*440+(200+floor(min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)/10)*40*Saveid))*2;}",425);
		funMap.put("with(Math){ return _96110_>=1;}",609);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*(0.3+0.05*skilllevela)*0.5;}",128);
		funMap.put("with(Math){ return (5000*MonsterLv*0.011*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*(1-IsDbPoint)+5000*MonsterLv*0.02*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*IsDbPoint)*(random()*(1.02-0.98)+0.98);}",442);
		funMap.put("with(Math){ return effectpointa<2;}",123);
		funMap.put("with(Math){ return -curhpa*0.8;}",155);
		funMap.put("with(Math){ return _94030_==2&&_94029_>=1;}",516);
		funMap.put("with(Math){ return (400*RoleLv*1)*(random()*(1.02-0.98)+0.98);}",385);
		funMap.put("with(Math){ return _96351_>=1&&_96357_<1;}",634);
		funMap.put("with(Math){ return _95080_==1;}",581);
		funMap.put("with(Math){ return (1000*RoleLv*0.215*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1))*0.5)*(random()*(1.02-0.98)+0.98);}",265);
		funMap.put("with(Math){ return _94044_>=1;}",528);
		funMap.put("with(Math){ return -min(0.1*curhpb,20*skilllevela);}",119);
		funMap.put("with(Math){ return _94004_>=1;}",485);
		funMap.put("with(Math){ return quality*0.4+10;}",6);
		funMap.put("with(Math){ return _96203_>=4;}",655);
		funMap.put("with(Math){ return (1000*RoleLv*0.377*3)*(random()*(1.02-0.98)+0.98);}",248);
		funMap.put("with(Math){ return -curmpa;}",186);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*6);}",412);
		funMap.put("with(Math){ return _95013_==1;}",532);
		funMap.put("with(Math){ return _95053_==1;}",560);
		funMap.put("with(Math){ return _96351_>=1;}",632);
		funMap.put("with(Math){ return (1*min(max(RoleLv,FuBenLv),FuBenLv+9)*5)*(random()*(1.02-0.98)+0.98);}",312);
		funMap.put("with(Math){ return _96123_>=1;}",617);
		funMap.put("with(Math){ return effectpointa>=2;}",124);
		funMap.put("with(Math){ return 10+1.4*gradea;}",201);
		funMap.put("with(Math){ return 400*min(RoleLv,49)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",373);
		funMap.put("with(Math){ return _96217_<3;}",668);
		funMap.put("with(Math){ return _99001_<1&&_99018_>=1;}",687);
		funMap.put("with(Math){ return gradea>=60;}",168);
		funMap.put("with(Math){ return gradea*1.5;}",191);
		funMap.put("with(Math){ return _99031_>=1;}",675);
		funMap.put("with(Math){ return -(phyattacka*0.4+defenda*0.5-defendb+2*gradea);}",130);
		funMap.put("with(Math){ return _504003_;}",711);
		funMap.put("with(Math){ return (StdMoney*2/10)*(random()*(1.05-0.95)+0.95);}",278);
		funMap.put("with(Math){ return quality*32+1000;}",4);
		funMap.put("with(Math){ return -curhpb*0.1;}",144);
		funMap.put("with(Math){ return (5000*TeamLv*0.025*(0.78+0.04*Ring)*(1-IsDbPoint)+5000*TeamLv*0.102*(0.78+0.04*Ring)*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",437);
		funMap.put("with(Math){ return _95082_>=2;}",584);
		funMap.put("with(Math){ return 0*TeamNum+8+2*random();}",735);
		funMap.put("with(Math){ return MonsterLv+20;}",334);
		funMap.put("with(Math){ return (400*RoleLv*0.7)*(random()*(1.02-0.98)+0.98);}",413);
		funMap.put("with(Math){ return _96301_>=1&&_96307_<1;}",621);
		funMap.put("with(Math){ return _95024_<=0;}",539);
		funMap.put("with(Math){ return !_506201_;}",720);
		funMap.put("with(Math){ return _95086_>=1&&_95087_<1;}",589);
		funMap.put("with(Math){ return -(phyattacka*0.95-defendb+1*skilllevela);}",76);
		funMap.put("with(Math){ return (1*TeamLv*0.239*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",311);
		funMap.put("with(Math){ return _95055_>=1;}",562);
		funMap.put("with(Math){ return _97003_>=1;}",649);
		funMap.put("with(Math){ return (1000*RoleLv*0.3)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",281);
		funMap.put("with(Math){ return -min(0.05*curmpb,10*skilllevela);}",120);
		funMap.put("with(Math){ return _99001_<1&&_99013_>=1;}",682);
		funMap.put("with(Math){ return (400*TeamLv*0.15*(0.78+0.04*Ring)*(1-IsDbPoint)+400*TeamLv*1.02*(0.78+0.04*Ring)*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",346);
		funMap.put("with(Math){ return (1000*RoleLv*0.5)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",263);
		funMap.put("with(Math){ return !_506109_;}",712);
		funMap.put("with(Math){ return _504002_;}",693);
		funMap.put("with(Math){ return skilllevela*50;}",139);
		funMap.put("with(Math){ return (5000*RoleLv*0.05)*(random()*(1.02-0.98)+0.98)*0.5;}",450);
		funMap.put("with(Math){ return 4000+6000*random();}",277);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,74)/10)*440+(200+floor(min(RoleLv,74)/10)*40*10))*2;}",429);
		funMap.put("with(Math){ return (1*RoleLv*2)*(random()*(1.02-0.98)+0.98);}",328);
		funMap.put("with(Math){ return _96113_<1;}",613);
		funMap.put("with(Math){ return _96351_>=1&&_96362_<1;}",639);
		funMap.put("with(Math){ return _94012_>=2;}",494);
		funMap.put("with(Math){ return max(floor((Ring-4)/2),0);}",340);
		funMap.put("with(Math){ return (1+IsSerMul)*(StdExp*0.83/14.5*((Ring-1)*0.1+1));}",363);
		funMap.put("with(Math){ return _95018_==1;}",567);
		funMap.put("with(Math){ return _95088_<=0;}",592);
		funMap.put("with(Math){ return -maxhpb*0.5;}",151);
		funMap.put("with(Math){ return gradea*0.8;}",159);
		funMap.put("with(Math){ return skilllevela*8;}",140);
		funMap.put("with(Math){ return gradea*1.6;}",162);
		funMap.put("with(Math){ return _95042_>=1;}",549);
		funMap.put("with(Math){ return _96301_>=1&&_96365_<1;}",629);
		funMap.put("with(Math){ return 400*min(RoleLv,59)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",418);
		funMap.put("with(Math){ return _95079_<1;}",582);
		funMap.put("with(Math){ return phyattacka*0.1;}",194);
		funMap.put("with(Math){ return _95074_==1;}",575);
		funMap.put("with(Math){ return _95003_<=1;}",534);
		funMap.put("with(Math){ return (1*RoleLv*6.66)*(random()*(1.02-0.98)+0.98);}",317);
		funMap.put("with(Math){ return effectpointa>=3;}",505);
		funMap.put("with(Math){ return (400*RoleLv*1.215*1.5)*(random()*(1.02-0.98)+0.98);}",394);
		funMap.put("with(Math){ return (5000*RoleLv*0.05*(0.78+0.04*Ring))*(random()*(1.02-0.98)+0.98);}",438);
		funMap.put("with(Math){ return _505005_||_504011_;}",702);
		funMap.put("with(Math){ return _96210_<3;}",662);
		funMap.put("with(Math){ return (105+random()*30)*RoleLv;}",370);
		funMap.put("with(Math){ return (400*RoleLv*0.25*(0.79+0.02*AnswerCnt))*(random()*(1.02-0.98)+0.98);}",369);
		funMap.put("with(Math){ return _95050_>=1;}",557);
		funMap.put("with(Math){ return (curhpa/maxhpa)<=0.6;}",41);
		funMap.put("with(Math){ return !_506306_;}",719);
		funMap.put("with(Math){ return _94036_>=2;}",475);
		funMap.put("with(Math){ return _94044_>=3;}",526);
		funMap.put("with(Math){ return _96107_>=1;}",602);
		funMap.put("with(Math){ return min(-1*gradea+0.5*skilllevela,0);}",88);
		funMap.put("with(Math){ return -(phyattacka*1.15-min(defendb,magicdefb)+1*skilllevela);}",196);
		funMap.put("with(Math){ return (StdMoney*6/10)*(random()*(1.05-0.95)+0.95);}",271);
		funMap.put("with(Math){ return _99001_<1&&_99011_>=1;}",680);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb+1*gradea);}",78);
		funMap.put("with(Math){ return -min(0.25*curhpb,50*skilllevela);}",58);
		funMap.put("with(Math){ return _96208_<3;}",660);
		funMap.put("with(Math){ return -8*skilllevela;}",104);
		funMap.put("with(Math){ return _94003_>=1&&_94006_>=3;}",501);
		funMap.put("with(Math){ return _96124_<=0.01;}",597);
		funMap.put("with(Math){ return (400*RoleLv*1.215*2)*(random()*(1.02-0.98)+0.98);}",390);
		funMap.put("with(Math){ return (StdMoney*3.15/8*((Ring-1)*0.3+1))*(random()*(1.05-0.95)+0.95);}",255);
		funMap.put("with(Math){ return 400*(havebuffa(508042)?(1.2):(1));}",7);
		funMap.put("with(Math){ return 0.6+0.002*(skilllevela-gradeb)+(enhanceseala-resistsealb);}",103);
		funMap.put("with(Math){ return (400*RoleLv*1.25)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",374);
		funMap.put("with(Math){ return gradea*1.3;}",164);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb*0.9+1*skilllevela+max(0,speeda-speedb)*0.5);}",207);
		funMap.put("with(Math){ return effectpointa>=0;}",504);
		funMap.put("with(Math){ return 0.5+((survivala<survivalb)?(0.25):(0));}",65);
		funMap.put("with(Math){ return _94015_>=1;}",498);
		funMap.put("with(Math){ return 150*(havebuffa(508042)?(1.2):(1));}",15);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,84)/10)*440+(200+floor(min(RoleLv,84)/10)*40*10))*2;}",432);
		funMap.put("with(Math){ return _504013_;}",714);
		funMap.put("with(Math){ return (400*MonsterLv*0.667)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",381);
		funMap.put("with(Math){ return _94020_>=1;}",508);
		funMap.put("with(Math){ return _95021_==1;}",538);
		funMap.put("with(Math){ return 0*TeamNum+6+2*random();}",734);
		funMap.put("with(Math){ return (1+IsSerMul)*(5000*RoleLv*0.5/20);}",460);
		funMap.put("with(Math){ return (1000*min(max(RoleLv,FuBenLv),FuBenLv+9)*0.1*(0.7+0.1*Ring))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",286);
		funMap.put("with(Math){ return _96109_>=1;}",608);
		funMap.put("with(Math){ return (400*RoleLv*0.75)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",409);
		funMap.put("with(Math){ return _99001_<1&&_99030_==1;}",672);
		funMap.put("with(Math){ return (400*RoleLv*2.222)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",392);
		funMap.put("with(Math){ return !_13_&&!_501008_;}",707);
		funMap.put("with(Math){ return _95045_>=1;}",552);
		funMap.put("with(Math){ return _96125_>1;}",670);
		funMap.put("with(Math){ return effectpointa<1;}",525);
		funMap.put("with(Math){ return 5000*RoleLv*0.1*(0.74+0.02*Time)*(random()*(1.02-0.98)+0.98);}",455);
		funMap.put("with(Math){ return (400*RoleLv*1.215*1)*(random()*(1.02-0.98)+0.98);}",366);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))+0.05;}",86);
		funMap.put("with(Math){ return -(phyattacka*1.6-defendb+1*skilllevela);}",47);
		funMap.put("with(Math){ return maxhpb*0.03+200;}",178);
		funMap.put("with(Math){ return _13_;}",713);
		funMap.put("with(Math){ return skilllevela>=90;}",53);
		funMap.put("with(Math){ return _94006_<3&&_94005_>=1;}",500);
		funMap.put("with(Math){ return gradea*0.6;}",163);
		funMap.put("with(Math){ return (IsDbPoint*(Ring-1)*1+6);}",324);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*(0.3+0.05*skilllevela)*0.5/(1-(((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*(0.3+0.05*skilllevela)*0.5);}",129);
		funMap.put("with(Math){ return 1000*RoleLv*0.194*(0.85+0.03*((Time-1)%9+1))*(0.9+0.1*(floor((Time-1)/9)+1))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",289);
		funMap.put("with(Math){ return _502002_;}",689);
		funMap.put("with(Math){ return (StdMoney*6/28.1*((Ring-1)*0.09+1))*(random()*(1.05-0.95)+0.95);}",264);
		funMap.put("with(Math){ return _96003_>=1;}",596);
		funMap.put("with(Math){ return min(maxhpb*0.25,gradeb*18);}",183);
		funMap.put("with(Math){ return (1000*RoleLv*0.215*2)*(random()*(1.02-0.98)+0.98);}",254);
		funMap.put("with(Math){ return effectpointa>=1;}",121);
		funMap.put("with(Math){ return _95084_<1;}",587);
		funMap.put("with(Math){ return (1*RoleLv*0.775*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",310);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*6.7/5);}",410);
		funMap.put("with(Math){ return 0.6+0.002*(skilllevela-gradeb);}",28);
		funMap.put("with(Math){ return _95052_>=2;}",559);
		funMap.put("with(Math){ return (curhpa/maxhpa)<=0.8;}",40);
		funMap.put("with(Math){ return _94034_<1;}",470);
		funMap.put("with(Math){ return -curhpb*0.2;}",145);
		funMap.put("with(Math){ return (1000*min(max(RoleLv,FuBenLv),FuBenLv+9)*0.2*(0.7+0.1*Ring))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",258);
		funMap.put("with(Math){ return (1*RoleLv*10)*(random()*(1.02-0.98)+0.98);}",318);
		funMap.put("with(Math){ return _95082_>=3;}",590);
		funMap.put("with(Math){ return (1000*RoleLv*0.05*(0.79+0.02*AnswerCnt))*(random()*(1.02-0.98)+0.98);}",249);
		funMap.put("with(Math){ return (400*RoleLv*0.5*(0.79+0.02*AnswerCnt))*(random()*(1.02-0.98)+0.98);}",403);
		funMap.put("with(Math){ return round((1-pow(0.98,skilllevela))*1000);}",246);
		funMap.put("with(Math){ return _94038_>=2;}",522);
		funMap.put("with(Math){ return skilllevela>=2;}",66);
		funMap.put("with(Math){ return _95047_>=2;}",554);
		funMap.put("with(Math){ return -min((random()*(0.13-0.07)+0.07)*maxhpa,curhpa-1);}",34);
		funMap.put("with(Math){ return -(phyattacka*0.75-defendb+1*gradea);}",171);
		funMap.put("with(Math){ return 1000*min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)*0.35*(0.5+0.1*Saveid)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",287);
		funMap.put("with(Math){ return RoleLv*5;}",332);
		funMap.put("with(Math){ return 400*min(RoleLv,79)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",420);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,94)/10)*440+(200+floor(min(RoleLv,94)/10)*40*10))*2;}",428);
		funMap.put("with(Math){ return -(phyattacka*1-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",235);
		funMap.put("with(Math){ return _94001_>=1;}",482);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",236);
		funMap.put("with(Math){ return (1000*RoleLv*0.5)*(random()*(1.02-0.98)+0.98);}",267);
		funMap.put("with(Math){ return (1*RoleLv*1.256*2*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",321);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*gradea)*(0.5+0.05*(2-preaimcount));}",167);
		funMap.put("with(Math){ return _94003_>=1&&_94006_<3;}",502);
		funMap.put("with(Math){ return 4+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),1);}",54);
		funMap.put("with(Math){ return 400*RoleLv*1*(0.74+0.02*Time)*(random()*(1.02-0.98)+0.98);}",362);
		funMap.put("with(Math){ return _509201_;}",726);
		funMap.put("with(Math){ return -(phyattacka*1.6-defendb+1*gradea);}",189);
		funMap.put("with(Math){ return _96120_>=1;}",614);
		funMap.put("with(Math){ return (1000*RoleLv*0.194*1)*(random()*(1.02-0.98)+0.98);}",270);
		funMap.put("with(Math){ return RoleLv*100*30/5;}",457);
		funMap.put("with(Math){ return _95083_==1;}",586);
		funMap.put("with(Math){ return 0.6+0.001*(skilllevela-gradeb);}",57);
		funMap.put("with(Math){ return (400*TeamLv*0.3*(1-IsDbPoint)+400*TeamLv*1.244*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",359);
		funMap.put("with(Math){ return ((random()*(5-3))+3);}",177);
		funMap.put("with(Math){ return (400*min(max(RoleLv,FuBenLv),FuBenLv+9)*2*(0.7+0.1*Ring))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",391);
		funMap.put("with(Math){ return _94054_>=1;}",480);
		funMap.put("with(Math){ return _96200_>=4;}",652);
		funMap.put("with(Math){ return (5000*RoleLv*0.049*2)*(random()*(1.02-0.98)+0.98);}",447);
		funMap.put("with(Math){ return maindamage*((random()*(1.05-0.95))+0.95);}",190);
		funMap.put("with(Math){ return _95028_==1;}",540);
		funMap.put("with(Math){ return _95076_==1;}",577);
		funMap.put("with(Math){ return _95044_>=1;}",551);
		funMap.put("with(Math){ return _94025_>=1&&_94030_==1;}",514);
		funMap.put("with(Math){ return _96351_>=1&&_96361_<1;}",638);
		funMap.put("with(Math){ return -(phyattacka*0.9-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",231);
		funMap.put("with(Math){ return (400*RoleLv*0.678*0.6);}",406);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16;}",126);
		funMap.put("with(Math){ return -(magicattacka*2.5-magicdefb+2*skilllevela)*0.5;}",102);
		funMap.put("with(Math){ return (5000*RoleLv*0.1)*(random()*(1.02-0.98)+0.98);}",444);
		funMap.put("with(Math){ return _94027_>=1&&_94053_<1;}",474);
		funMap.put("with(Math){ return 4*skilllevela*(1+healrevisea+healedreviseb)*(1+medicaljiashena/1000+medicalonjiashenb/1000);}",117);
		funMap.put("with(Math){ return -(phyattacka*1-defendb+3*gradea);}",169);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*6.7/15);}",389);
		funMap.put("with(Math){ return !_120_;}",695);
		funMap.put("with(Math){ return (400*RoleLv*0.278*(0.78+0.04*Ring))*(random()*(1.02-0.98)+0.98);}",356);
		funMap.put("with(Math){ return -curhpb*0.5;}",146);
		funMap.put("with(Math){ return maxhpa*0.1;}",193);
		funMap.put("with(Math){ return !_501010_&&!_13_;}",703);
		funMap.put("with(Math){ return maxhpb*1;}",137);
		funMap.put("with(Math){ return -(phyattacka*0.75-defendb+1*skilllevela+max(0,speeda-speedb)*0.5);}",211);
		funMap.put("with(Math){ return -(phyattacka*1.05-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",230);
		funMap.put("with(Math){ return skilllevela>=3;}",67);
		funMap.put("with(Math){ return (StdMoney*2/5)*(random()*(1.05-0.95)+0.95);}",260);
		funMap.put("with(Math){ return _99001_<1&&_99015_>=1;}",684);
		funMap.put("with(Math){ return -(phyattacka*1.2-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",238);
		funMap.put("with(Math){ return (MonsterLv-30)*0.2+4;}",331);
		funMap.put("with(Math){ return 1*TeamNum;}",733);
		funMap.put("with(Math){ return maindamage*0.2*((random()*(1.05-0.95))+0.95);}",174);
		funMap.put("with(Math){ return 2200+floor(min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)/10)*440+(200+floor(min(RoleLv,FuBenId*10-1051-floor(FuBenId/113)*5)/10)*40*Saveid);}",427);
		funMap.put("with(Math){ return ((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))*0.3;}",154);
		funMap.put("with(Math){ return _96302_>=1;}",620);
		funMap.put("with(Math){ return (400*RoleLv*0.678*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1))*0.5)*(random()*(1.02-0.98)+0.98);}",347);
		funMap.put("with(Math){ return (1*RoleLv*0.717*2)*(random()*(1.02-0.98)+0.98);}",308);
		funMap.put("with(Math){ return 5000*RoleLv*0.049*(0.85+0.03*((Time-1)%9+1))*(0.9+0.1*(floor((Time-1)/9)+1))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",443);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*0.2;}",244);
		funMap.put("with(Math){ return 400*min(RoleLv,89)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",421);
		funMap.put("with(Math){ return -(phyattacka*1.15-defendb+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",237);
		funMap.put("with(Math){ return -maindamage;}",175);
		funMap.put("with(Math){ return (5000*RoleLv*0.049*1)*(random()*(1.02-0.98)+0.98);}",452);
		funMap.put("with(Math){ return _95038_>=1;}",546);
		funMap.put("with(Math){ return -(phyattacka*0.85-defendb+1*skilllevela);}",77);
		funMap.put("with(Math){ return _94039_>=1;}",477);
		funMap.put("with(Math){ return -(phyattacka*1.25-min(defendb,magicdefb)+1*skilllevela);}",197);
		funMap.put("with(Math){ return _95075_==1;}",576);
		funMap.put("with(Math){ return 2.5*skilllevela;}",21);
		funMap.put("with(Math){ return (400*RoleLv*0.017)*(random()*(1.02-0.98)+0.98);}",358);
		funMap.put("with(Math){ return -(phyattacka*1-defendb+1*gradea);}",1);
		funMap.put("with(Math){ return (2200+floor(min(RoleLv,99)/10)*440+(200+floor(min(RoleLv,99)/10)*40*10))*2;}",431);
		funMap.put("with(Math){ return maxmpb*0.1+150;}",181);
		funMap.put("with(Math){ return !_506101_;}",717);
		funMap.put("with(Math){ return (175+random()*50)*RoleLv;}",400);
		funMap.put("with(Math){ return _99001_<1&&_99014_>=1;}",683);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*0.1;}",106);
		funMap.put("with(Math){ return _94007_>=2;}",491);
		funMap.put("with(Math){ return _96301_>=1;}",619);
		funMap.put("with(Math){ return _96351_>=1&&_96358_<1;}",635);
		funMap.put("with(Math){ return (quality*5+100)*(havebuffa(508042)?(1.2):(1));}",11);
		funMap.put("with(Math){ return -(phyattacka*0.85-defendb+1*skilllevela+max(0,speeda-speedb)*0.5);}",210);
		funMap.put("with(Math){ return (400*RoleLv*0.25*(0.89+0.02*AnswerCnt))*(random()*(1.02-0.98)+0.98);}",349);
		funMap.put("with(Math){ return 3+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),6);}",96);
		funMap.put("with(Math){ return pve;}",98);
		funMap.put("with(Math){ return (400*RoleLv*0.25*(0.89+0.02*AnswerCnt))*0.5*(random()*(1.02-0.98)+0.98);}",364);
		funMap.put("with(Math){ return _96201_>=4;}",653);
		funMap.put("with(Math){ return 5+min(floor(skilllevela/60),1)+min(floor(skilllevela/90),1);}",222);
		funMap.put("with(Math){ return _94025_>=1&&_94029_>=1;}",511);
		funMap.put("with(Math){ return (400*RoleLv*0.628*0.6);}",388);
		funMap.put("with(Math){ return skilllevela>=4;}",68);
		funMap.put("with(Math){ return (1000*RoleLv*0.1)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",256);
		funMap.put("with(Math){ return (1000*RoleLv*0.4)*(random()*(1.02-0.98)+0.98);}",295);
		funMap.put("with(Math){ return (1000*RoleLv*0.1*(0.89+0.02*AnswerCnt))*(random()*(1.02-0.98)+0.98);}",303);
		funMap.put("with(Math){ return (1+14*IsDbPoint+IsSerMul)*(StdMoney*1.5/74*(14*0.09+1))*(random()*(1.2-0.8)+0.8);}",298);
		funMap.put("with(Math){ return _96121_>=1;}",615);
		funMap.put("with(Math){ return !_501010_;}",715);
		funMap.put("with(Math){ return (curhpa/maxhpa)>=0.5;}",30);
		funMap.put("with(Math){ return 100*1;}",283);
		funMap.put("with(Math){ return (400*RoleLv*0.667)*(random()*(1.02-0.98)+0.98)*0.5;}",352);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*0.3;}",243);
		funMap.put("with(Math){ return (1000*RoleLv*0.1*(0.78+0.04*Ring))*(random()*(1.02-0.98)+0.98);}",247);
		funMap.put("with(Math){ return _94005_>=5;}",486);
		funMap.put("with(Math){ return 300*(havebuffa(508042)?(1.2):(1));}",16);
		funMap.put("with(Math){ return -min(0.15*curhpb,15*skilllevela)-3*skilllevela;}",202);
		funMap.put("with(Math){ return _94056_>=2;}",479);
		funMap.put("with(Math){ return (curhpa/maxhpa)<=0.2;}",43);
		funMap.put("with(Math){ return 250*(havebuffa(508042)?(1.2):(1));}",17);
		funMap.put("with(Math){ return (medicala+3*skilllevela)*0.4;}",112);
		funMap.put("with(Math){ return _95086_>=1;}",585);
		funMap.put("with(Math){ return 3.4*RoleLv*(random()*(1.02-0.98)+0.98);}",338);
		funMap.put("with(Math){ return _99001_<1&&_99019_>=1;}",688);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+7*IsDbPoint+IsSerMul)*(StdExp*10/63*(14*0.05+1));}",345);
		funMap.put("with(Math){ return _95041_>=2;}",548);
		funMap.put("with(Math){ return 5000*RoleLv;}",299);
		funMap.put("with(Math){ return (5000*TeamLv*0.05*(1-IsDbPoint)+5000*TeamLv*0.124*IsDbPoint)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15)*(1+0.05*IsTL);}",446);
		funMap.put("with(Math){ return skilllevela>=70;}",27);
		funMap.put("with(Math){ return _506002_;}",706);
		funMap.put("with(Math){ return (1*RoleLv*0.717*2*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",325);
		funMap.put("with(Math){ return 400*min(RoleLv,74)*0.35*(0.75+0.05*10)*(random()*(1.02-0.98)+0.98);}",383);
		funMap.put("with(Math){ return _501004_;}",722);
		funMap.put("with(Math){ return (5000*RoleLv*0.125)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",449);
		funMap.put("with(Math){ return _94055_>=1;}",524);
		funMap.put("with(Math){ return _95036_>=1;}",544);
		funMap.put("with(Math){ return -3*skilllevela;}",56);
		funMap.put("with(Math){ return _509082_||_509083_||_506201_;}",705);
		funMap.put("with(Math){ return gradea*0.9;}",161);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb*0.9+1*skilllevela+4*skilllevela*(1-curhpa/maxhpa));}",233);
		funMap.put("with(Math){ return _96017_<1;}",601);
		funMap.put("with(Math){ return -curhpb*0.7;}",147);
		funMap.put("with(Math){ return (5000*MonsterLv*0.017*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*(1-IsDbPoint)+5000*MonsterLv*0.02*(MonsterNum*0.083+MasterNum*0.1245)*min(max(1-0.2*floor(abs(MonsterLv-RoleLv)/5),0.1),1)*IsDbPoint)*(random()*(1.02-0.98)+0.98);}",456);
		funMap.put("with(Math){ return 1*TeamNum+3;}",727);
		funMap.put("with(Math){ return -(magicattacka*1.5-magicdefb+2*skilllevela)*(0.5+0.05*(3-preaimcount));}",51);
		funMap.put("with(Math){ return (1*RoleLv*1.256*2)*(random()*(1.02-0.98)+0.98);}",319);
		funMap.put("with(Math){ return _96016_<1&&_96018_<1;}",600);
		funMap.put("with(Math){ return -curmpa*0.8;}",157);
		funMap.put("with(Math){ return (1+IsSerMul)*(StdExp*0.2);}",367);
		funMap.put("with(Math){ return _95035_>=1;}",543);
		funMap.put("with(Math){ return _95078_<1;}",580);
		funMap.put("with(Math){ return (1000*RoleLv*0.194*1.5)*(random()*(1.02-0.98)+0.98);}",284);
		funMap.put("with(Math){ return _94003_>=1;}",484);
		funMap.put("with(Math){ return medicala+3*skilllevela+((pve)?(2*skilllevela):(0));}",223);
		funMap.put("with(Math){ return _110_||_120_||_13_;}",708);
		funMap.put("with(Math){ return _99001_<1&&_99017_>=1;}",686);
		funMap.put("with(Math){ return (((sealhita>=unsealb)?(0.98-0.32*pow(0.95,(sealhita/10-unsealb/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)):(0.66*pow(0.9,(unsealb/10-sealhita/10))+(enhanceseala-resistsealb)+(kongzhijiachenga/1000-kongzhimianyib/1000)))-0.16)*0.4;}",242);
		funMap.put("with(Math){ return (1+0.05*IsTL)*(1+IsSerMul)*(StdExp*2.14*7/8*((Ring-1)*0.3+1));}",341);
		funMap.put("with(Math){ return random()<0.3?2:1;}",234);
		funMap.put("with(Math){ return 1*RoleLv*0.667*(3.5+0.5*(floor((Time-1)/5)+1))*floor(1-(Time%5)*0.2)*(random()*(1.02-0.98)+0.98);}",322);
		funMap.put("with(Math){ return -(phyattacka*0.8-defendb+1*gradea);}",80);
		funMap.put("with(Math){ return _95025_==1;}",541);
		funMap.put("with(Math){ return _99001_<1;}",671);
		funMap.put("with(Math){ return (5000*min(max(RoleLv,FuBenLv),FuBenLv+9)*0.1*(0.7+0.1*Ring))*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",458);
		funMap.put("with(Math){ return _95041_>=3;}",561);
		funMap.put("with(Math){ return _94025_>=1&&_94030_==2;}",513);
		funMap.put("with(Math){ return _97004_>=4;}",650);
		funMap.put("with(Math){ return quality*60+2000;}",2);
		funMap.put("with(Math){ return _96301_>=1&&_96310_<1;}",624);
		funMap.put("with(Math){ return _96301_>=1&&_96363_<1;}",627);
		funMap.put("with(Math){ return quality*0.6+10;}",3);
		funMap.put("with(Math){ return (1000*RoleLv*0.05*(0.79+0.02*AnswerCnt))*0.5*(random()*(1.02-0.98)+0.98);}",276);
		funMap.put("with(Math){ return (1000*RoleLv*0.194*2)*(random()*(1.02-0.98)+0.98);}",293);
		funMap.put("with(Math){ return _506306_;}",709);
		funMap.put("with(Math){ return (5000*RoleLv*0.1)*(random()*(1.02-0.98)+0.98)*(1-min(5-TeamNum,2)*0.15);}",440);
		funMap.put("with(Math){ return -(phyattacka*1.1-defendb+1*skilllevela+max(0,speeda-speedb)*0.5);}",205);
		funMap.put("with(Math){ return _94002_>=1;}",483);
		funMap.put("with(Math){ return _99001_<1&&_99016_>=1;}",685);
		funMap.put("with(Math){ return _96354_<1;}",644);
		funMap.put("with(Math){ return (1000*RoleLv*0.377*(0.95+0.05*(PVPCnt+1))*(0.95+0.05*(PVPTargetCnt+1)))*(random()*(1.02-0.98)+0.98);}",279);
		funMap.put("with(Math){ return _96351_>=1&&_96366_<1;}",643);
		funMap.put("with(Math){ return -1*skilllevela;}",229);
		funMap.put("with(Math){ return 1*TeamNum+2;}",731);
		funMap.put("with(Math){ return _94038_>=2||_94028_>=1;}",478);
		funMap.put("with(Math){ return _94005_>=3;}",487);
		funMap.put("with(Math){ return _95081_<1;}",583);
		funMap.put("with(Math){ return _95018_<=1;}",536);
		funMap.put("with(Math){ return _506003_;}",698);
		funMap.put("with(Math){ return (400*RoleLv*0.628*2)*(random()*(1.02-0.98)+0.98);}",396);
		funMap.put("with(Math){ return (StdExp*13.333/28.867*((Ring-1)*0.08+1)+RoleLv*50-1000);}",361);
		funMap.put("with(Math){ return min(max(rolenum*0.001,2),4)+(ServerLv-50)*0.2;}",461);
		funMap.put("with(Math){ return -0.1-(0.1+0.05*skilllevela)*(1-curmpb/maxmpb);}",97);
	}
	public static Object JsFunbyID(IJavaScriptEngine engine, Fighter opf, Fighter aimf, int id)
	{
		switch(id)
		{
			case 176:  { return ((Math.random()*(3-2))+2);}
			case 304:  { return (1000*engine.getDouble("RoleLv").intValue()*0.4)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 55:  { return -2*engine.getDouble("skilllevel").intValue();}
			case 133:  { return -(opf.getEffectRole().getAttrById(130)*0.7-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 280:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*0.5*(Math.random()*(1.02-0.98)+0.98);}
			case 241:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*0.5;}
			case 165:  { return 3*engine.getDouble("gradea").intValue();}
			case 716:  { return (Boolean)engine.get("_506109_");}
			case 463:  { return Math.floor((Math.min(Math.max(Math.floor((engine.getDouble("ServerLv").intValue()-50)*0.2),0),3)*2+3)*Math.min(Math.max(engine.getDouble("rolenum").intValue()*0.0005,1),2));}
			case 371:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.35*(0.5+0.1*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 611:  { return engine.getDouble("_96101_")==1;}
			case 435:  { return engine.getDouble("SwXs").intValue()*1;}
			case 570:  { return engine.getDouble("_95073_")==1;}
			case 424:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),59)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),59)/10)*40*10))*2;}
			case 282:  { return (1000*engine.getDouble("RoleLv").intValue()*0.232*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 368:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+7*engine.getDouble("IsDbPoint").intValue()+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*10/63*((engine.getDouble("Ring").intValue()-1)*0.05+1));}
			case 537:  { return engine.getDouble("_95020_")==1;}
			case 550:  { return engine.getDouble("_95043_")>=3;}
			case 337:  { return 2.5*engine.getDouble("RoleLv").intValue()*(Math.random()*(1.02-0.98)+0.98);}
			case 387:  { return (400*engine.getDouble("MonsterLv").intValue()*0.017*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*(1-engine.getDouble("IsDbPoint").intValue())+400*engine.getDouble("MonsterLv").intValue()*0.13*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 59:  { return 0.5+0.001*(engine.getDouble("skilllevel").intValue()-aimf.getEffectRole().getLevel());}
			case 467:  { return engine.getDouble("_94024_")>=1;}
			case 217:  { return 10+2.4*engine.getDouble("gradea").intValue();}
			case 199:  { return -(opf.getEffectRole().getAttrById(130)*1.6-Math.min(aimf.getEffectRole().getAttrById(140),aimf.getEffectRole().getAttrById(160))+1*engine.getDouble("skilllevel").intValue());}
			case 39:  { return -(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue())*(0.45+0.05*engine.getDouble("skilllevel").intValue());}
			case 393:  { return 400*engine.getDouble("RoleLv").intValue()*0.656*(0.82+0.04*((engine.getDouble("Ring").intValue()-1)%8+1))*(0.58+0.04*(Math.floor((engine.getDouble("Ring").intValue()-1)/8)+1));}
			case 209:  { return -(opf.getEffectRole().getAttrById(130)*0.95-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+Math.max(0,opf.getEffectRole().getAttrById(200)-aimf.getEffectRole().getAttrById(200))*0.5);}
			case 138:  { return engine.getDouble("skilllevel").intValue()*1;}
			case 69:  { return engine.getDouble("survivala").intValue()<engine.getDouble("survivalb").intValue();}
			case 529:  { return engine.getDouble("_95001_")>=1;}
			case 294:  { return 9.5*engine.getDouble("RoleLv").intValue();}
			case 603:  { return engine.getDouble("_96103_")>=1;}
			case 519:  { return engine.getDouble("_94044_")>=1&&engine.getDouble("_94057_")<1;}
			case 122:  { return -(opf.getEffectRole().getAttrById(130)*1.2-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 645:  { return engine.getDouble("_96401_")==1;}
			case 224:  { return opf.getEffectRole().getAttrById(60)*0.08;}
			case 636:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96359_")<1;}
			case 350:  { return (1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*5);}
			case 326:  { return (1*engine.getDouble("RoleLv").intValue()*8)*(Math.random()*(1.02-0.98)+0.98);}
			case 517:  { return engine.getDouble("_94030_")==2;}
			case 732:  { return 0*engine.getDouble("TeamNum").intValue()+5;}
			case 134:  { return -(opf.getEffectRole().getAttrById(150)*0.9-aimf.getEffectRole().getAttrById(160)+3*engine.getDouble("skilllevel").intValue());}
			case 681:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99012_")>=1;}
			case 94:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),4);}
			case 673:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99004_")==1;}
			case 52:  { return engine.getDouble("skilllevel").intValue()>=60;}
			case 401:  { return (400*engine.getDouble("RoleLv").intValue()*1.099*0.6);}
			case 203:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*(0.5+0.05*(3-engine.getDouble("preaimcount").intValue()))*1.5;}
			case 50:  { return 3*engine.getDouble("skilllevel").intValue();}
			case 453:  { return 5000*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.058*(0.5+0.1*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 333:  { return 1*engine.getDouble("RoleLv").intValue()*0.667*2*(3.5+0.5*(Math.floor((engine.getDouble("Time").intValue()-1)/5)+1))*Math.floor(1-(engine.getDouble("Time").intValue()%5)*0.2)*(Math.random()*(1.02-0.98)+0.98);}
			case 195:  { return -(opf.getEffectRole().getAttrById(130)*1.05-Math.min(aimf.getEffectRole().getAttrById(140),aimf.getEffectRole().getAttrById(160))+1*engine.getDouble("skilllevel").intValue());}
			case 221:  { return 4*engine.getDouble("skilllevel").intValue();}
			case 342:  { return (400*engine.getDouble("RoleLv").intValue()*1.099*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 10:  { return engine.getDouble("quality").intValue()*3*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 654:  { return engine.getDouble("_96202_")>=4;}
			case 253:  { return (1000*engine.getDouble("MonsterLv").intValue()*0.1)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 633:  { return engine.getDouble("_96352_")>=1;}
			case 496:  { return engine.getDouble("_94016_")>=1;}
			case 216:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))+(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))*0.1;}
			case 355:  { return (400*engine.getDouble("RoleLv").intValue()*0.628*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 423:  { return 2200+Math.floor(engine.getDouble("RoleLv").intValue()/10)*440+(200+Math.floor(engine.getDouble("RoleLv").intValue()/10)*40*(engine.getDouble("Ring").intValue()-1));}
			case 93:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),3);}
			case 208:  { return -(opf.getEffectRole().getAttrById(130)*1.05-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+Math.max(0,opf.getEffectRole().getAttrById(200)-aimf.getEffectRole().getAttrById(200))*0.5);}
			case 547:  { return engine.getDouble("_95037_")>=2||engine.getDouble("_95040_")>=1;}
			case 555:  { return engine.getDouble("_95048_")>=1;}
			case 272:  { return 200*engine.getDouble("RoleLv").intValue();}
			case 407:  { return engine.getDouble("RoleLv").intValue()*100;}
			case 411:  { return (400*engine.getDouble("RoleLv").intValue()*0.5*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*0.5*(Math.random()*(1.02-0.98)+0.98);}
			case 100:  { return 10+2.4*engine.getDouble("skilllevel").intValue();}
			case 344:  { return 400*engine.getDouble("RoleLv").intValue()*0.875*(0.91+0.02*((engine.getDouble("Ring").intValue()-1)%8+1))*(0.58+0.04*(Math.floor((engine.getDouble("Ring").intValue()-1)/8)+1));}
			case 402:  { return (400*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*1*(0.7+0.1*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 240:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*0.6;}
			case 468:  { return engine.getDouble("_94029_")>=1;}
			case 64:  { return engine.getDouble("skilllevel").intValue()>=50;}
			case 290:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1)*(Math.random()*(1.02-0.98)+0.98)*0.5;}
			case 375:  { return 400*engine.getDouble("RoleLv").intValue()*1.215*(0.85+0.03*((engine.getDouble("Time").intValue()-1)%9+1))*(0.9+0.1*(Math.floor((engine.getDouble("Time").intValue()-1)/9)+1))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 262:  { return (1000*engine.getDouble("RoleLv").intValue()*0.215*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 13:  { return -engine.getDouble("quality").intValue()*3;}
			case 357:  { return (400*engine.getDouble("RoleLv").intValue()*0.678*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 269:  { return (1000*engine.getDouble("RoleLv").intValue()*0.2)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 182:  { return aimf.getEffectRole().getAttrById(90)*0.15+250;}
			case 198:  { return -(opf.getEffectRole().getAttrById(130)*1-Math.min(aimf.getEffectRole().getAttrById(140),aimf.getEffectRole().getAttrById(160))+1*engine.getDouble("skilllevel").intValue());}
			case 512:  { return engine.getDouble("_94025_")>=1&&engine.getDouble("_94030_")>=3;}
			case 598:  { return engine.getDouble("_96015_")==1;}
			case 261:  { return 1000*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.35*(0.75+0.05*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 63:  { return engine.getDouble("maindamage").intValue();}
			case 416:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),99)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 674:  { return engine.getDouble("_99003_")>=1&&engine.getDouble("_96124_")<=0.01;}
			case 518:  { return engine.getDouble("_94030_")==1;}
			case 659:  { return engine.getDouble("_96207_")>=4;}
			case 729:  { return 1*engine.getDouble("TeamNum").intValue()+4;}
			case 156:  { return -aimf.getEffectRole().getAttrById(80)*0.8;}
			case 166:  { return -(opf.getEffectRole().getAttrById(150)*2.2-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("gradea").intValue())*0.5;}
			case 730:  { return 0*engine.getDouble("TeamNum").intValue()+4;}
			case 488:  { return engine.getDouble("_94005_")>=1;}
			case 18:  { return engine.getDouble("quality").intValue()*12+150;}
			case 307:  { return (1*engine.getDouble("RoleLv").intValue()*1.256*2*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 313:  { return (1*engine.getDouble("RoleLv").intValue()*1.256*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 490:  { return engine.getDouble("_94007_")>=2&&engine.getDouble("_94011_")>=1;}
			case 153:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))*0.4;}
			case 651:  { return engine.getDouble("_97005_")==1;}
			case 492:  { return engine.getDouble("_94008_")>=1;}
			case 377:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.35*(0.75+0.05*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 386:  { return (1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*13.333/28.867*((engine.getDouble("Ring").intValue()-1)*0.08+1)+engine.getDouble("RoleLv").intValue()*50-1000);}
			case 542:  { return engine.getDouble("_95030_")>=1;}
			case 33:  { return Math.min(-2.4*engine.getDouble("gradea").intValue()+1.2*engine.getDouble("skilllevel").intValue(),0);}
			case 143:  { return -aimf.getEffectRole().getAttrById(80)*0.05;}
			case 152:  { return aimf.getEffectRole().getAttrById(60)*0.5;}
			case 667:  { return engine.getDouble("_96216_")<=3;}
			case 710:  { return (Boolean)engine.get("_503002_");}
			case 535:  { return engine.getDouble("_95017_")==1;}
			case 558:  { return engine.getDouble("_95051_")>=2;}
			case 38:  { return -((0.15+0.001*engine.getDouble("skilllevel").intValue())*aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 95:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),5);}
			case 623:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96309_")<1;}
			case 73:  { return 1.4*engine.getDouble("skilllevel").intValue();}
			case 74:  { return -aimf.getEffectRole().getAttrById(80);}
			case 72:  { return 2*engine.getDouble("skilllevel").intValue();}
			case 676:  { return engine.getDouble("_99032_")>=1;}
			case 239:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue());}
			case 701:  { return (Boolean)engine.get("_502003_")||(Boolean)engine.get("_506002_");}
			case 336:  { return 2.22*engine.getDouble("RoleLv").intValue()*(Math.random()*(1.02-0.98)+0.98);}
			case 105:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.25;}
			case 110:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*(0.55+0.05*(3-engine.getDouble("preaimcount").intValue()));}
			case 114:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*(0.5+0.05*(3-engine.getDouble("preaimcount").intValue()))*(opf.getBuffAgent().existBuff(506109)?((Math.random()*(1.20-1.1))+1.1):((Math.random()*(1.20-0.9))+0.9));}
			case 430:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),49)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),49)/10)*40*10))*2;}
			case 699:  { return !(Boolean)engine.get("_506003_");}
			case 149:  { return opf.getEffectRole().getAttrById(60)*1;}
			case 631:  { return engine.getDouble("_96304_")<1;}
			case 115:  { return -(opf.getEffectRole().getAttrById(150)*2.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.5*(opf.getBuffAgent().existBuff(506109)?((Math.random()*(1.20-1.1))+1.1):((Math.random()*(1.20-0.9))+0.9));}
			case 188:  { return Math.min(aimf.getEffectRole().getAttrById(60)*0.15,aimf.getEffectRole().getLevel()*12);}
			case 527:  { return engine.getDouble("_94043_")>=1;}
			case 641:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96364_")<1;}
			case 473:  { return engine.getDouble("_94035_")>=1;}
			case 228:  { return -0.7*engine.getDouble("skilllevel").intValue();}
			case 610:  { return engine.getDouble("_96111_")>=1;}
			case 579:  { return engine.getDouble("_95077_")>=2;}
			case 399:  { return engine.getDouble("StdExp").intValue()*7*2.86/168*8;}
			case 273:  { return (engine.getDouble("StdMoney").intValue()*2/15)*(Math.random()*(1.05-0.95)+0.95);}
			case 0:  { return -(Math.max(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue(),opf.getEffectRole().getAttrById(130)*0.1)+(opf.getBuffAgent().existBuff(509300)?(Math.min(Math.max(aimf.getEffectRole().getAttrById(140)-aimf.getEffectRole().getAttrById(130),((((boolean)engine.get("pve")))?(2*engine.getDouble("gradea").intValue()):(10))),4*engine.getDouble("gradea").intValue())):(0)));}
			case 131:  { return -0.05+0.1*engine.getDouble("skilllevel").intValue();}
			case 414:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.35*(0.75+0.05*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 531:  { return engine.getDouble("_95009_")==1;}
			case 700:  { return !(Boolean)engine.get("_509097_");}
			case 83:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))*0.5;}
			case 679:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99010_")>=1;}
			case 495:  { return engine.getDouble("_94013_")>=1;}
			case 250:  { return (1000*engine.getDouble("TeamLv").intValue()*0.075*(1-engine.getDouble("IsDbPoint").intValue())+1000*engine.getDouble("TeamLv").intValue()*0.12*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 297:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 218:  { return 0.5+0.001*(engine.getDouble("skilllevel").intValue()-aimf.getEffectRole().getLevel())+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820));}
			case 320:  { return (1*engine.getDouble("MonsterLv").intValue()*0.3)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 520:  { return engine.getDouble("_94044_")>=1&&engine.getDouble("_94057_")>=1;}
			case 315:  { return (1*engine.getDouble("RoleLv").intValue()*0.775*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 82:  { return 10+2*engine.getDouble("skilllevel").intValue();}
			case 187:  { return Math.min(aimf.getEffectRole().getAttrById(60)*0.25,aimf.getEffectRole().getLevel()*12);}
			case 434:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),79)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),79)/10)*40*10))*2;}
			case 343:  { return (400*engine.getDouble("RoleLv").intValue()*2.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 127:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("gradea").intValue())*0.5;}
			case 521:  { return engine.getDouble("_94037_")>=1;}
			case 690:  { return (Boolean)engine.get("_502002_")&&!(Boolean)engine.get("_502003_");}
			case 669:  { return engine.getDouble("_96218_")>=1;}
			case 376:  { return (400*engine.getDouble("MonsterLv").intValue()*0.011*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*(1-engine.getDouble("IsDbPoint").intValue())+400*engine.getDouble("MonsterLv").intValue()*0.13*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 471:  { return engine.getDouble("_94046_")<1&&engine.getDouble("_94045_")>=3;}
			case 604:  { return engine.getDouble("_96108_")>=1;}
			case 225:  { return opf.getEffectRole().getAttrById(150)*0.1;}
			case 118:  { return 2.1*engine.getDouble("skilllevel").intValue();}
			case 46:  { return 1*engine.getDouble("skilllevel").intValue();}
			case 594:  { return engine.getDouble("_96001_")/engine.getDouble("_96002_")<0.2;}
			case 305:  { return engine.getDouble("StdMoney").intValue()*0.4*((engine.getDouble("Ring").intValue()-1)*0.09+1);}
			case 380:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*6.7);}
			case 640:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96363_")<1;}
			case 736:  { return 0*engine.getDouble("TeamNum").intValue()+2;}
			case 84:  { return -(opf.getEffectRole().getAttrById(150)*2-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.5;}
			case 19:  { return engine.getDouble("quality").intValue()*5+50;}
			case 12:  { return 100*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 25:  { return engine.getDouble("skilllevel").intValue()>=40;}
			case 696:  { return (Boolean)engine.get("_120_");}
			case 426:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),69)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),69)/10)*40*10))*2;}
			case 113:  { return 0.15+0.1*engine.getDouble("skilllevel").intValue();}
			case 509:  { return engine.getDouble("_94021_")>=1;}
			case 404:  { return engine.getDouble("StdExp").intValue()*5/10*(Math.random()*(1.2-0.8)+0.8);}
			case 718:  { return !(Boolean)engine.get("_510139_");}
			case 200:  { return 0.6*engine.getDouble("skilllevel").intValue();}
			case 445:  { return (5000*engine.getDouble("MonsterLv").intValue()*0.05)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 630:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96366_")<1;}
			case 109:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("gradea").intValue())*0.5*(1.2+(0.6+0.2*engine.getDouble("skilllevel").intValue())*(1-aimf.getEffectRole().getAttrById(100)/aimf.getEffectRole().getAttrById(90)));}
			case 433:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),89)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),89)/10)*40*10))*2;}
			case 595:  { return engine.getDouble("_96004_")>=4;}
			case 329:  { return (1*engine.getDouble("RoleLv").intValue()*0.717*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 219:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*(0.55+0.05*(4-engine.getDouble("preaimcount").intValue()));}
			case 398:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),94)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 227:  { return Math.max((opf.getEffectRole().getAttrById(150)*2.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.5*(opf.getBuffAgent().existBuff(506109)?((Math.random()*(1.20-1.1))+1.1):((Math.random()*(1.20-0.9))+0.9)),3*engine.getDouble("skilllevel").intValue());}
			case 245:  { return Math.round((Math.pow(1.02,engine.getDouble("skilllevel").intValue())-1)*1000);}
			case 360:  { return (400*engine.getDouble("RoleLv").intValue()*0.694*(0.78+0.04*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 571:  { return engine.getDouble("_95063_")==1;}
			case 75:  { return 6*engine.getDouble("skilllevel").intValue();}
			case 91:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),1);}
			case 296:  { return (1000*engine.getDouble("RoleLv").intValue()*0.2*(0.78+0.04*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 397:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.0392*(0.4+0.1*engine.getDouble("Saveid").intValue());}
			case 499:  { return engine.getDouble("_94017_")<1;}
			case 441:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+1.5*engine.getDouble("IsDbPoint").intValue()+engine.getDouble("IsSerMul").intValue())*(5000*engine.getDouble("RoleLv").intValue()*1.25/50);}
			case 408:  { return (400*engine.getDouble("RoleLv").intValue()*1.5)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 497:  { return engine.getDouble("_94014_")>=1;}
			case 647:  { return engine.getDouble("_97001_")/engine.getDouble("_97002_")>=0.2;}
			case 29:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1);}
			case 365:  { return (400*engine.getDouble("RoleLv").intValue()*0.833)*(Math.random()*(1.02-0.98)+0.98);}
			case 422:  { return engine.getDouble("Ring").intValue()+1;}
			case 697:  { return (Boolean)engine.get("_500033_");}
			case 395:  { return engine.getDouble("StdExp").intValue()*0.83/14.5*((engine.getDouble("Ring").intValue()-1)*0.1+1);}
			case 724:  { return !(Boolean)engine.get("_502002_");}
			case 70:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 309:  { return (1*engine.getDouble("RoleLv").intValue()*0.775*2*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 99:  { return -(opf.getEffectRole().getAttrById(150)*1+opf.getEffectRole().getAttrById(160)*1-aimf.getEffectRole().getAttrById(160)+1*engine.getDouble("gradea").intValue());}
			case 85:  { return -0.3*engine.getDouble("skilllevel").intValue();}
			case 628:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96364_")<1;}
			case 327:  { return (1*engine.getDouble("RoleLv").intValue()*0.717*2*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 454:  { return 5000*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.058*(0.75+0.05*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 565:  { return engine.getDouble("_95052_")>=1;}
			case 605:  { return engine.getDouble("_96106_")>=1;}
			case 135:  { return -(opf.getEffectRole().getAttrById(130)*0.5+opf.getEffectRole().getAttrById(140)*0.6-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 150:  { return aimf.getEffectRole().getAttrById(60);}
			case 288:  { return 3000+5000*Math.random();}
			case 462:  { return Math.min(Math.max(engine.getDouble("rolenum").intValue()*0.004,8),16);}
			case 180:  { return aimf.getEffectRole().getAttrById(60)*0.09+600;}
			case 656:  { return engine.getDouble("_96204_")>=1;}
			case 378:  { return (400*engine.getDouble("RoleLv").intValue()*1.099*3)*(Math.random()*(1.02-0.98)+0.98);}
			case 35:  { return -(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 678:  { return engine.getDouble("_99034_")>=1;}
			case 439:  { return (5000*engine.getDouble("RoleLv").intValue()*0.075)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 5:  { return engine.getDouble("quality").intValue();}
			case 62:  { return ((opf.getEffectRole().getAttrById(170)+3*engine.getDouble("skilllevel").intValue())*0.5+Math.abs(engine.getDouble("maindamage").intValue())*0.5)*(1+opf.getEffectRole().getAttrById(790))*(1+opf.getEffectRole().getAttrById(990)/1000);}
			case 339:  { return 1.38*engine.getDouble("RoleLv").intValue()*(Math.random()*(1.02-0.98)+0.98);}
			case 616:  { return engine.getDouble("_96122_")<1;}
			case 136:  { return -(opf.getEffectRole().getAttrById(150)*1-aimf.getEffectRole().getAttrById(160)+3*engine.getDouble("skilllevel").intValue());}
			case 330:  { return (1*engine.getDouble("RoleLv").intValue()*5)*(Math.random()*(1.02-0.98)+0.98);}
			case 569:  { return engine.getDouble("_95062_")==1;}
			case 158:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)*0.9+2*engine.getDouble("skilllevel").intValue());}
			case 489:  { return engine.getDouble("_94006_")<3;}
			case 658:  { return engine.getDouble("_96206_")>=4;}
			case 704:  { return !(Boolean)engine.get("_501004_");}
			case 90:  { return 2+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1);}
			case 179:  { return aimf.getEffectRole().getAttrById(60)*0.06+400;}
			case 132:  { return (opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.38*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)*(0.3+0.05*engine.getDouble("skilllevel").intValue())):(0.6*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)*(0.3+0.05*engine.getDouble("skilllevel").intValue()));}
			case 622:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96308_")<1;}
			case 185:  { return -Math.max(opf.getEffectRole().getAttrById(80)-opf.getEffectRole().getAttrById(60)*0.1,0);}
			case 607:  { return engine.getDouble("_96105_")>=1;}
			case 417:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),84)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 556:  { return engine.getDouble("_95049_")>=2||engine.getDouble("_95054_")>=1;}
			case 275:  { return (1000*engine.getDouble("MonsterLv").intValue()*0.019*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 405:  { return (400*engine.getDouble("RoleLv").intValue()*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 348:  { return (400*engine.getDouble("RoleLv").intValue()*0.628*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1))*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 101:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*(0.5+0.05*(4-engine.getDouble("preaimcount").intValue()));}
			case 351:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*6.6667/8*((engine.getDouble("Ring").intValue()-1)*0.3+1));}
			case 160:  { return engine.getDouble("gradea").intValue()*0.4;}
			case 677:  { return engine.getDouble("_99033_")>=1;}
			case 87:  { return -1.4*engine.getDouble("skilllevel").intValue();}
			case 213:  { return 5*engine.getDouble("skilllevel").intValue();}
			case 572:  { return engine.getDouble("_95057_")<1;}
			case 89:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)));}
			case 274:  { return (engine.getDouble("StdMoney").intValue()*4.2/8*((engine.getDouble("Ring").intValue()-1)*0.3+1))*(Math.random()*(1.05-0.95)+0.95);}
			case 599:  { return engine.getDouble("_96016_")>=1||engine.getDouble("_96018_")>=1;}
			case 314:  { return (1*engine.getDouble("TeamLv").intValue()*0.196*(0.78+0.04*engine.getDouble("Ring").intValue())*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 36:  { return 4+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1);}
			case 510:  { return engine.getDouble("_94022_")>=1;}
			case 26:  { return -(opf.getEffectRole().getAttrById(130)*0.75-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 625:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96311_")<1;}
			case 302:  { return (1+14*engine.getDouble("IsDbPoint").intValue()+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdMoney").intValue()*1.5/74*((engine.getDouble("Ring").intValue()-1)*0.09+1))*(Math.random()*(1.2-0.8)+0.8);}
			case 593:  { return engine.getDouble("_96001_")/engine.getDouble("_96002_")>=0.2;}
			case 626:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96362_")<1;}
			case 515:  { return engine.getDouble("_94030_")>=3;}
			case 192:  { return 10+1.2*engine.getDouble("gradea").intValue();}
			case 335:  { return (1*engine.getDouble("RoleLv").intValue()*4)*(Math.random()*(1.02-0.98)+0.98);}
			case 204:  { return ((opf.getEffectRole().getAttrById(170)+3*engine.getDouble("skilllevel").intValue())*0.25+Math.abs(engine.getDouble("maindamage").intValue())*0.25)*(1+opf.getEffectRole().getAttrById(790))*(1+opf.getEffectRole().getAttrById(990)/1000);}
			case 232:  { return -(opf.getEffectRole().getAttrById(130)*0.75-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 523:  { return engine.getDouble("_94028_")>=1;}
			case 268:  { return (1000*engine.getDouble("TeamLv").intValue()*0.038*(0.78+0.04*engine.getDouble("Ring").intValue())*(1-engine.getDouble("IsDbPoint").intValue())+1000*engine.getDouble("TeamLv").intValue()*0.098*(0.78+0.04*engine.getDouble("Ring").intValue())*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 692:  { return (Boolean)engine.get("_509082_")||(Boolean)engine.get("_509083_");}
			case 661:  { return engine.getDouble("_96209_")>=1;}
			case 642:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96365_")<1;}
			case 472:  { return engine.getDouble("_94031_")>=3;}
			case 533:  { return engine.getDouble("_95010_")>=1;}
			case 606:  { return engine.getDouble("_96104_")>=1;}
			case 141:  { return -(opf.getEffectRole().getAttrById(150)*1.1-aimf.getEffectRole().getAttrById(160)+3*engine.getDouble("skilllevel").intValue());}
			case 566:  { return engine.getDouble("_95060_")>=1;}
			case 664:  { return engine.getDouble("_96212_")>=1;}
			case 81:  { return 7*engine.getDouble("skilllevel").intValue();}
			case 148:  { return -aimf.getEffectRole().getAttrById(60)*2.5;}
			case 721:  { return (Boolean)engine.get("_501010_");}
			case 436:  { return (5000*engine.getDouble("RoleLv").intValue()*0.111)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 568:  { return engine.getDouble("_95057_")==1;}
			case 8:  { return (engine.getDouble("quality").intValue()*12+150)*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 71:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)*0.9+1*engine.getDouble("skilllevel").intValue());}
			case 493:  { return engine.getDouble("_94009_")<1;}
			case 212:  { return opf.getEffectRole().getAttrById(200);}
			case 14:  { return 200*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 37:  { return -((0.15+0.001*engine.getDouble("skilllevel").intValue())*aimf.getEffectRole().getAttrById(160)+1*engine.getDouble("skilllevel").intValue());}
			case 108:  { return opf.getEffectRole().getAttrById(170)+1*engine.getDouble("skilllevel").intValue();}
			case 285:  { return (1000*engine.getDouble("RoleLv").intValue()*0.232*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 24:  { return -(opf.getEffectRole().getAttrById(130)*0.9-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 657:  { return engine.getDouble("_96205_")>=1;}
			case 23:  { return -(opf.getEffectRole().getAttrById(130)*1.05-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 354:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*6.7/10)*((engine.getDouble("TeamNum").intValue()-1)*0.05+1);}
			case 694:  { return !(Boolean)engine.get("_504002_");}
			case 9:  { return (engine.getDouble("quality").intValue()*5+50)*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 476:  { return engine.getDouble("_94040_")<1;}
			case 665:  { return engine.getDouble("_96213_")>=3;}
			case 291:  { return 250*engine.getDouble("RoleLv").intValue()*(Math.random()*(1.02-0.98)+0.98);}
			case 214:  { return 10+2*engine.getDouble("gradea").intValue();}
			case 646:  { return engine.getDouble("_96402_")>=1||engine.getDouble("_96403_")>=1;}
			case 226:  { return -(opf.getEffectRole().getAttrById(150)*2.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.5*(opf.getBuffAgent().existBuff(506109)?((Math.random()*(1.20-1.1))+1.1):((Math.random()*(1.20-0.9))+0.9))*2;}
			case 564:  { return engine.getDouble("_95059_")>=1;}
			case 60:  { return 0.25+0.001*(engine.getDouble("skilllevel").intValue()-aimf.getEffectRole().getLevel());}
			case 172:  { return engine.getDouble("maindamage").intValue()*0.33*((Math.random()*(1.05-0.95))+0.95);}
			case 22:  { return 10+1.2*engine.getDouble("skilllevel").intValue();}
			case 45:  { return 10*engine.getDouble("skilllevel").intValue();}
			case 251:  { return (15+Math.random()*5)*engine.getDouble("RoleLv").intValue();}
			case 300:  { return (engine.getDouble("StdMoney").intValue()*2)*(Math.random()*(1.05-0.95)+0.95);}
			case 553:  { return engine.getDouble("_95046_")<=2;}
			case 419:  { return (400*engine.getDouble("RoleLv").intValue()*0.25*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*0.5*(Math.random()*(1.02-0.98)+0.98);}
			case 79:  { return -(opf.getEffectRole().getAttrById(130)*0.9-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 466:  { return engine.getDouble("_94023_")>=1;}
			case 469:  { return engine.getDouble("_94033_")>=2;}
			case 252:  { return (1000*engine.getDouble("RoleLv").intValue()*0.232*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1))*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 506:  { return opf.getFighterBean().getInitattrs().get(1010)>=4;}
			case 372:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),69)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 588:  { return engine.getDouble("_95085_")>=2&&engine.getDouble("_95089_")<1;}
			case 530:  { return engine.getDouble("_95002_")==1;}
			case 379:  { return (400*engine.getDouble("RoleLv").intValue()*1.25)*(Math.random()*(1.02-0.98)+0.98);}
			case 20:  { return engine.getDouble("skilllevel").intValue()*10;}
			case 481:  { return engine.getDouble("_94026_")>=1;}
			case 618:  { return engine.getDouble("_99002_")<1;}
			case 266:  { return 500*engine.getDouble("RoleLv").intValue();}
			case 125:  { return Math.abs(engine.getDouble("maindamage").intValue()*0.5);}
			case 206:  { return 0.3*engine.getDouble("skilllevel").intValue();}
			case 574:  { return engine.getDouble("_95065_")>=3;}
			case 170:  { return -(opf.getEffectRole().getAttrById(130)*1.05-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 465:  { return Math.floor((Math.min(Math.max(Math.floor((engine.getDouble("ServerLv").intValue()-40)*0.2),0),4)*3+6)*Math.min(Math.max(engine.getDouble("rolenum").intValue()*0.0005,1),2));}
			case 464:  { return null;}
			case 563:  { return engine.getDouble("_95058_")>=1;}
			case 142:  { return -opf.getEffectRole().getAttrById(80)*1;}
			case 666:  { return engine.getDouble("_96215_")>=4;}
			case 415:  { return (400*engine.getDouble("RoleLv").intValue()*1)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 612:  { return engine.getDouble("_96102_")>=1;}
			case 184:  { return Math.min(aimf.getEffectRole().getAttrById(60)*0.50,aimf.getEffectRole().getLevel()*30);}
			case 503:  { return engine.getDouble("_94019_")>=1;}
			case 111:  { return opf.getEffectRole().getAttrById(170)+3*engine.getDouble("skilllevel").intValue();}
			case 691:  { return !(Boolean)engine.get("_13_");}
			case 215:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))+0.05+(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))*0.1;}
			case 545:  { return engine.getDouble("_95039_")<1;}
			case 220:  { return opf.getEffectRole().getAttrById(170)+3*engine.getDouble("skilllevel").intValue()+((((boolean)engine.get("pve")))?(1*engine.getDouble("skilllevel").intValue()):(0));}
			case 723:  { return (Boolean)engine.get("_509082_")||(Boolean)engine.get("_509083_")||(Boolean)engine.get("_506201_")||(Boolean)engine.get("_509068_")||(Boolean)engine.get("_509031_");}
			case 573:  { return engine.getDouble("_95064_")>=2;}
			case 648:  { return engine.getDouble("_97001_")/engine.getDouble("_97002_")<0.2;}
			case 32:  { return -(opf.getEffectRole().getAttrById(130)*1.25-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 173:  { return -(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue()+Math.max((opf.getEffectRole().getAttrById(130)-aimf.getEffectRole().getAttrById(130))*0.25,0));}
			case 382:  { return (400*engine.getDouble("RoleLv").intValue()*1.099*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1))*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 257:  { return 1000*engine.getDouble("RoleLv").intValue()*0.067*(0.74+0.02*engine.getDouble("Time").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 44:  { return 0.5*engine.getDouble("skilllevel").intValue();}
			case 451:  { return (5000*engine.getDouble("RoleLv").intValue()*0.049*1.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 728:  { return engine.getDouble("TeamLv").intValue();}
			case 578:  { return engine.getDouble("_95046_")<=1;}
			case 637:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96360_")<1;}
			case 92:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),2);}
			case 448:  { return (5000*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*0.2*(0.7+0.1*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 663:  { return engine.getDouble("_96211_")>=1;}
			case 49:  { return 10+1.4*engine.getDouble("skilllevel").intValue();}
			case 507:  { return opf.getFighterBean().getInitattrs().get(1010)>=5;}
			case 292:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1*(0.89+0.02*engine.getDouble("AnswerCnt").intValue()))*0.5*(Math.random()*(1.02-0.98)+0.98);}
			case 591:  { return engine.getDouble("_95057_")>=1;}
			case 459:  { return (5000*engine.getDouble("RoleLv").intValue()*0.15)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 107:  { return randint(1,2);}
			case 306:  { return (1*engine.getDouble("RoleLv").intValue()*1.333)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 353:  { return (400*engine.getDouble("RoleLv").intValue()*0.678*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 116:  { return -(opf.getEffectRole().getAttrById(150)*2.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.5*(opf.getBuffAgent().existBuff(506109)?((Math.random()*(1.20-1.1))+1.1):((Math.random()*(1.20-0.9))+0.9))*(1+(0.4+0.1*engine.getDouble("skilllevel").intValue())*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 301:  { return 100*engine.getDouble("RoleLv").intValue();}
			case 384:  { return 400*engine.getDouble("RoleLv").intValue()*1.215*(0.88+0.03);}
			case 48:  { return 14*engine.getDouble("skilllevel").intValue();}
			case 31:  { return -(opf.getEffectRole().getAttrById(130)*1.15-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 42:  { return (opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))<=0.4;}
			case 316:  { return (1*engine.getDouble("RoleLv").intValue()*0.775*2*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 725:  { return (Boolean)engine.get("_110_")||(Boolean)engine.get("_120_");}
			case 323:  { return (1*engine.getDouble("RoleLv").intValue()*1)*(Math.random()*(1.02-0.98)+0.98);}
			case 61:  { return -Math.min(0.1*aimf.getEffectRole().getAttrById(80),10*engine.getDouble("skilllevel").intValue())-3*engine.getDouble("skilllevel").intValue();}
			case 259:  { return (1000*engine.getDouble("RoleLv").intValue()*0.377*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1))*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 425:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)/10)*40*engine.getDouble("Saveid").intValue()))*2;}
			case 609:  { return engine.getDouble("_96110_")>=1;}
			case 128:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*(0.3+0.05*engine.getDouble("skilllevel").intValue())*0.5;}
			case 442:  { return (5000*engine.getDouble("MonsterLv").intValue()*0.011*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*(1-engine.getDouble("IsDbPoint").intValue())+5000*engine.getDouble("MonsterLv").intValue()*0.02*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 123:  { return opf.getFighterBean().getInitattrs().get(1010)<2;}
			case 155:  { return -opf.getEffectRole().getAttrById(80)*0.8;}
			case 516:  { return engine.getDouble("_94030_")==2&&engine.getDouble("_94029_")>=1;}
			case 385:  { return (400*engine.getDouble("RoleLv").intValue()*1)*(Math.random()*(1.02-0.98)+0.98);}
			case 634:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96357_")<1;}
			case 581:  { return engine.getDouble("_95080_")==1;}
			case 265:  { return (1000*engine.getDouble("RoleLv").intValue()*0.215*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1))*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 528:  { return engine.getDouble("_94044_")>=1;}
			case 119:  { return -Math.min(0.1*aimf.getEffectRole().getAttrById(80),20*engine.getDouble("skilllevel").intValue());}
			case 485:  { return engine.getDouble("_94004_")>=1;}
			case 6:  { return engine.getDouble("quality").intValue()*0.4+10;}
			case 655:  { return engine.getDouble("_96203_")>=4;}
			case 248:  { return (1000*engine.getDouble("RoleLv").intValue()*0.377*3)*(Math.random()*(1.02-0.98)+0.98);}
			case 186:  { return -opf.getEffectRole().getAttrById(100);}
			case 412:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*6);}
			case 532:  { return engine.getDouble("_95013_")==1;}
			case 560:  { return engine.getDouble("_95053_")==1;}
			case 632:  { return engine.getDouble("_96351_")>=1;}
			case 312:  { return (1*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*5)*(Math.random()*(1.02-0.98)+0.98);}
			case 617:  { return engine.getDouble("_96123_")>=1;}
			case 124:  { return opf.getFighterBean().getInitattrs().get(1010)>=2;}
			case 201:  { return 10+1.4*engine.getDouble("gradea").intValue();}
			case 373:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),49)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 668:  { return engine.getDouble("_96217_")<3;}
			case 687:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99018_")>=1;}
			case 168:  { return engine.getDouble("gradea").intValue()>=60;}
			case 191:  { return engine.getDouble("gradea").intValue()*1.5;}
			case 675:  { return engine.getDouble("_99031_")>=1;}
			case 130:  { return -(opf.getEffectRole().getAttrById(130)*0.4+opf.getEffectRole().getAttrById(140)*0.5-aimf.getEffectRole().getAttrById(140)+2*engine.getDouble("gradea").intValue());}
			case 711:  { return (Boolean)engine.get("_504003_");}
			case 278:  { return (engine.getDouble("StdMoney").intValue()*2/10)*(Math.random()*(1.05-0.95)+0.95);}
			case 4:  { return engine.getDouble("quality").intValue()*32+1000;}
			case 144:  { return -aimf.getEffectRole().getAttrById(80)*0.1;}
			case 437:  { return (5000*engine.getDouble("TeamLv").intValue()*0.025*(0.78+0.04*engine.getDouble("Ring").intValue())*(1-engine.getDouble("IsDbPoint").intValue())+5000*engine.getDouble("TeamLv").intValue()*0.102*(0.78+0.04*engine.getDouble("Ring").intValue())*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 584:  { return engine.getDouble("_95082_")>=2;}
			case 735:  { return 0*engine.getDouble("TeamNum").intValue()+8+2*Math.random();}
			case 334:  { return engine.getDouble("MonsterLv").intValue()+20;}
			case 413:  { return (400*engine.getDouble("RoleLv").intValue()*0.7)*(Math.random()*(1.02-0.98)+0.98);}
			case 621:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96307_")<1;}
			case 539:  { return engine.getDouble("_95024_")<=0;}
			case 720:  { return !(Boolean)engine.get("_506201_");}
			case 589:  { return engine.getDouble("_95086_")>=1&&engine.getDouble("_95087_")<1;}
			case 76:  { return -(opf.getEffectRole().getAttrById(130)*0.95-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 311:  { return (1*engine.getDouble("TeamLv").intValue()*0.239*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 562:  { return engine.getDouble("_95055_")>=1;}
			case 649:  { return engine.getDouble("_97003_")>=1;}
			case 281:  { return (1000*engine.getDouble("RoleLv").intValue()*0.3)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 120:  { return -Math.min(0.05*aimf.getEffectRole().getAttrById(100),10*engine.getDouble("skilllevel").intValue());}
			case 682:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99013_")>=1;}
			case 346:  { return (400*engine.getDouble("TeamLv").intValue()*0.15*(0.78+0.04*engine.getDouble("Ring").intValue())*(1-engine.getDouble("IsDbPoint").intValue())+400*engine.getDouble("TeamLv").intValue()*1.02*(0.78+0.04*engine.getDouble("Ring").intValue())*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 263:  { return (1000*engine.getDouble("RoleLv").intValue()*0.5)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 712:  { return !(Boolean)engine.get("_506109_");}
			case 693:  { return (Boolean)engine.get("_504002_");}
			case 139:  { return engine.getDouble("skilllevel").intValue()*50;}
			case 450:  { return (5000*engine.getDouble("RoleLv").intValue()*0.05)*(Math.random()*(1.02-0.98)+0.98)*0.5;}
			case 277:  { return 4000+6000*Math.random();}
			case 429:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),74)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),74)/10)*40*10))*2;}
			case 328:  { return (1*engine.getDouble("RoleLv").intValue()*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 613:  { return engine.getDouble("_96113_")<1;}
			case 639:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96362_")<1;}
			case 494:  { return engine.getDouble("_94012_")>=2;}
			case 340:  { return Math.max(Math.floor((engine.getDouble("Ring").intValue()-4)/2),0);}
			case 363:  { return (1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*0.83/14.5*((engine.getDouble("Ring").intValue()-1)*0.1+1));}
			case 567:  { return engine.getDouble("_95018_")==1;}
			case 592:  { return engine.getDouble("_95088_")<=0;}
			case 151:  { return -aimf.getEffectRole().getAttrById(60)*0.5;}
			case 159:  { return engine.getDouble("gradea").intValue()*0.8;}
			case 140:  { return engine.getDouble("skilllevel").intValue()*8;}
			case 162:  { return engine.getDouble("gradea").intValue()*1.6;}
			case 549:  { return engine.getDouble("_95042_")>=1;}
			case 629:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96365_")<1;}
			case 418:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),59)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 582:  { return engine.getDouble("_95079_")<1;}
			case 194:  { return opf.getEffectRole().getAttrById(130)*0.1;}
			case 575:  { return engine.getDouble("_95074_")==1;}
			case 534:  { return engine.getDouble("_95003_")<=1;}
			case 317:  { return (1*engine.getDouble("RoleLv").intValue()*6.66)*(Math.random()*(1.02-0.98)+0.98);}
			case 505:  { return opf.getFighterBean().getInitattrs().get(1010)>=3;}
			case 394:  { return (400*engine.getDouble("RoleLv").intValue()*1.215*1.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 438:  { return (5000*engine.getDouble("RoleLv").intValue()*0.05*(0.78+0.04*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 702:  { return (Boolean)engine.get("_505005_")||(Boolean)engine.get("_504011_");}
			case 662:  { return engine.getDouble("_96210_")<3;}
			case 370:  { return (105+Math.random()*30)*engine.getDouble("RoleLv").intValue();}
			case 369:  { return (400*engine.getDouble("RoleLv").intValue()*0.25*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 557:  { return engine.getDouble("_95050_")>=1;}
			case 41:  { return (opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))<=0.6;}
			case 719:  { return !(Boolean)engine.get("_506306_");}
			case 475:  { return engine.getDouble("_94036_")>=2;}
			case 526:  { return engine.getDouble("_94044_")>=3;}
			case 602:  { return engine.getDouble("_96107_")>=1;}
			case 88:  { return Math.min(-1*engine.getDouble("gradea").intValue()+0.5*engine.getDouble("skilllevel").intValue(),0);}
			case 196:  { return -(opf.getEffectRole().getAttrById(130)*1.15-Math.min(aimf.getEffectRole().getAttrById(140),aimf.getEffectRole().getAttrById(160))+1*engine.getDouble("skilllevel").intValue());}
			case 271:  { return (engine.getDouble("StdMoney").intValue()*6/10)*(Math.random()*(1.05-0.95)+0.95);}
			case 680:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99011_")>=1;}
			case 78:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 58:  { return -Math.min(0.25*aimf.getEffectRole().getAttrById(80),50*engine.getDouble("skilllevel").intValue());}
			case 660:  { return engine.getDouble("_96208_")<3;}
			case 104:  { return -8*engine.getDouble("skilllevel").intValue();}
			case 501:  { return engine.getDouble("_94003_")>=1&&engine.getDouble("_94006_")>=3;}
			case 597:  { return engine.getDouble("_96124_")<=0.01;}
			case 390:  { return (400*engine.getDouble("RoleLv").intValue()*1.215*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 255:  { return (engine.getDouble("StdMoney").intValue()*3.15/8*((engine.getDouble("Ring").intValue()-1)*0.3+1))*(Math.random()*(1.05-0.95)+0.95);}
			case 7:  { return 400*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 103:  { return 0.6+0.002*(engine.getDouble("skilllevel").intValue()-aimf.getEffectRole().getLevel())+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820));}
			case 374:  { return (400*engine.getDouble("RoleLv").intValue()*1.25)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 164:  { return engine.getDouble("gradea").intValue()*1.3;}
			case 207:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)*0.9+1*engine.getDouble("skilllevel").intValue()+Math.max(0,opf.getEffectRole().getAttrById(200)-aimf.getEffectRole().getAttrById(200))*0.5);}
			case 504:  { return opf.getFighterBean().getInitattrs().get(1010)>=0;}
			case 65:  { return 0.5+((engine.getDouble("survivala").intValue()<engine.getDouble("survivalb").intValue())?(0.25):(0));}
			case 498:  { return engine.getDouble("_94015_")>=1;}
			case 15:  { return 150*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 432:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),84)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),84)/10)*40*10))*2;}
			case 714:  { return (Boolean)engine.get("_504013_");}
			case 381:  { return (400*engine.getDouble("MonsterLv").intValue()*0.667)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 508:  { return engine.getDouble("_94020_")>=1;}
			case 538:  { return engine.getDouble("_95021_")==1;}
			case 734:  { return 0*engine.getDouble("TeamNum").intValue()+6+2*Math.random();}
			case 460:  { return (1+engine.getDouble("IsSerMul").intValue())*(5000*engine.getDouble("RoleLv").intValue()*0.5/20);}
			case 286:  { return (1000*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*0.1*(0.7+0.1*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 608:  { return engine.getDouble("_96109_")>=1;}
			case 409:  { return (400*engine.getDouble("RoleLv").intValue()*0.75)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 672:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99030_")==1;}
			case 392:  { return (400*engine.getDouble("RoleLv").intValue()*2.222)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 707:  { return !(Boolean)engine.get("_13_")&&!(Boolean)engine.get("_501008_");}
			case 552:  { return engine.getDouble("_95045_")>=1;}
			case 670:  { return engine.getDouble("_96125_")>1;}
			case 525:  { return opf.getFighterBean().getInitattrs().get(1010)<1;}
			case 455:  { return 5000*engine.getDouble("RoleLv").intValue()*0.1*(0.74+0.02*engine.getDouble("Time").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 366:  { return (400*engine.getDouble("RoleLv").intValue()*1.215*1)*(Math.random()*(1.02-0.98)+0.98);}
			case 86:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))+0.05;}
			case 47:  { return -(opf.getEffectRole().getAttrById(130)*1.6-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 178:  { return aimf.getEffectRole().getAttrById(60)*0.03+200;}
			case 713:  { return (Boolean)engine.get("_13_");}
			case 53:  { return engine.getDouble("skilllevel").intValue()>=90;}
			case 500:  { return engine.getDouble("_94006_")<3&&engine.getDouble("_94005_")>=1;}
			case 163:  { return engine.getDouble("gradea").intValue()*0.6;}
			case 324:  { return (engine.getDouble("IsDbPoint").intValue()*(engine.getDouble("Ring").intValue()-1)*1+6);}
			case 129:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*(0.3+0.05*engine.getDouble("skilllevel").intValue())*0.5/(1-(((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*(0.3+0.05*engine.getDouble("skilllevel").intValue())*0.5);}
			case 289:  { return 1000*engine.getDouble("RoleLv").intValue()*0.194*(0.85+0.03*((engine.getDouble("Time").intValue()-1)%9+1))*(0.9+0.1*(Math.floor((engine.getDouble("Time").intValue()-1)/9)+1))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 689:  { return (Boolean)engine.get("_502002_");}
			case 264:  { return (engine.getDouble("StdMoney").intValue()*6/28.1*((engine.getDouble("Ring").intValue()-1)*0.09+1))*(Math.random()*(1.05-0.95)+0.95);}
			case 596:  { return engine.getDouble("_96003_")>=1;}
			case 183:  { return Math.min(aimf.getEffectRole().getAttrById(60)*0.25,aimf.getEffectRole().getLevel()*18);}
			case 254:  { return (1000*engine.getDouble("RoleLv").intValue()*0.215*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 121:  { return opf.getFighterBean().getInitattrs().get(1010)>=1;}
			case 587:  { return engine.getDouble("_95084_")<1;}
			case 310:  { return (1*engine.getDouble("RoleLv").intValue()*0.775*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 410:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*6.7/5);}
			case 28:  { return 0.6+0.002*(engine.getDouble("skilllevel").intValue()-aimf.getEffectRole().getLevel());}
			case 559:  { return engine.getDouble("_95052_")>=2;}
			case 40:  { return (opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))<=0.8;}
			case 470:  { return engine.getDouble("_94034_")<1;}
			case 145:  { return -aimf.getEffectRole().getAttrById(80)*0.2;}
			case 258:  { return (1000*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*0.2*(0.7+0.1*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 318:  { return (1*engine.getDouble("RoleLv").intValue()*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 590:  { return engine.getDouble("_95082_")>=3;}
			case 249:  { return (1000*engine.getDouble("RoleLv").intValue()*0.05*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 403:  { return (400*engine.getDouble("RoleLv").intValue()*0.5*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 246:  { return Math.round((1-Math.pow(0.98,engine.getDouble("skilllevel").intValue()))*1000);}
			case 522:  { return engine.getDouble("_94038_")>=2;}
			case 66:  { return engine.getDouble("skilllevel").intValue()>=2;}
			case 554:  { return engine.getDouble("_95047_")>=2;}
			case 34:  { return -Math.min((Math.random()*(0.13-0.07)+0.07)*opf.getEffectRole().getAttrById(60),opf.getEffectRole().getAttrById(80)-1);}
			case 171:  { return -(opf.getEffectRole().getAttrById(130)*0.75-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 287:  { return 1000*Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)*0.35*(0.5+0.1*engine.getDouble("Saveid").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 332:  { return engine.getDouble("RoleLv").intValue()*5;}
			case 420:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),79)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 428:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),94)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),94)/10)*40*10))*2;}
			case 235:  { return -(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 482:  { return engine.getDouble("_94001_")>=1;}
			case 236:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 267:  { return (1000*engine.getDouble("RoleLv").intValue()*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 321:  { return (1*engine.getDouble("RoleLv").intValue()*1.256*2*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 167:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("gradea").intValue())*(0.5+0.05*(2-engine.getDouble("preaimcount").intValue()));}
			case 502:  { return engine.getDouble("_94003_")>=1&&engine.getDouble("_94006_")<3;}
			case 54:  { return 4+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),1);}
			case 362:  { return 400*engine.getDouble("RoleLv").intValue()*1*(0.74+0.02*engine.getDouble("Time").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 726:  { return (Boolean)engine.get("_509201_");}
			case 189:  { return -(opf.getEffectRole().getAttrById(130)*1.6-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 614:  { return engine.getDouble("_96120_")>=1;}
			case 270:  { return (1000*engine.getDouble("RoleLv").intValue()*0.194*1)*(Math.random()*(1.02-0.98)+0.98);}
			case 457:  { return engine.getDouble("RoleLv").intValue()*100*30/5;}
			case 586:  { return engine.getDouble("_95083_")==1;}
			case 57:  { return 0.6+0.001*(engine.getDouble("skilllevel").intValue()-aimf.getEffectRole().getLevel());}
			case 359:  { return (400*engine.getDouble("TeamLv").intValue()*0.3*(1-engine.getDouble("IsDbPoint").intValue())+400*engine.getDouble("TeamLv").intValue()*1.244*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 177:  { return ((Math.random()*(5-3))+3);}
			case 391:  { return (400*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*2*(0.7+0.1*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 480:  { return engine.getDouble("_94054_")>=1;}
			case 652:  { return engine.getDouble("_96200_")>=4;}
			case 447:  { return (5000*engine.getDouble("RoleLv").intValue()*0.049*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 190:  { return engine.getDouble("maindamage").intValue()*((Math.random()*(1.05-0.95))+0.95);}
			case 540:  { return engine.getDouble("_95028_")==1;}
			case 577:  { return engine.getDouble("_95076_")==1;}
			case 551:  { return engine.getDouble("_95044_")>=1;}
			case 514:  { return engine.getDouble("_94025_")>=1&&engine.getDouble("_94030_")==1;}
			case 638:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96361_")<1;}
			case 231:  { return -(opf.getEffectRole().getAttrById(130)*0.9-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 406:  { return (400*engine.getDouble("RoleLv").intValue()*0.678*0.6);}
			case 126:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16;}
			case 102:  { return -(opf.getEffectRole().getAttrById(150)*2.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.5;}
			case 444:  { return (5000*engine.getDouble("RoleLv").intValue()*0.1)*(Math.random()*(1.02-0.98)+0.98);}
			case 474:  { return engine.getDouble("_94027_")>=1&&engine.getDouble("_94053_")<1;}
			case 117:  { return 4*engine.getDouble("skilllevel").intValue()*(1+opf.getEffectRole().getAttrById(790)+aimf.getEffectRole().getAttrById(800))*(1+opf.getEffectRole().getAttrById(990)/1000+aimf.getEffectRole().getAttrById(1000)/1000);}
			case 169:  { return -(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+3*engine.getDouble("gradea").intValue());}
			case 389:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*6.7/15);}
			case 695:  { return !(Boolean)engine.get("_120_");}
			case 356:  { return (400*engine.getDouble("RoleLv").intValue()*0.278*(0.78+0.04*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 146:  { return -aimf.getEffectRole().getAttrById(80)*0.5;}
			case 193:  { return opf.getEffectRole().getAttrById(60)*0.1;}
			case 703:  { return !(Boolean)engine.get("_501010_")&&!(Boolean)engine.get("_13_");}
			case 137:  { return aimf.getEffectRole().getAttrById(60)*1;}
			case 211:  { return -(opf.getEffectRole().getAttrById(130)*0.75-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+Math.max(0,opf.getEffectRole().getAttrById(200)-aimf.getEffectRole().getAttrById(200))*0.5);}
			case 230:  { return -(opf.getEffectRole().getAttrById(130)*1.05-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 67:  { return engine.getDouble("skilllevel").intValue()>=3;}
			case 260:  { return (engine.getDouble("StdMoney").intValue()*2/5)*(Math.random()*(1.05-0.95)+0.95);}
			case 684:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99015_")>=1;}
			case 238:  { return -(opf.getEffectRole().getAttrById(130)*1.2-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 331:  { return (engine.getDouble("MonsterLv").intValue()-30)*0.2+4;}
			case 733:  { return 1*engine.getDouble("TeamNum").intValue();}
			case 174:  { return engine.getDouble("maindamage").intValue()*0.2*((Math.random()*(1.05-0.95))+0.95);}
			case 427:  { return 2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenId").intValue()*10-1051-Math.floor(engine.getDouble("FuBenId").intValue()/113)*5)/10)*40*engine.getDouble("Saveid").intValue());}
			case 154:  { return ((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))*0.3;}
			case 620:  { return engine.getDouble("_96302_")>=1;}
			case 347:  { return (400*engine.getDouble("RoleLv").intValue()*0.678*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1))*0.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 308:  { return (1*engine.getDouble("RoleLv").intValue()*0.717*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 443:  { return 5000*engine.getDouble("RoleLv").intValue()*0.049*(0.85+0.03*((engine.getDouble("Time").intValue()-1)%9+1))*(0.9+0.1*(Math.floor((engine.getDouble("Time").intValue()-1)/9)+1))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 244:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*0.2;}
			case 421:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),89)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 237:  { return -(opf.getEffectRole().getAttrById(130)*1.15-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 175:  { return -engine.getDouble("maindamage").intValue();}
			case 452:  { return (5000*engine.getDouble("RoleLv").intValue()*0.049*1)*(Math.random()*(1.02-0.98)+0.98);}
			case 546:  { return engine.getDouble("_95038_")>=1;}
			case 77:  { return -(opf.getEffectRole().getAttrById(130)*0.85-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue());}
			case 477:  { return engine.getDouble("_94039_")>=1;}
			case 197:  { return -(opf.getEffectRole().getAttrById(130)*1.25-Math.min(aimf.getEffectRole().getAttrById(140),aimf.getEffectRole().getAttrById(160))+1*engine.getDouble("skilllevel").intValue());}
			case 576:  { return engine.getDouble("_95075_")==1;}
			case 21:  { return 2.5*engine.getDouble("skilllevel").intValue();}
			case 358:  { return (400*engine.getDouble("RoleLv").intValue()*0.017)*(Math.random()*(1.02-0.98)+0.98);}
			case 1:  { return -(opf.getEffectRole().getAttrById(130)*1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 431:  { return (2200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),99)/10)*440+(200+Math.floor(Math.min(engine.getDouble("RoleLv").intValue(),99)/10)*40*10))*2;}
			case 181:  { return aimf.getEffectRole().getAttrById(90)*0.1+150;}
			case 717:  { return !(Boolean)engine.get("_506101_");}
			case 400:  { return (175+Math.random()*50)*engine.getDouble("RoleLv").intValue();}
			case 683:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99014_")>=1;}
			case 106:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*0.1;}
			case 491:  { return engine.getDouble("_94007_")>=2;}
			case 619:  { return engine.getDouble("_96301_")>=1;}
			case 635:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96358_")<1;}
			case 11:  { return (engine.getDouble("quality").intValue()*5+100)*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 210:  { return -(opf.getEffectRole().getAttrById(130)*0.85-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+Math.max(0,opf.getEffectRole().getAttrById(200)-aimf.getEffectRole().getAttrById(200))*0.5);}
			case 349:  { return (400*engine.getDouble("RoleLv").intValue()*0.25*(0.89+0.02*engine.getDouble("AnswerCnt").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 96:  { return 3+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),6);}
			case 98:  { return ((boolean)engine.get("pve"));}
			case 364:  { return (400*engine.getDouble("RoleLv").intValue()*0.25*(0.89+0.02*engine.getDouble("AnswerCnt").intValue()))*0.5*(Math.random()*(1.02-0.98)+0.98);}
			case 653:  { return engine.getDouble("_96201_")>=4;}
			case 222:  { return 5+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/60),1)+Math.min(Math.floor(engine.getDouble("skilllevel").intValue()/90),1);}
			case 511:  { return engine.getDouble("_94025_")>=1&&engine.getDouble("_94029_")>=1;}
			case 388:  { return (400*engine.getDouble("RoleLv").intValue()*0.628*0.6);}
			case 68:  { return engine.getDouble("skilllevel").intValue()>=4;}
			case 256:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 295:  { return (1000*engine.getDouble("RoleLv").intValue()*0.4)*(Math.random()*(1.02-0.98)+0.98);}
			case 303:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1*(0.89+0.02*engine.getDouble("AnswerCnt").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 298:  { return (1+14*engine.getDouble("IsDbPoint").intValue()+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdMoney").intValue()*1.5/74*(14*0.09+1))*(Math.random()*(1.2-0.8)+0.8);}
			case 615:  { return engine.getDouble("_96121_")>=1;}
			case 715:  { return !(Boolean)engine.get("_501010_");}
			case 30:  { return (opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))>=0.5;}
			case 283:  { return 100*1;}
			case 352:  { return (400*engine.getDouble("RoleLv").intValue()*0.667)*(Math.random()*(1.02-0.98)+0.98)*0.5;}
			case 243:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*0.3;}
			case 247:  { return (1000*engine.getDouble("RoleLv").intValue()*0.1*(0.78+0.04*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98);}
			case 486:  { return engine.getDouble("_94005_")>=5;}
			case 16:  { return 300*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 202:  { return -Math.min(0.15*aimf.getEffectRole().getAttrById(80),15*engine.getDouble("skilllevel").intValue())-3*engine.getDouble("skilllevel").intValue();}
			case 479:  { return engine.getDouble("_94056_")>=2;}
			case 43:  { return (opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60))<=0.2;}
			case 17:  { return 250*(opf.getBuffAgent().existBuff(508042)?(1.2):(1));}
			case 112:  { return (opf.getEffectRole().getAttrById(170)+3*engine.getDouble("skilllevel").intValue())*0.4;}
			case 585:  { return engine.getDouble("_95086_")>=1;}
			case 338:  { return 3.4*engine.getDouble("RoleLv").intValue()*(Math.random()*(1.02-0.98)+0.98);}
			case 688:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99019_")>=1;}
			case 345:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+7*engine.getDouble("IsDbPoint").intValue()+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*10/63*(14*0.05+1));}
			case 548:  { return engine.getDouble("_95041_")>=2;}
			case 299:  { return 5000*engine.getDouble("RoleLv").intValue();}
			case 446:  { return (5000*engine.getDouble("TeamLv").intValue()*0.05*(1-engine.getDouble("IsDbPoint").intValue())+5000*engine.getDouble("TeamLv").intValue()*0.124*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15)*(1+0.05*engine.getDouble("IsTL").intValue());}
			case 27:  { return engine.getDouble("skilllevel").intValue()>=70;}
			case 706:  { return (Boolean)engine.get("_506002_");}
			case 325:  { return (1*engine.getDouble("RoleLv").intValue()*0.717*2*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 383:  { return 400*Math.min(engine.getDouble("RoleLv").intValue(),74)*0.35*(0.75+0.05*10)*(Math.random()*(1.02-0.98)+0.98);}
			case 722:  { return (Boolean)engine.get("_501004_");}
			case 449:  { return (5000*engine.getDouble("RoleLv").intValue()*0.125)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 524:  { return engine.getDouble("_94055_")>=1;}
			case 544:  { return engine.getDouble("_95036_")>=1;}
			case 56:  { return -3*engine.getDouble("skilllevel").intValue();}
			case 705:  { return (Boolean)engine.get("_509082_")||(Boolean)engine.get("_509083_")||(Boolean)engine.get("_506201_");}
			case 161:  { return engine.getDouble("gradea").intValue()*0.9;}
			case 233:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)*0.9+1*engine.getDouble("skilllevel").intValue()+4*engine.getDouble("skilllevel").intValue()*(1-opf.getEffectRole().getAttrById(80)/opf.getEffectRole().getAttrById(60)));}
			case 601:  { return engine.getDouble("_96017_")<1;}
			case 147:  { return -aimf.getEffectRole().getAttrById(80)*0.7;}
			case 456:  { return (5000*engine.getDouble("MonsterLv").intValue()*0.017*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*(1-engine.getDouble("IsDbPoint").intValue())+5000*engine.getDouble("MonsterLv").intValue()*0.02*(engine.getDouble("MonsterNum").intValue()*0.083+engine.getDouble("MasterNum").intValue()*0.1245)*Math.min(Math.max(1-0.2*Math.floor(Math.abs(engine.getDouble("MonsterLv").intValue()-engine.getDouble("RoleLv").intValue())/5),0.1),1)*engine.getDouble("IsDbPoint").intValue())*(Math.random()*(1.02-0.98)+0.98);}
			case 727:  { return 1*engine.getDouble("TeamNum").intValue()+3;}
			case 51:  { return -(opf.getEffectRole().getAttrById(150)*1.5-aimf.getEffectRole().getAttrById(160)+2*engine.getDouble("skilllevel").intValue())*(0.5+0.05*(3-engine.getDouble("preaimcount").intValue()));}
			case 319:  { return (1*engine.getDouble("RoleLv").intValue()*1.256*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 600:  { return engine.getDouble("_96016_")<1&&engine.getDouble("_96018_")<1;}
			case 157:  { return -opf.getEffectRole().getAttrById(100)*0.8;}
			case 367:  { return (1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*0.2);}
			case 543:  { return engine.getDouble("_95035_")>=1;}
			case 580:  { return engine.getDouble("_95078_")<1;}
			case 284:  { return (1000*engine.getDouble("RoleLv").intValue()*0.194*1.5)*(Math.random()*(1.02-0.98)+0.98);}
			case 484:  { return engine.getDouble("_94003_")>=1;}
			case 223:  { return opf.getEffectRole().getAttrById(170)+3*engine.getDouble("skilllevel").intValue()+((((boolean)engine.get("pve")))?(2*engine.getDouble("skilllevel").intValue()):(0));}
			case 708:  { return (Boolean)engine.get("_110_")||(Boolean)engine.get("_120_")||(Boolean)engine.get("_13_");}
			case 686:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99017_")>=1;}
			case 242:  { return (((opf.getEffectRole().getAttrById(180)>=aimf.getEffectRole().getAttrById(190))?(0.98-0.32*Math.pow(0.95,(opf.getEffectRole().getAttrById(180)/10-aimf.getEffectRole().getAttrById(190)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)):(0.66*Math.pow(0.9,(aimf.getEffectRole().getAttrById(190)/10-opf.getEffectRole().getAttrById(180)/10))+(opf.getEffectRole().getAttrById(810)-aimf.getEffectRole().getAttrById(820))+(opf.getEffectRole().getAttrById(2130)/1000-aimf.getEffectRole().getAttrById(2140)/1000)))-0.16)*0.4;}
			case 341:  { return (1+0.05*engine.getDouble("IsTL").intValue())*(1+engine.getDouble("IsSerMul").intValue())*(engine.getDouble("StdExp").intValue()*2.14*7/8*((engine.getDouble("Ring").intValue()-1)*0.3+1));}
			case 234:  { return Math.random()<0.3?2:1;}
			case 322:  { return 1*engine.getDouble("RoleLv").intValue()*0.667*(3.5+0.5*(Math.floor((engine.getDouble("Time").intValue()-1)/5)+1))*Math.floor(1-(engine.getDouble("Time").intValue()%5)*0.2)*(Math.random()*(1.02-0.98)+0.98);}
			case 80:  { return -(opf.getEffectRole().getAttrById(130)*0.8-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("gradea").intValue());}
			case 541:  { return engine.getDouble("_95025_")==1;}
			case 671:  { return engine.getDouble("_99001_")<1;}
			case 458:  { return (5000*Math.min(Math.max(engine.getDouble("RoleLv").intValue(),engine.getDouble("FuBenLv").intValue()),engine.getDouble("FuBenLv").intValue()+9)*0.1*(0.7+0.1*engine.getDouble("Ring").intValue()))*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 561:  { return engine.getDouble("_95041_")>=3;}
			case 513:  { return engine.getDouble("_94025_")>=1&&engine.getDouble("_94030_")==2;}
			case 650:  { return engine.getDouble("_97004_")>=4;}
			case 2:  { return engine.getDouble("quality").intValue()*60+2000;}
			case 624:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96310_")<1;}
			case 627:  { return engine.getDouble("_96301_")>=1&&engine.getDouble("_96363_")<1;}
			case 3:  { return engine.getDouble("quality").intValue()*0.6+10;}
			case 276:  { return (1000*engine.getDouble("RoleLv").intValue()*0.05*(0.79+0.02*engine.getDouble("AnswerCnt").intValue()))*0.5*(Math.random()*(1.02-0.98)+0.98);}
			case 293:  { return (1000*engine.getDouble("RoleLv").intValue()*0.194*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 709:  { return (Boolean)engine.get("_506306_");}
			case 440:  { return (5000*engine.getDouble("RoleLv").intValue()*0.1)*(Math.random()*(1.02-0.98)+0.98)*(1-Math.min(5-engine.getDouble("TeamNum").intValue(),2)*0.15);}
			case 205:  { return -(opf.getEffectRole().getAttrById(130)*1.1-aimf.getEffectRole().getAttrById(140)+1*engine.getDouble("skilllevel").intValue()+Math.max(0,opf.getEffectRole().getAttrById(200)-aimf.getEffectRole().getAttrById(200))*0.5);}
			case 483:  { return engine.getDouble("_94002_")>=1;}
			case 685:  { return engine.getDouble("_99001_")<1&&engine.getDouble("_99016_")>=1;}
			case 644:  { return engine.getDouble("_96354_")<1;}
			case 279:  { return (1000*engine.getDouble("RoleLv").intValue()*0.377*(0.95+0.05*(engine.getDouble("PVPCnt").intValue()+1))*(0.95+0.05*(engine.getDouble("PVPTargetCnt").intValue()+1)))*(Math.random()*(1.02-0.98)+0.98);}
			case 643:  { return engine.getDouble("_96351_")>=1&&engine.getDouble("_96366_")<1;}
			case 229:  { return -1*engine.getDouble("skilllevel").intValue();}
			case 731:  { return 1*engine.getDouble("TeamNum").intValue()+2;}
			case 478:  { return engine.getDouble("_94038_")>=2||engine.getDouble("_94028_")>=1;}
			case 487:  { return engine.getDouble("_94005_")>=3;}
			case 583:  { return engine.getDouble("_95081_")<1;}
			case 536:  { return engine.getDouble("_95018_")<=1;}
			case 698:  { return (Boolean)engine.get("_506003_");}
			case 396:  { return (400*engine.getDouble("RoleLv").intValue()*0.628*2)*(Math.random()*(1.02-0.98)+0.98);}
			case 361:  { return (engine.getDouble("StdExp").intValue()*13.333/28.867*((engine.getDouble("Ring").intValue()-1)*0.08+1)+engine.getDouble("RoleLv").intValue()*50-1000);}
			case 461:  { return Math.min(Math.max(engine.getDouble("rolenum").intValue()*0.001,2),4)+(engine.getDouble("ServerLv").intValue()-50)*0.2;}
			case 97:  { return -0.1-(0.1+0.05*engine.getDouble("skilllevel").intValue())*(1-aimf.getEffectRole().getAttrById(100)/aimf.getEffectRole().getAttrById(90));}
		}
		return null;
	}
}