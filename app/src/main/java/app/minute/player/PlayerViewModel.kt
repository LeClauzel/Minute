package app.minute.player

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private var _player: ExoPlayer? = null
    val player: Player? get() = _player

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _fileName = MutableStateFlow("Unknown")
    val fileName: StateFlow<String> = _fileName.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    init {
        _player = ExoPlayer.Builder(application).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }

                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        _duration.value = duration
                    } else if (state == Player.STATE_ENDED) {
                        if (_repeatMode.value == Player.REPEAT_MODE_OFF) {
                            _isPlaying.value = false
                        }
                    }
                }

                override fun onRepeatModeChanged(repeatMode: Int) {
                    _repeatMode.value = repeatMode
                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                    _playbackSpeed.value = playbackParameters.speed
                }
            })
        }
    }

    fun playUri(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        _player?.setMediaItem(mediaItem)
        _player?.prepare()
        _player?.play()
        
        // Try to get the display name from ContentResolver
        var name = uri.lastPathSegment ?: "Audio File"
        getApplication<Application>().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
        _fileName.value = name
    }

    fun togglePlayPause() {
        if (_player?.isPlaying == true) {
            _player?.pause()
        } else {
            _player?.play()
        }
    }

    fun seekTo(position: Long) {
        _player?.seekTo(position)
        _currentPosition.value = position
    }

    fun seekRelative(offsetMs: Long) {
        val newPosition = (_player?.currentPosition ?: 0) + offsetMs
        val clampedPosition = newPosition.coerceIn(0, _duration.value)
        _player?.seekTo(clampedPosition)
        _currentPosition.value = clampedPosition
    }

    fun toggleRepeatMode() {
        val newMode = if (_repeatMode.value == Player.REPEAT_MODE_OFF) {
            Player.REPEAT_MODE_ONE
        } else {
            Player.REPEAT_MODE_OFF
        }
        _player?.repeatMode = newMode
    }

    fun cyclePlaybackSpeed() {
        val speeds = listOf(0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)
        val currentIndex = speeds.indexOf(_playbackSpeed.value)
        val nextIndex = (currentIndex + 1) % speeds.size
        val newSpeed = speeds[nextIndex]
        _player?.playbackParameters = PlaybackParameters(newSpeed)
    }

    fun updateProgress() {
        _currentPosition.value = _player?.currentPosition ?: 0L
    }

    override fun onCleared() {
        super.onCleared()
        _player?.release()
        _player = null
    }
}
