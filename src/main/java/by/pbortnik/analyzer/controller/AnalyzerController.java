package by.pbortnik.analyzer.controller;

import by.pbortnik.analyzer.model.AnalyzedItemRs;
import by.pbortnik.analyzer.model.IndexLaunch;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Controller
public class AnalyzerController {

	@RequestMapping(value = "/_analyze", method = RequestMethod.POST, consumes = APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<AnalyzedItemRs> analyze(@RequestBody List<IndexLaunch> launches) {
		List<AnalyzedItemRs> response = new ArrayList<>();
		IndexLaunch indexLaunch = launches.get(0);
		indexLaunch.getTestItems().forEach(item -> {
			if (item.getLogs().stream().anyMatch(it -> it.getMessage().contains("AssertionError"))) {
				item.setIssueType("PB001");
				AnalyzedItemRs analyzedItem = new AnalyzedItemRs();
				analyzedItem.setItemId(item.getTestItemId());
				analyzedItem.setIssueType("PB001");
				response.add(analyzedItem);
			}
		});
		return response;
	}
}
