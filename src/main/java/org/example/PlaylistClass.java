package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "Playlist", schema = "Chinook", catalog = "")
public class PlaylistClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PlaylistId", nullable = false)
    private int playlistId;
    @Basic
    @Column(name = "Name", nullable = true, length = 120)
    private String name;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
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

        PlaylistClass that = (PlaylistClass) o;

        if (playlistId != that.playlistId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playlistId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
