package redstonetim.lacomputadora

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Main {
    const val version = "1.0.0"
    const val prefix = "¿"
    val logger: Logger = LoggerFactory.getLogger(Main.javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        val token = System.getenv("DISCORD_TOKEN")
        if (token.isNullOrEmpty()) {
            logger.error("Couldn't start application: No token has been supplied")
        } else {
            val jda = JDABuilder.createDefault(token).build()
            jda.awaitReady()
            jda.addEventListener(EventHandler)
            logger.info("Started application \"La Computadora\"")
            Markov.load()
        }
    }
}