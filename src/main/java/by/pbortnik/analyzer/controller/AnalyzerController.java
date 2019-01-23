package by.pbortnik.analyzer.controller;

import by.pbortnik.analyzer.model.AnalyzedItemRs;

import by.pbortnik.analyzer.model.IndexLaunch;
import by.pbortnik.analyzer.model.IndexRs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyzerController {

	@Autowired
	private ObjectMapper objectMapper;

	@RabbitListener(queues = "custom-rp-analyzer")
	public List<AnalyzedItemRs> analyze(@Payload IndexLaunch indexLaunch) throws JsonProcessingException {
		List<AnalyzedItemRs> response = new ArrayList<>();
		Long project = indexLaunch.getProject();
		indexLaunch.getTestItems()
				.forEach(item -> item.getLogs().stream().filter(it -> it.getMessage().contains("ERROR")).findAny().ifPresent(it -> {
					AnalyzedItemRs rs = new AnalyzedItemRs();
					rs.setItemId(item.getTestItemId());
					rs.setLocator("ab001");
					response.add(rs);
				}));
		String s = objectMapper.writeValueAsString(response);
		System.err.println(s);
		return response;
	}

	@RabbitListener(queues = "custom-rp-index")
	public IndexRs index(@Payload IndexLaunch indexLaunch) {
		System.err.println("WORKING EPTA");
		return null;
	}

	@RabbitListener(queues = "custom-rp-delete")
	public void delete(@Payload Long index) {
		System.err.println("WORKING EPTA DELETING " + index);
	}

}