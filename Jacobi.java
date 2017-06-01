import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Jacobi{
    
    
    private static void println(String s){System.out.println(s);}
    private static void print(String s){System.out.print(s); }
    
    private static double grid [][];
    private static double newGrid [][];
    public static void main(String [] args)throws InterruptedException{
        
        File file;
        Scanner fileScan;
        String fileName   = "";
        
        if(args.length == 1)
            fileName = args[0];
            
        try{
            fileScan = new Scanner( new File(fileName));
            //initGrid(fileScan);            
            String format = "%1.5f ";
            

            jacobiSmear(9 000);
            jacobi( 9000, 1 );
            jacobi( 9000, 2 );
            jacobi( 9000, 4 );
            jacobi( 9000, 6 );
                                            
        }catch(FileNotFoundException e){
            println("File not found");
            System.exit(3);
        }
    }
    
    
    private static void init(Scanner in){        
        grid = new double [2048][2048];
        newGrid = new double [2048][2048];
        
        //init grid
        for(int i =0; i < N; i++)
            for(int j =0; j < N; j++){
                grid[i][j] = Double.parseDouble(in.next());    
                newGrid[i][j]=0.0;
            }
        in.close();            
    }
    
   
        
    private static void jacobi( int MAXITERS, int NOTH) throws InterruptedException {        
                
        int N = grid.length;        
        int height = N / NOTH;        
        Thread[] thds = new Thread[NOTH];                    
             
        //CyclicBarrier barrier = new CyclicBarrier(NOTH);
        Barrier barrier = new Barrier(NOTH);
        
        
        //long start = System.nanoTime();
        for(int i = 0; i < NOTH; i++)
            maxDiff[i] = 0.0;
        
        //co block, init NOTH threads
        for(int w =0; w < NOTH; ++w){    
            int firstRow = w*height;
            int lastRow = firstRow + height-1;
            
            final int W = w;
            
            //thread job assignment
            thds[w] = new Thread(
                new Runnable(){
                    public void run(){
                            
                           // System.out.println("runnable");                            
                            try { barrier.waitForMe();}
                            catch (InterruptedException ex) {return;} 
                            //catch (BrokenBarrierException ex) {return;}
                           // println("1");
                            //row for computation
                            for(int iters = 1; iters < MAXITERS; iters++){
                                for(int i = firstRow+1; i < lastRow; ++i){
                                    for(int j = 1; j< N-1; ++j)
                                        newGrid[i][j] = (grid[i-1][j] + grid[i+1][j] + grid[i][j-1]+ grid[i][j+1])*0.25;
                                }
                                
                                try { barrier.waitForMe();}
                                catch (InterruptedException ex) {return;}
                              //  catch (BrokenBarrierException ex) {return;}
                                
                                for(int i = firstRow+1; i< lastRow; i++) {
                                    for(int j = 1; j < N -1; j++) {
                                        grid[i][j] = (newGrid[i-1][j] + newGrid[i+1][j] + newGrid[i][j-1]+ newGrid[i][j+1])*0.25;
                                    }
                                }
                                
                                try { barrier.waitForMe ();}
                                catch (InterruptedException ex) {return;}
                               // catch (BrokenBarrierException ex) {return;}
                            }//end maxIters, runnable
                           // println("2");
                            double diff = 0.0;
                            for(int i = firstRow; i < lastRow; i++) {
                                for(int j = 1; j < N ; j++) {
                                    diff = Math.max(diff,Math.abs(grid[i][j] - newGrid[i][j]));
                                }
                            }//end maxDiff comp
                            
                            maxDiff[W] = diff;
                            try { barrier.waitForMe();}
                            catch (InterruptedException ex) {return;}
                       //     println("3");
                            //catch (BrokenBarrierException ex) {return;}                            
                    }//end run
                }//end Runnable
                
            );//end Thread
            thds[w].start();            
        }//end thread init
        
      //  println("thds length "+thds.length);
        
        for(int j = 0; j < NOTH; ++j){
            thds[j].join();
           // System.out.println("joining");
        }    
        long end = System.nanoTime();    
        println("Multithreading, Total time: "+ (end - start)*0.000000001);
       
//        String format = "%1.10f ";            
/*            
         for(int x =0; x < N; ++x){
             for(int y =0; y < N; ++y)
                 System.out.format(format, grid[x][y]) ;
             print("\n");
          } */                   
    }
    
    private static void jacobiSmear(Scanner in, int maxIters){

        //grid = new double [2048][2048];
        //int N = grid.length;
        double threshold = 0.0001;
        //double [][] newGrid = new double [N][N];       
        double max=1.0;        
        String format = "%.10f ";
        
        long start = System.nanoTime();
/*
        for(int i =0; i < N; i++)
            for(int j =0; j < N; j++){
                grid[i][j] = Double.parseDouble(in.next());    
                newGrid[i][j]=0.0;
            } */       
        
        for(int k = 0; k< maxIters; k++){
            
            for(int i = 1; i < N-1; ++i)
                for(int j = 1; j< N-1; ++j)
                    newGrid[i][j] = (grid[i-1][j] + grid[i+1][j] + grid[i][j-1]+ grid[i][j+1])*0.25;
                
     
            for(int i = 1; i < N-1; ++i)
                for(int j = 1; j< N-1; ++j)
                    grid[i][j] = newGrid[i][j];
        }
            
        for(int i = 0; i < N; ++i){
            for(int j = 0; j< N; ++j){
                max = Math.max(max, Math.abs(newGrid[i][j]-grid[i][j])  ); 
                //print(grid[i][j] + " ");
              //  System.out.format(format, grid[i][j]);
            }
            //println("");
        }
        
        long end = System.nanoTime();    
        println("Sequential, Total time: "+ (end - start)*0.000000001);        
    }
}










































/*
        grid = new double [50][50];
        int N = grid.length;
        double threshold = 0.0001;
        double [][] newGrid = new double [N][N];       
        double max=1.0;
        double oldMax;        
        
        
        for(int i =0; i < N; i++)
            for(int j =0; j < N; j++){
                grid[i][j] = Double.parseDouble(in.next());    
                newGrid[i][j]=0.0;
            }
        
        while(true){
            
            for(int i = 1; i < N-1; ++i)
                for(int j = 1; j< N-1; ++j)
                    newGrid[i][j] = (grid[i-1][j] + grid[i+1][j] + grid[i][j-1]+ grid[i][j+1])*0.25;
            
            oldMax = max;
            max = 0.0;
            
            for(int i = 0; i < N; ++i)
                for(int j = 0; j< N; ++j)
                    max = Math.max(max, Math.abs(newGrid[i][j]-grid[i][j])  ); 
             
           // println( max+ "");
            
            if( max < threshold)
                break;
     
            for(int i = 1; i < N-1; ++i)
                for(int j = 1; j< N-1; ++j)
                    grid[i][j] = newGrid[i][j];

                    
            for(int i = 0; i < N; ++i){
                for(int j = 0; j< N; ++j)
                    print(grid[i][j] + " ");
                println("");
            }   
        }//end loop
        
        for(int i = 0; i < N; ++i){
            for(int j = 0; j< N; ++j)
                print(grid[i][j] + " ");
            println("");
*/