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
        * ��ȡ��ַ����
        * @param url
        * @return
        * @throws Exception
        */
        public static String getContent(String url) throws Exception{
            StringBuilder sb = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            
        	//�������糬ʱ����
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
        
        //�ж��Ƿ�����������
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

        
        //�ж�wifi�Ƿ����
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
        //�ж�mobile�Ƿ����        
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
        
        //��ȡ��ǰ�������ӵ�������Ϣ 
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










