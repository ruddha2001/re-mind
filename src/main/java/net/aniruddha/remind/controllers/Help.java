package net.aniruddha.remind.controllers;

import discord4j.core.object.entity.Message;

public class Help {
	public void displayHelp(Message message) {
		message.getChannel().subscribe(channel -> {
			channel.createEmbed(messageSpec -> {
				messageSpec.setTitle("Re-Mind | Help Section");
				messageSpec.setDescription(
						"Hello there! Here is a list of all the commands I support. Please remember to prefix each command with `"
								+ System.getenv("prefix") + "`."
								+ "\n\n**help**\nShow a list of all available commands."
								+ "\n\n**list**\nList down all the current reminders."
								+ "\n\n**add**\nAdd a new reminder. The format for this command is `add <name> <time> <type>`."
								+ "\n\n**delete**\nDelete an exisitng reminder."
								+ "\n\nSome of the commands might need you to enter additional values, the details of which are as follows:"
								+ "\n`<name>` The name of the reminder. Could be any string of data."
								+ "\n`<time>` The time to set the reminder for. I am able to decode the time in multiple formats and even support parsing words and phrases."
								+ "\n`<type>` The type could be either `recurring` or `once`. If no value is specified, it is taken as `once`.");
				messageSpec.setFooter("Re-Mind | Don't mind to remind", "https://canvas.aniruddha.net/canvas.png");
			}).subscribe();
		});
	}
}
