package com.beertag.repositories.base;

import com.beertag.models.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> getAll();
    Tag getTagById(int id);
    Tag getByName(String name);
}
