package redstonetim.lacomputadora

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object EventHandler : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.author.isBot) {
            val message = event.message
            val rawMessage = message.contentRaw

            if (rawMessage.startsWith(Main.prefix)) {
                Main.logger.info("Received message: \"${rawMessage}\"")
                val parseResult = CommandHandler.dispatcher.parse(rawMessage, message)

                if (!parseResult.reader.canRead() && parseResult.exceptions.isEmpty()) {
                    if (
                        try {
                            CommandHandler.dispatcher.execute(parseResult) != 1
                        } catch (e: CommandSyntaxException) {
                            true
                        }
                    ) {
                        CommandHandler.sendUsage(parseResult.context.build(rawMessage))
                    }
                } else {
                    CommandHandler.sendMessage(
                        "Available commands:\n${CommandHandler.getCommandList(message)}",
                        message
                    )
                }
            }
        }
    }
}