// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.dto;

public class SongDto {

  private int id;

  private String title;

  private String language;

  private String text;

  private String translation;

  private ArtistDto artist;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTranslation() {
    return translation;
  }

  public void setTranslation(String translation) {
    this.translation = translation;
  }

  public ArtistDto getArtist() {
    return artist;
  }

  public void setArtist(ArtistDto artist) {
    this.artist = artist;
  }
}
