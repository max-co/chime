// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.max_co.chime.dto.ArtistDto;
import com.github.max_co.chime.dto.SongDto;
import com.github.max_co.chime.entity.Artist;
import com.github.max_co.chime.entity.Song;

@Component
public class SongMapper {

  private ArtistMapper artistMapper;

  @Autowired
  public SongMapper(ArtistMapper artistMapper) {
    this.artistMapper = artistMapper;
  }

  /**
   * Maps Song entity to corresponding SongDto.
   *
   * @param song
   * @return SongDto
   */
  public SongDto toDto(Song song) {
    SongDto dto = new SongDto();
    dto.setId(song.getId());
    dto.setTitle(song.getTitle());
    dto.setLanguage(song.getLanguage());
    dto.setText(song.getText());
    dto.setTranslation(song.getTranslation());
    Artist artist = song.getArtist();
    if (artist != null) {
      ArtistDto artistDto = artistMapper.toDto(artist);
      dto.setArtist(artistDto);
    }
    return dto;
  }

  /**
   * Maps SongDto to corresponding Song entity.
   *
   * @param dto
   * @return Song
   */
  public Song toEntity(SongDto dto) {
    Song song = new Song();
    song.setId(dto.getId());
    song.setTitle(dto.getTitle());
    song.setLanguage(dto.getLanguage());
    song.setText(dto.getText());
    song.setTranslation(dto.getTranslation());
    ArtistDto artistDto = dto.getArtist();
    if (artistDto != null) {
      Artist artist = artistMapper.toEntity(artistDto);
      song.setArtist(artist);
    }
    return song;
  }
}
