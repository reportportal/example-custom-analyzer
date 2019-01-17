package by.pbortnik.analyzer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean(name = "analyzerConnectionFactory")
	public ConnectionFactory analyzerConnectionFactory(@Value("${rp.amqp.addresses}") URI addresses) {
		CachingConnectionFactory factory = new CachingConnectionFactory(addresses);
		factory.setVirtualHost("analyzer");
		return factory;
	}

	@Bean(name = "analyzerRabbitTemplate")
	public RabbitTemplate asyncRabbitTemplate(@Autowired @Qualifier("analyzerConnectionFactory") ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public AsyncRabbitTemplate asyncAmqpTemplate(@Autowired @Qualifier("analyzerRabbitTemplate") RabbitTemplate rabbitTemplate) {
		return new AsyncRabbitTemplate(rabbitTemplate);
	}

	@Bean
	public DirectExchange directExchange() {
		Map<String, Object> params = new HashMap<>();
		params.put("priority", 1);
		params.put("analyzer", "custom-rp-analyzer");
		return new DirectExchange("custom-rp-analyzer", true, false, params);
	}

	@Bean
	public Queue analyzeQueue() {
		return new Queue("custom-rp-analyzer");
	}

	@Bean
	public Binding analyzeQueueBinding() {
		return BindingBuilder.bind(analyzeQueue()).to(directExchange()).with("analyze");
	}
}
