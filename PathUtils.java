package org.cocos2dx.lib;

import android.os.Environment;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PathUtils {

	public static String getSDcardDir() {
		return Environment.getExternalStorageDirectory().getPath() + "/";
	}

	public static String checkAndMkdirs(String dir) {
		File file = new File(dir);
		if (file.exists() == false) {
			file.mkdirs();
		}
		return dir;
	}

	public static String getAppPath(){
		String dir = getSDcardDir() + "APP/";
		return checkAndMkdirs(dir);
	}

	public static String getTempPath(){
		String dir = getAppPath()+"Temp/";
		return checkAndMkdirs(dir);
	}

	

/***
	 * 获得路径 也许是zip里面的的路径
	 * @param pPath
	 * @return
	 * @throws ZipException
	 * @throws IOException
     */
	public static String getZipFilePath(String pPath) throws ZipException, IOException {
		String zipfilepath = "";
		String filename = "";
		int index = pPath.indexOf("#");
		String ppPath = pPath;
		//是否在zip里
		if(index != -1){
			zipfilepath = pPath.substring(0,index);
			filename = pPath.substring(index+2);
			Log.d("RecordManager","zipfilepath:"+ zipfilepath + "--filename:" + filename);
			String filesavename = filename.replaceAll("/","_");
			ppPath = PathUtils.getTempPath() +filesavename+".temp";
			File filetemp = new File(ppPath);
			//是否有临时解压文件 避免重复解压
			if(!filetemp.exists()){
				filetemp.createNewFile();
				ZipFile file = new ZipFile(zipfilepath);
				FileHeader fileHeader = file.getFileHeader(filename);
				net.lingala.zip4j.io.ZipInputStream zipInputStream =  file.getInputStream(fileHeader);
				FileOutputStream fo =  new FileOutputStream(ppPath);
				byte[] b = new byte[4096];
				int readLine = -1;
				while ((readLine = zipInputStream.read(b)) != -1) {
					fo.write(b,0,readLine);
				}
				fo.close();
				zipInputStream.close();
			}
		}
		return ppPath;
	}

}
