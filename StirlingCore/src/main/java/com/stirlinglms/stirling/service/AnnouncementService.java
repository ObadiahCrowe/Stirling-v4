package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.announcement.Announcement;
import com.stirlinglms.stirling.entity.res.Resource;
import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import com.stirlinglms.stirling.repository.AnnouncementRepository;

import java.util.List;
import java.util.Set;

public interface AnnouncementService extends RepositoryService<Announcement, AnnouncementRepository> {

    Announcement createAnnouncement(String title, User poster, String content);

    Announcement createAnnouncement(String tittle, User poster, String content, Set<Resource> attachments,
                                    Set<UserType> audience, Set<String> tags);

    Set<Announcement> getByTitle(String title);

    Set<Announcement> getByPoster(User poster);

    Set<Announcement> getByAudience(UserType... types);

    Set<Announcement> getByAudience(Set<UserType> types);

    Set<Announcement> getByAudience(List<UserType> types);

    Set<Announcement> getByTags(String... tags);

    Set<Announcement> getByTags(Set<String> tags);

    Set<Announcement> getByTags(List<String> tags);
}
