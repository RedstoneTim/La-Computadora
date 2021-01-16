package redstonetim.lacomputadora

import net.dv8tion.jda.api.JDABuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Main {
    const val version = "1.0.0"
    const val prefix = "¿"
    val logger: Logger = LoggerFactory.getLogger(Main.javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        val jda = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN")).build()
        jda.awaitReady()
        jda.addEventListener(EventHandler)
        logger.info("Started application \"La Computadora\"")
        Markov.load()
    }
}