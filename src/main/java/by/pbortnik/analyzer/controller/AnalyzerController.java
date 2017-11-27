package by.pbortnik.analyzer.controller;

import by.pbortnik.analyzer.model.AnalyzedItemRs;
import by.pbortnik.analyzer.model.CleanIndexRq;
import by.pbortnik.analyzer.model.IndexLaunch;
import by.pbortnik.analyzer.model.IndexRs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Controller
public class AnalyzerController {

	@Autowired
	private SimpleStorage simpleStorage;

	@RequestMapping(value = "/_analyze", method = RequestMethod.POST, consumes = APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<AnalyzedItemRs> analyze(@RequestBody List<IndexLaunch> launches) {
		List<AnalyzedItemRs> response = new ArrayList<>();
		IndexLaunch indexLaunch = launches.get(0);

		indexLaunch.getTestItems().forEach(item -> {
			if (item.getLogs().stream().anyMatch(it -> it.getMessage().contains("AssertionError"))) {
				item.setIssueType("AB001");
			}
			simpleStorage.getRepository()
					.entrySet()
					.stream()
					.flatMap(it -> it.getValue().stream())
					.flatMap(it -> it.getTestItems().stream())
					.filter(it -> it.getUniqueId().equals(item.getUniqueId()))
					.findFirst()
					.ifPresent(it -> {
						AnalyzedItemRs analyzedItem = new AnalyzedItemRs();
						analyzedItem.setItemId(item.getTestItemId());
						analyzedItem.setIssueType(it.getIssueType());
						response.add(analyzedItem);
					});
		});
		return response;
	}

	@RequestMapping(value = "/_index", method = RequestMethod.POST, consumes = APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public IndexRs index(@RequestBody List<IndexLaunch> launches) {
		simpleStorage.addAll(launches);
		return null;
	}

	@RequestMapping(value = "/_index/{project}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteIndex(@PathVariable String project) {
		simpleStorage.removeProject(project);
	}

	@RequestMapping(value = "/_index/delete", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void cleanIndex(@RequestBody CleanIndexRq rq) {
		simpleStorage.cleanIndex(rq.getIds(), rq.getProject());
	}

}