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
          - analyzer_priority=0  
          # Does indexes logs. If value is not specified or incorrect than 'false by default
          - analyzer_index=false 
```