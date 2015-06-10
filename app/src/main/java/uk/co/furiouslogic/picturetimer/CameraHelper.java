package uk.co.furiouslogic.picturetimer;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bdonalds-admin on 18/05/2015.
 */
public class CameraHelper {
    private static final String LOG_TAG = "CameraHelper";

    public static File generateTimeStampPhotoFile() {
        File photoFile = null;
        File outputDir = getPhotoDirectory();

        if (outputDir != null) {
            String timestamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
            String photoFileName = "IMG_LWC_" + timestamp + ".jpg";

            photoFile = new File(outputDir, photoFileName);
        }

        return photoFile;
    }

    public static File generateTimeStampVideoFile() {
        File videoFile = null;
        File outputDir = getVideoDirectory();

        if (outputDir != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String videoFileName = "VID_LWC_" + timeStamp + ".mp4";

            videoFile = new File(outputDir, videoFileName);
        }

        return videoFile;
    }

    public static int getDisplayOrientationForCamera(int cameraId, Context context) {
        final int DEGREES_IN_A_CIRCLE = 360;
        int temp = 0;
        int previewOrientation = 0;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        int deviceOrientation = getDeviceOrientationDegrees(context);
        switch (cameraInfo.facing) {
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                temp = cameraInfo.orientation - deviceOrientation + DEGREES_IN_A_CIRCLE;
                previewOrientation = temp % DEGREES_IN_A_CIRCLE;
                break;
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
                temp = (cameraInfo.orientation + deviceOrientation) % DEGREES_IN_A_CIRCLE;
                previewOrientation = (DEGREES_IN_A_CIRCLE - temp) % DEGREES_IN_A_CIRCLE;
                break;
        }

        return previewOrientation;
    }

    static int getDeviceOrientationDegrees(Context context) {
        int degrees = 0;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        return degrees;
    }

    private static File getPhotoDirectory() {
        File outputDir = null;
        String externalStoreageState = Environment.getExternalStorageState();
        if (externalStoreageState.equals(Environment.MEDIA_MOUNTED)) {
            File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(pictureDir, "LightweightCamera");
            if (!outputDir.exists()) {
                if (!outputDir.mkdir()) {
                    outputDir = null;
                }
            }
        }

        return outputDir;
    }

    private static File getVideoDirectory() {
        File outputDir = null;
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            File videoDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            outputDir = new File(videoDir, "LightweightCamera");
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    String message = "Failed to create directory:" + outputDir.getAbsolutePath();
                    Log.d(LOG_TAG, message);
                    outputDir = null;
                }
            }
        }

        return outputDir;
    }

    public static MediaRecorder createAndPrepareVideoRecorder(Context context, Camera camera, int cameraId, int camcorderProfile, String outputFileName) {
        if (camera == null)
            return null;

        MediaRecorder mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

        mediaRecorder.setProfile(CamcorderProfile.get(camcorderProfile));
        mediaRecorder.setOutputFile(outputFileName);

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            String message = "Error preparing video recorder. " + e.getMessage();
            Log.e(LOG_TAG, message);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            releaseVideoRecorder(mediaRecorder, camera);
            mediaRecorder = null;
        }

        return mediaRecorder;
    }

    public static void releaseVideoRecorder(MediaRecorder mediaRecorder, Camera camera) {

        if (mediaRecorder == null)
            return;

    }
}
