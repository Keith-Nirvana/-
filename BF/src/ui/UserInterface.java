package ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;

public class UserInterface extends Application{
	Parent root;
	private RemoteHelper remoteHelper;
	
	public UserInterface(){
		linkToServer();
	}
	
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));
			//System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception{
		root = FXMLLoader.load(getClass().getResource("ui.fxml"));
		
		Scene scene = new Scene(root, 840, 584);
		stage.setTitle("Interpreter");
		stage.setScene(scene);
		stage.show();
	}
	
	
	public static void main(String[] args) {
		UserInterface ui = new UserInterface();
		Application.launch(args);
	}

}
