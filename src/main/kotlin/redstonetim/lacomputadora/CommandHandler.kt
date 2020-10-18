package redstonetim.lacomputadora

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import kotlin.random.Random

object CommandHandler {
    val dispatcher = CommandDispatcher<Message>()

    init {
        // info
        dispatcher.register(firstLiteral("info").executes {
            val embed = EmbedBuilder()
            embed.setTitle("La Computadora")
            embed.setDescription("Information")
            embed.setAuthor(
                "RedstoneTim#5144",
                "https://github.com/RedstoneTim",
                "https://en.gravatar.com/userimage/193359831/005858dff01241a248d6151df2d40f7f.png"
            )
            embed.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/7/79/Dora_The_Explorer_%282056486897%29.jpg")
            embed.addField("Version", Main.version, false)
            embed.addField("Prefix", Main.prefix, false)
            embed.addField("Commands", getCommandList(it.source), false)
            embed.addField("Source code", "https://github.com/RedstoneTim/La-Computadora", false)
            embed.addField(
                "Avatar source",
                "https://commons.wikimedia.org/wiki/File:Dora_The_Explorer_(2056486897).jpg (by Ian Gampon, licensed under CC BY 2.0: https://creativecommons.org/licenses/by/2.0/deed.en)",
                false
            )
            sendMessage(embed.build(), it.source)
            1
        })

        // spanishname
        dispatcher.register(firstLiteral("spanishname").executes {
            sendMessage(Data.spanishNames.random(), it.source)
            1
        }.then(argument<Message, String>("name", greedyString()).executes {
            // TODO: Maybe do something more interesting
            sendMessage("El ${getString(it, "name").replace(" ", "").toLowerCase().capitalize()}os", it.source)
            1
        }))

        // markov
        dispatcher.register(firstLiteral("markov").then(argument<Message, String>("author", string()).executes {
            sendMessage(Markov.generate(getString(it, "author")), it.source)
            1
        }))

        // iq
        dispatcher.register(firstLiteral("iq").executes {
            sendMessage("Your IQ is ${Random.nextInt(150) - 50}", it.source)
            1
        })

        // adjektive
        dispatcher.register(firstLiteral("adjektiv").executes {
            sendMessage("Du bist ${Data.adjektive.random()}", it.source)
            1
        })

        // ja/nein
        dispatcher.register(firstLiteral("janein").executes {
            sendMessage(
                if (Random.nextBoolean()) Data.vielleicht.random() else if (Random.nextBoolean()) "Ja" else "Nein",
                it.source
            )
            1
        }.then(argument<Message, String>("frage", greedyString()).executes {
            sendMessage(
                if (Random.nextBoolean()) Data.vielleicht.random() else if (Random.nextBoolean()) "Ja" else "Nein",
                it.source
            )
            1
        }))

        // ja/nein
        dispatcher.register(firstLiteral("yesno").executes {
            sendMessage(
                if (Random.nextBoolean()) Data.maybe.random() else if (Random.nextBoolean()) "Yes" else "No",
                it.source
            )
            1
        }.then(argument<Message, String>("question", greedyString()).executes {
            sendMessage(
                if (Random.nextBoolean()) Data.maybe.random() else if (Random.nextBoolean()) "Yes" else "No",
                it.source
            )
            1
        }))
    }

    fun sendMessage(message: Message, context: Message) {
        context.channel.sendMessage(message).queue()
        logMessage(message)
    }

    fun sendMessage(message: String, context: Message) {
        context.channel.sendMessage(message).queue()
        logMessage(message)
    }

    fun sendMessage(message: MessageEmbed, context: Message) {
        context.channel.sendMessage(message).queue()
        logMessage(message)
    }

    private fun logMessage(message: Any) {
        Main.logger.info("Sent message: \"$message\"")
    }

    fun getCommandList(message: Message): String =
        dispatcher.getSmartUsage(dispatcher.root, message).values.joinToString("\n")

    fun sendUsage(context: CommandContext<Message>) {
        sendMessage(
            "Command usage: ${dispatcher.getSmartUsage(context.rootNode, context.source).values.firstOrNull()}",
            context.source
        )
    }

    fun literal(name: String): LiteralArgumentBuilder<Message> = LiteralArgumentBuilder.literal(name)

    fun firstLiteral(name: String): LiteralArgumentBuilder<Message> = literal(Main.prefix + name)
}