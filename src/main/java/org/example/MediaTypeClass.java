package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "MediaType", schema = "Chinook", catalog = "")
public class MediaTypeClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MediaTypeId", nullable = false)
    private int mediaTypeId;
    @Basic
    @Column(name = "Name", nullable = true, length = 120)
    private String name;

    public int getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(int mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaTypeClass that = (MediaTypeClass) o;

        if (mediaTypeId != that.mediaTypeId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mediaTypeId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
