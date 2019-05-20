package com.stirlinglms.stirling.service.impl;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.entity.announcement.Announcement;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.repository.AnnouncementRepository;
import com.stirlinglms.stirling.service.AnnouncementService;
import com.stirlinglms.stirling.util.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final String[] VALID_FIELDS = new String[] {
      "title",
      "content",
      "attachments",
      "audiences",
      "tags"
    };

    private final AnnouncementRepository repository;

    @Autowired
    AnnouncementServiceImpl(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Announcement createAnnouncement(String title, User poster, String content) {
        Announcement announcement = new Announcement(title, poster, content, null, null, null);

        this.repository.save(announcement);

        return announcement;
    }

    @Override
    public Announcement createAnnouncement(String title, User poster, String content, Set<Resource> attachments, Set<UserType> audiences, Set<String> tags) {
        Announcement announcement = new Announcement(title, poster, content, attachments, audiences, tags);

        this.repository.save(announcement);

        return announcement;
    }

    @Override
    public Set<Announcement> getByTitle(String title) {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAllByTitle(title)));
    }

    @Override
    public Set<Announcement> getByPoster(User poster) {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAllByPoster(poster)));
    }

    @Override
    public Set<Announcement> getByAudience(UserType... types) {
        return this.getAll().stream()
          .filter(a -> a.getAudiences().containsAll(Arrays.asList(types)))
          .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Announcement> getByAudience(Set<UserType> types) {
        return this.getAll().stream()
          .filter(a -> a.getAudiences().containsAll(types))
          .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Announcement> getByAudience(List<UserType> types) {
        return this.getAll().stream()
          .filter(a -> a.getAudiences().containsAll(types))
          .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Announcement> getByTags(String... tags) {
        return this.getAll().stream()
          .filter(a -> a.getTags().containsAll(Arrays.asList(tags)))
          .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Announcement> getByTags(Set<String> tags) {
        return this.getAll().stream()
          .filter(a -> a.getTags().containsAll(tags))
          .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Announcement> getByTags(List<String> tags) {
        return this.getAll().stream()
          .filter(a -> a.getTags().containsAll(tags))
          .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Announcement update(Announcement entity, UpdateLevel level) throws Exception {
        return this.repository.performTransaction((repo) -> repo.save(entity));
    }

    @Override
    public Announcement delete(Announcement announcement) {
        this.repository.delete(announcement);

        return announcement;
    }

    @Override
    public Announcement getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public Set<Announcement> getAll() {
        return Collections.unmodifiableSet(Sets.newConcurrentHashSet(this.repository.findAll()));
    }

    @Override
    public AnnouncementRepository getRepository() {
        return this.repository;
    }
}
