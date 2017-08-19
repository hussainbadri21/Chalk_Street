package com.hussain.chalkstreet;


        import android.Manifest;
        import android.app.Activity;
        import android.content.pm.ActivityInfo;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.media.CamcorderProfile;
        import android.media.MediaRecorder;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.view.LayoutInflater;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.androidhiddencamera.CameraConfig;
        import com.androidhiddencamera.CameraError;
        import com.androidhiddencamera.HiddenCameraFragment;
        import com.androidhiddencamera.config.CameraFacing;
        import com.androidhiddencamera.config.CameraImageFormat;
        import com.androidhiddencamera.config.CameraResolution;
        import com.androidhiddencamera.config.CameraRotation;

        import java.io.File;
        import java.io.IOException;

public class VideoCapture extends Activity implements View.OnClickListener, SurfaceHolder.Callback {
    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recorder = new MediaRecorder();
        initRecorder();
        setContentView(R.layout.activity_main);

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
             // prepareRecorder();

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);
    }

    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_LOW);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile("/storage/emulated/0/vid.mp4");

        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
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
        recorder.start();
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
        finish();
    }
}