package com.seeyon.line;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.seeyon.line.view.ContentView;
import com.seeyon.line.view.Drawl.GestureCallBack;

public class MainActivity extends Activity implements SensorEventListener{

	private FrameLayout body_layout;
	private ContentView content;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager_g;
    private Sensor mAccelerometer_g;
    private String hostname = "192.168.1.108";//要连接的PC端的IP地址
    private final int PORT=60000;
    private String[] oldAcc={"0","0","0"};
    private String[] oldGyr={"0","0","0"};
    
	@Override
	protected void onStart() {
		super.onStart();
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager_g = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer_g = mSensorManager_g.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager_g.registerListener(this, mAccelerometer_g, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		body_layout = (FrameLayout) findViewById(R.id.body_layout);

		content = new ContentView(this, "75342619",new GestureCallBack() {
			@Override
			public void checkedSuccess(){}
			
			@Override
			public void checkedFail(){}
		});
		content.setParentView(body_layout);
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	@Override
	public void onSensorChanged(final SensorEvent event) {
		new Thread(new Runnable() { 
			@Override 
			public void run() { 
				try {
					//创建socket
					Socket socket = new Socket(hostname, PORT);
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));   
		            
					if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
		            	writer.write(event.values[0]+" "+event.values[1]+" "+event.values[2]+" "+oldGyr[0]+" "+oldGyr[1]+" "+oldGyr[2]+"\n");
		            	for(int i = 0;i<3;i++){
		            		oldAcc[i] = event.values[i]+"";
		            	}
		        	}
		        	else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
		        		writer.write(oldAcc[0]+" "+oldAcc[1]+" "+oldAcc[2]+" "+event.values[0]+" "+event.values[1]+" "+event.values[2]+"\n");
		        		for(int i = 0;i <3;i++){
		        			oldGyr[i] = event.values[i]+"";
		        		}
		        	}
		            writer.flush();  
		            writer.close();   
				} catch (UnknownHostException e) {
					Toast.makeText(getApplicationContext(), "unknown host", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "ioexception", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}).start(); 
	}
}