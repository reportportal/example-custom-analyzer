### Analyzer interface

Since version 4.0 ReportPortal uses a new version of analyzer. The implementation of analyzer was moved to the
separate service. If the default implementation does not provide a solution it can be extended by 
custom realization of analyzer. Default analyzer can work together with custom. ReportPortal communicate
with analyzers by HTTP and it has pretty simple interface. 

In a realization should be implemented at least one controller with path that consumes request from RP to analyze launch:
```yaml
http://host:port/_analyzer
```
It consumes requests in the next json format:

```yaml
[
  {
    # Launch id in ReportPortal
    "launchId": "5a0d84a8eff46f62cfd9cbe4",                   
    # Launch name in ReportPortal. In default implementation issues from the launch with
    # the same launch name have a higher priority
    "launchName": "test-results",                             
    # Project name. Default implementation categorises items by project  
    "project": "analyzer",                                    
    # Test items to be analyzed
    "testItems": [                                            
      {
        # Optional, used for marking items as analyzed. In default implementation it means 
        # that the item that was analyzed by human ('false') has a higher priority
        "autoAnalyzed": false,                                 
        # Test item id in ReportPortal
        "testItemId": "5a0d84b6eff46f62cfd9cd9f",             
        # Only items with RP status 'TO_INVESTIGATE' can be analyzed
        "issueType": "TI001",                       
        # Test item's logs with level "ERROR" and higher          
        "logs": [
          {
            "logLevel": 40000,
            "message": "java.lang.AssertionError: expected..."
          } 
        ],                                                    
        # Test Item's unique id in RP. In default implementation issue from item 
        # with the same unique id has a higher priority
        "uniqueId": "auto:f9377b76a3ebede22df09fa76400788b"   
      }
    ]
  }
]
```

ReportPortal accepts the analyzed items as a response in the next json format:

```yaml
[
    {
        # Test item id in RP
        "test_item": "5a0d84b6eff46f62cfd9cd9f",
        # Analyzed issue type
        "issue_type": "PB001",
        # The most relevant item id. In default implementation it used for taking a comment 
        # and an external system issue from the most relevant item in RP
        "relevant_item": "5a1be11deff46f8b838fc5e5"
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
