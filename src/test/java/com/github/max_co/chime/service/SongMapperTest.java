// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.max_co.chime.dto.ArtistDto;
import com.github.max_co.chime.dto.SongDto;
import com.github.max_co.chime.entity.Artist;
import com.github.max_co.chime.entity.Song;

class SongMapperTest {

  private SongMapper mapper;

  @BeforeEach
  void setup() {
    ArtistMapper artistMapper = new ArtistMapper();
    mapper = new SongMapper(artistMapper);
  }

  @ParameterizedTest
  @CsvSource({
      "0, Title, EN, Text, Translation, false",
      "1, Amazing Song, LA, Hello, Hi, true",
      "8, Ballad, FR, La la la, null, true",
      "88, S0NG, ES, Sing a song, null, false",
  })
  void testToDto(int id, String title, String language, String text, String translation,
      boolean hasArtist) {
    boolean isTranslationNull = false;
    if (translation.equals("null")) {
      translation = null;
      isTranslationNull = true;
    }
    Artist artist = null;
    if (hasArtist) {
      artist = new Artist();
      artist.setName(title);
      artist.setId(id);
    }
    Song song = new Song();
    song.setId(id);
    song.setTitle(title);
    song.setLanguage(language);
    song.setText(text);
    song.setTranslation(translation);
    song.setArtist(artist);
    SongDto dto = mapper.toDto(song);

    assertEquals(id, dto.getId());
    assertEquals(title, dto.getTitle());
    assertEquals(language, dto.getLanguage());
    assertEquals(text, dto.getText());
    if (isTranslationNull) {
      assertNull(dto.getTranslation());
    } else {
      assertNotNull(dto.getTranslation());
      assertEquals(translation, dto.getTranslation());
    }
    assertNotSame(song, dto);
    if (hasArtist) {
      assertNotSame(artist, dto.getArtist());
      assertEquals(artist.getId(), dto.getArtist().getId());
      assertEquals(artist.getName(), dto.getArtist().getName());
    } else {
      assertNull(dto.getArtist());
    }
  }

  @ParameterizedTest
  @CsvSource({
      "10, Long_Title, EN, Text, Translation, false",
      "225, Song name, LA, Song, Song, true",
      "0, Rock, DE, Do re mi, null, true",
      "1333, Awesome song, ES, Violin flute, null, false",
  })
  void testToEntity(int id, String title, String language, String text, String translation,
      boolean hasArtist) {
    boolean isTranslationNull = false;
    if (translation.equals("null")) {
      translation = null;
      isTranslationNull = true;
    }
    ArtistDto artistDto = null;
    if (hasArtist) {
      artistDto = new ArtistDto();
      artistDto.setName(title);
      artistDto.setId(id);
    }
    SongDto dto = new SongDto();
    dto.setId(id);
    dto.setTitle(title);
    dto.setLanguage(language);
    dto.setText(text);
    dto.setTranslation(translation);
    dto.setArtist(artistDto);
    Song song = mapper.toEntity(dto);

    assertEquals(id, song.getId());
    assertEquals(title, song.getTitle());
    assertEquals(language, song.getLanguage());
    assertEquals(text, song.getText());
    if (isTranslationNull) {
      assertNull(song.getTranslation());
    } else {
      assertNotNull(song.getTranslation());
      assertEquals(translation, song.getTranslation());
    }
    assertNotSame(dto, song);
    if (hasArtist) {
      assertNotSame(artistDto, song.getArtist());
      assertEquals(artistDto.getId(), song.getArtist().getId());
      assertEquals(artistDto.getName(), song.getArtist().getName());
    } else {
      assertNull(song.getArtist());
    }
  }
}
