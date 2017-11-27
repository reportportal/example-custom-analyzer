# Custom analyzer

Since version 4.0 ReportPortal uses a new version of analyzer. The implementation of analyzer was moved to the
separate service. If the default implementation does not provide a solution it can be extended by 
custom realization of analyzer. Default analyzer can work together with custom. ReportPortal has a [client](https://github.com/reportportal/service-api/blob/develop/src/main/java/com/epam/ta/reportportal/core/analyzer/client/AnalyzerServiceClient.java) communicate
with analyzers by HTTP and it has pretty simple interface. 

### Supported endpoints:

All supported endpoints that should be implemented in custom analyzer are:

[Analyze launch](https://github.com/reportportal/example-custom-analyzer/blob/master/README.md#L37-L103)
```yaml
POST
http://analyzer_host:port/_analyzer
```

[Index items](). Optional
```yaml
POST
http://analyzer_host:port/_index
```

[Delete indexed project](). Optional
```yaml
DELETE
http://analyzer_host:port/_index/{project}
```

[Clean indexed items](). Optional
```yaml
PUT
http://analyzer_host:port/_index/delete
```


### Analyze
In a realization should be implemented at least [one controller](https://github.com/reportportal/example-custom-analyzer/blob/b866fb64441cb25651e37e39411631aa2b6f46d7/src/main/java/by/pbortnik/analyzer/controller/AnalyzerController.java#L17) that consumes request from RP to analyze launch with the next endpoint:
```yaml
POST
http://analyzer_host:port/_analyzer
```
It consumes requests in the next json format:

[Implementation in Java](https://github.com/reportportal/example-custom-analyzer/blob/master/src/main/java/by/pbortnik/analyzer/model/IndexLaunch.java)

```yaml
[
  {
    "launchId": "5a0d84a8eff46f62cfd9cbe4",                   
    # Launch id in ReportPortal
    "launchName": "test-results",  
    # Launch name in ReportPortal. In default implementation issues from the launch with
    # the same launch name have a higher priority
    "project": "analyzer",                       
    # Project name. Default implementation categorises items by project  
    "testItems": [                                            
    # Test items to be analyzed
      {
        "autoAnalyzed": false,
        # Optional, used for marking items as analyzed. In default implementation it means 
        # that the item that was analyzed by human ('false') has a higher priority
        "testItemId": "5a0d84b6eff46f62cfd9cd9f",             
        # Test item id in ReportPortal       
        "issueType": "TI001",         
        # Only items with RP status 'TO_INVESTIGATE' can be analyzed        
        "logs": [
        # Test item's logs with level "ERROR" and higher          
          {
            "logLevel": 40000,
            "message": "java.lang.AssertionError: expected..."
          } 
        ],                                                    
        "uniqueId": "auto:f9377b76a3ebede22df09fa76400788b"   
        # Test Item's unique id in RP. In default implementation issue from item 
        # with the same unique id has a higher priority
      }
    ]
  }
]
```
In the current default realization it actually sends only one launch in any case. 


ReportPortal accepts the analyzed items back as a response in the next json format:

[Implementation in Java](https://github.com/reportportal/example-custom-analyzer/blob/master/src/main/java/by/pbortnik/analyzer/model/AnalyzedItemRs.java)
```yaml
[
    {
        "test_item": "5a0d84b6eff46f62cfd9cd9f",
        # Test item id in RP        
        "issue_type": "PB001",
        # Investigated issue type that will be applied to the item
        "relevant_item": "5a1be11deff46f8b838fc5e5"
        # The most relevant item id. It is used for taking a comment 
        # and an external system issue from the most relevant item in RP
    }
]
```

As a result items are updated in ReportPortal database with a new issue, comment and reference in BTS if any are found.

### Analyzer with processing previous data

If an analyzing alghorithm is based on the previous results, the analyzer interface also provides possibility to collect information about updated items in RP. To have that logic there should be implemented one more endpoint:

```yaml
POST
http://analyzer_host:port/_index
```

The request contains the same list of json objects as in "_analyze" higher except of the "issueType" sould be provided by user and it should be different from "TO_INVESTIGATE". In the current realisation RP doesn't really use the [response from index](https://github.com/reportportal/service-api/blob/master/src/main/java/com/epam/ta/reportportal/core/analyzer/model/IndexRs.java) so it could be ignored.

There are a few more optional endpoints. They are required for the default analyzer implementation. The first one is for deleting all the accumulated information about specified project: 

```yaml
DELETE
http://analyzer_host:port/_index/{project}
```
The other is for cleaning information about the specified test items from the specified project: 

```yaml
PUT
http://analyzer_host:port/_index/delete

{
  "ids": [
    "5a1be11deff46f8b838fc5e5"
  ],
  "project": "analyzer"
}
```

### Configuring service for Consul

For correct interaction with ReportPortal the analyzer must have several required tags in it's consul service configuration. 

* `analyzer=custom` 

      Marks that the current service is analyzer implementation with name 'custom'

* `analyzer_priority=0` 

      If the service is going to work with default analyzer together there should be specified the serivce's priority 
      For basic analyzer it is 10. So if the current analyzer's resutls are more important than defaults the priority 
      parameter should be lower than 10. If the value is not specified or incorrect than the priority is the lowest by default.
      
* `analyzer_index=false`

      If the service processes previous results it should have the tag 'analyzer_index=true'. Does index logs. If value is
      not specified or incorrect than 'false' by default.

[Java configuration example](https://github.com/reportportal/example-custom-analyzer/blob/master/src/main/resources/application.yaml)

All included analyzers could be founded by sending a reques to the next endpoint:

```yaml
GET
http://reportportal_host:port/composite/info
```

![composite/info](/CompositeInfo.png?raw=true)
