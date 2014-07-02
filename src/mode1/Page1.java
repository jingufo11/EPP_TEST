package mode1;


import com.example.epp_test.R;
import com.kmy.epp.Epp;
import com.kmy.helper.LogHelper;

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
	private View mMainView;
	
	private Button ResetBt,ConfigBt,EchoBt,GetFWVerBt,GetSNBt;
	private TextView ReportTv,FWVerTv,SNTv;
	private EditText EchoSend,EchoRec;
    private Spinner BRspinner,COMspinner;
    private ArrayAdapter<String> BRadapter;
    private RadioGroup RestRd; 
      
    private int BaudRateVaule = 9600;
    private Epp	KMY3501;
    private  LogHelper logbook = new LogHelper();
    
    public Page1(Epp Actepp){
    	KMY3501 = Actepp;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("EPP", "Page1-->onCreate()");
		
		
		//LayoutInflater inflater = getActivity().getLayoutInflater();
		//mMainView = inflater.inflate(R.layout.layout1, (ViewGroup)getActivity().findViewById(R.id.), false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("EPP", "Page1-->onCreateView()");
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
		mMainView = inflater1.inflate(R.layout.layout1, container, false);
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.d("EPP", "Page1-->移除已存在的View");
        } 
		CreatInterface();
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("EPP", "Page1-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("EPP", "Page1-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("EPP", "Page1-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//CreatPage1Interface();
		Log.d("EPP", "Page1-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("EPP", "Page1-->onStop()");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    Log.d("EPP", "Page1-->setUserVisibleHint:"+isVisibleToUser);
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
		COMspinner.setVisibility(View.GONE);	
		EchoSend.setSelection(EchoSend.length());
		
		 //将可选内容与ArrayAdapter连接起来
		BRadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,BRStr);

		//设置下拉列表的风格
		BRadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		BRspinner.setAdapter(BRadapter);
		//添加事件Spinner事件监听 

		BRspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		BRspinner.setVisibility(View.VISIBLE);
		BRspinner.setSelection(1);


		// handle write click 
		ConfigBt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				KMY3501.OpenComm(null,BaudRateVaule);
				Log.d("EPP","BaudRate="+BaudRateVaule);
				//rec.append("SetBaudrate\r\n");
				if(KMY3501.FTUart.READ_ENABLE!=true){
					//rec.append("READ_ENABLE false\r\n");
					ReportTv.setText("Config Port fail!!");
				}
				else{
					ReportTv.setText("Config Port ok!!");
					ConfigBt.setEnabled(false);
					//rec.append("READ_ENABLE true\r\n");
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
			/*			
			Log.d("EPP","onItemSelected arg2->"+arg2);

			TextView tx_spinner = (TextView)BRspinner.getSelectedView();
			if(tx_spinner==null){
				Log.d("EPP","Bad spinner");
			}
			
			String Sta = (String) tx_spinner.getText();
			//String Sta ="9600";
			BaudRateVaule = Integer.parseInt(Sta);
			*/
			Log.d("EPP","onItemSelected arg2->"+arg2);
			String Sta = BRStr[arg2];
			BaudRateVaule = Integer.parseInt(Sta);
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




	
	
		
	
























