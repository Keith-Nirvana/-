package serviceImpl;

import java.rmi.RemoteException;
import service.UserService;

import java.io.*;

public class UserServiceImpl implements UserService{
	String theUsername = "";
	
	@Override
	public boolean login(String username, String password) throws RemoteException {
		File file = new File("UserInfo.txt");
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String pieceInfo;
			String[] detailInfo;
			while((pieceInfo = reader.readLine()) != null){
				detailInfo = pieceInfo.split(" ");
				
				if(detailInfo[0].equals(username) && detailInfo[1].equals(password)){
					theUsername = username;
					reader.close();
					return true;
				}
			}
			
			reader.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean logout(String username) throws RemoteException {		
		if(!theUsername.equals("")){
			theUsername = "";
			return true;
		}
		else{
			return false;
		}

	}

	@Override
	public boolean register(String username, String password){
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File("UserInfo.txt")));
			
			String pieceInfo;
			String[] detailInfo;
			while((pieceInfo = reader.readLine()) != null){
				detailInfo = pieceInfo.split(" ");
				
				if(detailInfo[0].equals(username)){
					reader.close();
					return false;
				}
			}
			
			
			FileWriter writer = new FileWriter(new File("UserInfo.txt"), true);
			writer.write(System.lineSeparator());
			writer.write(username + " " + password);
			writer.flush();
			writer.close();
			reader.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return true;
	}
	
	@Override
	public boolean checkUser(String username){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File("UserInfo.txt")));
			
			String pieceInfo;
			String[] detailInfo;
			while((pieceInfo = reader.readLine()) != null){
				detailInfo = pieceInfo.split(" ");
				
				if(detailInfo[0].equals(username)){
					reader.close();
					return true;
				}
			}
			
			reader.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		return false;
	}
}
