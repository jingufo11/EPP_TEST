package mode1;


import com.example.epp_test.R;
import com.kmy.epp.Epp;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;



public class Page3 extends Fragment {
	final String[] MKIndex = {"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F"};
	final String[] WKIndex = {"00","01","02","03"};
	
    private Spinner MKIndexspinner,WKIndexspinner;
    private ArrayAdapter<String> MKIndexadapter,WKIndexadapter;
    private Button LoadMKBt,LoadWKBt;
    private CheckBox MKKcvCb,WKKcvCb;
    private EditText MKContext,WKContext;
    private TextView ReportTv,MKKcv,WKKcv,MKLenTv,WKLenTv;
    private RadioGroup AlgEncRdg;
    
    private String CurMKIndex,CurWKIndex;
    
	private View mMainView;
	private Epp	KMY3501;
	//private ToolHelper toolbox = new ToolHelper();
	
    public Page3(Epp Actepp){
    	KMY3501 = Actepp;
    }
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
			
		CurMKIndex = MKIndex[0];
		CurWKIndex = WKIndex[0];
		Log.d("EPP", "Page3-->onCreate()");				
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("EPP", "Page3-->onCreateView()");
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
		mMainView = inflater1.inflate(R.layout.layout3, container, false);
		
		ViewGroup p = (ViewGroup) mMainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
            Log.d("EPP", "Page3-->移除已存在的View");
        } 
        
        CreatInterface();
		
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("EPP", "Page3-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("EPP", "Page3-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("EPP", "Page3-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("EPP", "Page3-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("EPP", "Page3-->onStop()");
	}
	
	public void CreatInterface(){
		LoadMKBt =  (Button) mMainView.findViewById(R.id.BT_LoadMK);
		MKKcvCb = (CheckBox)mMainView.findViewById(R.id.CB_MKCV);
		MKContext = (EditText)mMainView.findViewById(R.id.ED_MKCont);
		MKKcv = (TextView)mMainView.findViewById(R.id.TV_MKCV);		
		LoadWKBt =  (Button) mMainView.findViewById(R.id.BT_LoadWK);
		WKKcvCb = (CheckBox)mMainView.findViewById(R.id.CB_WKCV);
		WKContext = (EditText)mMainView.findViewById(R.id.ED_WKCont);
		WKKcv = (TextView)mMainView.findViewById(R.id.TV_WKCV);		
		AlgEncRdg = (RadioGroup) mMainView.findViewById(R.id.RDG_EncAlg);
		ReportTv = (TextView)mMainView.findViewById(R.id.TV_Report3);
		MKIndexspinner = (Spinner)mMainView.findViewById(R.id.SPNER_MKIndex);
		WKIndexspinner = (Spinner)mMainView.findViewById(R.id.SPNER_WKIndex);
		MKLenTv = (TextView)mMainView.findViewById(R.id.TV_MKLen);
		WKLenTv = (TextView)mMainView.findViewById(R.id.TV_WKLen);
		
        int len = MKContext.getText().toString().length();
        MKLenTv.setText(Integer.toString(len));
        
        len = WKContext.getText().toString().length();
        WKLenTv.setText(Integer.toString(len));
        MKKcvCb.setChecked(true);
        WKKcvCb.setChecked(true);
		//MKContext.setTransformationMethod(new AllCapTransformationMethod ());
		//WKContext.setTransformationMethod(new AllCapTransformationMethod ());
		
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
		/**/	
		LoadMKBt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				String MKCntxt = MKContext.getText().toString();
				int ret=0;
				if(MKCntxt.length()==16){
					ret = KMY3501.SetDesOr3Des(1);
				}
				else if(MKCntxt.length()==32){
					ret = KMY3501.SetDesOr3Des(3);
				}
				else{
					ReportTv.setText("Key Length must be 8or16 byte!");
					return;
				}
				if(ret!=0){
					UpdateReport("SetDesOr3Des");
					return;
				}
				
				if(MKKcvCb.isChecked()){
					ret = KMY3501.IsNeedKCV(1);
				}
				else{
					ret = KMY3501.IsNeedKCV(0);
				}
				if(ret!=0){
					UpdateReport("SetDesOr3Des");
					return;
				}
				
				
				ret = KMY3501.LoadMasterKey(CurMKIndex, MKCntxt);
				if(ret==0 && MKKcvCb.isChecked()){
					MKKcv.setText(KMY3501.EppResult);		
				}
				UpdateReport("LoadMasterKey");
			}
		});
		
		LoadWKBt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				String WKCntxt = WKContext.getText().toString();
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
				
				if(WKCntxt.length()!=16&&WKCntxt.length()!=32){
					ReportTv.setText("Key Length must be 8or16 byte!");
					return;
				}
	
				if(WKKcvCb.isChecked()){
					ret = KMY3501.IsNeedKCV(1);
				}
				else{
					ret = KMY3501.IsNeedKCV(0);
				}
				if(ret!=0){
					UpdateReport("IsNeedKCV");
					return;
				}
				
				
				ret = KMY3501.LoadWorkKey(CurMKIndex,CurWKIndex,WKCntxt);
				if(ret==0 && MKKcvCb.isChecked()){
					WKKcv.setText(KMY3501.EppResult);		
				}
				UpdateReport("LoadWorkKey");
			}
		});
		
		MKContext.addTextChangedListener(new TextWatcher() { 
		    @Override 
		    public void onTextChanged(CharSequence s, int start, int before,int count) { 
		    } 
		 
		    @Override 
		    public void beforeTextChanged(CharSequence s, int start, int count, 
		    int after) { 
		    } 
		 
		    @Override 
		    public void afterTextChanged(Editable s) { 
		        //在afterTextChanged中，调用setText()方法会循环递归触发监听器，必须合理退出递归，不然会产生异常 
		        if (s.length() > 1 && s.charAt(0) == '0') { 
		            //Integer integer = Integer.valueOf(s.toString()); 
		           // MKContext.setText(integer.toString()); 
		        } 
		        int len = MKContext.getText().toString().length();
		        MKLenTv.setText(Integer.toString(len));
		    } 
		});
		
		WKContext.addTextChangedListener(new TextWatcher() { 
		    @Override 
		    public void onTextChanged(CharSequence s, int start, int before,int count) { 
		    } 
		 
		    @Override 
		    public void beforeTextChanged(CharSequence s, int start, int count, 
		    int after) { 
		    } 
		 
		    @Override 
		    public void afterTextChanged(Editable s) { 
		        //在afterTextChanged中，调用setText()方法会循环递归触发监听器，必须合理退出递归，不然会产生异常 
		        if (s.length() > 1 && s.charAt(0) == '0') { 
		            //Integer integer = Integer.valueOf(s.toString()); 
		           // MKContext.setText(integer.toString()); 
		        } 
		        int len = WKContext.getText().toString().length();
		        WKLenTv.setText(Integer.toString(len));
		    } 
		});

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

























