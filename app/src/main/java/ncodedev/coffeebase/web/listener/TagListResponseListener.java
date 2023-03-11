package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Tag;

import java.util.List;

public interface TagListResponseListener {
    void handleGetList(List<Tag> tags);
    void handleSearchResult(List<Tag> tags);
}
