package xyz.paintingthefish.chatti

import org.ini4j.Wini
import java.io.File
import java.util.*
import kotlin.system.exitProcess

/**
 * 
 * @author To Paint The Fish Studios™
 * @version 0.0.0
 * @see Client
 */
object ServerWrapper {
    private val server_instance: ChattiServer? = null
    private const val dataBaseFormatting: String = "jdbc:sqlite:%s"

    @JvmStatic
    fun main(args: Array<String>) {
        val input = Scanner(System.`in`)
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())

        if (!os.contains("nux")) {
            exitProcess(1)
        }


        if ((!File(System.getProperty("user.home") + "/.chatti/server/config.ini").exists()) || Shared.Companion.hasFlag(
                args,
                "--setup"
            )
        ) {
            val cfg: Wini =
                Shared.Companion.getIniFromFpath(System.getProperty("user.home") + "/.chatti/server/config.ini")
            var defaultValue: Any = Objects.requireNonNullElse<String>(cfg.get("info", "name"), "CHATTiProvider")
            System.out.printf(
                "What would you like the provider to be named? (may be changed by clients you authorize)? (%s)\n>> ",
                defaultValue
            )
            cfg.put(
                "info",
                "name",
                if (Shared.Companion.isNullOrEmpty(input.nextLine())) defaultValue else input.nextLine()
            )
            defaultValue = Objects.requireNonNullElse<String>(
                cfg.get("info", "name"),
                System.getProperty("user.home") + "/.chatti/server/conf.db"
            )
            System.out.printf(
                "Where would you like to put the SQLite database containing info? (%s)\n>> ",
                Objects.requireNonNullElse<String?>(
                    cfg.get("info", "port"),
                    System.getProperty("user.home") + "/.chatti/server/conf.db"
                )
            )
            cfg.put(
                "info",
                "port",
                if (Shared.Companion.isNullOrEmpty(input.nextLine())) defaultValue else input.nextLine()
            )
        }
        val cfg: Wini =
            Shared.Companion.getIniFromFpath(System.getProperty("user.home") + "/.chatti/server/config.ini")
    }
}
