// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.max_co.chime.dto.ArtistDto;
import com.github.max_co.chime.dto.SongDto;

@SpringBootTest
class DbSongServiceTest {

  @Autowired
  private DbSongService dbSongService;

  @Autowired
  private JdbcTemplate jdbc;

  @BeforeEach
  void setupDb() {
    jdbc.execute("INSERT INTO artist (name) VALUES ('Marcus Tullius Cicero'), ('Tully');");
    jdbc.execute("INSERT INTO song (title, language, text, translation, artist_id) VALUES " +
        "('TITLE', 'LA', 'TEXT', 'TRANSLATION', 1), " +
        "('Song', 'EN', 'La la la', NULL, NULL);");
  }

  @AfterEach
  void cleanupDb() {
    jdbc.execute("DELETE FROM song;");
    jdbc.execute("DELETE FROM artist;");
    jdbc.execute("ALTER TABLE song ALTER COLUMN id RESTART WITH 1");
    jdbc.execute("ALTER TABLE artist ALTER COLUMN id RESTART WITH 1");
  }

  @Test
  void testFindById() {
    assertThrows(Exception.class, () -> dbSongService.findById(0));
    SongDto dto = dbSongService.findById(1);
    assertEquals(1, dto.getId());
    assertEquals("TITLE", dto.getTitle());
    assertEquals("LA", dto.getLanguage());
    assertEquals("TEXT", dto.getText());
    assertEquals("TRANSLATION", dto.getTranslation());
    ArtistDto artist = dto.getArtist();
    assertNotNull(artist);
    assertEquals(1, artist.getId());
    assertEquals("Marcus Tullius Cicero", artist.getName());
    dto = dbSongService.findById(2);
    assertEquals(2, dto.getId());
    assertEquals("Song", dto.getTitle());
    assertEquals("EN", dto.getLanguage());
    assertEquals("La la la", dto.getText());
    assertNull(dto.getTranslation());
    assertNull(dto.getArtist());
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2 })
  void testDeleteById(int id) {
    SongDto dto = dbSongService.findById(id);
    assertEquals(id, dto.getId());
    dbSongService.deleteById(dto.getId());
    assertThrows(Exception.class, () -> dbSongService.findById(id));
  }

  @Test
  void testCreate() {
    ArtistDto artist = new ArtistDto();
    artist.setName("Foo Bar");
    SongDto song = new SongDto();
    song.setArtist(artist);
    song.setTitle("t1tl3");
    song.setLanguage("ES");
    song.setText("ta ta ta");
    song.setTranslation("nice sounds");
    song = dbSongService.save(song);
    int id = song.getId();
    assertNotNull(id);
    assertDoesNotThrow(() -> dbSongService.findById(id));
    SongDto saved = dbSongService.findById(id);
    assertEquals("t1tl3", saved.getTitle());
    assertEquals("ES", saved.getLanguage());
    assertEquals("ta ta ta", saved.getText());
    assertEquals("nice sounds", saved.getTranslation());
    ArtistDto savedArtist = saved.getArtist();
    assertNotNull(savedArtist);
    assertNotNull(savedArtist.getId());
    assertEquals("Foo Bar", savedArtist.getName());
  }
}
