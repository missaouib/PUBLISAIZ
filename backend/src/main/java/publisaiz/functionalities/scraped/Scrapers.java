package publisaiz.functionalities.scraped;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Scrapers {

    private static final int MAX_UPPERCASES_IN_SENTENCE = 3;
    private static final int TOLERANCE_FOR_UPPERCASES_OVER_SENTENCE_COMPLEXITY = 2;
    private static final int MINIMUM_WORDS_NUMBER_IN_SENTENCE = 5;
    private final Logger logger = LoggerFactory.getLogger(Scrapers.class);

    public Scrapers() {
    }

    void scrapeEmails(Scraped scraped, Document document) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = pattern.matcher(document.text());
        while (matcher.find()) {
            scraped.getEmails().add(matcher.group());
        }
    }

    void scrapeContent(Scraped scraped, Document document) {
        String input = document.text();
        scraped.setContent(
                List.of(input.split("\\.")).stream()
                        .filter(s -> uppercasesPolicyValidation(s))
                        .filter(s -> minimumWordsNuberInSentence(s))
                        .filter(s -> s.length() > 10)
                        .map(s -> s.trim())
                        .filter(s -> s.charAt(0) == Character.toUpperCase(s.charAt(0)))
                        .peek(c -> logger.trace(c))
                        .collect(Collectors.joining(". "))
        );
    }

    private boolean minimumWordsNuberInSentence(String s) {
        return s.trim().split(" ").length > MINIMUM_WORDS_NUMBER_IN_SENTENCE;
    }

    private boolean uppercasesPolicyValidation(String s) {
        return countUppercase(s) < MAX_UPPERCASES_IN_SENTENCE || countLogicalSubsentencesMarkers(s) + TOLERANCE_FOR_UPPERCASES_OVER_SENTENCE_COMPLEXITY > countUppercase(s);
    }

    void scrapeLinks(Scraped scraped, Document document, URL url) {
        Elements links = document.select("a[href]");
        for (Element link : links) {
            final String address = link.attr("href");
            String relativeAddress = null;
            try {
                addAbsoluteLinks(scraped, address);
            } catch (IOException e) {
                addRelativeLinks(scraped, url, address, relativeAddress);
            }
        }
    }

    private void addAbsoluteLinks(Scraped scraped, String address) throws IOException {
        new URL(address).getContent();
        scraped.getConnections().add(new Scraped(address));
    }

    private void addRelativeLinks(Scraped scraped, URL url, String address, String relativeAddress) {
        // VerbalExpression vex = VerbalExpression.regex().startOfLine().withAnyCase().range("a", "z").withAnyCase().build();

        String relative = null;
        try {
            relative = url.getProtocol() + "://" + url.getHost().concat("/" + address);
            relativeAddress = new URL(relative).toString();
            url.getContent();
            scraped.getConnections().add(new Scraped(relativeAddress));
        } catch (MalformedURLException e1) {
            logger.trace("could not resolve [{}]", relative);
        } catch (IOException e1) {
            logger.trace("could not validate [{}]", relativeAddress);
        }
    }

    private int countUppercase(String str) {
        char ch;
        int capitalFlag = 0;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) capitalFlag++;
        }
        return capitalFlag;
    }

    private int countLogicalSubsentencesMarkers(String str) {
        char ch;
        int semicolons = 0;
        for (String s : str.split(" ")) {
            if (s.contains("and") || s.contains("or") || s.contains("if"))
                semicolons++;
        }
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (ch == ',') semicolons++;
        }
        return semicolons;
    }
}
