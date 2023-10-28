package dev.rachamon.rachamoncore.api.locale;

import java.util.function.Function;

public class Locale<T> {
	private final String prefix;
	private final T config;

	public Locale(T config, Function<T, String> consumer) {
		this.config = config;
		this.prefix = consumer.apply(this.config);
	}

	public Message from(Function<T, String> consumer) {
		return new Message(prefix + consumer.apply(this.config));
	}
	public Message raw(Function<T, String> consumer) {
		return new Message(consumer.apply(this.config));
	}

	public static Message fromText(String message) {
		return new Message(message);
	}

	public T get() {
		return config;
	}
}
