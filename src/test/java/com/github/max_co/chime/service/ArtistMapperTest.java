// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.max_co.chime.dto.ArtistDto;
import com.github.max_co.chime.entity.Artist;
import com.github.max_co.chime.entity.Song;

class ArtistMapperTest {

  private ArtistMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new ArtistMapper();
  }

  @ParameterizedTest
  @CsvSource({
      "Name, 42, 0",
      "null, 1, 2",
      "Firstname Lastname, 1000, 1",
      "Amazing Band Name, 1517, 5"
  })
  void testToDto(String name, int id, int songNum) {
    boolean isNameNull = false;
    if (name.equals("null")) {
      name = null;
      isNameNull = true;
    }
    List<Song> songs = new ArrayList<>();
    for (int i = 0; i < songNum; i++) {
      songs.add(new Song());
    }
    Artist artist = new Artist();
    artist.setId(id);
    artist.setName(name);
    artist.setSongs(songs);
    ArtistDto dto = mapper.toDto(artist);

    assertEquals(id, dto.getId());
    if (isNameNull) {
      assertNull(dto.getName());
    } else {
      assertNotNull(dto.getName());
      assertEquals(name, dto.getName());
    }
    assertNotSame(artist, dto);
  }

  @ParameterizedTest
  @CsvSource({
      "N4M3, 19",
      "null, 1",
      "Fistname Capital_Letter, 0",
      "The Band, 7777"
  })
  void testToEntity(String name, int id) {
    boolean isNameNull = false;
    if (name.equals("null")) {
      name = null;
      isNameNull = true;
    }
    ArtistDto dto = new ArtistDto();
    dto.setId(id);
    dto.setName(name);
    Artist artist = mapper.toEntity(dto);

    assertEquals(id, dto.getId());
    if (isNameNull) {
      assertNull(artist.getName());
    } else {
      assertNotNull(artist.getName());
      assertEquals(name, artist.getName());
    }
    assertNotSame(dto, artist);
    List<Song> songs = artist.getSongs();
    assertTrue(songs == null || songs.isEmpty());
  }
}
