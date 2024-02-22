package fr.group5.magellangpt.common.helpers.implementations

import android.content.Context
import android.media.MediaPlayer
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.MediaPlayerHelper
import org.koin.java.KoinJavaComponent.get

class MediaPlayerHelperImpl(
    private val context : Context = get(Context::class.java)
) : MediaPlayerHelper {

    override fun playSound(soundId: Int) {
        val mMediaPlayer = MediaPlayer.create(context, soundId)
        mMediaPlayer.start()
    }

}