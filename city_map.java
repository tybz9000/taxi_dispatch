package DIdi;
import java.io.*;
import java.util.*;
import java.util.Queue;
public class city_map extends Thread{
	private int[][] initial_map;//存初始地图，文件中的地图
	private RG_LIGHT rl;
	private int[][] initial_cross_map;
	private static point [][] pmap;//翻译后的地图
	private static Map<point,point> step;//存bfs得到的树
	public city_map(){//初始化部分完成了地图的读取以及翻译
		
		//前置条件:none
		//副作用:this
		//后置条件:initial_map读取了map.txt，经过翻译，以point实例的形式存在pmap中
		rl=new RG_LIGHT();
		//step1 :initial_map initialize
		step=new HashMap<point,point>();
		initial_map=new int [80][80];
		initial_cross_map =new int [80][80];
		pmap=new point[80][80];//初始化广搜用map
		
		String map_file_path="./map.txt";//地图文件地址
		String cross_file_path="./cross.txt";
		File map_file=new File(map_file_path);//文件建立
		File cross_file=new File(cross_file_path);
		if(map_file.exists()&&cross_file.exists()){
			System.out.println("地图文件已找到，读取中");
		}
		int y=0;
		try{
			FileReader fr=new FileReader(map_file);//Filereader建立
			BufferedReader bf = new BufferedReader(fr);//bufferedreader建立
			String line=null;//每一行要读取的存在这。
			while((line=bf.readLine())!=null){
				//System.out.println(line);
				for(int x=0;x<80;x++){
					String sz=line.substring(x,x+1);
					initial_map[x][y]=Integer.valueOf(sz);
					//System.out.println(initial_map[x][y]);
				}
				y++;
			}
			y=0;
			FileReader fr2=new FileReader(cross_file);//Filereader建立
			BufferedReader bf2 = new BufferedReader(fr2);//bufferedreader建立
			String line2=null;//每一行要读取的存在这。
			while((line2=bf2.readLine())!=null){
				//System.out.println(line);
				for(int x=0;x<80;x++){
					String sz=line2.substring(x,x+1);
					initial_cross_map[x][y]=Integer.valueOf(sz);
					//System.out.println(initial_map[x][y]);
				}
				y++;
			}
		}
		catch (Exception e){
			System.out.println("初始地图建立出现错误,请检查您的map文件路径，地图格式,cross文件路径");
			System.exit(1);
			e.printStackTrace();
			
		}
		// step2 pmap initial,地图翻译
		boolean n,s,w,e;//四个方向所用的标记
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				pmap[ii][i]=new point(ii,i);
				n=false;s=false;w=false;e=false;
				switch(initial_map[ii][i]){
				case 0:{
						s=false;
						e=false;
						break;
				}
				case 1:{
						s=false;
						e=true;
						break;
				}
				case 2:{
						e=false;
						s=true;
						break;
				}
				case 3:{
						e=true;
						s=true;
						break;
				}
				}
				if(ii==0&&i!=0){
					w=false;
					if(initial_map[ii][i-1]==2||initial_map[ii][i-1]==3){
						n=true;
					}
				}
				if(i==0&&ii!=0){
					n=false;
					if(initial_map[ii-1][i]==1||initial_map[ii-1][i]==3){
						w=true;
					}
				}
				if(i>0&&ii>0){
					if(initial_map[ii-1][i]==1||initial_map[ii-1][i]==3){
						w=true;
					}
					if(initial_map[ii][i-1]==2||initial_map[ii][i-1]==3){
						n=true;
					}
				}
				pmap[ii][i].direction(n, s, w, e);
				
			}
		}
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				if(initial_cross_map[ii][i]==1){
					pmap[ii][i].to_three_dimensional();
					//System.out.print(0);
				}
				else{
					
					if(pmap[ii][i].connected_number_ask()>=3){
						rl.Light(ii,i);
						//System.out.print(1);
					}
					else{
						//System.out.print(0);
					}
				}
			}
			//System.out.print("\n");
		}
		
		/*for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				System.out.print(pmap[ii][i].notrh());
			}
			System.out.print("\n");
		}
		System.out.println("最短路径初始化运算开始");
		try{
			mldmadd();
		}
		catch(Exception ee){
			System.out.println("最短路径解析错误，请检查您地图的连通性");
			System.exit(1);
		}
		
		System.out.println("最短路径初始化运算完成");*/
	}
	
	public static synchronized bfs_result min_length_cal(int x1,int y1,int x2,int y2){//计算从一个点出发到另一个点的最短距离第一步,同时算出最短距离;
		//前置条件 起始点，目标点的坐标，要求坐标有效
		//副作用 this
		//后置条件 得到(x1,y1)到(x2,y2)点的最短距离 以及 第一步前进的方向，以bfs_result实例的形式返回
		Queue<point> q=new LinkedList<point>();//初始化队列
		step.clear();//清空第一步映射地图
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				pmap[ii][i].devisit();
			}
		}//所有点访问恢复
		
		point V,W;
		pmap[x1][y1].visit();
		q.offer(pmap[x1][y1]);
		while(!q.isEmpty()){
			V=q.poll();
			if(V.notrh()){
				W=pmap[V.x()][V.y()-1];
				if(!W.visited()){
					W.visit();
					q.offer(W);
					//System.out.println("访问点"+W.x()+","+W.y());
					step.put(W,V);
					if(W.x()==x2&&W.y()==y2){
						break;
					}
				}
			}
			if(V.east()){
				W=pmap[V.x()+1][V.y()];
				if(!W.visited()){
					W.visit();
					q.offer(W);
					//System.out.println("访问点"+W.x()+","+W.y());
					step.put(W,V);
					if(W.x()==x2&&W.y()==y2){
						break;
					}
				}
			}
			if(V.south()){
				W=pmap[V.x()][V.y()+1];
				if(!W.visited()){
					W.visit();
					q.offer(W);
					//System.out.println("访问点"+W.x()+","+W.y());
					step.put(W,V);
					if(W.x()==x2&&W.y()==y2){
						break;
					}
				}
			}
			if(V.west()){
				W=pmap[V.x()-1][V.y()];
				if(!W.visited()){
					W.visit();
					q.offer(W);
					//System.out.println("访问点"+W.x()+","+W.y());
					step.put(W,V);
					if(W.x()==x2&&W.y()==y2){
						break;
					}
				}
			}
		}
		//到这里，完成了一个点到所有点的bfs访问
		/*开始填充mldm
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				mldm[x1][y1][ii][i]=get_first_path(x1,y1,ii,i);
				//System.out.println(mldm[x1][y1][ii][i]);
			}
		}*/
		return get_first_path(x1,y1,x2,y2);
	}
	public static synchronized bfs_result get_first_path(int x,int y,int aimx,int aimy){
		//前置条件 起始点，目标点的坐标，要求坐标有效
		//副作用 this
		//后置条件 得到(x1,y1)到(x2,y2)点的最短距离 以及 第一步前进的方向，以bfs_result实例的形式返回
		//回溯求第一步
		bfs_result br=new bfs_result();
		point E,B;
		E=pmap[aimx][aimy];
		B=pmap[x][y];
		if(x==aimx&&y==aimy){
			br.firststep=0;
			br.length=0;
			return br;
		}
		
		while(!(B.x()==step.get(E).x()&&B.y()==step.get(E).y())){
			E=step.get(E);
			br.length++;
		}
		
		if(E.y()==B.y()-1){
			br.firststep=1;//north
			return br;
		}
		if(E.x()==B.x()+1){
			br.firststep=2;//east
			return br;
		}
		if(E.y()==B.y()+1){
			br.firststep=3;//south
			return br;
		}
		if(E.x()==B.x()-1){
			br.firststep=4;//west
			return br;
		}
		br.firststep=-1;
		return br;
		
	}
	
	public static point pmapask(int x1,int y1){
		//前置条件：point坐标
		//副作用：this
		//后置条件 point实例返回
		return pmap[x1][y1];
	}
	
	public static int wayoperation(int x1,int y1,boolean is_east,boolean to_open){
		//前置条件:点坐标(x1,y1) 布尔值  东方  布尔值  打开
		//副作用  this
		//后置条件  ： 如果点非法，返回0  否则返回1  并进行相应的路径开关工作
		if(x1==79&&is_east==true){
			return 0;//不允许操作最右边一列的东方
		}
		if(y1==79&&is_east==false){
			return 0;//不允许操作最下边一行的南方
		}
		if(is_east){
			if(to_open){
				pmap[x1][y1].wayopen(1);
				pmap[x1+1][y1].wayopen(3);
				return 1;
			}
			else{
				pmap[x1][y1].wayclose(1);
				pmap[x1+1][y1].wayclose(3);
				return 1;
			}
		}
		else{
			if(to_open){
				pmap[x1][y1].wayopen(2);
				pmap[x1][y1+1].wayopen(0);
				return 1;
			}
			else{
				pmap[x1][y1].wayclose(2);
				pmap[x1][y1+1].wayclose(0);
				return 1;
			}
		}
	}
	public static void flow_clear(){
		//副作用this pmap各点
		//后置条件： 全点流量清空
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				pmap[ii][i].flow_clear();
			}
		}
	}
	public static int flow_operation(int x,int y,int dir){
		//前置条件 坐标(x,y)以及方向dir {0,1,2,3}
		//副作用 this
		//后置条件   进行相应道路的流量增加
		switch(dir){
		case 0:
			pmap[x][y].flow_add(2);
			pmap[x][y+1].flow_add(0);
			break;
		case 1:
			pmap[x][y].flow_add(3);
			pmap[x-1][y].flow_add(1);
			break;
		case 2:
			pmap[x][y].flow_add(0);
			pmap[x][y-1].flow_add(2);
			break;
		case 3:
			pmap[x][y].flow_add(1);
			pmap[x+1][y].flow_add(3);
			break;
		}
		return 0;
	}
	public boolean repOK(){
		//前置条件 none
		//副作用 none
		//后置条件  如果满足不定式，返回true
		for(int i=0;i<80;i++){
			for(int ii=0;ii<80;ii++){
				if(this.initial_map[ii][i]<0&&this.initial_map[ii][i]>3)return false;
			}
		}
		
				for(int i=0;i<80;i++){
					for(int ii=0;ii<80;ii++){
						if(this.initial_cross_map[ii][i]<0&&this.initial_map[ii][i]>1)return false;
					}
				}
		return true;
	}
	
	
	
}
