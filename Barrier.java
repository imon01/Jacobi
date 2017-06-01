//import java.util.concurrent.Semaphore;

public class Barrier{
    
    private volatile int counter = 0;
    private int NOTH;
    
    public Barrier(int NOTH){
        this.NOTH = NOTH;
    }
    
    
    public synchronized void waitForMe() throws InterruptedException{
        ++counter;
        
       // System.out.println("thread arrived!");
        if(counter < NOTH)
            wait();            
        else{//if(counter == NOTH){
         //   System.out.println("all threads go!");
            counter = 0; 
            notifyAll();
        }
        
        
        
    }
}//end class

