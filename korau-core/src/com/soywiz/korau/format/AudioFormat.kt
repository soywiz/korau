@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.soywiz.korau.format

import com.soywiz.korio.stream.*
import com.soywiz.korio.util.Extra
import com.soywiz.korio.vfs.PathInfo
import com.soywiz.korio.vfs.VfsFile
import java.util.*

open class AudioFormat(vararg exts: String) {
    val extensions = exts.map { it.toLowerCase().trim() }.toSet()

    data class Info(
            var lengthInMicroseconds: Long = 0L,
            var channels: Int = 2
    ) : Extra by Extra.Mixin() {
        val msLength = lengthInMicroseconds / 1000L
        val length = lengthInMicroseconds.toDouble() / 1_000_000.0
    }

    suspend open fun tryReadInfo(data: AsyncStream): Info? = null

    suspend open fun decodeStream(data: AsyncStream): AudioStream? = null

    suspend fun decode(data: AsyncStream): AudioData? {
        val s = decodeStream(data) ?: return null
        val out = AudioBuffer()
        val buffer = ShortArray(1024)
        while (true) {
            val read = s.read(buffer, 0, buffer.size)
            if (read <= 0) break
            out.write(buffer, 0, read)
        }
        return AudioData(s.rate, s.channels, out.toShortArray())
    }

    suspend open fun encode(data: AudioData, out: AsyncOutputStream, filename: String): Unit = TODO()

    suspend fun encodeToByteArray(data: AudioData, filename: String): ByteArray {
        val out = MemorySyncStream()
        encode(data, out.toAsync(), filename)
        return out.toByteArray()
    }
}

object AudioFormats : AudioFormat() {
    val formats = ServiceLoader.load(AudioFormat::class.java).toList()

    suspend override fun tryReadInfo(data: AsyncStream): Info? {
        for (format in formats) {
            try {
                return format.tryReadInfo(data.clone()) ?: continue
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    suspend override fun decodeStream(data: AsyncStream): AudioStream? {
        for (format in formats) {
            try {
                return format.decodeStream(data.clone()) ?: continue
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    suspend override fun encode(data: AudioData, out: AsyncOutputStream, filename: String) {
        val ext = PathInfo(filename).extensionLC
        val format = formats.firstOrNull { ext in it.extensions } ?: throw UnsupportedOperationException("Don't know how to generate file for extension '$ext'")
        return format.encode(data, out, filename)
    }
}

suspend fun VfsFile.readSoundInfo() = this.openUse { AudioFormats.tryReadInfo(this) }