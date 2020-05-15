package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	int e=0;
    	Stack<String>array=new Stack<String>();
    	Stack<String>array2=new Stack<String>();
    	Stack<String>array3=new Stack<String>();
    	Stack<String>array4=new Stack<String>();
    	StringTokenizer  one=new StringTokenizer(expr,delims, true);
    	while (one.hasMoreTokens())
		{
			array.push(one.nextToken());
		}
    	while (array.isEmpty()==false)
    	{
    		array2.push(array.pop());
    		if(array2.peek().equals("["))
    		{
    			e=1;
    			String temp=array.peek();
    			Array j=new Array(temp);
    			while (array3.isEmpty()==false)
    			{ 
    				String d=array3.pop();
    				array4.push(d);
    				if (j.name.equals(d))
    				{
    					e--;
    					break;
    				}
    			}
    			if (e==1)
    			{
    				j.values=null;
    				arrays.add(j);
    				array3.push(j.name);
    			}    
    			putTogether(array3,array4);
    		}
    	}
    	Stack<String>copy=new Stack<String>();
    	Stack<String>copy2=new Stack<String>();
    	int c=1;
    	StringTokenizer  two=new StringTokenizer(expr, delims+ "0123456789", false);
    	while (two.hasMoreTokens())
    	{
    		c=1;
    		Variable r=new Variable ("");
    		r.name=two.nextToken();
    		while (copy.isEmpty()==false)
    		{ 
    			String d=copy.pop();
    			copy2.push(d);
    			if (r.name.equals(d))
    			{
    				c--;
    				break;
    			}
    		}
    		while (array3.isEmpty()==false)
    		{ 
    			String d=array3.pop();
    			array4.push(d);
    			if (r.name.equals(d))
    			{
    				c--;
    				break;
    			}
    		}
    		if (c==1)
    		{
    			r.value=0;
    			vars.add(r);
    			copy.push(r.name);
    		}    
    		putTogether(copy,copy2);
    		putTogether(array3,array4);
    	}
    }
   
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	if (vars.isEmpty()==false && arrays.isEmpty()==true)
    	{
    		String t="";
    		Stack <Variable>variablelist=new Stack<Variable>();
    		Stack <String>tempexpression=new Stack<String>();
    		Stack <String>correctexpression=new Stack<String>();
    		StringTokenizer  fix=new StringTokenizer(expr, delims,true);
    		while (fix.hasMoreTokens())
    		{
    			tempexpression.push(fix.nextToken());
    		}
    		for (int i=0; i<vars.size();i++)
    		{
    			variablelist.push(vars.get(i));
    		}
    		while (tempexpression.isEmpty()==false)
    		{
    			if (!tempexpression.peek().equals(" "))
    			{
    				correctexpression.push(tempexpression.pop());
    			}
    			else
    			{
    				tempexpression.pop();
    			}
    		}
    		while (variablelist.isEmpty()==false)
    		{
    			Variable l=variablelist.pop();
    			while (correctexpression.isEmpty()==false)
    			{
    				if (l.name.equals(correctexpression.peek()))
    				{
    					tempexpression.push(Integer.toString(l.value));
    					correctexpression.pop();
    				}
    				else
    				{
    					tempexpression.push(correctexpression.pop());
    				}
    			}
    			putTogether(correctexpression,tempexpression);
    		}
    		correctexpression=calculate(correctexpression);
    		String r=correctexpression.pop();
    		return Float.parseFloat(r);
    	}
    	else if (arrays.isEmpty()==false)
    	{
    		Stack <Array>arraylist=new Stack<Array>();
    		Stack <String>tempexpression=new Stack<String>();
    		Stack <String>correctexpression=new Stack<String>();
    		Stack <Variable>variablelist=new Stack<Variable>();
    		for (int i=0; i<vars.size();i++)
    		{
    			variablelist.push(vars.get(i));
    		}
    		StringTokenizer  fix=new StringTokenizer(expr, delims,true);
    		while (fix.hasMoreTokens())
    		{
    			tempexpression.push(fix.nextToken());
    		}
    		for (int i=0; i<arrays.size();i++)
    		{
    			arraylist.push(arrays.get(i));
    		}
    		while (tempexpression.isEmpty()==false)
    		{
    			if (!tempexpression.peek().equals(" "))
    			{
    				correctexpression.push(tempexpression.pop());
    			}
    			else
    			{
    				tempexpression.pop();
    			}
    		}
    		while (variablelist.isEmpty()==false)
    		{
    			Variable l=variablelist.pop();
    			while (correctexpression.isEmpty()==false)
    			{
    				if (l.name.equals(correctexpression.peek()))
    				{
    					tempexpression.push(Integer.toString(l.value));
    					correctexpression.pop();
    				}
    				else
    				{
    					tempexpression.push(correctexpression.pop());
    				}
    			}
    			putTogether(correctexpression,tempexpression);
    		}
    		int c=check2(correctexpression);
    		int l=0;
    		while(c!=0)
    		{
    			correctexpression=arraysolver(correctexpression);
        		correctexpression=solver(correctexpression,arraylist);
    			l++;
    			if (l==c)
    				break;
    		}  		
    		correctexpression=calculate(correctexpression);
    		String r=correctexpression.pop();
    		return Float.parseFloat(r);
    	}
    	else
    	{
    		Stack <String>expression=new Stack<String>();
    		Stack <String>expression2=new Stack<String>();
    		StringTokenizer  rum=new StringTokenizer(expr, delims, true);
    		while (rum.hasMoreTokens())
    		{
    			expression2.push(rum.nextToken());
    		}
    		while (expression2.isEmpty()==false)
    		{
    			if (!expression2.peek().equals(" "))
    			{
    				expression.push(expression2.pop());
    			}
    			else
    			{
    				expression2.pop();
    			}
    		}
    		expression=calculate(expression);
    		String r=expression.pop();
    		return Float.parseFloat(r);
    }    		
}
private static Stack<String> putTogether(Stack<String>r, Stack<String> s){
	while (s.isEmpty()==false)
	{
		r.push(s.pop());
	}
	return r;
}
private static Stack<String> multiplydivide(Stack<String> r)
{
	Stack <String>expression=r;
	Stack <String>expression2=new Stack<String>();
	while (expression.isEmpty()==false)
	{
		if(expression.peek().equals("*"))
		{
			expression.pop();
			String temp1=expression2.pop();
			String temp2=expression.pop();
			float tempfl=Float.parseFloat(temp1);
	    	float tempfl2=Float.parseFloat(temp2);
	    	float tempfl3=tempfl*tempfl2;
	    	expression2.push(String.valueOf(tempfl3));
		}
		else if(expression.peek().equals("/"))
		{
			expression.pop();
			String temp1=expression2.pop();
			String temp2=expression.pop();
			float tempfl=Float.parseFloat(temp1);
	    	float tempfl2=Float.parseFloat(temp2);
	    	float tempfl3=tempfl/tempfl2;
	    	expression2.push(String.valueOf(tempfl3));
		}
		else
		{
			expression2.push(expression.pop());
		}
	}
	return putTogether(expression,expression2);
}
private static Stack<String> addsubtract(Stack<String> r)
{
	Stack <String>expression=r;
	Stack <String>expression2=new Stack<String>();
	while (expression.isEmpty()==false)
	{
		if(expression.peek().equals("+"))
		{
			expression.pop();
			String temp1=expression2.pop();
			String temp2=expression.pop();
			float tempfl=Float.parseFloat(temp1);
	    	float tempfl2=Float.parseFloat(temp2);
	    	float tempfl3=tempfl+tempfl2;
	    	expression2.push(String.valueOf(tempfl3));
		}
		else if(expression.peek().equals("-"))
		{
			expression.pop();
			String temp1=expression2.pop();
			String temp2=expression.pop();
			float tempfl=Float.parseFloat(temp1);
	    	float tempfl2=Float.parseFloat(temp2);
	    	float tempfl3=tempfl-tempfl2;
	    	expression2.push(String.valueOf(tempfl3));
		}
		else
		{
			expression2.push(expression.pop());
		}
	}
	return putTogether(expression,expression2);
}
private static Stack<String> parentheses(Stack<String>r)
{
	Stack <String>expression=r;
	Stack <String>expression2=new Stack<String>();
	Stack <String>temp=new Stack<String>();
	Stack <String>temp2=new Stack<String>();
	Stack <String>temp3=new Stack<String>();
	while (expression.isEmpty()==false)
	{
		outerloop:
		if(expression.peek().equals("("))
		{
			expression.pop();
			while(!expression.peek().equals(")"))
			{
				if(expression.peek().equals("("))
				{
					expression2.push("(");
					putTogether(temp3,temp);
					putTogether(expression2,temp3);
					break outerloop;
				}
				temp.push(expression.pop());
			}
				expression.pop();
				putTogether(temp2,temp);
				temp2=multiplydivide(temp2);
				temp2=addsubtract(temp2);
				expression2.push(temp2.pop());
				break;
		}
		else
		{
			expression2.push(expression.pop());
		}
	}
	return putTogether(expression,expression2);
}
private static int check(Stack<String>l)
{
	Stack<String>r=l;
	int c=0;
	Stack<String>j=new Stack<String>();
	while (r.isEmpty()==false)
	{
		if (r.peek().equals("("))
		{
			c++;
		}
		j.push(r.pop());
	}
	putTogether(r,j);
	return c;
}
private static Stack<String>calculate(Stack<String>r)
{
	int c=check(r);
	int l=0;
	while(c!=0)
	{
		r=parentheses(r);
		l++;
		if (l==c)
			break;
		
	}
	r=multiplydivide(r);
	r=addsubtract(r);
	return r;
}
private static Stack<String>arraysolver(Stack<String>r)
{
	Stack <String>expression=r;
	Stack <String>expression2=new Stack<String>();
	Stack <String>temp=new Stack<String>();
	Stack <String>temp2=new Stack<String>();
	Stack <String>temp3=new Stack<String>();
	while (expression.isEmpty()==false)
	{
		outerloop:
		if(expression.peek().equals("["))
		{
			expression.pop();
			while(!expression.peek().equals("]"))
			{
				if(expression.peek().equals("["))
				{
					expression2.push("[");
					putTogether(temp3,temp);
					putTogether(expression2,temp3);
					break outerloop;
				}
				temp.push(expression.pop());
			}
				expression.pop();
				putTogether(temp2,temp);
				temp2=calculate(temp2);
				expression2.push("[");
				expression2.push(temp2.pop());
				expression2.push("]");
				break;
		}
		else
		{
			expression2.push(expression.pop());
		}
    }
	return putTogether(expression,expression2);
}
private static Stack<String>solver(Stack<String>b, Stack<Array>c)
{
	Stack <String>expression=b;
	Stack <String>expression2=new Stack<String>();
	Stack <String>temp=new Stack<String>();
	Stack <String>temp2=new Stack<String>();
	Stack <String>temp3=new Stack<String>();
	Stack <Array>array=new Stack<Array>();
	while (expression.isEmpty()==false)
	{
		outerloop:
		if(expression.peek().equals("["))
		{
			expression.pop();
			while(!expression.peek().equals("]"))
			{
				if(expression.peek().equals("["))
				{
					expression2.push("[");
					putTogether(temp3,temp);
					putTogether(expression2,temp3);
					break outerloop;
				}
				temp.push(expression.pop());
			}
			expression.pop();
			putTogether(temp2,temp);
			String d=temp2.pop();
			float t=Float.parseFloat(d);
			int r=(int)t;
			String e=expression2.pop();
			while (c.isEmpty()==false)
			{
				if(c.peek().name.equals(e))
				{
					int value=c.peek().values[r];
					String correct=Integer.toString(value);
					expression2.push(correct);
					break;
				}
				array.push(c.pop());
			}
			while (array.isEmpty()==false)
			{
				c.push(array.pop());
			}
			break;
		}
		else
		{
			expression2.push(expression.pop());
		}
    }
	return putTogether(expression,expression2);
}
private static int check2(Stack<String>l)
{
	Stack<String>r=l;
	int c=0;
	Stack<String>j=new Stack<String>();
	while (r.isEmpty()==false)
	{
		if (r.peek().equals("["))
		{
			c++;
		}
		j.push(r.pop());
	}
	putTogether(r,j);
	return c;
}
}
