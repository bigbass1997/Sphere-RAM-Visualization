package com.bigbass1997.sphereram.ram;

import java.util.LinkedList;

import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;

/**
 * @see <a href="https://github.com/bigbass1997/Rectangular-Approximation-Method/blob/master/src/com/bigbass1997/ram/RAMUtil.java">Original Source on Github</a>
 * @see <a href="https://github.com/darius/expr">Expr by Darius on Github</a>
 */
public class RAMUtil {
	
	/**
	 * <p>Left Rectangular Approximation Method</p>
	 * 
	 * @param initial value simply added to the total
	 * @param yList ordered list of all the 'y' or height values normally generated by a function
	 * @param x the 'x' or width increment value
	 * @return the left approximated value of the area under a curve
	 */
	public static double LRAM(double initial, LinkedList<Double> yList, double x){
		double total = initial;
		for(int i = 0; i < yList.size(); i++){
			total += (yList.get(i) * x);
		}
		return total;
	}

	/**
	 * <p>Middle Rectangular Approximation Method</p>
	 * 
	 * @param initial value simply added to the total
	 * @param yList ordered list of all the 'y' or height values normally generated by a function
	 * @param x the 'x' or width increment value
	 * @return the middle approximated value of the area under a curve
	 */
	public static double MRAM(double initial, LinkedList<Double> yList, double x){
		double total = initial;
		for(int i = 0; i < yList.size(); i++){
			double n1, n2;
			
			n1 = yList.get(i);
			
			//In order to average the final number, it must be averaged with 0 to avoid crashing behavior.
			try {
				n2 = yList.get(i + 1);
			} catch(Exception e){
				n2 = 0;
			}
			
			total += (((n1 + n2) / 2) * x);
		}
		return total;
	}

	/**
	 * <p>Right Rectangular Approximation Method</p>
	 * 
	 * @param initial value simply added to the total
	 * @param yList ordered list of all the 'y' or height values normally generated by a function
	 * @param x the 'x' or width increment value
	 * @return the right approximated value of the area under a curve
	 */
	public static double RRAM(double initial, LinkedList<Double> yList, double x){
		double total = initial;
		for(int i = 1; i < yList.size(); i++){
			total += (yList.get(i) * x);
		}
		return total;
	}
	
	/**
	 * <p>Gets the list of y values based on a certain width, used to increment the x value used
	 * in the function, according to the "function" that is hard coded into this method.</p>
	 * <p>Note that the smaller the width is, the more numbers will be added to the returned list.
	 * Which when combined with the RAM functions, allow greater accuracy but also longer calculation time.</p>
	 * 
	 * @param min lowest x value
	 * @param max highest x value
	 * @param width value used to increase x incrementally until x reaches max
	 * @return ordered list of y values based on hard coded "function" using x
	 */
	public static LinkedList<Double> getList(String func, double min, double max, double radius, double width){
		LinkedList<Double> list = new LinkedList<Double>();
		Variable x = Variable.make("x");
		Variable r = Variable.make("r");
		Expr expr = null;
		try {
			expr = Parser.parse(func);
		} catch (SyntaxException e) {
			e.printStackTrace();
			return list;
		}
		
		x.setValue(min);
		r.setValue(radius);
		while(x.value() <= max){
			double val = expr.value();
			
			list.add(Double.valueOf(val));
			
			x.setValue(x.value() + width); //Increase x value based on provided width. Ensures that all values are equally incremented.
		}
		
		return list;
	}
}