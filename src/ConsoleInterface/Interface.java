package ConsoleInterface;

import java.util.Scanner;

public class Interface {
   
    public double[][] implement() {
           
        Scanner in = new Scanner(System.in);
        System.out.print("Input a number of nodes: ");
        int num = in.nextInt();
        double [][] data = new double[num][9];
        System.out.print("Input a radius: ");
    	data[0][0] = in.nextInt();
    	System.out.print("Input a thickness: ");
    	data[0][1] = in.nextInt();
    	System.out.print("Input a Young's modulus: ");
    	data[0][2] = in.nextInt();
        for(int i=0;i<num;i++) {
        	System.out.println("Node ¹"+i+":");        	
        	System.out.print("Input X1: ");
        	data [i+1][0] = in.nextInt();        	
        	System.out.print("Input X2: ");
        	data [i+1][1] = in.nextInt();
        	System.out.print("Input X3: ");
        	data [i+1][2] = in.nextInt();
        	System.out.print("Choose a constraint of X1 (t(1) or f(0)):");
        	data [i+1][6] = in.nextInt();
        	System.out.print("Choose a constraint of X2 (t(1) or f(0)):");
        	data [i+1][7] = in.nextInt();
        	System.out.print("Choose a constraint of X3 (t(1) or f(0)):");
        	data [i+1][8] = in.nextInt();
        	System.out.print("Set a force of X1 (t(1) or f(0)):");
        	data [i+1][3] = in.nextInt();
        	System.out.print("Set a force of X2 (t(1) or f(0)):");
        	data [i+1][4] = in.nextInt();
        	System.out.print("Set a force of X3 (t(1) or f(0)):");
        	data [i+1][5] = in.nextInt();
        }       
       in.close();
       return data;
    }    
}