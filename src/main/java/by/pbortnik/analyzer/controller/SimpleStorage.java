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

import by.pbortnik.analyzer.model.IndexLaunch;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel Bortnik
 */
@Service
public class SimpleStorage {

	private Map<String, List<IndexLaunch>> repository;

	public SimpleStorage() {
		repository = new HashMap<>();
	}

	public Map<String, List<IndexLaunch>> getRepository() {
		return repository;
	}

	public void addAll(List<IndexLaunch> launches) {
		IndexLaunch launch = launches.get(0);

		if (repository.containsKey(launch.getProject())) {
			List<IndexLaunch> indexLaunches = repository.get(launch.getProject());
			indexLaunches.forEach(indexed -> {
				if (indexed.getLaunchId().equals(launch.getLaunchId())) {
					indexed.getTestItems().addAll(launch.getTestItems());
				}
			});
		} else {
			repository.put(launch.getProject(), Lists.newArrayList(launch));
		}
	}

	public void removeProject(String project) {
		repository.remove(project);
	}

	public void cleanIndex(List<String> ids, String project) {
		//clean index implementation
	}
}
