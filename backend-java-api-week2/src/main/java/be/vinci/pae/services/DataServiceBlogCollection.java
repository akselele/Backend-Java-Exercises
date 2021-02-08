package be.vinci.pae.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.Blog;

public class DataServiceBlogCollection {
    private static final String DB_FILE_PATH = "db.json";
    private static final String COLLECTION_NAME = "blogs";
    private final static ObjectMapper jsonMapper = new ObjectMapper();

    private static List<Blog> blogs ;
    static {
        blogs = loadDataFromFile();
    }

    public static Blog getBlog(int id) {
        return blogs.stream().filter(item -> item.getId() == id).findAny().orElse(null);
    }

    public static List<Blog> getBlogs() {
        return blogs;
    }

    public static Blog addBlog(Blog blog) {
        blog.setId(nexFilmtId());
        // escape dangerous chars to protect against XSS attacks
        blog.setTitle(StringEscapeUtils.escapeHtml4(blog.getTitle()));
        blog.setURI(StringEscapeUtils.escapeHtml4(blog.getURI()));
        blog.setContent(StringEscapeUtils.escapeHtml4(blog.getContent()));
        blog.setAuthor(StringEscapeUtils.escapeHtml4(blog.getAuthor()));
        if(blog.getPublished() != true || blog.getPublished() != false) {
            blog.setPublished(false);
        }
        blog.setPublished(blog.getPublished());
        blogs.add(blog);
        saveDataToFile();
        return blog;
    }

    public static int nexFilmtId() {
        if (blogs.size() == 0)
            return 1;
        return blogs.get(blogs.size() - 1).getId() + 1;
    }

    public static Blog deleteBlog(int id) {
        if (blogs.size() == 0 | id == 0)
            return null;
        Blog filmToDelete = getBlog(id);
        if (filmToDelete == null)
            return null;
        int index = blogs.indexOf(filmToDelete);
        blogs.remove(index);
        saveDataToFile();
        return filmToDelete;
    }

    public static Blog updateBlog(Blog blog) {
        if (blogs.size() == 0 | blog == null)
            return null;
        Blog filmToUpdate = getBlog(blog.getId());
        if (filmToUpdate == null)
            return null;
        // escape dangerous chars to protect against XSS attacks
        blog.setTitle(StringEscapeUtils.escapeHtml4(blog.getTitle()));
        blog.setURI(StringEscapeUtils.escapeHtml4(blog.getURI()));
        blog.setContent(StringEscapeUtils.escapeHtml4(blog.getContent()));
        blog.setAuthor(StringEscapeUtils.escapeHtml4(blog.getAuthor()));
        blog.setPublished(blog.getPublished());
        // update the data structure
        int index = blogs.indexOf(filmToUpdate);
        blogs.set(index, blog);
        saveDataToFile();
        return blog;
    }

    private static List<Blog> loadDataFromFile() {
        try {
            JsonNode node = jsonMapper.readTree(Paths.get(DB_FILE_PATH).toFile());
            JsonNode collection = node.get(COLLECTION_NAME);
            if (collection == null)
                return new ArrayList<Blog>();
            return jsonMapper.readerForListOf(Blog.class).readValue(node.get(COLLECTION_NAME));

        } catch (FileNotFoundException e) {
            return new ArrayList<Blog>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Blog>();
        }
    }

    private static void saveDataToFile() {
        try {

            // get all collections
            Path pathToDb = Paths.get(DB_FILE_PATH);
            if (!Files.exists(pathToDb)) {
                // write a new collection to the db file
                ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(COLLECTION_NAME, blogs);
                jsonMapper.writeValue(pathToDb.toFile(), newCollection);
                return;

            }
            // get all collections
            JsonNode allCollections = jsonMapper.readTree(pathToDb.toFile());

            if (allCollections.has(COLLECTION_NAME)) {// remove current collection
                ((ObjectNode) allCollections).remove(COLLECTION_NAME);
            }

            // create a new JsonNode and add it to allCollections
            String currentCollectionAsString = jsonMapper.writeValueAsString(blogs);
            JsonNode updatedCollection = jsonMapper.readTree(currentCollectionAsString);
            ((ObjectNode) allCollections).putPOJO(COLLECTION_NAME, updatedCollection);

            // write to the db file
            jsonMapper.writeValue(pathToDb.toFile(), allCollections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
