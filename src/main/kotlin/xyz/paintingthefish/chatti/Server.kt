package xyz.paintingthefish.chatti

import org.ini4j.Wini
import java.io.File
import java.util.*
import org.sqlite.*;
import java.sql.Connection
import kotlin.system.exitProcess

/**
 * 
 * @author To Paint The Fish Studios™
 * @version 0.0.0
 * @see Client
 */
object Server {
    private val server_instance: ChattiServer? = null

    @JvmStatic
    fun main(args: Array<String>) {
        val input = Scanner(System.`in`)
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())

        if (!os.contains("nux")) {
            System.err.println("[ERROR] CHATTi Server software only runs on linux");
            exitProcess(1)
        }


        if ((!File(System.getProperty("user.home") + "/.chatti/server/config.ini").exists()) || Shared.hasFlag(
                args,
                "--setup"
            )
        ) {
            val cfg: Wini =
                Shared.getIniFromFpath(System.getProperty("user.home") + "/.chatti/server/config.ini")
            println("Welcome to the CHATTi provider setup wizard!")
            var defaultValue: Any = Objects.requireNonNullElse<String>(cfg.get("info", "name"), "CHATTiProvider")
            System.out.printf(
                "What would you like the provider to be named? (may be changed by clients you authorize)? (%s)\n>> ",
                defaultValue
            )
            cfg.put(
                "info",
                "name",
                if (Shared.isNullOrEmpty(input.nextLine())) defaultValue else input.nextLine()
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
                "db",
                if (Shared.isNullOrEmpty(input.nextLine())) defaultValue else input.nextLine()
            )
            // "jdbc:sqlite:${cfg.get("info", "db")}"
            val server = ChattiServer(cfg, 130308); // closest approx. messages to fit in 256MiB
        }
        val cfg: Wini =
            Shared.getIniFromFpath(System.getProperty("user.home") + "/.chatti/server/config.ini")
    }
}
