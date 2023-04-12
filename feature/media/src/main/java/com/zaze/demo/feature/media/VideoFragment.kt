package com.zaze.demo.feature.media

import android.app.Activity
import android.content.Intent
import android.media.MediaExtractor
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.zaze.demo.feature.media.databinding.FragmentVideoBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class VideoFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentVideoBinding

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.playVideoView.stopPlayback()
                videoUri = it.data?.data?.also {uri->
                    binding.playVideoThumbIv.setImageBitmap(MediaHelper.frameAtTime(requireContext(), uri))
                    binding.playVideoView.setVideoURI(uri)
                }
                //            videoUri = Uri.parse(
//                "android.resource://${requireContext().packageName}/" + R.raw.vid_bigbuckbunny
//            )

            }
        }

    //
    private var mExtractor: MediaExtractor? = null
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().addMenuProvider(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.playVideoView.suspend()
    }
    override fun onDestroy() {
        super.onDestroy()
        requireActivity().removeMenuProvider(this)
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.action_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_play -> {
                binding.attribView.visibility = View.VISIBLE
                startPlayback()
                binding.playVideoView.setOnCompletionListener {
                    mExtractor?.release()
                    mExtractor = null
                    binding.playVideoView.setOnCompletionListener(null)
                    ZLog.d(ZTag.TAG_ERROR, "setOnCompletionListener")
                }
                binding.playVideoView.setOnErrorListener { mp, what, extra ->
                    ZLog.d(ZTag.TAG_ERROR, "setOnErrorListener: ${what};$extra ")
                    true
                }
                binding.playVideoView.start()
            }
            R.id.menu_select -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "video/*"
                videoLauncher.launch(intent)
            }
        }
        return true
    }


    private fun startPlayback() {
//        if(mExtractor == null) {
//            mExtractor = MediaExtractor()
//        }
//        mExtractor?.let {
//            // 设置数据源
//            it.setDataSource(requireContext(), videoUri, null)
//            //
//            val nTracks = it.trackCount
//
//            // Begin by unselecting all of the tracks in the extractor, so we won't see
//            // any tracks that we haven't explicitly selected.
//            for (i in 0 until nTracks) {
//                it.unselectTrack(i)
//            }


//        for (i in 0 until nTracks) {
//            mCodecWrapper = MediaCodecWrapper.fromVideoFormat(
//                mExtractor.getTrackFormat(i),
//                Surface(mPlaybackView.getSurfaceTexture())
//            )
//            if (mCodecWrapper != null) {
//                mExtractor.selectTrack(i)
//                break
//            }
//        }
//        }
    }
}