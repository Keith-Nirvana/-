package ui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.*;

import rmi.RemoteHelper;

import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.collections.*;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.URL;

import java.util.*;

public class Controller implements Initializable{
	
	@Override
	public void initialize(URL url, ResourceBundle rb){
		ObservableList<String> optionsOfLanguages = FXCollections.observableArrayList("bf", "ook");
		cbLanguageChooser.setItems(optionsOfLanguages);
	}
	
	@FXML
	private Pane mainPane;
	@FXML
	private Pane loginPane;
	
	//主面板上的内容---------------------------------------------
	//主面板MenuBar里面要用的结点
	@FXML
	private Menu menuFile;
	@FXML
	private MenuItem miNew;
	@FXML
	private MenuItem miOpen;
	@FXML
	private MenuItem miSave;
	@FXML
	private MenuItem miExit;
	@FXML
	private MenuItem miRun;
	
	@FXML
	private MenuItem miVersion1;
	@FXML
	private MenuItem miVersion2;
	@FXML
	private MenuItem miVersion3;	
	
	@FXML
	private ComboBox<String> cbLanguageChooser;
	@FXML
	private Button btLogout;
	@FXML
	private ImageView ivLoginIcon;
	@FXML
	private Label lblUser;
	
	//各个文本域
	@FXML
	private TextArea taCode;
	@FXML
	private TextArea taInput;
	@FXML
	private TextArea taOutput;
	
	//登陆面板上的内容----------------------------------------------
	@FXML
	private Button btLogin;
	@FXML
	private Button btRegister;
	
	@FXML
	private TextField tfUserName;
	@FXML
	private PasswordField pfPassword;
	
	@FXML
	private ImageView imReturn;
	
	private boolean userCheck;
	private boolean openFlag = false;
	//这三个是用来实现撤销和重做的
	private ArrayList<Character> codeContent = new ArrayList<>();
	private int pointer = 0;
	private boolean backspace = false;
	
	private ArrayList<String> listOfContent = new ArrayList<>();
	private String currentFile;
	
	//用来弹出的界面----------------------------------------------
	private Stage stage = new Stage();
	private Pane tempPane = new Pane();
	private Scene scene = new Scene(tempPane, 600, 400);
	
	private Stage saveStage = new Stage();
	private Pane savePane = new Pane();
	private Scene saveScene = new Scene(savePane, 600, 400);
	
	private Stage openStage = new Stage();
	private Pane openPane = new Pane();
	private Scene openScene = new Scene(openPane, 600, 400);
	
	public void onNewClicked(ActionEvent ev){
		//去除版本号
		miVersion1.setVisible(false);
		miVersion2.setVisible(false);
		miVersion3.setVisible(false);
		
		openFlag = false;
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;
		//界面文字去除
		taCode.setText("");
		taInput.setText("");
		taOutput.setText("");
	}
	
