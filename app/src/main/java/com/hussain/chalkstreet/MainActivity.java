package com.hussain.chalkstreet;

import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity which contains the recycler view
 * and where recording and playback of video is done
 */

public class MainActivity extends AppCompatActivity  {
    ArrayList<String> name;
    FloatingActionButton play;
    int i = 2,z=0;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    boolean recorded=false;
  private int currentCameraId=0;
    private boolean mInitSuccesful;
Map <Integer,String> m;
    Boolean playFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.recycler);
       //Function to initialize UI components
        initViews();

    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<String>();
        /*Hashmap to store position of recycler view item
         and location where the video is stored*/
        m=new HashMap<Integer,String>();

        name.add("1");

        final RecyclerView.Adapter adapter = new Adapter(name);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SwipeHelper((Adapter) adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //Adding first element of recycler view
                name.add(String.valueOf(i));
                i++;
                adapter.notifyDataSetChanged();
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int position=0;


                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    position = rv.getChildAdapterPosition(child);
                    Log.e("ffs", String.valueOf(adapter.getItemCount()) + " " + position);


                    if (position + 1 == adapter.getItemCount()) {


                        mSurfaceView = (SurfaceView) child.findViewById(R.id.CameraView);
                         play=(FloatingActionButton) child.findViewById(R.id.play);
                        FloatingActionButton pause=(FloatingActionButton) child.findViewById(R.id.stop);
                        FloatingActionButton delete=(FloatingActionButton) child.findViewById(R.id.delete);
                        FloatingActionButton change=(FloatingActionButton) child.findViewById(R.id.change);
                        play.setVisibility(View.GONE);
                        pause.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                          z=position+1;
                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playFlag=true;
                            }
                        });
                         //Event which occurs when camera change button is clicked
                        change.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (recorded) {
                                    mCamera.stopPreview();

                                }

                                mCamera.release();


                                if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                                }
                                else {
                                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                                }
                                mCamera = Camera.open(currentCameraId);

                               
                               try {

                                    mCamera.setPreviewDisplay(mHolder);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mCamera.startPreview();
                                recorded=false;
                            }


                        });

                        mHolder = mSurfaceView.getHolder();
                        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



                        mHolder.addCallback(new SurfaceHolder.Callback() {
                            @Override
                            public void surfaceCreated(SurfaceHolder holder) {
                                try {

                                    if (!mInitSuccesful) {

                                        mMediaRecorder.prepare();

                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                            }

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                if(recorded) {
                                    mCamera.stopPreview();
                                    recorded = false;
                                }

                                mMediaRecorder = null;
                                mCamera = null;

                            }
                        });
                        //Recording Video
                        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                                try {
                                    if (!recorded) {
                                        Toast.makeText(getApplicationContext(), "Recording", Toast.LENGTH_SHORT).show();
                                        initRecorder(mHolder.getSurface(),position);
                                        mMediaRecorder.start();
                                        recorded = true;
                                    } else {
                                        mMediaRecorder.stop();
                                        Toast.makeText(getApplicationContext(), "Recorded", Toast.LENGTH_SHORT).show();
                                        mCamera.stopPreview();
                                        play.setVisibility(View.VISIBLE);
                                        pause.setVisibility(View.VISIBLE);
                                        delete.setVisibility(View.VISIBLE);
                                        change.setVisibility(View.GONE);
                                        //recorded = false;
                                        Log.e("link",String.valueOf(m.get(position+1)));

                                        z=position+1;
                                       if(!recorded  && playFlag )
                                       {
                                           //Playing Video inside card
                                                mSurfaceView.setVisibility(View.GONE);
                                                RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl);
                                                VideoView video = new VideoView(getApplicationContext());
                                                video.setVideoPath(String.valueOf(m.get(z)));
                                                video.setLayoutParams(new FrameLayout.LayoutParams(550, 550));
                                                layout.addView(video);
                                            }



                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();


                                finish();
                            }

                    }

                   // Toast.makeText(getApplicationContext(), String.valueOf(name.get(position)), Toast.LENGTH_SHORT).show();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {



            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }



//Initializing Camera
    private void initRecorder(Surface surface,int position) throws IOException {

        if(mCamera == null) {
            mCamera = Camera.open();
            mCamera.unlock();

        }


        if(mMediaRecorder == null)  mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        //       mMediaRecorder.setOutputFormat(8);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);

        mMediaRecorder.setVideoSize(640, 480);
        //Location where the video file is stored
        String loc="/storage/emulated/0/vid"+(position+1)+".mp4";
        mMediaRecorder.setOutputFile(loc);
        m.put((position+1),loc);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        }
        mInitSuccesful = true;
    }



    }



