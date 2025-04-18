// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.max_co.chime.entity.Song;

public interface SongRepository extends JpaRepository<Song, Integer> {
}