	public void onOpenClicked(ActionEvent ev){
		
		try{
			userCheck = RemoteHelper.getInstance().getUserService().checkUser(lblUser.getText());
			
			if(!userCheck){
				showStage("Please login first");
			}
			else{
				showOpenStage();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@FXML
	public void onSaveClicked(ActionEvent ev){
		try{
			userCheck = RemoteHelper.getInstance().getUserService().checkUser(lblUser.getText());
			
			if(!userCheck){
				showStage("Please login first");
			}
			else{
				showSaveStage();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void onExitClicked(ActionEvent ev){
		System.exit(0);
	}
	
	@FXML
	public void onRunClicked(ActionEvent ev){
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;
		
		try{
			taOutput.setText(RemoteHelper.getInstance().getExcecuteService().execute(taCode.getText(), taInput.getText()));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void onVersion1Clicked(ActionEvent ev){
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;
		
		if(listOfContent.size() >= 1){
			taCode.setText(listOfContent.get(0).split("\\*")[0]);
			taInput.setText(listOfContent.get(0).split("\\*")[1]);
		}
	}
	
	public void onVersion2Clicked(ActionEvent ev){
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;
		
		if(listOfContent.size() >= 2){
			taCode.setText(listOfContent.get(1).split("\\*")[0]);
			taInput.setText(listOfContent.get(1).split("\\*")[1]);
		}
	}
	
	public void onVersion3Clicked(ActionEvent ev){
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;
		
		if(listOfContent.size() >= 3){
			taCode.setText(listOfContent.get(2).split("\\*")[0]);
			taInput.setText(listOfContent.get(2).split("\\*")[1]);
		}
	}
	//撤销
	public void onUndoClicked(ActionEvent ev){
	
		StringBuilder sb = new StringBuilder();
		
		if(backspace){
			//判断是为了防止越界
			if(pointer < codeContent.size()){
				pointer++;
			
				for(int i = 0; i < pointer; i++)
					sb.append(codeContent.get(i));
			
				taCode.setText(sb.toString());
			}
			
		}else{
			
			if(pointer > 0){
				pointer--;
				
				for(int i = 0; i < pointer; i++)
					sb.append(codeContent.get(i));
				
				taCode.setText(sb.toString());
			}
			
		}
		
	}
	//重做
	public void onRedoClicked(ActionEvent ev){
		StringBuilder sb = new StringBuilder();
		
		if(!backspace){
			
			if(pointer < codeContent.size()){
				pointer++;
			
				for(int i = 0; i < pointer; i++)
					sb.append(codeContent.get(i));
			
				taCode.setText(sb.toString());
			}
			
		}else{
			
			if(pointer > 0){
				pointer--;
				
				for(int i = 0; i < pointer; i++)
					sb.append(codeContent.get(i));
				
				taCode.setText(sb.toString());
			}
			
		}
	}
	
	public void onCodePressed(KeyEvent kv){
		//切换输入法时什么也不做
		if(kv.getCode().equals(KeyCode.SHIFT) || kv.getCode().equals(KeyCode.CONTROL)) {}
		
		else if(!kv.getCode().equals(KeyCode.BACK_SPACE)){
			backspace = false;
			codeContent.add(pointer, kv.getText().charAt(kv.getText().length()-1));
			pointer++;
		}
		
		else if(kv.getCode().equals(KeyCode.BACK_SPACE)){
			backspace = true;
			//防止越界
			if(pointer > 0)
				pointer--;
		}
	}
	
	
	//用户登陆图标点选事件
	public void onUserIconClicked(MouseEvent me){	
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;

		loginPane.setVisible(true);
		mainPane.setVisible(false);
	}
	
	public void onLoginClicked(ActionEvent ev){
		boolean loginResult;
		
		try{
			loginResult = RemoteHelper.getInstance().getUserService().login(tfUserName.getText(), pfPassword.getText());
						
			if(loginResult){
				lblUser.setText(tfUserName.getText());
				userCheck = RemoteHelper.getInstance().getUserService().checkUser(lblUser.getText());
				
				tfUserName.setText("");
				pfPassword.setText("");
				//版本号去除
				miVersion1.setVisible(false);
				miVersion2.setVisible(false);
				miVersion3.setVisible(false);
				openFlag = false;
				//主板的显示问题
				mainPane.setVisible(true);
				loginPane.setVisible(false);
			}else{
				showStage("Login Failed! "   + "Check Your Info!");
				
				tfUserName.setText("");
				pfPassword.setText("");
			}

		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public void onRegisterClicked(ActionEvent ev){
		boolean registerResult;
		
		try{
			registerResult = RemoteHelper.getInstance().getUserService().register(tfUserName.getText(), pfPassword.getText());
			
			tfUserName.setText("");
			pfPassword.setText("");
			
			if(registerResult){
				showStage("You've register successfully!");
			}
			else{
				showStage("Username already exists.");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void onLogoutClicked(ActionEvent ev){
		boolean logoutResult;
		//清空记录
		pointer = 0; 
		codeContent.clear();
		backspace = false;
		
		try{
			logoutResult = RemoteHelper.getInstance().getUserService().logout(lblUser.getText());
			
			if(logoutResult){
				//退出成功清除一切文本内容
				lblUser.setText("");
				taCode.setText("");
				taInput.setText("");
			}
			else{
				showStage("You haven't logged in yet!");
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//Login按钮
	public void onReturnClicked(MouseEvent me){
		mainPane.setVisible(true);
		loginPane.setVisible(false);
	}
	
	public void showStage(String word){
		loginPane.setDisable(true);
		mainPane.setDisable(true);
		
		Rectangle rec = new Rectangle(600, 400);
		rec.setFill(Color.CORNSILK);
		rec.setLayoutX(0);rec.setLayoutY(0);
		tempPane.getChildren().add(rec);
		
		Label warning = new Label(word);
		warning.setAlignment(Pos.CENTER);
		warning.setFont(Font.font("Lucida Handwriting", 30));
		warning.setLayoutX(50);		warning.setLayoutY(75);
		tempPane.getChildren().add(warning);
		
		Button confirmButton = new Button("OK!");
		confirmButton.setFont(Font.font(35));
		confirmButton.setLayoutX(250);
		confirmButton.setLayoutY(225);
		tempPane.getChildren().add(confirmButton);
		confirmButton.setOnAction(e -> {
			stage.close();
			loginPane.setDisable(false);
			mainPane.setDisable(false);
		});
		
		stage.setScene(scene);
		stage.setTitle("Caution!");
		stage.showAndWait();
	}
	
	public void showSaveStage(){
		mainPane.setDisable(true);
		
		Rectangle rec = new Rectangle(600, 400);
		rec.setFill(Color.CORNSILK);
		rec.setLayoutX(0);rec.setLayoutY(0);
		savePane.getChildren().add(rec);
		
		Label prompt = new Label("File name: ");
		prompt.setAlignment(Pos.CENTER);
		prompt.setFont(Font.font("San Serif", 25));
		prompt.setLayoutX(100);		prompt.setLayoutY(120);
		savePane.getChildren().add(prompt);
		
		TextField fileName = new TextField();
		fileName.setPromptText("Choose File Category First");
		fileName.setLayoutX(300);
		fileName.setLayoutY(120);
		fileName.setPrefWidth(200);
		fileName.setFont(Font.font(20));
		savePane.getChildren().add(fileName);
		
		
		Button saveButton = new Button("Save");
		saveButton.setPrefWidth(120);
		saveButton.setFont(Font.font(20));
		saveButton.setLayoutX(120);
		saveButton.setLayoutY(230);
		savePane.getChildren().add(saveButton);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setPrefWidth(120);
		cancelButton.setFont(Font.font(20));
		cancelButton.setLayoutX(350);
		cancelButton.setLayoutY(230);
		savePane.getChildren().add(cancelButton);
		
		saveButton.setOnAction(e -> {
			try{
				//清空记录
				pointer = 0; 
				codeContent.clear();
				backspace = false;
				
				String content = fileName.getText();
				String[] detail = content.split("\\.");//看看用户自己加了后缀名没有，对应length
				
				Calendar calendar = Calendar.getInstance();
				String time = calendar.get(Calendar.YEAR) + " " + (calendar.get(Calendar.MONTH)+1) + " " + calendar.get(Calendar.DAY_OF_MONTH);
				String file = taCode.getText() + "*" + taInput.getText() + "*" + time;//文件具体内容组成及分隔符*
				
				if(detail.length == 2){
					RemoteHelper.getInstance().getIOService().writeFile(file, lblUser.getText(), content);
				}else{
					if(cbLanguageChooser.getValue() != null)
						RemoteHelper.getInstance().getIOService().writeFile(file, lblUser.getText(), content + "." + cbLanguageChooser.getValue());
					//防止文件没有后缀名
					else
						showStage("Choose the language");
				}
				
				currentFile = detail.length == 2 ? content : content+"."+cbLanguageChooser.getValue();
				
				//如果现在处于打开一个文件的状态
				if(openFlag){
					String[] tempResult = RemoteHelper.getInstance().getIOService().readFile(lblUser.getText(), currentFile).split("#");
					
					//listOfContent里记录了远端拿过来的数据
					listOfContent.clear();
					miVersion1.setVisible(false);
					miVersion2.setVisible(false);
					miVersion3.setVisible(false);
					for(int i = 0; i < tempResult.length; i++){
						listOfContent.add(tempResult[i]);
					}
					
					//主要就是为了这里刷新version
					switch (tempResult.length){
						case 1:	miVersion1.setVisible(true);
								miVersion1.setText(listOfContent.get(0).split("\\*")[2]);
								break;
						case 2:	miVersion1.setVisible(true);
								miVersion1.setText(listOfContent.get(0).split("\\*")[2]);
								miVersion2.setVisible(true);
								miVersion2.setText(listOfContent.get(1).split("\\*")[2]);
								break;
						case 3:	miVersion1.setVisible(true);
								miVersion1.setText(listOfContent.get(0).split("\\*")[2]);
								miVersion2.setVisible(true);
								miVersion2.setText(listOfContent.get(1).split("\\*")[2]);
								miVersion3.setVisible(true);
								miVersion3.setText(listOfContent.get(2).split("\\*")[2]);
								break;
					}
				}
				
				//对应清空记录。把字符串再写入便于撤销和重做
				for(int i = 0; i < taCode.getText().length(); i++){
					pointer++;
					codeContent.add(taCode.getText().charAt(i));
				}
				
				mainPane.setDisable(false);
				saveStage.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		});
		
		cancelButton.setOnAction(e -> {
			mainPane.setDisable(false);
			saveStage.close();
		});
		
		saveStage.setScene(saveScene);
		saveStage.setTitle("Save File");
		saveStage.showAndWait();
	}
	
	public void showOpenStage(){
		mainPane.setDisable(true);
		
		Rectangle rec = new Rectangle(600, 400);
		rec.setFill(Color.CORNSILK);
		rec.setLayoutX(0);rec.setLayoutY(0);
		openPane.getChildren().add(rec);
		
		Label prompt = new Label("File name: ");
		prompt.setAlignment(Pos.CENTER);
		prompt.setFont(Font.font("San Serif", 25));
		prompt.setLayoutX(100);		prompt.setLayoutY(120);
		openPane.getChildren().add(prompt);
		
		//该用户名下的各个文件
		ComboBox<String> fileList = new ComboBox<>();
		try{
			fileList.getItems().clear();
			fileList.setPromptText("choose your file");
			String[] content = RemoteHelper.getInstance().getIOService().readFileList(lblUser.getText()).split("#");
			
			for(int i = 0; i < content.length; i++)
				fileList.getItems().add(content[i]);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		fileList.setLayoutX(300);
		fileList.setLayoutY(120);
		fileList.setPrefWidth(200);
		openPane.getChildren().add(fileList);
		
		Button openButton = new Button("Open");
		openButton.setPrefWidth(120);
		openButton.setFont(Font.font(20));
		openButton.setLayoutX(120);
		openButton.setLayoutY(230);
		openPane.getChildren().add(openButton);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setPrefWidth(120);
		cancelButton.setFont(Font.font(20));
		cancelButton.setLayoutX(350);
		cancelButton.setLayoutY(230);
		openPane.getChildren().add(cancelButton);
		
		openButton.setOnAction(e -> {
					
			openFlag = true;
			currentFile = fileList.getValue();
			cbLanguageChooser.setValue(fileList.getValue().split("\\.")[1]);
			
			//清空记录
			pointer = 0; 
			codeContent.clear();
			backspace = false;
			
			try{
				String[] tempResult = RemoteHelper.getInstance().getIOService().readFile(lblUser.getText(), fileList.getValue()).split("#");
				
				listOfContent.clear();
				miVersion1.setVisible(false);
				miVersion2.setVisible(false);
				miVersion3.setVisible(false);
				//listOfContent里记录了远端拿过来的数据
				
				for(int i = 0; i < tempResult.length; i++){
					listOfContent.add(tempResult[i]);
				}
				
				taCode.setText(listOfContent.get(0).split("\\*")[0]);
				taInput.setText(listOfContent.get(0).split("\\*")[1]);
				
				System.out.println(listOfContent.size());
				switch (tempResult.length){
					case 1:	miVersion1.setVisible(true);
							miVersion1.setText(listOfContent.get(0).split("\\*")[2]);
							break;
					case 2:	miVersion1.setVisible(true);
							miVersion1.setText(listOfContent.get(0).split("\\*")[2]);
							miVersion2.setVisible(true);
							miVersion2.setText(listOfContent.get(1).split("\\*")[2]);
							break;
					case 3:	miVersion1.setVisible(true);
							miVersion1.setText(listOfContent.get(0).split("\\*")[2]);
							miVersion2.setVisible(true);
							miVersion2.setText(listOfContent.get(1).split("\\*")[2]);
							miVersion3.setVisible(true);
							miVersion3.setText(listOfContent.get(2).split("\\*")[2]);
							break;
				}
				
				for(int i = 0; i < taCode.getText().length(); i++){
					pointer++;
					codeContent.add(taCode.getText().charAt(i));
				}
				
				mainPane.setDisable(false);
				openStage.close();
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		});
		
		cancelButton.setOnAction(e -> {
			mainPane.setDisable(false);
			openStage.close();
		});
		
		openStage.setScene(openScene);
		openStage.setTitle("Open File");
		openStage.showAndWait();
	}
}
