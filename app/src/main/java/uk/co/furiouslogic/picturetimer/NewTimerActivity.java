package uk.co.furiouslogic.picturetimer;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class NewTimerActivity extends ActionBarActivity {

    private static final java.lang.String SELECTED_CAMERA_ID_KEY = "_selectedCameraId";
    private static final int CAMERA_ID_NOT_SET = -1;
    private static final String LOG_TAG = "New Timer Activity";
    private boolean _hasCamera;
    private boolean _hasFrontCamera;
    private int _selectedCameraId = CAMERA_ID_NOT_SET;
    private Camera _selectedCamera;
    private Camera.Parameters _cameraParameters;
    private int _backFacingCameraId = CAMERA_ID_NOT_SET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timer);

        PackageManager pm = getPackageManager();
        _hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        _hasFrontCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        if (!_hasCamera)
            showNoCameraDialog();

        if (savedInstanceState != null)
            _selectedCameraId = savedInstanceState.getInt(SELECTED_CAMERA_ID_KEY, CAMERA_ID_NOT_SET);

        setupCameraControlsEventHandlers();

        onMenuOpenBackCamera();
    }

    public void onMenuOpenBackCamera() {
        _selectedCameraId = getBackFacingCameraId();
        openSelectedCamera();
    }

    void releaseSelectedCamera() {
        if (_selectedCamera != null) {
            CameraPreview cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
            cameraPreview.releaseCamera();

            _selectedCamera.release();
            _selectedCamera = null;
        }
    }

    void openSelectedCamera() {
        releaseSelectedCamera();

        if (_selectedCameraId != CAMERA_ID_NOT_SET)
            try {
                _selectedCamera = Camera.open(_selectedCameraId);

                CameraPreview cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
                cameraPreview.connectCamera(_selectedCamera, _selectedCameraId);
            } catch (Exception ex) {
                Log.e(LOG_TAG, ex.getMessage());
                String message = "Failed to open Camera Id: " + _selectedCameraId;
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
    }

    int getBackFacingCameraId() {
        if (_backFacingCameraId == CAMERA_ID_NOT_SET)
            _backFacingCameraId = getFacingCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);

        return _backFacingCameraId;
    }

    private int getFacingCameraId(int facing) {
        int cameraId = CAMERA_ID_NOT_SET;

        int nCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraInfoId = 0; cameraInfoId < nCameras; cameraInfoId++) {
            Camera.getCameraInfo(cameraInfoId, cameraInfo);
            if (cameraInfo.facing == facing) {
                cameraId = cameraInfoId;
                break;
            }
        }

        return cameraId;
    }

    private void setupCameraControlsEventHandlers() {

    }

    private void showNoCameraDialog() {
        CameraPreview cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
        cameraPreview.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
