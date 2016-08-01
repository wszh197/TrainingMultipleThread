package com.wszh.weitrainingmultiplethread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private ImageView mImageView;
    private Button mLoadImageButton;
    private Button mToasButton;
    private ProgressBar mProgressBar;  //4.加进度条
    private static final int PROGRESS_VISIBILITY=0;


    //5.
    private Handler mHandler=new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressBar.setProgress(0);
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mProgressBar.setProgress((int)msg.obj);
                    break;
                case 2:
                    mImageView.setImageBitmap((Bitmap) msg.obj);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };

    //1,2,,3,4
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.activity_main_image);
        mLoadImageButton = (Button) findViewById(R.id.activity_main_load_image_button);
        mToasButton = (Button) findViewById(R.id.activity_main_toast_button);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progressbar);

        mLoadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadImage();   //1,2
                new LoadImageTask().execute();  //3
            }
        });

        mToasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"终于吐一下！！",Toast.LENGTH_SHORT).show();
            }
        });
    }



    /* 1.等loadImage结束 再Toast
    private void loadImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mImageView.setImageBitmap(bitmap);

    }
    */

    //2.使用多线程，可以同时
    /*
    private void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //mImageView.setImageBitmap(bitmap);
                //会把Runnable()放入MessageQueue，
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MainActivity","imageView",)
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();


    }
    */

    //3.使用AsyncTask
    /*
    class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }
    */

    /*1234
    //4.加进度条
    class LoadImageTask extends AsyncTask<Void, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            for (int i=1;i<=10;i++) {
                sleep();
                publishProgress(i*10);
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            return bitmap;
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }
1234*/

    //5.使用handler实现,本例不应用handler实现，应该用AsyncTask
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.activity_main_image);
        mLoadImageButton = (Button) findViewById(R.id.activity_main_load_image_button);
        mToasButton = (Button) findViewById(R.id.activity_main_toast_button);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progressbar);

        mLoadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                // Message msg=new Message();  //每次都new一个Message不好
                                Message msg=mHandler.obtainMessage();
                                //msg.what=0;  //让Progressbar显示出来
                                msg.what=PROGRESS_VISIBILITY;
                                //mHandler.sendMessage(msg);  //直接调用 handler 的发送消息方法发送消息
                                msg.sendToTarget();  //message 从handler 类获取，从而可以直接向该handler 对象发送消息
                                for(int i=1;i<11;i++) {
                                    sleep();
                                    //Message msg2 = new Message();
                                    Message msg2 =mHandler.obtainMessage();
                                    msg2.what=1;//表示让progressbar往前走
                                    msg2.obj=i*10;
                                    mHandler.sendMessage(msg2);
                                }
                                Bitmap bitmap=BitmapFactory.decodeResource(getResources(),
                                        R.drawable.ic_launcher);
                                Message msgBitMag=mHandler.obtainMessage();
                                msgBitMag.what=2;
                                msgBitMag.obj=bitmap;
                                mHandler.sendMessage(msgBitMag);
                            }
                            private void sleep() {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();
            }
        });

        mToasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"终于吐一下！！",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
