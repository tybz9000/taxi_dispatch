package DIdi;
import java.util.*;
public class requestion {
	private point Start;
	private point End;
	private int window_time;
	private ArrayList<point> xcjly;
	private ArrayList<taxi> taxi_choose_list;
	public requestion(int xs,int ys,int xe,int ye){
		//前置条件   合法的起点坐标 终点坐标
		//副作用 this
		//后置条件  初始化
		xcjly=new ArrayList<point>();
		Start=new point(xs,ys);
		End=new point(xe,ye);
		window_time=0;
		this.taxi_choose_list=new ArrayList<taxi>();
	}
	public void xcjly_jl(point p){
		//前置条件   合法的点
		//副作用  this
		//后置条件  记录此请求下经过的点
		this.xcjly.add(p);
	}
	public ListIterator<point> cx(){
		//前置条件 null
		//副作用 this
		//后置条件 返回当前请求下 送客阶段路程查询的迭代
		return this.xcjly.listIterator();
	}
	public point get_Start(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.Start;
	}
	public point get_End(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.End;
	}
	public void time_flow(){
		//前置条件:none
		//副作用:none
		//后置条件：改变当前请求时间窗口
		this.window_time++;
	}
	public boolean time_off_judge(){
		//前置条件:
		//副作用
		//后置条件：根据时间窗口的值返回相应的值
		if(this.window_time>=30){
			return true;
		}
		else{
			return false;
		}
	}
	public void list_add_car(taxi t){
		//前置条件  可用的出租车t
		//副作用 令该车抢单
		//后置条件	选定出租车抢单
		if(!this.taxi_choose_list.contains(t)){
			this.taxi_choose_list.add(t);
			t.qiang_dan();
		}
		
	}
	public void car_condition_check(){
		//出租车状态检查
		//前置条件 无
		//副作用 this
		//后置条件，剔除可用出租车列表里不符合条件的出租车
		for(int i=0;i<this.taxi_choose_list.size();i++){
			if(this.taxi_choose_list.get(i).condition_get()!=0){
				this.taxi_choose_list.remove(this.taxi_choose_list.get(i));
				i--;
			}
		}
	}
	public taxi choose_best_taxi(){
		//选择最好的出租车
		//前置条件 无
		//副作用  this
		//后置条件  选择最适合的出租车并返回
		int length=180;
		int maxcredit=-1;
		if(this.taxi_choose_list.isEmpty()){
			System.out.println("起点为"+this.Start.x()+","+this.Start.y()+"的请求无车辆接受");
			return null;
		}
		for(taxi t:this.taxi_choose_list){
			if(t.credit_get()>maxcredit){
				maxcredit=t.credit_get();
			}
		}
		for(int i=0;i<this.taxi_choose_list.size();i++){
			if(this.taxi_choose_list.get(i).credit_get()!=maxcredit){
				this.taxi_choose_list.remove(this.taxi_choose_list.get(i));
				i--;
			}
		}
		if(this.taxi_choose_list.size()==1){
			return this.taxi_choose_list.get(0);
		}
		else{
			int minlength=180;
			for(taxi t:this.taxi_choose_list){
				if((length=city_map.min_length_cal(t.get_x(),t.get_y(),Start.x(),Start.y()).length)<minlength){
					minlength=length;
				}
			}
			for(int i=0;i<this.taxi_choose_list.size();i++){
				if(city_map.min_length_cal(this.taxi_choose_list.get(i).get_x(),this.taxi_choose_list.get(i).get_y(),Start.x(),Start.y()).length!=minlength){
					this.taxi_choose_list.remove(this.taxi_choose_list.get(i));
					i--;
				}
			}
			if(this.taxi_choose_list.size()==1){
				return this.taxi_choose_list.get(0);
			}
			else{
				//此处修改，引入路径机制
				Random rand=new Random();
				return this.taxi_choose_list.get(rand.nextInt(this.taxi_choose_list.size()));
			}
		}
	}
	public boolean self_check(){
		//自检
		//前置条件  无
		//副作用 无
		//后置条件，判断起始点，终点是否符合条件并返回判断结果
		boolean endcheck=this.End.x()>=0&&this.End.x()<80&&this.End.y()>=0&&this.End.y()<80;
		boolean startcheck=this.Start.x()>=0&&this.Start.x()<80&&this.Start.y()>=0&&this.Start.y()<80;
		if(endcheck&&startcheck){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		if(!this.Start.repOK())return false;
		if(!this.End.repOK())return false;
		if(this.window_time<0)return false;
		for(taxi t:this.taxi_choose_list){
			if(!t.repOK())return false;
		}
		return true;
	}
}
