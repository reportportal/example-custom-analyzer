package by.pbortnik.analyzer.controller;

import by.pbortnik.analyzer.model.IndexLaunch;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Controller
public class AnalyzerController {

	@RequestMapping(value = "/_analyze", method = RequestMethod.POST, consumes = APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public IndexLaunch[] analyze(@RequestBody List<IndexLaunch> launches) {
		IndexLaunch indexLaunch = launches.get(0);
		indexLaunch.getTestItems().forEach(item -> {
			if (item.getLogs().stream().anyMatch(it -> it.getMessage().contains("AssertionError"))) {
				item.setIssueType("PB001");
			}
		});
		IndexLaunch[] indexLaunches = new IndexLaunch[1];
		indexLaunches[0] = indexLaunch;
		return indexLaunches;
	}
}
