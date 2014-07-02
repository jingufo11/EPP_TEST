package mode2;

import com.kmy.epp.EppT2;
import com.kmy.helper.LogHelper;

import com.example.epp_test.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;



public class Page1 extends Fragment {
	private static final String[] BRStr={"1200","9600","38400","57200","115200"};
	private static final String[] COMStr={"ttyS0","ttyS1","ttyS2","ttyUSB0","ttyUSB1","ttyUSB2"};
	private View mMainView;
	
	private Button ResetBt,ConfigBt,EchoBt,GetFWVerBt,GetSNBt;
	private TextView ReportTv,FWVerTv,SNTv;
	private EditText EchoSend,EchoRec;
    private Spinner BRspinner,COMspinner;
    private ArrayAdapter<String> BRadapter,COMadapter;
    private RadioGroup RestRd; 
    
    
    private int BaudRateVaule = 9600;
    private String COMValue = new String();
    private EppT2	KMY3501;
    private  LogHelper logbook = new LogHelper();
    
    public Page1(EppT2 Actepp){
    	KMY3501 = Actepp;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("EPPT2", "Page1-->onCreate()");
		
		
		//LayoutInflater inflater = getActivity().getLayoutInflater();
		//mMainView = inflater.inflate(R.layout.layout1, (ViewGroup)getActivity().findViewById(R.id.), false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("EPPT2", "Page1-->onCreateView()");
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
		mMainView = inflater1.inflate(R.layout.layout1, container, false);
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.d("EPPT2", "Page1-->移除已存在的View");
        } 
		CreatInterface();
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("EPPT2", "Page1-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("EPPT2", "Page1-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("EPPT2", "Page1-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//CreatPage1Interface();
		Log.d("EPPT2", "Page1-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("EPPT2", "Page1-->onStop()");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    Log.d("EPPT2", "Page1-->setUserVisibleHint:"+isVisibleToUser);
	    if (isVisibleToUser) {
	        //相当于Fragment的onResume
	    } else {
	    	//相当于Fragment的onPause
	    }
	}
	

	public void CreatInterface(){	 
		ConfigBt = (Button) mMainView.findViewById(R.id.BT_Config);
		ReportTv = (TextView) mMainView.findViewById(R.id.TV_Report);
		ResetBt = (Button) mMainView.findViewById(R.id.BT_Reset);
		BRspinner = (Spinner)mMainView.findViewById(R.id.SPNER_BR);
		COMspinner = (Spinner)mMainView.findViewById(R.id.SPNER_COM);
		EchoBt = (Button) mMainView.findViewById(R.id.BT_ECHO);
		EchoSend = (EditText) mMainView.findViewById(R.id.ED_ESend);
		EchoRec = (EditText)mMainView.findViewById(R.id.ED_ERec);
		RestRd = (RadioGroup) mMainView.findViewById(R.id.RDG_Rst);
		GetFWVerBt = (Button) mMainView.findViewById(R.id.BT_GetVer);
		FWVerTv = (TextView) mMainView.findViewById(R.id.TV_FWVer);
		GetSNBt = (Button) mMainView.findViewById(R.id.BT_GetSN);
		SNTv = (TextView) mMainView.findViewById(R.id.TV_SN);
			
		EchoSend.setSelection(EchoSend.length());
		
//		 将可选内容与ArrayAdapter连接起来bn
		BRadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,BRStr);

		//设置下拉列表的风格
		BRadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		BRspinner.setAdapter(BRadapter);
		//添加事件Spinner事件监听 

		BRspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		BRspinner.setVisibility(View.VISIBLE);
		BRspinner.setSelection(1);

		
		COMadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,COMStr);

		//设置下拉列表的风格
		COMadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		COMspinner.setAdapter(COMadapter);
		//添加事件Spinner事件监听 

		COMspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		COMspinner.setVisibility(View.VISIBLE);
		COMspinner.setSelection(2);

		// handle write click 
		ConfigBt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				Log.d("EPPT2","COMValue="+COMValue);
				Log.d("EPPT2","BaudRate="+BaudRateVaule);
				int ret = KMY3501.OpenComm(COMValue,BaudRateVaule);			
				if(ret==0){
					ReportTv.setText("Config Port OK!!");
				}
				else{
					ReportTv.setText("Config Port fail!!");
					//ConfigBt.setEnabled(false);
				}
			}
		});
		
		//handle reset click 
		ResetBt.setOnClickListener(new View.OnClickListener() {
			
			// @Override
			public void onClick(View v) {	
				int KeyCB = RestRd.getCheckedRadioButtonId();
				
				if(KeyCB == R.id.RD_Rst1){
					KMY3501.DevReset(1);
					logbook.WriteLog("Page->ResetBt", "DevReset(1)");
				}
				else{
					KMY3501.DevReset(2);
					logbook.WriteLog("Page->ResetBt", "DevReset(2)");
				}
				UpdateReport("Reset");
			}
		});
		
		//handle reset click 
		EchoBt.setOnClickListener(new View.OnClickListener() {
			
			// @Override
			public void onClick(View v) {	
				int ret = KMY3501.Echo(EchoSend.getText().toString());
				
				if(ret==0){
					String RecStr = KMY3501.EppResult;
					EchoRec.setText(RecStr);	
				}
				
				UpdateReport("Echo");
			}
		});
		
		//handle reset click 
		GetFWVerBt.setOnClickListener(new View.OnClickListener() {
			
			// @Override
			public void onClick(View v) {	
				int ret = KMY3501.GetVersion();
				if(ret==0){
					String RecStr = KMY3501.EppResult;
					FWVerTv.setText(RecStr);	
				}
				UpdateReport("GetFWVer");
			}
		});

		//handle reset click 
		GetSNBt.setOnClickListener(new View.OnClickListener() {
			
			// @Override
			public void onClick(View v) {	
				int ret = KMY3501.ReadClientSN();
				if(ret==0){
					String RecStr = KMY3501.EppResult;
					SNTv.setText(RecStr);	
				}
				UpdateReport("GetSN");
			}
		});
		
		ConfigBt.setFocusable(true);
	}

	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			Log.d("EPPT2","onItemSelected arg2->"+arg2);

			
			if(arg0==BRspinner){
				String Sta = BRStr[arg2];
				BaudRateVaule = Integer.parseInt(Sta);
			}
			else if(arg0==COMspinner){
				COMValue = COMStr[arg2];
			}
			else{
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
}




	
	
		
	
























