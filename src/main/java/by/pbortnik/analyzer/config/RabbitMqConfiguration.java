package by.pbortnik.analyzer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@EnableRabbit
@Configuration
public class RabbitMqConfiguration {

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean(name = "analyzerConnectionFactory")
	public ConnectionFactory analyzerConnectionFactory(@Value("${rp.amqp.addresses}") URI addresses) {
		CachingConnectionFactory factory = new CachingConnectionFactory(addresses);
		factory.setVirtualHost("analyzer");
		return factory;
	}

	@Bean
	public DirectExchange directExchange() {
		Map<String, Object> params = new HashMap<>();
		params.put("analyzer_priority", 1);
		params.put("analyzer", "custom-rp-analyzer");
		params.put("analyzer_index", true);
		return new DirectExchange("custom-rp-analyzer", true, true, params);
	}

	@Bean
	public Queue analyzeQueue() {
		return QueueBuilder.durable("custom-rp-analyzer").build();
	}

	@Bean
	public Binding analyzeQueueBinding() {
		return BindingBuilder.bind(analyzeQueue()).to(directExchange()).with("analyze");
	}

	@Bean
	public Queue indexQueue() {
		return QueueBuilder.durable("custom-rp-index").build();
	}

	@Bean
	public Binding indexQueueBinding() {
		return BindingBuilder.bind(indexQueue()).to(directExchange()).with("index");
	}

	@Bean
	public Queue deleteQueue() {
		return QueueBuilder.durable("custom-rp-delete").build();
	}

	@Bean
	public Binding deleteQueueBinding() {
		return BindingBuilder.bind(deleteQueue()).to(directExchange()).with("delete");
	}
}
