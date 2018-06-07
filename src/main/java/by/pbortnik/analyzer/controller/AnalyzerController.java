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