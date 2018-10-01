package org.freeman.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity, also implements view on click listener
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String LOG_TAG = MainActivity.class.getSimpleName();

  private CameraManager mCameraManager;
  private CaptureRequest.Builder mCaptureRequestBuilder;
  private CameraCaptureSession mCameraCaptureSession;
  private CameraDevice mCameraDevice;
  private SurfaceTexture mSurfaceTexture;
  private Surface mSurface;
  private Button mButton;
  private boolean isFlashlightOn;

  /**
   * Main activity create
   * <p>
   * Initialize and turn on camera flashlight
   *
   * @param savedInstanceState saved instance state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    setContentView(R.layout.activity_main);
    checkFlashAvailable();
    initialize();
    mButton = (Button) findViewById(R.id.buttonFlashlight);
    mButton.setText(R.string.button_name_off);
    mButton.setOnClickListener(this);
  }

  /**
   * Handle on click
   *
   * @param view view from activity main xml
   */
  @Override
  public void onClick(View view) {
    if (isFlashlightOn) {
      turnOffFlashlight();
      mButton.setText(R.string.button_name_on);
    } else {
      turnOnFlashlight();
      mButton.setText(R.string.button_name_off);
    }
    isFlashlightOn = !isFlashlightOn;
  }

  /**
   * Check device has camera with flashlight
   */
  private void checkFlashAvailable() {
    Boolean isFlashAvailable = getApplicationContext()
            .getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    if (!isFlashAvailable) {
      AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
      alert.setTitle("No Flash Light");
      alert.setMessage("Device does not support flash light");
      alert.setButton(DialogInterface.BUTTON_POSITIVE, "FINE",
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  System.exit(0);
                }
              });
      alert.show();
    }
  }

  /**
   * Initialize
   */
  @TargetApi(value = 21)
  private void initialize() {
    final String cameraId;
    this.mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      cameraId = this.mCameraManager.getCameraIdList()[0];
      this.mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
          try {
            mCameraDevice = camera;
            mCaptureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            List<Surface> outputs = new ArrayList<>();
            mSurfaceTexture = new SurfaceTexture(1);
            mSurface = new Surface(mSurfaceTexture);
            outputs.add(mSurface);
            mCaptureRequestBuilder.addTarget(mSurface);
            camera.createCaptureSession(outputs, new CameraCaptureSession.StateCallback() {
              @Override
              public void onConfigured(@NonNull CameraCaptureSession session) {
                mCameraCaptureSession = session;
                try {
                  session.setRepeatingRequest(mCaptureRequestBuilder.build(), null, null);
                } catch (CameraAccessException e) {
                  Log.i(LOG_TAG, e.getMessage());
                }
              }

              @Override
              public void onConfigureFailed(@NonNull CameraCaptureSession session) {

              }
            }, null);
          } catch (CameraAccessException e) {
            Log.i(LOG_TAG, e.getMessage());
          }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
      }, null);
      this.isFlashlightOn = true;
    } catch (CameraAccessException | SecurityException e) {
      Log.i(LOG_TAG, e.getMessage());
    }
  }

  /**
   * Turn On
   */
  @TargetApi(value = 21)
  private void turnOnFlashlight() {
    try {
      mCaptureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
      mCameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, null);
    } catch (CameraAccessException e) {
      Log.i(LOG_TAG, e.getMessage());
    }
  }

  /**
   * Turn Off
   */
  @TargetApi(value = 21)
  private void turnOffFlashlight() {
    try {
      mCaptureRequestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
      mCameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, null);
    } catch (CameraAccessException e) {
      Log.i(LOG_TAG, e.getMessage());
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    destroy();
    this.mCameraCaptureSession = null;
    this.mCameraDevice = null;
  }

  /**
   * Close camera capture session first and then close camera device
   */
  @TargetApi(value = 21)
  private void destroy() {
    if (this.mCameraCaptureSession != null) {
      this.mCameraCaptureSession.close();
    }
    if (this.mCameraDevice != null) {
      this.mCameraDevice.close();
    }
    this.mCameraCaptureSession = null;
    this.mCameraDevice = null;
  }

  /**
   * Configuration changes
   *
   * @param newConfig new configuration
   */
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.i(LOG_TAG, "Configuration changes");
  }

}
