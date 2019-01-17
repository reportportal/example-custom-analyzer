package by.pbortnik.analyzer.controller;

import by.pbortnik.analyzer.model.AnalyzedItemRs;

import by.pbortnik.analyzer.model.CleanIndexRq;
import by.pbortnik.analyzer.model.IndexLaunch;
import by.pbortnik.analyzer.model.IndexRs;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyzerController {

	@Autowired
	private SimpleStorage simpleStorage;

	@RabbitListener(queues = "custom-rp-analyzer")
	public List<AnalyzedItemRs> analyze(@Payload IndexLaunch indexLaunch) {
		List<AnalyzedItemRs> response = new ArrayList<>();
		String project = indexLaunch.getProject();
		indexLaunch.getTestItems()
				.forEach(item -> item.getLogs()
						.stream()
						.filter(it -> it.getMessage().contains("TooLittleActualInvocations"))
						.findAny()
						.ifPresent(it -> {
							AnalyzedItemRs rs = new AnalyzedItemRs();
							rs.setItemId(item.getTestItemId());
							rs.setIssueType("AB001");
							rs.setUniqueId(item.getUniqueId());
							response.add(rs);
						}));
		return response;
	}

	public IndexRs index(@RequestBody List<IndexLaunch> launches) {
		simpleStorage.addAll(launches);
		return null;
	}

	public void deleteIndex(@PathVariable String project) {
		simpleStorage.removeProject(project);
	}

	public void cleanIndex(@RequestBody CleanIndexRq rq) {
		simpleStorage.cleanIndex(rq.getIds(), rq.getProject());
	}

}