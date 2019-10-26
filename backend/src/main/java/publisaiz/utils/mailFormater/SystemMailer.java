package publisaiz.utils.mailFormater;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import publisaiz.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
class SystemMailer implements ApplicationContextAware, EnvironmentAware {

    private final SystemMailTemplate systemMailTemplate = new SystemMailTemplate();
    private Environment environment;
    private String from;
    private String subject;
    private String htmlContent;
    private Iterable<? extends File> files;

    public SystemMailer() {

    }

    public JavaMailSenderImpl setupMailSender(JavaMailSenderImpl mailSender) {
        mailSender.setHost(environment.getProperty("system.mail.host"));
        mailSender.setPort(Integer.parseInt(environment.getProperty("system.mail.port")));
        mailSender.setProtocol(environment.getProperty("system.mail.protocol"));
        mailSender.setUsername(environment.getProperty("system.mail.username"));
        mailSender.setPassword(environment.getProperty("system.mail.password"));
        JavaMailSenderImpl mailSender1;
        return mailSender1 = mailSender;
    }

    public void welcome(User user) throws IOException {
        setSubject(environment.getProperty("system.mail.subject.welcome"));
        setFrom(environment.getProperty("system.mail.from.welcome"));
        setFiles(environment.getProperty("system.mail.files.welcome"));
        systemMailTemplate.setUser(user);
        setHtmlContent(systemMailTemplate.getTemplate("welcome"));
    }

    public void goodbye(User user) throws IOException {
        setSubject(environment.getProperty("system.mail.subject.goodbye"));
        setFrom(environment.getProperty("system.mail.from.goodbye"));
        setFiles(environment.getProperty("system.mail.files.goodbye"));
        systemMailTemplate.setUser(user);
        setHtmlContent(systemMailTemplate.getTemplate("goodbye"));
    }

    public void message(User user) throws IOException {
        setSubject(environment.getProperty("system.mail.subject.message"));
        setFrom(environment.getProperty("system.mail.from.message"));
        setFiles(environment.getProperty("system.mail.files.message"));
        systemMailTemplate.setUser(user);
        setHtmlContent(systemMailTemplate.getTemplate("message"));
    }

    public void warning(User user) throws IOException {
        setSubject(environment.getProperty("system.mail.subject.warning"));
        setFrom(environment.getProperty("system.mail.from.warning"));
        setFiles(environment.getProperty("system.mail.files.warning"));
        systemMailTemplate.setUser(user);
        setHtmlContent(systemMailTemplate.getTemplate("warning"));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    public String getFrom() {
        return from;
    }

    private void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    private void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    private void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Iterable<? extends File> getFiles() {
        return files;
    }

    private void setFiles(String files) {
        this.files = Arrays.stream(files.split(",")).map(a -> new File(a.trim())).filter(a -> a.isFile()).collect(Collectors.toList());
    }
}
