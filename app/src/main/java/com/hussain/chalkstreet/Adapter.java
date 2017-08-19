package com.hussain.chalkstreet;

import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder>implements View.OnClickListener, SurfaceHolder.Callback {
    private ArrayList<String> name;
    int i=0;



    public Adapter(ArrayList<String> name) {
        this.name = name;
    }
    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        recorder = new MediaRecorder();
        initRecorder();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder viewHolder, int i) {

        viewHolder.tv.setText(name.get(i));
        holder = viewHolder.sv.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // prepareRecorder();

        viewHolder.sv.setClickable(true);
        viewHolder.sv.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public void dismiss(int pos)
    {
        name.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        SurfaceView sv;
        public ViewHolder(View view) {
            super(view);

            tv = (TextView)view.findViewById(R.id.title);
            sv = (SurfaceView) view.findViewById(R.id.CameraView);

        }
    }
    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile("/storage/emulated/0/Video/"+String.valueOf(i)+".mp4");
        i++;

        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
           // finish();
        } catch (IOException e) {
            e.printStackTrace();
            //finish();
        }
    }

    public void onClick(View v) {
        if (recording) {
            recorder.stop();
            recording = false;

            // Let's initRecorder so we can record again
            initRecorder();
            prepareRecorder();
        } else {

            recording = true;
            recorder.start();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
       // recorder.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();

    }


}

