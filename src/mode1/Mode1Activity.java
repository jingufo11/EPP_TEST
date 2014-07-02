package mode1;

import java.util.ArrayList;
import java.util.List;

import com.example.epp_test.R;
import com.kmy.epp.Epp;
import com.kmy.helper.LogHelper;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Mode1Activity extends FragmentActivity {

	//final String TitleStr[] = {"基本测试","明文测试","密文测试",""};
	final static String TitleStr[] = {"Base Setting","Press Test","Key Manager","PIN Test","Encry Test"};
	//通过pagerTabStrip可以设置标题的属性
	private List<Fragment>	fragmentList = new ArrayList<Fragment>();
	private List<String> titleList = new ArrayList<String>();
	private ActionBar bar ;
	private ViewPager vp;
	
    public Epp KMY3501;
    public String act_string;
    public SharedPreferences sharePrefSettings;
    public LogHelper logbook = new LogHelper();
    
    Page1 page1;
    Page2 page2;
    Page3 page3;
    Page4 page4;
    Page5 page5;
    int curItem = 0;
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode1);
        sharePrefSettings = getSharedPreferences("UARTLBPref", 0);
        KMY3501 = new Epp(this,sharePrefSettings);
		
		bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		int i =0;
		ActionBar.Tab[] tabArray = new ActionBar.Tab[TitleStr.length];
		for(i=0;i<TitleStr.length;i++){
			tabArray[i] = bar.newTab().setText(TitleStr[i]);
		}



        page1 = new Page1(KMY3501);
        page2 = new Page2(KMY3501);
        page3 = new Page3(KMY3501);
        page4 = new Page4(KMY3501);
        page5 = new Page5(KMY3501);
        

		tabArray[0].setTabListener(new MyTabsListener(page1, this));
		tabArray[1].setTabListener(new MyTabsListener(page2, this));
		tabArray[2].setTabListener(new MyTabsListener(page3, this));
		tabArray[3].setTabListener(new MyTabsListener(page4, this));
		tabArray[4].setTabListener(new MyTabsListener(page5, this));
		for(i=0;i<TitleStr.length;i++){
			bar.addTab(tabArray[i]);
		}
		
		vp = (ViewPager)findViewById(R.id.viewPager);
		fragmentList = new ArrayList<Fragment>();
		//加载界面
		fragmentList.add(page1);
        fragmentList.add(page2);
        fragmentList.add(page3);
        fragmentList.add(page4);
        fragmentList.add(page5);
        
        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
        vp.setOnPageChangeListener(new MyPageScrollEvent());
        vp.setCurrentItem(0);
     
	}
	
	class MyPageScrollEvent implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			bar.selectTab(bar.getTabAt(arg0));
		}
		
	}
	
	
	protected class MyTabsListener implements ActionBar.TabListener {
			FragmentTransaction fmt;
	
			public MyTabsListener(Fragment fragment,FragmentActivity activity) {
				//this.fragment = fragment;
				//this.mActivity = activity;
				
			}
	
			@Override
			public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
				// TODO Auto-generated method stub
				if(vp!=null)
					vp.setCurrentItem(tab.getPosition());	
			}
	
			@Override
			public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
				// TODO Auto-generated method stub	
			}
		}
	
	@Override
	public boolean onMenuItemSelected (int featureId, MenuItem item){
	/*
		 switch (item.getItemId()){
		 	case R.id.action_about:
		 		//Toast.makeText(getApplicationContext(), "action_settings", Toast.LENGTH_SHORT).show();
		 		return true;
		 }
		 */
		return false;	
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mode1, menu);
		 Log.d("Mode1Activity","TestToolActivity-onCreateOptionsMenu");
		return true;
	}
	
	//@Override
	public void onHomePressed() {
		onBackPressed();
		Log.d("Mode1Activity","TestToolActivity-onHomePressed");

	}	
	
	@Override
	public void onBackPressed() {
		Log.d("Mode1Activity","TestToolActivity-onBackPressed");
	    super.onBackPressed();    
	    finish();  
	    //overridePendingTransition(R.anim.in_fromleft,R.anim.out_fromright);
		//System.exit(0);//彻底关闭APP
	}	
	
	@Override
	protected void onResume() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();	
		//usb_to_uart  drive must do Resume()!!
		//if( 2 == KMY3501.UartResumeAccessory() )
		//{
			//cleanPreference();
			//restorePreference();
		//}
		Log.d("Mode1Activity","TestToolActivity-onResume");
	}

	@Override
	protected void onPause() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		Log.d("Mode1Activity","TestToolActivity-onPause");
	}

	@Override
	protected void onStop() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onStop();
		Log.d("Mode1Activity","TestToolActivity-onStop");
	}

	@Override
	protected void onDestroy() {
		KMY3501.FTUart.DestroyAccessory(true);
		//KMY3501.FTUart.
		super.onDestroy();
		Log.d("Mode1Activity","TestToolActivity-onDestroy");
		//System.exit(0);//彻底关闭APP
	}
	

	
	/**
	* 定义适配器
	* 
	* @author gxwu@lewatek.com 2012-11-15
	*/
	class myPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList){
            super(fm);
            this.fragmentList = fragmentList;
            //this.titleList = titleList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            //return (titleList.size() > position) ? titleList.get(position) : "";
        	return "";
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

}
