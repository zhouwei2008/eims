package eims

class SendService {

    static transactional = false
    def woxpSmsService
    def validateconn=true;

    def send(String target,String content,long id) {
        def item=EimsItem.findById(id)
        String response=""
        if(item!=null&&item.opersts!="Y"&&item.opercounts<=3){
            try{
                   response=woxpSmsService.SendSms(target,content);
                   if(Integer.valueOf(response) > 0){
                       response="success"
                   }
                   item.channel="WoxpSMS"
                log.info item.userid+":"+item.channel+":"+target+":"+content+":"+response

             }catch(Exception e){
                e.printStackTrace()
             }

             if("success".equals(response)){
                 item.opersts="Y"
                 item.opercounts+=1
             }
             item.closedate=new Date()
             item.save flush:true,failOnError:true
        }else{
           log.info "MessageId:"+id+ "is sended already."
        }
        return response
     }


}
