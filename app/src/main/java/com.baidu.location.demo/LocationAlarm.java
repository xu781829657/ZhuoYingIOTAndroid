//package com.baidu.location.demo;
//import com.baidu.baidulocationdemo.R;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.service.LocationService;
//import com.baidu.location.service.WriteLog;
//import android.app.Activity;
//import android.location.Location;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
///***
// * 自定义距离回调示例，注意：如果使用gps结果，回调结果比较准确，
// * 网络定位结果由于精度较差(误差在20m～60m之间),
// * 所有如果是网络定位，建议不要设置太小
// * @author baidu
// *
// */
//public class LocationAlarm extends Activity{
//	private LocationService locationService;
//	private EditText callbackDistance;
//	private Button startLoc;
//	private TextView log;
//	private BDLocation lastLocation = null;
//	private int defaultDistance = 20;
//	private int columnCount = 0;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		locationService = ((LocationApplication)getApplication()).locationService;
//		LocationClientOption mOption = locationService.getDefaultLocationClientOption();
//		mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//		mOption.setCoorType("bd09ll");
//		locationService.setLocationOption(mOption);
//		locationService.registerListener(mListener);
//		setContentView(R.layout.locationalarm);
//		callbackDistance = (EditText)findViewById(R.id.call_back_distance);
//		startLoc = (Button)findViewById(R.id.startlocationalarm);
//		log = (TextView)findViewById(R.id.alarmlog);
//	}
//	private void logMsg(String str) {
//	        try {
//	            if (log != null){
//	            	  String laststr = log.getText().toString();
//	            	if(columnCount > 50){
//	            		laststr = "";
//	            		columnCount = 1;
//	            	}
//	            	else
//	            		columnCount++;
//	            	laststr +=str +"\n";
//	            	log.setText(laststr);
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    }
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		locationService.stop();
//	}
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		startLoc.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				try {
//					defaultDistance = Integer.parseInt(callbackDistance.getText().toString());
//				} catch (Exception e) {
//					// TODO: handle exception
//					defaultDistance  = 20;
//				}
//				 if(startLoc.getText().equals(getString(R.string.startlocation))){
//		            	locationService.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
//		            	startLoc.setText(getString(R.string.stoplocation));
//		            }else{
//		            	locationService.stop();
//		            	startLoc.setText(getString(R.string.startlocation));
//		            }
//			}
//		});
//	}
//	  private BDLocationListener mListener = new BDLocationListener() {
//
//			@Override
//			public void onReceiveLocation(BDLocation location) {
//				// TODO Auto-generated method stub
//				if(null != location && location.getLocType() != BDLocation.TypeServerError){
//					WriteLog.getInstance().writeLog(location.getLongitude()+","+location.getLatitude());
//					if(lastLocation == null){
//						 lastLocation = location;
//						 logMsg("call back:"+location.getLongitude()+","+location.getLatitude()); //GPS初始阶段会存在定位失败情况
//					 }else{
//						 float[] distance = new float[1];
//						 Location.distanceBetween(lastLocation.getLatitude(), lastLocation.getLongitude(), location.getLatitude(), location.getLongitude(), distance);
//						 if(distance[0] >= defaultDistance){
//							 lastLocation = location;
//							 logMsg("call back:"+location.getLongitude()+","+location.getLatitude());
//						 }
//					 }
//				}
//			}
//
//		};
//}
