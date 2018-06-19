package com.zuzseb.learning.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FILE")
@NamedQueries({
        @NamedQuery(name = "File.getByPost",query = "SELECT f FROM File f WHERE f.post = :post")
})
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public File() {
    }

    public File(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public File(String fileName, byte[] fileData, User user, Post post) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File file = (File) o;
        return Objects.equals(id, file.id) &&
                Objects.equals(user, file.user) &&
                Objects.equals(post, file.post);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user, post);
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", user=" + user +
                ", post=" + post +
                '}';
    }
}
