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

package by.pbortnik.analyzer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Pavel Bortnik
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalyzerConfig {

	@JsonProperty(value = "minDocFreq")
	private Integer minDocFreq;

	@JsonProperty(value = "minTermFreq")
	private Integer minTermFreq;

	@JsonProperty(value = "minShouldMatch")
	private Integer minShouldMatch;

	@JsonProperty(value = "numberOfLogLines")
	private Integer numberOfLogLines;

	@JsonProperty(value = "isAutoAnalyzerEnabled")
	private Boolean isAutoAnalyzerEnabled;

	@JsonProperty(value = "analyzer_mode")
	private String analyzerMode;

	@JsonProperty(value = "indexing_running")
	private boolean indexingRunning;

	public boolean isIndexingRunning() {
		return indexingRunning;
	}

	public void setIndexingRunning(boolean indexingRunning) {
		this.indexingRunning = indexingRunning;
	}

	public Integer getMinDocFreq() {
		return minDocFreq;
	}

	public void setMinDocFreq(Integer minDocFreq) {
		this.minDocFreq = minDocFreq;
	}

	public Integer getMinTermFreq() {
		return minTermFreq;
	}

	public void setMinTermFreq(Integer minTermFreq) {
		this.minTermFreq = minTermFreq;
	}

	public Integer getMinShouldMatch() {
		return minShouldMatch;
	}

	public void setMinShouldMatch(Integer minShouldMatch) {
		this.minShouldMatch = minShouldMatch;
	}

	public Integer getNumberOfLogLines() {
		return numberOfLogLines;
	}

	public void setNumberOfLogLines(Integer numberOfLogLines) {
		this.numberOfLogLines = numberOfLogLines;
	}

	public Boolean getIsAutoAnalyzerEnabled() {
		return isAutoAnalyzerEnabled;
	}

	public void setIsAutoAnalyzerEnabled(Boolean autoAnalyzerEnabled) {
		isAutoAnalyzerEnabled = autoAnalyzerEnabled;
	}

	public String getAnalyzerMode() {
		return analyzerMode;
	}

	public void setAnalyzerMode(String analyzerMode) {
		this.analyzerMode = analyzerMode;
	}

}
