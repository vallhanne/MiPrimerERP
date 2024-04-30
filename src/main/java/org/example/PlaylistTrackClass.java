package org.example;

import java.io.Serializable;

import jakarta.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "PlaylistTrack", schema = "Chinook", catalog = "")
@IdClass(PlaylistTrackClassPK.class)
public class PlaylistTrackClass implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PlaylistId", nullable = false)
    private int playlistId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "TrackId", nullable = false)
    private int trackId;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistTrackClass that = (PlaylistTrackClass) o;

        if (playlistId != that.playlistId) return false;
        if (trackId != that.trackId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playlistId;
        result = 31 * result + trackId;
        return result;
    }
}
