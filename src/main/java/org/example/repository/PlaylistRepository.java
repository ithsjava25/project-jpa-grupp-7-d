package org.example.repository;

import org.example.model.Playlist;
import org.example.model.Song;

import javax.persistence.EntityManager;
import java.util.List;

public class PlaylistRepository {

    private final EntityManager em;

    public PlaylistRepository(EntityManager em) {
        this.em = em;
    }

    public Playlist createPlaylist(String name) {
        Playlist p = new Playlist(name);
        em.persist(p);
        return p;
    }

    public List<Playlist> findAll() {
        return em.createQuery("SELECT p FROM Playlist p ORDER BY p.createdAt DESC", Playlist.class)
                    .getResultList();
    }

    public Playlist findByName(String name) {
        List<Playlist> results = em.createQuery("SELECT p FROM Playlist p WHERE p.name = :name", Playlist.class)
                .setParameter("name", name)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public Song findSongByTitle(String title) {
        List<Song> results = em.createQuery("SELECT s FROM Song s WHERE s.title = :title", Song.class)
                .setParameter("title", title)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public void addSong(String playlistName, String songTitle, int position) {
        Playlist playlist = findByName(playlistName);
        if (playlist == null) throw new IllegalArgumentException("Playlist with name '" + playlistName + "' not found.");

        Song song = findSongByTitle(songTitle);
        if (song == null) throw new IllegalArgumentException("Song with title '" + songTitle + "' not found.");

        playlist.addSong(song, position);
    }

    public void removeSong(String playlistName, String songTitle) {
        Playlist playlist = findByName(playlistName);
        if (playlist == null) throw new IllegalArgumentException("Playlist with name '" + playlistName + "' not found.");

        Song song = findSongByTitle(songTitle);
        if (song == null) throw new IllegalArgumentException("Song with title '" + songTitle + "' not found.");

        playlist.removeSongBySongId(song.getId());
    }
}
