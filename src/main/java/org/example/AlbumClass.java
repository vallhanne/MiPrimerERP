package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "Album", schema = "Chinook", catalog = "")
public class AlbumClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "AlbumId", nullable = false)
    private int albumId;
    @Basic
    @Column(name = "Title", nullable = false, length = 160)
    private String title;
    @Basic
    @Column(name = "ArtistId", nullable = false)
    private int artistId;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ArtistId", referencedColumnName = "ArtistId")
//    private ArtistClass artist;  // Referencia al objeto ArtistClass

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumClass that = (AlbumClass) o;

        if (albumId != that.albumId) return false;
        if (artistId != that.artistId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = albumId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + artistId;
        return result;
    }
}
