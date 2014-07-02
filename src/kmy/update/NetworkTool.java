package kmy.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTool {
		private Context context;
		public NetworkTool(Context context){
			this.context = context;
		}
        /**
        * 获取网址内容
        * @param url
        * @return
        * @throws Exception
        */
        public static String getContent(String url) throws Exception{
            StringBuilder sb = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            
        	//设置网络超时参数
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 5000);
            HttpGet get = new HttpGet(url);
            HttpResponse response;

			response = client.execute(get);
			String Status = response.getStatusLine().toString();
			if(Status.indexOf("200 OK")==-1){
				Exception e = new Exception(Status);
				throw  e;
			}
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
                
                String line = null;
                while ((line = reader.readLine())!= null){
                    //sb.append(line + "\n");
                	sb.append(line );
                }
                reader.close();
            }   
            return sb.toString();
        }
        
        //判断是否有网络连接
        public boolean isNetworkConnected() { 
	        if (context != null) { 
		        ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		        		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		        if (mNetworkInfo != null) { 
		        	return mNetworkInfo.isAvailable(); 
		        } 
	        } 
	        return false; 
        } 

        
        //判断wifi是否可用
        public boolean isWifiConnected() { 
	        if (context != null) { 
	        	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
	        			.getSystemService(Context.CONNECTIVITY_SERVICE); 
	        	NetworkInfo mWiFiNetworkInfo = mConnectivityManager 
	        			.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
	        	if (mWiFiNetworkInfo != null) { 
	        		return mWiFiNetworkInfo.isAvailable(); 
	        	} 
	        } 
	        return false; 
        } 
        //判断mobile是否可用        
        public boolean isMobileConnected() { 
	        if (context != null) { 
	        	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
	        			.getSystemService(Context.CONNECTIVITY_SERVICE); 
	        	NetworkInfo mMobileNetworkInfo = mConnectivityManager 
	        			.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
	        	if (mMobileNetworkInfo != null) { 
	        		return mMobileNetworkInfo.isAvailable(); 
	        	} 
	        } 
	        return false; 
        } 
        
        //获取当前网络连接的类型信息 
        public  int getConnectedType() { 
        	if (context != null) { 
        		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
        				.getSystemService(Context.CONNECTIVITY_SERVICE); 
        		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
	        	if (mNetworkInfo != null && mNetworkInfo.isAvailable()) { 
	        		return mNetworkInfo.getType(); 
	        	} 
        	} 
        	return -1; 
        }
        


}










