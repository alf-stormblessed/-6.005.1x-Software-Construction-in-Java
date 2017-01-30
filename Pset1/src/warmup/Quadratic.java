package warmup;

import java.util.HashSet;
import java.util.Set;

public class Quadratic {

    /**
     * Find the integer roots of a quadratic equation, ax^2 + bx + c = 0.
     * @param a coefficient of x^2
     * @param b coefficient of x
     * @param c constant term.  Requires that a, b, and c are not ALL zero.
     * @return all integers x such that ax^2 + bx + c = 0.
     */
    public static Set<Integer> roots(int a, int b, int c) {
        
        Set<Integer> solution = new HashSet<Integer>();
        double insideSqrt = -4*(double)c*(double)a;
        double x1= 0, x2 = 0;

        if (Math.pow(b, 2) + (long)insideSqrt < (double)0){// Imaginary roots - returns no solution

            return solution;
        }
        else if (a == 0){// Degenerate roots - returns one solution
            
            x1 = -(double)c/(double)b;
            System.out.println("sol " + x1);

            if ((int)x1-x1 == 0){// if solution is  an int - adds solution
                
                solution.add((int)x1);
                return solution;
                }
            else {return solution;}

        }
        x1 = (- (double)b + Math.sqrt(Math.pow(b,2) + (double)insideSqrt))/(double)(2*a);
        x2 = (- (double)b - Math.sqrt(Math.pow(b,2) + (double)insideSqrt))/(double)(2*a);

        if((int)x1-x1 != 0 || (int)x2-x2!=0){
            
            return solution;
        }
        else{

            solution.add((int)x1);
            if (x1!=x2){
        
                solution.add((int)x2);
            }
            
        }
        

        return solution;
   }

    
    /**
     * Main function of program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("For the equation x^2 - 4x + 3 = 0, the possible solutions are:");
        Set<Integer> result = roots(0, -5, 5*3);
        System.out.println(result);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
