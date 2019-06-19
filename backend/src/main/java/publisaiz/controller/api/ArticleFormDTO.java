package publisaiz.controller.api;

import publisaiz.datasources.database.repository.custom.formbasedcustomrepository.CustomRepositoryForm;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;

public class ArticleFormDTO implements CustomRepositoryForm {
    public static final String WILDCARD = "%";
    public static final String AND_A_CREATED_BEFORE = " and a.created < :before ";
    public static final String AND_A_CREATED_AFTER = " and a.created > :after ";
    public static final String AND_A_LIKE_AUTHOR = " and (a.author.login like :author or a.author.name like :author or a.author.surname like :author) ";
    public static final String AND_A_LIKE_TERM = " and (a.title like :term or a.content like :term) ";

    private String before;
    private String after;
    private String author;
    private String term;

    public ArticleFormDTO() {
    }

    public ZonedDateTime getBefore() {
        if(before==null || before.length()<1)
            return null;
        return ZonedDateTime.parse(before);
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public ZonedDateTime getAfter() {
        if(after==null || after.length()<1)
            return null;
        return ZonedDateTime.parse(after);
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "ArticleFormDTO{" +
                "before=" + before +
                ", after=" + after +
                ", author='" + author + '\'' +
                ", term='" + term + '\'' +
                '}';
    }

    @Override
    public void setParameters(TypedQuery query, TypedQuery countQuery) {
        if (getBefore() != null) {
            query.setParameter("before", getBefore().toLocalDateTime());
            countQuery.setParameter("before", getBefore().toLocalDateTime());
        }
        if (getAfter() != null) {
            query.setParameter("after", getAfter().toLocalDateTime());
            countQuery.setParameter("after", getAfter().toLocalDateTime());
        }
        if (getAuthor() != null && getAuthor().length() > 0) {
            query.setParameter("author", WILDCARD + getAuthor() + WILDCARD);
            countQuery.setParameter("author", WILDCARD + getAuthor() + WILDCARD);
        }
        if (getTerm() != null && getTerm().length() > 0) {
            query.setParameter("term", WILDCARD + getTerm() + WILDCARD);
            countQuery.setParameter("term", WILDCARD + getTerm() + WILDCARD);
        }
    }

    @Override
    public String setFilters() {
        String filter = "";
        if (getBefore() != null) filter += AND_A_CREATED_BEFORE;
        if (getAfter() != null) filter += AND_A_CREATED_AFTER;
        if (getAuthor() != null && getAuthor().length() > 0) filter += AND_A_LIKE_AUTHOR;
        if (getTerm() != null && getTerm().length() > 0) filter += AND_A_LIKE_TERM;
        if (filter != "") filter = " where " + filter.strip().substring(3);
        return filter;
    }
}
