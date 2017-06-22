package serviceImpl;

import java.io.*;
import service.IOService;

public class IOServiceImpl implements IOService{
	String theUserName;
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		File folder = new File(userId);
		File f = new File(userId + "\\/" + userId + "_" + fileName);
		
		//文件存在时怎么处理
		if(f.exists()){
			
			String[] content = new String[3]; //用来存放三个版本数据
			int counter = 0;
			
			try{
				BufferedReader reader = new BufferedReader(new FileReader(f));
				
				while((content[counter] = reader.readLine()) != null){
					
					//如果存在重复的文件,保存失败。无需给予提示，因为信息未丢失
					if(file.equals(content[counter])){
						reader.close();
						return false;
					}
					
					counter++;
					if(counter == 3) break;
				}
				
				//看是否写满三个版本。写满要替换。若需要替换则将最久的一个覆盖掉
				if(counter == 3){
					content[0] = content[1];	content[1] = content[2];	content[2] = file;
					counter--;    //解决循环时少写一行的错误
				}else{
					content[counter] = file;
				}
				
				//写入文件
				FileWriter writer = new FileWriter(f);
				String result = content[0];
				for(int i = 1; i <= counter; i++){
					result = result + System.lineSeparator() + content[i];
				}
				writer.write(result);
				writer.flush();
				writer.close();
				
				reader.close();
				return true;
				
			}catch(IOException ex){
				ex.printStackTrace();
			}
			
		}
		
		//文件不存在时怎么处理
		else{
			try{
				folder.mkdirs();
				f.createNewFile();
				
				FileWriter writer = new FileWriter(f, true);
				writer.write(file);
				writer.flush();
				writer.close();
				
				return true;
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		
		return true;
		
	}

	@Override
	public String readFile(String userId, String fileName) {
		File f = new File(userId + "\\/" + userId + "_" + fileName);
		
		if(f.exists()){
			
			try{
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String content;	StringBuilder result = new StringBuilder();
				
				while((content = reader.readLine()) != null){
					result.append(content + "#");
				}
				
				result.deleteCharAt(result.length()-1);
				reader.close();
				return result.toString();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		return "";
	}

	@Override
	public String readFileList(String userId) {
		File f = new File(userId);
		
		if(f.exists()){
			if(f.isDirectory()){
				String[] fContent = f.list();
				
				String result = fContent[0].split("_")[1];
				for(int i = 1; i < fContent.length; i++){
					result = result + "#" + fContent[i].split("_")[1];
				}
				
				return result;
			}
			
			else
				return "";
		}
		else
			return "";
	}
	
}
