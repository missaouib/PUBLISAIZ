package publisaiz.datasources.database.entities;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class MailBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String protocol;
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean smtpAuth;
    private boolean tls;
    @ManyToOne
    private User owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = "imap" == protocol.toLowerCase().trim() ? "imap" : "smtp";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailBox)) return false;
        MailBox mailBox = (MailBox) o;
        return getId() == mailBox.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getId());
    }
}
