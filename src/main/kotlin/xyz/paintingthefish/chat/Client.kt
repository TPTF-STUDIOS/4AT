package xyz.paintingthefish.chat

import org.ini4j.Wini
import java.awt.BorderLayout
import java.awt.Font
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JLabel
import kotlin.math.roundToInt

// IO and configuration stuff

// Graphics
/**
 * 
 * @author To Paint The Fish Studios™
 * @see Server for server software
 * 
 * @see xyz.paintingthefish.chat.ServerClass
 */
object Client {
    var main_cfg: Wini? = null
    var window: JFrame? = null

    fun initWindow() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        window = JFrame("ЧATV${Shared.getVersion()}")
        val icon: BufferedImage?
        try {
            val imgStream = Client::class.java.getResourceAsStream("/xyz/paintingthefish/chat/images/icon.png")
            if (imgStream == null) {
                System.err.println("[ERROR] COULD NOT LOAD `icon.png`")
            } else {
                icon = ImageIO.read(imgStream)
                window!!.iconImage = icon
            }
        } catch (e: IOException) {
            println("[ERROR] could not load images/icon.png :I using default Swing icon.")
        }
        window!!.name = "ЧAT"
        window!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window!!.setSize((screenSize.width / 2.0).roundToInt(), (screenSize.height / 2.0).roundToInt())
        window!!.layout = BorderLayout()
        val loadingTexts = arrayOf<JLabel>(JLabel("ЧATV${Shared.getVersion()}™"), JLabel("Loading"))
        loadingTexts[0].setFont(Font("Arial", Font.BOLD, 40))
        loadingTexts[1].setFont(Font("Arial", Font.ITALIC, 40))
        window!!.add(loadingTexts[0], BorderLayout.CENTER)
        window!!.add(loadingTexts[1], BorderLayout.WEST)
        window!!.isVisible = true
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        System.out.printf("ЧATV${Shared.getVersion()}\nproduct of To Paint The Fish Studios™\n%s\n", os)

        if (os.contains("nux")) {
            main_cfg =
                Shared.getIniFromFpath(Paths.get(System.getProperty("user.home") + "/.ЧAT/client/config.ini"))
        }
        initWindow()

        /*if (main_cfg.get("data", "provider") == null) {
            System.out.println("[INFO] user config missing 'provider' key, initiating setup");
            System.out.println(main_cfg.get("data", "provider"));
            for (Component component : window.getComponents()) {
                window.remove(component);
            }
        }*/
    }
}
