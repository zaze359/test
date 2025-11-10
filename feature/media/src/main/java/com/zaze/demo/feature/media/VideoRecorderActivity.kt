package com.zaze.demo.feature.media


/**
 * Description: 录制页面
 *
 * @author zhaozhen
 * @version 2025/6/4 21:23
 */
import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zaze.common.base.AbsActivity
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VideoRecorderActivity : AbsActivity(), SurfaceHolder.Callback {
    private val TAG = "VideoRecorderActivity"
    private val REQUEST_PERMISSION_CODE = 100

    private var camera: Camera? = null
    private var mediaRecorder: MediaRecorder? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var isRecording = false
    private var surfaceCreated = false

    private lateinit var surfaceView: SurfaceView
    private lateinit var recordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_recorder)

        surfaceView = findViewById(R.id.surfaceView)
        recordButton = findViewById(R.id.recordButton)

        surfaceHolder = surfaceView.holder
        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        recordButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "需要权限才能录制视频", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
            }
            prepareCamera()
        }
    }

    private fun prepareCamera() {
        try {
            releaseCamera()
            camera = Camera.open()
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: Exception) {
            Log.e(TAG, "相机初始化失败: ${e.message}")
            Toast.makeText(this, "相机初始化失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {
        if (camera == null || !surfaceCreated) return

        try {
            camera?.unlock()
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setCamera(camera)

            // 设置音频和视频源
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
            mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.CAMERA)

            // 设置输出格式和编码器
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP)

            // 设置视频参数
            mediaRecorder?.setVideoSize(640, 480)
            mediaRecorder?.setVideoFrameRate(30)
            mediaRecorder?.setVideoEncodingBitRate(3 * 1024 * 1024)
            mediaRecorder?.setOrientationHint(90)

            // 设置输出文件
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            val outputFile = "${storageDir?.absolutePath}/VID_$timeStamp.mp4"
            ZLog.d(ZTag.TAG, "outputFile: $outputFile")
            mediaRecorder?.setOutputFile(outputFile)

            // 设置预览
            mediaRecorder?.setPreviewDisplay(surfaceHolder?.surface)

            // 准备并开始录制
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
            recordButton.text = "停止录制"
            Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e(TAG, "录制准备失败: ${e.message}")
            releaseMediaRecorder()
            Toast.makeText(this, "录制准备失败", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "录制启动失败: ${e.message}")
            releaseMediaRecorder()
            Toast.makeText(this, "录制启动失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        if (mediaRecorder != null && isRecording) {
            try {
                mediaRecorder?.stop()
                Toast.makeText(this, "录制已保存", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "停止录制失败: ${e.message}")
                Toast.makeText(this, "停止录制失败", Toast.LENGTH_SHORT).show()
            } finally {
                releaseMediaRecorder()
                isRecording = false
                recordButton.text = "开始录制"
                prepareCamera()
            }
        }
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.reset()
        mediaRecorder?.release()
        mediaRecorder = null
        camera?.lock()
    }

    private fun releaseCamera() {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceCreated = true
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            prepareCamera()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (surfaceHolder?.surface == null) return

        try {
            camera?.stopPreview()
        } catch (e: Exception) {
            Log.e(TAG, "停止预览失败: ${e.message}")
        }

        try {
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: Exception) {
            Log.e(TAG, "启动预览失败: ${e.message}")
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        surfaceCreated = false
        stopRecording()
        releaseCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        releaseCamera()
    }

    override fun onPause() {
        super.onPause()
        stopRecording()
        releaseCamera()
    }
}