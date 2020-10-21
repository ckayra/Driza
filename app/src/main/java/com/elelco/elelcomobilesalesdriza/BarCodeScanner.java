package com.elelco.elelcomobilesalesdriza;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeScanner  extends Activity
{
    TextView barcodeInfoQR;
    TextView barcodeInfo39;
    SurfaceView cameraView;
    CameraSource cameraSource;
    Button additem;

    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;

    private void playSound(){



        if (loaded) {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            float actualVolume = (float) audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = actualVolume / maxVolume;
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            Log.e("Test", "Played sound");
        }
    }


    private void sendBarCode(){
        // Get the text from the EditText
        String stringToPassBack = barcodeInfoQR.getText().toString();
        String code39 = barcodeInfo39.getText().toString();
        // Put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("codice", stringToPassBack);
        intent.putExtra("codice39",code39);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        setContentView(R.layout.barcodescanner);

        //sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.pio, 1);


        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeInfoQR = (TextView) findViewById(R.id.txtContent);
        barcodeInfo39 = (TextView) findViewById(R.id.txtContent2);
        additem = (Button) findViewById(R.id.barcodescanner_additem);

        additem.setOnClickListener(new View.OnClickListener(){
            public void onClick(final View v)
            {
               /* // Get the text from the EditText
                String stringToPassBack = barcodeInfoQR.getText().toString();
                String code39 = barcodeInfo39.getText().toString();
                // Put the String to pass back into an Intent and close this activity
                Intent intent = new Intent();
                intent.putExtra("codice", stringToPassBack);
                intent.putExtra("codice39",code39);
                setResult(RESULT_OK, intent);
                finish();*/
               sendBarCode();
            }
        });

        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)//QR_CODE)
                        //  .setBarcodeFormats(Barcode.CODE_39)//QR_CODE)
                        //  .setBarcodeFormats(Barcode.QR_CODE)
                        .build();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)

                // .setRequestedPreviewSize(640, 480)
                .setRequestedPreviewSize(width, height)

                .build();


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();



                if (barcodes.size() != 0) {

                    barcodeInfoQR.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            playSound();

                            int format=  barcodes.valueAt(0).format;
                            if (format==2) barcodeInfo39.setText(  barcodes.valueAt(0).displayValue );
                            if (format==256) barcodeInfoQR.setText(  barcodes.valueAt(0).displayValue );
                            if (format==256) sendBarCode();
                        }
                    });
                }
            }
        });
    }
}
