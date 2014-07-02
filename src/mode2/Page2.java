package mode2;

import com.kmy.epp.EppT2;
import com.kmy.helper.LogHelper;
import com.kmy.helper.ToolHelper;
import com.example.epp_test.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class Page2 extends Fragment {
	final static int Ratio=50;
	private View mMainView;
	private EppT2	KMY3501;
    
	private Button KeyCtrBt,ClearBt;
	private EditText KeyValueEd,KeyAscEd;
	private TextView ReportTv;
	
	public handler_thread PlaintextThread;
	public String KeyVal = new String();
	String PLStr = new String();
	
	boolean IsMon = false;
	private LogHelper logbook = new LogHelper(); 
	private ToolHelper toolbox = new ToolHelper();
	
    public Page2(EppT2 Actepp){
    	KMY3501 = Actepp;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("EPPT2", "Page2-->onCreate()");
		
		//LayoutInflater inflater = getActivity().getLayoutInflater();
		//mMainView = inflater.inflate(R.layout.layout2, (ViewGroup)getActivity().findViewById(R.id.fragment_place), false);		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("EPPT2", "Page2-->onCreateView()");
		
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
		mMainView = inflater1.inflate(R.layout.layout2, container, false);
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.d("EPPT2", "Page2-->移除已存在的View");
        } 
        CreatInterface();
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("EPPT2", "Page2-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("EPPT2", "Page2-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("EPPT2", "Page2-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("EPPT2", "Page2-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("EPPT2", "Page2-->onStop()");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    Log.d("EPPT2", "Page2-->setUserVisibleHint:"+isVisibleToUser);
	    if (isVisibleToUser) {
	        //相当于Fragment的onResume
			IsMon = false;
			KeyCtrBt.setText("Start");
			PLStr = "";
			ReportTv.setText("PLStr");
			
	    } else {
			//相当于Fragment的onPause
	    	IsMon = false;
	    	if(KMY3501.IsComOpen()){
	    		KMY3501.KeyBoardCtrl((byte)0);
	    	}
	    }
	}
	
	public void CreatInterface(){
		KeyCtrBt = (Button) mMainView.findViewById(R.id.BT_Start);
		ClearBt = (Button) mMainView.findViewById(R.id.BT_Clear);
		KeyValueEd = (EditText) mMainView.findViewById(R.id.ED_KeyPre);
		ReportTv = (TextView) mMainView.findViewById(R.id.TV_Report2);
		KeyAscEd = (EditText) mMainView.findViewById(R.id.ED_KeyAsc);
		

		
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();  
		float density  = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
		int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
		
		float xdpi = dm.xdpi;  
		float ydpi = dm.ydpi; 
		logbook.WriteLog("Screen Metrics", "xdpi="+xdpi+"ydpi="+ydpi);
		logbook.WriteLog("Screen Metrics", "density="+density+"densityDPI="+densityDPI);
		
		int width = dm.widthPixels;//宽度
		int height = dm.heightPixels ;//高度
		logbook.WriteLog("Screen Metrics", "width="+width+"height="+height);

		
		//将editText设置成只读的方法
		//KeyValueEd.setCursorVisible(false);      //设置输入框中的光标不可见  
		KeyValueEd.setFocusable(false);           //无焦点  
		KeyValueEd.setFocusableInTouchMode(false);     //触摸时也得不到焦点  
		//将editText设置成只读的方法
		//KeyAscEd.setCursorVisible(false);      //设置输入框中的光标不可见  
		KeyAscEd.setFocusable(false);           //无焦点  
		KeyAscEd.setFocusableInTouchMode(false);     //触摸时也得不到焦点 
		
		if(height<1000){
			KeyValueEd.setMaxHeight((height/100)*Ratio);
			KeyValueEd.setHeight((height/100)*Ratio);
		}
		else{
			KeyValueEd.setMaxHeight((height/1000)*Ratio*10);
			KeyValueEd.setHeight((height/1000)*Ratio*10);
		}
	

		// handle write click 
		KeyCtrBt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
			
				if(IsMon){
					IsMon = false;
					KMY3501.KeyBoardCtrl((byte)0);
					KeyCtrBt.setText("Start");
				}
				else{
					int ret = KMY3501.KeyBoardCtrl((byte)3);
					UpdateReport("KeyCtrl");
					KeyCtrBt.setText("Stop");
					IsMon = true;
					if(ret == 0){
						KeyCtrBt.setText("Stop");
						IsMon = true;
						PlaintextThread = new handler_thread(handler);
						PlaintextThread.start();
						//KeyCtrBt.setEnabled(false);
						//KMY3501.ClearUartBuf();
					}
				}	
				logbook.WriteLog("KeyCtrlClick", "IsMon="+IsMon);
				/*
				int ret = KMY3501.KeyBoardCtrl((byte)3);
				if(ret == 0){
					PlaintextThread = new handler_thread(handler);
					PlaintextThread.start();
					//KeyCtrBt.setEnabled(false);
					KMY3501.ClearUartBuf();
				}
				*/
			}
		});
		
		ClearBt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				PLStr= "";
				KeyValueEd.setText(PLStr);
				KeyAscEd.setText("");
			}
		});
		
	}
		

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String KeyValStr = msg.getData().getString("KeyVal");
			//logbook.WriteLog("EPPT2", "GetKeyval1","KeyVal="+KeyValStr);
			byte[] KeyRe = KeyValStr.getBytes();
			if(KeyRe.length==0)
				return;
			String CurKeyVal = toolbox.BcdToStr(KeyRe);
			KeyAscEd.setText(CurKeyVal);
			//logbook.WriteLog("EPPT2", "GetKeyval","CurKeyVal="+CurKeyVal);
			if(CurKeyVal.equals("08")==true){
				if(PLStr.length()>0)
					PLStr = PLStr.substring(0, PLStr.length()-1);
			}
			else if(CurKeyVal.equals("0D")==true) {
				PLStr += "\n";
			}			
			else if(CurKeyVal.equals("2F")||CurKeyVal.equals("7F")){
				PLStr += "00";
			}
			else if(KeyRe[0]<=0x39&&KeyRe[0]>=0x30 || KeyRe[0]<=0x41&&KeyRe[0]>=0x46){
				//logbook.WriteLog("EPPT2", "GetKeyval","KeyVal="+KeyValStr);
				PLStr += new String(KeyRe);
			}
			else{
				
			}
			//logbook.WriteLog("EPPT2", "GetKeyval","PLStr="+PLStr);
			KeyValueEd.setText(PLStr);
		}
	};
	
	/* usb input data handler */
	private class handler_thread extends Thread {
		Handler mHandler;
		
		/* constructor */
		handler_thread(Handler h) {
			mHandler = h;
		}

		public void run() {
			Message msg;
			Bundle bundle;
			while (IsMon) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}

				KeyVal += KMY3501.GetKeyPress();	
				if(KeyVal.length()==0){
					continue;
				}
				//logbook.WriteLog("EPPT2", "Thread","KeyVal="+KeyVal);
				msg = mHandler.obtainMessage();
				bundle = new Bundle();
				bundle.putString("KeyVal",KeyVal);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				KeyVal = new String();
			}
			//Log.d("EPPT2", "Thread Over");
		}

	}
	
	public void UpdateReport(String Title){
		String recs = new String();
		String recexplain = new String();
		
		recs = KMY3501.GetCmdRet();
		recexplain = KMY3501.CodeExplain(recs);
		if(recexplain.equals("OK")){
			ReportTv.setText(Title+":"+recexplain);	
		}
		else{
			ReportTv.setText(Title+":"+recs+"-"+recexplain);
		}	
	}
	
	
}

























