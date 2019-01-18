package com.soywiz.korau.format.ogg

import com.soywiz.klock.*
import com.soywiz.korau.sound.*
import com.soywiz.korio.async.*
import com.soywiz.korio.file.std.*
import kotlinx.coroutines.*
import org.junit.*

class PlaySoundJvmTest {
    @Test
    @Ignore
    fun test() = suspendTest {
        coroutineScope {
            val sound = resourcesVfs["ogg1.ogg"].readNativeSound()
            //val sound = resourcesVfs["wav1.wav"].readNativeSound()
            launchAsap {
                sound.playAndWait()
            }
            launchAsap {
                delay(100.milliseconds)
                sound.playAndWait()
            }
            launchAsap {
                delay(200.milliseconds)
                sound.playAndWait()
            }
            launchAsap {
                delay(300.milliseconds)
                sound.playAndWait()
            }
            launchAsap {
                delay(400.milliseconds)
                sound.playAndWait()
            }
            launchAsap {
                delay(500.milliseconds)
                val channel = sound.play()
                channel.pitch = 0.2
                channel.await()
            }
        }
    }
}
