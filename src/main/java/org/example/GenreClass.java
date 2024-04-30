package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "Genre", schema = "Chinook", catalog = "")
public class GenreClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "GenreId", nullable = false)
    private int genreId;
    @Basic
    @Column(name = "Name", nullable = true, length = 120)
    private String name;

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
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

        GenreClass that = (GenreClass) o;

        if (genreId != that.genreId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = genreId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
