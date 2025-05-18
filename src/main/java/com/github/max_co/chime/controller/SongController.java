// Copyright 2025 Massimo Comuzzo
// SPDX-License-Identifier: Apache-2.0

package com.github.max_co.chime.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.max_co.chime.dto.SongDto;
import com.github.max_co.chime.service.SongService;

@Controller
@RequestMapping("song")
public class SongController {

  private SongService songService;

  @Autowired
  public SongController(SongService songService) {
    this.songService = songService;
  }

  /**
   * Shows page with song list.
   *
   * @param model
   * @return
   */
  @GetMapping("")
  public String showList(Model model) {
    List<SongDto> songs = songService.findAll();
    model.addAttribute("songs", songs);

    return "song/list";
  }

  @GetMapping("/create-form")
  public String showCreateForm(Model model) {
    SongDto song = new SongDto();
    model.addAttribute("song", song);
    return "song/save-form";
  }

  @PostMapping("/save")
  public String save(@ModelAttribute("song") SongDto song, Model model) {
    SongDto saved = songService.save(song);
    return "redirect:/song/update-form/" + saved.getId();
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable int id, Model model) {
    songService.deleteById(id);
    return "redirect:/song";
  }

  @GetMapping("/update-form/{id}")
  public String showCreateForm(@PathVariable int id, Model model) {
    SongDto song = songService.findById(id);
    model.addAttribute("song", song);
    return "song/save-form";
  }

  @GetMapping("/{id}")
  public String showSong(@PathVariable int id, Model model) {
    SongDto song = songService.findById(id);
    model.addAttribute("song", song);
    model.addAttribute("textLines", splitLines(song.getText()));
    model.addAttribute("translationLines", splitLines(song.getTranslation()));
    return "song/view";
  }

  /**
   * Splits text in list of lines.
   *
   * @param text can be null
   * @return
   */
  private List<String> splitLines(String text) {
    if (text == null) {
      return Collections.emptyList();
    }
    return text.lines().toList();
  }
}
