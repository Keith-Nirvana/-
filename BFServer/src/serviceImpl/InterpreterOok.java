package serviceImpl;

import java.util.ArrayList;

import service.ExecuteService;

public class InterpreterOok implements ExecuteService{
	String code;
	
	ArrayList<Integer> array = new ArrayList<>();
	StringBuilder result = new StringBuilder();
	private int arrayPointer = 0;
	private int paramPointer = 0;
	private int codePointer = 0;
	
	ArrayList<Integer> stack = new ArrayList<>();
	private int stackPointer = -1;
	
	
	public String execute(String code, String param){
		String realCode = transist(code);
		this.code = realCode;
		
		array.add(0);
		
		while(codePointer < realCode.length()){
			
			switch (realCode.charAt(codePointer)){
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
	
	public String transist(String someCode){
		int length = someCode.length() / 10;
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i < length; i++){
			if(someCode.substring(10*i, 10*(i+1)).equals("Ook. Ook? "))
				result.append('>');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook? Ook. "))
				result.append('<');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook. Ook. "))
				result.append('+');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook! Ook! "))
				result.append('-');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook! Ook. "))
				result.append('.');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook. Ook! "))
				result.append(',');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook! Ook? "))
				result.append('[');
			
			else if(someCode.substring(10*i, 10*(i+1)).equals("Ook? Ook! "))
				result.append(']');
			
			else
				result.append('#');
		}
		
		if(someCode.length() % 10 != 0){
			if(someCode.endsWith("Ook. Ook?"))
				result.append('>');
			
			else if(someCode.endsWith("Ook? Ook."))
				result.append('<');
			
			else if(someCode.endsWith("Ook. Ook."))
				result.append('+');
			
			else if(someCode.endsWith("Ook! Ook!"))
				result.append('-');
			
			else if(someCode.endsWith("Ook! Ook."))
				result.append('.');
			
			else if(someCode.endsWith("Ook. Ook!"))
				result.append(',');
			
			else if(someCode.endsWith("Ook! Ook?"))
				result.append('[');
			
			else if(someCode.endsWith("Ook? Ook!"))
				result.append(']');
			
			else
				result.append('#');
			
		}
		
		return result.toString();
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
		//InterpreterOok o = new InterpreterOok();
		//String s = o.transist("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook. Ook! Ook.");
		//System.out.println(new InterpreterBF().execute(s, ""));
		System.out.println(new InterpreterOok().execute("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook. Ook! Ook. ", ""));
	}
}
