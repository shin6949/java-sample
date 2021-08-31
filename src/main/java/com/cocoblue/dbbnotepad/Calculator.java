package com.cocoblue.dbbnotepad;

// ��ó: https://webclass.tistory.com/entry/JAVA-Infix-%EC%82%AC%EC%B9%99%EC%97%B0%EC%82%B0-%EA%B3%84%EC%82%B0%EA%B8%B0

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

public class Calculator {
	private String inputStream;
	private Stack<Character> operator = new Stack();
	private Stack<Double> operand = new Stack();
	private Vector<Integer> list = new Vector();
	private double result = 0;
	
	public Calculator() { }
	
	public Calculator(String input) {
		inputStream = input;
	}
	
	public void findOrder() {
		StringTokenizer st = new StringTokenizer(inputStream);
		
		int countMul = 0; 
		int totalCount = 0; 
		
		while(st.countTokens() > 1) {
			operand.push(Double.parseDouble(st.nextToken()));
			String oper = st.nextToken();
			char[] tempOpper = oper.toCharArray();
			operator.push(tempOpper[0]);	
			
			switch(tempOpper[0]) {
			case '*':
				list.add(totalCount-countMul);
				countMul++;
				break;
			case '/':
				list.add(totalCount-countMul);
				countMul++;
				break;
			}
			
			totalCount++;
		}
		
		operand.push(Double.parseDouble(st.nextToken()));
		calc();
	}
	
	private void calc() {
		int a = list.size();
		int i = 0;

		while(a > 0) {
			int temp = list.get(i);
			
			char tempSaveOperator;
			double[] tempSaveOperand = {0 , 0};
			tempSaveOperator = operator.get(temp);
			tempSaveOperand[0] = operand.get(temp);
			tempSaveOperand[1] = operand.get(temp + 1);
			
			if(tempSaveOperator == '*') { operand.set(temp, tempSaveOperand[0] * tempSaveOperand[1]); }
			else if(tempSaveOperator == '/') { operand.set(temp, tempSaveOperand[0] / tempSaveOperand[1]); }
			
			operand.remove(temp + 1);
			operator.remove(temp);

			i++;
			a--;
		}

		int size = operator.size();
		int index = 1;
		result = operand.get(0);
		
		while(size > 0) {
			char tempSaveOperator;
			tempSaveOperator = operator.get(index - 1);
			double tempSaveOperand = operand.get(index);
			
			switch(tempSaveOperator) {
			case '+':
				result += tempSaveOperand; 
				break;
			case '-':
				result -= tempSaveOperand; 
				break;
			}
			
			size--;
			index++;
		}
	}
	
	public double returnResult() { return result; }
}