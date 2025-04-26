package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "news")
@Data
public class ScrapModel {
    @Id
    private String id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Source cannot be empty")
    private String source;

    @NotBlank(message = "Link cannot be empty")
    private String link;

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotBlank(message = "Title cannot be empty") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title cannot be empty") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Source cannot be empty") String getSource() {
        return source;
    }

    public void setSource(@NotBlank(message = "Source cannot be empty") String source) {
        this.source = source;
    }

    public @NotBlank(message = "Link cannot be empty") String getLink() {
        return link;
    }

    public void setLink(@NotBlank(message = "Link cannot be empty") String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
