package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.IOService;
import service.UserService;
import service.ExecuteService;

import serviceImpl.InterpreterOok;
import serviceImpl.InterpreterBF;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService1;
	private ExecuteService executeService2;
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService1 = new InterpreterBF();
		executeService2 = new InterpreterOok();
	}

	@Override
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId, fileName);
	}

	@Override
	public String readFile(String userId, String fileName) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, fileName);
	}

	@Override
	public String readFileList(String userId) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}
	
	@Override
	public boolean register(String username, String password) throws RemoteException {
		
		return userService.register(username, password);
	}
	
	@Override
	public boolean checkUser(String username) throws RemoteException{
		
		return userService.checkUser(username);
	}
	
	@Override
	public String execute(String code, String param) throws RemoteException{
		boolean tempFlag = true; //看看是不是BF
		
		for(int i = 0; i < code.length(); i++){
			if(code.charAt(i) == 'o'){
				tempFlag = false;
				break;
			}
		}
		
		
		if(tempFlag) 
			return executeService1.execute(code, param);
		else
			return executeService2.execute(code, param);
	}
}
