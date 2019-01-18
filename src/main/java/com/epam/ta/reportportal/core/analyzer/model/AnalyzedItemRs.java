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

package com.epam.ta.reportportal.core.analyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Pavel Bortnik
 */
public class AnalyzedItemRs implements Comparable<AnalyzedItemRs> {

	@JsonProperty("test_item")
	private Long itemId;

	@JsonProperty("relevant_item")
	private Long relevantItemId;

	@JsonProperty("issue_type")
	private Long issueType;

	@Override
	public int compareTo(AnalyzedItemRs o) {
		return itemId.compareTo(o.getItemId());
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getRelevantItemId() {
		return relevantItemId;
	}

	public void setRelevantItemId(Long relevantItemId) {
		this.relevantItemId = relevantItemId;
	}

	public Long getIssueType() {
		return issueType;
	}

	public void setIssueType(Long issueType) {
		this.issueType = issueType;
	}
}
