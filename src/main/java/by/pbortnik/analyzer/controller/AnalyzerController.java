package by.pbortnik.analyzer.controller;

import com.epam.ta.reportportal.core.analyzer.model.AnalyzedItemRs;

import by.pbortnik.analyzer.model.CleanIndexRq;
import by.pbortnik.analyzer.model.IndexLaunch;
import by.pbortnik.analyzer.model.IndexRs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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
					rs.setIssueType(2L);
					response.add(rs);
				}));
		String s = objectMapper.writeValueAsString(response);
		System.err.println(s);
		return response;
	}

}