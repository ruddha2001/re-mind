package net.aniruddha.remind;

import java.util.Optional;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import net.aniruddha.remind.controllers.Help;

public class Bot {
	public static void main(String[] args) {
		try {

			Help help = new Help();

			GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("token")).build().login().block();

			client.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
				final User self = event.getSelf();
				System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
			});

			client.getEventDispatcher().on(MessageCreateEvent.class).map(event -> {
				final Message message = event.getMessage();
				Optional<User> user = message.getAuthor();
				String command[] = message.getContent().split(" ");

				user.ifPresent(author -> {
					if (!author.isBot() && command[0].toString().equals(System.getenv("prefix"))) {
						if (command.length < 2)
							defaultWrongCommandMessage(message);
						else if (System.getenv("maintenance").equals("enabled"))
							maintenanceModeMessage(message);
						else {
							if (command[1].toString().equals("help"))
								help.displayHelp(message);
							else
								defaultWrongCommandMessage(message);
						}
					}
				});

				return message;
			}).subscribe();

			client.onDisconnect().block();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void defaultWrongCommandMessage(Message message) {
		message.getChannel().subscribe(channel -> {
			channel.createMessage("I am sorry, but I could not understand what you wanted me to do. Please run `"
					+ System.getenv("prefix") + " help` to see a list of all valid commands.").subscribe();
		});
	}

	public static void maintenanceModeMessage(Message message) {
		message.getChannel().subscribe(channel -> {
			channel.createMessage(
					"I am so sorry to disappoint you, but I am currently undergoing maintenance. Please re-try after sometime. You can visit https://remind.aniruddha.net/status to know more.")
					.subscribe();
		});
	}
}
