package xyz.paintingthefish.chat

import org.ini4j.Wini
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * @author To Paint The Fish Studios™
 * @version 1.0
 */
class Shared {

    enum class CLIArgStyles {
        EqualSign,
        Separate
    }

    companion object {
        const val defaultPort: Int = 1997
        fun getVersion(): String {
            return Shared::class.java.`package`.implementationVersion ?: "IDKMAN"
        }
        fun hasFlag(args: Array<String>, rawFlag: String, caseSensitive: Boolean): Boolean {
            var flag = rawFlag
            if (!caseSensitive) {
                flag = flag.lowercase(Locale.getDefault())
            }
            for (rawArg in args) {
                var arg = rawArg
                if (!caseSensitive) {
                    arg = rawArg.lowercase(Locale.getDefault())
                }

                if (arg == flag) {
                    return true
                }
            }
            return false
        }

        fun hasFlag(args: Array<String>, flag: String?): Boolean {
            for (arg in args) {
                if (arg == flag) {
                    return true
                }
            }
            return false
        }

        fun getValue(args: Array<String>, key: String?, argStyle: CLIArgStyles?): String? {
            var i = 0

            if (argStyle == CLIArgStyles.Separate) {
                for (arg in args) {
                    if (arg == key) {
                        try {
                            return args[i + 1]
                        } catch (e: ArrayIndexOutOfBoundsException) {
                            println("[ERROR] Fuck you.")
                            throw e
                        }
                    }
                    i++
                }
            } else if (argStyle == CLIArgStyles.EqualSign) {
                for (arg in args) {
                    if (arg.startsWith(key + "=")) {
                        return arg.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    }
                    i++
                }
            } else {
                System.err.println("UNRECOGNIZED CLIArgStyles member") // Should not be achievable but better safe than sorry
            }
            return null
        }

        fun getIniFromFpath(path: Path): Wini {
            val f = path.toFile()

            try {
                FileInputStream(f).use { `is` ->
                    return Wini(`is`)
                }
            } catch (e: FileNotFoundException) {
                val ini = Wini()
                try {
                    // 1. Get the parent folder path
                    val parentDir = path.parent

                    // 2. Create the missing folders if they don't exist
                    if (parentDir != null) {
                        Files.createDirectories(parentDir)
                    }

                    // 3. Create the empty file
                    Files.createFile(path)

                    // 4. Save the blank INI structure to the file
                    ini.store(f)

                    // FIX: Return the active ini object instead of an empty dummy
                    return ini
                } catch (err: IOException) {
                    println("[ERROR] Could not create empty config file: " + err.message)
                    return Wini()
                }
            } catch (e: IOException) {
                println("[ERROR] Invalid file type or read failure: " + e.message)
                return Wini()
            }
        }

        fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }

        fun getIniFromFpath(fpath: String): Wini {
            val path = Paths.get(fpath)
            val f = path.toFile()

            try {
                FileInputStream(f).use { `is` ->
                    return Wini(`is`)
                }
            } catch (e: FileNotFoundException) {
                val ini = Wini()
                try {
                    // 1. Get the parent folder path
                    val parentDir = path.parent

                    // 2. Create the missing folders if they don't exist
                    if (parentDir != null) {
                        Files.createDirectories(parentDir)
                    }

                    // 3. Create the empty file
                    Files.createFile(path)

                    // 4. Save the blank INI structure to the file
                    ini.store(f)

                    // FIX: Return the active ini object instead of an empty dummy
                    return ini
                } catch (err: IOException) {
                    return Wini()
                }
            } catch (e: IOException) {
                return Wini()
            }
        }

        fun getWiniFromStr(str: String): Wini {
            try {
                val wini = Wini()
                wini.load(StringReader(str))
                return wini
            } catch (e: Exception) {
                System.err.println("[ERROR] $e")
                return Wini()
            }
        }
    }
}
