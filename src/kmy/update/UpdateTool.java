package kmy.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UpdateTool {
	public final int err_DownFile_Exception=101;
	public final int err_DownFile_Interrupt=102;
	public final int err_DownFile_GetHttp=103;
	
    private static final String TAG = "UpdateTool";
    public int newVerCode = 0;
    public String newVerName = "";	
    public Context gl_context;
    private boolean isStop = false;
    public String errMsg = new String();

    
    public UpdateTool(Context context){
    	this.gl_context = context;
    	//this.gl_file = file;
    }
    
    private int loadpercent = 0;
	
    /**************************
     * @return
     * 0-hasnewversion 1-nonewversion -1-getvercode fail
     */
	public int CheckUpdate(){
		 if (getServerVerCode()) {
			int vercode = Config.getVerCode(gl_context);
			if (newVerCode > vercode) {
				return 0;
			} else {
				return 1;
			}
		 }else{
			 return -1;
		 }
	}

    private boolean getServerVerCode() {
        try {
                String verjson = NetworkTool.getContent(Config.UPDATE_SERVER
                                + Config.UPDATE_VERJSON);
                JSONArray array = new JSONArray(verjson);
                if (array.length() > 0) {
                        JSONObject obj = array.getJSONObject(0);
                        try {
                                newVerCode = Integer.parseInt(obj.getString("verCode"));
                                newVerName = obj.getString("verName");
                        } catch (Exception e) {
                                newVerCode = -1;
                                newVerName = "";
                                errMsg = e.getMessage();
                                return false;
                        }
                }
        } catch (Exception e) {
        		Log.e(TAG,e.getMessage());
        		errMsg = e.getMessage();
                return false;
        }

        return true;
    }

	    

    public void downFile(final String url,final File file) {

            new Thread() {
                    public void run() {
                            HttpClient client = new DefaultHttpClient();
                            HttpGet get = new HttpGet(url);
                            HttpResponse response;
                            isStop = false;
                            try {
                                    response = client.execute(get);
                        			String Status = response.getStatusLine().toString();
                        			if(Status.indexOf("200 OK")==-1){
                        				//Exception e = new Exception(Status);
                        				//throw  e;
                                       	loadpercent = err_DownFile_GetHttp; 
                                        errMsg = Status;
                        			}
                        			
                                    HttpEntity entity = response.getEntity();
                                    long length = entity.getContentLength();
                                    InputStream is = entity.getContent();
                                    FileOutputStream fileOutputStream = null;
                                    if (is != null) {

                                          fileOutputStream = new FileOutputStream(file);

                                            byte[] buf = new byte[1024];
                                            int ch = -1;
                                            int count = 0;
                                            while ((ch = is.read(buf)) != -1 && !isStop) {
                                                    fileOutputStream.write(buf, 0, ch);
                                                    count += ch;
                                                    if (length > 0) {
                                                    }
                                                    
                                                    loadpercent = (int)(((float)count/length)*100);
                                            }
                                            if(isStop){
                                            	Log.d(TAG,"Interrupt down"+"percent="+loadpercent);                                           	
                                            	errMsg = "Interrupt down"+"at progress="+loadpercent;
                                            	loadpercent = err_DownFile_Interrupt;
                                            }
                                            
                                    }
                                    fileOutputStream.flush();
                                    if (fileOutputStream != null) {
                                            fileOutputStream.close();
                                    }
                                    
                            } catch (ClientProtocolException e) {
                            		loadpercent = err_DownFile_Exception; 
                                    errMsg = e.getMessage();
                                    e.printStackTrace();
                            } catch (IOException e) {
                            		loadpercent = err_DownFile_Exception; 
                                    errMsg = e.getMessage();
                                    e.printStackTrace();
                            } 
                    }

            }.start();
    }


    public void InterruptDown(){
    	isStop = true;
    }
 
    /**************************
     * @return
     * <=100 is progress;
     * >100 downfile has error
     */
    public int GetDownFileProgress(){
    	return loadpercent;
    }
    
    public String GetLastError(){
    	return errMsg;
    }

}
