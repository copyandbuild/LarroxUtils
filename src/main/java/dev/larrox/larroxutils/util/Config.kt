package dev.larrox.larroxutils.util

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class Config {
    val file: File

    @JvmField
    val config: YamlConfiguration

    init {
        val dir = File("./plugins/Larrox/")

        if (!dir.exists()) {
            dir.mkdirs()
        }

        this.file = File(dir, "config.yml")

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        try {
            config.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}