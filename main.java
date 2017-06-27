package DIdi;
import java.util.*;
public class main {
	public static void main(String args[]){
		city_map m=new city_map();
		Scheduler s=new Scheduler();
		Clock c=new Clock();
		s.start();
		
		try{
			
		
		/*
		 * you could do something to add requestion here
		 * and use 
		 * s.get_requestion(requestion q);
		 * to add it to requestion_queue;
		 * 
		 * when you want to moniter a taxi
		 * use s.moniter(int i)to get a taxi's condition
		 * it will end when this car try to sleep for a while
		 * 
		 * when you want to get all car'condition
		 * use s.show_all_condition()
		 * 
		 * when you want to moniter a taxi automitically
		 * use s.auto_moniter();
		 * it will moniter next working car until it sleep;
		 * 
		 * if you want to get a advanced_car's listInterator
		 * use s.xcjly_get(i, ii)
		 * i is the num of the car
		 * ii is the num of the passanger
		 * it will return a listInterator of point!!
		 * you could use next()to get the point
		 * you could use x() to get the point'x
		 * you could use y() to get the point'y
		 */
		//while(true){
			
			s.auto_moniter();
			Random rand=new Random();
			s.get_requestion(new requestion(25,25,50,50));
			s.get_requestion(new requestion(rand.nextInt(80),rand.nextInt(80),rand.nextInt(80),rand.nextInt(80)));
			System.out.println(s.xcjly_get(79,0).next().x());
			s.get_requestion(new requestion(rand.nextInt(80),rand.nextInt(80),rand.nextInt(80),rand.nextInt(80)));
			s.get_requestion(new requestion(rand.nextInt(80),rand.nextInt(80),rand.nextInt(80),rand.nextInt(80)));
			s.get_requestion(new requestion(rand.nextInt(80),rand.nextInt(80),rand.nextInt(80),rand.nextInt(80)));
			s.get_requestion(new requestion(rand.nextInt(80),rand.nextInt(80),rand.nextInt(80),rand.nextInt(80)));
			//}
		//}
		}
		catch(Exception e){
			System.out.println("请勿进行极端操作");
		}
		
		
	}
}
