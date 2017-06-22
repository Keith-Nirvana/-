package serviceImpl;


import service.ExecuteService;
import java.util.*;

public class InterpreterBF implements ExecuteService{
	String code;
	
	ArrayList<Integer> array = new ArrayList<>();
	StringBuilder result = new StringBuilder();
	private int arrayPointer = 0;
	private int paramPointer = 0;
	private int codePointer = 0;
	
	ArrayList<Integer> stack = new ArrayList<>();
	private int stackPointer = -1;
	
	@Override
	public String execute(String code, String param) {
		array.add(0);
		this.code = code;
		
		while(codePointer < code.length()){
			
			switch (code.charAt(codePointer)){
				case '+':	add();
							break;
				case '-':	sub();
							break;
				case '>':	addPtr();
							break;
				case '<':	subPtr();
							break;
				case '.':   output();
							break;
				case ',':	input(param.charAt(paramPointer));
							break;
				case '[':	fLoop();
							break;
				case ']':	lLoop();
							break;
				default:	return "Error!";
			}			
		}
		
		return result.toString();
	}
	
	public void printList(){
		for(int i = 0; i < array.size(); i++)
			System.out.print(array.get(i)+" ");
		System.out.println();
	}
	
	public void add(){
		int temp = array.get(arrayPointer); 
		array.set(arrayPointer, temp+1);
		
		codePointer++;
	}
	
	public void sub(){
		int temp = array.get(arrayPointer);
		array.set(arrayPointer, temp-1);
		
		codePointer++;
	}
	
	public void addPtr(){
		if(arrayPointer == array.size()-1){
			array.add(0);
		}
		
		arrayPointer++;
		codePointer++;
	}
	
	public void subPtr(){
		arrayPointer--;
		codePointer++;
	}
	
	public void output(){
		result.append((char)array.get(arrayPointer).intValue());
		codePointer++;
	}
	
	public void input(char c){
		array.set(arrayPointer, (int)c);
		
		paramPointer++;
		codePointer++;
	}
	
	public void fLoop(){
		if(array.get(arrayPointer) == 0){
			int counter = 0;
			
			while(true){
				codePointer++;
				
				if(code.charAt(codePointer) == '[')
					counter++;
				
				if(code.charAt(codePointer) == ']' && counter == 0){
					codePointer++;
					break;
				}
				
				if(code.charAt(codePointer) == ']' && counter != 0)
					counter--;
			}
		}
		
		
		else{
			stack.add(codePointer);
			stackPointer++;
			codePointer++;
		}
		
		
		
	}
	
	public void lLoop(){
		if(array.get(arrayPointer) == 0){
			stack.remove(stackPointer);
			stackPointer--;
			
			codePointer++;
		}
		
		else{
			codePointer = stack.get(stackPointer);
			codePointer++;
		}
	}
	
	public static void main(String[] args){
		InterpreterBF bf = new InterpreterBF();
		System.out.println(bf.execute(">+++++++++[<++++++++>-]<.>+++++++[<++++>-]<+.+++++++..+++.>>>++++++++[<++++>-]<.>>>++++++++++[<+++++++++>-]<---.<<<<.+++.------.--------.>>+.", "2 3 "));
		System.out.println(bf.code);
	}
}
