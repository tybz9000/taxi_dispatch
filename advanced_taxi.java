package DIdi;
import java.util.*;
public class advanced_taxi extends taxi{
	private ArrayList<requestion> xcjly;
	public advanced_taxi(int num){
		super(num);
		xcjly=new ArrayList<requestion>();
	}
	//重写get_passenger方法，在这个方法运行的时候，开始一个新一轮的行车记录仪记录。
	public void get_passenger(){
		//前置条件  无
		//副作用  this
		//后置条件，改变车的状态,添加新一条记录
		if(this.position.x()==car_requestion.get_Start().x()&&this.position.y()==car_requestion.get_Start().y()){
			//状态转换五部曲 二 1->2前往接客 至前往目的地;
			this.condition=2;
			xcjly.add(this.car_requestion);
			//System.out.println("car:"+this.num_ask()+"get passenger at"+this.position.x()+","this.position.y());
			try{
				Thread.sleep(900);
			}
			catch(Exception e){
				
			}
		}
		
	}
	public ListIterator<point> xcjly_cx(int i){
		//前置条件   要查询的乘客号
		//副作用 null
		//后置条件  如果没有此乘客，输出查无此人   否则，返回响应的迭代
		if(i>=this.xcjly.size()){
			System.out.println("查无此人");
			return null;
		}
		else{
			return this.xcjly.get(i).cx();
		}
	}
	public void move(int x,int y){//重载，有目的移动一步
		//前置条件  目的地的坐标(x,y)
		//副作用 this
		//后置条件   依照目的地的位置，移动车位置一步,在状态2的情况下记录当前请求的路径
		if(this.condition==2){
			this.xcjly.get(this.xcjly.size()-1).xcjly_jl(this.position);
		}
		int north_shortest=180;
		int east_shortest=180;
		int south_shortest=180;
		int west_shortest=180;
		if(position.notrh()||position.notrh_changed()){
			north_shortest=city_map.min_length_cal(position.x(),position.y()-1,x,y).length;
		}
		if(position.east()||position.eastchanged()){
			east_shortest=city_map.min_length_cal(position.x()+1,position.y(),x,y).length;
		}
		if(position.south()||position.southchanged()){
			south_shortest=city_map.min_length_cal(position.x(),position.y()+1,x,y).length;
		}
		if(position.west()||position.westchanged()){
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
	public void move(){//无目的运动,执行此方法时，taxi会往可以移动的路径上随意走一个。
		//前置条件  无
		//副作用，无
		//后置条件   依流量与随机规则改变车的位置
		ArrayList <Integer> direction=new ArrayList<Integer>();
		int minflow=101;
		int thisflow=0;
		if(position.notrh()||position.notrh_changed()){
			direction.add(0);
		}
		if(position.east()||position.eastchanged()){
			direction.add(1);
		}
		if(position.south()||position.southchanged()){
			direction.add(2);
		}
		if(position.west()||position.westchanged()){
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
}
