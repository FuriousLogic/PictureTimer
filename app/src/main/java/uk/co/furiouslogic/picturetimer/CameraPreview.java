package uk.co.furiouslogic.picturetimer;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by bdonalds-admin on 15/05/2015.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    Camera _camera;
    SurfaceHolder _holder;
    private String LOG_TAG = "CameraPreview";

    public CameraPreview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CameraPreview(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        stopPreview();
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
    }

    public void connectCamera(Camera camera, int cameraId) {
        _camera = camera;

        int cameraPreviewOrientation = CameraHelper.getDisplayOrientationForCamera(cameraId, getContext());
        _camera.setDisplayOrientation(cameraPreviewOrientation);

        _holder = getHolder();
        _holder.addCallback(this);

        startPreview();
    }

    public void releaseCamera() {
        if (_camera != null) {

            stopPreview();
            _camera = null;
        }
    }

    void startPreview() {
        if (_camera != null && _holder.getSurface() != null) {
            try {
                _camera.setPreviewDisplay(_holder);
                _camera.startPreview();
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error getting preview display: " + ex.getMessage());
            }
        }
    }

    void stopPreview() {
        if (_camera != null) {
            try {
                _camera.stopPreview();
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error stopping preview display: " + ex.getMessage());
            }
        }
    }
}
