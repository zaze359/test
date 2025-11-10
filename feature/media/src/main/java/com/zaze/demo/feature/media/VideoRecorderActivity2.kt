package com.zaze.demo.feature.media

/**
 * Description:
 *
 * @author zhaozhen
 * @version 2025/6/5 09:06
 */
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoRecordEvent.Start
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.core.content.ContextCompat
import com.zaze.demo.feature.media.databinding.ActivityVideoRecorder2Binding
import com.zaze.utils.log.ZLog
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VideoRecorderActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityVideoRecorder2Binding
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoRecorder2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 检查并请求权限
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions.launch(REQUIRED_PERMISSIONS)
        }

        // 设置录制按钮
        binding.recordButton.setOnClickListener { captureVideo() }
        binding.switchCameraButton.setOnClickListener { switchCamera() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // 获取 CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // 设置预览
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // 创建视频录制器
            val recorder = Recorder.Builder()
                .setQualitySelector(
                    QualitySelector.fromOrderedList(
                        listOf(
                            Quality.HD,
                            Quality.FHD,
                            Quality.SD
                        ), FallbackStrategy.lowerQualityThan(Quality.SD)
                    )
                )
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                // 解绑之前的用例
                cameraProvider.unbindAll()

                // 绑定所有用例到相机
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    videoCapture
                )
            } catch (e: Exception) {
                ZLog.e(TAG, "使用相机失败: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        binding.recordButton.isEnabled = false

        if (recording != null) {
            // 停止录制
            recording?.stop()
            recording = null
            binding.recordButton.text = "开始录制"
            binding.recordButton.isEnabled = true
            return
        }
        // 创建视频保存文件
        val name = "VID_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                put(
                    android.provider.MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_MOVIES
                )
            }
        }
        ZLog.i(TAG, "创建视频保存文件: /${getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath}/${Environment.DIRECTORY_MOVIES}/${name}.mp4")

        // 创建录制输出选项
        ZLog.i(TAG, "创建录制输出选项中...")
        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        // 开始录制
        ZLog.i(TAG, "调用开始录制...")
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (ContextCompat.checkSelfPermission(
                        this@VideoRecorderActivity2,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { event ->
                ZLog.i(TAG, "event: ${event}")
                when (event) {
                    is Start -> {
                        binding.recordButton.apply {
                            text = "停止录制"
                            isEnabled = true
                        }
                    }

                    is Finalize -> {
                        if (event.hasError()) {
                            recording?.close()
                            recording = null
                            ZLog.e(TAG, "录制错误: ${event.error} ${event}")
                            Toast.makeText(this, "录制失败: ${event}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this,
                                "视频已保存: ${event.outputResults.outputUri}",
                                Toast.LENGTH_SHORT
                            ).show()
                            ZLog.d(TAG, "录制成功: ${event.outputResults.outputUri}")
                        }
                        binding.recordButton.text = "开始录制"
                    }
                }
            }
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            startCamera()
        } else {
            Toast.makeText(this, "需要相机和录音权限", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "VideoRecorderActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}