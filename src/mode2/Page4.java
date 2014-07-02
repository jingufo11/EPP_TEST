package mode2;


import com.kmy.epp.EppT2;
import com.kmy.helper.LogHelper;
import com.kmy.helper.ToolHelper;
import com.example.epp_test.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Page4 extends Fragment {
	final String[] MKIndex = {"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F"};
	final String[] WKIndex = {"00","01","02","03"};
	final String[] PinLenOpt = {"04","05","06","07","08","09","0A","0B","0C"};
	final int MSG_PinNum=0x1000;
	final int MSG_Complete=0x1001;
	final int MSG_Clear=0x1002;
	
    private Spinner MKIndexspinner,WKIndexspinner,PinLenspinner;
    private ArrayAdapter<String> MKIndexadapter,WKIndexadapter,PinLenadapter;
    private Button PinStartBt;
    private CheckBox AutoEndCb;
    private EditText CardNoEd;
    private TextView ReportTv,InputTv,PinBlockTv;
    private RadioGroup AlgEncRdg;

    
    private String CurMKIndex,CurWKIndex,CurPinLen;
	public String PLStr,KeyVal;
	ToolHelper toolbox = new ToolHelper();
	boolean IsMon = false;
	public handler_thread PINThread;
    
    
	private View mMainView;
    private EppT2	KMY3501;
    private  LogHelper logbook = new LogHelper();
    
    public Page4(EppT2 Actepp){
    	KMY3501 = Actepp;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("EPPT2", "Page4-->onCreate()");
		
		
		//LayoutInflater inflater = getActivity().getLayoutInflater();
		//mMainView = inflater.inflate(R.layout.layout1, (ViewGroup)getActivity().findViewById(R.id.), false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("EPPT2", "Page4-->onCreateView()");
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
		mMainView = inflater1.inflate(R.layout.layout4, container, false);
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.d("EPPT2", "Page4-->移除已存在的View");
        } 
		CreatInterface();
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("EPPT2", "Page4-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("EPPT2", "Page4-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("EPPT2", "Page4-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//CreatPage1Interface();
		Log.d("EPPT2", "Page4-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("EPPT2", "Page4-->onStop()");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    Log.d("EPPT2", "Page4-->setUserVisibleHint:"+isVisibleToUser);
	    if (isVisibleToUser) {
	        //相当于Fragment的onResume
	    	IsMon = false;
	    } else {
	    	//相当于Fragment的onPause
	    	IsMon = false;
	    	if(KMY3501.IsComOpen()){
				KMY3501.KeyBoardCtrl((byte)0);
	    	}
	    }
	}
	

	public void CreatInterface(){    
	    PinStartBt = (Button) mMainView.findViewById(R.id.BT_PinStart);
	    AutoEndCb = (CheckBox) mMainView.findViewById(R.id.CB_AutoEnd);
	    CardNoEd = (EditText) mMainView.findViewById(R.id.ED_CardNO);
	    ReportTv = (TextView) mMainView.findViewById(R.id.TV_Report4);
	    InputTv = (TextView) mMainView.findViewById(R.id.TV_PIN);
	    PinBlockTv  = (TextView) mMainView.findViewById(R.id.TV_PINBlock);
	    AlgEncRdg = (RadioGroup) mMainView.findViewById(R.id.RDG_EncAlg);
	    
		MKIndexspinner = (Spinner)mMainView.findViewById(R.id.SPNER_MKIndex);
		WKIndexspinner = (Spinner)mMainView.findViewById(R.id.SPNER_WKIndex);
		PinLenspinner = (Spinner)mMainView.findViewById(R.id.SPNER_PINLen);
	    
		//AutoEndCb.setChecked(true);
		 //将可选内容与ArrayAdapter连接起来
		MKIndexadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,MKIndex);
		//设置下拉列表的风格
		MKIndexadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		MKIndexspinner.setAdapter(MKIndexadapter);
		//添加事件Spinner事件监听 
		MKIndexspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		MKIndexspinner.setVisibility(View.VISIBLE);
		
		 //将可选内容与ArrayAdapter连接起来
		WKIndexadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,WKIndex);
		//设置下拉列表的风格
		WKIndexadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		WKIndexspinner.setAdapter(WKIndexadapter);
		//添加事件Spinner事件监听 
		WKIndexspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		WKIndexspinner.setVisibility(View.VISIBLE);
		
		 //将可选内容与ArrayAdapter连接起来
		PinLenadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,PinLenOpt);
		//设置下拉列表的风格
		PinLenadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		PinLenspinner.setAdapter(PinLenadapter);	
		//添加事件Spinner事件监听 
		PinLenspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		PinLenspinner.setVisibility(View.VISIBLE);
		Page4ClickListener MyClickListener = new Page4ClickListener();
		PinStartBt.setOnClickListener(MyClickListener);
		
		PinLenspinner.setSelection(2);
	}
	
	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			if(arg0==MKIndexspinner){
				CurMKIndex = MKIndex[arg2];
			}
			else if(arg0==WKIndexspinner){
				CurWKIndex = WKIndex[arg2];
			}
			else if(arg0 == PinLenspinner){
				CurPinLen = PinLenOpt[arg2];			
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
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

	
	class Page4ClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0==PinStartBt){
				StartPin();
			}
		}
		
	}
	
	
	public void StartPin(){
		
		if(PINThread!=null){
			 if(PINThread.isAlive())
				 return;
		}
		String CardNoCntxt = CardNoEd.getText().toString();
		int ret=0;
		
		int KeyCB = AlgEncRdg.getCheckedRadioButtonId();
		
		if(KeyCB == R.id.RD_DES){
			ret = KMY3501.SetDesOr3Des(1);
		}
		else{
			ret = KMY3501.SetDesOr3Des(3);
		}
		if(ret!=0){
			UpdateReport("SetDesOr3Des");
			return;
		}
		
		ret = KMY3501.ActivateWorkKey(CurMKIndex, CurWKIndex);
		if(ret!=0){
			UpdateReport("ActivateWorkKey");
			return;	
		}
		
		if(CardNoCntxt.length()<12){
			ReportTv.setText("CardNO. length must be more than 12 digit!");
			return;
		}				
		ret = KMY3501.SetAccount(CardNoCntxt);
		if(ret!=0){
			UpdateReport("SetAccount");
			return;	
		}
		
		if(AutoEndCb.isChecked()){
			ret = KMY3501.IsAutoEnd(1);
		}
		else{
			ret = KMY3501.IsAutoEnd(0);
		}
		if(ret!=0){
			UpdateReport("IsAutoEnd");
			return;
		}
		
		ret = KMY3501.PINStart(CurPinLen, "30");
		if(ret == 0){
			Toast.makeText(getActivity().getBaseContext(), "Please enter password", Toast.LENGTH_SHORT).show();
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
			
			IsMon = true;
			PLStr = new String();
			InputTv.setText("");
			PinBlockTv.setText("");
			PINThread = new handler_thread(handler); 
			PINThread.start();
		}
		UpdateReport("PINStart");

	}
	
	private synchronized void  ProPinInput(String Key){
		if(Key.equals("2A"))
			PLStr += "*";
		InputTv.setText(PLStr);

	}
	
	private void ProPinComplete(String Key){
		logbook.WriteLog("ProPinComplete", "Key="+Key);
		if(Key.equals("0D")){
			int ret = KMY3501.ReadPinBlock();
			if(ret==0){
				PinBlockTv.setText(KMY3501.EppResult);
			}
			UpdateReport("ReadPinBlock");
		}
		else if(Key.equals("1B")){
			PLStr = "";		
			Toast.makeText(getActivity().getBaseContext(), "PIN Cancel", Toast.LENGTH_SHORT).show();
			//toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
		}
		InputTv.setText(PLStr);
	}	
	
	private void ProPinClear(String Key){
		logbook.WriteLog("ProPinClear", "Key="+Key);
		if(PLStr.length()>0)
			PLStr = PLStr.substring(0, PLStr.length()-1);
		
		InputTv.setText(PLStr);
	}
	
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//logbook.WriteLog("EPPT2", "handler", "handleMessage");
			switch(msg.what){
				case MSG_PinNum:	
					ProPinInput(msg.getData().getString("KeyVal"));
					break;
				case MSG_Complete:
					ProPinComplete(msg.getData().getString("KeyVal"));
					break;
				case MSG_Clear:
					ProPinClear(msg.getData().getString("KeyVal"));
					break;
						
			}
			//logbook.WriteLog("EPPT2", "handler", "handleMessage over");	
		}
	};

	/* usb input data handler */
	private class handler_thread extends Thread {
		Handler mHandler = null;
		
		/* constructor */
		handler_thread(Handler h) {
				this.mHandler = h;
		}

		public void run() {
			logbook.WriteLog("EPPT2", "PinThread", "Thread run");
			Message msg = null;
			Bundle bundle;
			String CurKeyVal;
			//Toast.makeText(getActivity().getBaseContext(), "PIN thread run", Toast.LENGTH_SHORT).show();
			while (IsMon) {
				try{
					Thread.sleep(50);
				} catch (InterruptedException e){
				}
				
				KeyVal = KMY3501.GetKeyPress();
				if(KeyVal.length()==0 || KeyVal.equals("")||KeyVal==null){
					continue;
				}
				//logbook.WriteLog("EPPT2", "GetPinThread", KeyVal);
				msg = mHandler.obtainMessage();
				bundle = new Bundle();
				
				CurKeyVal = toolbox.BcdToStr(KeyVal.getBytes());
				/*
				CurKeyVal = toolbox.BcdToStr( KeyVal.substring(0, 1).getBytes());
				if(KeyVal.length()>1){
					KeyVal = KeyVal.substring(1, KeyVal.length());
				}
				else
					KeyVal = new String();
				*/
				
				if(CurKeyVal.equals("08")){
					msg.what = MSG_Clear;//key press message				
				}
				else if(CurKeyVal.equals("0D")) {
					IsMon = false;
					msg.what = MSG_Complete;
				}			
				else if(CurKeyVal.equals("2A") ){
					msg.what = MSG_PinNum;//key press message
				}
				else if(CurKeyVal.equals("1B")){
					IsMon = false;
					msg.what = MSG_Complete;
				}
				else{					
				}
				bundle.putString("KeyVal",CurKeyVal);
				msg.setData(bundle);
				mHandler.sendMessage(msg);	
			}
		}
	}
}




	
	
		
	
























