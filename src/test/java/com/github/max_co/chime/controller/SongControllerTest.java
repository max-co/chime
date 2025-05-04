// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SongControllerTest {

  @Autowired
  private JdbcTemplate jdbc;

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
    ModelAndViewAssert.assertViewName(mav, "song/create-form");
  }
}
