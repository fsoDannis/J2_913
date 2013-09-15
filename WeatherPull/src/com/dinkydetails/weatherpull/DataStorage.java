//Dan Annis
//Sept. 2013

package com.dinkydetails.weatherpull;

import java.io.*;

import android.content.Context;
//import android.os.*;
import android.util.*;

public class DataStorage {

	public static String FILE_NAME2 = "my_data.txt";

	public static String JSON_DAY = "weekday";
	public static String JSON_TEMP = "fahrenheit";
	public static String JSON_CLOUD = "skyicon";

	//creating a history
	@SuppressWarnings("resource")
	public static Boolean storeStringFile(Context context, String filename, String content, Boolean external){
		try{
			File file;
			FileOutputStream fos;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			}else{
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			//write content
			fos.write(content.getBytes());
			fos.close();
		} catch(IOException e){
			Log.e("WRITE ERROR", filename);
		}
		return true;
	}
	//store objects on hard drive getting an object as content
	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String filename, Object content, Boolean external){
		try{
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			}else{
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			oos = new ObjectOutputStream(fos);
			//write content
			oos.writeObject(content);
			oos.close();
			fos.close();
		} catch(IOException e){
			Log.e("WRITE ERROR", filename);
		}
		return true;
	}

	public static String readStringFile2(Context context, String filename){
		String content = "";
		try{

			FileInputStream fin = context.openFileInput(filename);
			BufferedInputStream bin = new BufferedInputStream(fin);

			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();

			while((bytesRead = bin.read(contentBytes)) != -1){
				content = new String(contentBytes,0,bytesRead);
				contentBuffer.append(content);
			}
			content = contentBuffer.toString();
			fin.close();
		} catch (FileNotFoundException e){
			Log.e("readStringFile", filename + " file not found");
		} catch (IOException e){
			Log.e("readStringFile", "I/O Error");
		}
		return content;
	}
	@SuppressWarnings("resource")
	public static Object readObjectFile(Context context, String filename, Boolean external){
		Object content = new Object();
		try{
			File file;
			FileInputStream fin;
			if(external){
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
			}else{
				file = new File(filename);
				fin = context.openFileInput(filename);
			}

			ObjectInputStream ois = new ObjectInputStream(fin);

			try{
				content = (Object) ois.readObject();
			} catch (ClassNotFoundException e){
				Log.e("READ ERROR", "INVALID JAVA OBJECT FILE");
			}
			ois.close();
			fin.close();
		}catch (FileNotFoundException e){
			Log.e("READ ERROR", "FILE NOT FOUND" + filename);
			return null;
		}catch (IOException e){
			Log.e("READ ERROR", "I/O ERROR");
		}
		return content;
	}
	public static String readStringFile(Context context, String FILE_NAME2,
			boolean external) {
		//TODO Auto-generated method stub
		return null;
	}
}
