package mode2;

import java.util.ArrayList;
import java.util.List;

import com.example.epp_test.R;
import com.kmy.epp.EppT2;
import com.kmy.helper.LogHelper;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
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


public class Mode2Activity extends FragmentActivity {

	//final String TitleStr[] = {"基本测试","明文测试","密文测试",""};
	final static String TitleStr[] = {"Base Setting","Press Test","Key Manager","PIN Test","Encry Test"};
	//通过pagerTabStrip可以设置标题的属性
	private List<Fragment>	fragmentList = new ArrayList<Fragment>();
	private List<String> titleList = new ArrayList<String>();
	private ActionBar bar ;
	private ViewPager vp;
	
	Page1 page1;
	Page2 page2;
	Page3 page3;
	Page4 page4;
	Page5 page5;
	
    int curItem = 0;
	LogHelper logbook = new LogHelper();
	EppT2 KMY350X = new EppT2();
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode2);	
		Log.d("Mode2Activity", "onCreate");
		bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		int i =0;
		ActionBar.Tab[] tabArray = new ActionBar.Tab[TitleStr.length];
		for(i=0;i<TitleStr.length;i++){
			tabArray[i] = bar.newTab().setText(TitleStr[i]);
		}
		
        page1 = new Page1(KMY350X);      
        page2 = new Page2(KMY350X);

        page3 = new Page3(KMY350X);
        page4 = new Page4(KMY350X);
        page5 = new Page5(KMY350X);
        /**/        

		tabArray[0].setTabListener(new MyTabsListener(page1, this));
		tabArray[1].setTabListener(new MyTabsListener(page2, this));
		tabArray[2].setTabListener(new MyTabsListener(page3, this));
		tabArray[3].setTabListener(new MyTabsListener(page4, this));
		tabArray[4].setTabListener(new MyTabsListener(page5, this));
		for(i=0;i<TitleStr.length;i++){
			bar.addTab(tabArray[i]);
		}
		
		vp = (ViewPager)findViewById(R.id.SviewPager);
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
        /**/
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
			//FragmentTransaction fmt = mActivity.getSupportFragmentManager().beginTransaction();
			//fmt.add(R.id.fragment_place, fragment);
			//fmt.commit();
			if(vp!=null)
				vp.setCurrentItem(tab.getPosition());	
		}

		@Override
		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
			//FragmentTransaction fmt = mActivity.getSupportFragmentManager().beginTransaction();
			//fmt.remove(fragment);
			//fmt.commit();
			
		}
	}
	

	class myPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList){
            super(fm);
            this.fragmentList = fragmentList;
            //this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return (titleList.size() > position) ? titleList.get(position) : "";
        	return "";
        }
   
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }
	/**/	
	
/*	
	@Override
	public boolean onMenuItemSelected (int featureId, MenuItem item){
		 switch (item.getItemId()){
		 	case R.id.action_settings:
		 		Toast.makeText(getApplicationContext(), "action_settings", Toast.LENGTH_SHORT).show();
		 		return true;
		 	case R.id.action_switch:
		 		Toast.makeText(getApplicationContext(), "action_switch", Toast.LENGTH_SHORT).show();
		 		Switch_FTDActivity();
		 		return true;
		 }
		return false;	
	}
*/
		
	@Override
	protected void onPause() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		Log.d("Mode2Activity", "onPause");
	}

	@Override
	protected void onStop() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onStop();
		Log.d("Mode2Activity", "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		 Log.d("Mode2Activity", "onDestroy");
		//System.exit(0);//彻底关闭APP
	}
	
	//@Override
	public void onHomePressed() {
		onBackPressed();
		Log.d("Mode2Activity","onHomePressed");
	}	

	public void onBackPressed() {
		Log.d("Mode2Activity","onBackPressed");
	    super.onBackPressed();
	    KMY350X.CloseComm();
	    this.finish();
	    //overridePendingTransition(R.anim.in_fromleft,R.anim.out_fromright); 
	    /*
	     Log.d("Mode2Activity","onBackPressed");
		 new Handler().postDelayed(new Runnable(){
			 @Override
			 public void run(){
				 Intent intent = new Intent(Mode2Activity.this,TestToolActivity.class);
				 Mode2Activity.this.startActivityForResult(intent,0); 
				 Mode2Activity.this.finish();
			 }
		 },10);
		 */
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mode2, menu);
		return true;
	}
	
	 @Override  
	 public boolean onOptionsItemSelected(MenuItem item) { 
/*
		 switch (item.getItemId()){
		 	case R.id.action_settings:
		 		Toast.makeText(getApplicationContext(), "action_settings", Toast.LENGTH_SHORT).show();
		 		return true;
		 }
	 */
		return false;
	 }

	
	
		
}
