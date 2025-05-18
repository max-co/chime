// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import com.github.max_co.chime.dto.SongDto;
import com.github.max_co.chime.service.SongService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class SongControllerTest {

  @Autowired
  private JdbcTemplate jdbc;

  @Autowired
  private SongService songService;

  @Autowired
  private MockMvc mockMvc;

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
  void testShowList() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/song"))
        .andExpect(status().isOk()).andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "song/list");
  }

  @Test
  void testShowCreateForm() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/song/create-form"))
        .andExpect(status().isOk()).andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "song/save-form");
  }

  @Test
  void testSave() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/song/save")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("title", "T1TL3")
        .param("language", "EN")
        .param("text", "T3XT")
        .param("translation", "TR4NSL4T10N")).andExpect(status().isFound()).andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "redirect:/song/update-form/3");

    List<SongDto> songs = songService.findAll();
    assertEquals(3, songs.size());
    SongDto song = songs.stream().filter((s) -> "T1TL3".equals(s.getTitle())).findAny().orElse(null);
    assertNotNull(song);
  }

  @Test
  void testDelete() throws Exception {
    assertDoesNotThrow(() -> songService.findById(1));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/song/delete/{id}", 1))
        .andExpect(status().isFound())
        .andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "redirect:/song");
    assertThrows(Exception.class, () -> songService.findById(1));
  }

  @Test
  void testShowUpdateForm() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/song/update-form/{id}", 1))
        .andExpect(status().isOk()).andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "song/save-form");
    SongDto song = (SongDto) mav.getModel().get("song");
    assertEquals(1, song.getId());
  }

  @Test
  void testUpdate() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/song/save")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("artist.id", "1")
        .param("artist.name", "Marcus+Tullius+Cicero")
        .param("id", "1")
        .param("title", "T1TL3")
        .param("language", "EN")
        .param("text", "T3XT")
        .param("translation", "TR4NSL4T10N")).andExpect(status().isFound()).andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "redirect:/song/update-form/1");

    List<SongDto> songs = songService.findAll();
    assertEquals(2, songs.size());
    SongDto song = songs.stream().filter((s) -> "T1TL3".equals(s.getTitle())).findAny().orElse(null);
    assertNotNull(song);
    assertEquals(1, song.getId());
    assertNotNull(song.getArtist());
    assertEquals("Marcus+Tullius+Cicero", song.getArtist().getName());
    assertEquals(1, song.getArtist().getId());
  }

  @Test
  void testShowSongView() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/song/{id}", 2))
        .andExpect(status().isOk()).andReturn();
    ModelAndView mav = mvcResult.getModelAndView();
    ModelAndViewAssert.assertViewName(mav, "song/view");
    SongDto song = (SongDto) mav.getModel().get("song");
    assertEquals(2, song.getId());
  }
}
