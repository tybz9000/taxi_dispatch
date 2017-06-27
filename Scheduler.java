package DIdi;
import java.util.*;
public class Scheduler extends Thread{
	private taxi []taxilist;//出租车数组
	private boolean auto_moniter;
	private Vector<requestion> Q_list;//请求队列
	private int car_to_be_monited;
	private int timer1;
	public Scheduler(){
		//前置条件none
		//副作用 this
		//后置条件  初始化
		timer1=0;
		taxilist=new taxi[100];
		car_to_be_monited=-1;
		auto_moniter=false;
		for(int i=0;i<100;i++){
			if(i<70){
				taxilist[i]=new taxi(i);
			}
			else{
				taxilist[i]=new advanced_taxi(i);
			}
		}
		Q_list=new Vector<requestion>();
	}
	public void run(){
		
		
		taxi bestaxi=null;
		
		
		for(int i=0;i<100;i++){
			taxilist[i].start();
		}
		
		while(true){
			timer1++;
			if(timer1%2==0){
				taxi_moniter(car_to_be_monited);
			}
			if(timer1%6==0){
				RG_LIGHT.RG_change();
				//System.out.println(RG_LIGHT.get_RG(1,0));
			}
			for(int i=0;i<100;i++){
				city_map.flow_operation(taxilist[i].get_x(),taxilist[i].get_y(),taxilist[i].cmd_ask()-1);
			}
			if(!this.Q_list.isEmpty()){
				for(int i=0;i<Q_list.size();i++){
					if(!Q_list.get(i).self_check()){
						Q_list.remove(Q_list.get(i));
						i--;
						continue;
					}
					if(Q_list.get(i).time_off_judge()){
						Q_list.get(i).car_condition_check();
						if((bestaxi=Q_list.get(i).choose_best_taxi())!=null){
							bestaxi.get_requestion(Q_list.get(i));
							
							if(auto_moniter){
								car_to_be_monited=bestaxi.num_ask();
								auto_moniter=false;
							}
						}
						
						Q_list.remove(Q_list.get(i));
						i--;
						continue;
					}
					for(taxi t:taxilist){
						if(t.get_x()>=Q_list.get(i).get_Start().x()-2&&t.get_x()<=Q_list.get(i).get_Start().x()+2&&t.get_y()>=Q_list.get(i).get_Start().y()-2&&t.get_y()<=Q_list.get(i).get_Start().x()+2){
							Q_list.get(i).list_add_car(t);
						}
					}
					Q_list.get(i).time_flow();
				}
			}
			
			try{
				sleep(50);
			}
			catch(Exception e){
				
			}
			city_map.flow_clear();
		}
		
		
	}
	public void taxi_moniter(int i){
		//出租车监视函数
		//前置条件，合法的出租车代号
		//副作用 none
		//后置条件  输出出租车状态
		if(i==-1){
			
		}
		else{
			if(taxilist[i].condition_get()==3){
				this.car_to_be_monited=-1;
			}
			System.out.println("car"+taxilist[i].num_ask()+"'s condition:"+taxilist[i].get_x()+","+taxilist[i].get_y()+","+taxilist[i].condition_get()+",time is:"+Clock.timeask());
		}
	}
	public void moniter(int i){
		//前置条件，合法的出租车代号
		//副作用 this
		//后置条件 改变监视目标
		this.car_to_be_monited=i;
	}
	public void auto_moniter(){
		//前置条件，none
		//副作用 this
		//后置条件  改变监视模式
		this.auto_moniter=true;
	}
	public void get_requestion(requestion q){
		//前置条件   请求q
		//副作用  this
		//后置条件  输出提示，给予请求给车
		System.out.println("新请求到达，起点"+q.get_Start().x()+","+q.get_Start().y()+",请附近的老司机做好准备");
		this.Q_list.add(q);
	}
	public void show_all_condition(){
		//前置条件 none
		//副作用 none
		//后置条件  输出所有车的状态
		for(int i=0;i<taxilist.length;i++){
			System.out.println("car"+taxilist[i].num_ask()+"'s condition:"+taxilist[i].get_x()+","+taxilist[i].get_y()+","+taxilist[i].condition_get()+",time is:"+Clock.timeask());
		}
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		for(taxi t:this.taxilist){
			if(!t.repOK())return false;
		}
		for(requestion r:this.Q_list){
			if(!r.repOK())return false;
		}
		if(this.car_to_be_monited<0||this.car_to_be_monited>100){
			return false;
		}
		if(timer1<0)return false;
		return true;
	}
	public ListIterator<point> xcjly_get(int i,int ii){
		//前置条件  符合条件的i
		//副作用   none
		//后置条件   返回第i辆车，（如果是先进车）的第ii个迭代器
		if(i<70){
			System.out.println("这辆车没有装行车记录仪");
			return null;
		}
		else{
			return taxilist[i].xcjly_cx(ii);
		}
	}
}
