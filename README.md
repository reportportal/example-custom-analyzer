# Custom analyzer

Since the version 5.0 Report Portal uses a different communication interface between API and Analyzers. The implementation of the analyzer was moved to the
separate service. To exchange information they use RabbitMQ message broker/ If the default implementation does not provide a solution it can be extended by a
custom implementation of an analyzer. The default analyzer can work together with a custom one. ReportPortal has a [client](https://github.com/reportportal/service-api/blob/ps-migrations/src/main/java/com/epam/ta/reportportal/core/analyzer/client/AnalyzerServiceClient.java) that communicates
with analyzers by RabbitMQ and it has a pretty simple interface. 

### Configuring service for RabbitMQ

For correct interaction with Report Portal the analyzer must have several required things in it's RabbitMQ configuration. 
At first, analyzer has to create it's own exchange in 'analyzer' virtual host in RabbitMQ. It needs to have four queues with according bindings.

[Analyze launch](#analyze)
```yaml
Bind a new queue to the created exchange in 'analyzer' host with 'analyze' route.
```

[Index items](#analyzer-with-processing-previous-data). Optional
```yaml
Bind a new queue to the created exchange in 'analyzer' host with 'index' route.
```

[Delete indexed project](#analyzer-with-processing-previous-data). Optional
```yaml
Bind a new queue to the created exchange in 'analyzer' host with 'delete' route.
```

[Clean indexed items](#analyzer-with-processing-previous-data). Optional
```yaml
Bind a new queue to the created exchange in 'analyzer' host with 'clean' route.
```

Also, a created exchange can have the next optional arguments:

* `analyzer=custom` 

      Marks that the current service is analyzer implementation with name 'custom'

* `analyzer_priority=0` 

      If the service is going to work together with default analyzer there should be specified the serivce's priority. 
      For basic analyzer it is 10. So if the current analyzer's resutls are more important than default's the priority 
      parameter should be lower than 10. If the value is not specified or incorrect than the priority is the 
      lowest by default.
      
* `analyzer_index=true`

      If the service processes previous results it should have the tag 'analyzer_index=true'. It does indexing of logs.
      If value is not specified or incorrect than 'false' by default.

All included analyzers could be found by sending a request to the next endpoint:

### Implemntation example

This repository is an example of all endpoints' implementation using Java
