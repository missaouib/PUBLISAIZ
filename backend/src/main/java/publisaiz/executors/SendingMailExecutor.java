package publisaiz.executors;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import publisaiz.datasources.database.entities.MailBox;
import publisaiz.datasources.database.entities.User;
import publisaiz.tools.mailFormater.SystemMailer;

import java.io.File;
import java.util.Set;

@Component
public class SendingMailExecutor {

    private SystemMailer systemMailer;
    private JavaMailSender javaMailSender;

    public SendingMailExecutor(SystemMailer systemMailer, JavaMailSender javaMailSender) {
        this.systemMailer = systemMailer;
        this.javaMailSender = javaMailSender;
    }

    public void send(MailBox mailbox, final User from, final User toUser, final Set<File> files, final String subject, final String html, boolean isMultipart) {
        Thread sendingMail = new Thread(() -> {
            javaMailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, "UTF-8");
                message.setFrom(from.getLogin());
                message.setTo(toUser.getLogin());
                message.setSubject(subject);
                message.setText(html, true);
                for (File file : files) {
                    if (file.isFile())
                        message.addAttachment(file.getName(), file);
                }
            });
        }, "sending email from " + from.getLogin() + " to " + toUser);
        sendingMail.start();
    }

    public void sendSystemMail(final User toUser) {
        Thread sendingMail = new Thread(() -> {
            javaMailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                message.setFrom(systemMailer.getFrom());
                message.setTo(toUser.getLogin());
                message.setSubject(systemMailer.getSubject());
                message.setText(systemMailer.getHtmlContent(), true);
                for (File file : systemMailer.getFiles()) {
                    if (file.isFile())
                        message.addAttachment(file.getName(), file);
                }
            });
        }, "sending email from " + systemMailer.getFrom() + " to " + toUser.getLogin());
        sendingMail.start();
    }

    public SystemMailer getSystemMailer() {
        return systemMailer;
    }
}