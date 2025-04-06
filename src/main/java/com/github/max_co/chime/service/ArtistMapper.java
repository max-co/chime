// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import org.springframework.stereotype.Component;

import com.github.max_co.chime.dto.ArtistDto;
import com.github.max_co.chime.entity.Artist;

@Component
public class ArtistMapper {

  /**
   * Maps Artist entity to corresponding ArtistDto.
   *
   * @param artist
   * @return ArtistDto without songs
   */
  public ArtistDto toDto(Artist artist) {
    ArtistDto dto = new ArtistDto();
    dto.setId(artist.getId());
    dto.setName(artist.getName());
    return dto;
  }

  /**
   * Maps ArtistDto to corresponding Artist entity.
   *
   * @param dto
   * @return Artist without songs
   */
  public Artist toEntity(ArtistDto dto) {
    Artist artist = new Artist();
    artist.setId(dto.getId());
    artist.setName(dto.getName());
    return artist;
  }
}
