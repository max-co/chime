// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "artist")
public class Artist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @OneToMany(mappedBy = "artist")
  private List<Song> songs;

  public Artist() {
  }

  public Artist(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }
}
