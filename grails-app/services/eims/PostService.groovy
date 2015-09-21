package eims
import ru.perm.kefir.asynchronousmail.AsynchronousMailService
import javax.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor
import org.springframework.mail.javamail.MimeMessageHelper
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import javax.mail.Session;

class PostService {
    static transactional = true
    def asynchronousMailService
    private JavaMailSender sender;
	def threadPool;

    def asynsend(String mailto,String mailsubject,String mailbody) {
         asynchronousMailService.sendAsynchronousMail{
            to mailto
            subject mailsubject
            //html mailbody
            body(view:"/inAccess/email_template",model:[body:mailbody])
            maxAttemptsCount 3
        }
    }
    /**
    def synsend(String mailto,String mailsubject,String mailbody){
        def pcb={message,to,subject,body->
                MimeMessageHelper helper=new MimeMessageHelper(message, true,"GBK");
                helper.setTo(to);
                //helper.addTo(to, from);
                //helper.setFrom(String.valueOf(ConfigurationHolder.config.grails.mail.default.from));
                helper.setFrom("荣程支付 <"+ConfigurationHolder.config.syn.mail.username+">")
                helper.setSubject(subject);
                helper.setText("", body);
                return message;
        }
        log.info("to:"+mailto+" subject:"+mailsubject)

        sender=new org.springframework.mail.javamail.JavaMailSenderImpl();
        sender.username=ConfigurationHolder.config.syn.mail.username
        sender.password=ConfigurationHolder.config.syn.mail.password
        sender.host=ConfigurationHolder.config.syn.mail.host
//        sender.javaMailProperties.put("mail.smtp.auth","true")


        try {
			threadPool.execute(
				new Runnable() {
					public void run() {
						try {

 //                           MyAuthenticator  myauth = new MyAuthenticator (ConfigurationHolder.config.syn.mail.username, ConfigurationHolder.config.syn.mail.password)
//
 //                          Session session = Session.getDefaultInstance(sender, myauth);
//
//							MimeMessage message = new MimeMessage(session);

							MimeMessage message = sender.createMimeMessage();							;
							sender.send(pcb(message,mailto,mailsubject,mailbody));
						}catch(Exception ex){
							ex.printStackTrace();
                            println ("syn exception changes to call asynsend:"+mailto)
                            asynsend(mailto,mailsubject,mailbody);
						}
					}
				});
		}catch(Exception ex){
			ex.printStackTrace();
            log.info ("thread exception changes to call asynsend:"+mailto)
            asynsend(mailto,mailsubject,mailbody);
		}
    }
 */
    def synsend(String mailto,String mailsubject,String mailbody){
              // 这个类主要是设置邮件
       MailSenderInfo mailInfo = new MailSenderInfo();
       mailInfo.setMailServerHost(ConfigurationHolder.config.syn.mail.host);
      // mailInfo.setMailServerPort(ConfigurationHolder.config.syn.mail.port);
       mailInfo.setValidate(true);
       mailInfo.setUserName(ConfigurationHolder.config.syn.mail.from); // 实际发送者
       mailInfo.setPassword(ConfigurationHolder.config.syn.mail.password);// 您的邮箱密码
       mailInfo.setFromAddress(ConfigurationHolder.config.syn.mail.from); // 设置发送人邮箱地址
       mailInfo.setToAddress(mailto); // 设置接受者邮箱地址
       mailInfo.setSubject(mailsubject);
       mailInfo.setContent(mailbody);
       // 这个类主要来发送邮件
       SimpleMailSender sms = new SimpleMailSender();
       try {
			threadPool.execute(
				new Runnable() {
					public void run() {
						try {
                             sms.sendHtmlMail(mailInfo); // 发送文体格式
						}catch(Exception ex){
							 ex.printStackTrace();
                        }
					}
			});
		}catch(Exception ex){
			ex.printStackTrace();
 		}

    }
}






