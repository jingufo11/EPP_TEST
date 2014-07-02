package com.example.epp_test;

import java.io.File;
import java.util.Locale;

import kmy.update.Config;
import kmy.update.NetworkTool;
import kmy.update.UpdateTool;

import com.kmy.epp.InfoReporter;
import mode1.Mode1Activity;
import mode2.Mode2Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	final String[] modeStr = new String[]{"mode1","mode2"};
	final int MSG_noNewVersion = 0x10;
	final int MSG_hasNewVersion = 0x11;
	final int MSG_downComplete = 0x12;
	final int MSG_updateProcess = 0x13;
	final int MSG_downFail = 0x14;
	final int MSG_StartDialog = 0x15;
	final int MSG_DismissDialog = 0x16;
	final int MSG_updateErr = 0x17;
	
	private Button	mode1Bt,mode2Bt;
	private TextView fwVerTv;
	private InfoReporter reporter = new InfoReporter();
	private Context context = MainActivity.this;
	private int languageId = 0;

    public ProgressDialog pBar;
    public UpdateTool updatetool = new UpdateTool(MainActivity.this);
    
    NetworkTool networktool = new NetworkTool(MainActivity.this); 
    Dialog startDialog;    
    private boolean isCreatUpdate = true;
    private StringBuilder updateErr = new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		//在获取资源文件之前，必须先读取语言配置信息
		SharedPreferences languagePre=getSharedPreferences("language_choice", Context.MODE_PRIVATE);
		int id=languagePre.getInt("id", 0);
		Log.d("MainActivity","ID="+id);
		//应用内配置语言
		 Resources resources =getResources();//获得res资源对象  
		 Configuration config = resources.getConfiguration();//获得设置对象  
		 DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
		 switch (id) {
			 case 0:
				 config.locale=Locale.getDefault();         //系统默认语言
				 break;
			 case 1:
				 config.locale = Locale.ENGLISH;            //英文
				 break;
			 case 2:
				 config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文 
				 break;
			 default:
				 config.locale=Locale.getDefault(); 
				 break;
		}
		resources.updateConfiguration(config, dm);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.activity_main);		
		//获得资源文件
		mode1Bt = (Button) findViewById(R.id.bt_Mode1);
		mode2Bt = (Button) findViewById(R.id.bt_Mode2);
		fwVerTv = (TextView) findViewById(R.id.tv_fwVer);
		mode2Bt.setWidth(mode1Bt.getWidth());
		fwVerTv.setText(reporter.GetDrVersion());
		//按钮1监听器
		mode1Bt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,Mode1Activity.class);
				MainActivity.this.startActivityForResult(intent,0); 
				overridePendingTransition(R.anim.in_fromright,R.anim.out_fromleft); 
				//MainActivity.this.finish();
			}
		});
		//按钮2监听器
		mode2Bt.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,Mode2Activity.class);
				MainActivity.this.startActivityForResult(intent,0); 
				overridePendingTransition(R.anim.in_fromright,R.anim.out_fromleft); 
				//MainActivity.this.finish();
			}
		});
		
		ViewTreeObserver vto = mode1Bt.getViewTreeObserver(); 
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { 
		    public boolean onPreDraw() { 
		        int width = mode1Bt.getMeasuredWidth(); 
		        mode2Bt.setWidth(width);
		        return true; 
		    } 
		});  
		
		//版本更新检测
		updateThread updatethread = new updateThread();
		updatethread.start();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected (int featureId, MenuItem item){
		 switch (item.getItemId()){
		 	case R.id.action_about:
		 		//Toast.makeText(getApplicationContext(), "action_about", Toast.LENGTH_SHORT).show();
		 		DispAboutDialog();
		 		return true;
		 	case R.id.action_language:
		 		DispLanguageDialog();
		 		return true;
		 	case R.id.action_update:
		 		procUpdate();
		 		return true;
		 }
		return false;	
	}
	
	public void procUpdate(){
		isCreatUpdate = false;
		//版本更新检测
		updateThread updatethread = new updateThread();
		updatethread.start();
	}
	
	public void DispAboutDialog(){
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage(this.getResources().getString(R.string.aboutContext));//aboutCntxt
		builder.setTitle(this.getResources().getString(R.string.about));
		builder.setNegativeButton(this.getResources().getString(R.string.Enter), new DialogInterface.OnClickListener() { 
            
           @Override 
           public void onClick(DialogInterface dialog, int which) { 
               // TODO Auto-generated method stub
        	   dialog.dismiss();
           } 
		});
		builder.create().show();	
	}

	public void DispLanguageDialog(){
		
		String[] languages=context.getResources().getStringArray(R.array.Language);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.language);
        final SharedPreferences languagePre=context.getSharedPreferences("language_choice", Context.MODE_PRIVATE);
        final int id=languagePre.getInt("id", 0);
     
        builder.setSingleChoiceItems(languages, id,  new DialogInterface.OnClickListener(){
               @Override
               public void onClick(DialogInterface arg0, int index) {  
					switch (index) {
					case 0://系统默认语言                  
						languageId=0;
						break;
                    case 1: //英语
                    	languageId=1;
                    	break;
                    case 2://简体中文
                        languageId=2;
                        break;
                    default:
                    	languageId=0;
                    	break;
                    }
      
               }
        	});
    
        	//保存
        	builder.setPositiveButton(R.string.Enter, new DialogInterface.OnClickListener() {
    			@Override
                public void onClick(DialogInterface dialog, int which) {  
    		        final SharedPreferences languagePre=context.getSharedPreferences("language_choice", Context.MODE_PRIVATE);
    		        //final int id=languagePre.getInt("id", 0);
    		        Log.d("MainActivity","languageId="+languageId);
					//提交SharedPreferences
					languagePre.edit().putInt("id", languageId).commit();  	
					int id=languagePre.getInt("id", 0);
					Log.d("MainActivity","id="+id);
					
					//重启activity
					finish();
					Intent intent=new Intent();
                    intent.setClass(context, MainActivity.class);
					context.startActivity(intent);
    				//Toast.makeText(MainActivity.this, "enter", Toast.LENGTH_SHORT).show();
                }
        });
        //取消
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
    			@Override
                public void onClick(DialogInterface dialog, int which) { 
    				//Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
    				dialog.dismiss();
                }
        });
        builder.create().show();
	}
	
	@Override
	public void onResume(){
		Log.d("MainActivity","onResume");
        super.onResume();

	}
	
	@Override
	public void onBackPressed() {
		Log.d("MainActivity","onBackPressed");

	    super.onBackPressed();    	    
	    System.exit(0);
	}
	
	@Override
	protected void onDestroy() {
		Log.d("MainActivity","onDestroy");
		super.onDestroy();
		//System.exit(0);
	}	
	
	
	final Handler updateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case MSG_StartDialog:
					CreatCheckDialog();
					break;
				case MSG_DismissDialog:
					DismissCheckDialog();
					break;
				case MSG_noNewVersion:
					UpdateErrShow(!isCreatUpdate,updateErr.toString());
					break;
				case MSG_updateErr:
					UpdateErrShow(!isCreatUpdate,updateErr.toString());
					break;
				case MSG_hasNewVersion:
					if(isCreatUpdate)
						Toast.makeText(MainActivity.this,getResources().getString(R.string.hasnewversion)
								, Toast.LENGTH_LONG).show();
					else
						doNewVersionUpdate();
					break;
				case MSG_downComplete:
				      pBar.cancel();
                      update();
					break;
				case MSG_updateProcess:
					pBar.setProgress(updatetool.GetDownFileProgress());
					break;
				case MSG_downFail:
					 pBar.cancel();
					 //Toast.makeText(MainActivity.this, updatetool.GetLastError(), Toast.LENGTH_SHORT).show();
					 UpdateErrShow(!isCreatUpdate,updateErr.toString());
					break;	
				default:
					break;
			}
		}
	};
	
	public void CreatCheckDialog(){
		if(isCreatUpdate)
			return;
		startDialog = new AlertDialog.Builder(MainActivity.this)
        .setTitle(getResources().getString(R.string.checkforupdate))
        .setMessage(getResources().getString(R.string.checkingNow)+getResources().getString(R.string.pleasewait))
        .create();// 设置内容
		startDialog.setCanceledOnTouchOutside(false);
        // 显示对话框
		startDialog.show();
	}
	public void DismissCheckDialog(){
		if(startDialog==null)
			return;
		if(startDialog.isShowing())
			startDialog.dismiss();
	}
	
	public class updateThread extends Thread{		
		@Override
		public void run(){
			Looper.prepare();
			Message msg = null;
			msg = updateHandler.obtainMessage();
			msg.what = MSG_StartDialog;
			updateHandler.sendMessage(msg);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!networktool.isWifiConnected()){
				msg = updateHandler.obtainMessage();
				msg.what = MSG_updateErr;
				updateErr = new StringBuilder();
				updateErr.append(getResources().getString(R.string.wificlose)+"\n");
				updateErr.append(getResources().getString(R.string.wifiopen));
				updateHandler.sendMessage(msg);
			}
			else{
				int res = updatetool.CheckUpdate();
				if(res==0){
					msg = updateHandler.obtainMessage();
					msg.what = MSG_hasNewVersion;
					updateHandler.sendMessage(msg);
				}
				else if(res==1){
					msg = updateHandler.obtainMessage();
					msg.what = MSG_noNewVersion;
					String verName = Config.getVerName(MainActivity.this);
					updateErr = new StringBuilder();
					updateErr.append(getResources().getString(R.string.currentversion)+":"+verName+"\n");
					updateErr.append(getResources().getString(R.string.noneedupdate));
					updateHandler.sendMessage(msg);
				}
				else{
					msg = updateHandler.obtainMessage();
					msg.what = MSG_updateErr;
					updateErr = new StringBuilder();
					updateErr.append(getResources().getString(R.string.getvercodeerr)+"\n");
					updateErr.append(updatetool.GetLastError());
					updateHandler.sendMessage(msg);
				}
			}
			
			
			msg = updateHandler.obtainMessage();
			msg.what = MSG_DismissDialog;
			updateHandler.sendMessage(msg);
			Looper.loop();
		}
	}
	
	private void UpdateErrShow(boolean isDisp,String errinfo) {
		if(isDisp==false){
			return;
		}
		
        
        Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.softupdate)).setMessage(errinfo)// 设置内容
                        .setPositiveButton(getResources().getString(R.string.Enter),// 设置确定按钮
                                        new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                int which) {
                                                        //finish();
                                                }

                                        }).create();// 创建
        // 显示对话框
        dialog.show();
}

