package com.soywiz.kpspemu.hle.manager

import com.soywiz.korio.async.go
import com.soywiz.korio.async.sleep
import com.soywiz.korio.vfs.ApplicationDataVfs
import com.soywiz.korio.vfs.MemoryVfs
import com.soywiz.korio.vfs.MemoryVfsMix
import com.soywiz.korio.vfs.VfsFile
import com.soywiz.kpspemu.Emulator
import com.soywiz.kpspemu.util.dropbox.Dropbox
import com.soywiz.kpspemu.util.dropbox.DropboxVfs
import com.soywiz.kpspemu.util.io.MountableSync
import com.soywiz.kpspemu.util.io.MountableVfsSync
import com.soywiz.kpspemu.util.mkdirsSafe

class DeviceManager(val emulator: Emulator) {
    lateinit var ms: VfsFile
    val flash = MemoryVfsMix(
    )
    val dummy = MemoryVfs()

    val root = MountableVfsSync {
    }

    val mountable = root.vfs as MountableSync

    suspend fun init() {
        reset()
    }

    suspend fun reset() {
        mountable.unmountAll()
    }

    //val devicesToVfs = LinkedHashMap<String, VfsFile>()

    fun mount(name: String, vfs: VfsFile) {
        mountable.unmount(name)
        mountable.mount(name, vfs)
        //devicesToVfs[name] = vfs
    }

    private var lastStorageType = ""
    private suspend fun updatedStorage() {
        val storage = emulator.configManager.storage.get()
        println("updatedStorage: $storage")
        if (storage == lastStorageType) {
            println("Already using that storage!")
            return
        }
        lastStorageType = storage

        val base: VfsFile = when (storage) {
            "local" -> ApplicationDataVfs["ms0"]
            "dropbox" -> DropboxVfs(Dropbox(emulator.configManager.dropboxBearer.get())).root
            else -> ApplicationDataVfs["ms0"]
        }
        ms = base.jail()
        mount("fatms0:", ms)
        mount("ms0:", ms)
        mount("mscmhc0:", ms)
        mount("host0:", dummy)
        mount("flash0:", flash)
        mount("emulator:", dummy)
        mount("kemulator:", dummy)
        mount("disc0:", dummy)
        mount("umd0:", dummy)

        println("Making directories...")
        go(emulator.coroutineContext) {
            emulator.coroutineContext.sleep(10)
            base.apply { mkdirsSafe() }
            ms["PSP"].mkdirsSafe()
            ms["PSP/GAME"].mkdirsSafe()
            ms["PSP/SAVES"].mkdirsSafe()
            println("Done")
        }
        println("Continuing...")
    }

    fun setStorage(storage: String) {
        println("Using storage: $storage")
        emulator.coroutineContext.go {
            updatedStorage()
        }
    }
}