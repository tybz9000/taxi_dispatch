package DIdi;
//这个类是广搜时用来保存点信息的类
public class point {
	private int x;
	private int y;//坐标
	private boolean visited;//是否被访问过了
	private boolean north,west,east,south;
	private boolean northchanged,westchanged,eastchanged,southchanged;
	private int east_way_flow;
	private int south_way_flow;
	private int north_way_flow;
	private int west_way_flow;
	private boolean is_three_dimensional;
	public point(int x,int y){
		//前置条件  合法的坐标
		//副作用 this
		//后置条件  初始化各个属性
		this.x=x;
		this.y=y;
		this.visited=false;
		this.east_way_flow=0;
		this.south_way_flow=0;
		this.north_way_flow=0;
		this.west_way_flow=0;
		this.is_three_dimensional=false;
		this.northchanged=false;
		this.eastchanged=false;
		this.southchanged=false;
		this.westchanged=false;
	}
	public void visit(){
		this.visited=true;
	}
	public void devisit(){
		this.visited=false;
	}
	public int x(){
		return this.x;
	}
	public int y(){
		return this.y;	
	}
	public boolean visited(){
		return this.visited;
	}
	public boolean notrh(){
		return this.north;
	}
	public boolean south(){
		return this.south;
	}
	public boolean west(){
		return this.west;
	}
	public boolean east(){
		return this.east;
	}
	public boolean notrh_changed(){
		return this.northchanged;
	}
	public boolean southchanged(){
		return this.southchanged;
	}
	public boolean westchanged(){
		return this.westchanged;
	}
	public boolean eastchanged(){
		return this.eastchanged;
	}
	public void to_three_dimensional(){
		//前置条件: none
		//副作用: 这个对象的  是否是三维  属性发生了变化
		//后置条件：设置为立体交通
		this.is_three_dimensional=true;
	}
	public boolean dimensional_ask(){
		//前置条件 ：none
		//副作用 ：none
		//后置条件 ：返回时候是立交桥
		return this.is_three_dimensional;
	}
	public void direction(boolean n,boolean s,boolean w,boolean e){
		//前置条件  表示方向上道路开启的布尔值 n s w e
		//副作用 无
		//后置条件   记录点各个方向道路开启情况
		this.north=n;
		this.south=s;
		this.west=w;
		this.east=e;
	}
	public void wayclose(int i){//断路函数
		//前置条件  要关闭的道路  i{0,1,2,3}
		//副作用  无
		//后置条件  关闭相应的道路,并记标志位为1
		switch(i){
		case 0:{
			this.north=false;
			this.northchanged=true;
			this.north_way_flow=0;
			break;
		}
		case 1:{
			this.eastchanged=true;
			this.east=false;
			this.east_way_flow=0;
			break;
		}
		case 2:{
			this.southchanged=true;
			this.south=false;
			this.south_way_flow=0;
			break;
		}
		case 3:{
			this.westchanged=true;
			this.west=false;
			this.west_way_flow=0;
			break;
		}
	}
	}
	public void wayopen(int i){//开路函数
		//前置条件  要开启的道路  i{0,1,2,3}
		//副作用  无
		//后置条件  开启相应的道路
		switch(i){
		case 0:{
			this.north=true;
			break;
		}
		case 1:{
			this.east=true;
			break;
		}
		case 2:{
			this.south=true;
			break;
		}
		case 3:{
			this.west=true;
			break;
		}
	}
	}
	public void flow_add(int dir){//流量增加函数
		//前置条件  要增加流量的方向  i{0,1,2,3}且道路开启
		//副作用  无
		//后置条件 相应方向增加流量
		switch(dir){
		case 0:
			if(this.north){
				this.north_way_flow++;
			}
			
			break;
		case 1:
			if(this.east){
				this.east_way_flow++;
			}
			
			break;
		case 2:
			if(this.south){
				this.south_way_flow++;
			}
			
			break;
		case 3:
			if(this.west){
				this.west_way_flow++;
			}
			
			break;
		}

	}
	public void flow_clear(){//流量清零函数
		//副作用this
		//后置条件 所有方向流量清空
		this.east_way_flow=0;
		this.south_way_flow=0;
		this.north_way_flow=0;
		this.west_way_flow=0;
	}
	public int flowask(int i){
		//前置条件  方向int i
		//后置条件  返回指定方向流量
		switch(i){
		case 0:
			return this.north_way_flow;
		case 1:
			return this.east_way_flow;
		case 2:
			return this.south_way_flow;
		case 3:
			return this.west_way_flow;
		
		}
		defult:
			return -1;
	}
	public int connected_number_ask(){
		//前置条件  none
		//副作用  none
		//后置条件，返回当前点连通数量
		int num=0;
		if(this.north){
			num++;
		}
		if(this.east){
			num++;
		}
		if(this.south){
			num++;
		}
		if(this.west){
			num++;
		}
		return num;
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		if(x<0||x>=80)return false;
		if(y<0||y>=80)return false;
		if(this.north_way_flow<0)return false;
		if(this.east_way_flow<0)return false;
		if(this.south_way_flow<0)return false;
		if(this.west_way_flow<0)return false;
		return true;
	}
}
