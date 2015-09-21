package eims

import grails.converters.JSON

class InAccessController {

    def sendService
    def postService
    def sms = {
        log.info "smsRequest Map:"+params

        def target = params.target
        def content = params.content
        def userid = params.userid
        def mode = params.mode
        def charset=params?.charset
        if (!target) {
            render([rescode: "01", resmg: "参数target空错误"] as JSON)
            return
        }
        if(!content){
            render([rescode: "01", resmg: "参数content空错误"] as JSON)
            return
        }
        if(!userid){
            render([rescode: "01", resmg: "参数userid空错误"] as JSON)
            return
        }
        //save messeage return
        String response = ""
        if (!mode || mode == "0") {//userid+userpwd mode
            def userpwd = params.userpwd
            if(!userpwd){
                render([rescode: "01", resmg: "参数userpwd空错误"] as JSON)
                return
            }
            def user = EimsUser.findByUseridAndUserpwd(userid, userpwd);
            if (user == null || user.usersts != 'normal') {
                if (EimsUser.count() > 0) {
                    log.debug "user sts1:"+user?.usersts
                    render([rescode: "02", resmg: "用户未授权"] as JSON)
                    return
                }
            }
            long msgid = 0;
            target.split(";").each {
                if (it ==~ /^[1][3|4|5|6|8][0-9]\d{8}$/) {
                    def final item = new EimsItem()
                    item.channelType = "SMS";
                    item.channel = "UNCETAIN"
                    item.target = it
                    item.content = charset?URLDecoder.decode(content,charset):content
                    item.userid = !userid ? "0" : userid
                    item.createdate = new Date();
                    item.closedate = new Date();
                    item.opersts = "N"
                    item.opercounts = 0
                    item.save(flush: true, failOnError: true);
                    response = sendService.send(it, charset?URLDecoder.decode(content,charset):content, item.id);
                    msgid = item.id;
                } else {
                    response = "无效手机号${it}"
                }
            }
            def code = "03"
            if ("success" == response) {
                code = "00"
            }
            render([rescode: code, resmsg: response, messageid: msgid] as JSON)
        }
        else{//sign mode

            def sign = params.sign

            charset=params?.charset==null?"utf-8":params.charset
            if(!sign){
                render([rescode: "01", resmg: "参数sign空错误"] as JSON)
                return
            }
            def user = EimsUser.findByUserid(userid);
            if (user == null || user.usersts != 'normal') {
                int i= EimsUser.count() ;
                log.info "user count:"+i
                if (i> 0) {
                    log.debug "user sts1:"+user?.usersts
                    render([rescode: "02", resmg: "用户未授权"] as JSON)
                    return
                }
            }
            log.info target + (charset?URLDecoder.decode(content,charset):content) + userid + user?.getUserpwd()
            if (sign?.equals(Md5Encrypt.md5(target + (charset?URLDecoder.decode(content,charset):content) + userid + user?.getUserpwd(),charset))) {
                long msgid = 0;
                target.split(";").each {
                    if (it ==~ /^[1][3|4|5|6|8][0-9]\d{8}$/) {
                        def final item = new EimsItem()
                        item.channelType = "SMS";
                        item.channel = "UNCETAIN"
                        item.target = it
                        item.content = charset?URLDecoder.decode(content,charset):content
                        item.userid = userid ? "0" : userid
                        item.createdate = new Date();
                        item.closedate = new Date();
                        item.opersts = "N"
                        item.opercounts = 0
                        item.save(flush: true, failOnError: true);
                        response = sendService.send(it, charset?URLDecoder.decode(content,charset):content, item.id);
                        msgid = item.id;
                    } else {
                        response = "无效手机号${it}"
                    }
                }
                def code = "03"
                if ("success" == response) {
                    code = "00"
                }
                render([rescode: code, resmsg: response, messageid: msgid] as JSON)
            } else {
                render([rescode: "03", resmg: "签名验证错误"] as JSON)
                return
            }

        }

    }

    def email = {
        log.info "emailRequest Map:"+params
        def charset=params?.charset
        if (!params?.to || !params?.body) {
            log.info(params?.to +" "+params?.body);
            render([rescode: "01", resmg: "参数空错误"] as JSON)
            return
        }
        if (!params?.to ==~ /^\w+([-+_.]\w+)*@\w+([-._]\w+)*\.\w+([-._]\w+)*$/) {
            render([rescode: "02", resmsg: "无效邮箱:${params.to}"] as JSON)
            return
        }
        if (!params?.subject) {
            params.subject = "From:" + params?.to
        }
        try {
            if (params?.mode == 'asyn')
                postService.asynsend(params.to, charset?URLDecoder.decode(params.subject,charset):params.subject,
                        charset?URLDecoder.decode(params.body,charset):params.body)
            else
                postService.synsend(params.to, charset?URLDecoder.decode(params.subject,charset):params.subject,
                        charset?URLDecoder.decode(params.body,charset):params.body)
        } catch (Exception ex) {
            ex.printStackTrace();
            render([rescode: "01", resmsg: response, message: ex.message] as JSON)
            return;
        }
        render([rescode: "00", resmsg: "success"] as JSON)

    }

}