private void doNewVersionUpdate() {
//        int verCode = Config.getVerCode(MainActivity.this);
        String verName = Config.getVerName(MainActivity.this);
        StringBuffer sb = new StringBuffer();
        sb.append(getResources().getString(R.string.currentversion)+":");
        sb.append(verName+"\n");
        //sb.append("Code:");
        //sb.append(verCode+"\n");
        sb.append(getResources().getString(R.string.findnewversion)+":");
        sb.append(updatetool.newVerName+"\n");
        //sb.append("Code:");
        //sb.append(updatetool.newVerCode+"\n");
        sb.append(getResources().getString(R.string.isupdate));
        Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.softupdate))
                        .setMessage(sb.toString())
                        // 设置内容
                        .setPositiveButton(getResources().getString(R.string.Enter),// 设置确定按钮
                                        new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                int which) {
                                                		//Message msg;
                                                		//msg=updateHandler.obtainMessage();
                                                        pBar = new ProgressDialog(MainActivity.this);
                                                		pBar.setOnKeyListener(new OnKeyListener() {		
                                                			@Override
                                                			public boolean onKey(DialogInterface arg0,int keyCode, KeyEvent event) {
                                                				Log.d("MainActivity", "keyCode"+keyCode);
                                                				// TODO Auto-generated method stub
                                                	              if(keyCode == KeyEvent.KEYCODE_BACK )
                                                                  {
                                                	        			updatetool.InterruptDown();
                                                	        			return true;
                                                                  }
                                                	              else
                                                	            	  return false;
                                                			}
                                                		});
                                                        pBar.setTitle(getResources().getString(R.string.Downloading));
                                                        pBar.setMessage(getResources().getString(R.string.pleasewait));
                                                        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                        pBar.show();
                                                        
                                                        File file = new File(Environment.getExternalStorageDirectory(),
                                                                Config.UPDATE_SAVENAME);
                                                        updatetool.downFile(Config.UPDATE_SERVER
                                                            + Config.UPDATE_APKNAME,file);
                                                        
                                                        new Thread(){
                                                        	@Override
                                                        	public void run(){
                                                               	Message msg;
                                                        	
                                                        		int preprocess = updatetool.GetDownFileProgress();
                                                                while(updatetool.GetDownFileProgress()<100){
                                                                	msg=updateHandler.obtainMessage();
                                                                	try {
                                                        				Thread.sleep(50);
                                                        			} catch (InterruptedException e) {
                                                        				// TODO Auto-generated catch block
                                                        				e.printStackTrace();
                                                        			}
                                                                	if(updatetool.GetDownFileProgress()!=preprocess){
                                                                		preprocess = updatetool.GetDownFileProgress();
                                                                		msg.what = MSG_updateProcess;
                                                                		updateHandler.sendMessage(msg);       		
                                                                	}
                                                                }
                                                                if(updatetool.GetDownFileProgress()==100){
                                                                    msg=updateHandler.obtainMessage();
                                                                   	msg.what = MSG_downComplete;
                                                                	updateHandler.sendMessage(msg);
                                                                }
                                                                else{
                                                                    msg=updateHandler.obtainMessage();
                                                                   	msg.what = MSG_downFail;
                                                                   	updateErr = new StringBuilder();
                                                                   	updateErr.append(updatetool.GetLastError());
                                                                	updateHandler.sendMessage(msg);
                                                                }
                                                                
                                                        	}
                                                        }.start();
                                                }

                                        })
                        .setNegativeButton(getResources().getString(R.string.dontupdate),
                                        new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                                        // 点击"取消"按钮之后退出程序
                                                        //finish();
                                                }
                                        }).create();// 创建
        // 显示对话框
        dialog.show();
	}
	void update() {

	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setDataAndType(Uri.fromFile(new File(Environment
	                    .getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
	                    "application/vnd.android.package-archive");
	    startActivity(intent);
	}
		
}











