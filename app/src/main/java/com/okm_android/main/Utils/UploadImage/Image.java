package com.okm_android.main.Utils.UploadImage;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
/*
 * 修改历史：
 */
public class Image {
	private byte[] data;	// 上传文件的数据
	
	private String fileName;// 文件名称
		
	private String formName;// type="file"表单字段所对应的name属性值
	
	private String contentType = "image/jpeg";	// 内容类型。不同的图片类型对应不同的值，具体请参考Multimedia MIME Reference - http://www.w3schools.com/media/media_mimeref.asp
	
	private int photoWidth;

	private int photoHeight;
//	public Image(String fileName, String formName) {
//		this(fileName, formName, null);
//	}
	
	public Image(){}
	
	public Image(String filePath, String formName, String contentType) {
		int beginIndex = filePath.lastIndexOf(System.getProperty("file.separator"));// The value of file.separator is '\\'
		if(beginIndex < 0) {
			beginIndex = filePath.lastIndexOf("/");
		}
		
		this.fileName = filePath.substring(beginIndex+1, filePath.length());
		this.formName = formName;
		if(contentType != null)
			this.contentType = contentType;
		if(data == null) {
			try {
				data = Image.read(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public Image(Bitmap bitmap, String formName) {
		
		this.formName = formName;
		
		if(bitmap != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();   
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);   
			data = baos.toByteArray();
		}
	}
	

	public Image(String path, String formName) throws IOException {
		
		this.formName = formName;
		this.data = Image.read(path);
		setPhotoHeight(BytesToBimap(data).getHeight());
		setPhotoWidth(BytesToBimap(data).getWidth());
	}
	
	public Image(String path, String formName, int num) throws IOException {
		
		this.formName = formName;
		this.data = path.getBytes();
		
	}
	
	/**
	 * 读取文件内容
	 * @param filepath 文件路径
	 * @return
	 * @throws java.io.IOException
	 */
	public static byte[] read(String filepath) throws IOException {
		DataInputStream input = new DataInputStream(new FileInputStream(filepath));
		int len = input.available();
		byte[] content = new byte[len];
		int r = input.read(content, 0, content.length);
		if(r != len) {
			content = null;
			throw new IOException("读取文件失败！");
		}
		input.close();
		
		return content;
	}
	
	public Bitmap BytesToBimap(byte[] b) {
		if(b.length != 0) {
			return  BitmapFactory.decodeByteArray(b, 0, b.length);
	    }else{
	        return null;
	    }
	 }

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getPhotoWidth() {
		return photoWidth;
	}
	
	public void setPhotoWidth(int photoWidth) {
		this.photoWidth = photoWidth;
	}
	
	public int getPhotoHeight() {
		return photoHeight;
	}
	
	public void setPhotoHeight(int photoHeight) {
		this.photoHeight = photoHeight;
	}
	
}
