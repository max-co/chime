// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.max_co.chime.dao.SongRepository;
import com.github.max_co.chime.dto.SongDto;
import com.github.max_co.chime.entity.Song;

@Service
public class DbSongService implements SongService {

  private SongRepository songRepository;
  private SongMapper songMapper;

  @Autowired
  public DbSongService(SongRepository songRepository, SongMapper songMapper) {
    this.songRepository = songRepository;
    this.songMapper = songMapper;
  }

  @Override
  public List<SongDto> findAll() {
    List<Song> songs = songRepository.findAll();
    return songs.stream().map(song -> songMapper.toDto(song)).collect(Collectors.toList());
  }

  @Override
  public SongDto findById(int id) {
    Optional<Song> result = songRepository.findById(id);
    Song song = null;
    if (result.isPresent()) {
      song = result.get();
    } else {
      throw new RuntimeException("Could not find song");
    }
    return songMapper.toDto(song);
  }

  @Override
  public SongDto save(SongDto songDto) {
    Song song = songMapper.toEntity(songDto);
    song = songRepository.save(song);
    return songMapper.toDto(song);
  }

  @Override
  public void deleteById(int id) {
    songRepository.deleteById(id);
  }
}
