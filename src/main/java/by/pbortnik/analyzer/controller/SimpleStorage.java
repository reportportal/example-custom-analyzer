/*
 * Copyright 2017 EPAM Systems
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/service-api
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package by.pbortnik.analyzer.controller;

import by.pbortnik.analyzer.model.AnalyzedItemRs;
import by.pbortnik.analyzer.model.IndexLaunch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Pavel Bortnik
 */
@Service
public class SimpleStorage {

	private Map<String, List<AnalyzedItemRs>> repository;

	public SimpleStorage() {
		repository = new HashMap<>();
	}

	public Map<String, List<AnalyzedItemRs>> getRepository() {
		return repository;
	}

	public void addAll(List<IndexLaunch> launches) {
		IndexLaunch launch = launches.get(0);
		String project = launch.getProject();
		if (!repository.containsKey(project)) {
			repository.put(project, new ArrayList<>());
		}

		List<AnalyzedItemRs> items = repository.get(project);

		repository.putAll(launch.getTestItems().stream().collect(Collectors.toMap(it -> project, it -> {
			AnalyzedItemRs analyzedItemRs = new AnalyzedItemRs();
			analyzedItemRs.setItemId(it.getTestItemId());
			analyzedItemRs.setIssueType(it.getIssueType());
			analyzedItemRs.setUniqueId(it.getUniqueId());
			items.add(analyzedItemRs);
			return items;
		})));
	}

	public void removeProject(String project) {
		repository.remove(project);
	}

	public void cleanIndex(List<String> ids, String project) {
		List<AnalyzedItemRs> newValue = repository.get(project).stream().filter(it -> !ids.contains(it.getItemId())).collect(toList());
		repository.put(project, newValue);
	}
}
