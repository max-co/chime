// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import java.util.List;

import com.github.max_co.chime.dto.SongDto;

public interface SongService {

  /**
   * Retrieves all saved songs.
   *
   * @return
   */
  List<SongDto> findAll();

  /**
   * Retrieves song with ID == id
   *
   * @param id
   * @return
   */
  SongDto findById(int id);

  /**
   * Persists song.
   *
   * @param song
   * @return
   */
  SongDto save(SongDto song);

  /**
   * Deletes song with ID == id.
   *
   * @param id
   */
  void deleteById(int id);
}
