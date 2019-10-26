package publisaiz.functionalities.scraped;

import publisaiz.entities.ScrapedResource;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


class Scraped {
    private String url;
    private Set<Scraped> connections = new HashSet<>();
    private Set<String> emails = new HashSet<>();
    private Set<String> images = new HashSet<>();
    private String content = "";
    private ZonedDateTime accessDate;

    public Scraped() {
    }

    public Scraped(String url) {
        this.url = url.length() > 200 ? "" : url;
    }

    public Scraped(ScrapedResource scrapedResource) {
        url = scrapedResource.getUrl();
        connections = scrapedResource.getConnections().stream().map(c -> new Scraped(c, false)).collect(Collectors.toSet());
        emails = scrapedResource.getEmails().stream().map(e -> e.getEmail()).collect(Collectors.toSet());
        content = scrapedResource.getContent();
    }

    private Scraped(ScrapedResource c, boolean nestedObjects) {
        url = c.getUrl();
        accessDate = c.getAccessDate();
        content = c.getContent();
    }

    public String getUrl() {
        return url;
    }

    public Set<Scraped> getConnections() {
        return connections;
    }

    public void setConnections(Set<Scraped> connections) {
        this.connections = connections;
    }

    public void addConnection(Scraped connection) {
        connections.add(connection);
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(ZonedDateTime accessDate) {
        this.accessDate = accessDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scraped scraped = (Scraped) o;
        return url.equals(scraped.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
