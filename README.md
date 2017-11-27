### Analyzer interface

Since version 4.0 ReportPortal uses a new version of analyzer. The implementation of analyzer was moved to the
separate service. If the default implementation does not provide a solution it can be extended by 
custom realization of analyzer. Default analyzer can work together with custom. ReportPortal communicate
with analyzers by HTTP and it has pretty simple interface. 

In a realization should be implemented at least one controller with path that consumes request from RP to analyze launch:
```yaml
http://host:port/_analyzer
```
It consumes request in the next format:

```yaml
[
  {
    "launchId": "5a0d84a8eff46f62cfd9cbe4",                   // Launch id
    "launchName": "test-results",                             // Launch name
    "project": "analyzer",                                    // Project name
    "testItems": [                                            // Launch name
      {
        "autoAnalyzed": false,                                // Optional, used for marking items as analyzed. In default implementation it means that the item that was analyzed by human has higher priority 
        "testItemId": "5a0d84b6eff46f62cfd9cd9f",             // Test item id
        "issueType": "TI001",                                 // Only items with status TO_INVESTIGATE can be analyzed
        "logs": [
          {
            "logLevel": 40000,
            "message": "java.lang.AssertionError: expected..."
          } 
        ],                                                    // Item logs with level ERROR and higer
        "uniqueId": "auto:f9377b76a3ebede22df09fa76400788b"   // Item's unique id in RP. In default implementation issue from item with the same unique id has higer priority
      }
    ]
  }
]
```

### Custom Analyzer Example

Example of configuring your analyzer

```yaml
spring:
  cloud:
    consul:
      discovery:
        tags:
          # This service is analyzer implementation with name 'custom'
          - analyzer=custom      
          # The service's priority. if value is not specified or incorrect than priority is lowest by default
          # For basic analyzer it is 10. So if you want your analyzer's resutls to be more important than ours
          # specify this parameter lower than 10.
          - analyzer_priority=0  
          # Does index logs. If value is not specified or incorrect than 'false by default.
          - analyzer_index=false 
```
