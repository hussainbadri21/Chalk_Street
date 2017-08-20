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
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    ArrayList<String> name;
    int i = 2;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    boolean recorded=false;

    private boolean mInitSuccesful;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.recycler);

        initViews();

    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<String>();
        name.add("1");

        final RecyclerView.Adapter adapter = new Adapter(name);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SwipeHelper((Adapter) adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        FloatingActionButton play=(FloatingActionButton) child.findViewById(R.id.play);
                        FloatingActionButton pause=(FloatingActionButton) child.findViewById(R.id.stop);
                        FloatingActionButton delete=(FloatingActionButton) child.findViewById(R.id.delete);
                        play.setVisibility(View.GONE);
                        pause.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);

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
                                mMediaRecorder.reset();
                                mMediaRecorder.release();
                                mCamera.release();

                                // once the objects have been released they can't be reused
                                mMediaRecorder = null;
                                mCamera = null;

                            }
                        });
                        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                                try {
                                    if (!recorded) {
                                        Toast.makeText(getApplicationContext(), "Recording", Toast.LENGTH_SHORT).show();
                                        initRecorder(mHolder.getSurface());
                                        mMediaRecorder.start();
                                        recorded = true;
                                    } else {
                                        mMediaRecorder.stop();
                                        Toast.makeText(getApplicationContext(), "Recorded", Toast.LENGTH_SHORT).show();
                                        recorded = false;


                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();


                                finish();
                            }

                    }

                    Toast.makeText(getApplicationContext(), String.valueOf(name.get(position)), Toast.LENGTH_SHORT).show();

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Toast.makeText(getApplicationContext(), "touched", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }



    private void initRecorder(Surface surface) throws IOException {

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
        mMediaRecorder.setOutputFile("/storage/emulated/0/vid.mp4");

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        }
        mInitSuccesful = true;
    }



    }



