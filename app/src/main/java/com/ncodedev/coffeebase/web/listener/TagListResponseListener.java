package com.ncodedev.coffeebase.web.listener;

import com.ncodedev.coffeebase.model.domain.Tag;

import java.util.List;

public interface TagListResponseListener {
    void handleGetList(List<Tag> tags);
    void handleSearchResult(List<Tag> tags);
}
