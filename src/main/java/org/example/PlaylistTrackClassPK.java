package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PlaylistTrackClassPK implements Serializable {
    @Column(name = "PlaylistId", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playlistId;
    @Column(name = "TrackId", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

        PlaylistTrackClassPK that = (PlaylistTrackClassPK) o;

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
