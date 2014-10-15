package com.okm_android_agile.Utils.UploadImage;

import android.util.Log;

import com.okm_android_agile.Utils.TokenUtils.QiniuToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/*
 * 实现图片文件上传
 * 修改历史：
 */
public class UploadImage {

    String uptoken = "7mGeCoNe_ecBsW210i5a0VDu4Yz8nZ5Ph6xUlV2E:ICehYqwmdzj4bJ5D8Ia8SyjA4to=:ewogICJkZWFkbGluZSIgOiAxNzA5MjA3MTcxLAogICJzY29wZSIgOiAibW90b3IiCn0=";
	String multipart_form_data = "multipart/form-data";
	String twoHyphens = "--";
	String boundary = "****************fD4fH3gL0hK7aI6";	// 数据分隔符
	String lineEnd = System.getProperty("line.separator");	// The value is "\r\n" in Windows.
	
	/*
	 * 上传图片内容，格式请参考HTTP 协议格式。
	 * 人人网Photos.upload中的”程序调用“http://wiki.dev.renren.com/wiki/Photos.upload#.E7.A8.8B.E5.BA.8F.E8.B0.83.E7.94.A8
	 * 对其格式解释的非常清晰。
	 * 格式如下所示：
	 * --****************fD4fH3hK7aI6
	 * Content-Disposition: form-data; name="upload_file"; filename="apple.jpg"
	 * Content-Type: image/jpeg
	 * 
	 * 这儿是文件的内容，二进制流的形式
	 */
	private void addImageContent(Image file, DataOutputStream output) {
			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + lineEnd);
			split.append("Content-Disposition: form-data; name=\"" + file.getFormName() + "\"; filename=\"" + file.getFileName() + "\"" + lineEnd);
			split.append("Content-Type: " + file.getContentType() + lineEnd);
			split.append(lineEnd);
			try {
				// 发送图片数据
				output.writeBytes(split.toString());
				output.write(file.getData(), 0, file.getData().length);
				output.writeBytes(lineEnd);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}
	
	/*
	 * 构建表单字段内容，格式请参考HTTP 协议格式（用FireBug可以抓取到相关数据）。(以便上传表单相对应的参数值)
	 * 格式如下所示：
	 * --****************fD4fH3hK7aI6
	 * Content-Disposition: form-data; name="action"
	 * // 一空行，必须有
	 * upload 
	 */
	private void addFormField(DataOutputStream output) {
        //计算token上传
        QiniuToken qiniuToken = new QiniuToken("back-end-linsece","juTtcBATDwmtCKT6fznhcbrHaBJrp4r3aB3K6xdG","34We6ZKVeyv4-jyEy006JNtDQDtiQAWhjpDwBjoc");
        String token = qiniuToken.uploadToken();

		StringBuilder sb = new StringBuilder();
			sb.append(twoHyphens + boundary + lineEnd);
			sb.append("Content-Disposition: form-data; name=\"token\"" + lineEnd);
			sb.append(lineEnd);
			sb.append(token + lineEnd);
		try {
			output.writeBytes(sb.toString());// 发送表单字段数据
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 直接通过 HTTP 协议提交数据到服务器，实现表单提交功能。
	 * @param actionUrl 上传路径
	 * @param actionUrl 请求参数key为参数名，value为参数值
	 * @param file 上传文件信息
	 * @return 返回请求结果
	 */
	public String post(String actionUrl, Image file) {
		HttpURLConnection conn = null;
		DataOutputStream output = null;
		BufferedReader input = null;
		try {
			URL url = new URL(actionUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(120000);
			conn.setDoInput(true);		// 允许输入
			conn.setDoOutput(true);		// 允许输出
			conn.setUseCaches(false);	// 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", multipart_form_data + "; boundary=" + boundary);
			conn.connect();
			output = new DataOutputStream(conn.getOutputStream());
			
			addImageContent(file, output);	// 添加图片内容
			
			addFormField(output);	// 添加表单字段内容
			
			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
			output.flush();
//			output.close();
			
//			int code = conn.getResponseCode();
//			if(code != 200) {
//				throw new RuntimeException("请求‘" + actionUrl +"’失败！");
//			}
			input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
     
			StringBuilder response = new StringBuilder();
			String oneLine;
			while((oneLine = input.readLine()) != null) {
				response.append(oneLine + lineEnd);
			}
//			input.close();

			return response.toString();
		} catch (IOException e) {
			Log.e("response error", e.toString());
			throw new RuntimeException(e);
		} finally {
			// 统一释放资源
			try {
				if(output != null) {
					output.close();
				}
				if(input != null) {
					input.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			if(conn != null) {
				conn.disconnect();
			}
		}
	}
}
