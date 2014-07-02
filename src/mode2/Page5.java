package mode2;

import com.kmy.epp.EppT2;
import com.kmy.helper.LogHelper;
import com.example.epp_test.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Page5 extends Fragment{
	final String[] MKIndex = {"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F"};
	final String[] WKIndex = {"00","01","02","03"};
	final String[] MacAlgOpt = {"X99","X919","UnionPay"};
	
    private Spinner MKIndexspinner,WKIndexspinner,MacAlgspinner;
    private ArrayAdapter<String> MKIndexadapter,WKIndexadapter,MacAlgadapter;
    private Button EncBt,DecBt,MacBt;
    private EditText EncOriEd,MacOriEd;
    private TextView ReportTv,EncDesTv,MacDesTv;
    private RadioGroup AlgEncRdg;
    
    private String CurMKIndex,CurWKIndex;
    private int CurMacAlg;
	
	private View mMainView;
    private EppT2	KMY3501;
    private  LogHelper logbook = new LogHelper();
    

    
    public Page5(EppT2 Actepp){
    	KMY3501 = Actepp;
    }
    

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("EPPT2", "Page5-->onCreate()");
		//LayoutInflater inflater = getActivity().getLayoutInflater();
		//mMainView = inflater.inflate(R.layout.layout1, (ViewGroup)getActivity().findViewById(R.id.), false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("EPPT2", "Page5-->onCreateView()");
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
 		mMainView = inflater1.inflate(R.layout.layout5, container, false);
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.d("EPPT2", "Page5-->移除已存在的View");
        } 
		CreatInterface();
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("EPPT2", "Page5-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("EPPT2", "Page5-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("EPPT2", "Page5-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//CreatPage1Interface();
		Log.d("EPPT2", "Page5-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("EPPT2", "Page5-->onStop()");
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    Log.d("EPPT2", "Page5-->setUserVisibleHint:"+isVisibleToUser);
	    if (isVisibleToUser) {
	        //相当于Fragment的onResume
	    } else {
	    //相当于Fragment的onPause
	    }
	}
	private void SetViewSize(){
		
		int Ratio = 30;
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();  
			
		int width = dm.widthPixels;//宽度
		int height = dm.heightPixels ;//高度
		logbook.WriteLog("Screen Metrics", "width="+width+"height="+height);

		EncOriEd.setWidth(width/2);
		EncDesTv.setWidth(width/2);
		MacOriEd.setWidth(width/2);
		MacDesTv.setWidth(width/2);
		
		if(height<1000){
			EncDesTv.setMaxHeight((height/100)*Ratio);
			EncDesTv.setHeight((height/100)*Ratio);
			EncOriEd.setMaxHeight((height/100)*Ratio);
			EncOriEd.setHeight((height/100)*Ratio);

			MacOriEd.setMaxHeight((height/100)*Ratio);
			MacOriEd.setHeight((height/100)*Ratio);
			MacDesTv.setMaxHeight((height/100)*Ratio);
			MacDesTv.setHeight((height/100)*Ratio);
		}
		else{
			EncOriEd.setMaxHeight((height/1000)*Ratio*10);
			EncOriEd.setHeight((height/1000)*Ratio*10);
			EncDesTv.setMaxHeight((height/1000)*Ratio*10);
			EncDesTv.setHeight((height/1000)*Ratio*10);
			MacOriEd.setMaxHeight((height/1000)*Ratio*10);
			MacOriEd.setHeight((height/1000)*Ratio*10);
			MacDesTv.setMaxHeight((height/1000)*Ratio*10);
			MacDesTv.setHeight((height/1000)*Ratio*10);
		}
	
	}
	public void CreatInterface(){   
		
		EncBt = (Button) mMainView.findViewById(R.id.BT_Encrypt);
		DecBt = (Button) mMainView.findViewById(R.id.BT_Decrypt);
		MacBt = (Button) mMainView.findViewById(R.id.BT_MAC);
	    EncOriEd = (EditText) mMainView.findViewById(R.id.ED_EncOri);
	    MacOriEd = (EditText) mMainView.findViewById(R.id.ED_MacOri);
	    
	    EncDesTv = (TextView) mMainView.findViewById(R.id.TV_EncDes);
	    MacDesTv  = (TextView) mMainView.findViewById(R.id.TV_MacDes);
	    ReportTv = (TextView) mMainView.findViewById(R.id.TV_Report5);
	    AlgEncRdg = (RadioGroup) mMainView.findViewById(R.id.RDG_EncAlg);
	    
	    SetViewSize();
	    
		MKIndexspinner = (Spinner)mMainView.findViewById(R.id.SPNER_MKIndex);
		WKIndexspinner = (Spinner)mMainView.findViewById(R.id.SPNER_WKIndex);
		MacAlgspinner = (Spinner)mMainView.findViewById(R.id.SPNER_MACAlg);
	    
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
		MacAlgadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,MacAlgOpt);
		//设置下拉列表的风格
		WKIndexadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		MacAlgspinner.setAdapter(MacAlgadapter);
		//添加事件Spinner事件监听 
		MacAlgspinner.setOnItemSelectedListener(new SpinnerSelectedListener());	
		MacAlgspinner.setVisibility(View.VISIBLE);
		/**/
		
		Page5ClickListener MyClickListener = new Page5ClickListener();
		EncBt.setOnClickListener(MyClickListener);
		DecBt.setOnClickListener(MyClickListener);
		MacBt.setOnClickListener(MyClickListener);
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
			else if(arg0 == MacAlgspinner){
				CurMacAlg= arg2;			
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	 }
	
	class Page5ClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0.equals(EncBt)){
				//StartPin();
				DoEncOrDec(true);
			}
			else if(arg0.equals(DecBt)){
				DoEncOrDec(false);	
			}
			else if(arg0.equals(MacBt)){
				DoMAC();
			}
		}
		
	}
	
	//true-Enc false-Dec
	public void DoEncOrDec(boolean EncOrDec){
		String EncData = EncOriEd.getText().toString();
		
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
		
		if(EncOrDec)
			ret = KMY3501.DataEnc(EncData);
		else
			ret = KMY3501.DataDec(EncData);
		if(ret==0){
			EncDesTv.setText(KMY3501.EppResult);
		}
		UpdateReport("EncOrDec");
	}
	
	//true-Enc false-Dec
	public void DoMAC(){
		String MacData = MacOriEd.getText().toString();
		
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
		ret = KMY3501.SetMACArithmetic(CurMacAlg);
		if(ret!=0){
			UpdateReport("SetDesOr3Des");
			return;
		}
		
		ret = KMY3501.DataMAC(MacData);
		if(ret==0){
			MacDesTv.setText(KMY3501.EppResult);
		}
		UpdateReport("Mac");
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
