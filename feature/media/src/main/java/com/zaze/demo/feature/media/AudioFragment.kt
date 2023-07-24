package com.zaze.demo.feature.media

import android.app.Activity
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.zaze.demo.feature.media.databinding.FragmentAudioBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding

    private var uri: Uri? = null

    private var mediaPlayer: MediaPlayer? = null

    private val audioLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                uri = it.data?.data?.also { uri ->
                    stop()
                    play(uri)
                }
                ZLog.d(ZTag.TAG_DEBUG, "audioLauncher: $uri")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.audioChooseBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*"
            intent.setDataAndType(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*")
            audioLauncher.launch(intent)
        }
        binding.audioPlayBtn.setOnClickListener {
            play(uri)
        }
        binding.audioPauseBtn.setOnClickListener {
            pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun pause() {
        mediaPlayer?.pause()
    }

    private fun stop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    private fun play(uri: Uri?) {
        if(uri == null) {
            return
        }

        mediaPlayer?.start() ?: let {
            binding.audioThumbIv.setImageBitmap(MediaHelper.buildEmbeddedPicture(requireContext(), uri))
            mediaPlayer = MediaPlayer().also {
                it.reset()
                it.setDataSource(requireContext(), uri)
                it.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                it.prepare()
                it.setOnCompletionListener {
                    ZLog.d(ZTag.TAG_DEBUG, "setOnCompletionListener")
                }
                it.start()
            }
        }
    }
}