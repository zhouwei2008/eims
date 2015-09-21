package eims

import java.sql.Timestamp
class EmisJob {
      static triggers = {
       simple name: 'SMSTrigger', startDelay:1000, repeatInterval:3*60*1000
    }
    def sendService
    def execute() {
         EimsItem.createCriteria().list {
            ne("opersts","Y")
            le("opercounts",3)
            ge("createdate",new Timestamp(System.currentTimeMillis()-360000))
            le("createdate",new Timestamp(System.currentTimeMillis()-120000))
            maxResults(50)
        }.each {
            sendService.send(it.target,it.content,it.id)
        }

    }
}
