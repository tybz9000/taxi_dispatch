package DIdi;
import java.util.*;
public class taxi extends Thread{
	protected int num;
	protected point position;
	protected int condition;//状态属性  值0：等待服务  值1：即将服务  值2：正在服务  值3：休息
	protected int self_timer;
	protected int credit;
	protected requestion car_requestion;
	protected int car_move_direction;
	public taxi(int num){
		//前置条件  合法的出租车数值
		//副作用 this
		//后置条件  出租车初始化   各项属性
		this.car_move_direction=0;
		this.num=num;
		this.credit=0;
		self_timer=0;
		car_requestion=null;
		Random rand=new Random();
		position=city_map.pmapask(rand.nextInt(80),rand.nextInt(80));
		condition=0;
	}
	public void run(){//管理出租车运动
		while(true){
			switch(condition){
			case 0://等待服务，乱跑就好
				move();
				self_timer++;
				if(self_timer==200){//状态变化之4 等待运行至休息
					this.condition=3;
				}
				break;
			case 1://即将服务，奔赴起点
				if(car_requestion==null){
					System.out.println("wrong : no aim");
				}
				else{
					move(car_requestion.get_Start().x(),car_requestion.get_Start().y());
				}
				this.get_passenger();
				break;
			case 2://正在服务 奔向目的地
				if(car_requestion==null){
					System.out.println("wrong : no aim");
				}
				else{
					move(car_requestion.get_End().x(),car_requestion.get_End().y());
				}
				this.put_passenger();
				break;
			case 3://休息
				this.car_move_direction=0;
				try{//状态转换5
					sleep(900);
				}
				catch(Exception e){
					System.out.println("sleep wrong");
				}
				this.self_timer=0;
				this.condition=0;
				break;
			}
			try{
				sleep(100);
			}
			catch(Exception e){
				System.out.println("sleep wrong");
			}
		}
		
	}
	public void move(){//无目的运动,执行此方法时，taxi会往可以移动的路径上随意走一个。
		//前置条件  无
		//副作用，无
		//后置条件   依流量与随机规则改变车的位置
		ArrayList <Integer> direction=new ArrayList<Integer>();
		int minflow=101;
		int thisflow=0;
		if(position.notrh()){
			direction.add(0);
		}
		if(position.east()){
			direction.add(1);
		}
		if(position.south()){
			direction.add(2);
		}
		if(position.west()){
			direction.add(3);
		}
		for(int i=0;i<direction.size();i++){
			if(minflow>position.flowask(direction.get(i))){
				minflow=position.flowask(direction.get(i));
			}
		}
		for(int i=0;i<direction.size();i++){
			if(minflow!=position.flowask(direction.get(i))){
				direction.remove(i);
				i--;
			}
		}
		Random rand=new Random();
		int go=direction.get(rand.nextInt(direction.size()));
		//System.out.println(go);
		int RG_light=RG_LIGHT.get_RG(position.x(),position.y());
		boolean NS_GREEN=(RG_light==1||RG_light==0);
		boolean WE_GREEN=RG_light==2||RG_light==0;
		//System.out.println(RG_light);
		switch(go){
		case 0:
			if(this.car_move_direction==0||this.car_move_direction==4||(this.car_move_direction==1||this.car_move_direction==3)&&NS_GREEN||this.car_move_direction==2&&WE_GREEN){
				this.car_move_direction=1;
				position=city_map.pmapask(position.x(),position.y()-1);
			}
			break;
		
		case 1:
			if(this.car_move_direction==0||this.car_move_direction==1||(this.car_move_direction==2||this.car_move_direction==4)&&WE_GREEN||this.car_move_direction==3&&NS_GREEN){
				this.car_move_direction=2;
				position=city_map.pmapask(position.x()+1,position.y());
			}
			
			break;
		case 2:
			if(this.car_move_direction==0||this.car_move_direction==2||(this.car_move_direction==1||this.car_move_direction==3)&&NS_GREEN||this.car_move_direction==4&&WE_GREEN){
				this.car_move_direction=3;
				position=city_map.pmapask(position.x(),position.y()+1);
			}
			
			break;
		case 3:
			if(this.car_move_direction==0||this.car_move_direction==3||(this.car_move_direction==2||this.car_move_direction==4)&&WE_GREEN||this.car_move_direction==1&&NS_GREEN){
				this.car_move_direction=4;
				position=city_map.pmapask(position.x()-1,position.y());
			}
			
			break;
		}
		direction.clear();
	}
	public ListIterator<point> xcjly_cx(int i){
		//前置条件   要查询的乘客号
		//副作用 null
		//后置条件  因为是普通车，所以没有返回
		return null;
	}
	public void move(int x,int y){//重载，有目的移动一步
		//前置条件  目的地的坐标(x,y)
		//副作用 无
		//后置条件   依照目的地的位置，移动车位置一步
		int north_shortest=180;
		int east_shortest=180;
		int south_shortest=180;
		int west_shortest=180;
		if(position.notrh()){
			north_shortest=city_map.min_length_cal(position.x(),position.y()-1,x,y).length;
		}
		if(position.east()){
			east_shortest=city_map.min_length_cal(position.x()+1,position.y(),x,y).length;
		}
		if(position.south()){
			south_shortest=city_map.min_length_cal(position.x(),position.y()+1,x,y).length;
		}
		if(position.west()){
			west_shortest=city_map.min_length_cal(position.x()-1,position.y(),x,y).length;
		}
		int min_length=Math.min(Math.min(north_shortest,east_shortest),Math.min(south_shortest,west_shortest));
		ArrayList <Integer> direction=new ArrayList<Integer>();
		if(north_shortest==min_length){
			direction.add(0);
		}
		if(east_shortest==min_length){
			direction.add(1);
		}
		if(south_shortest==min_length){
			direction.add(2);
		}
		if(west_shortest==min_length){
			direction.add(3);
		}
		int minflow=180;
		for(int i=0;i<direction.size();i++){
			if(minflow>position.flowask(direction.get(i))){
				minflow=position.flowask(direction.get(i));
			}
		}
		for(int i=0;i<direction.size();i++){
			if(minflow!=position.flowask(direction.get(i))){
				direction.remove(i);
				i--;
			}
		}
		Random rand=new Random();
		int first_dir=direction.get(rand.nextInt(direction.size()))+1;
		if(position.x()==x&&position.y()==y){
			first_dir=0;
		}
		int RG_light=RG_LIGHT.get_RG(position.x(),position.y());
		boolean NS_GREEN=(RG_light==1||RG_light==0);
		boolean WE_GREEN=(RG_light==2||RG_light==0);
		//System.out.println(first_dir+","+RG_light);
		switch(first_dir){
		case 0:
			break;
		case 1:
			if(this.car_move_direction==0||this.car_move_direction==4||(this.car_move_direction==1||this.car_move_direction==3)&&NS_GREEN||this.car_move_direction==2&&WE_GREEN){
				this.car_move_direction=1;
				position=city_map.pmapask(position.x(),position.y()-1);
			}
			break;
		
		case 2:
			if(this.car_move_direction==0||this.car_move_direction==1||(this.car_move_direction==2||this.car_move_direction==4)&&WE_GREEN||this.car_move_direction==3&&NS_GREEN){
				this.car_move_direction=2;
				position=city_map.pmapask(position.x()+1,position.y());
			}
			
			break;
		case 3:
			if(this.car_move_direction==0||this.car_move_direction==2||(this.car_move_direction==1||this.car_move_direction==3)&&NS_GREEN||this.car_move_direction==4&&WE_GREEN){
				this.car_move_direction=3;
				position=city_map.pmapask(position.x(),position.y()+1);
			}
			
			break;
		case 4:
			if(this.car_move_direction==0||this.car_move_direction==3||(this.car_move_direction==2||this.car_move_direction==4)&&WE_GREEN||this.car_move_direction==1&&NS_GREEN){
				this.car_move_direction=4;
				position=city_map.pmapask(position.x()-1,position.y());
			}
			
			break;
		
		}
	}
	public boolean get_requestion(requestion q){//状态变换五部曲之一，等待至前往接客
		//前置条件  请求q
		//副作用  无
		//后置条件  改变车的状态，信誉
		if(this.condition==0){
			System.out.println("car:"+this.num_ask()+"get requestion start at"+q.get_Start().x()+","+q.get_Start().y());
			this.car_requestion=q;
			this.condition=1;
			this.credit+=3;
			return true;
		}
		else{
			return false;
		}
	}
	public void get_passenger(){
		//前置条件  无
		//副作用  无
		//后置条件，改变车的状态
		if(this.position.x()==car_requestion.get_Start().x()&&this.position.y()==car_requestion.get_Start().y()){
			//状态转换五部曲 二 1->2前往接客 至前往目的地;
			this.condition=2;
			//System.out.println("car:"+this.num_ask()+"get passenger at"+this.position.x()+","this.position.y());
			try{
				Thread.sleep(900);
			}
			catch(Exception e){
				
			}
		}
	}
	public void put_passenger(){
		//前置条件 none
		//副作用 this
		//后置条件  改变出租车状态
		if(this.position.x()==car_requestion.get_End().x()&&this.position.y()==car_requestion.get_End().y()){
			//状态转换五部曲 三 2->0 前往目的地至放下乘客;
			this.condition=0;
			this.self_timer=0;
			this.car_requestion=null;
			System.out.println("car:"+this.num+"finished requestion");
			try{
				Thread.sleep(900);
			}
			catch(Exception e){
				
			}
		}
	}
	public void qiang_dan(){
		//前置条件 none
		//副作用 this
		//后置条件  提高出租车信用
		this.credit++;
	}
	
	public int credit_get(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.credit;
	}
	public int get_x(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.position.x();
	}
	public int get_y(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.position.y();
	}
	public int condition_get(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.condition;
	}
	public int num_ask(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.num;
	}
	public int cmd_ask(){
		//前置条件 none
		//副作用 none
		//后置条件  返回特定的值
		return this.car_move_direction;
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		if(this.num<0)return false;
		if(!this.position.repOK())return false;
		if(this.condition<0||this.condition>3)return false;
		if(this.self_timer<0)return false;
		if(this.credit<0)return false;
		//if(!this.car_requestion.repOK())return false;
		if(this.car_move_direction<0||this.car_move_direction>4)return false;
		return true;
	}

}
