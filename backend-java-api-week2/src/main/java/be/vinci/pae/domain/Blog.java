package be.vinci.pae.domain;

public class Blog {

    private int id;
    private String title;
    private String URI;
    private String content;
    private String author;
    private boolean published;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getURI() {
        return URI;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean getPublished() {
        return published;
    }


    public String toString() {
        return "BlogImpl [id=" + id + ", title=" + title + ", content=" + content + ", author=" + author + ", uri="
                + URI + ", published=" + published +  "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Blog other = (Blog) obj;
        if (id != other.id)
            return false;
        return true;
    }



}
