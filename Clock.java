package DIdi;

public class Clock {
	private static long time;
	private static long  begintime;
	public Clock(){
		//前置条件  none
		//副作用 this
		//后置条件  初始化clock
		begintime=System.currentTimeMillis();
		time=0;
	}
	public static long timeask(){
		//副作用 this
		//后置条件：返回当前系统时间（四舍五入，0.1s时间单位）
		time=System.currentTimeMillis()-begintime;
		//System.out.println(time+","+begintime);
		if(time-time/100*100>=50){return time/100+1;}
		else{return time/100;}
		
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		if(time<0)return false;
		if(this.begintime<0)return false;
		
		return true;
	}
}