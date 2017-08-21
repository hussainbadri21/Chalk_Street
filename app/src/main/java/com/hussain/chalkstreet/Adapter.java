package com.hussain.chalkstreet;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;

public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private ArrayList<String> name;
    int i=0,seen=0;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    //private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    boolean recorded=false,paused=false;
    private boolean mInitSuccesful;
    VideoView video;
    private static int camId =0;

    /**
     * Adapter to load elements into recycler view
     * @param name -  Used to store title of the name file
     */


    public Adapter(ArrayList<String> name) {
        this.name = name;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter.ViewHolder viewHolder,final int i) {

        viewHolder.tv.setText(name.get(i));
        Log.e("gg",String.valueOf(getItemCount())+" "+i);
        viewHolder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Camera.getNumberOfCameras() > 1 && camId == 0) {
                    Toast.makeText(v.getContext(), "Camera Switched to Front", Toast.LENGTH_SHORT).show();
                    camId = 1;
                } else {
                    Toast.makeText(v.getContext(), "Camera Switched to Back", Toast.LENGTH_SHORT).show();
                    camId=0;
                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(i);
            }
        });


        viewHolder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.play.setVisibility(View.GONE);
                viewHolder.pause.setVisibility(View.GONE);
                viewHolder.delete.setVisibility(View.GONE);
                viewHolder.change.setVisibility(View.GONE);
                mHolder = viewHolder.sv.getHolder();
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
                        Toast.makeText(v.getContext(), "Recording", Toast.LENGTH_SHORT).show();
                        initRecorder(mHolder.getSurface(),(viewHolder.getAdapterPosition()+1));
                        mMediaRecorder.start();
                        recorded = true;
                    } else {
                        mMediaRecorder.stop();
                        Toast.makeText(v.getContext(), "Recorded", Toast.LENGTH_SHORT).show();
                        mCamera.stopPreview();
                        viewHolder.play.setVisibility(View.VISIBLE);
                        viewHolder.pause.setVisibility(View.GONE);
                        viewHolder.delete.setVisibility(View.VISIBLE);
                        viewHolder.change.setVisibility(View.GONE);
                        viewHolder.record.setVisibility(View.GONE);
                       /* play.setVisibility(View.VISIBLE);
                        pause.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                        change.setVisibility(View.GONE);*/



                    }
                } catch (IOException e1) {
                    e1.printStackTrace();


                   // finish();
                }
            }
        });
        viewHolder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.pause.setVisibility(View.GONE);
                viewHolder.record.setVisibility(View.GONE);
                viewHolder.change.setVisibility(View.GONE);
                viewHolder.play.setVisibility(View.VISIBLE);

                if(video!=null) {
                   seen= video.getCurrentPosition();
                    video.pause();

                    paused = true;
                }

            }
        });

        viewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.pause.setVisibility(View.VISIBLE);
                viewHolder.record.setVisibility(View.GONE);
                viewHolder.change.setVisibility(View.GONE);
                viewHolder.play.setVisibility(View.GONE);
                if(paused)
                {

                    video.seekTo(seen);
                    video.start();
                    paused=false;
                }
                //viewHolder.sv.setVisibility(View.GONE);
                viewHolder.record.setVisibility(View.GONE);
                viewHolder.pause.setVisibility(View.VISIBLE);

                 video = new VideoView(v.getContext());
                Log.e("loc","/storage/emulated/0/vid"+(viewHolder.getAdapterPosition()+1)+".mp4");
                video.setVideoPath("/storage/emulated/0/vid"+(viewHolder.getAdapterPosition()+1)+".mp4");
                video.setLayoutParams(viewHolder.sv.getLayoutParams());
                viewHolder.sv.setVisibility(View.INVISIBLE);
                viewHolder.layout.addView(video);

                video.start();
               // viewHolder.pause.setVisibility(View.VISIBLE);

            }
        });


    }

    /**
     * Returns size of Recycler view
     * @return
     */
    @Override
    public int getItemCount() {
        return name.size();
    }
    public void dismiss(int pos)
    {
        name.remove(pos);
        this.notifyItemRemoved(pos);
    }
    private void initRecorder(Surface surface,int position) throws IOException {

        if(mCamera == null) {
            mCamera = Camera.open(camId);
            mCamera.setDisplayOrientation(90);
            mCamera.unlock();

        }


        if(mMediaRecorder == null)  mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        /*mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);


        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);

        mMediaRecorder.setVideoSize(640, 480);*/
        //Location where the video file is stored
        String loc="/storage/emulated/0/vid"+position+".mp4";
        mMediaRecorder.setOutputFile(loc);
       // m.put((0),loc);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        }
        mInitSuccesful = true;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        SurfaceView sv;
        FloatingActionButton play,pause,record,change,delete;
        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.title);
            sv = (SurfaceView) view.findViewById(R.id.CameraView);
            play=(FloatingActionButton)view.findViewById(R.id.play);
            pause=(FloatingActionButton)view.findViewById(R.id.stop);
            delete=(FloatingActionButton)view.findViewById(R.id.delete);
            change=(FloatingActionButton)view.findViewById(R.id.change);
             layout= (RelativeLayout)view.findViewById(R.id.rl);


            record=(FloatingActionButton)view.findViewById(R.id.record);

        }
    }


}

